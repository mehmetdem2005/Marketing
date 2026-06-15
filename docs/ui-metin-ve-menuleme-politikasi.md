# SeçAl — UI Metin (i18n) & Menüleme Politikası

> Amazon/Trendyol kalitesinde, çoklu-dile hazır arayüz kuralları. **Zorunlu.**
> Karar: [[mimari-karar-gunlugu]] · Tasarım skill'leri: `.claude/skills/` (design-standards, compose-expert).

## 1. Metin = dinamik (i18n), asla hardcode değil
- **Kullanıcıya görünen HER metin** `res/values/strings.xml`'de (modül başına). Composable'da
  `stringResource(R.string.key)`; literal string YASAK.
- **Çoklu dil hazır:** ileride `values-en/`, `values-de/`, `values-ar/` eklenir; kod değişmez.
  Sayılar için `plurals`, parametre için `%1$s` / `%1$d`.
- **İsimlendirme:** `<alan>_<eleman>` → `nav_home`, `home_search_hint`, `account_orders`,
  `cart_checkout`, `cd_back` (content description). Tutarlı, küçük harf, alt çizgi.
- **Marka/pazarlama metni (slogan, banner yazısı) ASLA onaysız yazılmaz.** Yapı kurulur, metin
  kullanıcıdan alınır; placeholder gerekiyorsa nötr (ör. yalnız marka adı) bırakılır.

## 2. Menüleme (Amazon/Trendyol kalitesi)
- **Alt menü (NavigationBar):** 4–5 ana hedef (Anasayfa · Keşfet · Sepet · Hesabım). Aktif sekme
  M3 pill göstergesi + dolu ikon; pasif outline ikon. Etiketler string kaynağından.
- **Sekme durumu korunur:** `popUpTo(home){saveState}` + `restoreState` → sekmeye dönünce kaldığın yer.
- **Derin ekranlar** (ürün detay, sipariş detay, satıcı): alt menü **gizlenir** + üstte geri butonlu
  `TopAppBar`. Geri afordansı her derin ekranda tutarlı.
- **Hesabım** = hesap menüsü (Profil · Siparişlerim · Satıcı · Çıkış) liste kalıbı; ikon + ok.
- **Boş/yükleme/hata** her listede 4-durum (UiStateScaffold).

## 3. Motion (wow, ölçülü)
- **Ekran geçişi:** ileri = slide-in (Start) + fade; geri = slide-out (End) + fade;
  süre `MotionTokens.DurationStandard`. **`rememberReduceMotion()` → anında (tween 0).**
- **Mikro-etkileşim:** buton press scale; sepete-ekle snackbar; liste girişleri ölçülü.
- Sabit ms YASAK → token. "Animasyon yok = iş bitmemiş" ama "abartı animasyon = kötü"; M3 emphasized easing.

## 4. Erişilebilirlik (WCAG 2.2 / ISO 9241)
- contentDescription string kaynağından; dekoratif görselde `null`.
- Dokunma hedefi ≥48dp; kontrast AA; odak görünür.

---
**Uygulama durumu:** app (nav) + feature:home (Anasayfa/Hesabım) i18n + motion geçişleri uygulandı.
**Sırada:** feature:catalog · cart · order · seller · profile · auth + core:ui/designsystem string'leri
bu politikaya taşınacak (kademeli; her modül kendi strings.xml'i).

**Standartlar:** design-standards (8pt/Atomic/M3) · ISO 9241 + WCAG 2.2 · 29148 (izlenebilir metin) ·
TOGAF Phase C · i18n-ready (tek-kaynak strings).
