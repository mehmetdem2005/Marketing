# SeçAl — Güvenlik (ISO 27001/27002 + OWASP MASVS)

## 1. Sır Politikası
- Sır **repoda tutulmaz**. `local.properties`, `*.keystore`, `.env` → `.gitignore`.
- Android anahtarları: `local.properties` / CI secret → `BuildConfig` (derleme zamanı).
- **Supabase anon key** tasarım gereği herkese açıktır (istemciye gömülür); güvenlik **RLS** ile sağlanır.
- **service-role key** ve **Stripe secret** yalnız **Edge Function env**'inde; istemciye **asla** girmez.
- Üç katmanlı sır taraması: lefthook (pre-commit) · CI (gitleaks-action) · Stop hook.

## 2. Kimlik & Oturum
- Supabase Auth (GoTrue). JWT + refresh.
- Token istemcide **EncryptedSharedPreferences / Android Keystore** ile saklanır (düz metin yok).
- Yedekleme kuralları token'ları hariç tutar (`backup_rules.xml`, `data_extraction_rules.xml`).

## 3. Ağ
- Yalnız HTTPS; `android:usesCleartextTraffic="false"`.
- (Faz 7 opsiyonel) certificate pinning.

## 4. RLS Matrisi (özet — detay migration'larda, marketing-2)
| Tablo | anon | buyer (auth) | seller | admin |
|---|---|---|---|---|
| categories | read | read | read | all |
| stores (public) | read | read | own: all | all |
| products / variants / images / inventory | read | read | own store: all | all |
| profiles | — | self: read/update | self | all |
| addresses | — | self: all | — | all |
| favorites / carts / cart_items | — | self: all | — | all |
| orders / order_items | — | self: read; create via RPC | own store siparişleri: read/update durum | all |
| order_status_history | — | self sipariş: read | own store: insert (geçerli geçiş) | all |
| reviews | read | self: create (teslim edilmiş siparişe), update/delete own | read | all |
| notifications | — | self: read/update | self | all |
| payment_intents | — | self: read | — | all |

İlke: **deny-by-default**; her satır sahibine `auth.uid()` ile bağlanır; satıcı erişimi
`store_members` üyeliğiyle doğrulanır; atomik işlemler `SECURITY DEFINER` RPC.

## 5. STRIDE Tehdit Modeli (özet)
| Tehdit | Örnek | Önlem |
|---|---|---|
| Spoofing | Sahte kimlik | Supabase Auth + JWT |
| Tampering | Stok/fiyat manipülasyonu | RLS (own store), RPC ile atomik stok |
| Repudiation | İşlem inkarı | order_status_history (denetim izi) |
| Information Disclosure | PII sızıntısı | RLS deny-by-default, PII zon, minimizasyon |
| Denial of Service | Aşırı istek | Supabase rate limit; istemci geri-çekilme |
| Elevation of Privilege | buyer→seller yetki | store_members doğrulaması, service-role yalnız Edge |

## 6. Girdi Doğrulama
Domain validator'ları (fiyat ≥ 0, miktar > 0, zorunlu alanlar); sunucu tarafı CHECK
kısıtları (migration'larda). İstemci doğrulaması UX için, sunucu doğrulaması güvenlik için.

---

**Standartlar:** ISO 27001/27002 (erişim, kriptografi, güvenli geliştirme, minimizasyon) ·
OWASP MASVS · STRIDE tehdit modeli · TOGAF Phase G. Faz 0 kapsamında **politika + matris
iskeleti** kuruldu; RLS politikaları marketing-2 migration'larında fazlarla uygulanır.
