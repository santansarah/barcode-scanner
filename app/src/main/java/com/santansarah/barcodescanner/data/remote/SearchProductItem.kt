package com.santansarah.barcodescanner.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchProductItem(
    @SerialName("brand_owner") val brandOwner: String? = null,
    @SerialName("image_front_small_url") val imageUrl: String? = null,
    @SerialName("product_name") val productName: String? = null
) {
    companion object {
        val fields = listOf("brand_owner",
            "image_front_small_url",
            "product_name")
    }
}