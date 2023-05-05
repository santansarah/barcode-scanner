package com.santansarah.barcodescanner.data.remote

import com.santansarah.barcodescanner.data.paging.ProductSearchPagingSource
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchProductItem(
    val code: String,
    @SerialName("brands") val brands: String? = null,
    @SerialName("image_front_small_url") val imageUrl: String? = null,
    @SerialName("product_name") val productName: String? = null
) {

    /**
     * Here, I just have a list of fields that I want. That way,
     * when I call the Api, I just join them to a string, with
     * comma separated values.
     */
    companion object {
        val fields = listOf(
            "code",
            "brands",
            "image_front_small_url",
            "product_name")
    }
}