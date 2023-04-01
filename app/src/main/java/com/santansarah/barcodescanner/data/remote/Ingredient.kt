package com.santansarah.barcodescanner.data.remote

import kotlinx.serialization.Serializable

@Serializable
data class Ingredient(
    val id: String,
    val text: String,
)