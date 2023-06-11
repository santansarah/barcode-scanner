package com.santansarah.barcodescanner.ui

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import com.santansarah.barcodescanner.domain.models.AppDestinations.HOME
import com.santansarah.barcodescanner.domain.models.AppDestinations.PRODUCT_DETAIL
import com.santansarah.barcodescanner.domain.models.AppDestinations.SEARCH
import com.santansarah.barcodescanner.domain.models.AppDestinations.ACCOUNT
import com.santansarah.barcodescanner.domain.models.AppDestinations.VERIFIED
import com.santansarah.barcodescanner.domain.models.AppRoutes.HOME_SCREEN
import com.santansarah.barcodescanner.domain.models.AppRoutes.PRODUCT_DETAIL_SCREEN
import com.santansarah.barcodescanner.domain.models.AppRoutes.SEARCH_SCREEN
import com.santansarah.barcodescanner.ui.account.AccountScreen
import com.santansarah.barcodescanner.ui.account.verified.VerifiedScreen
import com.santansarah.barcodescanner.ui.productdetail.ProductDetailsRoute
import com.santansarah.barcodescanner.ui.search.SearchRoute

@Composable
fun AppNavGraph(
    navController: NavHostController,
    startDestination: String,
) {

    NavHost(
        navController = navController,
        startDestination = startDestination,
    ) {
        composable(HOME) {
            HomeRoute(
                onGotBarcode = { barcode ->
                    navController.navigate("$PRODUCT_DETAIL_SCREEN/$HOME_SCREEN/$barcode")
                },
                onSearchClicked = { searchText ->
                    navController.navigate("$SEARCH_SCREEN/$searchText")
                },
                onAccountClicked = {
                    navController.navigate(ACCOUNT)
                }
            )
        }
        composable(PRODUCT_DETAIL) {
            ProductDetailsRoute(onBackClicked = { navController.popBackStack() },
                onGotBarcode = { barcode ->
                    navController.navigate("$PRODUCT_DETAIL_SCREEN/$SEARCH_SCREEN/$barcode")
                },
                onSignIn = {
                    navController.navigate(ACCOUNT)
                })
        }
        composable(ACCOUNT) {
            AccountScreen(
                onAddPhone = {
                    navController.navigate(VERIFIED)
                }
            )
        }
        composable(VERIFIED,  //won't work on Android 12, 13 - you need to own the uri.
            deepLinks = listOf(navDeepLink {
                uriPattern = "https://github.com/santansarah"
                action = Intent.ACTION_VIEW
            }),
        ) {
            VerifiedScreen()
        }
        composable(SEARCH) {
            SearchRoute(onBackClicked = { navController.popBackStack() },
                onGotBarcode = { barcode ->
                    navController.navigate("$PRODUCT_DETAIL_SCREEN/$SEARCH_SCREEN/$barcode")
                })
        }
    }
}
