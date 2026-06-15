---
type: compliance
date: 2026-06-15
tags: [lisans, uyum, hukuk, kvkk, gida, odeme, etbis]
ai-first: true
---

# SeçAl — Lisans & Uyum Rehberi (Türkiye)

> ⚠️ **Hukuki tavsiye değildir.** Yön gösterir. **Avukat + mali müşavir** ile teyit edilmeli
> (özellikle ödeme, KVKK, gıda). Karar bağlamı: [[siparis-odeme-mimarisi]] · [[mimari-karar-gunlugu#ADR-019]].

## For future Claude
SeçAl bir **pazaryeri** (ETAHS) ve **gıda/doğal ürün** satıyor → iki ayrı uyum ekseni: (1) e-ticaret/şirket,
(2) gıda. Para tutulmadığı için ödeme lisansı **bizde değil** (PSP'de). Aşamalı liste:

---

## A) HEMEN / baştan gerekenler (düşük maliyet)
1. **Şirket kuruluşu:** Şahıs ya da **Limited/Anonim**. Pazaryeri için Ltd/AŞ önerilir (sorumluluk, yatırım).
2. **Vergi:** vergi mükellefiyeti + vergi levhası; **mali müşavir**; gerekiyorsa **e-Fatura/e-Arşiv**.
3. **Ticaret sicil + oda kaydı.**
4. **ETBİS kaydı** (Elektronik Ticaret Bilgi Sistemi, Ticaret Bakanlığı) — **e-ticaret yapan herkese ZORUNLU**.
5. **Yasal metinler (sitede/app'te):** Mesafeli Satış Sözleşmesi, Ön Bilgilendirme Formu, Kullanım Koşulları,
   **Satıcı Aracılık Sözleşmesi**, KVKK Aydınlatma + Çerez/İzin, İade & Cayma politikası.
6. **Marka tescili (TÜRKPATENT):** "SeçAl" markasını koru (önerilir, zorunlu değil ama kritik).

## B) ÖDEME (en çok sorduğun) — lisans BİZDE DEĞİL
- **6493 sayılı Kanun (TCMB):** müşteri parası tutmak/escrow = **Ödeme/Elektronik Para Kuruluşu lisansı**
  (yüksek sermaye + denetim). **Kendi başına yapma.**
- **Çözüm:** lisanslı **PSP** kullan (**iyzico Pazaryeri** vb.). Parayı PSP tutar → **bizde lisans gerekmez.**
  - Bizim gereken: PSP ile **üye işyeri sözleşmesi**; satıcılar **alt üye işyeri (sub-merchant)** — KYC/IBAN PSP'de.
- **Faz A (COD/kapıda ödeme):** para akışı yok → ödeme tarafında hiçbir lisans gerekmez.

## C) VERİ — KVKK
- **VERBİS kaydı** (Veri Sorumluları Sicili): çalışan sayısı / yıllık ciro / veri hacmi eşiğine göre
  zorunlu olabilir → büyürken gerekli. Kişisel veri işlendiği için yüksek olasılık.
- KVKK: aydınlatma, açık rıza, veri güvenliği (zaten RLS/şifreleme var — ADR-005/006), saklama/imha politikası.

## D) ÜRÜNE ÖZEL — GIDA (KRİTİK, çoğu kişi atlıyor)
SeçAl bal/peynir/zeytinyağı/reçel... satıyor → **5996 sayılı Veteriner Hizmetleri ve Gıda Kanunu**:
- **Satıcılar (üreticiler)** için **İşletme Kayıt veya Onay Belgesi** (Tarım ve Orman Bakanlığı) gerekir;
  bal/süt/et gibi hayvansal ürünlerde **onay**, çoğu bitkiselde **kayıt**.
- **Etiketleme** (içindekiler, son tüketim, üretici bilgisi, parti no), hijyen, soğuk zincir (gerekiyorsa).
- **Platform sorumluluğu:** satıcıdan bu belgeleri **istemek/doğrulamak** (onboarding'de zorunlu alan),
  belgesiz gıda satışına izin vermemek. (Pazaryerinin sorumluluğu artıyor — ETK değişiklikleri.)
- **Öneri:** Satıcı kaydında "gıda işletme kayıt/onay belge no" + belge yükleme alanı (Faz: satıcı doğrulama).

## E) BÜYÜYÜNCE / İLERİDE
- **6563 ETK (pazaryeri değişiklikleri, 2022/2023):** net işlem hacmi eşiklerine göre ek yükümlülükler;
  çok büyük platformlarda (örn. **10 milyar TL+** net işlem hacmi) **Elektronik Ticaret Lisansı** (Ticaret Bakanlığı)
  + ek kısıtlar. **Startup için geçerli değil**, ama ölçeklenince gündeme gelir.
- **e-Fatura/e-Arşiv** ciro eşiği aşılınca zorunlu.
- **Reklam/indirim** kuralları (Reklam Kurulu), **rekabet** (büyük ölçekte).
- Kargo/lojistik sözleşmeleri; iade lojistiği.

## Öncelik sırası (pratik)
1. (Şimdi) Şirket + vergi + **ETBİS** + yasal metinler + (COD ile) ödeme-lisanssız çalış.
2. Satıcı onboarding'e **gıda belge no** alanı (gıda uyumu).
3. Ödeme açılınca **iyzico Pazaryeri** sözleşmesi (lisans PSP'de).
4. Büyürken **VERBİS**, marka tescili, e-Fatura.
5. Ölçeklenince ETK eşik yükümlülükleri.

## F-2) Satıcı tipi: ESNAF (üretici değil) — gıda tarafı basitleşir
Satıcılar **kahveci, turşucu, kuruyemişçi, aktar** = perakende gıda **esnafı** (üretici değil).
- Bunlar zaten **dükkân sahibi** → büyük olasılıkla **İşletme Kayıt Belgesi + belediye ruhsatı** mevcut.
  Platformun işi: bu mevcut belgeyi **kayıt sırasında istemek** (yeni belge çıkarttırmak değil).
- **İstisna:** kendi yaptıkları ürünü satan (ev yapımı turşu/kavurma vb.) varsa o ürün için üretim
  kaydı gerekir — ama paketli ürün **yeniden satışı** standart perakendedir.
- **Platform sorumluluğu** (ETK): belgesiz/etiketsiz gıda satışına izin verme → satıcı onboarding'de
  "İşletme Kayıt Belge No" zorunlu alan + belge yükleme. (Maliyet satıcıda, zaten varlar.)

## G-2) MALİYET TAHMİNİ (2026, kaba — enflasyonla değişir, teyit al)
**Tek seferlik (başlangıç):**
| Kalem | Tahmini (TL) | Not |
|---|---|---|
| Limited şirket kuruluş | 10.000–25.000 | Şahıs şirketi ~2.000–5.000 (pazaryeri için Ltd önerilir) |
| Yasal metinler (sözleşme+KVKK+mesafeli) | 15.000–50.000 | Avukat; şablon+kontrol daha ucuz |
| Marka tescili (TÜRKPATENT) | 5.000–15.000 | Vekil + 1 sınıf resmi ücret |
| **ETBİS kaydı** | **0** | e-Devlet, ücretsiz |
| KVKK/VERBİS kaydı | 0 | Kayıt ücretsiz; danışman ist-bağlı 10–30k |
| **Başlangıç toplamı** | **~30.000–90.000** | Marka ertelenebilir → alt sınır düşer |

**Süreklilik (aylık/yıllık):**
- Mali müşavir: ~3.000–8.000 TL/ay (hacme göre)
- iyzico komisyon: işlem başına ~%2.5–4 + sabit ücret → **cebinden çıkmaz, satıştan kesilir** (kuruluş ücreti ~0)
- Sunucu/Supabase/Render: başta ücretsiz tier → büyüyünce ~birkaç bin TL/ay

> **Faz A (kapıda ödeme) ile** ödeme komisyonu bile yok; gerçek başlangıç maliyeti **şirket + ETBİS + sözleşmeler** ≈ **30–90k TL** + mali müşavir.

## H-2) "Kendi ödeme lisansın olsa" senaryosu (iyzico yerine)
Avantaj: parayı kendin tutar/escrow yaparsın, PSP komisyonu vermezsin, akışa tam hâkimsin.
**Ama bariyer çok yüksek (6493 sK, TCMB):**
- **Ödeme Kuruluşu** asgari sermaye: **~milyonlarca TL** (yönetmelikle, enflasyonla güncellenir;
  ödeme kuruluşu için on milyon TL mertebesi, **elektronik para kuruluşu** daha da yüksek).
- + bağımsız denetim, uyum/risk personeli, bilgi güvenliği denetimi (TS/ISO), TCMB başvuru süreci **aylar–yıl**.
- + sürekli raporlama/denetim yükü.
→ **Erken aşamada gerçekçi değil.** Doğru sıra: **iyzico ile başla → ciddi hacme ulaşınca** kendi lisansını
(veya lisanslı bir kuruluşla ortaklık/satın alma) **stratejik** olarak değerlendir. (Trendyol/Hepsiburada
büyüdükten sonra kendi ödeme şirketlerini kurdu — aynı yol.)

---
**Standartlar:** ISO 27001/27002 (KVKK/veri) · 29148 (uyum gereksinim izlenebilirliği) · TOGAF Phase B
(iş/uyum kısıtları) · P5 (güvenlik/uyum). **Hukuki teyit zorunlu (avukat + mali müşavir).** Rakamlar tahmindir.
