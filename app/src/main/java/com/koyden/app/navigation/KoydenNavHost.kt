package com.koyden.app.navigation

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
import com.koyden.designsystem.component.KoydenButton
import com.koyden.designsystem.theme.LocalSpacing
import com.koyden.feature.auth.navigation.AuthRoutes
import com.koyden.feature.auth.navigation.authGraph
import com.koyden.feature.profile.navigation.ProfileRoutes
import com.koyden.feature.profile.navigation.profileScreen

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
fun KoydenNavHost(modifier: Modifier = Modifier) {
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
                onOpenProfile = { navController.navigate(ProfileRoutes.ROUTE) },
            )
        }
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
private fun HomePlaceholder(onOpenProfile: () -> Unit) {
    val spacing = LocalSpacing.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(spacing.lg),
        verticalArrangement = Arrangement.spacedBy(spacing.sm, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Köyden",
            style = MaterialTheme.typography.headlineMedium,
        )
        Text(
            text = "Köyden doğal ürünler pazaryeri — yakında.",
            style = MaterialTheme.typography.bodyMedium,
        )
        KoydenButton(
            text = "Profilim",
            onClick = onOpenProfile,
            modifier = Modifier.fillMaxWidth().padding(top = spacing.md),
        )
    }
}
