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
import com.santansarah.barcodescanner.data.remote.ItemListing
import com.santansarah.barcodescanner.data.remote.Product
import com.santansarah.barcodescanner.data.remote.mock.bakersChocolate
import com.santansarah.barcodescanner.data.remote.mock.notfound
import com.santansarah.barcodescanner.ui.components.MainAppBar
import com.santansarah.barcodescanner.ui.productdetail.sections.NutritionData
import com.santansarah.barcodescanner.ui.productdetail.sections.ProductImage
import com.santansarah.barcodescanner.ui.productdetail.sections.ProductIngredients
import com.santansarah.barcodescanner.ui.theme.BarcodeScannerTheme
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

@Composable
fun ProductDetailsRoute(
    viewModel: ProductDetailViewModel = hiltViewModel(),
    onBackClicked: () -> Unit
) {

    val barcode = viewModel.barcodeFromState
    val itemListing = viewModel.itemListing.collectAsStateWithLifecycle().value
    val isLoading = viewModel.isLoading.collectAsStateWithLifecycle().value

    ItemDetails(
        isLoading = isLoading,
        product = itemListing?.product,
        onBackClicked = onBackClicked,
        code = barcode
    )

}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun ItemDetails(
    isLoading: Boolean,
    code: String,
    product: Product?,
    onBackClicked: () -> Unit
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

            ProductImage(product, isLoading)

            Spacer(modifier = Modifier.height(20.dp))

            ProductIngredients(product, isLoading)

            Spacer(modifier = Modifier.height(20.dp))

            NutritionData(product, isLoading)
            Divider(thickness = 2.dp, color = Color.DarkGray)
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
            onBackClicked = {}, code = item.code
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
            onBackClicked = {}, code = item.code
        )
    }

}