package com.santansarah.barcodescanner.ui.account.verified

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.santansarah.barcodescanner.domain.models.PhoneAuthUIState
import com.santansarah.barcodescanner.utils.toPhone
import timber.log.Timber


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPhone(
    phoneAuthUIState: PhoneAuthUIState
) {

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
            visualTransformation = VisualTransformation {
                mobileNumberFilter(it)
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                phoneAuthUIState.onAddPhone()
            }) {
            Text(text = "Send Verification Code")
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
        override fun originalToTransformed(offset: Int): Int =
            origToTransformedOffset

        // I don't seem to need any modifications to the offset here.
        override fun transformedToOriginal(offset: Int): Int {
            Timber.d("transOffset: $offset")
            return if (offset > 3) offset - formattedCharacterCount else offset
        }
    }

    return TransformedText(annotatedString, phoneNumberOffsetTranslator)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerifyCode(
    phoneAuthUIState: PhoneAuthUIState
) {

    Text(text = "Enter your verification code:")
    Spacer(modifier = Modifier.height(16.dp))
    OutlinedTextField(
        value = phoneAuthUIState.verificationCode,
        onValueChange = { phoneAuthUIState.onVerificationCodeChanged(it) },
        leadingIcon = {
            Icon(
                imageVector = Icons.Outlined.Phone,
                contentDescription = "Phone"
            )
        },
        modifier = Modifier.fillMaxWidth()
    )

    Spacer(modifier = Modifier.height(16.dp))

    Button(
        modifier = Modifier.fillMaxWidth(),
        onClick = {
            phoneAuthUIState.onVerifyCode()
        }) {
        Text(text = "Verify Code")
    }

}


@Preview(showBackground = true)
@Composable
fun AddPhonePreview() {

    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    Column {
        AddPhone(
            phoneAuthUIState = PhoneAuthUIState()
        )
    }
}

@Preview(showBackground = true)
@Composable
fun VerifyCodePreview() {

    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    Column {
        VerifyCode(phoneAuthUIState = PhoneAuthUIState())
    }
}