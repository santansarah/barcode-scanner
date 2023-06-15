package com.santansarah.barcodescanner.ui.account.shared

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.santansarah.barcodescanner.data.local.AppPreferences
import com.santansarah.barcodescanner.data.local.AppPreferencesRepository
import com.santansarah.barcodescanner.domain.interfaces.IUserRepository
import com.santansarah.barcodescanner.domain.models.UserUIState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * Open/Closed.
 */
abstract class SignInViewModelBase(
    val dispatcher: CoroutineDispatcher,
    private val userRepository: IUserRepository,
    //private val appPreferences: AppPreferencesRepository
) : ViewModel() {

    //val userAppPreferences = appPreferences.appPreferencesFlow

    val userUIState = MutableStateFlow(
        UserUIState(onEmailChanged = { onEmailChanged(it) },
            onPasswordChanged = { onPasswordChanged(it) },
            onVerifyEmail = { onVerifyEmail() },
            onSignOut = { onSignOut() })
    )

    val currentUser = userRepository.currentUser(viewModelScope)

    private fun updateSignInStatus(isSignedIn: Boolean) {
        signInState
    }

    val userMessage = userRepository.signInError
    val signInState = userRepository.signInState

    fun reloadUser() {
        viewModelScope.launch(dispatcher) {
            userRepository.reloadUser()
        }
    }

    fun onSignIn() {

        if (validateSignIn()) {
            viewModelScope.launch(dispatcher) {
                with(userUIState.value) {
                    val result = userRepository.signIn(email, password)
                }
                Timber.d("user: ${userRepository.currentUser.toString()}")
            }
        }
    }

    fun onSignOut() {
        viewModelScope.launch(dispatcher) {
            userRepository.signOut()
        }
    }

    private fun onVerifyEmail() {
        viewModelScope.launch(dispatcher) {
            userRepository.verifyEmail()
        }
    }

    fun validateSignIn(): Boolean {
        return true
    }

    private fun onEmailChanged(newEmail: String) {
        userUIState.update {
            it.copy(email = newEmail)
        }
    }

    private fun onPasswordChanged(newPassword: String) {
        userUIState.update {
            it.copy(password = newPassword)
        }
    }

}