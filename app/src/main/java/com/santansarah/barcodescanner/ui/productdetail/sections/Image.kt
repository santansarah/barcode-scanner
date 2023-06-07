package com.santansarah.barcodescanner.ui.productdetail.sections

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.santansarah.barcodescanner.data.remote.Product
import com.santansarah.barcodescanner.domain.models.AppDestinations.HOME
import com.santansarah.barcodescanner.domain.models.AppDestinations.SEARCH
import com.santansarah.barcodescanner.domain.models.AppRoutes
import com.santansarah.barcodescanner.ui.components.darkGrayShimmer
import com.santansarah.barcodescanner.ui.components.loadingBrush
import com.santansarah.barcodescanner.ui.previewparams.ProductDetailParams
import com.santansarah.barcodescanner.ui.previewparams.ProductDetails
import com.santansarah.barcodescanner.ui.productdetail.ItemImageSlider
import com.santansarah.barcodescanner.ui.theme.BarcodeScannerTheme
import com.santansarah.barcodescanner.ui.theme.lightGray
import com.santansarah.barcodescanner.ui.theme.redishMagenta


@OptIn(ExperimentalAnimationApi::class, ExperimentalTextApi::class)
@Composable
fun ProductImage(
    product: Product?,
    isLoading: Boolean,
    productError: String?,
    fromScreen: String,
    barcode: String,
    onRetry: (String) -> Unit,
    onRescan: () -> Unit,
    onSignIn: () -> Unit
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
                transitionSpec = productLoadingTransitionSpec(),
                label = "LoadingProductImages"
            ) { isLoading ->
                if (isLoading) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(6.dp),
                        text = "Loading".uppercase(),
                        style = TextStyle(
                            brush = loadingBrush(fontSize = 16.sp, darkGrayShimmer),
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
                        text = productText.ifEmpty { "Product Not Found" }.uppercase(),
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

                /**
                 * If the product isn't loading and it's null, then I know I have a problem.
                 * Now, it's time to figure out what to show the user. First, I set up the
                 * barcode message.
                 */
                val barcodeMessage = "When you scan your barcode, make sure that " +
                        "it's flat, well lit, and matches $barcode. Tap to scan again."

                /**
                 * If the error message from the Api isn't null, then I include it.
                 */
                productError?.let {
                    ProductDetailError(errorMessage = "$it Tap to try again.",
                        onRetry = { onRetry(barcode) }
                    )
                }

                /**
                 * If we got here from the HOME screen, this means that a user scanned
                 * a barcode. If that's the case, show the barcode error message.
                 */
                if (fromScreen == AppRoutes.HOME_SCREEN) {
                    Spacer(Modifier.height(12.dp))
                    ProductDetailError(errorMessage = barcodeMessage, onRetry = onRescan)
                }
            } else {
                UserActions(onSignIn)
                ItemImageSlider(
                    imgFront = product?.imgFrontUrl,
                    imgBack = product?.imgNutritionUrl
                )
            }
        }
    }
}

@Composable
fun ProductDetailError(
    errorMessage: String,
    onRetry: () -> Unit,
) {

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onRetry()
            },
        shape = RectangleShape,
        colors = CardDefaults.elevatedCardColors(
            containerColor = lightGray.copy(.5f)
        )
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp, horizontal = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(.9f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier
                        .padding(end = 10.dp)
                        .size(38.dp),
                    imageVector = Icons.Outlined.Info,
                    contentDescription = "Error Icon",
                    tint = redishMagenta
                )
                Text(
                    text = errorMessage,
                    style = MaterialTheme.typography.titleMedium,
                    color = redishMagenta
                )
            }
        }
    }
    Spacer(modifier = Modifier.height(20.dp))

}

/**
 * With PreviewParameters, I can loop through each error scenario and see what it
 * looks like.
 */
@Preview
@Composable
fun PreviewProductImageLoading(
    @PreviewParameter(ProductDetailParams::class) featureParams: ProductDetails
) {
    BarcodeScannerTheme {
        ProductImage(
            product = featureParams.product,
            isLoading = featureParams.isLoading,
            productError = featureParams.errorMessage,
            fromScreen = featureParams.fromScreen,
            barcode = featureParams.barcode, {}, {}, {}
        )
    }
}

/**
 * Handling errors gracefully and responsively is key to creating a great User Experience.
 * A simple snack bar might work in some cases, but if you need more functionality, don't
 * hesitate to go the extra mile - your users will appreciate it 😀!
 * That's all I have for today, thanks for watching.
 */
