package com.santansarah.barcodescanner.domain.models

/*
enum class PhoneAuthState {
    PHONE_NOT_ADDED,
    CODE_SENT,
    CREDENTIALS_SET,
    VERIFY_FAILED,
    ENROLLED,
    VERIFYING_EMAIL
}
*/

enum class SignInState {
    NOT_SIGNED_IN,
    AUTHORIZED,
    NEEDS_2FA,
    CREDENTIAL_ERROR,
    USER_NOT_FOUND,
    CODE_SENT,
    CREDENTIALS_SET,
    VERIFY_FAILED,
    ENROLLED,
    VERIFYING_EMAIL,
    SMS_AUTHORIZED
}