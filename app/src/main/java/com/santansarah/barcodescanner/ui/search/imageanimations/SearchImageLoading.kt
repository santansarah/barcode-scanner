package com.santansarah.barcodescanner.ui.search.imageanimations

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.with
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.santansarah.barcodescanner.R
import com.santansarah.barcodescanner.ui.components.AnimateColor
import com.santansarah.barcodescanner.ui.theme.BarcodeScannerTheme
import com.santansarah.barcodescanner.ui.theme.gray
import com.santansarah.barcodescanner.ui.theme.redishMagenta


@Composable
fun ColorChangingImageLoading() {
    Image(
        modifier = Modifier
            .height(140.dp)
            .width(100.dp)
            .border(2.dp, Color(0xFFcacfc9)),
        painter = painterResource(id = R.drawable.food_placholder),
        contentDescription = "Loading Image",
        colorFilter = ColorFilter.tint(
            AnimateColor(
                startColor = gray,
                endColor = redishMagenta,
                duration = 500
            )
        )
    )
}

@Composable
@OptIn(ExperimentalAnimationApi::class)
fun searchImageLoadingTransition(): AnimatedContentScope<Boolean>.() -> ContentTransform =
    {
        slideInVertically(
            initialOffsetY = { -it },
            animationSpec = spring(Spring.DampingRatioMediumBouncy)
        ) with
                slideOutVertically(
                    targetOffsetY = { it },
                    animationSpec = spring(Spring.StiffnessHigh)
                ) + fadeOut()
    }
        /*slideInHorizontally(
            initialOffsetX = { -300 },
            animationSpec = tween(durationMillis = 300)
        ) with
                slideOutHorizontally(
                    targetOffsetX = { 300 },
                    animationSpec = tween(durationMillis = 300)
                ) + fadeOut()
    }*/

@Preview(showBackground = true)
@Composable
fun PreviewSearchImageLoading() {
    BarcodeScannerTheme {
        ColorChangingImageLoading()
    }

}

@OptIn(ExperimentalAnimationApi::class)
@Preview(showBackground = true)
@Composable
fun PreviewImageSlide() {
    BarcodeScannerTheme {
        var showLoadingAnimation by
        remember { mutableStateOf(true) }

        AnimatedContent(
            targetState = showLoadingAnimation,
            transitionSpec = searchImageLoadingTransition(),
            label = "searchLoadingPreview"
        ) { isLoading ->
            if (isLoading)
                ColorChangingImageLoading()
            else
                Image(
                    modifier = Modifier
                        .height(140.dp)
                        .width(100.dp)
                        .border(2.dp, Color(0xFFcacfc9)),
                    painter = painterResource(id = R.drawable.mock_image),
                    contentDescription = "Corn Chips",
                )
        }

    }
}