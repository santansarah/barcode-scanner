package com.santansarah.barcodescanner.ui.search

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.santansarah.barcodescanner.R
import com.santansarah.barcodescanner.data.remote.SearchProductItem


@Composable
fun ProductSearchListItem(
    productInfo: SearchProductItem?,
    onGotBarcode: (String) -> Unit,
    placeHolderImage: @Composable () -> Unit
) {
    productInfo?.let {
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                //.padding(vertical = 6.dp)
                .clickable {
                    onGotBarcode(it.code)
                },
            shape = RectangleShape
        ) {

            Box(modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier
                            .padding(end = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {

                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(productInfo.imageUrl)
                                .crossfade(true)
                                .build(),
                            placeholder = painterResource(id = R.drawable.food_placholder),
                            error = painterResource(id = R.drawable.food_placholder),
                            fallback = painterResource(id = R.drawable.food_placholder),
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .height(140.dp)
                                .width(100.dp)
                                .border(2.dp, Color(0xFFcacfc9)),
                            contentDescription = productInfo.productName
                        )

                    }

                    Column(
                        verticalArrangement = Arrangement.Center

                    ) {

                        val productText = listOfNotNull(
                            it.brands,
                            it.productName
                        ).joinToString(" ")

                        Text(
                            text = productText,
                            style = MaterialTheme.typography.titleLarge,
                            overflow = TextOverflow.Ellipsis,
                            color = Color.Black
                        )

                    }
                }

            }
        }
        Divider(thickness = 2.dp, color = Color.DarkGray)
        Spacer(Modifier.height(10.dp))
    }
}
