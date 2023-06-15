package com.santansarah.barcodescanner.ui.account

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
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.santansarah.barcodescanner.R
import com.santansarah.barcodescanner.domain.models.SignInState
import com.santansarah.barcodescanner.domain.models.User
import com.santansarah.barcodescanner.domain.models.UserUIState
import com.santansarah.barcodescanner.ui.components.MainAppBar
import com.santansarah.barcodescanner.ui.theme.BarcodeScannerTheme
import com.santansarah.barcodescanner.ui.theme.brightYellow

@Composable
fun AccountScreen(
    viewModel: AccountViewModel = hiltViewModel(),
    onAddPhone: () -> Unit,
    onSignIn: () -> Unit,
    onSignUp: () -> Unit
) {

    val userUIState by viewModel.userUIState.collectAsStateWithLifecycle()
    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle(initialValue = null)
    val userMessage by viewModel.userMessage.collectAsStateWithLifecycle()
    val signInState by viewModel.signInState.collectAsStateWithLifecycle()

    AccountContainer(
        userUIState = userUIState,
        currentUser = currentUser,
        signInState = signInState,
        onSignIn = onSignIn,
        onSignUp = onSignUp
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountContainer(
    userUIState: UserUIState,
    currentUser: User?,
    signInState: SignInState,
    onSignIn: () -> Unit,
    onSignUp: () -> Unit
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

            when (signInState) {
                SignInState.AUTHORIZED -> {
                    AccountDetails(currentUser = currentUser, userUIState = userUIState) {
                    }
                }

                else -> {
                    ChooseSignInOption(onSignIn = onSignIn, onSignUp = onSignUp)
                }
            }

        }
    }
}


@Composable
private fun ChooseSignInOption(
    onSignIn: () -> Unit,
    onSignUp: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize(),
    ) {

        Spacer(modifier = Modifier.height(16.dp))

        Divider(thickness = 2.dp, color = Color.DarkGray)
        ElevatedCard(
            shape = RectangleShape
        ) {

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
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
        }
        Divider(thickness = 2.dp, color = Color.DarkGray)

        Column(
        ) {

            ElevatedCard(
                modifier = Modifier.height(220.dp),
                shape = RectangleShape
            ) {

                Column(
                    modifier = Modifier
                        .background(brightYellow)
                        .fillMaxWidth()
                ) {
                    Text(
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 6.dp),
                        text = "I've never signed in",
                        style = MaterialTheme.typography.titleLarge
                    )
                }
                Divider(thickness = 2.dp, color = Color.DarkGray)

                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 16.dp),
                    text = "Sign up to save products to your lists, and " +
                            "sync your daily meals with MyFitnessPal."
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    onClick = {
                        onSignUp()
                    }) {
                    Text(text = "I'd like to sign up")
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
            Divider(thickness = 2.dp, color = Color.DarkGray)
            Spacer(modifier = Modifier.height(16.dp))

            Divider(thickness = 2.dp, color = Color.DarkGray)
            ElevatedCard(
                modifier = Modifier.height(220.dp),
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
                        text = "I have an account",
                        style = MaterialTheme.typography.titleLarge
                    )
                }
                Divider(thickness = 2.dp, color = Color.DarkGray)

                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 16.dp),
                    text = "Welcome back. Sign in to access additional features."
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    onClick = { onSignIn() }) {
                    Text(text = "I'd like to sign in")
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
            Divider(thickness = 2.dp, color = Color.DarkGray)

        }
    }
}

@Composable
private fun AccountDetails(
    currentUser: User?,
    userUIState: UserUIState,
    onAddPhone: () -> Unit
) {
    Column {
        Text(
            text = currentUser!!.email,
            style = MaterialTheme.typography.headlineLarge
        )
        Spacer(modifier = Modifier.height(16.dp))

        if (!currentUser!!.isEmailVerified) {
            Text(text = "To enable your account, you need to verify your email address. ")
            Spacer(modifier = Modifier.height(16.dp))
            TextButton(onClick = { userUIState.onVerifyEmail() }) {
                Text(text = "Resend link.")
            }
        } else {
            if (currentUser!!.phoneNumber.isNullOrBlank())
                TextButton(onClick = { onAddPhone() }) {
                    Text(text = "Add Phone")
                }
        }

        TextButton(onClick = { userUIState.onSignOut() }) {
            Text(text = "Sign Out")
        }

    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun NotSignedInPreview() {

    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    BarcodeScannerTheme {
        AccountContainer(
            userUIState = UserUIState(),
            currentUser = null,
            signInState = SignInState.NOT_SIGNED_IN,
            onSignIn = { /*TODO*/ }) {

        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AccountDetailsPreview() {

    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    BarcodeScannerTheme {
        AccountContainer(
            userUIState = UserUIState(email = "test@mail.com"),
            currentUser = User("test@mail.com", "Sarah", "", "", true),
            signInState = SignInState.AUTHORIZED,
            onSignIn = { /*TODO*/ }) {

        }
    }
}


