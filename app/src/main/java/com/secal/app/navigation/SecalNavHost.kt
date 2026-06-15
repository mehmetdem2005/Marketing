package com.secal.app.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.secal.feature.auth.navigation.AuthRoutes
import com.secal.feature.auth.navigation.authGraph
import com.secal.feature.cart.navigation.CartRoutes
import com.secal.feature.cart.navigation.cartGraph
import com.secal.feature.catalog.navigation.CatalogRoutes
import com.secal.feature.catalog.navigation.catalogGraph
import com.secal.feature.home.navigation.HomeRoutes
import com.secal.feature.home.navigation.accountScreen
import com.secal.feature.home.navigation.homeScreen
import com.secal.feature.profile.navigation.ProfileRoutes
import com.secal.feature.profile.navigation.profileScreen
import com.secal.feature.seller.navigation.SellerRoutes
import com.secal.feature.seller.navigation.sellerGraph

/** Alt menü sekmesi tanımı. */
private data class TopTab(
    val route: String,
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
)

private val TOP_TABS = listOf(
    TopTab(HomeRoutes.HOME, "Anasayfa", Icons.Filled.Home, Icons.Outlined.Home),
    TopTab(CatalogRoutes.LIST, "Keşfet", Icons.Filled.Search, Icons.Outlined.Search),
    TopTab(CartRoutes.ROUTE, "Sepet", Icons.Filled.ShoppingCart, Icons.Outlined.ShoppingCart),
    TopTab(HomeRoutes.ACCOUNT, "Hesabım", Icons.Filled.Person, Icons.Outlined.Person),
)

/**
 * Uygulama navigasyon iskeleti. Giriş sonrası **alt menü (NavigationBar)** ile 4 sekme:
 * Anasayfa · Keşfet · Sepet · Hesabım. Derin ekranlar (ürün detay, satıcı, profil) sekme
 * çubuğunu gizler. Çapraz-feature bağımlılığı yok — geçişler app seviyesinden bağlanır.
 */
@Composable
fun SecalNavHost(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route
    val showBottomBar = currentRoute in TOP_TABS.map { it.route }

    Scaffold(
        modifier = modifier,
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    TOP_TABS.forEach { tab ->
                        val selected = currentRoute == tab.route
                        NavigationBarItem(
                            selected = selected,
                            onClick = { navController.navigateToTab(tab.route) },
                            icon = {
                                Icon(
                                    if (selected) tab.selectedIcon else tab.unselectedIcon,
                                    contentDescription = tab.label,
                                )
                            },
                            label = { Text(tab.label) },
                        )
                    }
                }
            }
        },
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = AuthRoutes.GRAPH,
            modifier = Modifier.padding(innerPadding),
        ) {
            authGraph(
                navController = navController,
                onSignedIn = {
                    navController.navigate(HomeRoutes.HOME) {
                        popUpTo(AuthRoutes.GRAPH) { inclusive = true }
                        launchSingleTop = true
                    }
                },
            )
            homeScreen(
                onSearch = { navController.navigateToTab(CatalogRoutes.LIST) },
                onCategoryClick = { navController.navigateToTab(CatalogRoutes.LIST) },
                onProductClick = { id -> navController.navigate(CatalogRoutes.detail(id)) },
                onBecomeSeller = { navController.navigate(SellerRoutes.HOME) },
            )
            catalogGraph(
                navController = navController,
                onOpenCart = { navController.navigateToTab(CartRoutes.ROUTE) },
            )
            cartGraph(onExplore = { navController.navigateToTab(CatalogRoutes.LIST) })
            accountScreen(
                onProfile = { navController.navigate(ProfileRoutes.ROUTE) },
                onSeller = { navController.navigate(SellerRoutes.HOME) },
                onSignOut = { navController.returnToAuth() },
            )
            sellerGraph(navController = navController)
            profileScreen(onSignedOut = { navController.returnToAuth() })
        }
    }
}

/** Alt menü sekmeleri arası geçiş: Anasayfa çapa; yığını sığ tut, sekme durumunu koru. */
private fun NavController.navigateToTab(route: String) {
    navigate(route) {
        popUpTo(HomeRoutes.HOME) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}

/** Oturum kapatıldığında auth grafına dön ve kimlik gerektiren hedefleri temizle. */
private fun NavController.returnToAuth() {
    navigate(AuthRoutes.GRAPH) {
        popUpTo(HomeRoutes.HOME) { inclusive = true }
        launchSingleTop = true
    }
}
