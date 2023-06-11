package com.santansarah.barcodescanner.domain.models

import com.santansarah.barcodescanner.domain.models.AppRouteArgs.BARCODE
import com.santansarah.barcodescanner.domain.models.AppRouteArgs.FROM_SCREEN
import com.santansarah.barcodescanner.domain.models.AppRouteArgs.SEARCH_TEXT
import com.santansarah.barcodescanner.domain.models.AppRoutes.HOME_SCREEN
import com.santansarah.barcodescanner.domain.models.AppRoutes.PRODUCT_DETAIL_SCREEN
import com.santansarah.barcodescanner.domain.models.AppRoutes.SEARCH_SCREEN
import com.santansarah.barcodescanner.domain.models.AppRoutes.ACCOUNT_SCREEN
import com.santansarah.barcodescanner.domain.models.AppRoutes.VERIFIED_SCREEN

object AppRoutes {
    const val HOME_SCREEN = "home"
    const val PRODUCT_DETAIL_SCREEN = "productDetail"
    const val SEARCH_SCREEN = "search"
    const val ACCOUNT_SCREEN = "account"
    const val VERIFIED_SCREEN = "verified"
}

object AppRouteArgs {
    const val BARCODE = "barcode"
    const val SEARCH_TEXT = "search_text"
    const val FROM_SCREEN = "from_screen"
}

object AppDestinations {
    const val HOME = HOME_SCREEN
    const val PRODUCT_DETAIL = "${PRODUCT_DETAIL_SCREEN}/{$FROM_SCREEN}/{$BARCODE}"
    const val SEARCH = "$SEARCH_SCREEN/{$SEARCH_TEXT}"
    const val ACCOUNT = ACCOUNT_SCREEN
    const val VERIFIED = VERIFIED_SCREEN
    //const val HELP_ABOUT = AppRoutes.HELP_ABOUT
}