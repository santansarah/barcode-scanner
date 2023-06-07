package com.santansarah.barcodescanner.data.local

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.santansarah.barcodescanner.domain.interfaces.IUserRepository
import com.santansarah.barcodescanner.domain.models.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject

/**
 * D: Don't hide dependencies. BUT, this class is specifically meant for Firebase,
 * so I think it's ok here.
 */
class UserRepository @Inject constructor() : IUserRepository {

    private val auth = Firebase.auth
    override val signInError = MutableStateFlow<String?>(null)

    /**
     * L: Even though the user is coming from Firebase, we still make it
     * a [User] so it remains generic.
     */
    override val currentUser: User?
        get() = auth.currentUser?.let {
            User(
                email = it.email!!,
                displayName = it.displayName,
                profileUrl = it.photoUrl?.path
            )
        }

    override fun userSignedIn(scope: CoroutineScope) = callbackFlow {
        val authStateListener = FirebaseAuth.AuthStateListener {
            trySend(auth.currentUser == null)
        }
        auth.addAuthStateListener(authStateListener)
        awaitClose {
            auth.removeAuthStateListener(authStateListener)
        }
    }.stateIn(scope, SharingStarted.WhileSubscribed(), auth.currentUser == null)

    override suspend fun signUp(email: String, password: String): Boolean {
        return try {
            val authResult = auth.createUserWithEmailAndPassword(email, password).await()
            (authResult.user != null)
        } catch (e: Exception) {
            signInError.value = e.message
            Timber.d(e.message)
            false
        }
    }

    override suspend fun signIn(email: String, password: String): Boolean {
        return try {
            val authResult = auth.signInWithEmailAndPassword(email, password).await()
            (authResult.user != null)
        } catch (e: Exception) {
            signInError.value = e.message
            Timber.d(e.toString())
            when (e) {
                is FirebaseAuthInvalidUserException -> false
                is FirebaseAuthInvalidCredentialsException -> false
                else -> false
            }
        }
    }

    override suspend fun signOut() {
        auth.signOut()
    }
}