package com.santansarah.barcodescanner.ui.productdetail.sections

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.with
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.santansarah.barcodescanner.data.remote.Product
import com.santansarah.barcodescanner.ui.components.loadingBrush
import com.santansarah.barcodescanner.ui.theme.brightYellow


@OptIn(ExperimentalAnimationApi::class, ExperimentalTextApi::class)
@Composable
fun ProductIngredients(product: Product?, isLoading: Boolean) {

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

        AnimatedContent(
            targetState = isLoading,
            transitionSpec = {
                fadeIn(animationSpec = tween(150, 150)) with
                        fadeOut(animationSpec = tween(150)) using
                        SizeTransform { initialSize, targetSize ->
                            keyframes {
                                IntSize(initialSize.width, targetSize.height) at 150
                                durationMillis = 300
                            }
                        }
            }, label = ""
        ) { isLoading ->
            if (isLoading) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 6.dp, vertical = 12.dp),
                    text = "Loading",
                    style = TextStyle(
                        brush = loadingBrush(fontSize = 16.sp),
                        fontSize = 16.sp
                    )
                )
            } else {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 6.dp, vertical = 12.dp),
                    text = product?.ingredients ?: "Missing"
                )
            }
        }
    }
}