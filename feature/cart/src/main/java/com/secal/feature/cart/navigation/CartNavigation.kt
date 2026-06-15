package com.secal.feature.cart.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.secal.feature.cart.CartScreen

/** Sepet rotası. */
object CartRoutes {
    const val ROUTE = "cart"
}

/** Sepet grafiği. [onExplore] boş sepetten kataloğa götürür (app seviyesinden geçer). */
fun NavGraphBuilder.cartGraph(onExplore: () -> Unit) {
    composable(CartRoutes.ROUTE) {
        CartScreen(onExplore = onExplore)
    }
}
