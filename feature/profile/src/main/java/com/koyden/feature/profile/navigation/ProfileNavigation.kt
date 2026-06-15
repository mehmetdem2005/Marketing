package com.koyden.feature.profile.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.koyden.feature.profile.ProfileScreen

/** Profil rota sabiti (Faz 2'de type-safe rotaya taşınabilir). */
object ProfileRoutes {
    const val ROUTE = "profile"
}

/**
 * Profil hedefi. [onSignedOut] oturum kapatılınca çağrılır (çağıran auth akışına döner ve
 * kimlik gerektiren grafları yığından temizler).
 */
fun NavGraphBuilder.profileScreen(onSignedOut: () -> Unit) {
    composable(ProfileRoutes.ROUTE) {
        ProfileScreen(onSignedOut = onSignedOut)
    }
}
