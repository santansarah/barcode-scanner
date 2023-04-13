package com.santansarah.barcodescanner.ui

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.santansarah.barcodescanner.R
import com.santansarah.barcodescanner.ui.theme.BarcodeScannerTheme
import com.santansarah.barcodescanner.ui.theme.brightYellow
import com.santansarah.barcodescanner.ui.theme.cardBackground
import com.santansarah.barcodescanner.ui.theme.gray
import com.santansarah.barcodescanner.ui.theme.lightGray
import com.santansarah.barcodescanner.ui.theme.lightestGray
import kotlinx.coroutines.launch
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeRoute(
    viewModel: HomeViewModel = hiltViewModel(),
    onGotBarcode: (String) -> Unit,
    onSearchClicked: (String) -> Unit,
) {

    val barcodeResults = viewModel.barcode.collectAsStateWithLifecycle().value

    LaunchedEffect(key1 = barcodeResults) {
        Timber.d(barcodeResults)
        barcodeResults?.let {
            onGotBarcode(barcodeResults)
            viewModel.clearBarcode()
        }
    }

    HomeScreen(onScan = viewModel::scanBarcode, onSearch = onSearchClicked)

}

@OptIn(ExperimentalTextApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onScan: () -> Unit,
    onSearch: (String) -> Unit
) {

    Scaffold(
        topBar = {
            MainAppBar(
                onBackClicked = { },
                title = "Welcome"
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(top = 20.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            Divider(thickness = 2.dp, color = Color.DarkGray)
            ElevatedCard(
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RectangleShape
            ) {

                Column(
                    modifier = Modifier.background(brightYellow)
                        .fillMaxWidth()
                ) {
                    Text(
                        modifier = Modifier
                            .padding(6.dp),
                        text = "Search products",
                        style = MaterialTheme.typography.titleLarge
                    )
                }
                Divider(thickness = 2.dp, color = Color.DarkGray)

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 36.dp)
                ) {
                    Image(
                        modifier = Modifier.size(100.dp),
                        painter = painterResource(id = R.drawable.cherries),
                        contentDescription = "Cherries"
                    )
                    Image(
                        modifier = Modifier.size(100.dp),
                        painter = painterResource(id = R.drawable.banana),
                        contentDescription = "Banana"
                    )
                    Image(
                        modifier = Modifier.size(100.dp),
                        painter = painterResource(id = R.drawable.apple),
                        contentDescription = "Apple"
                    )
                }

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    var searchText by rememberSaveable {
                        mutableStateOf("")
                    }

                    OutlinedTextField(
                        shape = RectangleShape,
                        modifier = Modifier
                            .fillMaxWidth(),
                        //.padding(vertical = 4.dp),
                        value = searchText,
                        onValueChange = { searchText = it },
                        trailingIcon = {
                            IconButton(onClick = { onSearch(searchText) }) {
                                Icon(
                                    Icons.Default.Search,
                                    "Search for products"
                                )
                            }
                        }
                    )
                    //Spacer(Modifier.height(10.dp))

                }
            }
            Divider(thickness = 2.dp, color = Color.DarkGray)

            Spacer(modifier = Modifier.height(20.dp))

            Divider(thickness = 2.dp, color = Color.DarkGray)
            ElevatedCard(
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RectangleShape
            ) {

                Column(
                    modifier = Modifier.background(brightYellow)
                        .fillMaxWidth()
                ) {
                    Text(
                        modifier = Modifier.padding(6.dp),
                        text = "Scan barcodes",
                        style = MaterialTheme.typography.titleLarge
                    )
                }
                Divider(thickness = 2.dp, color = Color.DarkGray)

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 36.dp)
                ) {
                    Image(
                        modifier = Modifier.size(100.dp),
                        painter = painterResource(id = R.drawable.milk),
                        contentDescription = "Cherries"
                    )
                    Image(
                        modifier = Modifier.size(105.dp),
                        painter = painterResource(id = R.drawable.chicken),
                        contentDescription = "Banana"
                    )
                    Image(
                        modifier = Modifier.size(100.dp),
                        painter = painterResource(id = R.drawable.cheese),
                        contentDescription = "Apple"
                    )
                }

                val scope = rememberCoroutineScope()

                Box(//modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.BottomCenter
                ) {

                    Image(
                        painter = painterResource(id = R.drawable.barcode),
                        "Barcode",
                        modifier = Modifier.padding(bottom = 20.dp),
                        colorFilter = ColorFilter.tint(Color.DarkGray)
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxWidth(.4f)
                            .padding(bottom = 28.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        TextButton(
                            modifier = Modifier
                                .width(130.dp)
                                .background(Color(0xFF282829)),
                            colors = ButtonDefaults.outlinedButtonColors(
                                //containerColor = Color(0xFF076C94),
                                contentColor = Color.Black
                            ),
                            onClick = { onScan() }) {
                            Text(
                                text = "Scan Product",
                                textAlign = TextAlign.Center,
                                color = lightestGray,
                                style = TextStyle(fontWeight = FontWeight.Bold)
                            )
                        }
                    }
                    Divider(thickness = 2.dp, color = Color.DarkGray)
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewHomeScreen() {

    BarcodeScannerTheme {
        HomeScreen(onScan = { /*TODO*/ }, onSearch = {})
    }

}