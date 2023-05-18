package com.santansarah.barcodescanner

import com.google.firebase.ml.modeldownloader.CustomModelDownloadConditions
import com.google.firebase.ml.modeldownloader.DownloadType
import com.google.firebase.ml.modeldownloader.FirebaseModelDownloader
import com.santansarah.barcodescanner.data.remote.FoodRepository
import com.santansarah.barcodescanner.data.remote.ItemListing
import org.tensorflow.lite.Interpreter
import timber.log.Timber


/** Interface to load TfLite model and provide recommendations.  */
class RecommendationService(
    val foodRepository: FoodRepository
) {
    private val barcodes: MutableMap<Int, ItemListing> = HashMap()
    private var tflite: Interpreter? = null

    /** An immutable result returned by a RecommendationClient.  */
    data class Result(
        val barcode: String
    )

    /** Load recommendation candidate list.  */
    private suspend fun loadBarcodeList() {
        // TODO: Replace this function with code from the codelab to load a list of recommendation
        // candidates.
    }

    /** Free up resources as the client is no longer needed.  */
    fun unload() {
        tflite?.close()
        barcodes.clear()
    }


    fun recommend(currentProductCode: String, fat: Double, carbs: Double): List<Result> {

        val inputs = arrayOf<Any>(getDietType(fat, carbs))

        val recScore = Array(1) { FloatArray(10) }
        val barcodes = Array(1) { LongArray(10)}
        val outputMap: MutableMap<Int, Any> = HashMap()

        outputMap[0] = recScore
        outputMap[1] = barcodes

        tflite?.let {
            it.runForMultipleInputsOutputs(inputs, outputMap)

            val results = ArrayList<Result>()

            // Add recommendation results. Filter null or contained items.
            for (code in barcodes[0]) {
                if (currentProductCode != code.toString()) {
                    val result = Result(
                        code.toString()
                    )
                    results.add(result)
                }
            }
            Timber.d(results.toString())
            return results
        } ?: run {
            Timber.d("No tflite interpreter loaded")
            return emptyList()
        }
    }

    fun downloadModel() {
        val conditions = CustomModelDownloadConditions.Builder()
            .requireWifi()
            .build()

        FirebaseModelDownloader.getInstance()
            .getModel("Product-Recs", DownloadType.LOCAL_MODEL, conditions)
            .addOnCompleteListener {
                if (!it.isSuccessful) {
                    Timber.d("Failed to get model file.")
                } else {
                    tflite = Interpreter(it.result.file!!)
                    Timber.d("set tflite file...")
                    recommend("78742248271", 15.0, 2.0)
                }
            }
            .addOnFailureListener {
                Timber.d("Failed to get model file.")
            }
    }

    fun getDietType(fat: Double, carbs: Double): String {

        return when {
            carbs >= 30 -> "high_carb"
            fat >= 5 && carbs <= 12 -> "keto"
            fat <= 1 && carbs <= 1 -> "free_food"
            fat <= 3 -> "low_fat"
            else -> "standard"
        }

    }

}