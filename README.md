# SeçAl — Android Uygulaması

Köy/doğal/yerel ürünler pazaryeri. Mağazalar hesap açıp ürün yükler; alıcılar keşfeder,
sepete ekler ve sipariş verir. Native **Android (Kotlin + Jetpack Compose)**, backend
**Supabase** (Auth + Postgres/RLS + Storage).

> Multi-repo: bu repo **uygulama + dokümantasyon**; backend (`supabase/`) ayrı repodadır
> (`marketing-2`). Mimari/karar/standart dokümanları için `docs/` klasörüne bakın.

## Mimari (özet)
Clean Architecture + MVVM (tek-yönlü state / FSM), çok-modüllü Gradle:

```
:app                       Tek-Activity, NavHost, Hilt graph
:core:common               Result/hata modeli, dispatcher, validasyon
:core:domain               Entity'ler, repository port'ları, use-case (saf Kotlin)
:core:data                 supabase-kt adapter'lar, Auth/Session, Storage
:core:designsystem         Material 3 tema, token'lar (8pt), bileşen kütüphanesi
:core:ui                   4-durum scaffold, motion (reduce-motion)
:feature:auth|home|catalog|cart|order|seller|profile|reviews
```

## Geliştirme
- JDK 17, Android SDK (compileSdk 35, minSdk 24).
- Sırlar: `local.properties.sample` → `local.properties` (repoya girmez).
- Çalıştır: `./gradlew assembleDebug`
- Kalite kapısı: `./gradlew detekt lintDebug testDebugUnitTest`
- Git hook'ları: `lefthook install` (pre-commit: gitleaks + detekt, pre-push: test).

## Standart disiplini (ZORUNLU)
Her iş TOGAF ADM + ISO (42010/25010/25012/27001-27002/29148/9241) standartlarına uyar;
her anlamlı değişiklik bir ADR girdisidir; iş sonunda "Standartlar" dipnotu yazılır.
Kanonik: `docs/EA-TOGAF-mimari.md`, `docs/mimari-karar-gunlugu.md`.

Otomatik zorlama: `.claude/` (UserPromptSubmit hatırlatıcı + Stop kapısı),
`lefthook.yml` (pre-commit/pre-push), `.github/workflows/android-ci.yml` (CI).
