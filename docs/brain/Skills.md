---
type: brain-index
date: 2026-06-15
tags: [brain, index, skills]
ai-first: true
---

# Skills

## For future Claude
Bu ortamda kurulu yapay-zeka skill'leri ve komutları. Hepsi `~/.claude/skills` + `~/.claude/commands`'te.

## Hafıza & bilgi (Obsidian ekosistemi)
- **obsidian-second-brain** (eugeniughelbur) — kendini-yeniden-yazan ikinci beyin; `obsidian-*` komutları (`/obsidian-save`, `/obsidian-ingest`, `/obsidian-reconcile`, `/obsidian-adr`…).
- **obsidian-mind** (breferrari) — kalıcı çalışma hafızası (bu `brain/` yapısı); `om-*` komutları (`/om-standup`, `/om-dump`, `/om-wrap-up`, `/om-weekly`…).
- **obsidian-skills**: kepano (Steph Ango) → `obsidian-markdown`, `obsidian-bases`, `obsidian-cli`, `json-canvas`, `defuddle`; qhuang20 → `llm-wiki` (Karpathy paterni).
- **qmd** (`@tobilu/qmd`) — yerel BM25 + vektör arama. `qmd search "..." -c docs` (hızlı), `qmd query` (hibrit, CPU'da yavaş). Koleksiyon: `docs`.

## Arayüz
- **motion-design** (motion.dev / LottieFiles) — animasyon prensipleri + performans; reduce-motion zorunlu.

## Kullanım disiplini
- Yazmadan önce `qmd`/`/obsidian-find` ile ara (kopya not = vault çürümesi).
- Her anlamlı çıktı: cevap + vault güncellemesi (Two-Output). Karar → ADR + [[Key Decisions]]. Tuzak → [[Gotchas]].
- Oturum sonunda `/om-wrap-up` veya `/obsidian-save` mantığıyla kalıcılaştır.
