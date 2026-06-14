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

/**
 * Uygulama navigasyon iskeleti.
 *
 * Faz 0: tek placeholder hedef. Faz 2+ ile auth/home/catalog/cart/order/seller/profile
 * grafları type-safe rotalarla buraya eklenecek (ADR-002 navigasyon mimarisi).
 */
@Composable
fun KoydenNavHost(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "home",
        modifier = modifier,
    ) {
        composable("home") {
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
