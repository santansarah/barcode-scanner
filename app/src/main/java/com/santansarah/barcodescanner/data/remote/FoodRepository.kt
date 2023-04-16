package com.santansarah.barcodescanner.data.remote

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.santansarah.barcodescanner.data.paging.ProductSearchPagingSource
import com.santansarah.barcodescanner.domain.ErrorCode
import com.santansarah.barcodescanner.utils.ServiceResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FoodRepository @Inject constructor(
    private val api: FoodApi
) {

    suspend fun getInfoByBarCode(barCode: String): Flow<ServiceResult<ItemListing>> {
        return flow {
            emit(ServiceResult.Loading)
            try {
                val result = api.getInfoByBarCode(
                    barCode = barCode,
                    fields = Product.fields.joinToString(",")
                )
                Timber.d(result.toString())
                emit(ServiceResult.Success(result))
            } catch (e: Exception) {
                e.printStackTrace()
                emit(ServiceResult.Error(ErrorCode.API_ERROR))
            }
        }
    }

    fun getSearchResults(searchText: String): Flow<PagingData<SearchProductItem>> {
        return Pager(
            config = PagingConfig(pageSize = 24),
            pagingSourceFactory = {
                ProductSearchPagingSource(foodApi = api, searchText = searchText)
            }
        ).flow
    }

    /*fun getSearchResults(searchText: String): ServiceResult<Flow<PagingData<SearchProductItem>>> {
        return try {
            ServiceResult.Success(Pager(
                config = PagingConfig(pageSize = 24),
                pagingSourceFactory = {
                    ProductSearchPagingSource(foodApi = api, searchText = searchText)
                }
            ).flow)
        } catch (e: Exception) {
            Timber.d("api error from repo: ${e.message}")
            ServiceResult.Error(ErrorCode.valueOf(e.message!!))
        }
    }*/

    suspend fun getSearchResults(searchText: String, page: Int): ServiceResult<SearchResults> {
        return try {
            val result = api.searchProducts(
                searchText = searchText,
                fields = SearchProductItem.fields.joinToString(","),
                page = page
            )
            Timber.d(result.toString())
            ServiceResult.Success(result)
        } catch (e: Exception) {
            e.printStackTrace()
            ServiceResult.Error(ErrorCode.API_ERROR)
        }
    }

}