# Köyden — Tasarım Sistemi

Kanonik kod: `:core:designsystem`. UI işlerinde **design-standards + web-design-advanced +
ui-ux-advanced + motion-design** disiplinleri birlikte uygulanır.

## İlkeler
- **Atomic Design:** token → atom → molekül → organizma → şablon.
- **8pt grid:** boşluklar `LocalSpacing` token'ından (none/xxs/xs/sm/md/lg/xl/xxl). Sabit `dp` YOK.
- **Token tek-kaynak:** renk (`KoydenPalette`), tipografi (`KoydenTypography`), boşluk (`Spacing`). Inline renk/dp YASAK.
- **İkonografi:** vektör ikon (lucide/Material vektör). **EMOJİ-İKON YASAĞI.**
- **Material 3:** `KoydenTheme` (light/dark), marka renkleri (orman yeşili + buğday/toprak).

## Renk (token)
Birincil: Green40 (#2F6B3C) / container Green90 · İkincil: Wheat40 / container Wheat90 ·
Yüzey: Surface (#FBF7F0). Kontrast WCAG 2.2 AA (≥4.5:1) Faz 1'de token bazında doğrulanır.

## Tipografi
Modüler ölçek (headlineLarge 32 → bodyMedium 14). Faz 1'de marka fontu (self-host) eklenecek.

## Erişilebilirlik (WCAG 2.2 AA / ISO 9241)
- Kontrast ≥4.5:1 · görünür odak · dokunma hedefi ≥48dp.
- Her interaktif öğeye `contentDescription`/semantics; dekoratif öğeler `null`.
- Ekran okuyucu sırası mantıklı; `prefers-reduced-motion` (reduce-motion) animasyonu kapatabilir ama varsayılan animasyon vardır.

## 4-Durum Kuralı
Her veri ekranı: **loading / empty / error / content**. Ortak scaffold'lar `:core:ui` (Faz 1).

## Motion
Compose animasyonları (token'lı süre/easing); stagger; reduce-motion'a saygı.
"Animasyon yok = iş bitmemiş."

## Bileşen Kütüphanesi (Faz 1 hedefi)
Button, Card, TextField, Chip, Badge, Rating, Skeleton, ProductCard, PriceTag,
QuantityStepper, EmptyState, ErrorState, BottomSheet, Snackbar.

---

**Standartlar:** Atomic Design · Material 3 · 8pt grid · WCAG 2.2 AA / ISO 9241 ·
token tek-kaynak · emoji-ikon yasağı · motion+reduce-motion. Faz 0: tema + temel token'lar
(renk/tipografi/boşluk) kuruldu; bileşen kütüphanesi Faz 1.
