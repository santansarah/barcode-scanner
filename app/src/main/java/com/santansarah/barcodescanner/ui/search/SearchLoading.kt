package com.santansarah.barcodescanner.ui.search

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.santansarah.barcodescanner.R
import com.santansarah.barcodescanner.domain.ErrorCode
import com.santansarah.barcodescanner.ui.components.AnimateColor
import com.santansarah.barcodescanner.ui.components.SearchTextField
import com.santansarah.barcodescanner.ui.theme.BarcodeScannerTheme
import com.santansarah.barcodescanner.ui.theme.brightYellow
import com.santansarah.barcodescanner.ui.theme.darkBackground
import com.santansarah.barcodescanner.ui.theme.gray
import com.santansarah.barcodescanner.ui.theme.redishMagenta

@OptIn(ExperimentalTextApi::class)
@Composable
fun SearchLoadingScreen(
    padding: PaddingValues,
    searchText: String,
    error: Boolean,
    onSearchValueChanged: (String) -> Unit,
    onSearch: () -> Unit
) {

    val currentFontSizePx = with(LocalDensity.current) { 36.sp.toPx() }
    val currentFontSizeDoublePx = currentFontSizePx * 2

    val infiniteTransition = rememberInfiniteTransition(label = "")
    val offset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = currentFontSizeDoublePx,
        animationSpec = infiniteRepeatable(
            tween(
                1000,
                easing = LinearEasing
            )
        ), label = ""
    )

    val brush = Brush.linearGradient(
        colors = listOf(
            Color(0xFFfe0033),
            Color(0xFFb80025),
            Color(0xFFfe0033),
        ),
        start = Offset(offset, offset),
        end = Offset(offset + currentFontSizePx, offset + currentFontSizePx),
        tileMode = TileMode.Mirror
    )

    Column(
        modifier = Modifier
            .padding(padding)
            //.padding(top = 20.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {

        //Divider(thickness = 2.dp, color = Color.DarkGray)
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
                val header = if (error) "Try again" else
                    "Processing..."
                Text(
                    modifier = Modifier
                        .padding(6.dp),
                    text = header,
                    style = MaterialTheme.typography.titleLarge
                )
            }
            Divider(thickness = 2.dp, color = Color.DarkGray)

            val iconBackgroundColor by animateFloatAsState(
                targetValue = if (!error) 1f else 0f, label = "",
                animationSpec = tween(1000,
                    easing = LinearOutSlowInEasing),
            )

            val matrix = ColorMatrix()
            matrix.setToSaturation(iconBackgroundColor)

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 40.dp)
            ) {
                Image(
                    modifier = Modifier.size(100.dp),
                    painter = painterResource(id = R.drawable.strawberry),
                    contentDescription = "Cherries",
                    colorFilter = ColorFilter.colorMatrix(matrix)
                )
                Image(
                    modifier = Modifier.size(100.dp),
                    painter = painterResource(id = R.drawable.icecream),
                    contentDescription = "Banana",
                    colorFilter = ColorFilter.colorMatrix(matrix)
                )
                Image(
                    modifier = Modifier.size(100.dp),
                    painter = painterResource(id = R.drawable.cookie),
                    contentDescription = "Apple",
                    colorFilter = ColorFilter.colorMatrix(matrix)
                )
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (!error) {
                    Text(
                        modifier = Modifier
                            .padding(8.dp),
                        text = "Finding products that match:",
                        style = TextStyle(
                            brush = brush,
                            fontSize = 36.sp
                        ),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        modifier = Modifier
                            .padding(8.dp),
                        text = searchText,
                        style = MaterialTheme.typography.headlineMedium,
                        textAlign = TextAlign.Center
                    )
                } else {
                    Text(
                        modifier = Modifier
                            .padding(bottom=14.dp),
                        text = "Your search is taking longer than expected. " +
                                "Try again, or use more specific keywords.",
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

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 40.dp)
            ) {
                Image(
                    modifier = Modifier.size(100.dp),
                    painter = painterResource(id = R.drawable.bread),
                    contentDescription = "Cherries",
                    colorFilter = ColorFilter.colorMatrix(matrix)
                )
                Image(
                    modifier = Modifier.size(100.dp),
                    painter = painterResource(id = R.drawable.milkshake),
                    contentDescription = "Banana",
                    colorFilter = ColorFilter.colorMatrix(matrix)
                )
                Image(
                    modifier = Modifier.size(100.dp),
                    painter = painterResource(id = R.drawable.fries),
                    contentDescription = "Apple",
                    colorFilter = ColorFilter.colorMatrix(matrix)
                )
            }

            /*Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 40.dp)
            ) {
                Image(
                    modifier = Modifier.size(100.dp),
                    painter = painterResource(id = R.drawable.pretzel),
                    contentDescription = "Cherries"
                )
                Image(
                    modifier = Modifier.size(100.dp),
                    painter = painterResource(id = R.drawable.corn),
                    contentDescription = "Banana"
                )
                Image(
                    modifier = Modifier.size(100.dp),
                    painter = painterResource(id = R.drawable.burger),
                    contentDescription = "Apple"
                )
            }*/
        }

    }
}

@Preview
@Composable
fun PreviewSearchLoading() {

    BarcodeScannerTheme {
        Surface(
            color = darkBackground
        ) {
            SearchLoadingScreen(
                padding = PaddingValues(),
                searchText = "keto bomb",
                error = false,
                {},{}
            )
        }
    }

}

@Preview
@Composable
fun PreviewSearchNotFound() {

    BarcodeScannerTheme {
        Surface(
            color = darkBackground
        ) {
            SearchLoadingScreen(
                padding = PaddingValues(),
                searchText = "keto bomb",
                error = true,
                {},{}
            )
        }
    }

}
