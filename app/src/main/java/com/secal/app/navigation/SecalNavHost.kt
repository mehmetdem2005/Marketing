package com.secal.app.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.secal.designsystem.component.SecalButton
import com.secal.designsystem.component.SecalButtonVariant
import com.secal.designsystem.theme.LocalSpacing
import com.secal.feature.auth.navigation.AuthRoutes
import com.secal.feature.auth.navigation.authGraph
import com.secal.feature.catalog.navigation.CatalogRoutes
import com.secal.feature.catalog.navigation.catalogGraph
import com.secal.feature.profile.navigation.ProfileRoutes
import com.secal.feature.profile.navigation.profileScreen
import com.secal.feature.seller.navigation.SellerRoutes
import com.secal.feature.seller.navigation.sellerGraph

private const val HOME_ROUTE = "home"

/**
 * Uygulama navigasyon iskeleti.
 *
 * Faz 2: auth grafı (giriş/kayıt) + home + profil. Başlangıç auth'tur; giriş başarılı olunca
 * home'a geçilir ve auth yığından temizlenir. Profilden oturum kapatılınca auth'a dönülür ve
 * kimlik gerektiren graf (home/profil) yığından temizlenir. Faz 2+ ile oturum durumuna göre
 * (ObserveAuthState) otomatik başlangıç + catalog/cart/order/seller grafları eklenecek.
 */
@Composable
fun SecalNavHost(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = AuthRoutes.GRAPH,
        modifier = modifier,
    ) {
        authGraph(
            navController = navController,
            onSignedIn = {
                navController.navigate(HOME_ROUTE) {
                    popUpTo(AuthRoutes.GRAPH) { inclusive = true }
                    launchSingleTop = true
                }
            },
        )
        composable(HOME_ROUTE) {
            HomePlaceholder(
                onOpenCatalog = { navController.navigate(CatalogRoutes.LIST) },
                onOpenSeller = { navController.navigate(SellerRoutes.HOME) },
                onOpenProfile = { navController.navigate(ProfileRoutes.ROUTE) },
            )
        }
        catalogGraph(navController = navController)
        sellerGraph(navController = navController)
        profileScreen(
            onSignedOut = { navController.returnToAuth() },
        )
    }
}

/** Oturum kapatıldığında auth grafına dön ve kimlik gerektiren hedefleri temizle. */
private fun NavController.returnToAuth() {
    navigate(AuthRoutes.GRAPH) {
        popUpTo(HOME_ROUTE) { inclusive = true }
        launchSingleTop = true
    }
}

@Composable
private fun HomePlaceholder(
    onOpenCatalog: () -> Unit,
    onOpenSeller: () -> Unit,
    onOpenProfile: () -> Unit,
) {
    val spacing = LocalSpacing.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(spacing.lg),
        verticalArrangement = Arrangement.spacedBy(spacing.sm, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "SeçAl",
            style = MaterialTheme.typography.headlineMedium,
        )
        Text(
            text = "Köyün doğal ürünleri, üreticiden kapına.",
            style = MaterialTheme.typography.bodyMedium,
        )
        SecalButton(
            text = "Ürünleri keşfet",
            onClick = onOpenCatalog,
            modifier = Modifier.fillMaxWidth().padding(top = spacing.md),
        )
        SecalButton(
            text = "Satıcı paneli",
            onClick = onOpenSeller,
            variant = SecalButtonVariant.Secondary,
            modifier = Modifier.fillMaxWidth(),
        )
        SecalButton(
            text = "Profilim",
            onClick = onOpenProfile,
            variant = SecalButtonVariant.Secondary,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
