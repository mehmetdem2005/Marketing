---
type: brand-naming
title: İsim Adayları & Değerlendirme (Faz 3)
date: 2026-06-15
tags: [marka, isim, branding, faz-3]
ai-first: true
---

# İsim Adayları & Değerlendirme — Faz 3

## For future Claude
Köy/doğal/yerel ürün pazaryeri için isim adayları, kriter matrisi ve müsaitlik notları.
Karar kullanıcıya aittir (marka). Seçim sonrası: ADR (çalışma adı "SeçAl"i supersede et) +
[[marka-kilavuzu]] + teknik rename. İlgili: [[yol-haritasi]] Faz 3.

## Kriterler
Anlam (köy/doğal/üretici/güven) · Akılda kalıcılık & söyleyiş · Türkçe uyum · **Ayırt edicilik
& tescil edilebilirlik** (jenerik kelime = zayıf tescil) · Domain/handle müsaitliği.

> Not: `.com`'ların çoğu dolu (yaygın kelimeler). Domain marka kararını bağlamaz —
> `.app/.co/.store` veya önek (`get…`, `…app`) ile çözülür. DNS taraması kesin tescil değildir.

## Adaylar (kısa liste)
| İsim | Anlam | Güçlü yön | Zayıf yön | Domain (DNS) |
|---|---|---|---|---|
| **SeçAl** ⭐ | "köyden" — köyden gelen | Anlam birebir; kısa; mevcut marka birikimi | Jenerik → tescil zayıf | .com/.com.tr dolu → secal.app/.co |
| **Topraktan** | "topraktan gelen" | Doğal/organik, duygusal, premium his | 3 hece | dolu → topraktan.app/.co |
| **Obadan** | "obadan" (Anadolu yerleşimi) | Ayırt edici, Anadolu, tescile uygun, **.com.tr boş** | Anlamı herkese açık değil | obadan.com.tr boş olabilir |
| **Üreticiden** | "üreticiden" — aracısız | En net değer önerisi (doğrudan üretici) | Uzun (4 hece); jenerik | ureticiden.com.tr boş olabilir |
| **Yöreden** | "yöreden" — yöresel | Yöresel ürün çağrışımı; sıcak | Jenerik | dolu |

## Diğer havuz
Harman, Hasat (hasat/harman — güzel ama çok yaygın/tescil kalabalık), Köypazarı (açıklayıcı),
Yerelden, Köyce, Bereket (gıda olumlu ama çok yaygın marka).

## Öneri
- **Anlam + hız + mevcut birikim** istersek → **SeçAl** (en güçlü iletişim). Tescili marka+logo
  bütünüyle ("SeçAl" + özgün amblem) güçlendiririz.
- **Ayırt edicilik + müsait domain + tescil kolaylığı** istersek → **Obadan** (veya duygusal güç için **Topraktan**).

## Sonraki adım
Kullanıcı seçer → ADR-014 (isim kararı, "SeçAl" çalışma adını supersede/teyit) → [[marka-kilavuzu]]
(logo yönü, renk paleti = tasarım token revizyonu, tipografi, ton) → teknik rename (applicationId/
namespace/strings/paketler) → CI yeşil + WCAG kontrast doğrulama.
