package com.santansarah.barcodescanner.ui.account.addphone

import androidx.lifecycle.viewModelScope
import com.santansarah.barcodescanner.di.IoDispatcher
import com.santansarah.barcodescanner.domain.interfaces.IUserRepository
import com.santansarah.barcodescanner.domain.models.PhoneAuthUIState
import com.santansarah.barcodescanner.ui.account.shared.WithPhoneViewModelBase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AddPhoneViewModel @Inject constructor(
    @IoDispatcher val vmDispatcher: CoroutineDispatcher,
    private val userRepository: IUserRepository
) : WithPhoneViewModelBase(vmDispatcher, userRepository) {

    override val phoneAuthUIState = MutableStateFlow(
        PhoneAuthUIState(
            onPhoneChanged = { onPhoneChanged(it) },
            onVerificationCodeChanged = { onVerificationCodeChanged(it) },
            onVerifyCode = { onVerifyCode(it) },
            onAddPhone = { onAddPhone() }
        )
    )

    fun enrollUser() {
        viewModelScope.launch {
            userRepository.enrollUser()
        }
    }

    private fun onPhoneChanged(newPhone: String) {
        phoneAuthUIState.update {
            it.copy(phone = newPhone)
        }
    }

}