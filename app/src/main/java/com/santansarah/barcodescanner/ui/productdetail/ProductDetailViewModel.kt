package com.santansarah.barcodescanner.ui.productdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.santansarah.barcodescanner.BarcodeScanner
import com.santansarah.barcodescanner.data.remote.FoodRepository
import com.santansarah.barcodescanner.data.remote.ItemListing
import com.santansarah.barcodescanner.di.IoDispatcher
import com.santansarah.barcodescanner.domain.ErrorCode
import com.santansarah.barcodescanner.domain.models.AppDestinations
import com.santansarah.barcodescanner.domain.models.AppDestinations.HOME
import com.santansarah.barcodescanner.domain.models.AppDestinations.SEARCH
import com.santansarah.barcodescanner.domain.models.AppRouteArgs.BARCODE
import com.santansarah.barcodescanner.domain.models.AppRouteArgs.FROM_SCREEN
import com.santansarah.barcodescanner.utils.ServiceResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flattenConcat
import kotlinx.coroutines.flow.flattenMerge
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

data class ProductDetailUiState(
    val itemListing: ItemListing? = null,
    val isLoading: Boolean = true,
    val userMessage: String? = null,
    val barcode: String
)

@OptIn(FlowPreview::class)
@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val barcodeScanner: BarcodeScanner,
    @IoDispatcher val dispatcher: CoroutineDispatcher,
    savedStateHandle: SavedStateHandle,
    private val foodRepository: FoodRepository
) : ViewModel() {

    private val _barcodeSentToDetails = savedStateHandle
        .getStateFlow(BARCODE, "000000000")
    private val _barcodeFromScanner = barcodeScanner.barCodeResults
        .filterNotNull()

    private val _barcodeToUse = flowOf(_barcodeSentToDetails, _barcodeFromScanner)
        .flattenMerge()
        .onEach { getProductDetail(it) }

    val fromScreen: String = checkNotNull(savedStateHandle[FROM_SCREEN])

    private val _itemListing = MutableStateFlow<ItemListing?>(null)
    private val _isLoading = MutableStateFlow(true)
    private val _productError = MutableStateFlow<String?>(null)

    val productDetailState = combine(
        _itemListing, _isLoading, _productError, _barcodeToUse
    ) { itemListing, isLoading, productError, barcodeToUse ->

        ProductDetailUiState(
            itemListing = itemListing,
            isLoading = isLoading,
            userMessage = productError,
            barcode = barcodeToUse
        )
    }.flowOn(dispatcher)
        //.onStart { emit("Loading...") }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ProductDetailUiState(barcode = "000000000")
        )

    /**
     * getInfoByBarCode uses the same [ErrorCode] enum as my PagingSource class.
     * Here, I just check to see if my ServiceResult wrapper returns an error.
     * If it does, I update productError, which is collected in my composable.
     */
    fun getProductDetail(barcode: String) {
        Timber.d("getting product detail")
        viewModelScope.launch(dispatcher) {
            foodRepository.getInfoByBarCode(barcode).collect {
                when (it) {
                    is ServiceResult.Loading -> {
                        _isLoading.value = true
                    }

                    is ServiceResult.Success -> {
                        _isLoading.value = false
                        _itemListing.value = it.data
                    }

                    is ServiceResult.Error -> {
                        _productError.value = it.error.message
                        _isLoading.value = false
                    }
                }
            }
        }
    }

    fun scanBarcode() {
        viewModelScope.launch(dispatcher) {
            barcodeScanner.startScan()
        }
    }

    fun clearBarcode() {
        barcodeScanner.barCodeResults.value = null
    }

}