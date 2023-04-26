package com.santansarah.barcodescanner.ui.search

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.santansarah.barcodescanner.BarcodeScanner
import com.santansarah.barcodescanner.data.paging.ProductSearchPagingSource
import com.santansarah.barcodescanner.data.remote.FoodRepository
import com.santansarah.barcodescanner.data.remote.ItemListing
import com.santansarah.barcodescanner.data.remote.SearchProductItem
import com.santansarah.barcodescanner.data.remote.SearchResults
import com.santansarah.barcodescanner.di.IoDispatcher
import com.santansarah.barcodescanner.domain.models.AppRouteArgs.BARCODE
import com.santansarah.barcodescanner.domain.models.AppRouteArgs.SEARCH_TEXT
import com.santansarah.barcodescanner.utils.ServiceResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
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

    var rememberListScrollState = Pair(0, 0)

    private val searchStringFromState = savedStateHandle[SEARCH_TEXT] ?: ""
    val searchResults = MutableStateFlow<PagingData<SearchProductItem>>(PagingData.empty())
    val searchText = MutableStateFlow(searchStringFromState)

    init {
        getProducts(searchStringFromState)
    }

    fun onSearchValueChanged(newText: String) {
        searchText.value = newText
    }

    fun onSearch() {
        getProducts(searchText.value)
    }

    private fun getProducts(searchText: String) {
        viewModelScope.launch {
            foodRepository.getSearchResults(searchText).cachedIn(viewModelScope).collect {
                searchResults.value = it
            }
        }
    }

    /*private fun getProducts(searchText: String) {
        viewModelScope.launch {
            when (val result = foodRepository.getSearchResults(searchText)) {
                is ServiceResult.Success -> {
                    result.data.cachedIn(viewModelScope).collect {
                        searchResults.value = it
                    }
                }
                is ServiceResult.Error -> {
                    searchResults.value = PagingData.empty()
                    searchError.value = true
                }
                else -> {}
            }

        }
    }*/

}