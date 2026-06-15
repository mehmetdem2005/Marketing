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
- **Proje adı (Supabase'de):** `koyden` (eski kod-adı; ref değişmedi — yeniden adlandırma opsiyonel).
- **Plan:** free · **Durum:** ACTIVE_HEALTHY · **Auth:** `mailer_autoconfirm=true` (e-posta doğrulama KAPALI, test için).
- **Şema (2026-06-15):** `0001_init` (profiles/addresses + RLS + handle_new_user) · `0002_catalog`
  (stores/categories/products/product_images + kategori seed + `product-images` Storage bucket public) ·
  `0003_cart` (cart_items PII/RLS + `add_to_cart` RPC). Hepsi Management API ile uygulandı/doğrulandı.
- **Anahtarlar (anon / service_role / db_pass):** repoda DEĞİL → `/home/user/.secrets/` (oturumluk).
  **Build için:** `SECAL_SUPABASE_URL` + `SECAL_SUPABASE_ANON_KEY` artık `gradle.properties`'te
  (anon PUBLIC istemci kimliği — gitleaks allowlist). service_role yalnız sunucu/Edge tarafında.

## Render
- **Hesap:** My Workspace (`tea-d74hju14tr6s73cpbucg`). Servisler (2026-06-15): tek servis
  **`watcher-backend`** (`srv-d8jl19ek1jcs73ds9gng`) — **Watcher/mse-auto**'ya ait, SeçAl ile İLGİSİZ.
- **SeçAl için Render GEREKMİYOR:** native Android + doğrudan Supabase (ADR-002) → koşacak sunucu yok.
  Kullanıcı "Render'da oluştur" dedi → değerlendirildi, **açılmadı** (içinde çalışacak kod yok; anlamsız).
  SeçAl anahtarları watcher-backend env'ine **konmaz** (başka projeyi kirletir). İleride sunucu ihtiyacı =
  Supabase Edge Functions önce; ayrı Render servisi ADR ile.
- **render.key:** `/home/user/.secrets/render.key` (repo-dışı). Kalıcı saklama = kullanıcının parola
  yöneticisi (container efemeral — oturum bitince `.secrets/` uçar; git'e ASLA yazılmaz).

## Sır yönetimi (ISO 27001/27002 · ADR-005)
- Erişim token'ları (Whenly'den, kullanıcı izniyle): `render.key`, `SUPABASE_ACCESS_TOKEN`,
  `GITHUB_TOKEN` → yalnız `/home/user/.secrets/` (repo dışı). **Kullanıcı işi bitince revoke edecek.**
- Whenly'nin diğer prod sırları indirilmedi/silindi (gereksiz yayılım yok). Whenly kaynaklarına
  yalnız **okuma** yapıldı; hiçbir Whenly kaynağı değiştirilmedi.
- Kural: hiçbir sır **değeri** git'e girmez (gitleaks kapısı + bu disiplin).

## Durum (2026-06-15)
- **CI YEŞİL** ✅ (`android-ci.yml`, commit `bb338ab`): gitleaks + detekt + lint + test + assembleDebug.
  Fazlar: 1 (tasarım+Auth) · 2 (Profil+Katalog) · 4 (Satıcı) · **5a (Sepet)** + marka **SeçAl** rename.
- **APK indirilebilir:** CI artifact **`secal-debug-apk`** (workflow'a upload adımı eklendi) →
  telefona sideload. APK creds gömülü (gradle.properties) → canlı Supabase'e bağlı.
- **Build creds yolu (SUPERSEDES eski "GitHub Actions variable"):** repo araçlarıyla Actions
  *variable* set edilemediğinden URL+anon `gradle.properties`'e gömüldü (anon public). service_role
  yalnız `.secrets`. build.gradle `.trim()` uygular (newline → "unclosed string literal" geçmiş hatası).
- **Uçtan uca çalışır:** kayıt/giriş (autoconfirm) → satıcı: mağaza+ürün+görsel(Storage) → katalog →
  sepete ekle (RPC) → sepet. (Sipariş/checkout = Faz 5b, sırada.)

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
