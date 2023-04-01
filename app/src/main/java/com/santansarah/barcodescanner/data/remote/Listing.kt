package com.santansarah.barcodescanner.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Listing(
    val code: String,
    val product: Product,
    val status: Int,
    @SerialName("status_verbose") val statusVerbose: String
)