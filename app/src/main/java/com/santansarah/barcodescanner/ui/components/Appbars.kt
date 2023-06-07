package com.santansarah.barcodescanner.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.santansarah.barcodescanner.ui.theme.lightestGray


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppBar(
    title: String,
    onBackClicked: (() -> Unit)?,
    onAccountClicked: () -> Unit
) {
    Column {
        CenterAlignedTopAppBar(
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = Color(0xFFe0002d),
                titleContentColor = lightestGray
            ),
            title = { Text(
                modifier = Modifier.clickable {
                },
                text = title)
                    },
            navigationIcon = {
                if (onBackClicked != null)
                    IconButton(onClick = onBackClicked) {
                        BackIcon(contentDesc = "Go Back")
                    }
            },
            actions = {
                IconButton(onClick = { onAccountClicked() }) {
                    Icon(
                        imageVector = Icons.Outlined.AccountCircle,
                        contentDescription = "Account")
                }
            }
        )
        Divider(thickness = 2.dp, color = Color.DarkGray)
    }
}