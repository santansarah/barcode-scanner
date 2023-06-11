package com.santansarah.barcodescanner.ui.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.santansarah.barcodescanner.di.IoDispatcher
import com.santansarah.barcodescanner.domain.interfaces.IUserRepository
import com.santansarah.barcodescanner.domain.models.UserUIState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * Open/Closed.
 */
abstract class SignInViewModelBase constructor(
    @IoDispatcher val dispatcher: CoroutineDispatcher,
    private val userRepository: IUserRepository
) : ViewModel() {

    val userUIState = MutableStateFlow(
        UserUIState(onEmailChanged = { onEmailChanged(it) },
            onPasswordChanged = { onPasswordChanged(it) },
            onVerifyEmail = { onVerifyEmail() })
    )

    val currentUser = userRepository.userSignedIn(viewModelScope)
        .map { userRepository.currentUser }

    val userMessage = userRepository.signInError

    fun onSignIn() {

        if (validateSignIn()) {
            viewModelScope.launch(dispatcher) {
                with(userUIState.value) {
                    userRepository.signIn(email, password)
                }
                Timber.d("user: ${userRepository.currentUser.toString()}")
            }
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