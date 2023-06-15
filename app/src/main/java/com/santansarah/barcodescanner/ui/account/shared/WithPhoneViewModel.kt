package com.santansarah.barcodescanner.ui.account.shared

import androidx.lifecycle.viewModelScope
import com.santansarah.barcodescanner.domain.interfaces.IUserRepository
import com.santansarah.barcodescanner.domain.models.PhoneAuthUIState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber

abstract class WithPhoneViewModelBase(
    private val vmDispatcher: CoroutineDispatcher,
    private val userRepository: IUserRepository
) : SignInViewModelBase(vmDispatcher, userRepository) {

    open val phoneAuthUIState = MutableStateFlow(PhoneAuthUIState())

    fun onVerifyCode(isNew: Boolean) {
        viewModelScope.launch(dispatcher) {
            userRepository.verifyCode(phoneAuthUIState.value.verificationCode, isNew)
        }
    }

    fun onVerificationCodeChanged(newCode: String) {
        phoneAuthUIState.update {
            it.copy(verificationCode = newCode)
        }
    }

    fun onAddPhone() {
        viewModelScope.launch(dispatcher) {
            with(userUIState.value) {
                userRepository.sendVerificationSMS(
                    email, password,
                    phoneAuthUIState.value.phone
                )
            }
            Timber.d("user: ${userRepository.currentUser.toString()}")
        }
    }

    fun onResendVerification() {
        viewModelScope.launch(dispatcher) {
            userRepository.resendVerificationSMS()
            Timber.d("user: ${userRepository.currentUser.toString()}")
        }
    }

}