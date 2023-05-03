package com.santansarah.barcodescanner.ui.search.imageanimations

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.santansarah.barcodescanner.ui.theme.BarcodeScannerTheme
import kotlinx.coroutines.delay

@Composable
fun LESearchImagesAnimations(
    key: Boolean
) {
    LaunchedEffect(key) {

        if (!key) {
            val animatedImages = AnimatedSearchImages
                .imageList.sortedBy {
                    it.animateOrder
                }

            repeat(3) {
                animatedImages.forEach { img ->
                    AnimatedSearchImages.scaleImage(
                        onValueChanged = {
                            AnimatedSearchImages.updateAnimatedSearchScale(
                                img.imageId, it
                            )
                        }
                    )
                    delay(500)
                }
            }
        }

    }
}

@Composable
fun FoodImage(
    imageId: Int,
    imageDescription: String,
    imageScale: Float,
    error: Boolean
) {

    val iconBackgroundColor by animateFloatAsState(
        targetValue = if (!error) 1f else 0f, label = imageDescription + "AnimateFloat",
        animationSpec = tween(
            1000,
            easing = LinearOutSlowInEasing
        ),
    )

    val matrix = ColorMatrix()
    matrix.setToSaturation(iconBackgroundColor)

    Image(
        modifier = Modifier
            .size(100.dp)
            .scale(imageScale),
        painter = painterResource(id = imageId),
        contentDescription = imageDescription,
        colorFilter = ColorFilter.colorMatrix(matrix)
    )
}


@Composable
fun AnimatedSearchImageRow(
    error: Boolean,
    images: List<AnimatedImages>) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 40.dp)
    ) {
        images.forEach { img ->
            FoodImage(
                imageId = img.imageId,
                imageDescription = img.imageDescription,
                imageScale = img.currentScale.value,
                error = error
            )
        }
    }
}

@Preview
@Composable
fun PreviewLoadingImages() {

    BarcodeScannerTheme {

        Column {
            LESearchImagesAnimations(false)
            AnimatedSearchImageRow(false, AnimatedSearchImages.imageList.take(3))
            AnimatedSearchImageRow(false, AnimatedSearchImages.imageList.takeLast(3))
        }

    }

}