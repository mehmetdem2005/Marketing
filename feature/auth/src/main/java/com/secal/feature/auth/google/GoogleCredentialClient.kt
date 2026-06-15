package com.secal.feature.auth.google

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import kotlinx.coroutines.CancellationException
import java.security.MessageDigest
import java.util.UUID

/**
 * Google "ile giriş" akışı — Credential Manager üzerinden ID token alır.
 *
 * Güvenlik: her istekte rastgele **rawNonce** üretilir; bunun SHA-256 özeti Google'a verilir,
 * ham nonce Supabase'e iletilir → Supabase, idToken içindeki hash ile karşılaştırarak
 * tekrar-saldırıyı (replay) engeller.
 *
 * [context] bir Activity context olmalı (Credential Manager UI'ı için).
 */
class GoogleCredentialClient(private val context: Context) {

    data class GoogleToken(val idToken: String, val rawNonce: String)

    suspend fun requestIdToken(serverClientId: String): Result<GoogleToken> {
        return try {
            val rawNonce = UUID.randomUUID().toString()
            val hashedNonce = sha256(rawNonce)

            val option = GetSignInWithGoogleOption.Builder(serverClientId)
                .setNonce(hashedNonce)
                .build()
            val request = GetCredentialRequest.Builder()
                .addCredentialOption(option)
                .build()

            val response = CredentialManager.create(context).getCredential(context, request)
            val credential = response.credential

            if (credential is CustomCredential &&
                credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
            ) {
                val google = GoogleIdTokenCredential.createFrom(credential.data)
                Result.success(GoogleToken(idToken = google.idToken, rawNonce = rawNonce))
            } else {
                Result.failure(IllegalStateException("Beklenmeyen kimlik bilgisi tipi"))
            }
        } catch (c: CancellationException) {
            throw c
        } catch (e: GetCredentialException) {
            Result.failure(e)
        } catch (t: Throwable) {
            Result.failure(t)
        }
    }

    private fun sha256(value: String): String =
        MessageDigest.getInstance("SHA-256")
            .digest(value.toByteArray())
            .joinToString("") { "%02x".format(it) }
}
