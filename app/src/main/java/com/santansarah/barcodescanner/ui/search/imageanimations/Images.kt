package com.santansarah.barcodescanner.ui.search.imageanimations

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.santansarah.barcodescanner.R
import com.santansarah.barcodescanner.ui.theme.BarcodeScannerTheme
import timber.log.Timber

data class AnimatedImages(
    val imageId: Int,
    val imageDescription: String,
    val animateOrder: Int? = null,
    var currentScale: MutableState<Float> = mutableStateOf(1f)
)

object AnimatedSearchImages {
    val imageList = listOf(
        AnimatedImages(R.drawable.strawberry, "Strawberries", 6),
        AnimatedImages(R.drawable.icecream, "Ice Cream", 1),
        AnimatedImages(R.drawable.cookie, "Cookie", 4),
        AnimatedImages(R.drawable.bread, "Bread", 3),
        AnimatedImages(R.drawable.milkshake, "Milkshake", 5),
        AnimatedImages(R.drawable.fries, "Fries", 2),
    )

    fun updateAnimatedSearchScale(imageId: Int, newScale: Float) {
        Timber.d("newscale: $newScale")
        imageList.find { it.imageId == imageId }?.currentScale?.value = newScale
    }

    suspend fun scaleImage(
        onValueChanged: (Float) -> Unit,
    ) {
        animate(
            initialValue = 1f, targetValue = 1.5f,
            animationSpec = tween(durationMillis = 500)
        )
        { value: Float, _: Float ->
            onValueChanged(value)
        }
        animate(
            initialValue = 1.5f, targetValue = 1f,
            animationSpec = tween(durationMillis = 500)
        )
        { value: Float, _: Float ->
            onValueChanged(value)
        }
    }
}
