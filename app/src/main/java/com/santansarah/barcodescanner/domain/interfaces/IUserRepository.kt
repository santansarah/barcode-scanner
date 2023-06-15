package com.santansarah.barcodescanner.domain.interfaces

import com.google.firebase.auth.MultiFactorResolver
import com.santansarah.barcodescanner.domain.models.SignInState
import com.santansarah.barcodescanner.domain.models.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

interface IUserRepository {

    val currentUser: User?
    val signInError: MutableStateFlow<String?>
    val signInState: MutableStateFlow<SignInState>

    fun currentUser(scope: CoroutineScope): Flow<User?>
    suspend fun signIn(email: String, password: String)
    suspend fun verifyEmail()
    suspend fun signUp(email: String, password: String)
    suspend fun signOut()
    suspend fun reloadUser()
    suspend fun sendVerificationSMS(email: String, password: String, phone: String)
    suspend fun sendVerificationSMS(multiFactorResolver: MultiFactorResolver)
    suspend fun resendVerificationSMS()
    suspend fun verifyCode(verificationCode: String, isNew: Boolean)
    suspend fun enrollUser()

}