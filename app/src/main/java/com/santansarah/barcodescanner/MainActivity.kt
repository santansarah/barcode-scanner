package com.santansarah.barcodescanner

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import coil.ImageLoader
import com.santansarah.barcodescanner.data.remote.FoodApi
import com.santansarah.barcodescanner.data.remote.FoodRepository
import com.santansarah.barcodescanner.data.remote.ItemListing
import com.santansarah.barcodescanner.ui.theme.BarcodeScannerTheme
import dagger.hilt.android.AndroidEntryPoint
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var barcodeScanner: BarcodeScanner

    @Inject
    lateinit var foodRepository: FoodRepository

    @Inject
    lateinit var imageLoader: ImageLoader

    @Inject
    lateinit var foodApi: FoodApi

    @Inject
    lateinit var ktorClient: HttpClient

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

                    runBlocking {
                        val stringResponse = foodApi.getInfoByBarCodeString()
                        println(stringResponse.string())
                        /**
                         * {"code":"8886303210559","product":{"nutriments":{"carbohydrates":2.3,"carbohydrates_100g":2.3,"carbohydrates_serving":"","carbohydrates_unit":"g","carbohydrates_value":2.3,"carbon-footprint-from-known-ingredients_100g":27,"carbon-footprint-from-known-ingredients_product":108,"energy":1008,"energy-kcal":241,"energy-kcal_100g":241,"energy-kcal_serving":"","energy-kcal_unit":"kcal","energy-kcal_value":241,"energy-kcal_value_computed":240.6,"energy_100g":1008,"energy_serving":"","energy_unit":"kcal","energy_value":241,"fat":24.6,"fat_100g":24.6,"fat_serving":"","fat_unit":"g","fat_value":24.6,"fruits-vegetables-nuts-estimate-from-ingredients_100g":90,"fruits-vegetables-nuts-estimate-from-ingredients_serving":90,"nova-group":4,"nova-group_100g":4,"nova-group_serving":4,"nutrition-score-fr":7,"nutrition-score-fr_100g":7,"nutrition-score-fr_serving":"","proteins":2.5,"proteins_100g":2.5,"proteins_serving":"","proteins_unit":"g","proteins_value":2.5,"salt":0.04,"salt_100g":0.04,"salt_serving":"","salt_unit":"g","salt_value":0.04,"saturated-fat":23.5,"saturated-fat_100g":23.5,"saturated-fat_serving":"","saturated-fat_unit":"g","saturated-fat_value":23.5,"sodium":0.016,"sodium_100g":0.016,"sodium_serving":"","sodium_unit":"g","sodium_value":0.016,"sugars":2.2,"sugars_100g":2.2,"sugars_serving":"","sugars_unit":"g","sugars_value":2.2}},"status":1,"status_verbose":"product found"}
                         */
                    }

                    /*runBlocking {
                        val itemResponse = ktorClient.use { client ->
                            client.get("https://us.openfoodfacts.org/api/v2/product/8886303210559") {
                                parameter("fields", "brands,nutriments")
                            }
                        }.body<ItemListing>()
                        println(itemResponse)
                    }*/

                    //AppNavGraph(navController = rememberNavController(), startDestination = HOME)

                }
            }
        }
    }
}
