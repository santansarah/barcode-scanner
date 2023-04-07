package com.santansarah.barcodescanner.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeRoute(
    viewModel: HomeViewModel = hiltViewModel(),
    onGotBarcode: (String) -> Unit,
    onSearchClicked: (String) -> Unit
) {

    val barcodeResults = viewModel.barcode.collectAsStateWithLifecycle().value

    LaunchedEffect(key1 = barcodeResults) {
        Timber.d(barcodeResults)
        barcodeResults?.let {
            onGotBarcode(barcodeResults)
            viewModel.clearBarcode()
        }
    }

    Column {

        val scope = rememberCoroutineScope()
        Button(onClick = { viewModel.scanBarcode() }) {
            Text(text = "Scan Item")
        }

        Spacer(modifier = Modifier.height(20.dp))

        var searchText by rememberSaveable {
            mutableStateOf("")
        }

        TextField(value = searchText,
            onValueChange = { searchText = it }
        )

        Button(onClick = { onSearchClicked(searchText) }) {
            Text(text = "Search")
        }
    }

}