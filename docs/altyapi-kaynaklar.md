---
type: infra-resources
date: 2026-06-14
tags: [altyapi, supabase, render, secrets]
ai-first: true
---

# SeçAl — Altyapı & Kaynaklar

## For future Claude
SeçAl'in canlı bulut kaynakları ve sır yönetiminin tek kaydı. Supabase projesi
provisioned (Android doğrudan buna bağlanır — ADR-002/010). **Sır DEĞERLERİ bu repoda
asla bulunmaz**; yalnız non-secret tanımlayıcılar (proje ref, URL) + sırların *nerede*
durduğu yazılır. Yeni kaynak/karar → burayı güncelle + ADR yaz (self-rewriting; çelişki → supersedes).

## Supabase — SeçAl projesi (CANLI)
- **Proje adı:** `secal`
- **Ref / Project ID:** `yampwgdlqncdgwjslige`  *(public tanımlayıcı)*
- **URL:** `https://yampwgdlqncdgwjslige.supabase.co`  *(public; istemciye gömülür)*
- **Bölge:** `eu-central-1` (Frankfurt) · **Org:** `mehmetdem2005's Org` (`ghdmtojktewtwcyiazev`)
- **Plan:** free · **Durum:** ACTIVE_HEALTHY (2026-06-14)
- **Şema:** `0001_init` uygulandı → `profiles` (PII) + `addresses` + RLS (deny-by-default)
  + `handle_new_user` trigger + `user_role` enum. (Kaynak: `marketing-2/supabase/migrations/`)
- **Anahtarlar (anon / service_role / db_pass):** repoda DEĞİL. Container `/home/user/.secrets/`
  altında (oturumluk) + kalıcı kullanım için **CI secret / local.properties**'e girilir
  (`SECAL_SUPABASE_URL`, `SECAL_SUPABASE_ANON_KEY`). service_role yalnız sunucu/Edge tarafında.

## Render
- **Hesap:** My Workspace (`tea-...`, mehmetdem2005). SeçAl için **henüz servis oluşturulmadı.**
- **Durum:** Sunucu tarafı ihtiyacı (Stripe webhook / zamanlı iş / admin) **Supabase Edge Functions**
  ile karşılanabilir (mevcut `marketing-2/supabase/functions`). Ayrı **Render Node servisi**
  opsiyonu açık — backend kapsamı netleşince karar (ADR ile). Android mimarisi DEĞİŞMEDİ.

## Sır yönetimi (ISO 27001/27002 · ADR-005)
- Erişim token'ları (Whenly'den, kullanıcı izniyle): `render.key`, `SUPABASE_ACCESS_TOKEN`,
  `GITHUB_TOKEN` → yalnız `/home/user/.secrets/` (repo dışı). **Kullanıcı işi bitince revoke edecek.**
- Whenly'nin diğer prod sırları indirilmedi/silindi (gereksiz yayılım yok). Whenly kaynaklarına
  yalnız **okuma** yapıldı; hiçbir Whenly kaynağı değiştirilmedi.
- Kural: hiçbir sır **değeri** git'e girmez (gitleaks kapısı + bu disiplin).

## Durum (2026-06-15)
- **CI YEŞİL** ✅ (`android-ci.yml`, commit `ff99638`): gitleaks + detekt + lint + test +
  assembleDebug. **Faz 1** (tasarım sistemi + Supabase Auth) + **Faz 2** (Profil + RLS) derleniyor.
- **Supabase bağlı & çalışıyor:** `SECAL_SUPABASE_URL` + `SECAL_SUPABASE_ANON_KEY` GitHub Actions
  **variable** (anon/URL publishable) → CI build'inde BuildConfig'e geçiyor. service_role yalnız
  container/.secrets. Not: build.gradle değerleri `.trim()` eder (variable'da kaçak newline
  "unclosed string literal"e yol açmıştı — çözüldü).
- **Auth akışı uçtan uca:** giriş/kayıt (e-posta + Google) → home → profil (görüntüle/düzenle, RLS) → çıkış.

## Obsidian vault & yerel arama (qmd)
- **`docs/` artık bir Obsidian vault'tur** (`.obsidian/app.json` + [[index]] MOC + [[_CLAUDE]]
  operating manual). Obsidian'da "Open folder as vault" → `docs/`. Senkron köprüsü = GitHub repo
  (sandbox geçici; vault dosyaları repoda kalıcı).
- **Semantik/BM25 arama:** `@tobilu/qmd` (npm global). Yeniden kurulum (yeni/temiz ortam):
  ```
  npm install -g @tobilu/qmd
  qmd collection add <repo>/docs   # 'docs' koleksiyonu
  qmd update                       # indeksle (BM25 — offline çalışır)
  qmd embed                        # vektör (embeddinggemma modeli indirir — semantik arama)
  qmd search "..." -c docs         # veya: qmd query "..."
  ```
  qmd index/cache `~/.cache/qmd/` altındadır (türetilmiş veri, repoda değil).
- **Not:** Obsidian **masaüstü uygulaması** bu başsız/geçici sandbox'ta çalışmaz (GUI yok);
  vault dosyaları kullanıcının kendi Obsidian'ında açılır.

## Açık işler
- [ ] Yerel geliştirme: `local.properties`'e SECAL_SUPABASE_URL/ANON_KEY (CI variable'dan kopyala).
- [ ] Sunucu tarafı kararı: Supabase Edge vs Render Node (kapsam netleşince, ADR).
- [ ] Kullanıcı: Whenly'den alınan token'ları (render.key, SUPABASE_ACCESS_TOKEN, GITHUB_TOKEN) revoke et.
