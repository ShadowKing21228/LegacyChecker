# LegacyChecker

A lightweight, fail-safe utility mod that ensures players have all mandatory mods installed before entering the game.

It elegantly solves license restrictions (like **All Rights Reserved / ARR**) that prevent modpack authors from bundling specific mods directly into modpack files.

---

## 📦 Configuration (`legacychecker-common.yaml`)

The mod automatically generates a YAML configuration. You can specify as many required mods as your pack needs.

```yaml
# List of required mods that must be installed for the game to run.
# If any mod is missing, an error screen will block startup or show a warning.
#
# Structure per mod:
#   - modId:   "The exact internal ID of the mod"
#   - modName: "The friendly name displayed to the player"
#   - url:     "Direct download link (CurseForge, Modrinth, etc.)"
requiredMods:
  - modId: "jei"
    modName: "Just Enough Items (JEI)"
    url: "[https://www.curseforge.com/minecraft/mc-mods/jei](https://www.curseforge.com/minecraft/mc-mods/jei)"
  - modId: "gregtech"
    modName: "GregTech CE Unofficial"
    url: "[https://www.curseforge.com/minecraft/mc-mods/gregtech-ce-unofficial](https://www.curseforge.com/minecraft/mc-mods/gregtech-ce-unofficial)"

# If true, the "Continue" button appears, allowing players to skip the warning.
# If false, players must install the mods or exit the game.
canPlayerLoadGame: false