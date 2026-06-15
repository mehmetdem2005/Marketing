# SeçAl — ISO Uyumluluk Eşlemesi

Bu doküman, uygulanan ISO standartlarını ve SeçAl'deki karşılıklarını listeler.
Ayrıntılar: `EA-TOGAF-mimari.md` (§10 Requirements Management) ve `guvenlik.md`.

## ISO/IEC/IEEE 42010:2022 — Architecture Description
- Doküman kontrolü (§0), Paydaş–Concern–Viewpoint matrisi (EA §2.3).
- Görünümler: Business / Data / Application / Technology / Security / Interaction.

## ISO/IEC 25010:2023 — Ürün Kalite Modeli
- NFR tablosu (EA §10.1): 9 karakteristik + hedef + ölçüt. Yüksek öncelik: İşlevsel Uygunluk,
  Performans, Güvenilirlik, Etkileşim, Güvenlik, Bakım.

## ISO/IEC 25012:2024 — Veri Kalitesi
- Doğruluk + tamlık + güncellik: ürün/stok/fiyat verisi; sipariş tutarlılığı (RPC atomikliği).

## ISO/IEC 27001:2022 + 27002:2022 — Bilgi Güvenliği
- Kontrol checklist (EA §10.2) + `guvenlik.md`: erişim (RLS/JWT), kriptografi (HTTPS/Keystore),
  güvenli geliştirme (gitleaks/detekt/review), veri minimizasyonu, sınıflandırma (PII zon).
- Sertifikasyon kapsam dışı (tailoring); kontrol-adopsiyonu ölçülür.

## ISO/IEC/IEEE 29148:2018 — Gereksinim Mühendisliği
- İzlenebilirlik: Prensip → Gereksinim → Faz → Test/Kabul (EA §10.3).

## ISO 9241-110:2020 — Etkileşim İlkeleri
- WCAG 2.2 AA + Material 3; 4-durum; reduce-motion; dokunma hedefi ≥48dp; ekran okuyucu uyumu.
- Detay: `tasarim-sistemi.md`.

---

**Standartlar:** 42010 · 25010 · 25012 · 27001/27002 · 29148 · 9241 — eşleme tablosu kuruldu;
uygulama fazlarla derinleşir (abartma yok).
