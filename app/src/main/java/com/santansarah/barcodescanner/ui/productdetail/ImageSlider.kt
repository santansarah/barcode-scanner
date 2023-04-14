package com.santansarah.barcodescanner.ui.productdetail

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import coil.size.Size
import com.santansarah.barcodescanner.R

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ItemImageSlider(
    imgFront: String?,
    imgBack: String?
) {
    //val selectedImage = rememberSaveable { mutableStateOf(0) }
    val pagerState = rememberPagerState(0)
    val sliderList = listOf(imgFront, imgBack)

    HorizontalPager(
        modifier = Modifier.fillMaxWidth(),
        pageCount = 2,
        state = pagerState,
        verticalAlignment = Alignment.CenterVertically
    ) { currentImage ->

        ElevatedCard(
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(6.dp),
            modifier = Modifier
                .size(220.dp)
                .clickable(
                    onClick = { }
                )
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {

                sliderList[currentImage]?.let {
                    SubcomposeAsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(sliderList[currentImage])
                            .size(Size.ORIGINAL)
                            .build(),
                        loading = { AnimatedFoodIcon() },
                        error = { LargeImagePlaceholder() },
                        contentDescription = "Product",
                        alignment = Alignment.BottomEnd,
                        contentScale = ContentScale.Inside
                    )
                } ?: LargeImagePlaceholder()
            }
        }
    }
}

@Composable
fun LargeImagePlaceholder() {
    Image(
        painter = painterResource(id = R.drawable.food_placholder),
        contentDescription = "Image not found."
    )
}

