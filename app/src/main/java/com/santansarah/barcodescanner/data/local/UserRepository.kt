package com.santansarah.barcodescanner.data.local

import com.google.firebase.FirebaseException
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthMultiFactorException
import com.google.firebase.auth.MultiFactorResolver
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.PhoneMultiFactorGenerator
import com.google.firebase.auth.PhoneMultiFactorInfo
import com.google.firebase.auth.ktx.actionCodeSettings
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.santansarah.barcodescanner.domain.interfaces.IUserRepository
import com.santansarah.barcodescanner.domain.models.SignInState
import com.santansarah.barcodescanner.domain.models.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * D: Don't hide dependencies. BUT, this class is specifically meant for Firebase,
 * so I think it's ok here.
 */
class UserRepository @Inject constructor() : IUserRepository {

    private val auth = Firebase.auth
    private var user = auth.currentUser
    private lateinit var verifyId: String
    private lateinit var forceResend: PhoneAuthProvider.ForceResendingToken
    private lateinit var authCredential: PhoneAuthCredential

    override val signInError = MutableStateFlow<String?>(null)
    override val signInState = MutableStateFlow(SignInState.NOT_SIGNED_IN)

    /*init {
        if (user != null)
            signInState.value = SignInState.AUTHORIZED
    }*/

    override fun currentUser(scope: CoroutineScope): Flow<User?> = callbackFlow  {
        val authStateListener = FirebaseAuth.AuthStateListener { auth ->
            Timber.d("auth listener called...")
            trySend(auth.currentUser?.let {
                signInState.value = SignInState.AUTHORIZED
                User(
                    email = it.email!!,
                    displayName = it.displayName,
                    profileUrl = it.photoUrl?.path,
                    phoneNumber = it.phoneNumber,
                    isEmailVerified = it.isEmailVerified
                )
            })
        }
        auth.addAuthStateListener(authStateListener)
        awaitClose {
            auth.removeAuthStateListener(authStateListener)
        }
    }.stateIn(scope, SharingStarted.WhileSubscribed(), null)

    /**
     * L: Even though the user is coming from Firebase, we still make it
     * a [User] so it remains generic.
     */
    override val currentUser: User?
        get() = auth.currentUser?.let {
            Timber.d("user: ${it.isEmailVerified}")
            User(
                email = it.email!!,
                displayName = it.displayName,
                profileUrl = it.photoUrl?.path,
                phoneNumber = it.phoneNumber,
                isEmailVerified = it.isEmailVerified
            )
        }

    val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            Timber.d("onVerificationCompleted")
            authCredential = credential
            signInState.value = SignInState.SMS_AUTHORIZED
        }

        override fun onVerificationFailed(e: FirebaseException) {
            Timber.d("callback error: $e")
            signInState.value = SignInState.VERIFY_FAILED
        }

        override fun onCodeSent(
            verificationId: String, forceResendingToken: PhoneAuthProvider.ForceResendingToken
        ) {
            Timber.d("onCodeSent: $verificationId, $forceResendingToken")
            verifyId = verificationId
            forceResend = forceResendingToken
            signInState.value = SignInState.CODE_SENT
        }
    }

    override suspend fun reloadUser() {
        auth.currentUser?.reload()?.await()
        user = auth.currentUser
        Timber.d("user verified email: ${user?.isEmailVerified}")
        if (user?.isEmailVerified == true && signInState.value == SignInState.VERIFYING_EMAIL)
            signInState.value = SignInState.AUTHORIZED
    }

    override suspend fun signUp(email: String, password: String) {
        return try {
            val authResult = auth.createUserWithEmailAndPassword(email, password).await()
            signInState.value = SignInState.VERIFYING_EMAIL
            verifyEmail()
        } catch (e: Exception) {
            signInError.value = e.message
            Timber.d(e.message)
            signInState.value = SignInState.CREDENTIAL_ERROR
        }
    }

    override suspend fun verifyEmail() {
        try {
            val actionCodeSettings = actionCodeSettings {
                url = "https://github.com/santansarah"
            }
            auth.currentUser?.sendEmailVerification(actionCodeSettings)?.await()
            Timber.d("email sent")
        } catch (e: Exception) {
            Timber.d("verify email error: $e")
            signInError.value = e.message
        }
    }

    override suspend fun signIn(email: String, password: String) {
        return try {
            val authResult = auth.signInWithEmailAndPassword(email, password).await()
            if (authResult.user != null)
                signInState.value = SignInState.AUTHORIZED
            else
                signInState.value = SignInState.CREDENTIAL_ERROR
        } catch (e: Exception) {
            signInError.value = e.message
            Timber.d(e.toString())
            when (e) {
                is FirebaseAuthMultiFactorException -> {
                    val multiFactorResolver =
                        e.resolver

                    sendVerificationSMS(multiFactorResolver)
                    signInState.value = SignInState.NEEDS_2FA

                }
                is FirebaseAuthInvalidUserException -> signInState.value = SignInState.USER_NOT_FOUND
                is FirebaseAuthInvalidCredentialsException -> signInState.value = SignInState.CREDENTIAL_ERROR
                else -> signInState.value = SignInState.CREDENTIAL_ERROR
            }
        }
    }

    override suspend fun signOut() {
        auth.signOut()
        signInState.value = SignInState.NOT_SIGNED_IN
    }

    override suspend fun sendVerificationSMS(email: String, password: String, phoneNumber: String) {

        val credential = EmailAuthProvider
            .getCredential(email, password)

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

    override suspend fun resendVerificationSMS() {

        user?.phoneNumber?.let {
            val phoneAuthOptions = PhoneAuthOptions.newBuilder()
                .setPhoneNumber(it)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setCallbacks(callbacks)
                .setForceResendingToken(forceResend)
                .build()

            PhoneAuthProvider.verifyPhoneNumber(phoneAuthOptions)
        }

    }
    override suspend fun sendVerificationSMS(multiFactorResolver: MultiFactorResolver) {
        val selectedHint =
            multiFactorResolver.hints[0] as PhoneMultiFactorInfo

        val phoneAuthOptions = PhoneAuthOptions.newBuilder()
            .setMultiFactorHint(selectedHint)
            .setTimeout(30L, TimeUnit.SECONDS)
            .setMultiFactorSession(multiFactorResolver.session)
            .setCallbacks(callbacks) // Optionally disable instant verification.
            // .requireSmsValidation(true)
            .build()

        PhoneAuthProvider.verifyPhoneNumber(phoneAuthOptions)
    }

    override suspend fun verifyCode(verificationCode: String, isNew: Boolean) {
        // Ask user for the verification code.
        authCredential = PhoneAuthProvider.getCredential(verifyId, verificationCode)
        signInState.value = SignInState.SMS_AUTHORIZED

        if (isNew)
            enrollUser()
    }

    override suspend fun enrollUser() {
        val multiFactorAssertion = PhoneMultiFactorGenerator.getAssertion(authCredential)

        val result = FirebaseAuth.getInstance()
            .currentUser
            ?.multiFactor
            ?.enroll(multiFactorAssertion, "My personal phone number")?.await()

        Timber.d("enroll: ${result.toString()}")
        signInState.value = SignInState.ENROLLED
    }
}