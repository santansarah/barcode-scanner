package com.santansarah.barcodescanner.ui.productdetail.sections

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.with
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.santansarah.barcodescanner.data.remote.ItemListing
import com.santansarah.barcodescanner.data.remote.Product
import com.santansarah.barcodescanner.data.remote.mock.bakersChocolate
import com.santansarah.barcodescanner.ui.components.loadingBrush
import com.santansarah.barcodescanner.ui.productdetail.ItemImageSlider
import com.santansarah.barcodescanner.ui.theme.BarcodeScannerTheme
import com.santansarah.barcodescanner.ui.theme.brightYellow
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json


@OptIn(ExperimentalAnimationApi::class, ExperimentalTextApi::class)
@Composable
fun ProductImage(
    product: Product?,
    isLoading: Boolean
) {

    ElevatedCard(
        shape = RectangleShape
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AnimatedContent(
                targetState = isLoading,
                transitionSpec = productLoadingTransitionSpec(), label = ""
            ) { isLoading ->
                if (isLoading) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(6.dp),
                        text = "Loading".uppercase(),
                        style = TextStyle(
                            brush = loadingBrush(fontSize = 16.sp),
                            fontSize = 16.sp,
                        ),
                        textAlign = TextAlign.Center
                    )
                } else {

                    val productText = listOfNotNull(
                        product?.brands,
                        product?.productName
                    ).joinToString(" ")

                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(6.dp),
                        text = productText.ifEmpty { "Unknown Product" }.uppercase(),
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (!isLoading && product == null) {
                Text(
                    modifier = Modifier.padding(bottom = 16.dp),
                    text = "We couldn't find your product. If you scanned a barcode, make sure " +
                        "that it's not wrinkled and well lit. If the barcode listed up top " +
                        "doesn't seem to match, try again.")
            }

            ItemImageSlider(
                imgFront = product?.imgFrontUrl,
                imgBack = product?.imgNutritionUrl
            )
        }
    }
}

@Preview
@Composable
fun PreviewProductImageLoading() {
    BarcodeScannerTheme {
        ProductImage(product = null, isLoading = true)
    }
}

@Preview
@Composable
fun PreviewProductImage() {

    val item = Json {
        ignoreUnknownKeys = true
        isLenient = true
        encodeDefaults = true
        explicitNulls = false
    }.decodeFromString<ItemListing>(bakersChocolate)

    BarcodeScannerTheme {
        ProductImage(product = item.product, isLoading = false)
    }
}

@Preview
@Composable
fun PreviewProductImageNotFound() {

    BarcodeScannerTheme {
        ProductImage(product = null, isLoading = false)
    }
}
