package com.santansarah.barcodescanner.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Nutriments(
    val carbohydrates: Int,
    @SerialName("energy-kcal_serving") val calories: Int,
    @SerialName("fat_serving") val fat: Double,
    @SerialName("saturated-fat_value") val saturatedFat: Double,
    @SerialName("proteins_serving") val protein: Double,
    @SerialName("fiber_serving") val fiber: Double? = null,
    @SerialName("sodium_serving") val sodium: Double,
    @SerialName("sugars_serving") val sugar: Int,
    @SerialName("potassium_serving") val potassium: Double
)
