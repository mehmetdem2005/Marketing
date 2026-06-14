package com.koyden.app.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.koyden.feature.auth.navigation.AuthRoutes
import com.koyden.feature.auth.navigation.authGraph

private const val HOME_ROUTE = "home"

/**
 * Uygulama navigasyon iskeleti.
 *
 * Faz 1: auth grafı (giriş/kayıt) + home placeholder. Başlangıç auth'tur; giriş başarılı
 * olunca home'a geçilir ve auth yığından temizlenir. Faz 2+ ile oturum durumuna göre
 * (ObserveAuthState) otomatik başlangıç + catalog/cart/order/seller/profile grafları eklenecek.
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
            HomePlaceholder()
        }
    }
}

@Composable
private fun HomePlaceholder() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
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
    }
}
