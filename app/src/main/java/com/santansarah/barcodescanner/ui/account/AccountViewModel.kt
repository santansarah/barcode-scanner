package com.santansarah.barcodescanner.ui.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.santansarah.barcodescanner.di.IoDispatcher
import com.santansarah.barcodescanner.domain.interfaces.IPhoneAuthorization
import com.santansarah.barcodescanner.domain.interfaces.IUserRepository
import com.santansarah.barcodescanner.domain.models.UserUIState
import com.santansarah.barcodescanner.domain.models.PhoneAuthUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class AccountViewModel @Inject constructor(
    @IoDispatcher val vmDispatcher: CoroutineDispatcher,
    private val userRepository: IUserRepository
) : SignInViewModelBase(vmDispatcher, userRepository) {

    fun onSignUp() {
        if (validateSignIn()) {
            viewModelScope.launch(dispatcher) {
                with(userUIState.value!!) {
                    userRepository.signUp(email, password)
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

}