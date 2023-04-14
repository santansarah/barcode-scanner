package com.santansarah.barcodescanner.ui.productdetail

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.santansarah.barcodescanner.R

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
                        colors = listOf(
                            Color(0xFFdfe2de),
                            Color(0xFFcacfc9),
                            Color(0xFFdfe2de),
                        ),
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
            }
            .width(100.dp)
            .height(140.dp)
            .border(2.dp, Color(0xFFdfe2de)),
        contentDescription = null
    )
}
