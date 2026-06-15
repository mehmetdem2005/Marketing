package com.secal.feature.order.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.secal.feature.order.OrderDetailScreen
import com.secal.feature.order.OrderListScreen

/** Sipariş rotaları. */
object OrderRoutes {
    const val LIST = "orders"
    const val DETAIL = "orders/{orderId}"

    fun detail(orderId: String): String = "orders/$orderId"
}

/** Sipariş grafiği: Siparişlerim listesi + sipariş detayı/onayı. */
fun NavGraphBuilder.orderGraph(navController: NavController) {
    composable(OrderRoutes.LIST) {
        OrderListScreen(
            onOrderClick = { orderId -> navController.navigate(OrderRoutes.detail(orderId)) },
        )
    }
    composable(
        route = OrderRoutes.DETAIL,
        arguments = listOf(navArgument("orderId") { type = NavType.StringType }),
    ) {
        OrderDetailScreen(onBack = { navController.popBackStack() })
    }
}
