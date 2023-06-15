package com.santansarah.barcodescanner.domain.models

data class User(
    val email: String,
    val displayName: String?,
    val profileUrl: String?,
    val phoneNumber: String?,
    val isEmailVerified: Boolean
)

/**
 * Creating combined state classes like this prevents the issue of passing
 * too many parameters to your composables, and makes setting up previews
 * a breeze, because the functions here are initialized by default.
 */
data class UserUIState(
    val email: String = "",
    val password: String = "",
    val onEmailChanged: (String) -> Unit = {},
    val onPasswordChanged: (String) -> Unit = {},
    val onVerifyEmail: () -> Unit = {},
    val onSignOut: () -> Unit = {}
)

data class PhoneAuthUIState(
    val phone: String = "",
    val onPhoneChanged: (String) -> Unit = {},
    val verificationCode: String = "",
    val onVerificationCodeChanged: (String) -> Unit = {},
    val onVerifyCode: (Boolean) -> Unit = {},
    val onAddPhone: () -> Unit = {}
)