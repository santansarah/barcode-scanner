package com.santansarah.barcodescanner.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.santansarah.barcodescanner.data.remote.Nutriments
import com.santansarah.barcodescanner.ui.components.TableCell
import com.santansarah.barcodescanner.utils.toMgs
import com.santansarah.barcodescanner.utils.valueOrZero

@Composable
fun NutritionData(
    nutriments: Nutriments,
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
            .padding(16.dp)
    ) {

        Column(
            Modifier
                .background(Color.Gray)
                .fillMaxWidth()
                .border(1.dp, Color.Black)
        ) {
            Text(
                modifier = Modifier.padding(8.dp),
                text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    ) {
                        append("Nutrition Data\n")
                    }
                    withStyle(
                        style = SpanStyle(
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    ) {
                        append("Serving Size: $servingSize")
                    }
                })
            //TableCell(text = "Nutrition Data \n$servingSize", weight = 1f, normalPadding, TextAlign.Start)
        }

        Row(Modifier.fillMaxWidth()) {
            TableCell("Calories", weight = column1Weight, normalPadding, TextAlign.Start)
            TableCell(
                text = nutriments.calories.toInt().toString(),
                weight = column2Weight, normalPadding, TextAlign.End
            )
        }

        TableRow(
            column1Weight = column1Weight,
            normalPadding = normalPadding,
            heading = "Cholesterol",
            value = nutriments.cholesterol?.toMgs().toString() + "mg",
            column2Weight = column2Weight,
        )
        TableRow(
            column1Weight = column1Weight,
            normalPadding = normalPadding,
            heading = "Fat",
            value = nutriments.fat.toString() + "g",
            column2Weight = column2Weight,
        )
        TableRow(
            column1Weight = column1Weight,
            normalPadding = indentedPadding,
            heading = "Saturated",
            value = nutriments.saturatedFat.toString() + "g",
            column2Weight = column2Weight,
        )
        TableRow(
            column1Weight = column1Weight,
            normalPadding = indentedPadding,
            heading = "Monounsaturated",
            value = nutriments.monounsaturatedFat.toString() + "g",
            column2Weight = column2Weight,
        )
        TableRow(
            column1Weight = column1Weight,
            normalPadding = indentedPadding,
            heading = "Polyunsaturated",
            value = nutriments.polyunsaturatedFat.toString() + "g",
            column2Weight = column2Weight,
        )
        TableRow(
            column1Weight = column1Weight,
            normalPadding = normalPadding,
            heading = "Sodium",
            value = nutriments.sodium.toMgs().toString() + "mg",
            column2Weight = column2Weight,
        )
        TableRow(
            column1Weight = column1Weight,
            normalPadding = normalPadding,
            heading = "Potassium",
            value = nutriments.potassium?.toMgs().toString() + "mg",
            column2Weight = column2Weight,
        )
        TableRow(
            column1Weight = column1Weight,
            normalPadding = normalPadding,
            heading = "Carbohydrates",
            value = nutriments.carbohydrates.toString() + "g",
            column2Weight = column2Weight,
        )
        TableRow(
            column1Weight = column1Weight,
            normalPadding = indentedPadding,
            heading = "Fiber",
            value = "${nutriments.fiber.valueOrZero()}g",
            column2Weight = column2Weight,
        )
        TableRow(
            column1Weight = column1Weight,
            normalPadding = indentedPadding,
            heading = "Sugars",
            value = nutriments.sugar.toString() + "g",
            column2Weight = column2Weight,
        )
        TableRow(
            column1Weight = column1Weight,
            normalPadding = normalPadding,
            heading = "Protein",
            value = nutriments.protein.toInt().toString() + "g",
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