package com.santansarah.barcodescanner.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchResults(
    val count: Int? = 0,  // total number of products
    val page: Int,        // current page that we've requested
    @SerialName("page_count") val pageCount: Int? = 0,  // total # of products on this page
    @SerialName("page_size") val pageSize: Int,         // total # of products per page
    val products: List<SearchProductItem>,              // list of products
    val skip: Int
)