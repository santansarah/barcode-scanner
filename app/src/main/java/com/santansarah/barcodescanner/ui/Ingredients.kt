package com.santansarah.barcodescanner.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.santansarah.barcodescanner.data.remote.Product

@Composable
fun Ingredients(product: Product) {
    
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        
        Text(text = "Ingredients", style = MaterialTheme.typography.titleMedium)
        Text(text = product.ingredients ?: "Missing")
    }
    
}