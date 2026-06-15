package com.secal.core.data.di

import com.secal.core.data.auth.SupabaseAuthRepository
import com.secal.core.data.auth.SupabaseConfig
import com.secal.core.data.catalog.SupabaseCatalogRepository
import com.secal.core.data.profile.SupabaseProfileRepository
import com.secal.core.data.seller.SupabaseSellerRepository
import com.secal.core.domain.auth.AuthRepository
import com.secal.core.domain.catalog.CatalogRepository
import com.secal.core.domain.profile.ProfileRepository
import com.secal.core.domain.seller.SellerRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.storage.Storage
import javax.inject.Singleton

/**
 * Supabase istemcisi + plugin'leri (Auth, Postgrest) tekil sağlar.
 * [SupabaseConfig] app modülünden (BuildConfig) enjekte edilir.
 */
@Module
@InstallIn(SingletonComponent::class)
object SupabaseModule {

    @Provides
    @Singleton
    fun provideSupabaseClient(config: SupabaseConfig): SupabaseClient =
        createSupabaseClient(
            supabaseUrl = config.url,
            supabaseKey = config.anonKey,
        ) {
            install(Auth)        // oturum kalıcılığı + token yenileme (auth-kt varsayılan)
            install(Postgrest)   // RLS korumalı veri erişimi
            install(Storage)     // ürün görselleri (product-images bucket)
        }
}

/** Port → implementasyon bağlama. */
@Module
@InstallIn(SingletonComponent::class)
abstract class AuthBindingModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(impl: SupabaseAuthRepository): AuthRepository

    @Binds
    @Singleton
    abstract fun bindProfileRepository(impl: SupabaseProfileRepository): ProfileRepository

    @Binds
    @Singleton
    abstract fun bindCatalogRepository(impl: SupabaseCatalogRepository): CatalogRepository

    @Binds
    @Singleton
    abstract fun bindSellerRepository(impl: SupabaseSellerRepository): SellerRepository
}
