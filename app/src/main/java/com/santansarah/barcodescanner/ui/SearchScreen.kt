package com.santansarah.barcodescanner.ui

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Brush.Companion.linearGradient
import androidx.compose.ui.graphics.Brush.Companion.radialGradient
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.santansarah.barcodescanner.R
import com.santansarah.barcodescanner.data.remote.SearchProductItem
import com.santansarah.barcodescanner.data.remote.SearchResults
import com.santansarah.barcodescanner.data.remote.mock.searchResults
import com.santansarah.barcodescanner.ui.theme.BarcodeScannerTheme
import kotlinx.coroutines.flow.flowOf
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import timber.log.Timber

@Composable
fun SearchRoute(
    viewModel: SearchViewModel = hiltViewModel(),
    onBackClicked: () -> Unit,
    onGotBarcode: (String) -> Unit,
) {

    val searchResults = viewModel.searchResults.collectAsLazyPagingItems()

    ShowSearchResults(
        searchResults = searchResults,
        onBackClicked = onBackClicked,
        onGotBarcode = onGotBarcode
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowSearchResults(
    searchResults: LazyPagingItems<SearchProductItem>,
    onBackClicked: () -> Unit,
    onGotBarcode: (String) -> Unit
) {

    Scaffold(
        topBar = {
            MainAppBar(
                onBackClicked = onBackClicked,
                title = "Search Results"
            )
        }
    ) { padding ->

        when (searchResults.loadState.refresh) { //FIRST LOAD
            is LoadState.Error -> {
                //TODO Error Item
                //state.error to get error message
            }

            is LoadState.Loading -> { // Loading UI
                Timber.d("Refreshing loadState")
                Column(
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Text(
                        modifier = Modifier
                            .padding(8.dp),
                        text = "Loading Search Results"
                    )

                    CircularProgressIndicator(color = Color.Black)
                }
            }

            else -> {
                LazyColumn(
                    state = rememberLazyListState(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(padding),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    items(searchResults) { productInfo ->
                        ProductSearchListItem(productInfo, onGotBarcode)
                    }
                }
            }
        }

    }
}

@Composable
fun ProductSearchListItem(
    productInfo: SearchProductItem?,
    onGotBarcode: (String) -> Unit
) {
    productInfo?.let {
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(6.dp)
                .clickable {
                    onGotBarcode(it.code)
                },
            shape = RoundedCornerShape(10.dp),
        ) {

            Box(modifier = Modifier.fillMaxSize()) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .defaultMinSize(minHeight = 100.dp)
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(.15f)
                            .fillMaxHeight()
                            .padding(end = 10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {

                        it.imageUrl?.let {
                            val painter = rememberAsyncImagePainter(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(productInfo.imageUrl)
                                    .size(Size.ORIGINAL)
                                    .crossfade(true)
                                    .build(),
                            )

                            when (painter.state) {
                                is AsyncImagePainter.State.Loading -> {
                                    Image(
                                        painter = painterResource(id = R.drawable.image_placeholder),
                                        contentDescription = productInfo.productName,
                                        colorFilter = ColorFilter.tint(AnimateColor())
                                    )
                                }

                                is AsyncImagePainter.State.Success -> {
                                    Image(
                                        painter = painter,
                                        contentDescription = productInfo.productName
                                    )
                                }

                                else -> {
                                    Image(
                                        painter = painterResource(id = R.drawable.image_placeholder),
                                        contentDescription = productInfo.productName
                                    )
                                }
                            }
                        } ?: Image(
                            painter = painterResource(id = R.drawable.image_placeholder),
                            contentDescription = it.productName
                        )
                    }
                    Column(
                        modifier = Modifier.fillMaxWidth(.9f),
                        verticalArrangement = Arrangement.Center

                    ) {
                        it.brandOwner?.let {
                            Text(
                                text = it,
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                        it.productName?.let {
                            Text(
                                text = it,
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    }
                }
            }
        }

    }
}


@Composable
fun shimmerBrush(): Brush {

    val outsideColor = Color(0xFFEDE3E9)
    val insideColor = Color(0xFFe0a3b1).copy(.9f)
    val bufferColor = Color(0xFFe9cdd2)

    val shimmerColors = listOf(
        outsideColor,
        bufferColor,
        insideColor,
        bufferColor,
        outsideColor
    )

    val transition = rememberInfiniteTransition(label = "imageTransition")

    val translateAnimation = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1300f,
        animationSpec = infiniteRepeatable(
            animation = tween(800), repeatMode = RepeatMode.Reverse
        ), label = "imageAnimation"
    )
    return linearGradient(
        colors = shimmerColors,
        start = Offset.Zero,
        end = Offset(x = translateAnimation.value, y = translateAnimation.value)
    )
}


@Preview
@Composable
fun PreviewSearchResults() {

    val item = Json {
        ignoreUnknownKeys = true
    }.decodeFromString<SearchResults>(searchResults)
    val placeHolder = item.products.map {
        it.copy(imageUrl = null)
    }

    BarcodeScannerTheme {
        ShowSearchResults(
            searchResults =
            flowOf(PagingData.from(placeHolder)).collectAsLazyPagingItems(), {}, {}
        )
    }

}

@Composable
fun AnimateColor(): Color {
    val infiniteTransition = rememberInfiniteTransition(label = "")
    val color by infiniteTransition.animateColor(
        initialValue = Color.LightGray,
        targetValue = Color(0xFFff84a9),
        animationSpec = infiniteRepeatable(
            animation = tween(300, easing = FastOutLinearInEasing),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )

    return color
}

@Preview
@Composable
fun PreviewShimmer() {

    val outsideColor = Color(0xFFEDE3E9)
    val insideColor = Color(0xFFe0a3b1).copy(.9f)
    val bufferColor = Color(0xFFe9cdd2)

    val shimmerColors = listOf(
        bufferColor,
        insideColor,
        insideColor,
        bufferColor
    )

    val test = radialGradient(
        colors = shimmerColors,
        radius = 20f
    )



    Image(
        painter = painterResource(id = R.drawable.image_placeholder),
        contentDescription = "",
        colorFilter = ColorFilter.tint(AnimateColor())
        /*modifier = Modifier.background(
            shimmerBrush()
        )
*/
    )

}
