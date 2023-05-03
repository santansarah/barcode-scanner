package com.santansarah.barcodescanner.ui.productdetail.sections

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.with
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.santansarah.barcodescanner.ui.components.darkGrayShimmer
import com.santansarah.barcodescanner.ui.components.loadingBrush
import com.santansarah.barcodescanner.ui.theme.BarcodeScannerTheme

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun productLoadingTransitionSpec(): AnimatedContentScope<Boolean>.() -> ContentTransform =
    {
        slideInVertically(initialOffsetY = {-500},
            animationSpec = tween(150, 0)) with
                fadeOut(animationSpec = tween(150)) using
                SizeTransform { initialSize, targetSize ->
                    keyframes {
                        IntSize(initialSize.width, targetSize.height) at 150
                        durationMillis = 300
                    }
                }
    }


@OptIn(ExperimentalTextApi::class)
@Preview(showBackground = true)
@Composable
fun PreviewLoadingText() {
    BarcodeScannerTheme {
        Text(
            modifier = Modifier
                .padding(6.dp),
            text = "Loading",
            style = TextStyle(
                brush = loadingBrush(fontSize = 14.sp, darkGrayShimmer),
                fontSize = 14.sp
            )
        )
    }
}