package com.santansarah.barcodescanner.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
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
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOut
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
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
    val searchText = viewModel.searchStringFromState

    ShowSearchResults(
        searchResults = searchResults,
        onBackClicked = onBackClicked,
        onGotBarcode = onGotBarcode,
        searchText = searchText
    )

}

@OptIn(ExperimentalMaterial3Api::class)
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

        val showLoadingScreen by
        remember { mutableStateOf(MutableTransitionState(true)) }

        if (searchResults.loadState.refresh is LoadState.NotLoading) {
            showLoadingScreen.targetState = false
        }

        AnimatedVisibility(
            visibleState = showLoadingScreen,
            enter = slideInHorizontally(),
            exit = fadeOut(animationSpec = tween(1000))
        ) {
            SearchLoadingScreen(padding, searchText)
        }

        LazyColumn(
            state = rememberLazyListState(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(padding),
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

@OptIn(ExperimentalTextApi::class)
@Composable
private fun SearchLoadingScreen(
    padding: PaddingValues,
    searchText: String
) {

    val currentFontSizePx = with(LocalDensity.current) { 36.sp.toPx() }
    val currentFontSizeDoublePx = currentFontSizePx * 2

    val infiniteTransition = rememberInfiniteTransition(label = "")
    val offset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = currentFontSizeDoublePx,
        animationSpec = infiniteRepeatable(
            tween(
                1000,
                easing = LinearEasing
            )
        ), label = ""
    )

    val brush = linearGradient(
        colors = listOf(
            Color(0xFFfe0033),
            Color(0xFFb80025),
            Color(0xFFfe0033),
        ),
        start = Offset(offset, offset),
        end = Offset(offset + currentFontSizePx, offset + currentFontSizePx),
        tileMode = TileMode.Mirror
    )

    Column(
        modifier = Modifier
            .padding(padding)
            .padding(vertical = 20.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {

        ElevatedCard(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            shape = RectangleShape
        ) {

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 40.dp)
            ) {
                Image(
                    modifier = Modifier.size(100.dp),
                    painter = painterResource(id = R.drawable.cherries),
                    contentDescription = "Cherries"
                )
                Image(
                    modifier = Modifier.size(100.dp),
                    painter = painterResource(id = R.drawable.banana),
                    contentDescription = "Banana"
                )
                Image(
                    modifier = Modifier.size(100.dp),
                    painter = painterResource(id = R.drawable.apple),
                    contentDescription = "Apple"
                )
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier
                        .padding(8.dp),
                    text = "Finding products that match:",
                    style = TextStyle(
                        brush = brush,
                        fontSize = 36.sp
                    ),
                    textAlign = TextAlign.Center
                )
                Text(
                    modifier = Modifier
                        .padding(8.dp),
                    text = "$searchText",
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = TextAlign.Center
                )
            }

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 40.dp)
            ) {
                Image(
                    modifier = Modifier.size(100.dp),
                    painter = painterResource(id = R.drawable.milk),
                    contentDescription = "Cherries"
                )
                Image(
                    modifier = Modifier.size(100.dp),
                    painter = painterResource(id = R.drawable.chicken),
                    contentDescription = "Banana"
                )
                Image(
                    modifier = Modifier.size(100.dp),
                    painter = painterResource(id = R.drawable.cheese),
                    contentDescription = "Apple"
                )
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
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .padding(vertical = 6.dp)
                .clickable {
                    onGotBarcode(it.code)
                },
            shape = RectangleShape
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
                            //.fillMaxWidth(.15f)
                            //.fillMaxHeight()
                            .padding(end = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(productInfo.imageUrl)
                                .crossfade(true)
                                .build(),
                            modifier = Modifier
                                //.clip(RoundedCornerShape(20.dp))
                                .height(140.dp)
                                .width(100.dp)
                                .border(2.dp, Color(0xFFcacfc9)),
                            contentScale = ContentScale.Crop,
                            contentDescription = productInfo.productName,
                            placeholder = painterResource(id = R.drawable.food_placholder)
                        )

                        Column(
                            //modifier = Modifier.fillMaxWidth(.9f),
                            verticalArrangement = Arrangement.Center

                        ) {

                            val productText = listOfNotNull(
                                it.brandOwner,
                                it.productName
                            ).joinToString(" ")

                            Text(
                                text = productText,
                                style = MaterialTheme.typography.titleLarge,
                                overflow = TextOverflow.Ellipsis
                            )

                        }
                    }
                }
            }
        }
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


@Composable
fun shimmerBrush(): Brush {

    val outsideColor = Color(0xFFdfe2de)
    val insideColor = Color(0xFFcacfc9).copy(.9f)
    val bufferColor = Color(0xFFdfe2de)

    val shimmerColors = listOf(
        outsideColor,
        insideColor,
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
fun PreviewAnimation() {

    val item = Json {
        ignoreUnknownKeys = true
    }.decodeFromString<SearchResults>(searchResults)

    BarcodeScannerTheme {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {

            LazyColumn {

                items(item.products) {

                    ElevatedCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                            .clickable { },
                        shape = RectangleShape
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
                                        //.fillMaxWidth(.15f)
                                        //.fillMaxHeight()
                                        .padding(end = 16.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {

                                    val showLoadingAnimation by
                                    remember { mutableStateOf(MutableTransitionState(true)) }

                                    AnimatedVisibility(
                                        visibleState = showLoadingAnimation,
                                        enter = fadeIn(initialAlpha = 0.4f),
                                        exit = fadeOut(tween(durationMillis = 250))

                                    ) {

                                        AnimatedFoodIcon()


                                        /*Image(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(20.dp))
                                                .height(140.dp)
                                                .width(100.dp),
                                            painter = painterResource(id = R.drawable.image_loading),
                                            contentDescription = "",
                                            colorFilter = ColorFilter.tint(AnimateColor())
                                        )*/
                                    }

                                }
                                Column(
                                    //modifier = Modifier.fillMaxWidth(.9f),
                                    verticalArrangement = Arrangement.Center

                                ) {
                                    it.brandOwner?.let {
                                        Text(
                                            text = it,
                                            style = MaterialTheme.typography.titleLarge
                                        )
                                    }
                                    it.productName?.let {
                                        Text(
                                            text = it,
                                            style = MaterialTheme.typography.titleLarge
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}

@Composable
private fun AnimatedFoodIcon() {
    val transition = rememberInfiniteTransition(label = "")

    val progress by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1300f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = ""
    )

    Icon(
        painterResource(id = R.drawable.food_placholder),
        modifier = Modifier
            .drawWithContent {
                with(drawContext.canvas.nativeCanvas) {
                    val checkPoint = saveLayer(null, null)
                    val brush = linearGradient(
                        colors = listOf(
                            Color(0xFFdfe2de),
                            Color(0xFFcacfc9),
                            Color(0xFFdfe2de),
                        ),
                        start = Offset.Zero,
                        end = Offset(progress, progress)

                    )

                    // Destination
                    drawContent()

                    // Source
                    drawRect(
                        brush = brush,
                        blendMode = BlendMode.SrcIn
                    )

                    restoreToCount(checkPoint)

                }
            }
            .width(100.dp)
            .height(140.dp)
            .border(2.dp, Color(0xFFdfe2de)),
        contentDescription = null
    )
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
                                    .clip(RoundedCornerShape(20.dp))
                                    .height(140.dp)
                                    .width(100.dp),
                                //.border(1.dp, Color(0xFFf9004b), RoundedCornerShape(20.dp)),
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

/*
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
        */
/*modifier = Modifier.background(
            shimmerBrush()
        )
*//*

    )

}
*/
