package com.santansarah.barcodescanner.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.santansarah.barcodescanner.BarcodeScanner
import com.santansarah.barcodescanner.di.IoDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val barcodeScanner: BarcodeScanner,
    @IoDispatcher val dispatcher: CoroutineDispatcher
) : ViewModel() {

    val barcode = barcodeScanner.barCodeResults

    fun scanBarcode() {
        viewModelScope.launch(dispatcher) {
            barcodeScanner.startScan()
        }
    }

    fun clearBarcode() {
        barcodeScanner.barCodeResults.value = null
    }

}