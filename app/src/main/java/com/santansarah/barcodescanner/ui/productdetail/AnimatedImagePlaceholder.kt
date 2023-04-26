package com.santansarah.barcodescanner.ui.productdetail

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.santansarah.barcodescanner.R
import com.santansarah.barcodescanner.ui.components.lightGrayShimmer
import com.santansarah.barcodescanner.ui.theme.BarcodeScannerTheme

@Composable
fun AnimatedFoodIcon() {
    val transition = rememberInfiniteTransition(label = "")

    val progress by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1300f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = ""
    )

    Icon(
        painterResource(id = R.drawable.food_placholder),
        modifier = Modifier
            .drawWithContent {
                with(drawContext.canvas.nativeCanvas) {
                    val checkPoint = saveLayer(null, null)
                    val brush = Brush.linearGradient(
                        lightGrayShimmer,
                        start = Offset.Zero,
                        end = Offset(progress, progress)

                    )

                    // Destination
                    drawContent()

                    // Source
                    drawRect(
                        brush = brush,
                        blendMode = BlendMode.SrcIn
                    )

                    restoreToCount(checkPoint)

                }
            },
        contentDescription = null
    )
}


@Preview(showBackground = true)
@Composable
fun PreviewAnimatedSearchImageLoading() {
    BarcodeScannerTheme {
        AnimatedFoodIcon()
    }
}
