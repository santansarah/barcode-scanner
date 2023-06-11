package com.santansarah.barcodescanner.ui.account.verified

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.santansarah.barcodescanner.domain.models.PhoneAuthState
import com.santansarah.barcodescanner.domain.models.PhoneAuthUIState
import com.santansarah.barcodescanner.domain.models.User
import com.santansarah.barcodescanner.domain.models.UserUIState
import com.santansarah.barcodescanner.ui.account.NewOrExistingUser
import com.santansarah.barcodescanner.ui.account.SignInForm
import com.santansarah.barcodescanner.utils.toPhone
import timber.log.Timber

@Composable
fun VerifiedScreen(
    viewModel: VerifiedViewModel = hiltViewModel(),
    isNewlyVerified: Boolean = true
) {

    val userUIState by viewModel.userUIState.collectAsStateWithLifecycle()
    val phoneAuthUIState by viewModel.phoneAuthUIState.collectAsStateWithLifecycle()
    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle(null)
    val userMessage by viewModel.userMessage.collectAsStateWithLifecycle()
    val phoneAuthCurrentState by viewModel.phoneAuthCurrentState.collectAsStateWithLifecycle()

    LaunchedEffect(phoneAuthCurrentState) {
        if (phoneAuthCurrentState == PhoneAuthState.CREDENTIALS_SET) {
            viewModel.enrollUser()
        }
    }

    userMessage?.let {
        Text(text = it)
    }

    currentUser?.let {
        EmailVerified(
            currentUser = it,
            userUIState = userUIState,
            userMessage = userMessage,
            phoneAuthUIState = phoneAuthUIState,
            phoneAuthCurrentState = phoneAuthCurrentState,
            isNewlyVerified = isNewlyVerified,
            onSignIn = viewModel::onSignIn
        )
    }

}

@Composable
fun EmailVerified(
    currentUser: User,
    userUIState: UserUIState,
    userMessage: String?,
    phoneAuthUIState: PhoneAuthUIState,
    phoneAuthCurrentState: PhoneAuthState,
    isNewlyVerified: Boolean,
    onSignIn: () -> Unit,
) {
    Column {

        Text(text = "Your email has been verified.")

        Text(
            text = currentUser.email,
            style = MaterialTheme.typography.headlineLarge
        )
        Spacer(modifier = Modifier.height(16.dp))

        Text(text = currentUser.phoneNumber ?: "nope")
        Timber.d("phone: ${currentUser.phoneNumber}")

        if (currentUser.phoneNumber.isNullOrBlank()) {
            Text(
                modifier = Modifier.padding(
                    start = 20.dp,
                    end = 20.dp,
                ),
                text = "Optional: Add your phone for extra security. " +
                        "To enable 2FA, first, provide your credentials:"
            )
            Spacer(modifier = Modifier.height(16.dp))
            SignInForm(
                userUIState = userUIState
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                modifier = Modifier.padding(
                    start = 20.dp,
                    end = 20.dp,
                ),
                text = "Next, add your phone:"
            )
            Spacer(modifier = Modifier.height(16.dp))
            AddPhone(
                phoneAuthUIState
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        when (phoneAuthCurrentState) {
            PhoneAuthState.CODE_SENT -> {
                VerifyCode(phoneAuthUIState = phoneAuthUIState)
            }

            PhoneAuthState.CREDENTIALS_SET -> {
                CircularProgressIndicator()
            }

            PhoneAuthState.VERIFY_FAILED -> {
                Text(text = "Verification failed.")
            }

            else -> {}
        }
    }
}


@Preview(showBackground = true)
@Composable
fun EmailVerifiedPreview() {

    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var phone by rememberSaveable { mutableStateOf("") }

    Column {
        EmailVerified(
            currentUser = User(email = "test@test.com", "", "", "", true),
            userUIState = UserUIState(),
            userMessage = null,
            phoneAuthUIState = PhoneAuthUIState(
                phone = phone,
                onPhoneChanged = { phone = it.toPhone() }
            ),
            phoneAuthCurrentState = PhoneAuthState.NOT_ADDED,
            isNewlyVerified = true, {}
        )
    }
}