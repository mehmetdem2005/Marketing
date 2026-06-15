---
type: brain-gotchas
date: 2026-06-15
tags: [brain, gotchas]
ai-first: true
---

# Gotchas

## For future Claude
Daha önce ısıran ve yine ısıracak tuzaklar (obsidian-mind kalıcı hafıza). SeçAl Android +
Supabase yığınında CI'ı yeşile çekerken yaşananlar. Yeni iş yapmadan önce buraya bak.

- **Hilt + KSP component üretmiyor:** KSP ile `@HiltAndroidApp` root component'i (`Dagger*_HiltComponents_SingletonC`) üretilmiyordu (`kspDebugKotlin` "Did you forget to apply the Gradle Plugin?" + javac "cannot find symbol"). **Çözüm:** tüm Hilt modüllerini **KAPT**'a al (`kotlin-kapt` + `kapt(hilt.compiler)`). Yeni Hilt'li modül = KAPT kullan.
- **BuildConfig "unclosed string literal":** CI variable değerinde kaçak `\n` → `buildConfigField` Java string'ini bozuyor. **Çözüm:** build.gradle'da değeri `.trim()` et + GH Actions variable'larını newline'sız yaz (`printf %s`, here-string `<<<` DEĞİL).
- **supabase-kt 3.x `decodeSingle`/`decodeList`:** top-level import DEĞİL → `PostgrestResult` **üye fonksiyonu**. Import etme; `.decodeSingle<T>()` doğrudan çağır.
- **Modül variant uyumsuzluğu (jvm↔androidJvm):** kotlin.jvm modülü (`core:domain`) bir `android.library`'ye bağlıysa "No matching variant" verir. **Çözüm:** saf-Kotlin paylaşılan modülleri (`core:common`) `kotlin.jvm` yap.
- **detekt `UseCheckOrError`:** `throw IllegalStateException(...)` yerine `error(...)` kullan (yine ISE fırlatır).
- **detekt `LongParameterList` eşiği 8:** Composable content fonksiyonlarına tüm alanları tek tek geçme; `state` nesnesini geç.
- **detekt autoCorrect=true:** import sırası/unused gibi ktlint ihlalleri CI'da otomatik düzelir; ama autocorrect-dışı kurallar (UseCheckOrError, LongParameterList) build'i kırar.
- **Gradle config "libs in subprojects":** `subprojects {}` içinde `libs` version-catalog erişimi sınırlı; detekt sürümünü kök scope'ta `libs.versions.detekt.get()` ile string yakala.
- **`X.dp` özelliktir, fonksiyon değil:** `16.dp` (✓) / `16.dp()` (✗). `import androidx.compose.ui.unit.dp` unutma.
- **@Composable yalnız composable bağlamında:** `motionDuration()` gibi @Composable çağrıları `AnimatedContent.transitionSpec` (non-composable lambda) içinde ÇAĞRILAMAZ — composable gövdede hesapla, değeri yakala.
- **CI logları çok büyük:** `mcp__github__actions_list` ve job logları token sınırını aşar → dosyaya kaydedip `python slice`/grep ile oku; `get_job_logs` tail'i çoğu zaman temizlik adımını yakalar, asıl `e:`/`What went wrong` için tail'i büyüt.
- **Repoda `main` yoktu:** SeçAl repolarında varsayılan dal `claude/village-products-marketplace-c08a30`'di; `main` elle oluşturuldu.

## Repo-geneli paket rename (com.X → com.Y) — güvenli sıra
Sıra önemli: (1) içerik sed (`koyden→secal`, `Koyden→Secal`, `Köyden→SeçAl`) tüm dosyalarda;
(2) SONRA dizinleri taşı (`com/koyden`→`com/secal`); (3) `Koyden*.kt`→`Secal*.kt` dosya adları.
Kapsam: kt/kts/xml/md/pro/yml/toml/sh/sample (yalnız koyden geçen dosyaları hedefle). Unutulmaz:
`applicationId`, `namespace`, `rootProject.name`, manifest `android:name=".XApplication"`,
`@style/Theme.X`, `@color/x_*`, pref dosya adları (`x_secure_prefs`). Doğrulama: işlem sonu
`grep -rli koyden` + `grep -rl Köyden` = 0 olmalı. Kotlin'de dosya adı ≠ sınıf adı derlemeyi
bozmaz ama temizlik için dosyalar da yeniden adlandırılır.

## detekt UnusedParameter — refactor sonrası nav callback'leri
Bir composable'ı yeniden yazarken eski bir callback (ör. `onBack`) artık çağrılmıyorsa detekt
`UnusedParameter` ile turu kırar (CI'da `:feature:X:detekt FAILED`). Çözüm: parametreyi gerçekten
kullan (ör. TopAppBar geri ikonu) ya da imzadan + çağıran graf'tan kaldır. Boş bırakma.

## supabase-kt rpc(): parametreler JsonObject olmalı (3.x)
`postgrest.rpc("fn", parameters)` overload'u `JsonObject` bekler — keyfi `@Serializable` veri sınıfı
DEĞİL ("None of the following candidates is applicable"). Çözüm: `buildJsonObject { put("p_x", v) }`
ile parametre kur. RPC argüman adları SQL fonksiyon imzasıyla birebir (`p_product_id`, `p_qty`).
