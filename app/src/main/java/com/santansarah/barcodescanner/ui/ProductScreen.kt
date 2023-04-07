package com.santansarah.barcodescanner.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.santansarah.barcodescanner.data.remote.ItemListing
import com.santansarah.barcodescanner.data.remote.Product
import com.santansarah.barcodescanner.data.remote.mock.cashews
import com.santansarah.barcodescanner.data.remote.mock.notfound
import com.santansarah.barcodescanner.ui.components.BackIcon
import com.santansarah.barcodescanner.ui.components.ItemImageSlider
import com.santansarah.barcodescanner.ui.theme.BarcodeScannerTheme
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

@Composable
fun ProductDetailsRoute(
    viewModel: ProductDetailViewModel = hiltViewModel(),
    onBackClicked: () -> Unit
) {

    val itemListing = viewModel.itemListing.collectAsStateWithLifecycle().value

    if (itemListing != null) {
        ItemDetails(product = itemListing.product,
            onBackClicked = onBackClicked,
            code = itemListing.code
        )
    } else {
        ProductLoading()
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppBar(
    title: String,
    onBackClicked: () -> Unit
) {
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color(0xfff9004b)
        ),
        title = { Text(text = title) },
        navigationIcon = {
            IconButton(onClick = onBackClicked) {
                BackIcon(contentDesc = "Go Back")
            }
        }
    )
}

@Composable
fun ProductLoading() {

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator()
    }

}

@Composable
fun ProductNotFound(
    paddingValues: PaddingValues
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Product Not Found",
            style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(20.dp))
        Text(text = "Make sure you scan the barcode in a well lit room. If the " +
                "barcode doesn't seem to match, try again.")
    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemDetails(
    code: String,
    product: Product?,
    onBackClicked: () -> Unit
) {

    Scaffold(
        topBar = { MainAppBar(onBackClicked = onBackClicked,
            title = code) }
    ) { padding ->

        product?.let {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(padding)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                product.brandOwner?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.headlineSmall
                    )
                }
                Text(
                    text = product.productName,
                    style = MaterialTheme.typography.headlineSmall
                )

                Spacer(modifier = Modifier.height(20.dp))

                ItemImageSlider(
                    imgFront = product.imgFrontUrl,
                    imgBack = product.imgNutritionUrl
                )

                Spacer(modifier = Modifier.height(20.dp))

                Ingredients(product = product)

                Spacer(modifier = Modifier.height(20.dp))

                NutritionData(
                    nutriments = product.nutriments,
                    servingSize = product.servingSize ?: "Unknown"
                )

            }
        } ?: ProductNotFound(paddingValues = padding)
    }
}


@Preview
@Composable
fun PreviewItemDetails() {

    val item = Json {
        ignoreUnknownKeys = true
    }.decodeFromString<ItemListing>(cashews)
    val placeHolderImage = item.copy(product =
    item.product!!.copy(imgFrontUrl = null, imgNutritionUrl = null))

    BarcodeScannerTheme {
        ItemDetails(product = placeHolderImage.product,
            onBackClicked = {}, code = item.code)
    }

}

@Preview
@Composable
fun PreviewNotFound(){

    val item = Json {
        ignoreUnknownKeys = true
    }.decodeFromString<ItemListing>(notfound)


    BarcodeScannerTheme {
        ItemDetails(product = item.product,
            onBackClicked = {}, code = item.code)
    }

}