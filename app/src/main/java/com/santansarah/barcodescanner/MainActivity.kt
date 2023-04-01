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
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.santansarah.barcodescanner.data.remote.FoodRepository
import com.santansarah.barcodescanner.ui.theme.BarcodeScannerTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var barcodeScanner: BarcodeScanner

    @Inject
    lateinit var foodRepository: FoodRepository

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

                    Column {

                        Button(onClick = { barcodeScanner.startScan() }) {
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
                    }
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