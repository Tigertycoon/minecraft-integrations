# Legendary Arcane Survival

NeoForge 1.21.1 compatibility mod connecting magic systems to Legendary Survival Overhaul.

## Features

- Active Iron's Spells and Spell Engine casts add configurable thirst exhaustion.
- Fire, ice, frost, water, air, and lightning schools alter the caster's body temperature.
- Iron's spell-damage events and Spell Engine direct-target casts can warm, cool, wet, or dry player targets.
- Spell Engine passive and triggered archer skills are intentionally excluded from casting costs.
- Iron's command, mob, and non-player casts do not consume player thirst.
- All balance values and school mappings are editable in the generated common config.

## Required

- NeoForge 21.1.197 or newer
- Minecraft 1.21.1
- Legendary Survival Overhaul 2.4.5 or newer

Iron's Spells and Spell Engine are optional; the installed integration activates automatically.

## Build and development

From the repository root, run `./gradlew :arcane:build` (Windows: `gradlew.bat :arcane:build`). Java 21 is required. Dependencies are downloaded automatically; see the [pinned development runtime](docs/DEPENDENCIES.md).

`./gradlew :arcane:runClient` uses the pinned runtime. Optional mods beyond that profile need separate installation. See the collection's [validation scope](../../docs/VALIDATION.md) before treating a build as gameplay verification.
