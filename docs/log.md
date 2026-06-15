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
## [2026-06-15] db | 0002_catalog canlı Köyden Supabase'e uygulandı (HTTP 201): 6 tablo, 8 kategori seed, 10 RLS politikası doğrulandı.
## [2026-06-15] merge | Katalog yeşil (eff9c5c) → main'e merge (665377b). main + claude dalı senkron.
## [2026-06-15] plan | Faz faz mimari yol haritası ([[yol-haritasi]]) çıkarıldı: Faz 0-11; Faz 3 = İsim & Marka (isime ayrılmış faz). index/North Star bağlandı.
## [2026-06-15] brand | Faz 3 başladı: isim adayları + matris + domain DNS taraması → [[isim-adaylari]]. Karar kullanıcıda.
## [2026-06-15] brand | Kullanıcı adayları beğenmedi → harici LLM için isim-bulma promptu hazırlandı ([[isim-promptu]]). İsim gelince müsaitlik + ADR-014 + rename.
## [2026-06-15] feat | Faz 4 Satıcı (ADR-015): install(Storage) + SellerRepository/data + feature:seller (mağaza kur, ürün ekle, görsel upload PickVisualMedia). Yeni migration gerekmedi (RLS owns_store mevcut).
## [2026-06-15] merge | Faz 4 Satıcı yeşil (9c0ebd5) → main'e merge. Satıcı uçtan uca: mağaza aç + görselli ürün ekle → katalogda görünür.
