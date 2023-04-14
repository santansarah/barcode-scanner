package com.santansarah.barcodescanner.ui.productdetail

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.santansarah.barcodescanner.R
import com.santansarah.barcodescanner.ui.search.SearchLoadingScreen
import com.santansarah.barcodescanner.ui.theme.BarcodeScannerTheme
import com.santansarah.barcodescanner.ui.theme.brightYellow
import com.santansarah.barcodescanner.ui.theme.darkBackground
import com.santansarah.barcodescanner.ui.theme.gray
import com.santansarah.barcodescanner.ui.theme.lightGray
import com.santansarah.barcodescanner.ui.theme.lightShader
import com.santansarah.barcodescanner.ui.theme.lightestGray

@OptIn(ExperimentalTextApi::class)
@Composable
fun ProductLoading(
    padding: PaddingValues
) {

    val fontSize = 24.sp
    val currentFontSizePx = with(LocalDensity.current) { fontSize.toPx() }
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
            gray,
            lightGray,
            gray,
        ),
        start = Offset(0f, offset),
        end = Offset(offset + currentFontSizePx, offset + currentFontSizePx),
        tileMode = TileMode.Mirror
    )

    val reverseBrush = Brush.linearGradient(
        colors = listOf(
            gray,
            lightGray,
            gray,
        ),
        start = Offset(0f, currentFontSizeDoublePx),
        end = Offset(offset + currentFontSizePx, offset),
        tileMode = TileMode.Mirror
    )

    Column(
        modifier = Modifier
            .padding(padding)
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {

        ElevatedCard(
            shape = RectangleShape
        ) {

            Column(
                modifier = Modifier
                    .background(brightYellow)
                    .fillMaxWidth()
            ) {
                Text(
                    modifier = Modifier
                        .padding(6.dp),
                    text = "Product Images",
                    style = MaterialTheme.typography.titleLarge
                )
            }
            Divider(thickness = 2.dp, color = Color.DarkGray)

            LoadingDetails(brush, fontSize)
        }

        Spacer(modifier = Modifier.height(20.dp))

        Divider(thickness = 2.dp, color = Color.DarkGray)
        ElevatedCard(
            shape = RectangleShape
        ) {

            Column(
                modifier = Modifier
                    .background(brightYellow)
                    .fillMaxWidth()
            ) {
                Text(
                    modifier = Modifier
                        .padding(6.dp),
                    text = "Ingredients",
                    style = MaterialTheme.typography.titleLarge
                )
            }
            Divider(thickness = 2.dp, color = Color.DarkGray)

            LoadingDetails(reverseBrush, fontSize)

        }

        Spacer(modifier = Modifier.height(20.dp))

        Divider(thickness = 2.dp, color = Color.DarkGray)
        ElevatedCard(
            shape = RectangleShape
        ) {

            Column(
                modifier = Modifier
                    .background(brightYellow)
                    .fillMaxWidth()
            ) {
                Text(
                    modifier = Modifier
                        .padding(6.dp),
                    text = "Nutrition Data",
                    style = MaterialTheme.typography.titleLarge
                )
            }
            Divider(thickness = 2.dp, color = Color.DarkGray)
            Text(
                modifier = Modifier
                    .padding(6.dp),
                text = "Serving Size: Unknown",
                style = MaterialTheme.typography.bodyMedium
            )

            LoadingDetails(brush, fontSize)

        }
        Divider(thickness = 2.dp, color = Color.DarkGray)

    }
}

@Composable
@OptIn(ExperimentalTextApi::class)
private fun LoadingDetails(
    brush: Brush,
    fontSize: TextUnit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            modifier = Modifier
                .padding(8.dp),
            text = "Loading",
            style = TextStyle(
                brush = brush,
                fontSize = fontSize
            ),
            textAlign = TextAlign.Center
        )
    }
}

@Preview
@Composable
fun BrushTest() {

    val reverseBrush = Brush.linearGradient(
        colors = listOf(
            darkBackground,
            gray,
            brightYellow,
        ),
        start = Offset(800f, 200f),
        end = Offset(0f, 600f),
    )

Box(modifier = Modifier.size(200.dp, 100.dp)
    .background(reverseBrush))

}

@Preview
@Composable
fun PreviewProductLoading() {

    BarcodeScannerTheme {
        Surface(
            color = darkBackground
        ) {
            ProductLoading(padding = PaddingValues())
        }
    }

}
