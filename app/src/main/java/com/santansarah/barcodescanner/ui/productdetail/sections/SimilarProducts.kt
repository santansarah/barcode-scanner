package com.santansarah.barcodescanner.ui.productdetail.sections

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.santansarah.barcodescanner.data.remote.SimilarItemListing
import com.santansarah.barcodescanner.data.remote.SimilarProduct
import com.santansarah.barcodescanner.data.remote.mock.similarProducts
import com.santansarah.barcodescanner.ui.components.PlaceholderImage
import com.santansarah.barcodescanner.ui.theme.BarcodeScannerTheme
import com.santansarah.barcodescanner.ui.theme.brightYellow
import com.santansarah.barcodescanner.ui.theme.darkBackground

@Composable
fun SimilarProducts(
    similarProducts: List<SimilarItemListing>,
    onGotBarcode: (String) -> Unit
) {

    Divider(thickness = 2.dp, color = Color.DarkGray)
    ElevatedCard(
        shape = RectangleShape,
        colors = CardDefaults.elevatedCardColors(
            containerColor = Color.Transparent
        )
    ) {

        Column(
            modifier = Modifier
                .background(brightYellow)
                .fillMaxWidth()
        ) {
            Text(
                modifier = Modifier
                    .padding(6.dp),
                text = "You might also like:",
                style = MaterialTheme.typography.titleLarge
            )
        }
        Divider(thickness = 2.dp, color = Color.DarkGray)

        LazyHorizontalGrid(
            modifier = Modifier
                .padding(vertical = 16.dp, horizontal = 6.dp)
                .height(200.dp),
            rows = GridCells.Fixed(1),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(similarProducts) { itemListing ->
                SimilarProductListItem(
                    productInfo = itemListing.product,
                    barcode = itemListing.code,
                    onGotBarcode = onGotBarcode
                ) {
                    PlaceholderImage(
                        description = itemListing.product?.productName ?: "Product Image"
                    )
                }
            }
        }
    }

}


@Preview(showBackground = true)
@Composable
fun PreviewSimilarProducts() {

    val similarProducts = similarProducts

    BarcodeScannerTheme {
        Surface(
            color = darkBackground
        ) {
            Column {
                SimilarProducts(similarProducts = similarProducts, onGotBarcode = {})
            }
        }
    }
}


