---
type: roadmap
title: SeçAl — Faz Faz Mimari Yol Haritası
date: 2026-06-15
tags: [roadmap, mimari, plan, secal]
ai-first: true
---

# SeçAl (çalışma adı) — Faz Faz Mimari Yol Haritası

## For future Claude
Pazaryeri ürününün uçtan uca faz planı. Her faz: **amaç · mimari (domain/data/feature + DB/RLS) ·
kalite kapısı · bitti kriteri**. Tamamlananlar işaretli. Her faz hexagonal + 4-durum + RLS +
motion + TOGAF/ISO ile yapılır; CI yeşil → main merge → deploy. Kalite çıtası: **Trendyol**.
İlgili: [[index]] · [[North Star]] · [[Key Decisions]] · [[Patterns]] · [[mimari-karar-gunlugu]].

---

## ✅ Tamamlanan
- **Faz 0 — İskelet:** pnpm/Gradle monorepo, modüller (app/core/feature), CI (gitleaks+detekt+lint+test+build), Supabase provisioning. → [[altyapi-kaynaklar]]
- **Faz 1 — Tasarım sistemi + Auth:** token'lar, 12 bileşen, 4-durum, motion; e-posta/şifre + Google giriş (ADR-004). CI yeşil.
- **Faz 2 — Profil + Katalog:** Profil görüntüle/düzenle (RLS, ADR-012); Katalog/keşif (kategori/ürün/arama, embedded select, RLS, Storage, ADR-013). **Canlı** (migration uygulandı, main'e merge).

---

## 🔜 Sıradaki fazlar

### Faz 3 — İsim & Marka Kimliği  *(isime ayrılmış faz)*
- **Amaç:** "SeçAl" çalışma adını değerlendirip kalıcı **isim + marka kimliğini** kilitlemek (özellikler büyümeden önce).
- **İş kalemleri:**
  - İsim adayları üret (10-15): anlam (köy/doğal/yerel/güven), söyleyiş, akılda kalıcılık, Türkçe uyum, olumsuz çağrışım taraması.
  - **Müsaitlik:** `.com`/`.com.tr` domain, Play Store paket adı (`com.<isim>...`), sosyal medya handle, marka/ticari iz (kaba tarama).
  - Kısa liste (3) → değerlendirme matrisi → karar (ADR + supersedes "SeçAl" çalışma adı).
  - **Marka kimliği:** logo yönü, renk paleti (tasarım token revizyonu), tipografi, ton/ses; `docs/marka-kilavuzu.md`.
  - **Teknik rename:** `applicationId`/`namespace`, `strings.xml`/uygulama adı, paketler, repo/docs başlıkları, Supabase proje etiketi (ref sabit kalır).
- **Kalite kapısı:** marka kılavuzu vault'ta; rename sonrası CI yeşil; a11y kontrast (WCAG AA) yeni paletle.
- **Bitti:** isim kararı ADR'de; marka kılavuzu yayınlı; uygulama yeni isimle derleniyor.

### Faz 4 — Satıcı (Seller): Mağaza & Ürün Yönetimi
- **Amaç:** İki-taraflı pazaryeri — köy üreticisi mağaza açar, ürün ekler (katalog gerçek ürünle dolar).
- **Mimari:** DB `0003_seller` (`store_members` rolleri; products/stores write zaten RLS'te). domain `SellerRepository` (createStore, upsertProduct, uploadImage) → data (Postgrest + **Storage** upload, `product-images` bucket) → `feature:seller` (mağaza formu, ürün CRUD, çoklu görsel, stok/fiyat). Rol yükseltme: buyer→seller.
- **Kalite kapısı:** RLS satıcı izolasyonu testi; görsel boyut/format doğrulama; 4-durum.
- **Bitti:** satıcı mağaza açıp ürün yayınlayabiliyor; ürün katalogda görünüyor.

### Faz 5 — Sepet & Sipariş (Cart & Order)
- **Amaç:** Alıcı ürün→sepet→sipariş akışını tamamlasın.
- **Mimari:** DB `0004_orders` (`carts`, `cart_items`, `orders`, `order_items`; PII zonu; stok düşümü trigger/transaction). domain `Cart/Order` + use-case'ler → data → `feature:cart` + `feature:order` (sepet, adres seçimi [[Profil]] adresleri, sipariş özeti, sipariş geçmişi/durum).
- **Kalite kapısı:** stok tutarlılığı (race), fiyat kuruş bütünlüğü, RLS sahibi-erişimi.
- **Bitti:** alıcı sipariş oluşturabiliyor; satıcı siparişi görüyor.

### Faz 6 — Ödeme & Sunucu-tier
- **Amaç:** Gerçek ödeme + webhook; **sunucu-tier kararı** (Supabase Edge vs Render) netleşir.
- **Mimari:** ADR (Edge vs Render) → ödeme sağlayıcı (iyzico/Stripe) entegrasyonu; `payment_intents`; **webhook** (Edge Function veya Render) ile sipariş durumu; idempotency. Sırlar yalnız sunucu (service_role).
- **Kalite kapısı:** webhook imza doğrulama, idempotency, PII/PCI sınırları, hata kurtarma.
- **Bitti:** test ödemesi uçtan uca; sipariş "ödendi"ye geçiyor.

### Faz 7 — Değerlendirme & Güven
- **Amaç:** Sahte yorum/ürün riskine karşı güven (risk 3,6 — [[EA-TOGAF-mimari]]).
- **Mimari:** DB `reviews` (yalnız **teslim edilmiş siparişe** yorum — sipariş-yorum bağı); satıcı/ürün puanı (agregat); rapor/moderasyon kancası. `feature:reviews`.
- **Bitti:** alıcı teslim sonrası puan/yorum bırakıyor; ProductCard puanı gösteriyor.

### Faz 8 — Keşif & Arama (Gelişmiş)
- **Amaç:** Trendyol benzeri keşif derinliği.
- **Mimari:** filtre/sırala (fiyat/puan/yenilik), kategori ağacı gezinme, favoriler (`favorites`, PII), öneriler (basit), `pg_trgm`/FTS arama iyileştirme, sayfalama (keyset).
- **Bitti:** akıcı filtre/arama; favoriler; performanslı liste.

### Faz 9 — Bildirim & Mesajlaşma
- **Mimari:** FCM push (sipariş durumu), uygulama-içi bildirim merkezi, alıcı–satıcı mesajlaşma (`conversations`/`messages`, RLS). Watcher/asenkron iş için Edge.
- **Bitti:** sipariş olaylarında push; temel mesajlaşma.

### Faz 10 — Admin & Moderasyon
- **Mimari:** admin rolü (mobil-içi panel — ADR-032), satıcı/ürün/yorum denetimi, audit log, içerik bayrakları. RLS admin politikaları.
- **Bitti:** admin kullanıcı moderasyon yapabiliyor.

### Faz 11 — Yayın & Büyüme
- **Mimari:** Play Store (`eas build -p android`) + mobil-web (Vercel), tanıtım sitesi (SSG, GEO/pazarlama — [[EA-TOGAF-mimari]]), analytics, Core Web Vitals/RAIL, crash/perf izleme.
- **Bitti:** mağazada yayında; ölçümlenen büyüme döngüsü.

---

## Kesişen (her fazda)
Güvenlik (RLS deny-by-default, PII zonu, sır yönetimi — [[guvenlik]]) · Erişilebilirlik (WCAG 2.2 AA) ·
Motion (reduce-motion) · TOGAF ADM + ISO (42010/25010/25012/27001/29148 — [[ISO-uyumluluk]]) ·
Bilgi tabanı: her faz ADR + [[Key Decisions]] + [[Gotchas]] + log güncellemesi (obsidian-second-brain/mind).

## Önerilen sıra
Faz 4 (Satıcı) → Faz 5 (Sepet/Sipariş) **MVP çekirdeği**; **Faz 3 (İsim)** paralel/erken kilitlenebilir
(marka, yayın öncesi). Ödeme (6) MVP'yi ticari yapar.
