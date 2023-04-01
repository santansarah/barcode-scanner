package com.santansarah.barcodescanner.data.remote

data class IngredientX(
    val id: String,
    val percent_estimate: Double,
    val percent_max: Double,
    val percent_min: Int,
    val text: String
)