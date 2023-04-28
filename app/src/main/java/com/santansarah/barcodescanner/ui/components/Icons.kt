package com.santansarah.barcodescanner.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.santansarah.barcodescanner.R
import com.santansarah.barcodescanner.ui.theme.lightestGray

@Composable
fun BackIcon(
    modifier: Modifier = Modifier,
    contentDesc: String
) {
    Icon(
        imageVector = Icons.Default.ArrowBack,
        contentDescription = contentDesc,
        tint = lightestGray
    )
}

@Composable
fun PlaceholderImage(
    description: String
) {
    Image(
        modifier = Modifier
            //.clip(RoundedCornerShape(20.dp))
            .height(140.dp)
            .width(100.dp)
            .border(2.dp, Color(0xFFcacfc9)),
        //.border(1.dp, Color(0xFFf9004b), RoundedCornerShape(20.dp)),
        painter = painterResource(id = R.drawable.food_placholder),
        contentDescription = description,
        colorFilter = ColorFilter.tint(Color(0xFFcacfc9))
    )
}