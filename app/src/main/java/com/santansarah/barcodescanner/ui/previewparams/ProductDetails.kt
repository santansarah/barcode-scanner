package com.santansarah.barcodescanner.ui.previewparams

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.santansarah.barcodescanner.data.remote.ItemListing
import com.santansarah.barcodescanner.data.remote.Product
import com.santansarah.barcodescanner.data.remote.mock.bakersChocolate
import com.santansarah.barcodescanner.domain.ErrorCode
import com.santansarah.barcodescanner.domain.models.AppDestinations.HOME
import com.santansarah.barcodescanner.domain.models.AppDestinations.SEARCH
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

data class ProductDetails(
    val product: Product?,
    val isLoading: Boolean,
    val fromScreen: String,
    val errorMessage: String?,
    val barcode: String
)

val item = Json {
    ignoreUnknownKeys = true
    isLenient = true
    encodeDefaults = true
    explicitNulls = false
}.decodeFromString<ItemListing>(bakersChocolate)

class ProductDetailParams : PreviewParameterProvider<ProductDetails> {

    override val values = sequenceOf(
        // successful states
        ProductDetails(null, true, HOME, null, "0078742081304"),
        ProductDetails(item.product, false, HOME, null, "0078742081304"),

        // error states
        ProductDetails(null, false, SEARCH, ErrorCode.API_PRODUCT_TIMEOUT.message, "0078742081304"),
        ProductDetails(null, false, SEARCH, ErrorCode.NETWORK_PRODUCT_TIMEOUT.message, "0078742081304"),
        ProductDetails(null, false, HOME, ErrorCode.NOT_FOUND.message, "0078742081304"),
        ProductDetails(null, false, HOME, ErrorCode.NETWORK_PRODUCT_TIMEOUT.message, "0078742081304"),
    )
}