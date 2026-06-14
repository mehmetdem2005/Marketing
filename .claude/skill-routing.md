# Skill Yönlendirme — Köyden

> Bu dosya her oturum başında (SessionStart hook) bağlama enjekte edilir.
> İş türüne göre İLGİLİ skill'i Skill aracıyla yükle; gereksiz hepsini yükleme.
> **UI işinde DÖRTLÜ zorunlu:** design-standards + web-design-advanced + ui-ux-advanced + motion-design.

## Hafıza & bilgi yönetimi (kalıcı "ikinci beyin")
- **obsidian-second-brain** — bilgi tabanını kendini-yeniden-yazan ikinci beyin gibi yönet: karar/öğrenilen/araştırma kaydet, çelişkileri uzlaştır. Konuşma kalıcı değer ürettiğinde PROAKTİF kullan. Komutlar: `/obsidian-ingest`, `/obsidian-decide`, `/obsidian-adr`, `/obsidian-health`, `/obsidian-reconcile`, `/obsidian-save`, `/obsidian-architect`…
- **llm-wiki** — Karpathy LLM-Wiki kalıbı (ingest / query / lint) zihinsel modeli.
- **qmd** — markdown'da semantik arama; dosya okumadan ÖNCE proaktif kullan.
- **obsidian-mind** `/om-*` komutları + agent'lar (vault-librarian, cross-linker, context-loader…) — oturum başlat/bitir, bilgi tabanı bakımı.

## Köyden bilgi tabanı (kanonik gerçek)
`docs/EA-TOGAF-mimari.md` · `docs/mimari-karar-gunlugu.md` (ADR) · `docs/guvenlik.md` · `docs/ISO-uyumluluk.md` · `docs/tasarim-sistemi.md`
→ Anlamlı her değişiklik: **ADR yaz + EA güncelle**, çelişki → **supersedes** (second-brain disiplini). Sohbet kalıcı hafıza DEĞİL — durable bilgi git'teki markdown'a.

## UI / motion
- **motion-design** — timing/easing/choreography + Disney prensipleri + **MotionScore** kalite denetimi. Her animasyon/Compose işinde. reduce-motion zorunlu; "animasyon yok = iş bitmemiş".
- **design-standards / web-design-advanced / ui-ux-advanced** — her UI işinde birlikte (8pt grid, token, emoji-ikon yasağı, 4-durum, WCAG 2.2 AA, TOGAF+ISO).

## Web içerik
- **defuddle** — URL'den temiz markdown (WebFetch yerine; `.md` hariç).

## Genel kural
İşe başlamadan: ilgili skill'i yükle + bilgi tabanını tara. İş sonunda: kararları kaydet (second-brain) + "**Standartlar**" dipnotu (TOGAF faz/sınıf + ISO + uygulanan standartlar; ABARTMA YOK).
