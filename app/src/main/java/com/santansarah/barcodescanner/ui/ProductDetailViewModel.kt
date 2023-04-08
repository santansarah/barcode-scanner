package com.santansarah.barcodescanner.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.santansarah.barcodescanner.BarcodeScanner
import com.santansarah.barcodescanner.data.remote.FoodRepository
import com.santansarah.barcodescanner.data.remote.ItemListing
import com.santansarah.barcodescanner.di.IoDispatcher
import com.santansarah.barcodescanner.domain.models.AppRouteArgs.BARCODE
import com.santansarah.barcodescanner.utils.ServiceResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectIndexed
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

data class ProductDetailUiState(
    val itemListing: ItemListing? = null,
    val isLoading: Boolean = false,
    val userMessage: Int? = null,
)

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    @IoDispatcher val dispatcher: CoroutineDispatcher,
    savedStateHandle: SavedStateHandle,
    private val foodRepository: FoodRepository
) : ViewModel() {

    val barcodeFromState = savedStateHandle[BARCODE] ?: "000000000000"
    val itemListing = MutableStateFlow<ItemListing?>(null)
    val isLoading = MutableStateFlow(true)

    init {
        getProductDetail(barcodeFromState)
    }

    private fun getProductDetail(barcode: String) {
        Timber.d("getting product detail")
        viewModelScope.launch(dispatcher) {
            foodRepository.getInfoByBarCode(barcode).collect {
                when (it) {
                    is ServiceResult.Loading -> {
                        isLoading.value = true
                    }
                    is ServiceResult.Success -> {
                        isLoading.value = false
                        itemListing.value = it.data
                    }
                    is ServiceResult.Error -> {
                        isLoading.value = false
                    }
                }
            }
        }
    }

    fun clearBarcode() {
        //bar
    }

}