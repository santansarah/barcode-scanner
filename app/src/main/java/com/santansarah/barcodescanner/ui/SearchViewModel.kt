package com.santansarah.barcodescanner.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.santansarah.barcodescanner.BarcodeScanner
import com.santansarah.barcodescanner.data.remote.FoodRepository
import com.santansarah.barcodescanner.data.remote.ItemListing
import com.santansarah.barcodescanner.di.IoDispatcher
import com.santansarah.barcodescanner.domain.models.AppRouteArgs.BARCODE
import com.santansarah.barcodescanner.domain.models.AppRouteArgs.SEARCH_TEXT
import com.santansarah.barcodescanner.utils.ServiceResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class SearchViewModel @Inject constructor(
    @IoDispatcher val dispatcher: CoroutineDispatcher,
    private val savedStateHandle: SavedStateHandle,
    private val foodRepository: FoodRepository
) : ViewModel() {

    private val searchStringFromState = savedStateHandle.getStateFlow<String?>(SEARCH_TEXT,
        null)
        .filterNotNull()
        .flowOn(dispatcher)
        .onEach {
            Timber.d("search from savedState: $it")
            getProductDetail(it)
        }
        .launchIn(viewModelScope)

    val itemListing = MutableStateFlow<ItemListing?>(null)

    private fun getProductDetail(barcode: String) {
        Timber.d("getting product detail")
        viewModelScope.launch(dispatcher) {
            when(val result = foodRepository.getInfoByBarCode(barcode)) {
                is ServiceResult.Success -> {
                    itemListing.value = result.data
                }
                is ServiceResult.Error -> {}
            }
        }
    }

}