package com.santansarah.barcodescanner

import android.os.Bundle
import android.view.LayoutInflater
import android.view.autofill.AutofillManager
import android.widget.EditText
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import coil.ImageLoader
import com.google.firebase.ml.modeldownloader.CustomModelDownloadConditions
import com.google.firebase.ml.modeldownloader.DownloadType
import com.google.firebase.ml.modeldownloader.FirebaseModelDownloader
import com.santansarah.barcodescanner.data.remote.FoodApi
import com.santansarah.barcodescanner.data.remote.FoodRepository
import com.santansarah.barcodescanner.data.remote.ItemListing
import com.santansarah.barcodescanner.domain.models.AppDestinations.HOME
import com.santansarah.barcodescanner.ui.AppNavGraph
import com.santansarah.barcodescanner.ui.components.test
import com.santansarah.barcodescanner.ui.theme.BarcodeScannerTheme
import dagger.hilt.android.AndroidEntryPoint
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.internal.wait
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var recommendationService: RecommendationService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Downloading could take some time, so make sure to launch this in a
        // coroutine, so it loads in the background.
        this.lifecycleScope.launch {
            recommendationService.downloadModel()
            Timber.d("recid: $recommendationService")
        }

        setContent {

            var password by rememberSaveable {
                mutableStateOf("")
            }

            Column {
                AndroidView(

                    modifier = Modifier
                        .fillMaxWidth(),
                    factory = { context ->
                        val autofillManager = context.getSystemService(AutofillManager::class.java)
                        autofillManager.registerCallback(test)
                        val layout =
                            LayoutInflater.from(context).inflate(R.layout.username_field, null)
                        layout
                    },
                    update = {

                    }
                )


                Text(text = password)

                AndroidView(

                    modifier = Modifier
                        .fillMaxWidth(),
                    factory = { context ->
                        val autofillManager = context.getSystemService(AutofillManager::class.java)
                        autofillManager.registerCallback(test)
                        val layout =
                            LayoutInflater.from(context).inflate(R.layout.password_field, null)

                        val afm = context.getSystemService(AutofillManager::class.java)
                        afm?.requestAutofill(layout)
                        layout
                    },
                    update = {

                    }
                )
            }

            /*BarcodeScannerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    AppNavGraph(navController = rememberNavController(), startDestination = HOME)

                }
            }*/
        }
    }

}


