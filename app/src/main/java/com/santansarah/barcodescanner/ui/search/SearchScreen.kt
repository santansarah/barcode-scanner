package com.santansarah.barcodescanner.ui.search

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.with
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.santansarah.barcodescanner.data.remote.SearchProductItem
import com.santansarah.barcodescanner.ui.components.MainAppBar
import com.santansarah.barcodescanner.ui.components.PlaceholderImage
import com.santansarah.barcodescanner.ui.components.loadingBrush
import com.santansarah.barcodescanner.ui.previewparams.SearchParams
import com.santansarah.barcodescanner.ui.previewparams.SearchResultsFeature
import com.santansarah.barcodescanner.ui.theme.BarcodeScannerTheme
import com.santansarah.barcodescanner.ui.theme.blueButton
import com.santansarah.barcodescanner.ui.theme.lightestGray
import kotlinx.coroutines.flow.flowOf
import timber.log.Timber

@Composable
fun SearchRoute(
    viewModel: SearchViewModel = hiltViewModel(),
    onBackClicked: () -> Unit,
    onGotBarcode: (String) -> Unit
) {

    /**
     * The SearchRoute gets our searchResults from the ViewModel, and I collect them as
     * LazyPagingItems.
     */
    val searchResults = viewModel.searchResults.collectAsLazyPagingItems()
    val searchText = viewModel.searchText.collectAsStateWithLifecycle().value

    /**
     * Then, I pass these results to the SearchResults screen, which handles all of our possible
     * Paging states. You might be wondering why I pass the loadStates in separately - this is so
     * I can pass whatever states that I want to my previews, and see how my composables look
     * with different paging states.
     */
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

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class, ExperimentalTextApi::class)
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

        /**
         * There are 5 key states to consider here:
         * 1. Is the initial fetch loading?
         * 2. Does the initial fetch have an error?
         * 3. Is the initial fetch complete?
         * 4. Are we requesting the next page?
         * 5. and finally, was there an error requesting the next page?
         *
         * That's a lot to check, but really, in code, it's not so bad. What's also nice is that
         * since the searchResults are cached in the ViewModel, I don't have to worry to much
         * about variables & recompositions here; everything seems to work really well when
         * I rotate the phone without having to remember anything here.
         */

        /**
         * First, I see if I need to show the loading screen, by checking the refreshLoadState.
         */
        val showInitialLoadingOrErrorScreen = refreshLoadState !is LoadState.NotLoading

        /**
         * Next, I check to see if there's an initial loading error. This message is coming from
         * LoadResult.Error(Throwable()) that we saw earlier in the PagingSource class.
         */
        val initialLoadErrorMessage = if (refreshLoadState is LoadState.Error)
            refreshLoadState.error.message ?: null
        else
            null

        /**
         * And here, I check to see if we got an error when I tried to get the next page. Again,
         * this is coming from PagingSource.
         */
        val appendErrorMessage = if (appendLoadState is LoadState.Error)
            appendLoadState.error.message
        else
            null

        /**
         * This animates the content between the Search Loading Screen and the Search Results
         * List. I've already gone over this in my previous video, so I'll skip it here.
         */
        AnimatedContent(
            targetState = showInitialLoadingOrErrorScreen, label = "",
            transitionSpec = {
                slideInHorizontally { it } with slideOutHorizontally { -it } + fadeOut()
            }
        ) { loadingInitialFetch ->
            if (loadingInitialFetch)
            /**
             * If the initial search results are still loading, I pass the error message variable
             * to the SearchLoadingScreen, even if it's null. Let's see how the SearchLoadingScreen
             * handles the error message.
             */
                SearchLoadingScreen(
                    padding, searchText,
                    initialLoadErrorMessage,
                    onSearchValueChanged, onSearch
                )
            else
            /**
             * This composable loads if the initial fetch is complete, and there's no error. But
             * at this point, we still need to be aware of 3 different states.
             */
                Timber.tag("paging3").d("Loading initial fetch...")
            LazyColumn(
                state = rememberLazyListState(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(padding),
                //.padding(top = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                /**
                 * First, show the list of our 24 products from the API.
                 */
                items(searchResults) { productInfo ->
                    ProductSearchListItem(productInfo, onGotBarcode) {
                        PlaceholderImage(
                            description = productInfo?.productName ?: "Product Image"
                        )
                    }
                }

                /**
                 * At the end of the list, show a Loading shimmer if we need to load more.
                 */
                if (searchResults.loadState.append is LoadState.Loading) {
                    item {
                        LoadMoreItem()
                    }
                }

                /**
                 * And finally, check to see if we got an error when requesting
                 * additional pages. One really cool thing about Paging 3 is that if we did
                 * get an error here, there's no need to start over at page 1 again. Here,
                 * I use 'searchResults.retry()', to request the page that failed again. Let's
                 * take a look at this composable real quick and see how it works.
                 */
                appendErrorMessage?.let {
                    item {
                        PagingAppendErrorItem(appendErrorMessage) { searchResults.retry() }
                    }
                }

            }
        }
    }
}

/**
 * Now that we've gone over the different states, let's try to get a preview of what each of
 * them looks like on the Search screens. Here, I'm using the @PreviewParameter annotation
 * to loop through all of my Paging states. Let's go into SearchResultsFeature and see how
 * this preview data class is used.
 */
@Preview
@Composable
fun PreviewSearchResultsLoading(
    @PreviewParameter(SearchParams::class) featureParams: SearchResultsFeature
) {

    /**
     * With the PreviewParameterProvider in place, now it's super easy to
     * pass the current state into my ShowSearchResults composable. Let's
     * take a look.
     */
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

    /**
     * In the first preview, we're waiting for our search results.
     * In the second, we've got a Network error for the initial fetch.
     * The third is connection time out for the initial fetch.
     * The fourth is a network error while fetching the next page,
     * and finally, a connection time out when fetching the next page.
     *
     * Notice in the last two previews, we only get the error. But let's
     * try interactive mode & see how it looks.
     */

}


