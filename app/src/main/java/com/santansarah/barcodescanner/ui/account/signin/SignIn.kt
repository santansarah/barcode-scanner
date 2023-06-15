package com.santansarah.barcodescanner.ui.account.signin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.santansarah.barcodescanner.domain.models.PhoneAuthUIState
import com.santansarah.barcodescanner.domain.models.SignInState
import com.santansarah.barcodescanner.domain.models.UserUIState
import com.santansarah.barcodescanner.ui.account.shared.SignInButtons
import com.santansarah.barcodescanner.ui.account.shared.SignInForm
import com.santansarah.barcodescanner.ui.account.addphone.VerifyCode

@Composable
fun SignInScreen(
    viewModel: SignInViewModel = hiltViewModel(),
    onSearch: () -> Unit,
    onAccount: () -> Unit
) {

    val newUserUIState by viewModel.userUIState.collectAsStateWithLifecycle()
    val phoneAuthUIState by viewModel.phoneAuthUIState.collectAsStateWithLifecycle()
    val userMessage by viewModel.userMessage.collectAsStateWithLifecycle()
    val signInState by viewModel.signInState.collectAsStateWithLifecycle()

    when (signInState) {
        SignInState.SMS_AUTHORIZED, SignInState.AUTHORIZED -> {
            UserIsSignedIn(onSearch = onSearch, onAccount = onAccount)
        }
        SignInState.NEEDS_2FA, SignInState.VERIFY_FAILED -> {
            SignInWithTwoFA(phoneAuthUIState, userMessage, viewModel::onResendVerification, signInState)
        }
        else -> {
            OneFactorSignIn(newUserUIState, userMessage, viewModel::onSignIn, {  })
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
        modifier = Modifier.fillMaxSize()
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            modifier = Modifier.padding(bottom = 10.dp),
            text = "We've sent a verification code to your phone.",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))
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
        modifier = Modifier.fillMaxSize()
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            modifier = Modifier.padding(bottom = 10.dp),
            text = "You're signed in - what would you like to do next?",
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
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            modifier = Modifier.padding(
                start = 20.dp,
                end = 20.dp,
                bottom = 10.dp
            ),
            text = "Welcome back. If you've added your phone for 2FA, " +
                    "make sure to have it available.",
            style = MaterialTheme.typography.headlineSmall
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

    OneFactorSignIn(userUIState = UserUIState(), 
        userMessage = null, 
        onSignIn = { /*TODO*/ }) {
        
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun VerifyCodePreview() {

    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    Column {
        SignInWithTwoFA(phoneAuthUIState = PhoneAuthUIState(), userMessage = null, {}, SignInState.NEEDS_2FA)
        SignInWithTwoFA(phoneAuthUIState = PhoneAuthUIState(), userMessage = "Verification failed.", {}, SignInState.NEEDS_2FA)
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SignedInPreview() {

    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    UserIsSignedIn(onSearch = { /*TODO*/ }) {
        
    }
}


