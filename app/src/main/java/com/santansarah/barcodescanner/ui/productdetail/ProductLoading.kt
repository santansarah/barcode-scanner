package com.santansarah.barcodescanner.ui.productdetail

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.santansarah.barcodescanner.R
import com.santansarah.barcodescanner.ui.search.SearchLoadingScreen
import com.santansarah.barcodescanner.ui.theme.BarcodeScannerTheme
import com.santansarah.barcodescanner.ui.theme.brightYellow
import com.santansarah.barcodescanner.ui.theme.darkBackground

/*
@OptIn(ExperimentalTextApi::class)
@Composable
fun ProductLoading(
    padding: PaddingValues
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
                Text(
                    modifier = Modifier
                        .padding(6.dp),
                    text = "",
                    style = MaterialTheme.typography.titleLarge
                )
            }
            Divider(thickness = 2.dp, color = Color.DarkGray)

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 40.dp)
            ) {
                Image(
                    modifier = Modifier.size(100.dp),
                    painter = painterResource(id = R.drawable.strawberry),
                    contentDescription = "Cherries"
                )
                Image(
                    modifier = Modifier.size(100.dp),
                    painter = painterResource(id = R.drawable.icecream),
                    contentDescription = "Banana"
                )
                Image(
                    modifier = Modifier.size(100.dp),
                    painter = painterResource(id = R.drawable.cookie),
                    contentDescription = "Apple"
                )
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier
                        .padding(8.dp),
                    text = "Loading",
                    style = TextStyle(
                        brush = brush,
                        fontSize = 36.sp
                    ),
                    textAlign = TextAlign.Center
                )
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
                    contentDescription = "Cherries"
                )
                Image(
                    modifier = Modifier.size(100.dp),
                    painter = painterResource(id = R.drawable.milkshake),
                    contentDescription = "Banana"
                )
                Image(
                    modifier = Modifier.size(100.dp),
                    painter = painterResource(id = R.drawable.fries),
                    contentDescription = "Apple"
                )
            }

            */
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
            }*//*

        }

    }
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
*/
