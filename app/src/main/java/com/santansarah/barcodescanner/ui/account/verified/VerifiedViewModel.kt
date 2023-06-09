package com.santansarah.barcodescanner.ui.account.verified

import androidx.lifecycle.viewModelScope
import com.santansarah.barcodescanner.data.local.AppPreferences
import com.santansarah.barcodescanner.data.local.AppPreferencesRepository
import com.santansarah.barcodescanner.di.IoDispatcher
import com.santansarah.barcodescanner.domain.interfaces.IUserRepository
import com.santansarah.barcodescanner.domain.models.PhoneAuthUIState
import com.santansarah.barcodescanner.ui.account.shared.SignInViewModelBase
import com.santansarah.barcodescanner.ui.account.shared.WithPhoneViewModelBase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class VerifiedViewModel @Inject constructor(
    @IoDispatcher val vmDispatcher: CoroutineDispatcher,
    private val userRepository: IUserRepository,
   // private val appPreferences: AppPreferencesRepository
) : SignInViewModelBase(vmDispatcher, userRepository) {


}