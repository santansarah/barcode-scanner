package com.santansarah.barcodescanner.ui.account

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.santansarah.barcodescanner.R
import com.santansarah.barcodescanner.domain.models.PhoneAuthState
import com.santansarah.barcodescanner.domain.models.UserUIState
import com.santansarah.barcodescanner.domain.models.PhoneAuthUIState
import com.santansarah.barcodescanner.ui.account.verified.AddPhone

@Composable
fun AccountScreen(
    viewModel: AccountViewModel = hiltViewModel(),
    onAddPhone: () -> Unit
) {

    val newUserUIState by viewModel.userUIState.collectAsStateWithLifecycle()
    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle(null)
    val userMessage by viewModel.userMessage.collectAsStateWithLifecycle()

    if (currentUser == null) {
        NewOrExistingUser(
            userUIState = newUserUIState,
            onSignIn = viewModel::onSignIn,
            onSignUp = viewModel::onSignUp,
            userMessage = userMessage
        )
    } else {
        Column {
            Text(
                text = currentUser!!.email,
                style = MaterialTheme.typography.headlineLarge
            )
            Spacer(modifier = Modifier.height(16.dp))

            if (!currentUser!!.isEmailVerified) {
                Text(text = "To enable your account, you need to verify your email address. ")
                Spacer(modifier = Modifier.height(16.dp))
                TextButton(onClick = { newUserUIState.onVerifyEmail() }) {
                    Text(text = "Resend link.")
                }
            } else {
                if (currentUser!!.phoneNumber.isNullOrBlank())
                    TextButton(onClick = { onAddPhone() }) {
                        Text(text = "Add Phone")
                    }
            }

            TextButton(onClick = { viewModel.onSignOut() }) {
                Text(text = "Sign Out")
            }

        }
    }

}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NewOrExistingUser(
    userUIState: UserUIState,
    onSignIn: () -> Unit,
    onSignUp: () -> Unit,
    userMessage: String?
) {

    var showSignIn by rememberSaveable {
        mutableStateOf(false)
    }
    var isNew by rememberSaveable { mutableStateOf(false) }

    AnimatedContent(
        targetState = showSignIn,
        /*transitionSpec = {
            fadeIn(animationSpec = tween(2000)) with
                    fadeOut(animationSpec = tween(2000)) + slideOutHorizontally()
        },*/
        label = ""
    )
    { targetShowSignIn ->
        if (!targetShowSignIn) {
            ChooseSignInOption {
                showSignIn = true
                isNew = it
            }
        } else {
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
                    text = stringResource(R.string.sign_in),
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
                SignInButtons(isNew = isNew,
                    onSignUp = onSignUp,
                    onSignIn = onSignIn,
                    onCancel = { showSignIn = false})
            }
        }
    }
}

@Composable
private fun ChooseSignInOption(
    onClick: (Boolean) -> Unit
) {

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Column(
            modifier = Modifier.padding(horizontal = 20.dp)
        ) {
            Text(
                modifier = Modifier.padding(bottom = 10.dp),
                text = "It looks like you're not signed in. " +
                        "Choose from the following options:",
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { onClick(false) }) {
                Text(text = "I have an account")
            }

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    onClick(true)
                }) {
                Text(text = "I'd like to sign up")
            }
        }

    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SignInPreview() {

    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    NewOrExistingUser(
        UserUIState(),
        {},
        {},
        "Invalid credentials."
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ChoosePreview() {

    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    ChooseSignInOption {}
}


