package com.secal.feature.home.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.secal.feature.home.AccountScreen
import com.secal.feature.home.HomeScreen

/** Anasayfa + Hesabım rotaları (alt menü sekmeleri). */
object HomeRoutes {
    const val HOME = "home"
    const val ACCOUNT = "account"
}

/** Anasayfa sekmesi. Navigasyon callback'leri app seviyesinden geçer (çapraz-feature bağımlılığı yok). */
fun NavGraphBuilder.homeScreen(
    onSearch: () -> Unit,
    onCategoryClick: (String) -> Unit,
    onProductClick: (String) -> Unit,
    onBecomeSeller: () -> Unit,
) {
    composable(HomeRoutes.HOME) {
        HomeScreen(
            onSearch = onSearch,
            onCategoryClick = onCategoryClick,
            onProductClick = onProductClick,
            onBecomeSeller = onBecomeSeller,
        )
    }
}

/** Hesabım sekmesi. */
fun NavGraphBuilder.accountScreen(
    onProfile: () -> Unit,
    onOrders: () -> Unit,
    onSeller: () -> Unit,
    onSignOut: () -> Unit,
) {
    composable(HomeRoutes.ACCOUNT) {
        AccountScreen(
            onProfile = onProfile,
            onOrders = onOrders,
            onSeller = onSeller,
            onSignOut = onSignOut,
        )
    }
}
