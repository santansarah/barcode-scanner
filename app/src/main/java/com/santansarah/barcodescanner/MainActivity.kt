package com.santansarah.barcodescanner

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.santansarah.barcodescanner.data.remote.FoodRepository
import com.santansarah.barcodescanner.data.remote.ItemListing
import com.santansarah.barcodescanner.data.remote.mock.cornChips
import com.santansarah.barcodescanner.domain.models.AppDestinations.HOME
import com.santansarah.barcodescanner.ui.AppNavGraph
import com.santansarah.barcodescanner.ui.theme.BarcodeScannerTheme
import com.santansarah.barcodescanner.ui.ItemDetails
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var barcodeScanner: BarcodeScanner

    @Inject
    lateinit var foodRepository: FoodRepository

    @Inject
    lateinit var imageLoader: ImageLoader

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            val result = barcodeScanner.barCodeResults.collectAsState()

            BarcodeScannerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    AppNavGraph(navController = rememberNavController(), startDestination = HOME)


/*
                    AsyncImage(model = imageLoader.enqueue(
                        ImageRequest.Builder(LocalContext.current)
                            .data("https://images.openfoodfacts.org/images/products/007/874/208/1304/nutrition_en.9.400.jpg")
                            .build()
                    ), contentDescription = "")

                    AsyncImage(model = ImageRequest.Builder(LocalContext.current)
                            .data("https://images.openfoodfacts.org/images/products/007/874/208/1304/nutrition_en.9.400.jpg")
                            .build()
                    , contentDescription = "")
*/

                    /*Column {

                        *//*val item = Json {
                            ignoreUnknownKeys = true
                        }.decodeFromString<ItemListing>(cornChips)

                        BarcodeScannerTheme {
                            ItemDetails(itemListing = item)
                        }*//*

                        val scope = rememberCoroutineScope()
                        Button(onClick = {
                            scope.launch {
                                barcodeScanner.startScan()
                            }
                        }) {
                            Text(text = "Scan Item")
                        }

                        Text(
                            text = result.value ?: "Barcode Results",
                            style = MaterialTheme.typography.displayLarge
                        )

                        LaunchedEffect(key1 = result.value) {
                            result.value?.let {
                                val productSpec =
                                    foodRepository.getInfoByBarCode(it)
                            }
                        }
                    }*/
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    BarcodeScannerTheme {
        Greeting("Android")
    }
}