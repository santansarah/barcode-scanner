package com.santansarah.barcodescanner.ui.account

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.with
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.santansarah.barcodescanner.R
import com.santansarah.barcodescanner.ui.productdetail.sections.productLoadingTransitionSpec

@Composable
fun AccountScreen(
    viewModel: AccountViewModel = hiltViewModel()
) {

    val email by viewModel.email.collectAsStateWithLifecycle()
    val password by viewModel.password.collectAsStateWithLifecycle()
    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle(null)
    val userMessage by viewModel.userMessage.collectAsStateWithLifecycle()

    if (currentUser == null) {
        NewOrExistingUser(
            emailValue = email,
            passwordValue = password,
            onEmailChanged = viewModel::onEmailChanged,
            onPasswordChanged = viewModel::onPasswordChanged,
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
            TextButton(onClick = { viewModel.onSignOut() }) {
                Text(text = "Sign Out")
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun NewOrExistingUser(
    emailValue: String?,
    passwordValue: String?,
    onEmailChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
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
                        onClick = {
                            showSignIn = true
                        }) {
                        Text(text = "I have an account")
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            isNew = true
                            showSignIn = true
                        }) {
                        Text(text = "I'd like to sign up")
                    }
                }

            }

        } else {
            var passwordVisible by rememberSaveable { mutableStateOf(false) }

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
                        text = stringResource(R.string.sign_in),
                        style = MaterialTheme.typography.bodyLarge
                    )
                    OutlinedTextField(
                        value = emailValue ?: "",
                        onValueChange = { onEmailChanged(it) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Email,
                                contentDescription = "Email Icon"
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        value = passwordValue ?: "",
                        onValueChange = { onPasswordChanged(it) },
                        trailingIcon = {
                            val iconState =
                                if (passwordVisible) Pair(R.drawable.visibility, "Hide password")
                                else Pair(R.drawable.visibility_off, "Show password")

                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    painter = painterResource(id = iconState.first),
                                    iconState.second
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    userMessage?.let {
                        Text(text = userMessage)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            if (isNew) onSignUp() else onSignIn()
                        }) {
                        Text(text = if (isNew) "Sign up" else "Sign in")
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            isNew = false
                            showSignIn = false
                        }) {
                        Text(text = "Cancel")
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SignInPreview() {

    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    NewOrExistingUser(email, password, { email = it }, { password = it }, {}, {}, "Invalid credentials.")
}