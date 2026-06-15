---
type: operating-manual
title: Vault Operating Manual (Köyden docs)
ai-first: true
tags: [meta, claude, vault]
updated: 2026-06-15
---

# Vault Operating Manual — `docs/`

Bu klasör Köyden'in **ikinci beyni**dir (self-rewriting knowledge base). Claude ve
katkıda bulunanlar için kurallar:

## İlkeler
1. **Tek doğruluk kaynağı:** Her mimari karar [[mimari-karar-gunlugu]]'nde bir **ADR**'dir.
   Karar değişirse yeni ADR yaz ve eskisini `supersedes` ile işaretle (silme).
2. **AI-first frontmatter:** Yeni notlara `type`, `title`, `tags`, `updated`, `ai-first: true` ekle.
3. **Wikilink disiplini:** Notlar arası bağ `[[not-adı]]` ile kurulur; yeni not eklenince
   [[index]]'e (MOC) bağla.
4. **Sır YOK:** Hiçbir gizli değer (anahtar/şifre) bu vault'a yazılmaz — yalnız *nerede*
   durduğu. (gitleaks kapısı + [[guvenlik]].)
5. **Çelişki → uzlaştır:** Bir not başka notla çelişiyorsa, güncel olanı koru ve eskisini
   düzelt/işaretle (reconcile).

## Standartlar (her UI/kod işinde)
`design-standards` + `web-design-advanced` + `ui-ux-advanced` + `motion-design` →
detaylar [[tasarim-sistemi]], [[kurumsal-surec-ve-calisma-standardi]]. TOGAF/ISO izi
[[EA-TOGAF-mimari]] ve [[ISO-uyumluluk]]'ta.

## Araçlar
- **Semantik arama (kurulu ise):** `qmd query "<soru>"` — dosya okumadan önce proaktif kullan.
- **Obsidian:** "Open folder as vault" → `docs/`. Graph view ile bağları gör.
