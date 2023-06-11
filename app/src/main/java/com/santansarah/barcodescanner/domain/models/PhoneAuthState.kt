package com.santansarah.barcodescanner.domain.models

enum class PhoneAuthState {
    NOT_ADDED,
    CODE_SENT,
    CREDENTIALS_SET,
    VERIFY_FAILED,
    ENROLLED,
    VERIFYING_EMAIL
}
