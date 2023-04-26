package com.santansarah.barcodescanner.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp


@Composable
fun TableRow(
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

@Composable
fun RowScope.TableCell(
    text: String,
    weight: Float,
    padding: PaddingValues,
    align: TextAlign
) {
    Text(
        text = text,
        Modifier
            .border(1.dp, Color.Black)
            .weight(weight)
            .padding(padding),
        textAlign = align
    )
}