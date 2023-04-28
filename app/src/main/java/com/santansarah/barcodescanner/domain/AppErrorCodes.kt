package com.santansarah.barcodescanner.domain

enum class ErrorCode(val message: String) {
    API_ERROR("Couldn't get item data."),
    NETWORK_ERROR("Couldn't reach our food server; is your Internet connected?"),
    API_SEARCH_TIMEOUT("Your search is taking longer than expected. More specific keywords " +
            "might help.")
}
