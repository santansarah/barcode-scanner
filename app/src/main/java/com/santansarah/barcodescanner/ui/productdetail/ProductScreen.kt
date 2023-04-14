package com.santansarah.barcodescanner.ui.productdetail

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.with
import androidx.compose.foundation.background
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
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.santansarah.barcodescanner.data.remote.ItemListing
import com.santansarah.barcodescanner.data.remote.Product
import com.santansarah.barcodescanner.data.remote.mock.cashews
import com.santansarah.barcodescanner.data.remote.mock.notfound
import com.santansarah.barcodescanner.ui.components.MainAppBar
import com.santansarah.barcodescanner.ui.theme.BarcodeScannerTheme
import com.santansarah.barcodescanner.ui.theme.brightYellow
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import timber.log.Timber

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
        Text(
            text = "Product Not Found",
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Make sure you scan the barcode in a well lit room, and that the " +
                    "barcode surface is not wrinkled. If the " +
                    "barcode doesn't seem to match, try again."
        )
    }

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

        AnimatedContent(
            targetState = isLoading,
            label = "",
            transitionSpec = {
                fadeIn() with fadeOut()
            }
        ) { isLoading->
            if (isLoading)
                ProductLoading(padding)
            else
                product?.let {

                    val productText = listOfNotNull(
                        it.brandOwner,
                        it.productName
                    ).joinToString(" ")

                    Column(
                        modifier = Modifier
                            .padding(padding)
                            .verticalScroll(rememberScrollState()),
                    ) {

                        ElevatedCard(
                            shape = RectangleShape
                        ) {

                            Column(
                                modifier = Modifier
                                    .background(brightYellow)
                                    .fillMaxWidth()
                            ) {
                                Text(
                                    modifier = Modifier
                                        .padding(6.dp),
                                    text = productText,
                                    style = MaterialTheme.typography.titleLarge
                                )
                            }
                            Divider(thickness = 2.dp, color = Color.DarkGray)

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                ItemImageSlider(
                                    imgFront = it.imgFrontUrl,
                                    imgBack = it.imgNutritionUrl
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        Divider(thickness = 2.dp, color = Color.DarkGray)
                        ElevatedCard(
                            shape = RectangleShape
                        ) {

                            Column(
                                modifier = Modifier
                                    .background(brightYellow)
                                    .fillMaxWidth()
                            ) {
                                Text(
                                    modifier = Modifier
                                        .padding(6.dp),
                                    text = "Ingredients",
                                    style = MaterialTheme.typography.titleLarge
                                )
                            }
                            Divider(thickness = 2.dp, color = Color.DarkGray)

                            Ingredients(product = it)

                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        Divider(thickness = 2.dp, color = Color.DarkGray)
                        ElevatedCard(
                            shape = RectangleShape
                        ) {

                            Column(
                                modifier = Modifier
                                    .background(brightYellow)
                                    .fillMaxWidth()
                            ) {
                                Text(
                                    modifier = Modifier
                                        .padding(6.dp),
                                    text = "Nutrition Data",
                                    style = MaterialTheme.typography.titleLarge
                                )
                            }
                            Divider(thickness = 2.dp, color = Color.DarkGray)
                            Text(
                                modifier = Modifier
                                    .padding(6.dp),
                                text = "Serving Size: " + (it.servingSize ?: "Unknown"),
                                style = MaterialTheme.typography.bodyMedium
                            )

                            NutritionData(
                                nutriments = it.nutriments,
                                servingSize = it.servingSize ?: "Unknown"
                            )

                        }
                        Divider(thickness = 2.dp, color = Color.DarkGray)

                    }
                } ?: ProductNotFound(paddingValues = padding)
        }

    }
}


@Preview
@Composable
fun PreviewItemDetails() {

    val item = Json {
        ignoreUnknownKeys = true
    }.decodeFromString<ItemListing>(cashews)
    val placeHolderImage = item.copy(
        product =
        item.product!!.copy(imgFrontUrl = null, imgNutritionUrl = null)
    )

    BarcodeScannerTheme {
        ItemDetails(
            isLoading = true,
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