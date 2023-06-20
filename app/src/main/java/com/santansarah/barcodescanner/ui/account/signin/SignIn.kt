package com.santansarah.barcodescanner.ui.account.signin

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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.santansarah.barcodescanner.R
import com.santansarah.barcodescanner.domain.models.PhoneAuthUIState
import com.santansarah.barcodescanner.domain.models.SignInState
import com.santansarah.barcodescanner.domain.models.UserUIState
import com.santansarah.barcodescanner.ui.account.shared.SignInButtons
import com.santansarah.barcodescanner.ui.account.shared.SignInForm
import com.santansarah.barcodescanner.ui.account.addphone.VerifyCode
import com.santansarah.barcodescanner.ui.components.MainAppBar
import com.santansarah.barcodescanner.ui.theme.brightYellow

@Composable
fun SignInScreen(
    viewModel: SignInViewModel = hiltViewModel(),
    onSearch: () -> Unit,
    onAccount: () -> Unit
) {

    val userUIState by viewModel.userUIState.collectAsStateWithLifecycle()
    val phoneAuthUIState by viewModel.phoneAuthUIState.collectAsStateWithLifecycle()
    val userMessage by viewModel.userMessage.collectAsStateWithLifecycle()
    val signInState by viewModel.signInState.collectAsStateWithLifecycle()

    SignInContainer(
        signInState,
        onSearch,
        onAccount,
        phoneAuthUIState,
        userMessage,
        userUIState,
        viewModel::onResendVerification,
        viewModel::onSignIn
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SignInContainer(
    signInState: SignInState,
    onSearch: () -> Unit,
    onAccount: () -> Unit,
    phoneAuthUIState: PhoneAuthUIState,
    userMessage: String?,
    userUIState: UserUIState,
    onResend: () -> Unit,
    onSignIn: () -> Unit
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
                        text = "Sign in",
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
                        painter = painterResource(id = R.drawable.apple),
                        contentDescription = "Cherries"
                    )
                    Image(
                        modifier = Modifier.size(105.dp),
                        painter = painterResource(id = R.drawable.pretzel),
                        contentDescription = "Banana"
                    )
                    Image(
                        modifier = Modifier.size(100.dp),
                        painter = painterResource(id = R.drawable.corn),
                        contentDescription = "Apple"
                    )
                }

                when (signInState) {
                    SignInState.SMS_AUTHORIZED, SignInState.AUTHORIZED -> {
                        UserIsSignedIn(onSearch = onSearch, onAccount = onAccount)
                    }

                    SignInState.NEEDS_2FA,
                    SignInState.VERIFY_FAILED,
                    SignInState.CODE_SENT-> {
                        SignInWithTwoFA(
                            phoneAuthUIState,
                            userMessage,
                            onResend,
                            signInState
                        )
                    }

                    else -> {
                        OneFactorSignIn(userUIState, userMessage, onSignIn, { })
                    }
                }
            }
        }
    }
}


@Composable
private fun SignInWithTwoFA(
    phoneAuthUIState: PhoneAuthUIState,
    userMessage: String?,
    onResend: () -> Unit,
    signInState: SignInState
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        VerifyCode(
            phoneAuthUIState = phoneAuthUIState,
            userMessage = userMessage,
            onResend = onResend,
            signInState = signInState,
            isNew = false
        )
    }
}

@Composable
fun UserIsSignedIn(
    onSearch: () -> Unit,
    onAccount: () -> Unit
) {

    Column(
        modifier = Modifier.padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.padding(bottom = 10.dp),
            text = "You're signed in - what would you like to do next?",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

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
    }
}

@Composable
fun OneFactorSignIn(
    userUIState: UserUIState,
    userMessage: String?,
    onSignIn: () -> Unit,
    onCancel: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        Text(
            modifier = Modifier.padding(
                start = 20.dp,
                end = 20.dp,
                bottom = 20.dp
            ),
            text = "Welcome back. If you've added your phone for 2FA, " +
                    "make sure to have it available.",
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center
        )

        SignInForm(
            userUIState,
        )
        Spacer(modifier = Modifier.height(16.dp))
        userMessage?.let {
            Text(text = it)
            Spacer(modifier = Modifier.height(16.dp))
        }
        SignInButtons(
            isNew = false,
            onSignUp = { },
            onSignIn = onSignIn,
            onCancel = onCancel
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SignInPreview() {

    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    SignInContainer(
        signInState = SignInState.NOT_SIGNED_IN,
        onSearch = { /*TODO*/ },
        onAccount = { /*TODO*/ },
        phoneAuthUIState = PhoneAuthUIState(),
        userMessage = null,
        userUIState = UserUIState(),
        onResend = { /*TODO*/ }) {

    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun VerifyCodePreview() {

    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    SignInContainer(
        signInState = SignInState.NEEDS_2FA,
        onSearch = { /*TODO*/ },
        onAccount = { /*TODO*/ },
        phoneAuthUIState = PhoneAuthUIState(),
        userMessage = "Verification failed",
        userUIState = UserUIState(),
        onResend = { /*TODO*/ }) {

    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SignedInPreview() {

    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    SignInContainer(
        signInState = SignInState.AUTHORIZED,
        onSearch = { /*TODO*/ },
        onAccount = { /*TODO*/ },
        phoneAuthUIState = PhoneAuthUIState(),
        userMessage = null,
        userUIState = UserUIState(),
        onResend = { /*TODO*/ }) {

    }
}

