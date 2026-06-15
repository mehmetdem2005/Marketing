package com.secal.app.di

import com.secal.app.BuildConfig
import com.secal.core.data.auth.AuthConfig
import com.secal.core.data.auth.SupabaseConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Uygulama yapılandırması — sırlar/anahtarlar **BuildConfig**'ten (local.properties / CI secret)
 * okunup DI grafiğine verilir (ADR-005). Böylece core:data sağlayıcıya/derleme detayına bağlı kalmaz.
 */
@Module
@InstallIn(SingletonComponent::class)
object ConfigModule {

    @Provides
    @Singleton
    fun provideSupabaseConfig(): SupabaseConfig = SupabaseConfig(
        url = BuildConfig.SUPABASE_URL,
        anonKey = BuildConfig.SUPABASE_ANON_KEY,
    )

    @Provides
    @Singleton
    fun provideAuthConfig(): AuthConfig = AuthConfig(
        googleServerClientId = BuildConfig.GOOGLE_WEB_CLIENT_ID,
    )
}
