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
                Timber.d("from api call: $result")
                emit(ServiceResult.Success(result))
            } catch (e: Exception) {
                e.printStackTrace()
                emit(ServiceResult.Error(ErrorCode.API_ERROR))
            }
        }
    }

    /**
     * In my repository, all I need to do is return the flow of either a LoadResult.Page
     * or LoadResult.Error from my PagingSource load function. You do this by configuring
     * a Pager instance. Here are some notes about PagingConfig:
     * 1. I already know that I have 24 items per page; the Api has already done
     *    this for me on the server. Out of the 24, about 5 products are visible on the
     *    screen at a time.
     * 2. I've read the docs for PagingConfig, and played around with the settings, and
     *    honestly, I don't really understand how they are working behind the scenes. For me,
     *    they don't seem to be working the way that I've read & understood the settings. Here's
     *    what I can tell you:
     *    - When you set pageSize, the prefetchDistance & initialLoadSize parameters are then
     *      derived from this value by default, if you don't set them specifically.
     *    - If you set the prefetchDistance to a number 2x's or more than the pageSize, it
     *      impacts how many pages are loaded in the initial fetch too, and from what I can
     *      tell, this seems true no matter what you have set for initialLoadSize.
     *  3. What the docs did help me realize was that I was getting 2 pages in my initial fetch,
     *     before the results were even displayed for the user. The Api that I'm using takes
     *     a few seconds to come back, so this wasn't optimal for my User Experience. At the same
     *     time though, I didn't want my users to see a bunch of 'loading more records' shimmers
     *     while they scrolled through the results either. By setting my pageSize to 6, my
     *     initialLoadSize to 1, and my prefectDistance to 12, I get just 1 page on my initial
     *     fetch, and it gets more after I scroll past the 7th product. I don't fully understand
     *     why yet - but this seems to be the perfect balance for my app, and the product list
     *     seems to render just fine, with all 24 in each page.
     * 4. By default, my maxSize is set to MAX_SIZE_UNBOUNDED, which means that no pages are dropped
     *    in memory. So far, this seems to be working great - I haven't had a crash 🤞.
     * I encourage you to look at each of these settings and play around; writing out debug
     * info to Logcat definitely helps in situations like this. If your Api is fast, setting the
     * pageSize alone might be just fine for your needs.
     */
    fun getSearchResults(searchText: String): Flow<PagingData<SearchProductItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 6,
                initialLoadSize = 1,
                prefetchDistance = 12
                /*pageSize = 24,
                initialLoadSize = 24,
                prefetchDistance = 10*/
            ),
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