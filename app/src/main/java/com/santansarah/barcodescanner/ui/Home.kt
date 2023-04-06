package com.santansarah.barcodescanner.ui

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import timber.log.Timber

@Composable
fun HomeRoute(
    viewModel: HomeViewModel = hiltViewModel(),
    onGotBarcode: (String) -> Unit
) {

    val barcodeResults = viewModel.barcode.collectAsStateWithLifecycle().value

    LaunchedEffect(key1 = barcodeResults) {
        Timber.d(barcodeResults)
        barcodeResults?.let {
            onGotBarcode(barcodeResults)
            viewModel.clearBarcode()
        }
    }

    val scope = rememberCoroutineScope()
    Button(onClick = { viewModel.scanBarcode() }) {
        Text(text = "Scan Item")
    }

}