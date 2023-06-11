package com.santansarah.barcodescanner.ui.account.verified

import androidx.lifecycle.viewModelScope
import com.santansarah.barcodescanner.di.IoDispatcher
import com.santansarah.barcodescanner.domain.interfaces.IPhoneAuthorization
import com.santansarah.barcodescanner.domain.interfaces.IUserRepository
import com.santansarah.barcodescanner.domain.models.PhoneAuthUIState
import com.santansarah.barcodescanner.ui.account.SignInViewModelBase
import com.santansarah.barcodescanner.utils.toPhone
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class VerifiedViewModel @Inject constructor(
    @IoDispatcher val vmDispatcher: CoroutineDispatcher,
    private val userRepository: IUserRepository,
    private val phoneAuthorization: IPhoneAuthorization
) : SignInViewModelBase(vmDispatcher, userRepository) {

    val phoneAuthUIState = MutableStateFlow(
        PhoneAuthUIState(
            onPhoneChanged = { onPhoneChanged(it) },
            onVerificationCodeChanged = { onVerificationCodeChanged(it) },
            onVerifyCode = { onVerifyCode() },
            onAddPhone = { onAddPhone() }
        )
    )

    val phoneAuthCurrentState = phoneAuthorization.phoneAuthState

    private fun onAddPhone() {
        viewModelScope.launch(dispatcher) {
            phoneAuthorization.sendVerificationSMS(phoneAuthUIState.value.phone)
            Timber.d("user: ${userRepository.currentUser.toString()}")
        }
    }

    private fun onVerifyCode() {
        viewModelScope.launch {
            phoneAuthorization.verifyCode(phoneAuthUIState.value.verificationCode)
        }
    }

    fun enrollUser() {
        viewModelScope.launch {
            phoneAuthorization.enrollUser()
        }
    }

    private fun onPhoneChanged(newPhone: String) {
        phoneAuthUIState.update {
            it.copy(phone = newPhone)
        }
    }

    private fun onVerificationCodeChanged(newCode: String) {
        phoneAuthUIState.update {
            it.copy(verificationCode = newCode)
        }
    }


}