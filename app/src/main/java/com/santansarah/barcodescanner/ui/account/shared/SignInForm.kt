package com.santansarah.barcodescanner.ui.account.shared

import android.view.LayoutInflater
import android.widget.EditText
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.Autofill
import androidx.compose.ui.autofill.AutofillNode
import androidx.compose.ui.autofill.AutofillType
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalAutofill
import androidx.compose.ui.platform.LocalAutofillTree
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.widget.doAfterTextChanged
import com.google.android.material.textfield.TextInputEditText
import com.santansarah.barcodescanner.R
import com.santansarah.barcodescanner.domain.models.UserUIState
import com.santansarah.barcodescanner.ui.components.*

@Composable
@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
fun SignInForm(
    userUIState: UserUIState,
) {
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    val autofill = LocalAutofill.current

    Column(
        modifier = Modifier.padding(horizontal = 20.dp)
    ) {

        OutlinedTextField(
            value = userUIState.email,
            onValueChange = { userUIState.onEmailChanged(it) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = "Email Icon"
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .autofill(
                    autofillTypes = listOf(AutofillType.EmailAddress,
                        AutofillType.NewUsername, AutofillType.Username),
                    onFill = { userUIState.onEmailChanged(it) },
                )

        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            value = userUIState.password,
            onValueChange = { userUIState.onPasswordChanged(it) },
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
            modifier = Modifier.fillMaxWidth()
                .autofill(
                    autofillTypes = listOf(AutofillType.Password),
                    onFill = { userUIState.onPasswordChanged(it) },
                ),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation()
        )

        AndroidView(

        modifier = Modifier
                .fillMaxWidth(),
            factory = { context ->
                val layout = LayoutInflater.from(context).inflate(R.layout.password_field, null)
                layout.findViewById<EditText>(R.id.password).apply {
                    doAfterTextChanged {
                        userUIState.onPasswordChanged(it.toString())
                    }
                }
                layout
            },
            update = {

            }
        )



    }
}

@Composable
fun SignInButtons(
    isNew: Boolean,
    onSignUp: () -> Unit,
    onSignIn: () -> Unit,
    onCancel: () -> Unit
) {
    Column(
        modifier = Modifier.padding(horizontal = 20.dp)
    ) {

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
            onClick = { onCancel() }) {
            Text(text = "Cancel")
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SignInFormPreview() {

    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    SignInForm(
        userUIState = UserUIState(
            email = email,
            password = password,
            onEmailChanged = { email = it },
            onPasswordChanged = { password = it }
        )
    )
}
