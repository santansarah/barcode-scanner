package com.santansarah.barcodescanner.ui.productdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.santansarah.barcodescanner.BarcodeScanner
import com.santansarah.barcodescanner.RecommendationService
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
    private val foodRepository: FoodRepository,
    private val recommendationService: RecommendationService
) : ViewModel() {

    /**
     * In my ProductDetailViewModel, first, I get the barcode as a StateFlow from the
     * SavedStateHandle. This comes from either the search results, or from a barcode scan on the
     * home page. If the product isn't found when a user scans a barcode from the Home
     * screen, I'm also giving the users the option to scan it again. This means that I also
     * need to track the barcode from my BarcodeScanner service, and that the barcode from the
     * initial SavedStateHandle could change.
     */
    private val _barcodeSentToDetails = savedStateHandle
        .getStateFlow(BARCODE, "000000000")

    private val _barcodeFromScanner = barcodeScanner.barCodeResults
        .filterNotNull()

    /**
     * Then, I use flattenMerge to get a single flow from both of my barcode flows. When the
     * ViewModel loads, first, I'll get the barcode from the SavedStateHandle. After that, the flow
     * returns barcodes that are emitted from my BarcodeScanner service. Using onEach, every time
     * a barcode is read, the flow automatically initiates a call to the Api to get the
     * product detail information. This way, I don't need to have an init function in my ViewModel.
     */
    private val _barcodeToUse = flowOf(_barcodeSentToDetails, _barcodeFromScanner)
        .flattenMerge()
        .onEach { getProductDetail(it) }

    /**
     * For navigation arguments, I also get which screen called the Product Details composable.
     * Here, it will be HOME or SEARCH. This is important for knowing which errors to show.
     */
    val fromScreen: String = checkNotNull(savedStateHandle[FROM_SCREEN])

    private val _itemListing = MutableStateFlow<ItemListing?>(null)
    private val _isLoading = MutableStateFlow(true)
    private val _productError = MutableStateFlow<String?>(null)

    val similiarItems = recommendationService.similarItemsList

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
     * Here, I just check to see if my ServiceResult wrapper returns an ErrorCode.
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

                        getSimilarProducts(it.data)
                    }

                    is ServiceResult.Error -> {
                        _productError.value = it.error.message
                        _isLoading.value = false
                    }
                }
            }
        }
    }

    private fun getSimilarProducts(itemListing: ItemListing) {
        Timber.d("getting similar products...")
        Timber.d("recid: $recommendationService")
        viewModelScope.launch(dispatcher) {
            itemListing.product?.let { product ->
                with(product) {
                    if (nutriments.fat != null
                        && nutriments.carbohydrates != null
                    ) {
                        Timber.d("calling recommend...")
                        recommendationService.recommend(
                            itemListing.code,
                            nutriments.fat.asDouble,
                            nutriments.carbohydrates.asDouble
                        )
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