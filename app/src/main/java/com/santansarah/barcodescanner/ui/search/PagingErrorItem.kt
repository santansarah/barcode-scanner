package com.santansarah.barcodescanner.ui.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.santansarah.barcodescanner.data.remote.SearchResults
import com.santansarah.barcodescanner.data.remote.mock.searchResults
import com.santansarah.barcodescanner.domain.ErrorCode
import com.santansarah.barcodescanner.ui.components.PlaceholderImage
import com.santansarah.barcodescanner.ui.theme.BarcodeScannerTheme
import com.santansarah.barcodescanner.ui.theme.darkBackground
import com.santansarah.barcodescanner.ui.theme.lightGray
import com.santansarah.barcodescanner.ui.theme.redishMagenta
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

@Composable
fun PagingAppendErrorItem(
    appendErrorMessage: String,
    onRetry: () -> Unit
) {

    /**
     * At the end of the original results, this gray card appears
     * if there's an error. The whole thing is clickable, so users
     * can just tap, and Paging 3 will try again, based on the
     * current page key.
     */
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onRetry()
            },
        shape = RectangleShape,
        colors = CardDefaults.elevatedCardColors(
            containerColor = lightGray.copy(.5f)
        )
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp, horizontal = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(.9f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier
                        .padding(end = 10.dp)
                        .size(38.dp),
                    imageVector = Icons.Outlined.Info,
                    contentDescription = "Error Icon",
                    tint = redishMagenta
                )
                Text(
                    text = "$appendErrorMessage Tap to try again.",
                    style = MaterialTheme.typography.titleMedium,
                    color = redishMagenta
                )
            }
        }
    }
    Spacer(modifier = Modifier.height(20.dp))

}

@Preview(showBackground = true)
@Composable
fun PreviewPagingErrorItem() {

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
                PagingAppendErrorItem(ErrorCode.API_SEARCH_TIMEOUT.message, {})
            }
        }
    }
}