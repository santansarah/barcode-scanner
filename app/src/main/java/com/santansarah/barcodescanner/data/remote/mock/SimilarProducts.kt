package com.santansarah.barcodescanner.data.remote.mock

import com.santansarah.barcodescanner.data.remote.SearchResults
import com.santansarah.barcodescanner.data.remote.SimilarItemListing
import com.santansarah.barcodescanner.data.remote.SimilarProduct
import com.santansarah.barcodescanner.ui.productdetail.sections.SimilarProducts
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

val builder = Json {
    ignoreUnknownKeys = true
}

val similarProducts = listOf<SimilarItemListing>(
    builder.decodeFromString(cornChips),
    builder.decodeFromString(yogurt),
    builder.decodeFromString(soda),
    builder.decodeFromString(cashews)
)
