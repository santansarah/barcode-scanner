package com.santansarah.barcodescanner.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchResults(
    val count: Int? = 0,
    val page: Int,
    @SerialName("page_count") val pageCount: Int? = 0,
    @SerialName("page_size") val pageSize: Int,
    val products: List<SearchProductItem>,
    val skip: Int
)