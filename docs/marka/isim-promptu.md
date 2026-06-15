---
type: brand-naming-prompt
title: İsim Bulma Promptu (LLM brief)
date: 2026-06-15
tags: [marka, isim, prompt, faz-3]
ai-first: true
---

# İsim Bulma Promptu — ChatGPT/LLM için

## For future Claude
Kullanıcı aday isimleri beğenmedi; ismi harici bir LLM'e buldurmak istiyor. Bu, uygulamayı tam
tanıtan kopyala-yapıştır brief. Çıkan isim → ADR-014 + [[marka-kilavuzu]] + teknik rename.
İlgili: [[isim-adaylari]] · [[yol-haritasi]] (Faz 3) · [[North Star]].

## Prompt (aşağıdaki bloğu kopyala)

```
Sen kıdemli bir marka isimlendirme (brand naming) uzmanısın. Türkiye pazarına çıkacak bir
mobil pazaryeri uygulaması için akılda kalıcı, ayırt edici ve tescil edilebilir bir marka adı
bulmanı istiyorum.

## Ürün
Köy/doğal/yöresel ürünlerin, köy üreticisinden (ya da kooperatiften) doğrudan tüketiciye
ulaştığı iki taraflı bir mobil pazaryeri. Aracı yok; üreticiden alıcıya. Örnek ürünler: bal,
zeytin/zeytinyağı, peynir/süt ürünleri, kuruyemiş, bakliyat/tahıl, reçel/salça, baharat/bitki
çayı, el sanatları. Native Android + mobil web. Kalite/deneyim çıtası: Trendyol seviyesinde,
modern ama köklü/güven veren.

## Hedef kitle
- Alıcı: şehirde yaşayan, doğal/katkısız/otantik ürün arayan, üreticiye ve menşeine güvenmek
  isteyen tüketici.
- Satıcı: köy üreticisi, çiftçi, kadın kooperatifi, küçük yöresel işletme.

## Marka değerleri / hissi
Güven · doğallık/saflık · üreticiden doğrudan · yerel/yöresel/Anadolu · sıcaklık/samimiyet ·
tazelik · modern sadelik. Ton: sıcak, güvenilir, çağdaş ama topraktan beslenen.

## İstediğim isim kriterleri
1. Türkçe söyleyişe uygun, kolay yazılır/okunur (yabancı birine bile söyletilebilir).
2. Kısa ve akılda kalıcı (tercihen 2-3 hece).
3. Olumsuz/komik çağrışım yok (argo, kötü anlam taraması yapılmış).
4. AYIRT EDİCİ ve tescil edilebilir — salt jenerik kelime (ör. "köy", "doğal", "pazar) tek
   başına OLMASIN; özgün, sahiplenilebilir olsun.
5. Wordmark/logo ve app ikonunda iyi durmalı; kısa bir slogan ile eşlenebilmeli.
6. İdeal olarak .com / .com.tr veya .app domaini + sosyal medya handle'ı müsait olabilecek
   kadar özgün.

## Yöntem (lütfen hepsini dene)
- Türkçe kelime kökleri (köy, oba, yöre, harman, hasat, bereket, toprak, sofra, tezgah, çeşme,
  pınar, ambar, sepet, çıkrık...) ile yaratıcı türetmeler ve birleşik kelimeler.
- Uydurma/coined isimler (anlam çağrıştıran ama özgün), ekler (-ce, -den, -lı, -em),
  iki kelime birleşimi, kısaltma.
- Anadolu/yöresel kelimeler (anlamı kısa açıklanmak kaydıyla).

## Çıktı formatı
1. 15 aday: | İsim | Anlam/Mantık | Hece | Verdiği his | Olası risk (tescil/çağrışım) |
2. Ardından EN İYİ 5 kısa liste: her biri için neden öne çıktığı + 1 örnek slogan +
   önerilen domain varyantları (.com/.com.tr/.app + alternatif: get<isim>.com).
3. En sonda net 1 öneri ve gerekçesi.

Notlar: İngilizce ağırlıklı isimlerden kaçın. "SeçAl" çalışma adımız; daha güçlü bir
alternatif bulursan onu da değerlendir, bulamazsan bunu da finale koyabilirsin.
```

## Kullanım
1. Yukarıdaki bloğu ChatGPT'ye yapıştır.
2. Beğendiğin ismi bana getir → müsaitlik (domain/Play Store/handle) doğrularım,
   ADR-014 yazarım, marka kılavuzu + teknik rename yaparım.
