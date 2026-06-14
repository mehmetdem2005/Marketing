#!/bin/bash
# Köyden — SessionStart hook
#  1) Ajan skill'lerini (hafıza/bilgi yönetimi + motion) ~/.claude'a KALICI kurar/yeniler.
#     Ortam ephemeral olduğu için her web oturumunda yeniden kurulurlar (idempotent).
#  2) Skill yönlendirme rehberini (.claude/skill-routing.md) oturum bağlamına enjekte eder.
# Senkron çalışır (skill'lerin oturum başlamadan hazır olmasını garanti eder).
set -uo pipefail

CLAUDE_HOME="${HOME}/.claude"
SKILLS="$CLAUDE_HOME/skills"
COMMANDS="$CLAUDE_HOME/commands"
AGENTS="$CLAUDE_HOME/agents"
CACHE="${HOME}/.cache/koyden-agent-skills"
MARK="$SKILLS/.koyden-skills-installed"

log() { printf 'Köyden-hook: %s\n' "$*" >&2; }   # kurulum logu stderr'e (bağlamı kirletmesin)

_clone() { # idempotent shallow clone-or-update
  local dir="$1" url="$2"
  if [ -d "$CACHE/$dir/.git" ]; then
    git -C "$CACHE/$dir" pull --ff-only -q 2>/dev/null || true
  else
    git clone --depth 1 -q "$url" "$CACHE/$dir" 2>/dev/null || { log "klon hata: $url"; return 1; }
  fi
}
_put() { local src="$1" name="$2"; [ -d "$src" ] || return 0; rm -rf "$SKILLS/$name"; cp -R "$src" "$SKILLS/$name"; }

install_skills() {
  mkdir -p "$SKILLS" "$COMMANDS" "$AGENTS" "$CACHE"
  local fail=0

  # 1) kepano/obsidian-skills → 5 Obsidian araç skill'i
  if _clone kepano https://github.com/kepano/obsidian-skills; then
    for s in obsidian-markdown obsidian-bases json-canvas obsidian-cli defuddle; do
      _put "$CACHE/kepano/skills/$s" "$s"
    done
  else fail=1; fi

  # 2) breferrari/obsidian-mind → qmd skill + om-* komutlar + agent'lar
  if _clone mind https://github.com/breferrari/obsidian-mind; then
    _put "$CACHE/mind/.claude/skills/qmd" qmd
    cp "$CACHE/mind/.claude/commands/"*.md "$COMMANDS/" 2>/dev/null || true
    cp "$CACHE/mind/.claude/agents/"*.md "$AGENTS/" 2>/dev/null || true
  else fail=1; fi

  # 3) qhuang20/obsidian-skills → llm-wiki
  if _clone qhuang https://github.com/qhuang20/obsidian-skills; then
    _put "$CACHE/qhuang/skills/llm-wiki" llm-wiki
  else fail=1; fi

  # 4) eugeniughelbur/obsidian-second-brain → skill + obsidian-* komutlar
  if _clone secondbrain https://github.com/eugeniughelbur/obsidian-second-brain; then
    mkdir -p "$SKILLS/obsidian-second-brain"
    cp "$CACHE/secondbrain/SKILL.md" "$SKILLS/obsidian-second-brain/SKILL.md" 2>/dev/null || true
    for d in references scripts hooks; do
      [ -d "$CACHE/secondbrain/$d" ] && { rm -rf "$SKILLS/obsidian-second-brain/$d"; cp -R "$CACHE/secondbrain/$d" "$SKILLS/obsidian-second-brain/$d"; }
    done
    cp "$CACHE/secondbrain/commands/"*.md "$COMMANDS/" 2>/dev/null || true
  else fail=1; fi

  # 5) lottiefiles/motion-design-skill → motion-design
  if _clone motion https://github.com/lottiefiles/motion-design-skill; then
    _put "$CACHE/motion/skills/motion-design" motion-design
  else fail=1; fi

  if [ "$fail" -eq 0 ]; then date > "$MARK"; log "tüm skill'ler kuruldu."; else log "bazı skill'ler kurulamadı (ağ)."; fi
}

# Eksik varsa kur (idempotent + ağ-dostu: hepsi yerindeyse atla)
if [ ! -f "$MARK" ] || [ ! -d "$SKILLS/motion-design" ] || [ ! -d "$SKILLS/obsidian-second-brain" ] || [ ! -d "$SKILLS/llm-wiki" ]; then
  log "ajan skill'leri kuruluyor…"
  install_skills
else
  log "skill'ler zaten kurulu (atlandı)."
fi

# Bağlam enjeksiyonu: skill yönlendirme rehberi (stdout → oturum bağlamı)
ROUTING="${CLAUDE_PROJECT_DIR:-.}/.claude/skill-routing.md"
if [ -f "$ROUTING" ]; then cat "$ROUTING"; fi
exit 0
