package com.santansarah.barcodescanner.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.santansarah.barcodescanner.domain.models.AppDestinations.HOME
import com.santansarah.barcodescanner.domain.models.AppDestinations.PRODUCT_DETAIL
import com.santansarah.barcodescanner.domain.models.AppDestinations.SEARCH
import com.santansarah.barcodescanner.domain.models.AppRoutes.PRODUCT_DETAIL_SCREEN
import com.santansarah.barcodescanner.domain.models.AppRoutes.SEARCH_SCREEN

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
                    navController.navigate("$PRODUCT_DETAIL_SCREEN/$barcode")
                },
                onSearchClicked = { searchText ->
                    navController.navigate("$SEARCH_SCREEN/$searchText")
                }
            )
        }
        composable(PRODUCT_DETAIL) {
            ProductDetailsRoute(onBackClicked = { navController.popBackStack() })
        }
        composable(SEARCH) {
            SearchRoute(onBackClicked = { navController.popBackStack() },
                onGotBarcode = { barcode ->
                    navController.navigate("$PRODUCT_DETAIL_SCREEN/$barcode")
                })
        }
    }
}
