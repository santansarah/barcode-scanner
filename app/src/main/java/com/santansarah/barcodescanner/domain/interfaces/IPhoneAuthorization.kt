package com.santansarah.barcodescanner.domain.interfaces

import com.santansarah.barcodescanner.domain.models.PhoneAuthState
import kotlinx.coroutines.flow.MutableStateFlow

interface IPhoneAuthorization {

    val phoneAuthState: MutableStateFlow<PhoneAuthState>

    suspend fun sendVerificationSMS(phoneNumber: String)
    fun verifyCode(verificationCode: String)
    suspend fun enrollUser()
    fun emailVerified()
}


