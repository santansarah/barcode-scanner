package com.santansarah.barcodescanner.ui.productdetail.sections

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.with
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.IntSize

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun productLoadingTransitionSpec(): AnimatedContentScope<Boolean>.() -> ContentTransform =
    {
        fadeIn(animationSpec = tween(150, 150)) with
                fadeOut(animationSpec = tween(150)) using
                SizeTransform { initialSize, targetSize ->
                    keyframes {
                        IntSize(initialSize.width, targetSize.height) at 150
                        durationMillis = 300
                    }
                }
    }