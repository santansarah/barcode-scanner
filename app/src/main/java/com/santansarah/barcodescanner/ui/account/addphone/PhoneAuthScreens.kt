package com.santansarah.barcodescanner.ui.account.addphone

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.santansarah.barcodescanner.domain.models.PhoneAuthUIState
import com.santansarah.barcodescanner.domain.models.SignInState
import com.santansarah.barcodescanner.utils.toPhone
import timber.log.Timber


@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun AddPhone(
    phoneAuthUIState: PhoneAuthUIState,
    signInState: SignInState
) {

    var showButtonClicked by rememberSaveable {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier.padding(horizontal = 20.dp)
    ) {

        OutlinedTextField(
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            value = phoneAuthUIState.phone ?: "",
            onValueChange = { phoneAuthUIState.onPhoneChanged(it) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Phone,
                    contentDescription = "Phone"
                )
            },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = {
                mobileNumberFilter(it)
            }
        )

        Spacer(modifier = Modifier.height(26.dp))

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                showButtonClicked = true
                phoneAuthUIState.onAddPhone()
            }) {
            AnimatedContent(targetState = showButtonClicked, label = "") { showClicked ->
                if (showClicked)
                    if (signInState != SignInState.VERIFY_FAILED)
                        CircularProgressIndicator(
                            color = Color.White
                        )
                    else {
                        showButtonClicked = false
                        Text(text = "Send Verification Code")
                    }
                else
                    Text(text = "Send Verification Code")
            }
        }
    }

}


fun mobileNumberFilter(text: AnnotatedString): TransformedText {

    // Annotated text comes in as what was typed, before any transformation.
    // So to get the correct offset, I can just apply my formatting, then
    // see how long it is.
    val formattedPhone = text.text.toPhone()
    val origToTransformedOffset = formattedPhone.length
    val formattedCharacterCount = formattedPhone.filterNot {
        it.isDigit()
    }.count()
    Timber.d("formattedCharCount: $formattedCharacterCount")

    // Now, I just convert the [formattedPhone] to an annotated string.
    val annotatedString = AnnotatedString(formattedPhone)

    val phoneNumberOffsetTranslator = object : OffsetMapping {
        // This moves the cursor along as a user types, and the text is auto-formatted.
        override fun originalToTransformed(offset: Int): Int =
            origToTransformedOffset

        // This sends the cursor back as a user deletes digits
        override fun transformedToOriginal(offset: Int): Int {
            Timber.d("transOffset: $offset")
            return if (offset > 3) offset - formattedCharacterCount else offset
        }
    }

    return TransformedText(annotatedString, phoneNumberOffsetTranslator)
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun VerifyCode(
    phoneAuthUIState: PhoneAuthUIState,
    userMessage: String?,
    onResend: () -> Unit,
    signInState: SignInState,
    isNew: Boolean
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            modifier = Modifier.padding(bottom = 10.dp),
            text = "We've sent a verification code to your phone.",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Enter your verification code:",
            //style = MaterialTheme.typography.headlineSmall,
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = phoneAuthUIState.verificationCode,
            onValueChange = { phoneAuthUIState.onVerificationCodeChanged(it) },
            modifier = Modifier.fillMaxWidth()
        )

        userMessage?.let {
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = it, color = MaterialTheme.colorScheme.error)
        }

        Spacer(modifier = Modifier.height(26.dp))

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                phoneAuthUIState.onVerifyCode(isNew)
            }) {
            Text(text = "Continue")
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = { onResend() }) {
            Text(text = "Resend Code")
        }
    }
}


@Preview(showBackground = true)
@Composable
fun AddPhoneFieldPreview() {

    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    Column {
        AddPhone(
            phoneAuthUIState = PhoneAuthUIState(), SignInState.AUTHORIZED
        )
    }
}

@Preview(showBackground = true)
@Composable
fun VerifyCodePreview() {

    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    Column {
        VerifyCode(phoneAuthUIState = PhoneAuthUIState(),
            null, {},
            SignInState.AUTHORIZED,
            true)
    }
}