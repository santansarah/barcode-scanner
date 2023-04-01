package com.santansarah.barcodescanner

import android.content.Context
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import kotlinx.coroutines.flow.MutableStateFlow

class BarcodeScanner(
    activity: Context
) {

    private val options = GmsBarcodeScannerOptions.Builder()
        .setBarcodeFormats(
            Barcode.FORMAT_ALL_FORMATS
        )
        .build()

    private val scanner = GmsBarcodeScanning.getClient(activity, options)

    val barCodeResults = MutableStateFlow<String?>(null)

    fun startScan() {

        scanner.startScan()
            .addOnSuccessListener { barcode ->
                Log.d("scantest", barcode.rawValue ?: "no raw; ${barcode.displayValue}")
                barCodeResults.value = barcode.rawValue
            }
            .addOnCanceledListener {
                // Task canceled
            }
            .addOnFailureListener { e ->
                Log.d("barcode", e.toString())
                barCodeResults.value = e.message
            }
    }

}