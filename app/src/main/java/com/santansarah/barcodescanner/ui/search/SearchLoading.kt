package com.santansarah.barcodescanner.ui.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.santansarah.barcodescanner.domain.ErrorCode
import com.santansarah.barcodescanner.ui.search.imageanimations.AnimatedSearchImages
import com.santansarah.barcodescanner.ui.components.SearchTextField
import com.santansarah.barcodescanner.ui.components.searchLoadingBrush
import com.santansarah.barcodescanner.ui.previewparams.SearchParams
import com.santansarah.barcodescanner.ui.previewparams.SearchResultsFeature
import com.santansarah.barcodescanner.ui.search.imageanimations.AnimatedSearchImageRow
import com.santansarah.barcodescanner.ui.search.imageanimations.LESearchImagesAnimations
import com.santansarah.barcodescanner.ui.theme.BarcodeScannerTheme
import com.santansarah.barcodescanner.ui.theme.brightYellow
import com.santansarah.barcodescanner.ui.theme.darkBackground
import com.santansarah.barcodescanner.ui.theme.redishMagenta
import kotlinx.coroutines.flow.flowOf


/**
 * The search loading screen shows:
 * 1. Animations, if everything is going well
 * 2. The error message, if there's a problem with the API.
 */
@Composable
fun SearchLoadingScreen(
    padding: PaddingValues,
    searchText: String,
    initialLoadErrorMessage: String?,
    onSearchValueChanged: (String) -> Unit,
    onSearch: () -> Unit
) {

    Column(
        modifier = Modifier
            .padding(padding)
            //.padding(top = 20.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {

        LESearchImagesAnimations((initialLoadErrorMessage != null))

        ElevatedCard(
            modifier = Modifier
                .fillMaxSize(),
            shape = RectangleShape
        ) {

            Column(
                modifier = Modifier
                    .background(brightYellow)
                    .fillMaxWidth()
            ) {

                /**
                 * If there's an error, the header message is updated to 'Try again'.
                 */
                val header = initialLoadErrorMessage?.let{ "Try again" } ?: "Processing..."
                Text(
                    modifier = Modifier
                        .padding(6.dp),
                    text = header,
                    style = MaterialTheme.typography.titleLarge
                )
            }
            Divider(thickness = 2.dp, color = Color.DarkGray)

            /**
             * The animated icons also need to know if there's an error. If there is, then the
             * icons will do a color shift to black & white.
             */
            AnimatedSearchImageRow((initialLoadErrorMessage != null), AnimatedSearchImages.imageList.take(3))

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                /**
                 * If there's no error, then here I show the Loading shimmer.
                 */
                if (initialLoadErrorMessage == null) {
                    SearchLoadingShimmerText()
                    Text(
                        modifier = Modifier
                            .padding(8.dp),
                        text = searchText,
                        style = MaterialTheme.typography.headlineMedium,
                        textAlign = TextAlign.Center
                    )
                } else {
                    /**
                     * Otherwise, it displays the ENUM ErrorCode message that we got from the
                     * PagingSource.
                     */
                    Text(
                        modifier = Modifier
                            .padding(bottom = 14.dp),
                        text = initialLoadErrorMessage,
                        style = TextStyle(
                            color = redishMagenta,
                            fontSize = 28.sp
                        ),
                        textAlign = TextAlign.Center
                    )

                    SearchTextField(
                        searchText = searchText,
                        onValueChanged = onSearchValueChanged,
                        onSearchWithText = null,
                        onSearch = onSearch
                    )
                }

            }

            AnimatedSearchImageRow((initialLoadErrorMessage != null), AnimatedSearchImages.imageList.takeLast(3))

        }

    }
}

@OptIn(ExperimentalTextApi::class)
@Composable
fun SearchLoadingShimmerText() {
    Text(
        modifier = Modifier
            .padding(8.dp),
        text = "Finding products that match:",
        style = TextStyle(
            brush = searchLoadingBrush(),
            fontSize = 36.sp
        ),
        textAlign = TextAlign.Center
    )

}

// See search screen for other previews.
@Preview
@Composable
fun PreviewSearchLoadingShimmerText() {
    BarcodeScannerTheme {
        SearchLoadingShimmerText()
    }
}
