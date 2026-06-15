# SeçAl — Mimari Karar Günlüğü (ADR)

> Her anlamlı mimari karar burada kayıt altına alınır. Çelişen kararlar **"supersedes"**
> ile uzlaştırılır (self-rewriting knowledge base). Format: Durum · Bağlam · Karar ·
> Sonuçlar · (Mitigasyonlar) · Değerlendirilen alternatifler.

## İndeks
- ADR-001 — Native Android (Kotlin + Jetpack Compose)
- ADR-002 — Backend: doğrudan Supabase (Auth + Postgres/RLS + Storage)
- ADR-003 — Çok-modüllü Gradle + version catalog + üç katmanlı kalite kapısı
- ADR-004 — Clean Architecture + MVVM (tek-yönlü state / FSM), DI: Hilt
- ADR-005 — Güvenlik-öncelikli: RLS deny-by-default + sır yönetimi + gitleaks
- ADR-006 — PII zon ayrımı & veri minimizasyonu
- ADR-007 — Ödeme: Stripe yönlendirme, tahsilat ertelendi (PaymentGateway portu)
- ADR-008 — Multi-repo yapı (marketing = app+docs, marketing-2 = Supabase)
- ADR-009 — Tasarım sistemi: motion token sistemi + bileşen kütüphanesi + 4-durum scaffold
- ADR-010 — Kimlik: Supabase Auth (e-posta/şifre + Google ID token / Credential Manager)
- ADR-011 — Altyapı provisioning: SeçAl Supabase projesi + sır yönetimi + sunucu-tier kararı (açık)
- ADR-012 — Faz 2: Profil özelliği (hexagonal dikey dilim, RLS self-erişim)
- ADR-013 — Faz 2: Katalog/keşif (çok-repolu dikey dilim, public read)
- ADR-014 — Marka adı: **SeçAl** + tam paket rename (com.koyden → com.secal)
- ADR-015 — Faz 4: Satıcı (mağaza & ürün yönetimi + görsel upload)
- ADR-016 — Faz 5a: Sepet (cart_items + atomik add_to_cart RPC, PII/RLS)
- ADR-017 — UI: Trendyol-kalıbı navigasyon (M3 alt menü + zengin Anasayfa + Hesabım)
- ADR-018 — Faz 5b: Sipariş (orders/order_items + place_order RPC, fiyat snapshot)
- ADR-019 — Ödeme/escrow stratejisi: COD (MVP) → iyzico Pazaryeri (lisanslı PSP escrow)

---

## ADR-001 — Native Android (Kotlin + Jetpack Compose)
- **Durum:** Kabul · TOGAF Phase D (Yeniden-mimari — stack seçimi)
- **Bağlam:** Trendyol kalitesinde, daraltılmış nişte (köy/doğal ürünler) bir pazaryeri isteniyor. Cross-platform yerine native Android tercih edildi.
- **Karar:** **Kotlin + Jetpack Compose (Material 3)** ile native Android. minSdk 24, compileSdk 35.
- **Sonuçlar:** En iyi yerel performans/erişilebilirlik; tek platform odağı; Compose ile hızlı UI.
- **Değerlendirilen alternatifler:** React Native/Expo (mse-auto stack'i — reddedildi: native istendi), Flutter (reddedildi: ekip/native tercih).

## ADR-002 — Backend: doğrudan Supabase
- **Durum:** Kabul · TOGAF Phase C/D (Yeniden-mimari) · P3 (Buy>Build)
- **Bağlam:** Kimlik, veri, depolama, realtime gibi commodity yeteneklere ihtiyaç var; özel sunucu bakım maliyeti istenmiyor.
- **Karar:** **Supabase**: GoTrue (Auth), Postgres + **RLS**, Storage, Realtime, Edge Functions. İstemci supabase-kt SDK ile **doğrudan** erişir; güvenlik RLS ile.
- **Sonuçlar:** Hızlı geliştirme; güvenlik veri katmanında (RLS); anon key herkese açık (RLS ile güvenli).
- **Mitigasyonlar:** Sağlayıcı kilidi → repository/port soyutlaması (P7); service-role yalnız Edge'de.
- **Değerlendirilen alternatifler:** Özel backend (Hono/Ktor) — reddedildi (P3, P9); Firebase — reddedildi (Postgres/RLS/SQL tercihi).

## ADR-003 — Çok-modüllü Gradle + version catalog + üç katmanlı kalite kapısı
- **Durum:** Kabul · TOGAF Phase G (Artımlı)
- **Bağlam:** Kurumsal kalite, ölçeklenebilir build ve otomatik standart zorlama gerekiyor.
- **Karar:** Çok-modüllü Gradle (Kotlin DSL) + `gradle/libs.versions.toml`. Kalite kapısı üç katman: **lefthook** (pre-commit: gitleaks + detekt, pre-push: test), **GitHub Actions CI** (gitleaks + detekt + lint + test + assemble), **Claude Stop hook** (`.claude/hooks/standards-gate.sh`).
- **Sonuçlar:** Hızlı artımlı build; standartlar makinece zorlanır (hafıza değil harness).
- **Mitigasyonlar:** Convention plugin/buildSrc şimdilik yok (P9 — gerektiğinde ADR ile eklenecek; modül build dosyaları kısa tutuldu).
- **Değerlendirilen alternatifler:** Tek-modül (reddedildi: ölçek/derleme); buildSrc convention plugin (ertelendi: P9).

## ADR-004 — Clean Architecture + MVVM (tek-yönlü state / FSM), DI: Hilt
- **Durum:** Kabul · TOGAF Phase C (Artımlı) · P4
- **Bağlam:** Test edilebilir, sağlayıcıdan bağımsız, sürdürülebilir bir uygulama katmanı gerekiyor.
- **Karar:** Katmanlar: UI(Compose) → ViewModel(sealed UiState/FSM) → UseCase → Repository **port** (domain, saf Kotlin) → Repository impl (data). DI **Hilt**, async **Coroutines/Flow**.
- **Sonuçlar:** Tek-yönlü bağımlılık; domain saf; UI test edilebilir.
- **Değerlendirilen alternatifler:** MVI kütüphanesi (ertelendi: sade sealed UiState yeterli, P9), elle DI (reddedildi).

## ADR-005 — Güvenlik-öncelikli: RLS deny-by-default + sır yönetimi
- **Durum:** Kabul · TOGAF Phase G (Artımlı) · P5
- **Bağlam:** Pazaryeri kişisel/finansal veri içerir; ihlal maliyeti yüksek.
- **Karar:** Her tabloda RLS etkin + **deny-by-default**; sır **kodda yok** (local.properties/CI secret → BuildConfig; service-role yalnız Edge env); token EncryptedSharedPreferences/Keystore; yalnız HTTPS (`usesCleartextTraffic=false`); gitleaks taraması üç katmanda. OWASP MASVS gözetilir.
- **Sonuçlar:** Savunma derinliği; en az ayrıcalık.
- **Değerlendirilen alternatifler:** Uygulama-katmanı yetkilendirme tek başına (reddedildi: RLS veri katmanında zorunlu).

## ADR-006 — PII zon ayrımı & veri minimizasyonu
- **Durum:** Kabul · TOGAF Phase C (Artımlı) · P1
- **Bağlam:** KVKK/GDPR uyumu ve gizlilik-by-design gerekiyor.
- **Karar:** Veri iki zona ayrılır: **PII** (profiles, addresses, favorites, carts, orders, notifications, payment_intents) ve **paylaşılan** (categories, products, stores public, reviews). PII yalnız sahibi + yetkili rol erişir; minimum veri toplanır.
- **Sonuçlar:** Sızıntı yüzeyi küçük; uyum kolaylaşır.
- **Değerlendirilen alternatifler:** Tek-zon + yalnız uygulama filtresi (reddedildi: risk).

## ADR-007 — Ödeme: Stripe yönlendirme, tahsilat ertelendi
- **Durum:** Kabul · TOGAF Phase C (Artımlı) · P7
- **Bağlam:** İlk sürümde gerçek tahsilat istenmiyor; ödeme sağlayıcısı değiştirilebilir kalmalı.
- **Karar:** Ödeme `PaymentGateway` portu arkasında; Stripe adapter ile **yalnız yönlendirme** (Checkout/redirect placeholder), **tutar tahsil edilmez**. Stripe secret yalnız Edge Function env'inde.
- **Sonuçlar:** İleride tahsilat açılabilir; sağlayıcı kilidi düşük.
- **Mitigasyonlar:** Gerçek tahsilat öncesi PCI/uyum gözden geçirmesi (ayrı ADR).
- **Değerlendirilen alternatifler:** Doğrudan istemci-tarafı tahsilat (reddedildi: güvenlik/uyum).

## ADR-008 — Multi-repo yapı
- **Durum:** Kabul · TOGAF Phase F (Artımlı)
- **Bağlam:** Uygulama ve backend bağımsız yaşam döngülerine sahip; sorumluluk ayrımı isteniyor.
- **Karar:** **`marketing`** = Android uygulaması + `docs/` (EA/ADR/ISO/tasarım/güvenlik). **`marketing-2`** = Supabase backend/Edge (`supabase/` CLI: migrations, RLS, RPC, Edge, seed). **`mse-auto`** yalnız okunur referans — kod kopyalanmaz.
- **Sonuçlar:** Net sorumluluk; bağımsız CI/deploy.
- **Değerlendirilen alternatifler:** Mono-repo (ertelendi: ekip/araç tercihi multi-repo).

## ADR-009 — Tasarım sistemi: motion token sistemi + bileşen kütüphanesi + 4-durum scaffold
- **Durum:** Kabul · TOGAF Phase C/D (Artımlı) · P3 (tutarlılık) · P8 (erişilebilirlik)
- **Bağlam:** Faz 1; ekranlar yazılmadan önce tutarlı, erişilebilir, animasyonlu bir bileşen
  temeli gerekiyor. Sabit `dp`/renk/ms dağılması ve "AI-slop" animasyon riski azaltılmalı.
- **Karar:**
  - **Motion token sistemi** (`:core:designsystem` `motion/MotionTokens.kt`): süre paleti
    (quick150/standard250/emphasized350/large450) + Material 3 easing'leri; marka motion
    kişiliği "Corporate + sıcak". `rememberReduceMotion()` ile **reduce-motion zorunlu**
    (ANIMATOR_DURATION_SCALE==0 → süre 0). Ekranlarda sabit ms YASAK.
  - **Bileşen kütüphanesi** (`:core:designsystem` `component/`): Button, TextField, Card,
    Chip, Badge (count/status), Rating (vektör yıldız), PriceTag (₺, kuruş tabanlı),
    QuantityStepper, Skeleton (shimmer), EmptyState, ErrorState, ProductCard (görsel **slot** →
    designsystem Coil'den bağımsız). Atomic Design; 8pt grid token; emoji-ikon yasağı; ≥48dp.
  - **4-durum scaffold** (`:core:ui` `state/`): `UiState` (Loading/Empty/Error/Content) +
    `UiStateScaffold` (reduce-motion'lu crossfade) + `AppError`→Türkçe mesaj eşlemesi
    (ham hata sızdırılmaz).
- **Sonuçlar:** Feature ekranları (Faz 2+) bu temeli kullanır; tutarlılık + erişilebilirlik
  + motion garanti. designsystem görüntü kütüphanesinden bağımsız (slot deseni).
- **Değerlendirilen alternatifler:** Ekran-içi ad-hoc bileşen (reddedildi: tutarsızlık);
  motion'ı doğrudan M3 varsayılanlarına bırakmak (reddedildi: marka kimliği + reduce-motion kontrolü).

## ADR-010 — Kimlik: Supabase Auth (e-posta/şifre + Google ID token / Credential Manager)
- **Durum:** Kabul · TOGAF Phase C/D (Artımlı) · P5/P6 (güvenlik) · ISO 27001/27002 · ADR-002/004/005 ile uyumlu
- **Bağlam:** Faz 1; kullanıcı girişi gerekiyor. Hedef kitle için hem e-posta/şifre hem de hızlı
  onboarding (Google) isteniyor (ürün kararı). Sağlayıcı kilidi azaltılmalı; sırlar repoda olmamalı.
- **Karar:**
  - **Port:** `:core:domain` `auth/AuthRepository` (saf Kotlin) + use-case'ler (SignIn/SignUp/
    Google/SignOut/ObserveAuthState) + istemci doğrulaması (`CredentialValidation`). Domain'e
    Supabase tipi sızmaz.
  - **Adapter:** `:core:data` `SupabaseAuthRepository` (supabase auth-kt). Hatalar `DataResult`
    + `AppError`'a eşlenir; **ham hata/sağlayıcı detayı UI'a taşınmaz**. `UserInfo`→`AuthUser` map.
  - **Google:** Credential Manager + Google ID (`:feature:auth` `GoogleCredentialClient`);
    rastgele **nonce** (rawNonce istemci, SHA-256 hash Google'a) ile replay koruması; idToken
    Supabase `signInWith(IDToken)`'a verilir.
  - **Sırlar (ADR-005):** `SUPABASE_URL/ANON_KEY`, `GOOGLE_WEB_CLIENT_ID` → app BuildConfig
    (local.properties/CI), DI ile `SupabaseConfig`/`AuthConfig` olarak sağlanır. Anon key açık,
    güvenlik **RLS** ile.
  - **UI:** `:feature:auth` LoginScreen/RegisterScreen (designsystem bileşenleri + 4-durum alt
    durumları) + auth nav grafı; başarılı girişte home'a geçiş.
- **Sonuçlar:** Oturum kalıcılığı auth-kt varsayılanı; Faz 2'de ObserveAuthState ile otomatik
  başlangıç yönlendirmesi + profil/RLS. Sağlayıcı portla soyutlandığı için değiştirilebilir.
- **Değerlendirilen alternatifler:** Yalnız e-posta/şifre (reddedildi: onboarding hızı);
  Firebase Auth (reddedildi: Supabase ile tek backend tercihi — ADR-002).

## ADR-011 — Altyapı provisioning: SeçAl Supabase projesi + sır yönetimi + sunucu-tier (açık)
- **Durum:** Kabul (provisioning) · sunucu-tier kararı AÇIK · TOGAF Phase F/G · ISO 27001/27002
- **Bağlam:** Android uygulaması (ADR-001/002) çalışan bir Supabase projesine ihtiyaç duyuyor
  (auth/DB doğrudan Supabase). Kullanıcı ayrıca "bir sunucu gerekiyor" diyerek Render'ı gündeme getirdi.
- **Karar:**
  - **Supabase projesi `secal`** oluşturuldu (ref `yampwgdlqncdgwjslige`, eu-central-1, free).
    `0001_init` şeması uygulandı (profiles/PII + addresses + RLS + handle_new_user). Detay:
    `docs/altyapi-kaynaklar.md`.
  - **Sır yönetimi:** Whenly'nin Render'inden (kullanıcı izni + sağladığı Render key ile) yalnız
    **access token'lar** alındı (`SUPABASE_ACCESS_TOKEN`, `GITHUB_TOKEN`) ve Render key; tümü
    container `/home/user/.secrets/` (repo dışı). Whenly kaynaklarına yalnız okuma; hiçbir Whenly
    sırrı/değeri repoya yazılmadı; diğer Whenly sırlarının yerel kopyaları silindi. Kullanıcı
    token'ları sonra revoke edecek. Sır **değeri** asla git'e girmez (ADR-005).
  - **Sunucu-tier (AÇIK):** server-side ihtiyaç önce **Supabase Edge Functions** ile karşılanır
    (mevcut `marketing-2`); ayrı **Render Node servisi** opsiyonu backend kapsamı netleşince ayrı
    ADR ile karara bağlanır. **Android mimarisi değişmedi.**
- **Sonuçlar:** Android auth artık gerçek bir Supabase backend'ine bağlanabilir (URL/anon → CI/local).
  Render servisi henüz oluşturulmadı (kapsam beklemede).
- **Değerlendirilen alternatifler:** Whenly'nin mevcut Supabase projesini yeniden kullanmak
  (reddedildi: iki ürünün verisini karıştırır); frontend+backend'i Render'e taşımak (reddedildi:
  Android korunuyor — kullanıcı onayı).

---

## ADR-012 — Faz 2: Profil özelliği (hexagonal dikey dilim, RLS self-erişim)
- **Durum:** Kabul · TOGAF Phase B/C/D · ISO 25010/25012/29148
- **Bağlam:** Auth tamamlandı (ADR-004). İlk kimlik-gerektiren dikey dilim olarak profil
  görüntüleme/düzenleme gerekiyor; `profiles` tablosu (PII) + RLS self read/update zaten mevcut.
- **Karar:**
  - **Domain:** `Profile`/`UserRole` saf modeller + `ProfileRepository` portu +
    `GetMyProfile`/`UpdateProfile` use-case'leri. Ad doğrulaması (≤120) domain'de; rol/id
    istemciden DEĞİŞTİRİLEMEZ (yalnız ad/telefon).
  - **Data:** `SupabaseProfileRepository` (Postgrest + RLS); kullanıcı id/e-posta auth oturumundan.
    Okuma/yazma için ayrı `@Serializable` DTO; güncel sonra taze satır okunur (tek doğruluk = DB).
  - **UI:** `feature:profile` — `ProfileViewModel` (tek-yönlü `UiState` + kaydet/çıkış alt-durumları)
    + `ProfileScreen` (4-durum scaffold, tasarım sistemi bileşenleri, reduce-motion'a saygılı geçişler).
    NavHost: home→profil; profilden çıkış→auth (kimlik grafları temizlenir).
  - **Hilt:** `feature:profile` da KAPT'a alındı (ADR-? KAPT geçişiyle tutarlı; KSP component üretmiyordu).
- **Sonuçlar:** Auth akışı uçtan uca tamamlandı (giriş→home→profil→çıkış). Pazaryeri çekirdeği
  (katalog/sipariş) sonraki dilimlerde aynı hexagonal kalıpla eklenecek.
- **Değerlendirilen alternatifler:** Profili Edge Function arkasına koymak (reddedildi: RLS self-erişim
  yeterli, ekstra tier gereksiz); ekran-state'i tek `UiState`e gömmek (reddedildi: kaydet alt-durumu
  içerik görünürken inline gösterilmeli — progressive disclosure).

---

## ADR-013 — Faz 2: Katalog/keşif (çok-repolu dikey dilim, public read)
- **Durum:** Kabul · TOGAF Phase C/D · ISO 25010/25012 · kalite çıtası: Trendyol
- **Bağlam:** Pazaryeri çekirdeği — alıcının ürünleri keşfetmesi/araması/incelemesi. Paylaşılan
  (public) veri zonu; satıcı kendi mağaza/ürününü yazar.
- **Karar:**
  - **Şema (`marketing-2/0002_catalog.sql`):** stores, categories(ağaç), products, product_images.
    Fiyat `price_minor` (kuruş, bigint — kayan nokta yok). `pg_trgm` ile isim/açıklama arama indeksi.
    RLS: public read / satıcı (mağaza sahibi) write; `owns_store()` helper. `product-images` Storage
    bucket (public read, authenticated upload). Köy ürün kategorileri seed.
  - **Android (hexagonal):** domain (Category/Product/ProductQuery + CatalogRepository +
    Get{Categories,Products,Product}UseCase) → data (SupabaseCatalogRepository; embedded select ile
    mağaza adı + görseller tek istekte — N+1 yok) → feature:catalog (CatalogViewModel + CatalogScreen
    [arama + kategori filtre + 2-sütun grid, Coil görsel] + ProductDetail). 4-durum + reduce-motion.
  - **Para birimi:** kuruş tamsayı; UI'da PriceTag yerelleştirir (tr-TR).
- **Sonuçlar:** Alıcı home→katalog→ürün detay akışı. Sepet/sipariş sonraki dilim. Migration canlıya
  CI yeşil sonrası uygulanır (kullanıcı onayı: "kod yeşil olunca uygula").
- **Değerlendirilen alternatifler:** Fiyatı decimal/float tutmak (reddedildi: para bütünlüğü);
  görselleri ayrı istekle çekmek (reddedildi: N+1 — embedded select tercih edildi).

---

## ADR-014 — Marka adı: SeçAl + tam paket rename
- **Durum:** Kabul · TOGAF Phase A/H (vizyon + governance) · ISO 9241 (akılda kalıcılık)
- **Bağlam:** Ürün geçici "Köyden" kod-adıyla geliştirildi; kalıcı marka adı gerekiyordu. Aday
  setleri (köy/doğa temalı) kullanıcıya "itici" geldi; isim LLM brief'i (`docs/marka/isim-promptu.md`)
  ile dışarıda üretildi ve kullanıcı **SeçAl** ("Seç + Al") adını seçti.
- **Karar:**
  - Marka adı **SeçAl** — emir kipli, eylem-odaklı (seç → al), pazaryeri çağrışımı net, kısa, akılda kalıcı.
  - **Tam rename** (kullanıcı kararı, kapsam sorusu): `com.koyden` → `com.secal` (89 dosya + 14 paket
    dizini taşındı), bileşenler `Koyden* → Secal*` (Button/Card/TextField/Theme/Application/NavHost),
    `applicationId`/`namespace` = `com.secal.*`, `rootProject.name = Secal`, pref dosyaları
    `secal_secure_prefs`/`secal_auth`, `app_name = SeçAl`, görünen tüm "Köyden" → "SeçAl".
    Tüm dokümanlar (EA/ISO/yol-haritası/brain) dahil. Doğrulama: repo'da kalan `koyden`/`Köyden` = 0.
- **Sonuçlar:** Kod ve marka tam tutarlı; pre-release olduğundan paket/pref değişimi mevcut kullanıcıyı
  etkilemez. Geri dönüş zahmetli olduğu için **en ucuz an** (MVP iskeleti) tercih edildi.
- **Değerlendirilen alternatifler:** Sadece marka string'i + iç `com.koyden` kod-adı (reddedildi:
  kullanıcı tam tutarlılık istedi); köy/doğa temalı adlar (reddedildi: "itici").

---

## ADR-015 — Faz 4: Satıcı (mağaza & ürün yönetimi + görsel upload)
- **Durum:** Kabul · TOGAF Phase C/D · ISO 25010 · (ADR-014 isim kararına ayrıldı)
- **Bağlam:** İki-taraflı pazaryeri — satıcı mağaza açıp ürün eklemeli; katalog gerçek ürünle dolsun.
  Mevcut 0002_catalog şeması zaten satıcı-yazma RLS'ine sahip (owns_store) → **yeni migration gerekmez**.
- **Karar:**
  - `install(Storage)` (Supabase) — `product-images` bucket'a görsel upload; public URL ürüne işlenir.
  - domain `SellerRepository` (getMyStore, createStore, getMyProducts, createProduct, uploadProductImage)
    + `NewProduct` taslağı → data `SupabaseSellerRepository` (insert+select, slug üretimi, Storage upload;
    katalog DTO/PRODUCT_COLUMNS modül-içi `internal` yeniden kullanılır) → `feature:seller`
    (SellerViewModel faz-makinesi [Loading/NeedsStore/Ready/Error]; StoreSetupForm; ProductList;
    AddProduct: kategori filtre + fiyat parse [kuruş] + **PickVisualMedia** görsel seçici → upload).
  - Navigasyon: home → "Satıcı paneli" → mağaza kur/ürün listesi → ürün ekle. ON_RESUME'da liste tazelenir.
- **Sonuçlar:** Satıcı mağaza açıp görselli ürün yayınlayabilir; ürün katalogda görünür. Rol yükseltme
  (buyer→seller) RLS'te mağaza-sahipliğiyle örtüldü; ayrı rol kapısı Faz: admin'de.

---

## ADR-016 — Faz 5a: Sepet (cart_items + atomik RPC)
- **Durum:** Kabul · TOGAF Phase C/D · ISO 25010 (güvenilirlik) · 27002 (PII/RLS)
- **Bağlam:** Alıcının ürün→sepet akışı gerekiyor. Sepet PII'dir; eşzamanlı "sepete ekle"de
  miktar tutarlılığı (race) korunmalı.
- **Karar:**
  - Backend `0003_cart.sql` (marketing-2): `cart_items` (user_id+product_id unique, qty 1..99 check),
    deny-by-default RLS (yalnız sahibi), `set_updated_at` trigger. Sepet "başlığı" (carts) MVP'de
    cart_items'a (user_id anahtarlı) katlandı — gereksiz join yok. **Atomik** `add_to_cart(product, qty)`
    RPC: `insert ... on conflict do update set qty = least(qty+excluded, 99)` (race-safe artış).
  - Android: domain `CartRepository` (+CartItem, satır toplamı kuruş) → data `SupabaseCartRepository`
    (embedded select `products(PRODUCT_COLUMNS)` katalog DTO'su modül-içi yeniden kullanım; rpc/update/delete)
    → `feature:cart` (UiState 4-durum; QuantityStepper; kaldır; alt özet toplam). Ürün detayına
    **Sepete ekle** + snackbar geri bildirimi ("Sepete git"). Home "Sepetim". Çapraz-feature bağımlılığı
    yok (onOpenCart/onExplore app seviyesinden geçer).
- **Sonuçlar:** Alıcı sepet yönetebilir. **Migration canlıya uygulanmadı** (kullanıcı izni gerekli —
  uygulanana kadar sepet runtime'da çalışmaz). Sipariş/checkout = ADR-017 (Faz 5b).
- **Değerlendirilen alternatifler:** İstemci upsert ile miktar set (reddedildi: race + artış yapamaz →
  RPC tercih); ayrı carts başlık tablosu (ertelendi: MVP'de gereksiz join).

---

## ADR-017 — UI: Trendyol-kalıbı navigasyon (alt menü + Anasayfa + Hesabım)
- **Durum:** Kabul · TOGAF Phase C (Application) · ISO 9241 + 25010 Interaction Capability ·
  Değişiklik sınıfı: **Yeniden-mimari** (navigasyon iskeleti). Tetik: kullanıcı geri bildirimi
  ("anasayfa yok, menüler yok, berbat — Trendyol gibi yap").
- **Bağlam:** Giriş sonrası ekran 4 düğmelik placeholder'dı; gerçek anasayfa/gezinme yoktu.
- **Karar:**
  - **M3 NavigationBar (alt menü)** — 4 sekme: Anasayfa · Keşfet (katalog) · Sepet · Hesabım.
    Çapa = Anasayfa; `popUpTo(home){saveState}` + restoreState ile sığ yığın & sekme durumu korunur.
    Derin ekranlar (ürün detay, satıcı, profil) çubuğu gizler.
  - **Anasayfa** (`feature:home`): arama çubuğu (→Keşfet) + marka banner + kategori şeridi (avatar) +
    "Öne çıkan ürünler" yatay şeridi (boşsa satıcıya yönlendiren CTA). Atomic Design, 8pt grid,
    ProductCard yeniden kullanımı.
  - **Hesabım** (`feature:home`): menü satırları (Profilim / Satıcı paneli / Çıkış) — liste kalıbı.
  - Çapraz-feature bağımlılığı yok: tüm geçişler app `SecalNavHost`'tan callback ile bağlanır.
- **Sonuçlar:** Trendyol benzeri gezinme; placeholder kaldırıldı (supersedes eski HomePlaceholder).
  Kategori tıklaması şimdilik Keşfet'e götürür (filtre arg'ı sonraki iyileştirme).
- **Değerlendirilen alternatifler:** Navigation Drawer (reddedildi: mobil pazaryeri için alt menü
  standart) · tek ekran + buton listesi (reddedildi: kullanıcı reddetti).

---

## ADR-018 — Faz 5b: Sipariş (orders/order_items + place_order RPC)
- **Durum:** Kabul · TOGAF Phase C (Data+Application) · ISO 25010 Reliability + 25012 (snapshot tutarlılık) ·
  27002 (PII/RLS). Değişiklik sınıfı: Artımlı.
- **Bağlam:** Sepetten siparişe geçiş gerekiyor; sipariş kaydı, ürün sonradan değişse/silinse bile sabit
  kalmalı (fiyat/ad snapshot). Stok düşümü ve sepet temizliği atomik olmalı (race/yarım sipariş yok).
- **Karar:**
  - Backend `0004_orders.sql`: `orders` (status enum check) + `order_items` (product_name/unit_price_minor
    **snapshot**) + deny-by-default RLS (yalnız sahibi). **`place_order()`** `security definer` RPC:
    toplam hesapla → order + order_items → stok düş (greatest(.,0)) → sepeti temizle (tek transaction).
  - Android: domain `OrderRepository` (+Order/OrderItem) → data `SupabaseOrderRepository` (rpc place_order
    decodeAs<String>, embedded order_items select) + DI → `feature:order` (Siparişlerim listesi + detay/onay,
    4-durum). Sepet "Siparişi tamamla" → `placeOrder` → sipariş detayına. Hesabım → Siparişlerim.
- **Sonuçlar:** Alıcı uçtan uca sipariş verebilir; geçmiş siparişler görüntülenir. **0004 canlıya uygulandı**
  (Management API). Satıcının sipariş görünümü + durum güncelleme = sonraki faz (admin/satıcı).
- **Değerlendirilen alternatifler:** İstemci-tarafı çok-adımlı yazma (reddedildi: atomik değil, RLS stok
  düşümüne izin vermez) → security definer RPC. order_items'ta canlı ürün FK fiyatı (reddedildi: snapshot şart).

---

## ADR-019 — Ödeme/escrow stratejisi: COD → lisanslı PSP Pazaryeri (escrow PSP'de)
- **Durum:** Kabul · TOGAF Phase B+C+D · ISO 25010 Security/Reliability · 27002 · genişletir ADR-007.
  Tam mimari: [[siparis-odeme-mimarisi]]. Değişiklik sınıfı: Yeniden-mimari (ödeme yeteneği).
- **Bağlam (kullanıcı tespiti):** Erken aşamada platforma güven yok. (A) Parayı platform tutamaz —
  hem satıcı güvenmez hem **yasal değil** (TR 6493 sK — para tutmak Ödeme Kuruluşu lisansı ister).
  (B) Parayı doğrudan satıcıya göndermek alıcıyı korumasız bırakır (göndermeme riski).
- **Karar:**
  - **Faz A (MVP):** **Kapıda Ödeme (COD)** — para akışı YOK; platform parayı hiç görmez (sıfır lisans/risk).
    Mevcut `place_order` (pending) buna uygun. Güven + hacim inşa et.
  - **Faz B:** **iyzico Pazaryeri** (lisanslı PSP). Alıcı→PSP öder, **escrow'u PSP tutar** (lisanslı),
    teslim onayında satıcıya payout + komisyon **split**. Platform parayı **custody etmez** → lisans gerekmez.
    Satıcılar sub-merchant (IBAN/KYC PSP'de). `PaymentGateway` portu + `IyzicoMarketplaceGateway` adapter;
    ödeme başlatma/webhook **Edge Function** (PSP secret server-side; kart verisi istemciye değmez, PCI SAQ-A).
  - **Kendi escrow'umuz ASLA** (yasal değil).
- **Sonuçlar:** Güven (lisanslı PSP) + alıcı koruması (escrow) + yasal uyum + komisyon, tek modelde.
  Faz B'de orders.status genişler (paid/preparing/completed/refunded) + payments/payouts/sub-merchant tabloları.
- **Değerlendirilen alternatifler:** Kendi escrow (reddedildi: lisans/yasal + güven) · doğrudan satıcıya
  (reddedildi: alıcı koruması yok) · sadece online tahsilat baştan (ertelendi: önce COD ile güven).

---

**Standartlar:** TOGAF Phase H ADR governance · 42010 karar kaydı · ADR-001..019 kilitlenen
kararları belgeler (ADR-014 = marka adı **SeçAl**) · supersedes mekanizması tanımlı.
