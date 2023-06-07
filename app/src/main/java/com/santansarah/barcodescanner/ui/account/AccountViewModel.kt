package com.santansarah.barcodescanner.ui.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.santansarah.barcodescanner.di.IoDispatcher
import com.santansarah.barcodescanner.domain.interfaces.IUserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class AccountViewModel @Inject constructor(
    @IoDispatcher val dispatcher: CoroutineDispatcher,
    private val userRepository: IUserRepository
) : ViewModel() {

    val email = MutableStateFlow<String?>(null)
    val password = MutableStateFlow<String?>(null)

    val currentUser = userRepository.userSignedIn(viewModelScope)
        .map { userRepository.currentUser }

    val userMessage = userRepository.signInError

    fun onSignIn() {

        if (validateSignIn()) {
            viewModelScope.launch(dispatcher) {
                userRepository.signIn(email.value!!, password.value!!)
                Timber.d("user: ${userRepository.currentUser.toString()}")
            }
        }
    }

    fun onSignUp() {
        if (validateSignIn()) {
            viewModelScope.launch(dispatcher) {
                userRepository.signUp(email.value!!, password.value!!)
                Timber.d("user: ${userRepository.currentUser.toString()}")
            }
        }
    }

    private fun validateSignIn(): Boolean {
        return true
    }

    fun onEmailChanged(newEmail: String) {
        email.value = newEmail
    }

    fun onPasswordChanged(newPassword: String) {
        password.value = newPassword
    }

    fun onSignOut() {
        viewModelScope.launch(dispatcher) {
            userRepository.signOut()
        }
    }

}