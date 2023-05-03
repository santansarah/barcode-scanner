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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.santansarah.barcodescanner.data.remote.Nutriments
import com.santansarah.barcodescanner.data.remote.Product
import com.santansarah.barcodescanner.data.remote.formatToGrams
import com.santansarah.barcodescanner.data.remote.formatToMgs
import com.santansarah.barcodescanner.ui.components.TableCell
import com.santansarah.barcodescanner.ui.components.TableRow
import com.santansarah.barcodescanner.ui.components.darkGrayShimmer
import com.santansarah.barcodescanner.ui.components.loadingBrush
import com.santansarah.barcodescanner.ui.previewparams.ProductDetailParams
import com.santansarah.barcodescanner.ui.previewparams.ProductDetails
import com.santansarah.barcodescanner.ui.theme.BarcodeScannerTheme
import com.santansarah.barcodescanner.ui.theme.brightYellow

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
            transitionSpec = productLoadingTransitionSpec(),
            label = "LoadingNutrition"
        ) { isLoading ->
            if (isLoading) {
                Text(
                    modifier = Modifier
                        .padding(6.dp),
                    text = "Loading",
                    style = TextStyle(
                        brush = loadingBrush(fontSize = 14.sp, darkGrayShimmer),
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

        NutritionDetails(
            nutriments = product?.nutriments,
            servingSize = product?.servingSize ?: "Unknown"
        )

    }
}

@Composable
fun NutritionDetails(
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
                text = nutriments?.calories.formatToGrams(),
                weight = column2Weight, normalPadding, TextAlign.End
            )
        }

        TableRow(
            column1Weight = column1Weight,
            normalPadding = normalPadding,
            heading = "Cholesterol",
            value = nutriments?.cholesterol.formatToMgs() + "mg",
            column2Weight = column2Weight,
        )
        TableRow(
            column1Weight = column1Weight,
            normalPadding = normalPadding,
            heading = "Fat",
            value = nutriments?.fat.formatToGrams() + "g",
            column2Weight = column2Weight,
        )
        TableRow(
            column1Weight = column1Weight,
            normalPadding = indentedPadding,
            heading = "Saturated",
            value = nutriments?.saturatedFat.formatToGrams() + "g",
            column2Weight = column2Weight,
        )
        TableRow(
            column1Weight = column1Weight,
            normalPadding = indentedPadding,
            heading = "Monounsaturated",
            value = nutriments?.monounsaturatedFat.formatToGrams() + "g",
            column2Weight = column2Weight,
        )
        TableRow(
            column1Weight = column1Weight,
            normalPadding = indentedPadding,
            heading = "Polyunsaturated",
            value = nutriments?.polyunsaturatedFat.formatToGrams() + "g",
            column2Weight = column2Weight,
        )
        TableRow(
            column1Weight = column1Weight,
            normalPadding = normalPadding,
            heading = "Sodium",
            value = nutriments?.sodium.formatToMgs() + "mg",
            column2Weight = column2Weight,
        )
        TableRow(
            column1Weight = column1Weight,
            normalPadding = normalPadding,
            heading = "Potassium",
            value = nutriments?.potassium.formatToMgs() + "mg",
            column2Weight = column2Weight,
        )
        TableRow(
            column1Weight = column1Weight,
            normalPadding = normalPadding,
            heading = "Carbohydrates",
            value = nutriments?.carbohydrates.formatToGrams() + "g",
            column2Weight = column2Weight,
        )
        TableRow(
            column1Weight = column1Weight,
            normalPadding = indentedPadding,
            heading = "Fiber",
            value = "${nutriments?.fiber.formatToGrams()}g",
            column2Weight = column2Weight,
        )
        TableRow(
            column1Weight = column1Weight,
            normalPadding = indentedPadding,
            heading = "Sugars",
            value = nutriments?.sugar.formatToGrams() + "g",
            column2Weight = column2Weight,
        )
        TableRow(
            column1Weight = column1Weight,
            normalPadding = normalPadding,
            heading = "Protein",
            value = nutriments?.protein.formatToGrams() + "g",
            column2Weight = column2Weight,
        )
    }
}


@Preview
@Composable
fun PreviewNutritionData(
    @PreviewParameter(ProductDetailParams::class) featureParams: ProductDetails
) {
    BarcodeScannerTheme {
        NutritionData(
            product = featureParams.product,
            isLoading = featureParams.isLoading
        )
    }
}