package com.santansarah.barcodescanner.ui.search

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.material3.Text
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
import androidx.compose.ui.tooling.preview.PreviewParameter
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
import com.santansarah.barcodescanner.ui.components.PlaceholderImage
import com.santansarah.barcodescanner.ui.previewparams.SearchParams
import com.santansarah.barcodescanner.ui.previewparams.SearchResultsFeature
import com.santansarah.barcodescanner.ui.theme.BarcodeScannerTheme
import kotlinx.coroutines.flow.flowOf
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import timber.log.Timber

@Composable
fun SearchRoute(
    viewModel: SearchViewModel = hiltViewModel(),
    onBackClicked: () -> Unit,
    onGotBarcode: (String) -> Unit
) {

    val searchResults = viewModel.searchResults.collectAsLazyPagingItems()
    val searchText = viewModel.searchText.collectAsStateWithLifecycle().value

    ShowSearchResults(
        searchResults = searchResults,
        refreshLoadState = searchResults.loadState.refresh,
        appendLoadState = searchResults.loadState.append,
        onBackClicked = onBackClicked,
        onGotBarcode = onGotBarcode,
        searchText = searchText,
        onSearchValueChanged = viewModel::onSearchValueChanged,
        onSearch = viewModel::onSearch
    )

}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun ShowSearchResults(
    searchResults: LazyPagingItems<SearchProductItem>,
    refreshLoadState: LoadState,
    appendLoadState: LoadState,
    onBackClicked: () -> Unit,
    onGotBarcode: (String) -> Unit,
    searchText: String,
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

        var showLoadingScreen by
        rememberSaveable { mutableStateOf(true) }

        if (refreshLoadState is LoadState.NotLoading)
            showLoadingScreen = false

        var showAppendError by
        rememberSaveable { mutableStateOf(false) }

        if (appendLoadState is LoadState.Error)
            showAppendError = true

        AnimatedContent(
            targetState = showLoadingScreen, label = "",
            transitionSpec = {
                slideInHorizontally { it } with slideOutHorizontally { -it } + fadeOut()
            }
        ) {
            if (it)
                SearchLoadingScreen(padding, searchText,
                    (refreshLoadState is LoadState.Error),
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

                    if (showAppendError) {
                        item {
                            Text(
                                modifier = Modifier.padding(vertical = 12.dp),
                                text = "Error loading more results..."
                            )
                        }
                    }

                }
        }


    }
}

@Preview
@Composable
fun PreviewSearchResultsLoading(
    @PreviewParameter(SearchParams::class) featureParams: SearchResultsFeature
) {

    val refreshLoadState = featureParams.refreshLoadState
    val appendLoadState = featureParams.appendLoadState

    BarcodeScannerTheme {
        ShowSearchResults(
            searchResults = flowOf(PagingData.from(featureParams.searchResults.products))
                .collectAsLazyPagingItems(),
            refreshLoadState, appendLoadState, {}, {},
            "ice cream", {}, {}
        )
    }

}


