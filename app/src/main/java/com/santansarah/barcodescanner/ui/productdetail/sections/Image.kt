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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.santansarah.barcodescanner.data.remote.ItemListing
import com.santansarah.barcodescanner.data.remote.Product
import com.santansarah.barcodescanner.data.remote.mock.bakersChocolate
import com.santansarah.barcodescanner.ui.productdetail.ItemImageSlider
import com.santansarah.barcodescanner.ui.theme.BarcodeScannerTheme
import com.santansarah.barcodescanner.ui.theme.brightYellow
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json


@OptIn(ExperimentalAnimationApi::class)
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
                .background(brightYellow)
                .fillMaxWidth()
        ) {
            AnimatedContent(
                targetState = isLoading,
                transitionSpec = {
                    fadeIn(animationSpec = tween(150, 150)) with
                            fadeOut(animationSpec = tween(150)) using
                            SizeTransform { initialSize, targetSize ->
                                keyframes {
                                    IntSize(initialSize.width, targetSize.height) at 150
                                    durationMillis = 300
                                }
                            }
                }, label = ""
            ) { isLoading ->
                if (isLoading) {
                    Text(
                        modifier = Modifier
                            .padding(6.dp),
                        text = "Product Name",
                        style = MaterialTheme.typography.titleLarge
                    )
                } else {

                    val productText = listOfNotNull(
                        product?.brands,
                        product?.productName
                    ).joinToString(" ")

                    Text(
                        modifier = Modifier
                            .padding(6.dp),
                        text = productText.ifEmpty { "Unknown Product" },
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }
        }
        Divider(thickness = 2.dp, color = Color.DarkGray)

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (!isLoading && product == null) {
                Text(
                    modifier = Modifier.padding(bottom = 8.dp),
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
