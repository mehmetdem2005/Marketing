#!/usr/bin/env bash
# Stop hook — STANDART KAPISI (SeçAl / Android).
# Tur biterken:
#   1) GÜVENLİK: gitleaks ile sır taraması (varsa) — sır bulunursa BLOKLA.
#   2) KALİTE: gradlew + Android SDK varsa detekt (a11y/lint/format) — sorun varsa BLOKLA.
#   3) İNCELEME: origin/main'e göre güvenlik-dokunuşu veya büyük diff incelenmemişse BLOKLA
#      → /security-review · /code-review çalıştırılıp `echo <sig> > .claude/.reviewed` ile onaylanmalı.
# Araç yoksa ilgili adım sessizce atlanır (CI'da tam kapı zaten çalışır).

set -u
dir="${CLAUDE_PROJECT_DIR:-.}"
cd "$dir" 2>/dev/null || exit 0

fail=""

# ---- 1) Güvenlik: sır taraması ----
if command -v gitleaks >/dev/null 2>&1; then
  if ! gitleaks detect --no-banner --redact -s "$dir" >/tmp/secal-gitleaks.log 2>&1; then
    fail="${fail}\n── gitleaks (SIR BULUNDU) ──\n$(grep -E 'Finding|File|Secret|RuleID' /tmp/secal-gitleaks.log | head -20)"
  fi
fi

# ---- 2) Kalite: detekt (yalnız araç + SDK varsa) ----
if [ -x "./gradlew" ] && { [ -n "${ANDROID_HOME:-}" ] || [ -n "${ANDROID_SDK_ROOT:-}" ]; }; then
  out="$(./gradlew detekt --quiet 2>&1)" || \
    fail="${fail}\n── detekt (kalite/a11y/format) ──\n$(echo "$out" | grep -E 'detekt|issue|error|Error' | head -15)"
fi

if [ -n "$fail" ]; then
  { echo "STANDART KAPISI: turu bitirmeden DÜZELT:"; printf '%b\n' "$fail"; } >&2
  exit 2
fi

# ---- 3) İnceleme kapısı (güvenlik / büyük diff) ----
if git rev-parse origin/main >/dev/null 2>&1; then
  changed="$(git diff origin/main --name-only 2>/dev/null)"
  if [ -n "$changed" ]; then
    sig="$(git diff origin/main 2>/dev/null | sha1sum | cut -c1-12)"
    acked="$(cat .claude/.reviewed 2>/dev/null || true)"
    sec="$(echo "$changed" | grep -Ei 'migrations/|/auth|payment|stripe|local\.properties|secret|rls|service.?role|token|keystore|webhook|BuildConfig' || true)"
    lines="$(git diff origin/main --shortstat 2>/dev/null | grep -oE '[0-9]+ (insertion|deletion)' | grep -oE '^[0-9]+' | awk '{s+=$1} END{print s+0}')"
    need=""
    [ -n "$sec" ] && need="güvenlik-dokunuşu → /security-review"
    [ "${lines:-0}" -ge 400 ] && need="${need:+$need · }büyük diff (${lines} satır) → /code-review veya /simplify"
    if [ -n "$need" ] && [ "$sig" != "$acked" ]; then
      {
        echo "STANDART KAPISI — İNCELEME GEREKLİ: $need"
        [ -n "$sec" ] && { echo "Kritik dosyalar:"; echo "$sec" | head -8; }
        echo "İlgili review skill'ini çalıştır; sonra onayla:  echo $sig > .claude/.reviewed"
      } >&2
      exit 2
    fi
  fi
fi
exit 0
