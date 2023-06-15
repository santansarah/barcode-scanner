package com.santansarah.barcodescanner.ui.account.signup

import androidx.lifecycle.viewModelScope
import com.santansarah.barcodescanner.di.IoDispatcher
import com.santansarah.barcodescanner.domain.interfaces.IUserRepository
import com.santansarah.barcodescanner.ui.account.shared.SignInViewModelBase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class SignUpViewModel @Inject constructor(
    @IoDispatcher val vmDispatcher: CoroutineDispatcher,
    private val userRepository: IUserRepository
) : SignInViewModelBase(vmDispatcher, userRepository) {

    fun onSignUp() {
        if (validateSignIn()) {
            viewModelScope.launch(dispatcher) {
                with(userUIState.value) {
                    userRepository.signUp(email, password)
                }
                Timber.d("user: ${userRepository.currentUser.toString()}")
            }
        }
    }


}