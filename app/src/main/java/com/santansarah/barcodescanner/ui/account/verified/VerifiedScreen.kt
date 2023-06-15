package com.santansarah.barcodescanner.ui.account.verified

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.santansarah.barcodescanner.R
import com.santansarah.barcodescanner.domain.models.SignInState
import com.santansarah.barcodescanner.domain.models.User
import com.santansarah.barcodescanner.domain.models.UserUIState
import com.santansarah.barcodescanner.ui.account.AccountContainer
import com.santansarah.barcodescanner.ui.components.MainAppBar
import com.santansarah.barcodescanner.ui.search.imageanimations.AnimatedSearchImageRow
import com.santansarah.barcodescanner.ui.search.imageanimations.AnimatedSearchImages
import com.santansarah.barcodescanner.ui.theme.BarcodeScannerTheme
import com.santansarah.barcodescanner.ui.theme.brightYellow

@Composable
fun VerifiedScreen(
    viewModel: VerifiedViewModel = hiltViewModel(),
    onSearch: () -> Unit,
    onAccount: () -> Unit,
    onAddPhone: () -> Unit
) {

    LaunchedEffect(true) {
        viewModel.reloadUser()
    }

    VerifySuccess(onSearch, onAccount, onAddPhone)

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun VerifySuccess(
    onSearch: () -> Unit,
    onAccount: () -> Unit,
    onAddPhone: () -> Unit
) {

    Scaffold(
        topBar = {
            MainAppBar(
                onBackClicked = null,
                title = "Your Account",
                onAccountClicked = { }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                //.padding(top = 20.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            ElevatedCard(
                modifier = Modifier
                    .fillMaxSize(),
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
                        text = "Account verified. Welcome!",
                        style = MaterialTheme.typography.titleLarge
                    )
                }
                Divider(thickness = 2.dp, color = Color.DarkGray)
                AnimatedSearchImageRow(false, AnimatedSearchImages.imageList.take(3))

                Column(
                    modifier = Modifier.padding(horizontal = 20.dp)
                ) {
                    Text(
                        modifier = Modifier.padding(bottom = 36.dp),
                        text = "Your email is verified - what would you like to do next?",
                        style = MaterialTheme.typography.headlineMedium,
                        textAlign = TextAlign.Center
                    )

                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { onSearch() }) {
                        Text(text = "Search Products")
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { onAccount() }) {
                        Text(text = "Go to My Account")
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { onAddPhone() }) {
                        Text(text = "Add 2FA Phone Authentication")
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
            Divider(thickness = 2.dp, color = Color.DarkGray)

        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun VerifySuccessPreview() {

    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    BarcodeScannerTheme {
        VerifySuccess(onSearch = { /*TODO*/ }, onAccount = { /*TODO*/ }) {

        }
    }
}


