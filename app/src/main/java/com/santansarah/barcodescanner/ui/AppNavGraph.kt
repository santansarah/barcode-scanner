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
import com.santansarah.barcodescanner.domain.models.AppDestinations.ADD_PHONE
import com.santansarah.barcodescanner.domain.models.AppDestinations.SIGN_IN
import com.santansarah.barcodescanner.domain.models.AppDestinations.SIGN_UP
import com.santansarah.barcodescanner.domain.models.AppDestinations.VERIFIED
import com.santansarah.barcodescanner.domain.models.AppRoutes.HOME_SCREEN
import com.santansarah.barcodescanner.domain.models.AppRoutes.PRODUCT_DETAIL_SCREEN
import com.santansarah.barcodescanner.domain.models.AppRoutes.SEARCH_SCREEN
import com.santansarah.barcodescanner.ui.account.AccountScreen
import com.santansarah.barcodescanner.ui.account.addphone.AddPhoneScreen
import com.santansarah.barcodescanner.ui.account.signin.SignInScreen
import com.santansarah.barcodescanner.ui.account.signup.SignUpScreen
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
                    navController.navigate(ADD_PHONE)
                },
                onSignIn = {
                    navController.navigate(SIGN_IN)
                },
                onSignUp = {
                    navController.navigate(SIGN_UP)
                }
            )
        }
        composable(SIGN_IN) {
            SignInScreen(onSearch = {
                navController.navigate(HOME)
            }, onAccount = {
                navController.navigate(ACCOUNT)
            })
        }
        composable(SIGN_UP) {
            SignUpScreen()
        }
        composable(ADD_PHONE) {
            AddPhoneScreen(onAccount = {
                navController.navigate(ACCOUNT)
            },
                onSearch = {
                    navController.navigate(HOME)
                })
        }
        composable(
            VERIFIED,  //won't work on Android 12, 13 - you need to own the uri.
            deepLinks = listOf(navDeepLink {
                uriPattern = "https://github.com/santansarah"
                action = Intent.ACTION_VIEW
            }),
        ) {
            VerifiedScreen(
                onAddPhone = {
                    navController.navigate(ADD_PHONE)
                },
                onAccount = {
                    navController.navigate(ACCOUNT)
                },
                onSearch = {
                    navController.navigate(HOME)
                }
            )
        }
        composable(SEARCH) {
            SearchRoute(onBackClicked = { navController.popBackStack() },
                onGotBarcode = { barcode ->
                    navController.navigate("$PRODUCT_DETAIL_SCREEN/$SEARCH_SCREEN/$barcode")
                })
        }
    }
}
