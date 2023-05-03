package com.santansarah.barcodescanner.ui.components

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.santansarah.barcodescanner.ui.theme.gray
import com.santansarah.barcodescanner.ui.theme.lightGray
import com.santansarah.barcodescanner.ui.theme.lightestGray
import com.santansarah.barcodescanner.ui.theme.redishMagenta

val lightGrayShimmer = listOf(
    lightestGray,
    lightGray,
    lightestGray,
    )

val darkGrayShimmer = listOf(
    gray,
    lightGray,
    gray,
)

val redShimmer = listOf(
    redishMagenta,
    Color(0xFFa30021),
    Color(0xFFad0023),
    Color(0xFFb80025),
    redishMagenta
)

@Composable
fun loadingBrush(fontSize: TextUnit,
                 colors: List<Color>): Brush {
    val currentFontSizePx = with(LocalDensity.current) { fontSize.toPx() }
    val currentFontSizeDoublePx = currentFontSizePx * 2

    val infiniteTransition = rememberInfiniteTransition(label = "LoadingBrush")
    val offset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = currentFontSizeDoublePx,
        animationSpec = infiniteRepeatable(
            tween(
                1000,
                easing = LinearEasing
            )
        ), label = "LoadingAnimation"
    )

    return Brush.linearGradient(
        colors = colors,
        start = Offset(0f, offset),
        end = Offset(offset + currentFontSizePx, offset + currentFontSizePx),
        tileMode = TileMode.Mirror
    )
}

@Composable
fun searchLoadingBrush(): Brush {

    val currentFontSizePx = with(LocalDensity.current) { 36.sp.toPx() }
    val currentFontSizeDoublePx = currentFontSizePx * 2

    val infiniteTransition = rememberInfiniteTransition(label = "SearchTextBrush")
    val offset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = currentFontSizeDoublePx,
        animationSpec = infiniteRepeatable(
            tween(
                1000,
                easing = LinearOutSlowInEasing
            )
        ), label = "SearchTextAnimation"
    )

    return Brush.linearGradient(
        colors = redShimmer,
        start = Offset(offset, offset),
        end = Offset(offset + currentFontSizePx, offset + currentFontSizePx),
        tileMode = TileMode.Mirror
    )

}
