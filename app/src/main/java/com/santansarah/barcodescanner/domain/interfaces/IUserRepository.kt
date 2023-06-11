package com.santansarah.barcodescanner.domain.interfaces

import com.santansarah.barcodescanner.domain.models.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

interface IUserRepository {

    val currentUser: User?
    val signInError: MutableStateFlow<String?>

    fun userSignedIn(scope: CoroutineScope): Flow<Boolean>
    suspend fun signIn(email: String, password: String): Boolean
    suspend fun verifyEmail()
    suspend fun signUp(email: String, password: String): Boolean
    suspend fun signOut()

}