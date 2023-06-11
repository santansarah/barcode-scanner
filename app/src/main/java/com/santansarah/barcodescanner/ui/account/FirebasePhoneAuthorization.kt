package com.santansarah.barcodescanner.ui.account

import com.google.firebase.FirebaseException
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.PhoneMultiFactorGenerator
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.santansarah.barcodescanner.domain.interfaces.IPhoneAuthorization
import com.santansarah.barcodescanner.domain.models.PhoneAuthState
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@ViewModelScoped
class FirebasePhoneAuthorization @Inject constructor(): IPhoneAuthorization {

    private val auth = Firebase.auth
    private val user = auth.currentUser
    private lateinit var verifyId: String
    private lateinit var forceResend: PhoneAuthProvider.ForceResendingToken
    private lateinit var authCredential: PhoneAuthCredential

    override val phoneAuthState = MutableStateFlow(PhoneAuthState.NOT_ADDED)

    override fun emailVerified() {
        if (phoneAuthState.value == PhoneAuthState.VERIFYING_EMAIL)
            phoneAuthState.value = PhoneAuthState.NOT_ADDED
    }

    val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            Timber.d("onVerificationCompleted")
            authCredential = credential
            phoneAuthState.value = PhoneAuthState.CREDENTIALS_SET
        }

        override fun onVerificationFailed(e: FirebaseException) {
            Timber.d("callback error: $e")
            phoneAuthState.value = PhoneAuthState.VERIFY_FAILED
        }

        override fun onCodeSent(
            verificationId: String, forceResendingToken: PhoneAuthProvider.ForceResendingToken
        ) {
            Timber.d("onCodeSent: $verificationId, $forceResendingToken")
            verifyId = verificationId
            forceResend = forceResendingToken
            phoneAuthState.value = PhoneAuthState.CODE_SENT
        }
    }

    override suspend fun sendVerificationSMS(phoneNumber: String) {

        val credential = EmailAuthProvider
            .getCredential("", "")

        user!!.reauthenticate(credential).await()

        val userSession = user!!.multiFactor.session.await()

        val phoneAuthOptions = PhoneAuthOptions.newBuilder()
            .setPhoneNumber("+1$phoneNumber")
            .setTimeout(30L, TimeUnit.SECONDS)
            .setMultiFactorSession(userSession)
            .setCallbacks(callbacks)
            .build()

        PhoneAuthProvider.verifyPhoneNumber(phoneAuthOptions)

    }

    override fun verifyCode(verificationCode: String) {
        // Ask user for the verification code.
        authCredential = PhoneAuthProvider.getCredential(verifyId, verificationCode)
        phoneAuthState.value = PhoneAuthState.CREDENTIALS_SET
    }

    override suspend fun enrollUser() {
        val multiFactorAssertion = PhoneMultiFactorGenerator.getAssertion(authCredential)

        val result = FirebaseAuth.getInstance()
            .currentUser
            ?.multiFactor
            ?.enroll(multiFactorAssertion, "My personal phone number")?.await()

        Timber.d("enroll: ${result.toString()}")
        phoneAuthState.value = PhoneAuthState.ENROLLED
    }

}


