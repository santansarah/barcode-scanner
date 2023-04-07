package com.santansarah.barcodescanner.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.santansarah.barcodescanner.data.remote.FoodApi
import com.santansarah.barcodescanner.data.remote.FoodRepository
import com.santansarah.barcodescanner.data.remote.SearchProductItem
import com.santansarah.barcodescanner.data.remote.SearchResults
import com.santansarah.barcodescanner.domain.ErrorCode
import com.santansarah.barcodescanner.utils.ServiceResult
import timber.log.Timber
import javax.inject.Inject
import kotlin.math.ceil

class ProductSearchPagingSource constructor(
    private val foodApi: FoodApi,
    private val searchText: String
) : PagingSource<Int, SearchProductItem>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SearchProductItem> {
        val currentPage = params.key ?: 1

        Timber.d("currentPage: ${params.key}")

        when(val results = getSearchResults(searchText = searchText, page = currentPage)) {
            is ServiceResult.Success -> {

               /* val totalPages = ceil(results.data.count.toDouble()
                    .div(results.data.pageSize.toDouble())).toInt()*/
                val onLastPage = results.data.page.times(results.data.pageSize) >= results.data.count
                Timber.d("last page: $onLastPage")

                return LoadResult.Page(
                    data = results.data.products,
                    prevKey = if (currentPage == 1) null else currentPage - 1,
                    nextKey = if (onLastPage) null else results.data.page + 1
                )
            }
            is ServiceResult.Error -> {
                return LoadResult.Page(
                    data = emptyList(),
                    prevKey = null,
                    nextKey = null
                )
            }
        }
    }
    override fun getRefreshKey(state: PagingState<Int, SearchProductItem>): Int? {
        return state.anchorPosition
    }

    private suspend fun getSearchResults(searchText: String, page: Int): ServiceResult<SearchResults> {
        return try {
            val result = foodApi.searchProducts(
                searchText = searchText,
                fields = SearchProductItem.fields.joinToString(","),
                page = page)
            Timber.d(result.toString())
            ServiceResult.Success(result)
        } catch (e: Exception) {
            e.printStackTrace()
            ServiceResult.Error(ErrorCode.API_ERROR)
        }
    }
}