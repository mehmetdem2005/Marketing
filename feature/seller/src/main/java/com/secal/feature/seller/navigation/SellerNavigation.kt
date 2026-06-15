package com.secal.feature.seller.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.secal.feature.seller.AddProductScreen
import com.secal.feature.seller.SellerScreen

/** Satıcı rotaları. */
object SellerRoutes {
    const val HOME = "seller"
    const val ADD_PRODUCT = "seller/product/add/{storeId}"

    fun addProduct(storeId: String): String = "seller/product/add/$storeId"
}

/** Satıcı grafiği: panel (mağaza/ürünler) + ürün ekleme. */
fun NavGraphBuilder.sellerGraph(navController: NavController) {
    composable(SellerRoutes.HOME) {
        SellerScreen(
            onAddProduct = { storeId -> navController.navigate(SellerRoutes.addProduct(storeId)) },
        )
    }
    composable(
        route = SellerRoutes.ADD_PRODUCT,
        arguments = listOf(navArgument("storeId") { type = NavType.StringType }),
    ) {
        AddProductScreen(onSaved = { navController.popBackStack() })
    }
}
