---
type: operation-log
title: Vault İşlem Günlüğü
ai-first: true
tags: [log, meta]
---

# İşlem Günlüğü (append-only)

## For future Claude
Bu dosya vault'taki her anlamlı işlemin kronolojik, yalnızca-ekleme kaydıdır
(obsidian-second-brain disiplini). Silme/yeniden yazma yok — sadece alt satıra ekle.
Format: `## [YYYY-MM-DD] aksiyon | açıklama`.

## [2026-06-15] setup | docs/ Obsidian vault'una çevrildi (.obsidian + [[index]] + [[_CLAUDE]]); qmd kuruldu, indekslendi (9 not, 31 vektör).
## [2026-06-15] feat | Faz 2 Profil + RLS inşa edildi (ADR-012); Android CI yeşil (ff99638).
## [2026-06-15] ci | Supabase URL/anon CI build'ine bağlandı; BuildConfig .trim() (unclosed-string fix).
## [2026-06-15] policy | Kullanıcı kararı: bundan sonra yeşil dal her zaman main'e merge edilir (otomatik deploy). Kalite çıtası: Trendyol.
## [2026-06-15] infra | main dalı yeşil HEAD'den oluşturuldu (repoda yoktu); merge iş akışı kuruldu.
## [2026-06-15] tooling | obsidian-second-brain skill'i fiilen yüklendi/uygulanıyor; qmd kuruldu+indeksli (BM25+vektör).
## [2026-06-15] feat | Katalog dikey dilimi (ADR-013): 0002_catalog migration (marketing-2) + Android domain/data/UI (feature:catalog). Migration canlıya CI yeşil sonrası uygulanacak.
## [2026-06-15] db | 0002_catalog canlı SeçAl Supabase'e uygulandı (HTTP 201): 6 tablo, 8 kategori seed, 10 RLS politikası doğrulandı.
## [2026-06-15] merge | Katalog yeşil (eff9c5c) → main'e merge (665377b). main + claude dalı senkron.
## [2026-06-15] plan | Faz faz mimari yol haritası ([[yol-haritasi]]) çıkarıldı: Faz 0-11; Faz 3 = İsim & Marka (isime ayrılmış faz). index/North Star bağlandı.
## [2026-06-15] brand | Faz 3 başladı: isim adayları + matris + domain DNS taraması → [[isim-adaylari]]. Karar kullanıcıda.
## [2026-06-15] brand | Kullanıcı adayları beğenmedi → harici LLM için isim-bulma promptu hazırlandı ([[isim-promptu]]). İsim gelince müsaitlik + ADR-014 + rename.
## [2026-06-15] feat | Faz 4 Satıcı (ADR-015): install(Storage) + SellerRepository/data + feature:seller (mağaza kur, ürün ekle, görsel upload PickVisualMedia). Yeni migration gerekmedi (RLS owns_store mevcut).
## [2026-06-15] merge | Faz 4 Satıcı yeşil (9c0ebd5) → main'e merge. Satıcı uçtan uca: mağaza aç + görselli ürün ekle → katalogda görünür.
## [2026-06-15] brand | Faz 3 (ADR-014): marka adı = SeçAl. Tam rename com.koyden→com.secal (89 dosya, 14 paket dizini, Koyden*→Secal*, app_name=SeçAl). Marka kılavuzu eklendi. Kalan koyden/Köyden=0.
## [2026-06-15] feat | Faz 5a Sepet (ADR-016): cart_items+add_to_cart RPC (M2 0003), domain/data/feature:cart, ürün detayı "Sepete ekle"+snackbar, home "Sepetim". Migration canlıya UYGULANMADI (izin bekliyor).
## [2026-06-15] infra | Canlı Supabase (koyden projesi yampwgdlqncdgwjslige) Management API ile bağlandı: 0003 sepet migration uygulandı (0001/0002 zaten vardı), e-posta autoconfirm açıldı, product-images bucket doğrulandı. anon key+URL gradle.properties'e gömüldü (public; gitleaks allowlist). APK artık backend'e bağlı derlenecek.
## [2026-06-15] feat | UI yeniden tasarım (ADR-017): M3 alt menü (Anasayfa/Keşfet/Sepet/Hesabım) + zengin Anasayfa (arama+banner+kategori şeridi+öne çıkan ürünler) + Hesabım menüsü. HomePlaceholder kaldırıldı. Trendyol kalıbı. Kullanıcı geri bildirimi üzerine.
## [2026-06-15] merge | UI yeniden tasarım (ADR-017) yeşil → main. Alt menü + Anasayfa + Hesabım canlı. Yeni APK artifact hazır.
## [2026-06-15] feat | Faz 5b Sipariş (ADR-018): orders/order_items + place_order RPC (M2 0004 canlıda), domain/data/feature:order (Siparişlerim+detay), sepet checkout, Hesabım→Siparişlerim.
## [2026-06-15] chore | Agent skill'leri repoya kuruldu (.claude/skills): GitHub'dan compose-expert (aldefy) + android-kotlin (Drjacky/ninja) Kotlin/Compose skill'leri; tasarım/motion skill'leri (design-standards, web-design-advanced, ui-ux-advanced, motion-design) mse-auto'dan taşındı. Kullanıcı talebi.
