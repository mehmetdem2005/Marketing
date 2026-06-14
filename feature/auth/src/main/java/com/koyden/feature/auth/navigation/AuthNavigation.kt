package com.koyden.feature.auth.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.koyden.feature.auth.LoginScreen
import com.koyden.feature.auth.RegisterScreen

/** Auth rota sabitleri (Faz 2'de type-safe rotalara taşınabilir). */
object AuthRoutes {
    const val GRAPH = "auth"
    const val LOGIN = "auth/login"
    const val REGISTER = "auth/register"
}

/**
 * Auth navigasyon grafiği. Başarılı giriş/kayıtta [onSignedIn] çağrılır
 * (çağıran ana akışa yönlendirir ve auth grafını yığından temizler).
 */
fun NavGraphBuilder.authGraph(
    navController: NavController,
    onSignedIn: () -> Unit,
) {
    navigation(startDestination = AuthRoutes.LOGIN, route = AuthRoutes.GRAPH) {
        composable(AuthRoutes.LOGIN) {
            LoginScreen(
                onSignedIn = onSignedIn,
                onNavigateToRegister = { navController.navigate(AuthRoutes.REGISTER) },
            )
        }
        composable(AuthRoutes.REGISTER) {
            RegisterScreen(
                onSignedIn = onSignedIn,
                onNavigateToLogin = { navController.popBackStack() },
            )
        }
    }
}
