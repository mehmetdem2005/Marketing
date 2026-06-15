# SeçAl — Canlıya Alma Rehberi (Android telefon + Supabase)

> Amaç: uygulamayı telefonunda **gerçek veriyle** görmek. Sıra önemli. ~20-30 dk.

## A) Supabase projesi aç (ücretsiz)
1. https://supabase.com → **Start your project** → GitHub ile giriş.
2. **New project**: ad `secal`, güçlü bir **Database Password** belirle (bir yere kaydet),
   region **Frankfurt (eu-central)** (Türkiye'ye yakın). **Create new project** (~2 dk kurulur).
3. Kurulunca: sol menü **Project Settings → API**. Buradan iki değeri kopyala:
   - **Project URL** (ör. `https://xxxx.supabase.co`)
   - **anon public** key (uzun `eyJ...` anahtar)
   - (Bunları bana vermene gerek yok — birazdan GitHub'a gireceksin.)

## B) Migration'ları uygula (veritabanı şeması)
Supabase'de sol menü **SQL Editor → New query**. `marketing-2` repodaki şu dosyaların içeriğini
**SIRAYLA** yapıştırıp her birinde **Run** de:
1. `supabase/migrations/0001_init.sql`  (profiller, adresler, trigger'lar)
2. `supabase/migrations/0002_catalog.sql` (mağaza/kategori/ürün + **kategoriler hazır gelir** + görsel bucket)
3. `supabase/migrations/0003_cart.sql` (sepet)

> Her Run "Success" demeli. Sıra atlanırsa hata verir (0002, 0001'in fonksiyonlarını kullanır).

## C) Kayıt/giriş kolaylığı (test için)
Sol menü **Authentication → Providers → Email** → **Confirm email** seçeneğini **KAPAT** → Save.
(Böylece kayıt olunca e-posta doğrulaması beklemezsin.)

## D) Anahtarları GitHub'a gir (APK'nın backend'e bağlanması için)
`Marketing` repo → **Settings → Secrets and variables → Actions → Variables** sekmesi →
**New repository variable** ile İKİ değişken ekle (isimler birebir):
- `SECAL_SUPABASE_URL` = (A'daki Project URL)
- `SECAL_SUPABASE_ANON_KEY` = (A'daki anon public key)

## E) APK üret ve telefona kur
1. `Marketing` repo → **Actions** → en son **Android CI** koşusunu aç → sağ üst **Re-run all jobs**
   (değişkenler artık dolu olduğu için APK backend'e bağlı derlenir). Yeşili bekle (~4-5 dk).
2. Koşu sayfasında aşağıda **Artifacts → `secal-debug-apk`** indir (zip). Aç → içindeki `.apk`.
3. APK'yı telefona aktar (kablo/Drive/Telegram). Telefonda aç → "bilinmeyen kaynak" iznini ver → **Yükle**.

## F) İçeriği gör (boş katalogu doldur)
Uygulamada: **Kayıt ol** → ana sayfa → **Satıcı paneli** → mağaza oluştur → **+ Ürün ekle**
(ad, fiyat, stok, kategori, görsel seç) → **Yayınla**. Sonra ana sayfa → **Ürünleri keşfet**:
eklediğin ürün katalogda görünür. Ürün → **Sepete ekle** → **Sepetim**.

---
**Sorun olursa:** APK açılıp giriş çalışmıyorsa → D'deki değişken adları yanlış olabilir veya E'de
re-run yapılmamıştır. Katalog boşsa → F'de ürün eklenmemiştir. Migration hatası → B'de sıra bozulmuştur.

**Standartlar:** TOGAF Phase F/G (geçiş/uygulama yönetimi) · ISO 27001 (anahtar yönetimi: anon key
GitHub Variables'ta, kodda değil) · 29148 (kurulum gereksinimleri) · operasyonel runbook disiplini.
