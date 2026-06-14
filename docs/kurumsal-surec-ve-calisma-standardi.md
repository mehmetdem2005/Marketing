# Köyden — Kurumsal Süreç & Çalışma Standardı (Plan)

> **Amaç:** Tek geliştirici (solo) çalışsak bile **ekibe/kuruma uygun**, ölçeklenebilir,
> güvenli, denetlenebilir bir yazılım üretmek. Bu dosya her oturum başında (SessionStart hook)
> bağlama enjekte edilir; **her işte bu standartlara uyulur**.
> Durum işaretleri: ✅ Uygulanıyor · 🟡 Planlı (faz) · ⚪ Kapsam dışı (gerekçeli).
> Kaynak: kurumsal yazılım süreci derlemesi → Köyden (Android + Supabase, multi-repo) uyarlaması.

## 0. HER OTURUMDA UY (hızlı checklist)
1. İşe başlamadan: ilgili skill (koyden routing) + bilgi tabanı (docs/EA, ADR) tara.
2. Değişiklik sınıfını belirle (Basitleştirme / Artımlı / Yeniden-mimari) + TOGAF ADM taraması.
3. Mimari sınır: domain saf, bağımlılık içe; sır kodda yok; RLS deny-by-default.
4. Üret + **dört göz yerine self-review checklist** (aşağıda §3) + testi düşün.
5. Anlamlı karar → ADR + EA güncelle (çelişki → supersedes). Sohbet kalıcı hafıza değil.
6. UI işinde dörtlü: design-standards + web-design-advanced + ui-ux-advanced + motion-design.
7. İş sonunda: "**Standartlar**" dipnotu (TOGAF faz/sınıf + ISO + fiilen uygulananlar). ABARTMA YOK.
8. Sır taraması (gitleaks) + lint (detekt/biome a11y) temiz olmadan tur bitmez (Stop kapısı).

---

## 1. Yönetişim & Metodoloji
- **Metodoloji:** Solo Kanban (faz-bazlı yol haritası Faz 0–7). SAFe/PI ⚪ (tek kişi) ama
  **faz planı = PI disiplini**. ✅
- **İş takibi:** GitHub Issues/PR (Jira eşdeğeri). 🟡 issue şablonları.
- **Karar mekanizması:** ARB/CAB yerine **ADR + self-review kapısı**: her anlamlı karar
  `docs/mimari-karar-gunlugu.md`'ye yazılır; büyük/yapısal değişiklik öncesi EA-ADM taraması. ✅

## 2. Mimari Standartlar
- **Clean Architecture + MVVM (UDF/FSM):** bağımlılık içe; domain framework'süz (port). ✅ (ADR-004)
- **DDD-lite:** bounded context = Gradle modülleri (auth/catalog/cart/order/seller/...). ✅
- **API-first / kontrat:** Supabase şeması + RLS + (marketing-2) Edge sözleşmeleri; istemci
  zod/`@watcher`-eşdeğeri kontratları. 🟡 OpenAPI/Postgrest şema sürümleme.
- **Mikroservis/Event-driven (Kafka/RabbitMQ):** ⚪ tek-app + Supabase; gerekirse Edge Functions
  + Postgres LISTEN/NOTIFY ile sınırlı event. Gerçek mikroservis kapsam dışı (ölçek gelmeden).
- **Geriye dönük uyumluluk:** API/şema değişimi eski istemciyi kırmaz (additive + versioning). ✅ ilke

## 3. Kod Kalitesi (Dört Göz → Solo Disiplin)
- **Statik analiz:** detekt (a11y/kompleksite) zorunlu; SonarQube ⚪ (kurulursa 🟡). Coverage hedefi
  kritik modüllerde ≥%70 (kurumsal %80 hedefe yaklaşım). 🟡
- **Peer review:** tek kişi → **PR + zorunlu self-review checklist** (mimari sınır, sır, a11y,
  4-durum, test, geri uyum) + `/code-review` skill; güvenlik dokunuşunda `/security-review`. ✅
- **Formatlama:** lefthook pre-commit (biome/ktlint/detekt format). ✅
- **Trunk benzeri akış:** kısa ömürlü feature branch → main; gözden geçirilmeden merge yok. ✅

## 4. Regülasyon & Uyumluluk
- **KVKK/GDPR:** PII zon ayrımı + minimizasyon; loglarda TC/kart/parola YASAK; saklama/imha
  politikası 🟡. ✅ (ADR-006)
- **PCI DSS:** kart bilgisi **işlenmez**; Stripe yönlendirme + tokenizasyon; tahsilat ertelendi. ✅ (ADR-007)
- **Erişilebilirlik:** WCAG 2.2 AA + EAA farkındalığı (≥4.5:1, semantics, klavye/odak, reduce-motion). ✅
- **ISO:** 27001/27002 (güvenlik), 9001 (kalite süreci=bu plan), 25010/25012/29148/42010/9241. ✅ (docs/ISO)
- **SOX/sektörel (BDDK/HIPAA/...):** ⚪ kapsam dışı (pazaryeri, finansal raporlama sistemi değil).

## 5. DevOps & CI/CD
- **Pipeline (GitHub Actions):** Build → Unit Test → SAST(detekt/gitleaks) → (Android) assemble →
  🟡 instrumented/E2E → artifact. ✅ temel / 🟡 ileri aşamalar.
- **Branching:** trunk-based eğilimli; karartma periyodu ⚪ (solo).
- **Artifact:** Play Internal / EAS build (Nexus/Artifactory eşdeğeri). 🟡
- **IaC:** Supabase migration'ları **kod** (marketing-2 `supabase/`); Terraform/Ansible ⚪ (managed). ✅ ilke
- **Sırlar:** local.properties/CI secret → BuildConfig; repoda sır yok (gitleaks kapısı). ✅ (ADR-005)

## 6. Güvenlik (Zero Trust)
- **IAM/SSO:** Supabase Auth (e-posta/şifre + Google OIDC); token EncryptedSharedPreferences/Keystore. ✅ (ADR-010)
- **Yetki:** OPA/rego yerine **Postgres RLS politikaları** (deny-by-default) = politika motoru. ✅
- **SAST/DAST:** SAST=detekt+gitleaks ✅; DAST/pentest 🟡 (faz sonu, OWASP MASVS/ZAP-eşdeğeri).
- **SBOM / tedarik zinciri:** bağımlılık taraması (Dependabot/`gradle dependencies`) 🟡; SLSA imzalı
  build 🟡; sadece onaylı lisans (MIT/Apache2; GPL-viral ❌). ✅ ilke
- **Dayanıklılık:** retry + timeout + (istemci) rate-limit + circuit-breaker eğilimi (Resilience4j
  yok → Kotlin Flow retry/backoff). 🟡

## 7. Veri Yönetimi
- **Sınıflandırma & maskeleme:** PII etiketi; canlı veri test'e taşınmaz; loglarda maskeleme. ✅ ilke
- **Saklama/imha:** retention politikası + hukuki saklama uyumu. 🟡
- **MDM / Data Mesh / Lakehouse:** ⚪ (ölçek dışı).

## 8. Test Stratejisi
- **Piramit:** Unit (saf mantık/use-case/mapper) ✅ hedef · Bileşen (Compose/Testing-Library
  eşdeğeri: `createComposeRule`) 🟡 · Entegrasyon (Supabase test/in-memory adapter) 🟡 ·
  Sözleşme (zod/şema) 🟡 · E2E (kritik akış: login→sipariş) 🟡 · Pentest/Chaos ⚪/🟡.
- **Sürekli test:** her PR'da CI; geri bildirim <~10dk hedef. ✅

## 9. Gözlemlenebilirlik & Denetim
- **Logging/tracing:** yapılandırılmış log + trace-id (distributed tracing eşdeğeri) 🟡; ham PII yok. 
- **RUM/APM:** Datadog/Dynatrace ⚪ (maliyet); Crashlytics/Sentry-eşdeğeri 🟡.
- **Immutable audit log + SoD:** denetlenebilir kayıt 🟡; **görevler ayrılığı solo'da yapısal
  olarak sağlanamaz → bilinçli istisna, ADR/gerekçe ile belgelenir** (abartma yok).

## 10. Süreklilik & Değişiklik
- **Feature flags:** deploy≠release; remote config/flag 🟡.
- **Rollback:** her sürümün geri dönüş planı (önceki APK/migration down) ✅ ilke.
- **DR (RTO/RPO):** Supabase yönetilen yedek; multi-region ⚪ (ölçek gelmeden).

## 11. Arayüz (Enterprise UI) — Köyden
- **Tasarım sistemi:** `:core:designsystem` ortak bileşen + **design token** (renk/tipografi/
  spacing/motion) tek-kaynak; sıfırdan bileşen yasak (varsa kullan). ✅ (Faz 1, ADR-009)
- **A11y:** WCAG 2.2 AA, axe/detekt-a11y CI'da; ≥48dp, semantics, reduce-motion. ✅
- **Performans bütçesi:** liste sanal kaydırma (LazyColumn), görsel optimizasyon, jank yok;
  Core Web Vitals eşdeğeri (mobil-web). 🟡 ölçüm.
- **State:** sunucu durumu (repo/Flow) vs UI durumu (UiState) ayrımı; tek-kaynak + 4-durum. ✅
- **Güvenlik (FE):** token localStorage'a yazılmaz (Keystore); girdi temizliği; mobil-web'de CSP. ✅ ilke
- **Form mühendisliği:** doğrulama (CredentialValidation kalıbı) + draft/anlık kayıt 🟡; dinamik
  şema-form 🟡.
- **i18n/RTL:** string kaynakları (ham metin yasak) 🟡; çok-dillilik ⚪ (şimdilik TR).
- **Mikro-frontend:** ⚪ (tek uygulama).
- **Mevzuata bağlı UI:** "Onaylıyorum" üstü zorunlu metin, KVKK çerez/aydınlatma. 🟡

## 12. Kültür & Organizasyon
- **Blameless postmortem:** hata → süreç sorgusu (suç değil); öğrenilen → brain/Gotchas. ✅
- **Platform/DevEx düşüncesi:** ortak CI/şablon/skill'ler tekrar kullanılır. ✅
- **Conway:** modül sınırları = gelecekteki ekip sınırları (stream-aligned hazır). ✅

---

**Standartlar (bu plan dosyası):** ISO 9001 (kalite süreci) · TOGAF (yönetişim/ADM) ·
ISO 27001/27002 · WCAG 2.2 AA · solo→ekip-hazır disiplin. Bu dosya **yaşayan**dır; yeni karar
geldikçe güncellenir (çelişki → supersedes) ve ADR ile bağlanır. "Yapıldı" yalnız fiilen
yapılana yazılır; 🟡/⚪ dürüstçe işaretlenir.
