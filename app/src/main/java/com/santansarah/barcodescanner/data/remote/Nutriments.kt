package com.santansarah.barcodescanner.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Nutriments(
    @SerialName("carbohydrates_serving") val carbohydrates: Double,
    @SerialName("energy-kcal_serving") val calories: Double,
    @SerialName("cholesterol_serving") val cholesterol: Double? = null,
    @SerialName("fat_serving") val fat: Double,
    @SerialName("saturated-fat_serving") val saturatedFat: Double? = null,
    @SerialName("monounsaturated-fat_serving") val monounsaturatedFat: Double? = null,
    @SerialName("polyunsaturated-fat_serving") val polyunsaturatedFat: Double? = null,
    @SerialName("proteins_serving") val protein: Double,
    @SerialName("fiber_serving") val fiber: Double? = null,
    @SerialName("sodium_serving") val sodium: Double,
    @SerialName("sugars_serving") val sugar: Int,
    @SerialName("potassium_serving") val potassium: Double? = null,
)
