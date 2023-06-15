package com.santansarah.barcodescanner.ui.account.addphone

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.santansarah.barcodescanner.domain.models.PhoneAuthUIState
import com.santansarah.barcodescanner.domain.models.SignInState
import com.santansarah.barcodescanner.domain.models.UserUIState
import com.santansarah.barcodescanner.ui.account.shared.SignInForm
import com.santansarah.barcodescanner.ui.components.MainAppBar
import com.santansarah.barcodescanner.ui.search.imageanimations.AnimatedSearchImageRow
import com.santansarah.barcodescanner.ui.search.imageanimations.AnimatedSearchImages
import com.santansarah.barcodescanner.ui.theme.brightYellow

@Composable
fun AddPhoneScreen(
    viewModel: AddPhoneViewModel = hiltViewModel(),
    onSearch: () -> Unit,
    onAccount: () -> Unit,
) {

    val userUIState by viewModel.userUIState.collectAsStateWithLifecycle()
    val phoneAuthUIState by viewModel.phoneAuthUIState.collectAsStateWithLifecycle()
    val userMessage by viewModel.userMessage.collectAsStateWithLifecycle()
    val signInState by viewModel.signInState.collectAsStateWithLifecycle()

    AddPhoneContainer(
        signInState = signInState,
        userUIState = userUIState,
        phoneAuthUIState = phoneAuthUIState,
        userMessage = userMessage,
        onResendVerification = viewModel::onResendVerification,
        onSearch = onSearch,
        onAccount = onAccount
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddPhoneContainer(
    signInState: SignInState,
    userUIState: UserUIState,
    phoneAuthUIState: PhoneAuthUIState,
    userMessage: String?,
    onResendVerification: () -> Unit,
    onSearch: () -> Unit,
    onAccount: () -> Unit,
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
                        text = "Protect your account with 2FA",
                        style = MaterialTheme.typography.titleLarge
                    )
                }
                Divider(thickness = 2.dp, color = Color.DarkGray)

                AnimatedSearchImageRow(
                    error = false,
                    images = AnimatedSearchImages.imageList.takeLast(3)
                )

                when (signInState) {
                    SignInState.AUTHORIZED -> {
                        AddPhone(
                            userUIState = userUIState,
                            phoneAuthUIState = phoneAuthUIState,
                            userMessage = userMessage,
                            signInState = signInState
                        )
                    }

                    SignInState.CODE_SENT -> {
                        VerifyCode(
                            phoneAuthUIState = phoneAuthUIState,
                            userMessage = userMessage,
                            onResend = onResendVerification,
                            signInState = signInState,
                            isNew = true
                        )
                    }

                    SignInState.ENROLLED, SignInState.SMS_AUTHORIZED -> {
                        UserIsEnrolled(
                            onSearch = onSearch,
                            onAccount = onAccount
                        )
                    }

                    else -> { }
                }
            }
        }
    }
}

@Composable
fun AddPhone(
    userUIState: UserUIState,
    phoneAuthUIState: PhoneAuthUIState,
    userMessage: String?,
    signInState: SignInState
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.padding(
                start = 20.dp,
                end = 20.dp,
                bottom = 10.dp
            ),
            text = "To enable 2FA, first, re-enter your credentials:",
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        SignInForm(userUIState)
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Next, add your phone:",
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        AddPhone(
            phoneAuthUIState,
            signInState
        )
    }
}

@Composable
fun UserIsEnrolled(
    onSearch: () -> Unit,
    onAccount: () -> Unit,
) {

    Column(
        modifier = Modifier.padding(horizontal = 20.dp)
    ) {
        Text(
            modifier = Modifier.padding(bottom = 36.dp),
            text = "You phone has been added! What would you like to do next?",
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

        Spacer(modifier = Modifier.height(20.dp))
    }

}

@Preview(showBackground = true)
@Composable
fun AddPhonePreview() {

    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var phone by rememberSaveable { mutableStateOf("") }

    AddPhoneContainer(
        signInState = SignInState.AUTHORIZED,
        userUIState = UserUIState(),
        phoneAuthUIState = PhoneAuthUIState(),
        userMessage = null, {}, {}, {})
}

@Preview(showBackground = true)
@Composable
fun TextSentPreview() {

    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var phone by rememberSaveable { mutableStateOf("") }

    AddPhoneContainer(
        signInState = SignInState.CODE_SENT,
        userUIState = UserUIState(),
        phoneAuthUIState = PhoneAuthUIState(),
        userMessage = null, {}, {}, {})
}

@Preview(showBackground = true)
@Composable
fun EnrolledPreview() {

    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var phone by rememberSaveable { mutableStateOf("") }

    AddPhoneContainer(
        signInState = SignInState.ENROLLED,
        userUIState = UserUIState(),
        phoneAuthUIState = PhoneAuthUIState(),
        userMessage = null, {}, {}, {})
}