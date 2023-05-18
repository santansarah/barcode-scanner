package com.santansarah.barcodescanner.ui.productdetail.sections

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.santansarah.barcodescanner.data.remote.SearchResults
import com.santansarah.barcodescanner.data.remote.mock.searchResults
import com.santansarah.barcodescanner.ui.components.PlaceholderImage
import com.santansarah.barcodescanner.ui.search.imageanimations.ColorChangingImageLoading
import com.santansarah.barcodescanner.ui.search.imageanimations.searchImageLoadingTransition
import com.santansarah.barcodescanner.ui.theme.BarcodeScannerTheme
import com.santansarah.barcodescanner.ui.theme.darkBackground
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import timber.log.Timber
import com.santansarah.barcodescanner.R
import com.santansarah.barcodescanner.data.remote.SimilarProduct
import com.santansarah.barcodescanner.data.remote.mock.similarProducts

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SimilarProductListItem(
    productInfo: SimilarProduct?,
    barcode: String,
    onGotBarcode: (String) -> Unit,
    placeHolderImage: @Composable () -> Unit
) {
    productInfo?.let {
        ElevatedCard(
            modifier = Modifier
                .width(160.dp)
                .height(200.dp)
                .border(1.dp, Color.DarkGray)
                .clickable {
                    onGotBarcode(barcode)
                },
            shape = RectangleShape
        ) {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
            ) {

                productInfo.imageUrl?.let {

                    val painter = rememberAsyncImagePainter(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(it)
                            .size(Size.ORIGINAL)
                            .build(),
                        contentScale = ContentScale.Crop,
                        error = painterResource(id = R.drawable.food_placholder)
                    )

                    var showLoadingAnimation by
                    remember { mutableStateOf(true) }

                    Timber.d("Painter: " + painter.state.toString())

                    when (painter.state) {
                        is AsyncImagePainter.State.Loading, is AsyncImagePainter.State.Empty -> {
                        }

                        is AsyncImagePainter.State.Error, is AsyncImagePainter.State.Success -> {
                            showLoadingAnimation = false
                        }
                    }

                    AnimatedContent(
                        targetState = showLoadingAnimation,
                        transitionSpec = searchImageLoadingTransition(),
                        label = "LoadingSearchImage"
                    ) { isLoading ->
                        if (isLoading)
                            ColorChangingImageLoading()
                        else
                            Image(
                                modifier = Modifier
                                    .height(140.dp)
                                    .width(100.dp)
                                    .border(2.dp, Color(0xFFcacfc9)),
                                painter = painter,
                                contentDescription = productInfo.productName,
                            )
                    }

                } ?: placeHolderImage()

                val productText = it.productName ?: "Similar Product"

                Text(
                    text = productText,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Black,
                    overflow = TextOverflow.Ellipsis
                )

            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSimilarProductItem() {

    val similarProducts = similarProducts

    BarcodeScannerTheme {
        Surface(
            color = darkBackground
        ) {
            Row(
                modifier = Modifier.padding(8.dp)
                    .fillMaxWidth()
            ) {

                SimilarProductListItem(productInfo = similarProducts[0].product,
                    barcode = similarProducts[0].code, onGotBarcode = {}, {
                        PlaceholderImage(
                            description = ""
                        )
                    })

                Spacer(modifier = Modifier.width(20.dp))

                SimilarProductListItem(productInfo = similarProducts[1].product,
                    barcode = similarProducts[1].code, onGotBarcode = {}, {
                        PlaceholderImage(
                            description = ""
                        )
                    })
            }
        }
    }
}


