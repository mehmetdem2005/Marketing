package com.secal.feature.catalog.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.secal.feature.catalog.CatalogScreen
import com.secal.feature.catalog.ProductDetailScreen

/** Katalog rotaları. */
object CatalogRoutes {
    const val LIST = "catalog"
    const val DETAIL = "catalog/product/{productId}"

    fun detail(productId: String): String = "catalog/product/$productId"
}

/** Katalog grafiği: liste + ürün detay. */
fun NavGraphBuilder.catalogGraph(navController: NavController) {
    composable(CatalogRoutes.LIST) {
        CatalogScreen(
            onProductClick = { productId -> navController.navigate(CatalogRoutes.detail(productId)) },
        )
    }
    composable(
        route = CatalogRoutes.DETAIL,
        arguments = listOf(navArgument("productId") { type = NavType.StringType }),
    ) {
        ProductDetailScreen(onBack = { navController.popBackStack() })
    }
}
