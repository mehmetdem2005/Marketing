---
type: brain-key-decisions
date: 2026-06-15
tags: [brain, decisions]
ai-first: true
---

# Key Decisions

## For future Claude
Oturumlar arası hatırlanması gereken mimari/iş akışı kararları. Tam kayıt: [[mimari-karar-gunlugu]] (ADR-001..013).

- **Native Android (Compose), tek ürün:** admin dahil her şey mobil uygulamada (ADR-001/002/032).
- **Supabase doğrudan istemci:** anon + RLS; service_role yalnız sunucu/Edge (ADR-002/005).
- **Hexagonal + DataResult + 4-durum:** tüm dilimlerin iskeleti (ADR-003/004).
- **Hilt KAPT** (KSP component üretmiyor).
- **core:common = kotlin.jvm** (domain ile variant uyumu).
- **Para = price_minor kuruş (bigint).**
- **Katalog public read / satıcı write (RLS, owns_store);** embedded select (ADR-013).
- **İş akışı:** claude/... → CI yeşil → main merge → deploy. Migration canlıya yalnız kullanıcı onayıyla.
- **Bilgi tabanı = bu vault** (obsidian-second-brain + obsidian-mind brain); kararlar ADR + log + index olarak yazılır; çelişki → supersedes.
- **Kalite çıtası: Trendyol.**
