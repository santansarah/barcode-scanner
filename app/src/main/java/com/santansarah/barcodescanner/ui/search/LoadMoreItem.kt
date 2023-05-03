package com.santansarah.barcodescanner.ui.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.santansarah.barcodescanner.data.remote.SearchResults
import com.santansarah.barcodescanner.data.remote.mock.searchResults
import com.santansarah.barcodescanner.domain.ErrorCode
import com.santansarah.barcodescanner.ui.components.PlaceholderImage
import com.santansarah.barcodescanner.ui.components.loadingBrush
import com.santansarah.barcodescanner.ui.components.redShimmer
import com.santansarah.barcodescanner.ui.theme.BarcodeScannerTheme
import com.santansarah.barcodescanner.ui.theme.darkBackground
import com.santansarah.barcodescanner.ui.theme.lightGray
import com.santansarah.barcodescanner.ui.theme.redishMagenta
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

@OptIn(ExperimentalTextApi::class)
@Composable
fun LoadMoreItem() {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RectangleShape,
        colors = CardDefaults.elevatedCardColors(
            containerColor = lightGray
        )
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 6.dp, vertical = 12.dp),
            text = "Getting more products",
            style = TextStyle(
                brush = loadingBrush(fontSize = 16.sp, redShimmer),
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )
        )
    }
    Spacer(modifier = Modifier.height(20.dp))
}

@Preview(showBackground = true)
@Composable
fun PreviewLoadMoreItem() {

    val item = Json {
        ignoreUnknownKeys = true
    }.decodeFromString<SearchResults>(searchResults)

    BarcodeScannerTheme {
        Surface(
            color = darkBackground
        ) {
            Column {
                ProductSearchListItem(productInfo = item.products[0], onGotBarcode = {}, {
                    PlaceholderImage(
                        description = ""
                    )
                })
                Spacer(modifier = Modifier.height(12.dp))
                LoadMoreItem()
            }
        }
    }
}