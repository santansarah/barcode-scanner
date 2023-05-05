package com.santansarah.barcodescanner.domain

import com.santansarah.barcodescanner.data.paging.ProductSearchPagingSource

/**
 * This enum handles all of the error types:
 * An API Connection timeout
 * A network error,
 * and finally, a generic error, just in case we really
 * couldn't figure it out. Or, for example, in this case, the
 * user really doesn't need to know about HTTP specific errors,
 * and those error codes wouldn't be useful in this scenario. We can
 * still offer the option for them to try again though. Now let's go
 * back to [ProductSearchPagingSource] and see how this enum is used in
 * our Api call.
 */
enum class ErrorCode(val message: String) {
    API_SEARCH_TIMEOUT("Your search is taking longer than expected. More specific keywords " +
            "might help."),
    NETWORK_ERROR("Couldn't reach our food server; is your Internet connected?"),
    API_ERROR("We had a problem getting your products. Try again.")
}
