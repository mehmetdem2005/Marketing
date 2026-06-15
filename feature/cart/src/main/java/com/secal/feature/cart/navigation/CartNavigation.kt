package com.secal.feature.cart.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.secal.feature.cart.CartScreen

/** Sepet rotası. */
object CartRoutes {
    const val ROUTE = "cart"
}

/** Sepet grafiği. [onExplore] boş sepetten kataloğa; [onOrderPlaced] sipariş sonrası detaya götürür. */
fun NavGraphBuilder.cartGraph(onExplore: () -> Unit, onOrderPlaced: (String) -> Unit) {
    composable(CartRoutes.ROUTE) {
        CartScreen(onExplore = onExplore, onOrderPlaced = onOrderPlaced)
    }
}
