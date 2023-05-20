package com.santansarah.barcodescanner.ui.productdetail

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.mlkit.common.MlKitException.ErrorCode
import com.santansarah.barcodescanner.data.remote.ItemListing
import com.santansarah.barcodescanner.data.remote.Product
import com.santansarah.barcodescanner.data.remote.SimilarItemListing
import com.santansarah.barcodescanner.data.remote.mock.bakersChocolate
import com.santansarah.barcodescanner.data.remote.mock.notfound
import com.santansarah.barcodescanner.data.remote.mock.similarProducts
import com.santansarah.barcodescanner.domain.models.AppDestinations.HOME
import com.santansarah.barcodescanner.domain.models.AppDestinations.SEARCH
import com.santansarah.barcodescanner.ui.components.MainAppBar
import com.santansarah.barcodescanner.ui.productdetail.sections.NutritionData
import com.santansarah.barcodescanner.ui.productdetail.sections.ProductImage
import com.santansarah.barcodescanner.ui.productdetail.sections.ProductIngredients
import com.santansarah.barcodescanner.ui.productdetail.sections.SimilarProducts
import com.santansarah.barcodescanner.ui.theme.BarcodeScannerTheme
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import timber.log.Timber

@Composable
fun ProductDetailsRoute(
    viewModel: ProductDetailViewModel = hiltViewModel(),
    onBackClicked: () -> Unit,
    onGotBarcode: (String) -> Unit
) {

    val productDetailState = viewModel.productDetailState.collectAsStateWithLifecycle().value
    val similarItems = viewModel.similiarItems.collectAsStateWithLifecycle().value
    /**
     * Since I use flattenMerge and combine in my ViewModel, here, I can always be sure that
     * my barcode is current, and that the product that's loaded in itemListing matches the
     * barcode that came back from the flow.
     */
    val barcode = productDetailState.barcode
    val itemListing = productDetailState.itemListing
    val isLoading = productDetailState.isLoading
    val productError = productDetailState.userMessage

    ItemDetails(
        isLoading = isLoading,
        product = itemListing?.product,
        onBackClicked = onBackClicked,
        code = barcode,
        productError = productError,
        fromScreen = viewModel.fromScreen,
        onRetry = viewModel::getProductDetail,
        onRescan = viewModel::scanBarcode,
        similarItemListing = similarItems,
        onGotBarcode = onGotBarcode
    )

}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun ItemDetails(
    isLoading: Boolean,
    code: String,
    product: Product?,
    onBackClicked: () -> Unit,
    productError: String?,
    fromScreen: String,
    onRetry: (String) -> Unit,
    onRescan: () -> Unit,
    similarItemListing: List<SimilarItemListing>,
    onGotBarcode: (String) -> Unit
) {

    Scaffold(
        topBar = {
            MainAppBar(
                onBackClicked = onBackClicked,
                title = code
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .verticalScroll(rememberScrollState()),
        ) {

            /**
             * If the product error message isn't null, it's passed to my Product Image
             * composable.
             */
            ProductImage(product, isLoading, productError, fromScreen, code, onRetry, onRescan)

            Spacer(modifier = Modifier.height(20.dp))

            ProductIngredients(product, isLoading)

            Spacer(modifier = Modifier.height(20.dp))

            NutritionData(product, isLoading)

            Spacer(modifier = Modifier.height(20.dp))

            if (similarItemListing.isNotEmpty()) {
                SimilarProducts(similarItemListing, onGotBarcode)
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}


@Preview
@Composable
fun PreviewItemDetails() {

    val item = Json {
        ignoreUnknownKeys = true
        isLenient = true
        encodeDefaults = true
        explicitNulls = false
    }.decodeFromString<ItemListing>(bakersChocolate)
    val placeHolderImage = item.copy(
        product =
        item.product!!.copy(imgFrontUrl = null, imgNutritionUrl = null)
    )

    BarcodeScannerTheme {
        ItemDetails(
            isLoading = false,
            product = placeHolderImage.product,
            onBackClicked = {}, code = item.code, productError = null, fromScreen = HOME,
            onRetry = {}, onRescan = {},
            similarItemListing = similarProducts,
            onGotBarcode = {}
        )
    }

}

@Preview
@Composable
fun PreviewNotFound() {

    val item = Json {
        ignoreUnknownKeys = true
    }.decodeFromString<ItemListing>(notfound)


    BarcodeScannerTheme {
        ItemDetails(
            isLoading = false,
            product = item.product,
            onBackClicked = {}, code = item.code, productError = null,
            fromScreen = SEARCH, onRetry = {}, onRescan = {},
            similarItemListing = similarProducts,
            onGotBarcode = {}
        )
    }

}

@Preview
@Composable
fun PreviewApiError() {

    val item = Json {
        ignoreUnknownKeys = true
    }.decodeFromString<ItemListing>(notfound)


    BarcodeScannerTheme {
        ItemDetails(
            isLoading = false,
            product = item.product,
            onBackClicked = {}, code = item.code,
            productError = com.santansarah.barcodescanner.domain.ErrorCode.API_PRODUCT_TIMEOUT.message,
            fromScreen = SEARCH, onRetry = {}, onRescan = {},
            similarItemListing = similarProducts,
            onGotBarcode = {}
        )
    }

}