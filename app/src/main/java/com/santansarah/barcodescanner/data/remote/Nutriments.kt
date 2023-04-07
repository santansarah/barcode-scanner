package com.santansarah.barcodescanner.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Nutriments(
    @SerialName("carbohydrates_serving") val carbohydrates: Double? = null,
    @SerialName("energy-kcal_serving") val calories: Double? = null,
    @SerialName("cholesterol_serving") val cholesterol: Double? = null,
    @SerialName("fat_serving") val fat: Double? = null,
    @SerialName("saturated-fat_serving") val saturatedFat: Double? = null,
    @SerialName("monounsaturated-fat_serving") val monounsaturatedFat: Double? = null,
    @SerialName("polyunsaturated-fat_serving") val polyunsaturatedFat: Double? = null,
    @SerialName("proteins_serving") val protein: Double? = null,
    @SerialName("fiber_serving") val fiber: Double? = null,
    @SerialName("sodium_serving") val sodium: Double? = null,
    @SerialName("sugars_serving") val sugar: Double? = null,
    @SerialName("potassium_serving") val potassium: Double? = null,
)
