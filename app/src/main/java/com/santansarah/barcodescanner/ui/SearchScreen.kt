package com.santansarah.barcodescanner.ui

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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

@Composable
fun SearchRoute(
    viewModel: SearchViewModel = hiltViewModel(),
    onBackClicked: () -> Unit
) {

    val searchResults = viewModel.searchResults.collectAsLazyPagingItems()
    ShowSearchResults(searchResults = searchResults, onBackClicked = onBackClicked)

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowSearchResults(
    searchResults: LazyPagingItems<SearchProductItem>,
    onBackClicked: () -> Unit
) {

    Scaffold(
        topBar = {
            MainAppBar(
                onBackClicked = onBackClicked,
                title = "Search Results"
            )
        }
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            items(searchResults) { productInfo ->

                ProductSearchListItem(productInfo)
            }

            when (val state = searchResults.loadState.refresh) { //FIRST LOAD
                is LoadState.Error -> {
                    //TODO Error Item
                    //state.error to get error message
                }

                is LoadState.Loading -> { // Loading UI
                    item {
                        Column(
                            modifier = Modifier
                                .fillParentMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                        ) {
                            Text(
                                modifier = Modifier
                                    .padding(8.dp),
                                text = "Refresh Loading"
                            )

                            CircularProgressIndicator(color = Color.Black)
                        }
                    }
                }

                else -> {}
            }

            when (val state = searchResults.loadState.append) { // Pagination
                is LoadState.Error -> {
                    //TODO Pagination Error Item
                    //state.error to get error message
                }

                is LoadState.Loading -> { // Pagination Loading UI
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                        ) {
                            Text(text = "Pagination Loading")

                            CircularProgressIndicator(color = Color.Black)
                        }
                    }
                }

                else -> {}
            }
        }
    }

}

@Composable
private fun ProductSearchListItem(productInfo: SearchProductItem?) {
    productInfo?.let {
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(6.dp)
                .clickable {
                    //onClick(device.address)
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
                            .padding(end = 6.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {

                        productInfo.imageUrl?.let {
                            val painter = rememberAsyncImagePainter(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(productInfo.imageUrl)
                                    .size(Size.ORIGINAL)
                                    .build(),
                            )

                            when (painter.state) {
                                is AsyncImagePainter.State.Loading -> {
                                    CircularProgressIndicator()
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
                            contentDescription = productInfo.productName
                        )
                    }
                    Column(
                        modifier = Modifier.fillMaxWidth(.9f),
                        verticalArrangement = Arrangement.Center

                    ) {
                        productInfo.brandOwner?.let {
                            Text(
                                text = it,
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                        productInfo.productName?.let {
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
            flowOf(PagingData.from(placeHolder)).collectAsLazyPagingItems()
        ) {

        }
    }

}
