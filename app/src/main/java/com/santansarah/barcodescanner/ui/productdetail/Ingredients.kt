package com.santansarah.barcodescanner.ui.productdetail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.santansarah.barcodescanner.data.remote.Product

@Composable
fun Ingredients(product: Product) {
    
    Column(
        modifier = Modifier.fillMaxWidth()
            .padding(horizontal = 6.dp, vertical = 12.dp)
    ) {
        Text(text = product.ingredients ?: "Missing")
    }
    
}