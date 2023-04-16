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
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.santansarah.barcodescanner.data.remote.Nutriments
import com.santansarah.barcodescanner.data.remote.Product
import com.santansarah.barcodescanner.ui.components.TableCell
import com.santansarah.barcodescanner.ui.productdetail.loadingBrush
import com.santansarah.barcodescanner.ui.theme.brightYellow
import com.santansarah.barcodescanner.utils.toMgs
import com.santansarah.barcodescanner.utils.valueOrZero

@OptIn(ExperimentalTextApi::class, ExperimentalAnimationApi::class)
@Composable
fun NutritionData(
    product: Product?,
    isLoading: Boolean
) {
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
                text = "Nutrition Data",
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
                        .padding(6.dp),
                    text = "Loading",
                    style = TextStyle(
                        brush = loadingBrush(fontSize = 14.sp),
                        fontSize = 14.sp
                    )
                )
            } else {
                Text(
                    modifier = Modifier
                        .padding(6.dp),
                    text = "Serving Size: " + (product?.servingSize ?: "Unknown"),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        NutritionData(
            nutriments = product?.nutriments,
            servingSize = product?.servingSize ?: "Unknown"
        )

    }
}

@Composable
fun NutritionData(
    nutriments: Nutriments?,
    servingSize: String
) {
    // Each cell of a column must have the same weight.
    val column1Weight = .6f
    val column2Weight = .4f
    val normalPadding = PaddingValues(8.dp)
    val indentedPadding = PaddingValues(start = 16.dp, end = 8.dp, top = 8.dp, bottom = 8.dp)
    // The LazyColumn will be our table. Notice the use of the weights below
    Column(
        Modifier
            .fillMaxSize()
            .padding(6.dp)
    ) {

        Row(Modifier.fillMaxWidth()) {
            TableCell("Calories", weight = column1Weight, normalPadding, TextAlign.Start)
            TableCell(
                text = nutriments?.calories.valueOrZero(),
                weight = column2Weight, normalPadding, TextAlign.End
            )
        }

        TableRow(
            column1Weight = column1Weight,
            normalPadding = normalPadding,
            heading = "Cholesterol",
            value = nutriments?.cholesterol.toMgs() + "mg",
            column2Weight = column2Weight,
        )
        TableRow(
            column1Weight = column1Weight,
            normalPadding = normalPadding,
            heading = "Fat",
            value = nutriments?.fat.valueOrZero() + "g",
            column2Weight = column2Weight,
        )
        TableRow(
            column1Weight = column1Weight,
            normalPadding = indentedPadding,
            heading = "Saturated",
            value = nutriments?.saturatedFat.valueOrZero() + "g",
            column2Weight = column2Weight,
        )
        TableRow(
            column1Weight = column1Weight,
            normalPadding = indentedPadding,
            heading = "Monounsaturated",
            value = nutriments?.monounsaturatedFat.valueOrZero() + "g",
            column2Weight = column2Weight,
        )
        TableRow(
            column1Weight = column1Weight,
            normalPadding = indentedPadding,
            heading = "Polyunsaturated",
            value = nutriments?.polyunsaturatedFat.valueOrZero() + "g",
            column2Weight = column2Weight,
        )
        TableRow(
            column1Weight = column1Weight,
            normalPadding = normalPadding,
            heading = "Sodium",
            value = nutriments?.sodium.toMgs() + "mg",
            column2Weight = column2Weight,
        )
        TableRow(
            column1Weight = column1Weight,
            normalPadding = normalPadding,
            heading = "Potassium",
            value = nutriments?.potassium.toMgs() + "mg",
            column2Weight = column2Weight,
        )
        TableRow(
            column1Weight = column1Weight,
            normalPadding = normalPadding,
            heading = "Carbohydrates",
            value = nutriments?.carbohydrates.valueOrZero() + "g",
            column2Weight = column2Weight,
        )
        TableRow(
            column1Weight = column1Weight,
            normalPadding = indentedPadding,
            heading = "Fiber",
            value = "${nutriments?.fiber.valueOrZero()}g",
            column2Weight = column2Weight,
        )
        TableRow(
            column1Weight = column1Weight,
            normalPadding = indentedPadding,
            heading = "Sugars",
            value = nutriments?.sugar.valueOrZero() + "g",
            column2Weight = column2Weight,
        )
        TableRow(
            column1Weight = column1Weight,
            normalPadding = normalPadding,
            heading = "Protein",
            value = nutriments?.protein.valueOrZero() + "g",
            column2Weight = column2Weight,
        )
    }
}

@Composable
private fun TableRow(
    column1Weight: Float,
    normalPadding: PaddingValues,
    heading: String,
    value: String,
    column2Weight: Float
) {
    Row(Modifier.fillMaxWidth()) {
        TableCell(heading, weight = column1Weight, normalPadding, TextAlign.Start)
        TableCell(
            text = value,
            weight = column2Weight, normalPadding, TextAlign.End
        )
    }
}