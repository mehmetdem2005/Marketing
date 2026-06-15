---
type: brain-patterns
date: 2026-06-15
tags: [brain, patterns]
ai-first: true
---

# Patterns

## For future Claude
SeçAl'de tekrar eden, yeni iş yaparken izlenecek kalıplar. Yeni dikey dilim eklerken bunları kopyala.

- **Hexagonal dikey dilim:** `core:domain` (saf Kotlin: model + repository **port** + use-case'ler) → `core:data` (Supabase adapter, `@Serializable` DTO + mapper, DI `@Binds`) → `feature:X` (ViewModel tek-yönlü `UiState` + Screen). Auth/Profil/Katalog hep böyle.
- **Sonuç tipi:** Katmanlar `DataResult<T>` (Success/Failure[AppError]) döndürür; istisna UI'a sızmaz. `runResult { }` sarmalayıcısı `CancellationException`'ı yeniden fırlatır, gerisini `toAppError()`'a eşler.
- **4-durum UI:** `UiState` (Loading/Empty/Error/Content) + `UiStateScaffold` (reduce-motion crossfade). Her liste/detay ekranı bunu kullanır.
- **Para = kuruş (Long):** `price_minor` bigint; kayan nokta YOK. UI'da `PriceTag` tr-TR yerelleştirir.
- **Postgrest embedded select:** ilişkili veriyi tek istekte çek (`Columns.raw("...,stores(name),product_images(url)")`) — N+1 yok.
- **RLS zonları:** PII (profiles/addresses) self-erişim; paylaşılan (categories/products/stores) public read + sahibi (`owns_store()`) write. deny-by-default.
- **Hilt = KAPT** (KSP component üretmiyor — bkz [[Gotchas]]). Yeni Hilt modülü: `kotlin-kapt` + `kapt(hilt.compiler)`.
- **CI iş akışı:** `claude/...` dalında geliştir → CI yeşil → `main`'e merge (deploy). Migration canlıya yalnız kullanıcı onayıyla.
- **Tasarım:** design-standards + web-design-advanced + ui-ux-advanced + motion-design dörtlüsü; lucide vektör ikon (emoji yok); 8pt grid (`LocalSpacing`); WCAG dokunma hedefi ≥48dp.
