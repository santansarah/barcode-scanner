package com.santansarah.barcodescanner.ui.search

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
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.santansarah.barcodescanner.data.remote.SearchProductItem
import com.santansarah.barcodescanner.data.remote.SearchResults
import com.santansarah.barcodescanner.data.remote.mock.searchResults
import com.santansarah.barcodescanner.ui.components.PlaceholderImage
import com.santansarah.barcodescanner.ui.search.imageanimations.ColorChangingImageLoading
import com.santansarah.barcodescanner.ui.search.imageanimations.searchImageLoadingTransition
import com.santansarah.barcodescanner.ui.theme.BarcodeScannerTheme
import com.santansarah.barcodescanner.ui.theme.darkBackground
import kotlinx.coroutines.flow.flowOf
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import timber.log.Timber
import com.santansarah.barcodescanner.R

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ProductSearchListItem(
    productInfo: SearchProductItem?,
    onGotBarcode: (String) -> Unit,
    placeHolderImage: @Composable () -> Unit
) {
    productInfo?.let {
        Divider(thickness = 2.dp, color = Color.DarkGray)
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                //.padding(vertical = 6.dp)
                .clickable {
                    onGotBarcode(it.code)
                },
            shape = RectangleShape
        ) {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier
                            .padding(end = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
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


                    }

                    /* AsyncImage(
                         model = ImageRequest.Builder(LocalContext.current)
                             .data(productInfo.imageUrl)
                             .crossfade(true)
                             .build(),
                         placeholder = painterResource(id = R.drawable.food_placholder),
                         error = painterResource(id = R.drawable.food_placholder),
                         fallback = painterResource(id = R.drawable.food_placholder),
                         contentScale = ContentScale.Crop,
                         modifier = Modifier
                             .height(140.dp)
                             .width(100.dp)
                             .border(2.dp, Color(0xFFcacfc9)),
                         contentDescription = productInfo.productName
                     )*/

                    Column(
                        verticalArrangement = Arrangement.Center

                    ) {

                        val productText = listOfNotNull(
                            it.brands,
                            it.productName
                        ).joinToString(" ")

                        Text(
                            text = productText,
                            style = MaterialTheme.typography.titleLarge,
                            overflow = TextOverflow.Ellipsis,
                            color = Color.Black
                        )

                    }
                }

            }
        }
        Divider(thickness = 2.dp, color = Color.DarkGray)
        Spacer(Modifier.height(10.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSearchItem() {

    val item = Json {
        ignoreUnknownKeys = true
    }.decodeFromString<SearchResults>(searchResults)
    val placeHolder = item.products.map {
        it.copy(imageUrl = null)
    }

    BarcodeScannerTheme {
        Surface(
           color = darkBackground
        ) {
            Column {
                ProductSearchListItem(productInfo = item.products[0], onGotBarcode = {}, {
                    PlaceholderImage(
                        description = ""
                    )
                })
                Spacer(modifier = Modifier.height(12.dp))
                ProductSearchListItem(productInfo = item.products[1], onGotBarcode = {}, {
                    PlaceholderImage(
                        description = ""
                    )
                })
            }
        }
    }
}


