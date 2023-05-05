package com.santansarah.barcodescanner.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.santansarah.barcodescanner.data.remote.FoodApi
import com.santansarah.barcodescanner.data.remote.FoodRepository
import com.santansarah.barcodescanner.data.remote.SearchProductItem
import com.santansarah.barcodescanner.data.remote.SearchResults
import com.santansarah.barcodescanner.data.remote.mock.searchResults
import com.santansarah.barcodescanner.domain.ErrorCode
import com.santansarah.barcodescanner.utils.ServiceResult
import timber.log.Timber
import java.io.IOException
import java.net.SocketTimeoutException
import javax.inject.Inject
import kotlin.math.ceil

/**
 * Following good app architecture patterns, we might find ourselves
 * hitting our repository from our ViewModel, then pushing errors to
 * the UI with State Flows. Things are a bit different with Paging 3 -
 * most of the errors originate from loading state errors that we
 * have access to from our Composables. Because of this, I'll start
 * by going over my PagingSource class, and explain how I push my
 * custom error messages to the frontend.
 *
 * With Hilt, my ProductSearchPagingSource takes in my FoodApi, which
 * is just a Retrofit interface, and the product that a user's
 * searching for.
 */
class ProductSearchPagingSource constructor(
    private val foodApi: FoodApi,
    private val searchText: String
) : PagingSource<Int, SearchProductItem>() {

    /**
     * Next, I override the load function, and set it up to return my [SearchProductItem].
     * Before I show you that data class, now is a good time to show you how the data comes
     * back from the Api. In my data layer, under the remote package, I have a mock folder
     * that I use for displaying previews. Let's go to the [searchResults] mock
     * and check it out.
     */
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SearchProductItem> {

        val currentPage = params.key ?: 1
        Timber.tag("paging3").d("currentPage: ${params.key}, loadsize: ${params.loadSize}")

        /**
         * Now it's time to hit the Api, and set up the paging logic. I wrap the Api response
         * in a sealed [ServiceResult] class, that returns a [ServiceResult.Success] or
         * [ServiceResult.Error]. Let's take a look at the Error class, because this is
         * key to returning the custom error messages to the user on the frontend.
         *
         * I call the Api here in getSearchResults.
         */
        when (val results = getSearchResults(searchText = searchText, page = currentPage)) {
            is ServiceResult.Success -> {

                /**
                 * Ok, so the Api call was a success here, and now we need to define a
                 * previous page and a next page before we can return the data. To
                 * avoid unnecessary Api calls that return an empty, non-existent page,
                 * we should also see if we're currently on the last page, if possible.
                 * Luckily, with the Api that I'm using, I can get all of this from
                 * my JSON response. There are a few ways to check if I'm on the last
                 * page, but I went for multiplying the current page by the page size.
                 * So if I'm on page 5 times 24 products per page, that's 120. If that
                 * number is greater than or equal to the total product count for this search,
                 * then I'm on the last page.
                 */
                val onLastPage =
                    results.data.page.times(results.data.pageSize) >= (results.data.count ?: 0)
                Timber.d("Refreshing search: $currentPage, $onLastPage")

                return LoadResult.Page(
                    data = results.data.products,
                    prevKey = if (currentPage == 1) null else currentPage - 1,
                    nextKey = if (onLastPage) null else results.data.page + 1
                )
            }

            /**
             * Now, if there was an error, here I return LoadResult.Error, and pass
             * in the custom [ErrorCode] message that we checked for in the Api call.
             * Later, we'll check for this error message in our Composable.
             */
            is ServiceResult.Error -> {
                Timber.d("Api error")
                return LoadResult.Error(Throwable(message = results.error.message))
            }

            is ServiceResult.Loading -> {
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

    /**
     * This function takes in the search text, and current page,
     * which starts off with 1 for the initial fetch.
     */
    private suspend fun getSearchResults(
        searchText: String,
        page: Int
    ): ServiceResult<SearchResults> {
        Timber.tag("paging3").d("getting page: $page")
        return try {
            /**
             * I hit my Api, passing in the search text, current page, and specific
             * fields that I'd like to return instead of all of the product fields.
             * This keeps my response light, fast, and efficient. To do this, I have
             * a companion object on my [SearchProductItem]. Let's take a quick look.
             */
            val result = foodApi.searchProducts(
                searchText = searchText,
                fields = SearchProductItem.fields.joinToString(","),
                page = page
            )
            Timber.d(result.toString())
            ServiceResult.Success(result)
        } catch (e: Exception) {
            Timber.d(e.toString())
            /**
             * If my API call comes back with an error, check for
             * SocketTimeoutException if the search timed out,
             * IOException if something went wrong when we tried to connect to the server,
             * and finally, I trap all other errors, like HTTP error codes, in a more
             * generic block. Now, let's go back up to our load function.
             */
            when (e) {
                is SocketTimeoutException ->
                    ServiceResult.Error(ErrorCode.API_SEARCH_TIMEOUT)
                is IOException ->
                    ServiceResult.Error(ErrorCode.NETWORK_ERROR)
                else ->
                    ServiceResult.Error(ErrorCode.API_ERROR)
            }
        }
    }
}