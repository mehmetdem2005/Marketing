# SeçAl — Marka Kılavuzu

> Karar: [[mimari-karar-gunlugu#ADR-014 — Marka adı SeçAl + tam paket rename|ADR-014]] ·
> Üretim brief'i: [[isim-promptu]] · Aday değerlendirmesi: [[isim-adaylari]]

## İsim
**SeçAl** — "Seç + Al". Emir kipli, eylem-odaklı; kullanıcının pazaryerinde yaptığı iki temel
eylemi (ürünü **seç**, sonra **al**) tek kelimede birleştirir.

- **Söyleyiş/yazım:** Bitişik, kamel-case: **SeçAl** (iki büyük harf vurgusu eyleme ritim verir).
  Tam küçük/tam büyük yazımdan kaçın (logo dışında). ASCII gerekli yerlerde (paket, alan adı,
  e-posta) `secal`.
- **Kod kimliği:** `com.secal` (paket), `applicationId = com.secal.app`. Marka adı (UI) `SeçAl`,
  teknik kimlik `secal` — ç'siz ASCII.

## Konumlandırma
Daraltılmış nişte (köy / doğal / üreticiden ürünler) **Trendyol kalitesinde** bir pazaryeri.
Marka tonu: **kurumsal + sıcak** (motion kişiliğiyle tutarlı — bkz. `MotionTokens`).

- **Ses tonu:** Net, güven veren, kısa cümleler. Emoji yok (ürün genelinde emoji-ikon yasağı —
  lucide/vektör ikon). Abartısız, eyleme çağıran ("Seç, al, kapına gelsin").
- **Vaat:** Üreticiden alıcıya doğrudan, güvenli, hızlı.

## Görsel kimlik (yön)
> Logo/asset üretimi ayrı iş; burada yalnız yön verilir.

- **Logo fikri:** Sözcük-işareti (wordmark) "Seç**Al**" — "Al" hecesi vurgu rengiyle. Alternatif:
  sepet/onay (✓) çağrışımını **vektör** ikona gömen sade bir işaret.
- **Renk:** Designsystem token'ları kanonik (`core/designsystem/theme/Color.kt`). Marka aksanı
  sıcak bir vurgu rengi; nötr zemin. (Kesin paleti tasarım sistemi belirler — tek kaynak.)
- **Tipografi:** Material 3 tipografi ölçeği (designsystem). Wordmark için tek, okunaklı,
  yarı-kalın bir grotesk yeterli.
- **İkonografi:** Yalnız vektör (lucide tarzı); emoji asla.

## Kullanım kuralları
- UI'da görünen ad **her zaman** `app_name` string kaynağından (`SeçAl`) gelir; sabit string yazma.
- Teknik/teknik-olmayan ayrımı: kullanıcı "SeçAl" görür; kod/altyapı `secal` kullanır.
- Yeni modül/paket: `com.secal.<katman>.<özellik>`.

---

**Standartlar:** design-standards (marka tonu/ikonografi) · web-design-advanced (wordmark/tipografi
yönü) · ISO 9241 (akılda kalıcılık/öğrenilebilirlik) · TOGAF Phase A (vizyon/ilke) · tek-kaynak
(app_name + designsystem token) ilkesi.
