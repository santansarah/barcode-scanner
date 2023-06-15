package com.santansarah.barcodescanner.ui.account

import androidx.lifecycle.viewModelScope
import com.santansarah.barcodescanner.di.IoDispatcher
import com.santansarah.barcodescanner.domain.interfaces.IUserRepository
import com.santansarah.barcodescanner.ui.account.shared.SignInViewModelBase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class AccountViewModel @Inject constructor(
    @IoDispatcher val vmDispatcher: CoroutineDispatcher,
    private val userRepository: IUserRepository
) : SignInViewModelBase(vmDispatcher, userRepository) {


}