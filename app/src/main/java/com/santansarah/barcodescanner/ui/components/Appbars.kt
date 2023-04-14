package com.santansarah.barcodescanner.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppBar(
    title: String,
    onBackClicked: () -> Unit
) {
    Column {
        CenterAlignedTopAppBar(
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = Color(0xFFe0002d),
                titleContentColor = Color.White
            ),
            title = { Text(text = title) },
            navigationIcon = {
                IconButton(onClick = onBackClicked) {
                    BackIcon(contentDesc = "Go Back")
                }
            }
        )
        Divider(thickness = 2.dp, color = Color.DarkGray)
    }
}