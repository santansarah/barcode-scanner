package com.santansarah.barcodescanner.data.remote

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.santansarah.barcodescanner.data.paging.ProductSearchPagingSource
import com.santansarah.barcodescanner.domain.ErrorCode
import com.santansarah.barcodescanner.utils.ServiceResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException
import java.net.SocketTimeoutException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FoodRepository @Inject constructor(
    private val api: FoodApi
) {

    /**
     * Again, I'll start with the data layer, and work my way up to the UI. To get the Product
     * information, I send a request to the Api with the product's barcode in the path. I don't
     * check for HTTP status codes in my search results yet, but here, it's important that I
     * check for a 404 - Product not found. Besides that, the error handling is very similar to
     * the search results handler.
     */
    suspend fun getInfoByBarCode(barCode: String): Flow<ServiceResult<ItemListing>> {
        return flow {
            emit(ServiceResult.Loading)
            try {
                val result = api.getInfoByBarCode(
                    barCode = barCode,
                    fields = Product.fields.joinToString(",")
                )
                Timber.d("from getInfoByBarCode: $result")
                emit(ServiceResult.Success(result))
            } catch (e: Exception) {
                when (e) {
                    is SocketTimeoutException ->
                        emit(ServiceResult.Error(ErrorCode.API_PRODUCT_TIMEOUT))

                    is IOException ->
                        emit(ServiceResult.Error(ErrorCode.NETWORK_PRODUCT_TIMEOUT))

                    is HttpException -> {
                        if (e.code() == 404)
                            emit(ServiceResult.Error(ErrorCode.NOT_FOUND))
                        else emit(ServiceResult.Error(ErrorCode.API_ERROR))
                    }

                    else ->
                        emit(ServiceResult.Error(ErrorCode.API_ERROR))
                }
            }
        }
    }

    /**
     * In my repository, all I need to do is return the flow of either a LoadResult.Page
     * or LoadResult.Error from my PagingSource load function. You do this by configuring
     * a Pager instance. Here are some notes about PagingConfig:
     * 1. I already know that I have 24 items per page; the Api has done
     *    this for me on the server. Out of the 24, about 5 products are visible on the
     *    screen at a time.
     * 2. When you set pageSize, the prefetchDistance & initialLoadSize parameters are then
     *    derived from this value by default. At first, I set my pageSize to 24. This resulted in
     *    Paging 3 returning 2 pages for my initial fetch, before the user ever saw any results.
     *    Because the Api that I'm working with takes a few seconds to come back, this wasn't
     *    optimal for my User Experience. At the same time though, I didn't want my users to see a
     *    bunch of 'loading more records' shimmers while they scrolled through the results either.
     *    By setting my pageSize to 24 and my prefectDistance to 12, I now get 1 page on my
     *    initial fetch, and Paging 3 gets the next page after I scroll past the 7th product.
     *    This seems to be a good balance for my app.
     * 3. By default, my maxSize is set to MAX_SIZE_UNBOUNDED, which means that no pages are dropped
     *    in memory. So far, this seems to be working well - I haven't had a crash yet 🤞.
     * For me, the docs for PagingConfig are a little confusing. I encourage you to look at each of
     * these settings and play around; writing out debug info to Logcat definitely helps in
     * situations like this. If your Api is fast, setting the pageSize alone might be just fine
     * for your app.
     */
    fun getSearchResults(searchText: String): Flow<PagingData<SearchProductItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 24,
                prefetchDistance = 12
            ),
            pagingSourceFactory = {
                ProductSearchPagingSource(foodApi = api, searchText = searchText)
            }
        ).flow
    }

    suspend fun getSimilarItems(barcodes: List<String>): Flow<List<SimilarItemListing>> {

        val similarProducts: MutableList<SimilarItemListing> = mutableListOf()

        return flow {
            barcodes.forEach { code ->
                try {
                    val result = api.getSimilarProductByBarCode(
                        barCode = code,
                        fields = Product.fields.joinToString(",")
                    )
                    Timber.d("from getInfoByBarCode: $result")

                    similarProducts.add(result)
                    emit(similarProducts.toList())
                } catch (e: Exception) {
                    emit(similarProducts.toList())
                }
            }
        }
    }
}