package com.santansarah.barcodescanner

import com.google.firebase.ml.modeldownloader.CustomModelDownloadConditions
import com.google.firebase.ml.modeldownloader.DownloadType
import com.google.firebase.ml.modeldownloader.FirebaseModelDownloader
import com.santansarah.barcodescanner.data.remote.FoodRepository
import com.santansarah.barcodescanner.data.remote.SimilarItemListing
import com.santansarah.barcodescanner.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import org.tensorflow.lite.Interpreter
import timber.log.Timber


/** Interface to load TfLite model and provide recommendations.  */
class RecommendationService(
    private val foodRepository: FoodRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) {
    private var tflite: Interpreter? = null
    val similarItemsList =
        MutableStateFlow<List<SimilarItemListing>>(emptyList())

    /** Free up resources as the client is no longer needed.  */
    fun unload() {
        tflite?.close()
    }

    suspend fun recommend(currentProductCode: String, fat: Double, carbs: Double) {

        val dietType = longArrayOf(getDietType(fat, carbs))
        val fatValue = longArrayOf(fat.toLong())
        val carbsValue = longArrayOf(carbs.toLong())
        val barcode = arrayOf(currentProductCode)
        val inputs = arrayOf(dietType, fatValue, carbsValue, barcode)

        val score = Array(1) { FloatArray(10) }
        val barcodes = Array(1) { arrayOfNulls<String>(10) }
        val outputMap: MutableMap<Int, Any> = HashMap()

        outputMap[0] = score
        outputMap[1] = barcodes

        tflite?.let { interpreter ->
            interpreter.runForMultipleInputsOutputs(inputs, outputMap)

            Timber.d("got outputs from model...")

            val barcodesListAsString = barcodes[0]
                    .toList()
                    .filter { it != currentProductCode }
                    .take(5)

            Timber.d(barcodesListAsString.toString())

            foodRepository.getSimilarItems(barcodesListAsString).collect {
                similarItemsList.value = it
            }

        } ?: Timber.d("No model loaded...")
    }

    suspend fun downloadModel() {
        withContext(dispatcher) {
            val conditions = CustomModelDownloadConditions.Builder()
                .requireWifi()
                .build()

            try {
                val firebaseModelDownloader = FirebaseModelDownloader.getInstance()
                val model = firebaseModelDownloader
                    .getModel("Product-Recs", DownloadType.LATEST_MODEL, conditions).await()
                Timber.d("model file set...")
                tflite = Interpreter(model.file!!)
            } catch (e: Exception) {
                Timber.d("Failed to get model file.")
            }
        }
    }

    private fun getDietType(fat: Double, carbs: Double): Long {

        return when {
            carbs >= 30 -> 1
            fat >= 5 && carbs <= 12 -> 2
            fat <= 1 && carbs <= 1 -> 3
            fat <= 3 -> 4
            else -> 5
        }

        /*return when {
            carbs >= 30 -> "high_carb"
            fat >= 5 && carbs <= 12 -> "keto"
            fat <= 1 && carbs <= 1 -> "free_food"
            fat <= 3 -> "low_fat"
            else -> "standard"
        }*/

    }

}