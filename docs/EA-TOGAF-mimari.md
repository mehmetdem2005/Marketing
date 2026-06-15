# SeçAl — Kurumsal Mimari (EA / TOGAF)

## 0. Doküman Kontrolü (ISO/IEC/IEEE 42010 — Architecture Description)
- **System-of-interest:** SeçAl — köy/doğal/yerel ürünler pazaryeri (native Android + Supabase).
- **Kapsam:** Mobil uygulama (Kotlin/Compose), backend (Supabase: Auth + Postgres/RLS + Storage + Edge), ödeme entegrasyonu (Stripe — yönlendirme, tahsilat kapalı).
- **Birincil mimar:** SeçAl çekirdek ekibi.
- **Durum:** Taslak · Faz 0 (Yönetişim & İskelet).
- **Sürüm notu:** İlk sürüm — ADM Preliminary→H iskeleti + Requirements Management kuruldu.
- **Standart sürümleri:** TOGAF Standard 10th Edition (2022) · ISO/IEC/IEEE 42010:2022 · ISO/IEC 25010:2023 · ISO/IEC 25012:2024 · ISO/IEC 27001:2022 + 27002:2022 · ISO/IEC/IEEE 29148:2018 · ISO 9241-110:2020.
- **Multi-repo:** `marketing` (bu repo — uygulama + docs) · `marketing-2` (Supabase backend/Edge). `mse-auto` yalnız okunur referans (doküman/standart disiplini); kod kopyalanmaz.

---

## 1. PRELIMINARY PHASE — Çerçeve ve Prensipler

### 1.1 Amaç
Köy üreticilerinin (mağaza) doğal/yöresel ürünlerini doğrudan tüketiciye ulaştırdığı,
güvenilir, erişilebilir ve ölçeklenebilir bir pazaryeri kurmak.

### 1.2 Kapsam (Scope)
Kayıt/kimlik (alıcı + satıcı), mağaza & ürün yönetimi, katalog/keşif/arama, sepet,
sipariş & durum takibi, ödeme yönlendirme (tahsilat sonraki faz), değerlendirme/yorum,
bildirim. Kapsam dışı (şimdilik): gerçek tahsilat, kargo entegrasyonu, çok-dillilik.

### 1.3 Mimari Prensipler (P1–P9)

**P1 — Veri Minimizasyonu & PII-Sınırı**
- *Statement:* Yalnızca gerekli kişisel veri toplanır; PII (profil, adres, sipariş iletişimi) RLS ile izole edilir, üçüncü taraf hizmetlere gönderilmez.
- *Rationale:* KVKK/GDPR uyumu, gizlilik-by-design, saldırı yüzeyini küçültür.
- *Implications:* PII zonu ayrı; her veri akışı sınırı işaretler; ihlal = governance reddi. (Bkz. ADR-006.)

**P2 — Pazaryeri Güveni (Trust & Safety)**
- *Statement:* Satıcı kimliği, ürün bütünlüğü ve değerlendirme dürüstlüğü korunur (yalnız teslim edilmiş siparişe yorum).
- *Rationale:* İki-taraflı pazaryerinde güven = dönüşüm.
- *Implications:* Doğrulama durumları; yorum→sipariş bağı; raporlama mekanizması.

**P3 — Buy > Build (Sağ kaynak)**
- *Statement:* Commodity yetenekler (kimlik, depolama, DB, realtime) Supabase ile yönetilir; yalnız pazaryeri çekirdek domaini inşa edilir.
- *Rationale:* Hız + bakım maliyeti + güvenlik.
- *Implications:* Backend = Supabase; özel sunucu yok. (Bkz. ADR-002.)

**P4 — Sözleşme & Tip Güvenliği**
- *Statement:* Katman sınırları tiplerle korunur; domain port'ları + DTO/serialization tek kaynaktan türer; Supabase şeması ↔ Kotlin DTO senkron.
- *Rationale:* Regresyon önleme, refactor güvenliği.
- *Implications:* `:core:domain` saf port'lar; data adapter mapper'ları.

**P5 — Güvenlik-Öncelikli (Secure by Design)**
- *Statement:* RLS deny-by-default; sır kodda yok; token güvenli depoda; yalnız HTTPS; OWASP MASVS gözetilir.
- *Rationale:* Pazaryeri = finansal/kişisel veri; ihlal maliyeti yüksek.
- *Implications:* Üç katmanlı kapı (hook/CI/Stop) + RLS matrisi + tehdit modeli. (Bkz. docs/guvenlik.md, ADR-005.)

**P6 — Mobil/Offline Dayanıklılık**
- *Statement:* Kötü ağ ve offline durumları graceful; her veri ekranı 4-durum (loading/empty/error/content).
- *Rationale:* Kırsal kullanıcı tabanı, değişken bağlantı.
- *Implications:* Önbellek + yeniden deneme + net hata mesajı.

**P7 — Tersine Çevrilebilirlik (Escape Hatches)**
- *Statement:* Tek-yönlü kapı açılmaz; ödeme/sağlayıcı bağımlılıkları port arkasında (Stripe değiştirilebilir).
- *Rationale:* Sağlayıcı kilidi riskini azaltır.
- *Implications:* `PaymentGateway` portu; Supabase erişimi repo arkasında.

**P8 — Ölçülebilir Kalite (ISO 25010)**
- *Statement:* Her anlamlı özellik için ilgili kalite karakteristiği + ölçülebilir NFR tanımlanır.
- *Rationale:* "Bitti" tanımı nesnel olur.
- *Implications:* NFR tablosu (§10.1); conformance kontrolü.

**P9 — Sağ-Boyut (Right-sizing)**
- *Statement:* Ölçeğe uygun ceremony; gereksiz soyutlama/araç eklenmez.
- *Rationale:* Hız ve sadelik.
- *Implications:* Convention plugin/buildSrc gibi yatırımlar gerektiğinde, gerekçeyle.

### 1.4 TOGAF Framework Tailoring
ADM Preliminary→H uygulanır, **küçük-ekip/tek-ürün ölçeğine indirgenir**: ağır kurumsal
governance artefaktları minimal; ADR + EA + Requirements Management çekirdek tutulur.

### 1.5 Architecture Governance
Kararlar ADR ile kayıt altına alınır (`docs/mimari-karar-gunlugu.md`). Uyum: üç katmanlı
kalite kapısı (yerel hook + CI + Stop hook). Sapma (dispensation) gerekçeyle ADR'ye yazılır.

### 1.6 Paydaşlar ve Concern'ler (light)
Alıcı (kolay keşif/güven), Satıcı (kolay ürün yönetimi/satış), Platform sahibi
(güven/ölçek/maliyet), Düzenleyici (KVKK/GDPR).

### 1.7 Preliminary — Doğrulama
Prensipler tanımlandı (P1–P9), tailoring beyan edildi, governance mekanizması kuruldu.
Sonraki: Phase A — Architecture Vision.

---

## 2. PHASE A — Architecture Vision

### 2.1 Statement of Architecture Work
Native Android pazaryeri + Supabase backend; Faz 0–7 yol haritası (bkz. plan). Çıktı:
çalışan, güvenli, erişilebilir, ölçeklenebilir uygulama.

### 2.2 Architecture Vision (Target)
İki-taraflı pazaryeri: satıcı onboarding → ürün/katalog → alıcı keşif/sepet/sipariş →
ödeme yönlendirme → değerlendirme. Supabase RLS ile güvenli, Compose ile erişilebilir UI.

### 2.3 Paydaş–Concern–Viewpoint Matrisi (42010)
| Paydaş | Concern | Ele alan görünüm |
|---|---|---|
| Alıcı | Keşif, güven, hız | Application (feature'lar) · Interaction (9241) |
| Satıcı | Ürün yönetimi, satış | Application (seller) · Data (stores/products) |
| Platform | Güvenlik, maliyet, ölçek | Technology · Data (RLS) · Governance |
| Düzenleyici | KVKK/GDPR | Data (PII zon) · Security (27002) |

### 2.4 İş Senaryosu
Köy üreticisi mağaza açar, ürün ekler; alıcı kategoriden bulur, sepete ekler, sipariş
verir; satıcı durumu günceller; alıcı teslim sonrası yorum yapar.

### 2.5 Yetenek Değerlendirmesi
Kimlik, katalog, sipariş, ödeme-yönlendirme, değerlendirme, bildirim, arama.

### 2.6 İlk Risk Değerlendirmesi
| Risk | Etki | Azaltım | Faz |
|---|---|---|---|
| RLS yanlış yapılandırma → veri sızıntısı | Yüksek | RLS matrisi + test + deny-by-default | 0–5 |
| Sağlayıcı kilidi (Supabase) | Orta | Repo/port soyutlaması | 1 |
| Ödeme uyumu | Yüksek | Tahsilat ertelendi, port arkasında | 5 |
| Güven kötüye kullanımı (sahte ürün/yorum) | Orta | Doğrulama + yorum-sipariş bağı | 3,6 |
| Zayıf ağ | Orta | 4-durum + önbellek | 1+ |

### 2.7 Mimari Kabul Kriterleri / KPI
Build yeşil (detekt/lint/test/assemble) · RLS testleri geçer · a11y AA · cold start hedefi.

### 2.8 Phase A — Doğrulama
Vizyon, paydaş matrisi, risk ve kabul kriterleri tanımlandı. Sonraki: Phase B.

---

## 3. PHASE B — Business Architecture

### 3.1 İş Modeli
İki-taraflı pazaryeri; gelir modeli (komisyon) sonraki fazda, tahsilat ertelendi.

### 3.2 İş Kapasite Haritası
Kimlik & Hesap · Mağaza Yönetimi · Ürün/Katalog · Keşif & Arama · Sepet & Sipariş ·
Ödeme · Değerlendirme & Güven · Bildirim.

### 3.3 Aktörler / Roller
Misafir (anon) · Alıcı (authenticated) · Satıcı (store_member) · Admin.

### 3.4 Temel İş Süreçleri
Satıcı onboarding · Ürün yayınlama · Alıcı sipariş · Sipariş yaşam döngüsü (FSM) · Yorum.

### 3.5 Phase B — Doğrulama
Kapasite, aktör, süreç tanımlı. Sonraki: Phase C.

---

## 4. PHASE C — Information Systems Architecture

### 4.1 Data Architecture

#### 4.1.1 Mantıksal Veri Entity'leri
| Entity | Açıklama | Zon |
|---|---|---|
| profiles | Kullanıcı profili (rol: buyer/seller/admin) | PII |
| addresses | Teslimat adresleri | PII |
| stores | Mağaza (satıcıya bağlı) | Paylaşılan (public alanlar) |
| store_members | Mağaza–kullanıcı rol bağı | Karma |
| categories | Kategori ağacı (parent_id) | Paylaşılan |
| products | Ürün | Paylaşılan |
| product_variants | Varyant (gramaj/paket) | Paylaşılan |
| product_images | Ürün görselleri (Storage referansı) | Paylaşılan |
| inventory | Stok | Paylaşılan |
| favorites | Alıcı favorileri | PII (kullanıcıya özel) |
| carts / cart_items | Sepet | PII (kullanıcıya özel) |
| orders | Sipariş (iletişim/teslimat) | PII |
| order_items | Sipariş kalemleri | PII |
| order_status_history | Durum geçmişi (FSM) | Karma |
| reviews | Değerlendirme/yorum | Paylaşılan (yazar bağı) |
| notifications | Bildirimler | PII |
| payment_intents | Ödeme niyeti (Stripe ref) | PII |

#### 4.1.2 PII Sınıflandırma & Zon
- **PII zonu:** profiles, addresses, favorites, carts, orders, order_items, notifications, payment_intents.
- **Paylaşılan zon:** categories, products, variants, images, stores (public alanlar), reviews (gövde).
- İlke: PII satırları yalnız sahibi + yetkili rol görür; paylaşılan veri anon okunabilir.

#### 4.1.3 RLS Stratejisi (Supabase)
Her tabloda RLS **etkin** ve **deny-by-default**. Politikalar rol bazlı: anon (public read),
authenticated buyer (kendi PII + public), seller (kendi mağaza/ürün/sipariş kalemleri), admin.
Atomik işlemler `SECURITY DEFINER` RPC ile (örn. `create_order`). Detay matris: `docs/guvenlik.md`.

### 4.2 Application Architecture

#### 4.2.1 Uygulama Bileşenleri (modüller)
`:app`, `:core:common|domain|data|designsystem|ui`, `:feature:auth|home|catalog|cart|order|seller|profile|reviews`.

#### 4.2.2 Katmanlar
UI (Compose/feature) → ViewModel (UiState/FSM) → UseCase (domain) → Repository port (domain)
→ Repository impl (data, supabase-kt) → Supabase. Tek-yönlü bağımlılık (Clean Architecture).

### 4.3 Phase C — Doğrulama
Veri entity'leri, PII zonu, RLS stratejisi ve uygulama katmanları tanımlandı. Sonraki: Phase D.

---

## 5. PHASE D — Technology Architecture

### 5.1 Teknoloji Stack
- **Mobil:** Kotlin 2.1, Jetpack Compose (Material 3), Hilt, Coroutines/Flow, Navigation-Compose, Coil, DataStore + EncryptedSharedPreferences/Keystore.
- **Backend:** Supabase (Postgres + RLS, GoTrue auth, Storage, Realtime, Edge Functions/Deno).
- **Ödeme:** Stripe (yönlendirme; tahsilat kapalı).
- **Build/Kalite:** Gradle (Kotlin DSL + version catalog), detekt + formatting, Android Lint, gitleaks, lefthook, GitHub Actions.

### 5.2 Deployment Topolojisi
Android APK/AAB (Play) ← CI (GitHub Actions). Supabase yönetilen bulut (ayrı repo migration'ları).
Edge Functions Supabase'de. Sırlar: CI secret + Supabase env; repoda yok.

### 5.3 Phase D — Doğrulama
Stack ve topoloji tanımlı. Sonraki: Phase E–F (fırsat/planlama → plan dosyası yol haritası).

---

## 6. PHASE E — Opportunities & Solutions
Çözüm faz faz teslim edilir (Faz 0–7, plan dosyası). Her faz bağımsız doğrulanabilir artımdır.

## 7. PHASE F — Migration Planning
Faz sırası: 0 Yönetişim/İskelet → 1 Tasarım Sistemi/Çekirdek → 2 Kimlik → 3 Mağaza/Katalog
→ 4 Keşif/Alışveriş → 5 Sipariş/Ödeme → 6 Etkileşim/Güven → 7 Sertleştirme/Sürüm.

---

## 8. PHASE G — Implementation Governance

### 8.1 Mimari Uyum Süreci
Her PR: detekt + lint + test + assemble (CI) yeşil olmalı; güvenlik-dokunuşu → /security-review.

### 8.2 Prensip Conformance Checklist (her feature/PR'da)
| Prensip | Kontrol sorusu |
|---|---|
| P1 PII/Minimizasyon | Gereksiz PII toplanıyor mu? Dış hatta PII gidiyor mu? → hayır |
| P2 Güven | Doğrulama/yorum-sipariş bağı korunuyor mu? |
| P3 Buy>Build | Commodity yetenek Supabase ile mi? |
| P4 Sözleşme | Sınır tipten mi türüyor? DTO↔şema senkron mu? |
| P5 Güvenlik | RLS doğru mu? Kodda sır var mı? Token güvenli mi? |
| P6 Mobil/Offline | 4-durum + kötü ağ graceful mı? |
| P7 Tersine-çevrilebilir | Sağlayıcı port arkasında mı? Kaçış kapısı var mı? |
| P8 25010 | İlgili kalite + ölçülebilir NFR tanımlı mı? |
| P9 Sağ-boyut | Gereksiz ceremony ekleniyor mu? |

### 8.3 Kalite Kapıları (CI/CD)
Üç katman: (1) yerel hook (lefthook: gitleaks + detekt + test), (2) CI (gitleaks + detekt +
lint + test + assemble), (3) Stop hook (gitleaks + mümkünse detekt + inceleme kapısı).

### 8.4 Standart Conformance (25010 / 27002)
NFR tablosu (§10.1) + güvenlik kontrol checklist (§10.2) ile izlenir.

### 8.5 ADR Governance
Her anlamlı karar ADR. Çelişen kararlar "supersedes" ile uzlaştırılır.

---

## 9. PHASE H — Architecture Change Management

### 9.1 Değişiklik Tetikleyicileri
Yeni gereksinim, güvenlik bulgusu, ölçek baskısı, sağlayıcı değişimi.

### 9.2 Değişiklik Sınıflandırma (TOGAF)
| Sınıf | Süreç |
|---|---|
| Basitleştirme | Change-mgmt (ADR gerekmez) |
| Artımlı | İlgili faz(lar) + ADR |
| Yeniden-Mimari | Tam ADM döngüsü + ADR |
| Düzeltici | Hata düzeltme + (gerekirse) ADR notu |

### 9.3 Sürüm Yönetimi
SemVer (versionName). DB migration'ları yalnız açık izinle canlıya uygulanır.

---

## 10. Requirements Management (MERKEZ)

### 10.1 ISO/IEC 25010 — Kalite Modeli (SeçAl NFR'leri)
| Karakteristik | Hedef | Ölçüt |
|---|---|---|
| İşlevsel Uygunluk | Yüksek | Kabul kriterleri / use-case kapsama |
| Performans Verimliliği | Yüksek | Cold start < 2.5s, liste kaydırma jank-sız |
| Güvenilirlik | Yüksek | Hata oranı düşük; graceful timeout/retry |
| Etkileşim Yeteneği | Yüksek | WCAG 2.2 AA / 9241; 4-durum |
| Güvenlik | Yüksek | RLS test, sır taraması temiz, MASVS |
| Bakım Yapılabilirlik | Yüksek | Modülerlik, detekt temiz, test |
| Uyumluluk | Orta | minSdk 24+, çoklu cihaz |
| Esneklik | Orta | Port soyutlaması (ödeme/sağlayıcı) |
| Güvenlik (Safety) | Uyarlı | Veri bütünlüğü (sipariş/stok tutarlılığı) |

### 10.2 ISO/IEC 27002 — Güvenlik Kontrol Checklist (özet)
Erişim kontrolü (RLS/JWT) · Kriptografi (HTTPS, Keystore) · Güvenli geliştirme (gitleaks/detekt/review) ·
Loglama/izleme (Faz 7) · Veri minimizasyonu · Tedarikçi güvenliği (Supabase/Stripe) ·
Sınıflandırma (PII zon). Sertifikasyon kapsam dışı (tailoring); kontrol-adopsiyonu ölçülür.

### 10.3 Gereksinim İzlenebilirliği (29148)
Prensip → Gereksinim → Faz → Test/Kabul. Örn: P5 → "RLS deny-by-default" → Faz 2–5 →
RLS politikası testleri + gitleaks (kabul).

### 10.4 Doğrulama
Kalite modeli, güvenlik kontrolleri ve izlenebilirlik tanımlandı.

---

**Standartlar:** TOGAF Phase Prelim–H + Requirements Mgmt iskeleti · ISO 42010 (doküman/viewpoint) ·
25010 (NFR) · 25012 (veri) · 27001/27002 (güvenlik) · 29148 (izlenebilirlik) · 9241 (etkileşim) ·
Faz 0 kapsamında **iskelet + prensipler** kuruldu (içerik fazlarla derinleşecek — abartma yok). · ADR-001..008.
