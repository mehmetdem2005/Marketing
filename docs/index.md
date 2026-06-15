---
type: moc
title: Köyden — Bilgi Haritası
ai-first: true
tags: [moc, index, koyden]
updated: 2026-06-15
---

# Köyden — Bilgi Haritası (MOC)

> Bu klasör (`docs/`) bir **Obsidian vault**'tur. Obsidian'da "Open folder as vault" ile
> bu klasörü aç → tüm kararlar/mimari notları wikilink'lerle gezilir. Kaynak: GitHub
> `mehmetdem2005/Marketing` · dal `claude/village-products-marketplace-c08a30` · `docs/`.

## Çekirdek notlar
- [[mimari-karar-gunlugu]] — **ADR karar günlüğü** (ADR-001..012). Her mimari karar burada.
- [[EA-TOGAF-mimari]] — Kurumsal mimari (TOGAF ADM, kapasiteler, veri zonları, bileşenler).
- [[altyapi-kaynaklar]] — Canlı altyapı & sır yönetimi (Supabase projesi, CI, Render durumu).
- [[kurumsal-surec-ve-calisma-standardi]] — Çalışma/süreç standardı (kalite kapıları, akış).

## Kalite & uyum
- [[guvenlik]] — Güvenlik modeli (RLS matrisi, PII zonları, ISO 27001/27002).
- [[ISO-uyumluluk]] — ISO 42010/25010/25012/29148/9241 izlenebilirliği.
- [[tasarim-sistemi]] — Tasarım sistemi (token'lar, bileşenler, 4-durum, motion, a11y).

## Durum (2026-06-15)
- **Android CI yeşil**; Faz 1 (tasarım sistemi + Supabase Auth) + Faz 2 (Profil + RLS) inşa edildi.
- Açık işler için → [[altyapi-kaynaklar#Açık işler]].

## Nasıl gezilir (Obsidian)
- **Graph view** ile notlar arası bağları gör.
- `[[` yazıp wikilink ile not ara/bağla.
- Semantik arama için (kurulu ortamlarda) `qmd query "..."`.
