package com.santansarah.barcodescanner.ui.account.signup

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
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.santansarah.barcodescanner.R
import com.santansarah.barcodescanner.domain.models.SignInState
import com.santansarah.barcodescanner.domain.models.User
import com.santansarah.barcodescanner.domain.models.UserUIState
import com.santansarah.barcodescanner.ui.account.shared.SignInButtons
import com.santansarah.barcodescanner.ui.account.shared.SignInForm
import com.santansarah.barcodescanner.ui.components.MainAppBar
import com.santansarah.barcodescanner.ui.theme.BarcodeScannerTheme
import com.santansarah.barcodescanner.ui.theme.brightYellow

@Composable
fun SignUpScreen(
    viewModel: SignUpViewModel = hiltViewModel()
) {

    val newUserUIState by viewModel.userUIState.collectAsStateWithLifecycle()
    val signInState by viewModel.signInState.collectAsStateWithLifecycle()
    val userMessage by viewModel.userMessage.collectAsStateWithLifecycle()

    SignUpContainer(
        userUIState = newUserUIState,
        signInState = signInState,
        userMessage = userMessage,
        onSignUp = viewModel::onSignUp,
        onCancel = {}
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpContainer(
    userUIState: UserUIState,
    signInState: SignInState,
    userMessage: String?,
    onSignUp: () -> Unit,
    onCancel: () -> Unit
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
                        text = "Create new account",
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

                if (signInState == SignInState.NOT_SIGNED_IN || signInState ==
                    SignInState.CREDENTIAL_ERROR
                ) {
                    SignUpNewUser(
                        userUIState = userUIState,
                        onSignUp = onSignUp,
                        userMessage = userMessage,
                        {}
                    )
                } else {
                    SentVerificationEmail(userUIState)
                }
            }
        }
    }

}

@Composable
fun SignUpNewUser(
    userUIState: UserUIState,
    onSignUp: () -> Unit,
    userMessage: String?,
    onCancel: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        Text(
            modifier = Modifier.padding(horizontal = 20.dp),
            text = stringResource(id = R.string.sign_in),
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        SignInForm(
            userUIState,
        )
        Spacer(modifier = Modifier.height(16.dp))
        userMessage?.let {
            Text(
                modifier = Modifier.padding(horizontal = 20.dp),
                text = it,
                color = MaterialTheme.colorScheme.error
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        SignInButtons(
            isNew = true,
            onSignUp = onSignUp,
            onSignIn = { },
            onCancel = onCancel
        )
    }

}

@Composable
fun SentVerificationEmail(
    userUIState: UserUIState
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            modifier = Modifier.padding(horizontal = 20.dp),
            text = "Success! We've sent an email verification link to ${userUIState.email}." +
                    "\n\nOnce it's verified, " +
                    "click continue to complete the sign up process.",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SignInPreview() {

    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    BarcodeScannerTheme {
        SignUpContainer(
            UserUIState(),
            SignInState.NOT_SIGNED_IN,
            "Invalid credentials.",
            {}, {}
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SignInSuccessPreview() {

    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    BarcodeScannerTheme {
        SignUpContainer(
            UserUIState(email = "test@mail.com"),
            SignInState.VERIFYING_EMAIL,
            "Invalid credentials.",
            {}, {})
    }
}

