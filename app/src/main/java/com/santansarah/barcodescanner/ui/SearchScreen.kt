package com.santansarah.barcodescanner.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOut
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.with
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Brush.Companion.linearGradient
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.LinearGradientShader
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import androidx.paging.compose.itemsIndexed
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.santansarah.barcodescanner.R
import com.santansarah.barcodescanner.data.remote.SearchProductItem
import com.santansarah.barcodescanner.data.remote.SearchResults
import com.santansarah.barcodescanner.data.remote.mock.searchResults
import com.santansarah.barcodescanner.ui.components.MainAppBar
import com.santansarah.barcodescanner.ui.search.SearchLoadingScreen
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
    val searchText = viewModel.searchStringFromState

    ShowSearchResults(
        searchResults = searchResults,
        onBackClicked = onBackClicked,
        onGotBarcode = onGotBarcode,
        searchText = searchText
    )

}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun ShowSearchResults(
    searchResults: LazyPagingItems<SearchProductItem>,
    onBackClicked: () -> Unit,
    onGotBarcode: (String) -> Unit,
    searchText: String
) {

    Scaffold(
        topBar = {
            MainAppBar(
                onBackClicked = onBackClicked,
                title = "Search Results"
            )
        }
    ) { padding ->

        var showLoadingScreen by
        rememberSaveable { mutableStateOf(true) }

        if (searchResults.loadState.refresh is LoadState.NotLoading) {
            showLoadingScreen = false
        }

        AnimatedContent(
            targetState = showLoadingScreen, label = "",
            transitionSpec = {
               slideInHorizontally {it } with slideOutHorizontally { -it }
            }
        ) {
            if (it)
                SearchLoadingScreen(padding, searchText)
            else
                LazyColumn(
                    state = rememberLazyListState(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(padding),
                        //.padding(top = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    items(searchResults) { productInfo ->
                        ProductSearchListItem(productInfo, onGotBarcode) {
                            PlaceholderImage(
                                description = productInfo?.productName ?: "Product Image"
                            )
                        }
                    }
                }
        }


    }
}


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

            Box(modifier = Modifier.fillMaxSize()
                .padding(8.dp)) {

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

                        AsyncImage(
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
                        )

                    }

                    Column(
                        verticalArrangement = Arrangement.Center

                    ) {

                        val productText = listOfNotNull(
                            it.brandOwner,
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

@Composable
fun PlaceholderImage(
    description: String
) {
    Image(
        modifier = Modifier
            //.clip(RoundedCornerShape(20.dp))
            .height(140.dp)
            .width(100.dp)
            .border(2.dp, Color(0xFFcacfc9)),
        //.border(1.dp, Color(0xFFf9004b), RoundedCornerShape(20.dp)),
        painter = painterResource(id = R.drawable.food_placholder),
        contentDescription = description,
        colorFilter = ColorFilter.tint(Color(0xFFcacfc9))
    )
}


@Preview
@Composable
fun PreviewResultItemsNoImage() {

    val item = Json {
        ignoreUnknownKeys = true
    }.decodeFromString<SearchResults>(searchResults)
    val placeHolder = item.products.map {
        it.copy(imageUrl = null)
    }
    val itemsFlow =
        flowOf(PagingData.from(placeHolder)).collectAsLazyPagingItems()

    BarcodeScannerTheme {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {

            LazyColumn(
                state = rememberLazyListState(),
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                itemsIndexed(itemsFlow) { idx, productInfo ->
                    ProductSearchListItem(productInfo, { },
                        { productInfo?.productName?.let { PlaceholderImage(description = it) } })
                }
            }
        }
    }

}

@Preview
@Composable
fun PreviewResultItems() {

    val item = Json {
        ignoreUnknownKeys = true
    }.decodeFromString<SearchResults>(searchResults)
    val placeHolder = item.products.map {
        it.copy(imageUrl = null)
    }
    val itemsFlow =
        flowOf(PagingData.from(placeHolder)).collectAsLazyPagingItems()

    BarcodeScannerTheme {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {

            LazyColumn(
                state = rememberLazyListState(),
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                itemsIndexed(itemsFlow) { idx, productInfo ->
                    ProductSearchListItem(productInfo, { },
                        {

                            val previewImage = if (idx % 2 == 0)
                                painterResource(id = R.drawable.mock_image)
                            else
                                painterResource(id = R.drawable.mock_landscape)

                            Image(
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    //.clip(RoundedCornerShape(20.dp))
                                    .height(140.dp)
                                    .width(100.dp)
                                    .border(1.dp, Color(0xFFf9004b))
                                    .background(Color.DarkGray),
                                painter = previewImage,
                                contentDescription = ""
                            )
                        })
                }
            }
        }
    }

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
            flowOf(PagingData.from(placeHolder)).collectAsLazyPagingItems(), {}, {},
            "ice cream"
        )
    }

}

@Composable
fun AnimateColor(): Color {
    val infiniteTransition = rememberInfiniteTransition(label = "")
    val color by infiniteTransition.animateColor(
        initialValue = Color(0xFFdfe2de),
        targetValue = Color(0xFFcacfc9),
        animationSpec = infiniteRepeatable(
            animation = tween(300, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )

    return color
}

