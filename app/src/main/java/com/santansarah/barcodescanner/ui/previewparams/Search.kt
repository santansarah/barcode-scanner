package com.santansarah.barcodescanner.ui.previewparams

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.paging.LoadState
import com.santansarah.barcodescanner.data.remote.ItemListing
import com.santansarah.barcodescanner.data.remote.Product
import com.santansarah.barcodescanner.data.remote.SearchResults
import com.santansarah.barcodescanner.data.remote.mock.bakersChocolate
import com.santansarah.barcodescanner.data.remote.mock.searchResults
import com.santansarah.barcodescanner.domain.ErrorCode
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

/**
 * To see all of my states, I need the search results,
 * the initial fetch state, and the append state.
 */
data class SearchResultsFeature(
    val searchResults: SearchResults,
    val refreshLoadState: LoadState,
    val appendLoadState: LoadState
)

/**
 * Here, I manually decode the search results into a SearchResults
 * data class, using the searchResults mock JSON that we saw earlier.
 */
val chipsSearchResults = Json {
    ignoreUnknownKeys = true
}.decodeFromString<SearchResults>(searchResults)

/**
 * Now, with the PreviewParameterProvider, I can set up
 * all of the Paging states that I need to track,
 * along with the corresponding ErrorCode messages.
 */
class SearchParams : PreviewParameterProvider<SearchResultsFeature> {

    override val values = sequenceOf(
        SearchResultsFeature(chipsSearchResults, LoadState.Loading, LoadState.NotLoading(true)),
        SearchResultsFeature(chipsSearchResults, LoadState.Error(Throwable(ErrorCode.NETWORK_ERROR.message)),
            LoadState.NotLoading(true)),
        SearchResultsFeature(chipsSearchResults, LoadState.Error(Throwable(ErrorCode.API_SEARCH_TIMEOUT.message)),
            LoadState.NotLoading(true)),
        SearchResultsFeature(chipsSearchResults, LoadState.NotLoading(false),
            LoadState.Error(Throwable(ErrorCode.NETWORK_ERROR.message))),
        SearchResultsFeature(chipsSearchResults, LoadState.NotLoading(false),
            LoadState.Error(Throwable(ErrorCode.API_SEARCH_TIMEOUT.message))),
    )
}