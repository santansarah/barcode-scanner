package com.santansarah.barcodescanner.ui.previewparams

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.santansarah.barcodescanner.data.remote.ItemListing
import com.santansarah.barcodescanner.data.remote.Product
import com.santansarah.barcodescanner.data.remote.mock.bakersChocolate
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

data class ProductDetails(
    val product: Product?,
    val isLoading: Boolean
)

val item = Json {
    ignoreUnknownKeys = true
    isLenient = true
    encodeDefaults = true
    explicitNulls = false
}.decodeFromString<ItemListing>(bakersChocolate)

class ProductDetailParams : PreviewParameterProvider<ProductDetails> {

    override val values = sequenceOf(
        ProductDetails(null, true),
        ProductDetails(item.product, false),
        ProductDetails(null, false),
    )
}