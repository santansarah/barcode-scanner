package com.santansarah.barcodescanner

import com.google.firebase.ml.modeldownloader.CustomModelDownloadConditions
import com.google.firebase.ml.modeldownloader.DownloadType
import com.google.firebase.ml.modeldownloader.FirebaseModelDownloader
import com.santansarah.barcodescanner.data.remote.FoodRepository
import com.santansarah.barcodescanner.data.remote.SimilarItemListing
import com.santansarah.barcodescanner.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.withContext
import org.tensorflow.lite.Interpreter
import timber.log.Timber


/** Interface to load TfLite model and provide recommendations.  */
class RecommendationService(
    private val foodRepository: FoodRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) {
    private var tflite: Interpreter? = null
    val similarItemsList = MutableStateFlow<List<SimilarItemListing>>(emptyList())

    /** Free up resources as the client is no longer needed.  */
    fun unload() {
        tflite?.close()
    }

    suspend fun recommend(currentProductCode: String, fat: Double, carbs: Double) {

        val inputs = Array(1) {arrayOf(getDietType(fat, carbs))}
        Timber.d("diet type: ${inputs[0][0]}")

        // The key here is to create an Array<of type> vs a single FloatArray(10). My output is
        // (1,10), and the Tensorflow API sees the difference. We need to have one row of 10,
        // vs a single array of 10. So when we access the output, we'll call barcodes[0][i]
        // instead of barcodes[0].
        val score = Array(1) { FloatArray(10) }
        val barcodes = Array(1) { arrayOfNulls<String>(10) }
        val outputMap: MutableMap<Int, Any> = HashMap()

        outputMap[0] = score
        outputMap[1] = barcodes

        tflite?.let { interpreter ->
            interpreter.runForMultipleInputsOutputs(inputs, outputMap)

            Timber.d("got outputs from model...")

            val barcodesListAsString =
                barcodes[0].toList().filter { it != currentProductCode }

            // https://github.com/tensorflow/tensorflow/issues/25170
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

            FirebaseModelDownloader.getInstance()
                .getModel("Product-Recs", DownloadType.LATEST_MODEL, conditions)
                .addOnCompleteListener {
                    if (!it.isSuccessful) {
                        Timber.d("Failed to get model file.")
                    } else {
                        Timber.d("model file set...")
                        tflite = Interpreter(it.result.file!!)
                    }
                }
                .addOnFailureListener {
                    Timber.d("Failed to get model file.")
                }
        }
    }

    private fun getDietType(fat: Double, carbs: Double): String {

        return when {
            carbs >= 30 -> "high_carb"
            fat >= 5 && carbs <= 12 -> "keto"
            fat <= 1 && carbs <= 1 -> "free_food"
            fat <= 3 -> "low_fat"
            else -> "standard"
        }

    }

}