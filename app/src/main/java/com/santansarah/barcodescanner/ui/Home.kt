package com.santansarah.barcodescanner.ui

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.santansarah.barcodescanner.R
import com.santansarah.barcodescanner.ui.theme.BarcodeScannerTheme
import kotlinx.coroutines.launch
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeRoute(
    viewModel: HomeViewModel = hiltViewModel(),
    onGotBarcode: (String) -> Unit,
    onSearchClicked: (String) -> Unit,
) {

    val barcodeResults = viewModel.barcode.collectAsStateWithLifecycle().value

    LaunchedEffect(key1 = barcodeResults) {
        Timber.d(barcodeResults)
        barcodeResults?.let {
            onGotBarcode(barcodeResults)
            viewModel.clearBarcode()
        }
    }

    HomeScreen(onScan = viewModel::scanBarcode, onSearch = onSearchClicked)

}

@OptIn(ExperimentalTextApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onScan: () -> Unit,
    onSearch: (String) -> Unit
) {

    Scaffold(
        topBar = {
            MainAppBar(
                onBackClicked = { },
                title = "Welcome"
            )
        }
    ) { padding ->


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

                    var searchText by rememberSaveable {
                        mutableStateOf("")
                    }

                    TextField(value = searchText,
                        onValueChange = { searchText = it }
                    )

                    Button(onClick = { onSearch(searchText) }) {
                        Text(text = "Search")
                    }
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

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    val scope = rememberCoroutineScope()
                    Button(onClick = { onScan() }) {
                        Text(text = "Scan Item")
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewHomeScreen() {

    BarcodeScannerTheme {
        HomeScreen(onScan = { /*TODO*/ }, onSearch = {})
    }

}