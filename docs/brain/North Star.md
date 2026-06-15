---
type: brain-north-star
date: 2026-06-15
tags: [brain, north-star]
ai-first: true
---

# North Star

## For future Claude
Köyden'in yönü ve güncel odağı (oturum başında oku). Yön değişince güncelle.

## Vizyon
**Köyden** — köy/doğal/yöresel ürünlerin üreticiden (köy mağazası) doğrudan tüketiciye ulaştığı
iki-taraflı pazaryeri. **Trendyol kalitesi** çıta. Native Android (Jetpack Compose) + Supabase;
admin dahil her şey mobil uygulamada.

## Güncel odak
- Pazaryeri çekirdeğini faz faz tamamlamak: ✅ Auth ✅ Profil ✅ Katalog/keşif → **sıradaki: Sepet & Sipariş**.
- Her dilim: hexagonal + 4-durum + RLS + Trendyol kalite; CI yeşil → main merge → deploy.

## Hedefler
### Kısa vade
- Sepet & Sipariş dilimi (cart/order) + ürün→sepet akışı.
- Satıcı tarafı: mağaza aç + ürün ekle (seller).
### Orta vade
- Değerlendirme/yorum (yalnız teslim edilmiş siparişe), arama iyileştirme, bildirim.
- Sunucu-tier kararı (Supabase Edge vs Render) — ödeme webhook/zamanlı iş gerekince.

## İlkeler
- Para bütünlüğü (kuruş), gizlilik (RLS deny-by-default, PII zonu), erişilebilirlik (WCAG 2.2 AA).
- Sır değeri repoya girmez. DB migration canlıya yalnız kullanıcı onayıyla.
