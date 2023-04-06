package com.santansarah.barcodescanner.domain.models

import com.santansarah.barcodescanner.domain.models.AppRouteArgs.BARCODE
import com.santansarah.barcodescanner.domain.models.AppRoutes.HOME_SCREEN

object AppRoutes {
    const val HOME_SCREEN = "home"
    const val PRODUCT_DETAIL_SCREEN = "product_detail"
    const val HELP_ABOUT = "help_about"
}

object AppRouteArgs {
    const val BARCODE = "barcode"
}

object AppDestinations {
    const val HOME = HOME_SCREEN
    const val PRODUCT_DETAIL = "${AppRoutes.PRODUCT_DETAIL_SCREEN}/{$BARCODE}"
    //const val HELP_ABOUT = AppRoutes.HELP_ABOUT
}