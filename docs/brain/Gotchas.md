---
type: brain-gotchas
date: 2026-06-15
tags: [brain, gotchas]
ai-first: true
---

# Gotchas

## For future Claude
Daha önce ısıran ve yine ısıracak tuzaklar (obsidian-mind kalıcı hafıza). Köyden Android +
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
- **Repoda `main` yoktu:** Köyden repolarında varsayılan dal `claude/village-products-marketplace-c08a30`'di; `main` elle oluşturuldu.
