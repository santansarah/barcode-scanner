package com.santansarah.barcodescanner.ui

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.santansarah.barcodescanner.R
import com.santansarah.barcodescanner.data.remote.ItemListing
import com.santansarah.barcodescanner.data.remote.Product
import com.santansarah.barcodescanner.data.remote.SearchProductItem
import com.santansarah.barcodescanner.data.remote.SearchResults
import com.santansarah.barcodescanner.data.remote.mock.cashews
import com.santansarah.barcodescanner.data.remote.mock.notfound
import com.santansarah.barcodescanner.data.remote.mock.searchResults
import com.santansarah.barcodescanner.ui.components.BackIcon
import com.santansarah.barcodescanner.ui.components.ItemImageSlider
import com.santansarah.barcodescanner.ui.theme.BarcodeScannerTheme
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

@Composable
fun SearchRoute(
    viewModel: SearchViewModel = hiltViewModel(),
    onBackClicked: () -> Unit
) {

    val itemListing = viewModel.itemListing.collectAsStateWithLifecycle().value


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowSearchResults(
    searchResults: List<SearchProductItem>,
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
                                .padding(8.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth(.15f)
                                    .padding(end = 6.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {

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
                            }
                            Column(modifier = Modifier.fillMaxWidth(.9f)) {
                                productInfo.brandOwner?.let {
                                    Text(
                                        text = it
                                    )
                                }
                                productInfo.productName?.let {
                                    Text(
                                        text = it,
                                        style = MaterialTheme.typography.bodyMedium
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

@Preview
@Composable
fun PreviewSearchResults(){

    val item = Json {
        ignoreUnknownKeys = true
    }.decodeFromString<SearchResults>(searchResults)


    BarcodeScannerTheme {
        ShowSearchResults(searchResults = item.products) {

        }
    }

}
