---
type: architecture
date: 2026-06-15
tags: [siparis, odeme, escrow, marketplace, iyzico, togaf]
ai-first: true
---

# SeçAl — Sipariş & Ödeme Mimarisi

> Karar kaydı: [[mimari-karar-gunlugu#ADR-019]] · genişletir/sürdürür [[mimari-karar-gunlugu#ADR-007]]
> (PaymentGateway portu). Sipariş kod tarafı: Faz 5b (orders/order_items/place_order — ADR-018).

## For future Claude
SeçAl bir **pazaryeri**dir (iki taraf: alıcı + satıcı). Bu doküman, "parayı kim tutar?" sorusunun
**hem güven hem yasal** boyutunu çözer ve sipariş yaşam döngüsünü tanımlar. Kanonik karar burada;
çelişen eski metin supersede edilir.

---

## 1. Problem: ödeme güven ikilemi (kullanıcı tespiti)
Erken aşamada **iki yol da bozuk**:
- **(A) Parayı platform tutar (kendi escrow):** Satıcı, adı duyulmamış platforma güvenip parasını
  emanet etmez. **Ayrıca yasal değil:** Türkiye'de müşteri parasını tutmak/aracılık etmek **lisans**
  ister (6493 sayılı Kanun — **Ödeme/Elektronik Para Kuruluşu**, TCMB denetimi). Lisanssız escrow =
  suç riski. → **Kendi başına para TUTULAMAZ.**
- **(B) Para doğrudan satıcıya:** Alıcı korumasız; satıcı göndermezse alıcı parasını kaybeder →
  alıcı güveni çöker, iade/dispute kaosu, geri-ödeme (chargeback) yükü.

## 2. Çözüm: lisanslı PSP'nin "Pazaryeri" ürünü (escrow'u PSP yapar)
Sektör standardı: **parayı platform değil, lisanslı bir PSP tutar.**
- **iyzico Pazaryeri** (veya PayTR Marketplace / Stripe Connect muadili). Akış:
  1. Alıcı, **PSP'ye** öder (kart verisi platforma değmez — PCI SAQ-A).
  2. PSP parayı **escrow/bloke**de tutar (lisanslı kuruluş — yasal ve güvenilir).
  3. Teslimat onaylanınca (alıcı onayı **veya** X gün otomatik), PSP **satıcıya payout** yapar;
     **platform komisyonu** otomatik **split** edilir.
- Bunu çözer:
  - **Satıcı güveni:** parayı *tanınan lisanslı iyzico* tutuyor, "bilinmeyen startup" değil.
  - **Alıcı koruması:** teslimata kadar escrow.
  - **Yasal:** platform parayı hiç custody etmez → **lisans gerekmez** (aracı PSP lisanslı).
  - **Komisyon:** payout anında otomatik kesilir.
- Satıcılar **alt üye işyeri (sub-merchant)** olur (IBAN + KYC'yi PSP yürütür).

## 3. Faz planı (güven inşa et → para akışını aç)
- **Faz A — MVP (ŞİMDİ): Para akışı YOK.** **Kapıda Ödeme (COD)** veya "sipariş talebi".
  Alıcı kapıda öder, satıcı gönderir. Platform parayı **hiç görmez** → sıfır lisans, sıfır risk.
  Güven ve hacim inşa et. *(Mevcut `place_order` zaten tahsilat yapmıyor; sipariş `pending` → COD'a uygun.)*
- **Faz B — iyzico Pazaryeri:** sub-merchant onboarding + kart ile ödeme + escrow + komisyon split +
  teslim onayında payout. `PaymentGateway` portu arkasında `IyzicoMarketplaceAdapter`.
- **Faz C — Olgunluk:** iade/iptal/dispute akışı, otomatik payout zamanı (T+X), satıcı cüzdanı/rapor,
  fatura entegrasyonu.

## 4. Sipariş yaşam döngüsü (FSM)
```
            place_order
  (sepet) ─────────────► CREATED ('pending')
                            │  COD: satıcı kabul       │ Online: PSP ödeme OK
                            ▼                           ▼
                        CONFIRMED ◄───────────────── PAID (escrow @PSP)
                            │ satıcı hazırlar
                            ▼
                        PREPARING ──► SHIPPED ──► DELIVERED
                                                     │ alıcı onayı / T+X otomatik
                                                     ▼
                                                  COMPLETED  (PSP→satıcı payout, komisyon split)
   iptal/iade dalları: CANCELLED (gönderim öncesi) · REFUNDED (PSP geri ödeme)
```
- Mevcut `orders.status` check: pending/confirmed/shipped/delivered/cancelled → **preparing, completed,
  refunded, paid** eklenecek (Faz B migration). Durum geçişleri sunucu (RPC/Edge) ile zorlanır (istemci değil).

## 5. Hexagonal yapı (sağlayıcı-bağımsız — P7)
- **domain port `PaymentGateway`:** `startChekout(order) → PaymentIntent/redirect`, `confirm(ref)`,
  `refund(ref)`, `payoutOnDelivery(order)`. Domain hiçbir PSP'yi bilmez.
- **adapter'lar:** `CashOnDeliveryGateway` (Faz A — no-op tahsilat, COD işaretler) ·
  `IyzicoMarketplaceGateway` (Faz B). Seçim DI ile; **PSP sırrı yalnız sunucu/Edge** (anahtar istemcide yok).
- **Sunucu tarafı:** ödeme başlatma/doğrulama/webhook **Supabase Edge Function**'da (service_role +
  PSP secret server-side). İstemci yalnız redirect/sonuç alır. Kart verisi PSP-hosted (PCI SAQ-A).

## 6. Veri modeli (Faz B'de eklenecek)
- `orders`: + `payment_method` (cod|online), genişletilmiş `status`.
- `payments`: order_id, provider, provider_ref, amount_minor, escrow_status, created_at. (PII zon)
- `seller_payout_accounts`: seller_id, iban (maskeli), sub_merchant_id, kyc_status. (PII — şifreli/sunucu)
- `payouts`: order_id, seller_id, amount_minor, commission_minor, status, paid_at.
- `commissions`: oran/kural (kategori bazlı olabilir).
- RLS: alıcı kendi order/payment'ını; satıcı kendi payout'unu; finansal yazma yalnız Edge (service_role).

## 7. Komisyon
- Sipariş başına **platform komisyonu** (% — kategori bazlı ayarlanabilir). Faz B'de payout anında
  PSP split ile otomatik kesilir; Faz A'da komisyon yok (veya manuel/sonra), önce hacim.

## 8. Yasal/uyum notları (ISO 27001/27002 · KVKK)
- Platform **parayı custody etmez** → ödeme kuruluşu lisansı gerekmez (aracı PSP lisanslı).
- Kart verisi istemciye/sunucuya **değmez** (PSP tokenization, PCI SAQ-A).
- KYC/AML PSP tarafında (sub-merchant). KVKK: IBAN/KYC PII — şifreli, sunucu tarafı, minimum.
- Mesafeli satış sözleşmesi + iade hakkı (Tüketici Kanunu) Faz B'den önce hukuki gözden geçirme.

---
**Özet karar:** Faz A = **Kapıda Ödeme** (para akışı yok, güven inşa). Faz B = **iyzico Pazaryeri**
(escrow'u lisanslı PSP yapar; platform parayı tutmaz; komisyon split). Kendi escrow'umuz **asla** (yasal değil).

**Standartlar:** TOGAF Phase B(Business: ödeme yeteneği)+C(Data)+D(Application: PaymentGateway portu) ·
ISO 25010 Reliability/Security · 27002 (ödeme güvenliği, PCI, erişim) · 29148 (gereksinim izlenebilirliği) ·
P3 (Buy>Build: lisanslı PSP) · P5 (güvenlik) · P7 (tersine-çevrilebilir: port/adapter).
