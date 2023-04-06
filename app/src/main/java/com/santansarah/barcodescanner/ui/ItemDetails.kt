package com.santansarah.barcodescanner.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.santansarah.barcodescanner.data.remote.ItemListing
import com.santansarah.barcodescanner.data.remote.mock.yogurt
import com.santansarah.barcodescanner.ui.components.ItemImageSlider
import com.santansarah.barcodescanner.ui.theme.BarcodeScannerTheme
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.text.DateFormat

@Composable
fun ItemDetails(
    viewModel: ProductDetailViewModel = hiltViewModel()
) {

    val itemListing = viewModel.itemListing.collectAsStateWithLifecycle().value

    if (itemListing != null) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = itemListing.product.productName,
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(20.dp))

            ItemImageSlider(
                imgFront = itemListing.product.imgFrontUrl,
                imgBack = itemListing.product.imgNutritionUrl
            )

            Spacer(modifier = Modifier.height(20.dp))

            Ingredients(product = itemListing.product)

            Spacer(modifier = Modifier.height(20.dp))

            NutritionData(
                nutriments = itemListing.product.nutriments,
                servingSize = itemListing.product.servingSize
            )

        }
    }

}


@Preview
@Composable
fun PreviewItemDetails() {

    val item = Json {
        ignoreUnknownKeys = true
    }.decodeFromString<ItemListing>(yogurt)

    BarcodeScannerTheme {
        ItemDetails()
    }

}