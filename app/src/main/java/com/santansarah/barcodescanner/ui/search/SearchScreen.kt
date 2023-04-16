package com.santansarah.barcodescanner.ui.search

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.with
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import androidx.paging.compose.itemsIndexed
import com.santansarah.barcodescanner.R
import com.santansarah.barcodescanner.data.remote.SearchProductItem
import com.santansarah.barcodescanner.data.remote.SearchResults
import com.santansarah.barcodescanner.data.remote.mock.searchResults
import com.santansarah.barcodescanner.ui.components.MainAppBar
import com.santansarah.barcodescanner.ui.theme.BarcodeScannerTheme
import kotlinx.coroutines.flow.flowOf
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

@Composable
fun SearchRoute(
    viewModel: SearchViewModel = hiltViewModel(),
    onBackClicked: () -> Unit,
    onGotBarcode: (String) -> Unit
) {

    val searchResults = viewModel.searchResults.collectAsLazyPagingItems()
    val searchText = viewModel.searchText.collectAsStateWithLifecycle().value
    val searchError = viewModel.searchError.collectAsStateWithLifecycle().value

    ShowSearchResults(
        searchResults = searchResults,
        onBackClicked = onBackClicked,
        onGotBarcode = onGotBarcode,
        searchText = searchText,
        searchError = searchError,
        onSearchValueChanged = viewModel::onSearchValueChanged,
        onSearch = viewModel::onSearch
    )

}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun ShowSearchResults(
    searchResults: LazyPagingItems<SearchProductItem>,
    onBackClicked: () -> Unit,
    onGotBarcode: (String) -> Unit,
    searchText: String,
    searchError: Boolean,
    onSearchValueChanged: (String) -> Unit,
    onSearch: () -> Unit
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
        rememberSaveable {
            mutableStateOf(
                (searchResults.loadState.refresh is LoadState.Loading
                        || searchResults.loadState.refresh is LoadState.Error)
            )
        }

        AnimatedContent(
            targetState = showLoadingScreen, label = "",
            transitionSpec = {
                slideInHorizontally { it } with slideOutHorizontally { -it }
            }
        ) {
            if (it)
                SearchLoadingScreen(padding, searchText,
                    (searchResults.loadState.refresh is LoadState.Error),
                    onSearchValueChanged, onSearch
                )
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
            "ice cream", false, {}, {}
        )
    }

}

