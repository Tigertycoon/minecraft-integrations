# Minecraft Integrations

[![Build and test](https://github.com/Tigertycoon/minecraft-integrations/actions/workflows/build.yml/badge.svg)](https://github.com/Tigertycoon/minecraft-integrations/actions/workflows/build.yml)

Two Java mods that connect **magic, machines and survival mechanics** in Minecraft 1.21.1 with NeoForge.

| Mod | What it adds | Code to explore |
|---|---|---|
| [Legendary Arcane Survival](mods/legendary-arcane-survival) | Spellcasting costs thirst; elemental magic changes temperature and wetness. | Optional event adapters, shared effects and player cooldown lifecycle. |
| [Legendary Industrial Survival](mods/legendary-industrial-survival) | Create-powered heaters/coolers, backpack drinking and MekaSuit survival modules. | Inventory consumption, cross-mod health integration and data-driven recipes. |

These are personal gameplay integrations developed by Niklas / Tigertycoon with assistance from OpenAI Codex. The upstream mods remain separate projects; this repository contains the integration code and its resources. See [credits and provenance](CREDITS.md).

## Build

Install **JDK 21**, clone this repository, then run:

```sh
./gradlew clean build
```

On Windows: `gradlew.bat clean build`. The first build downloads Gradle, Minecraft/NeoForge development artifacts and pinned mod dependencies. No copied JARs, neighboring checkouts or credentials are needed.

Output:

- `mods/legendary-arcane-survival/build/libs/legendary_arcane_survival-1.0.1.jar`
- `mods/legendary-industrial-survival/build/libs/legendary_industrial_survival-1.1.1.jar`

Build only one mod with `./gradlew :arcane:build` or `./gradlew :industrial:build`.
Install its JAR and required dependencies in a **Minecraft 1.21.1 NeoForge** instance.
Each mod README lists its runtime requirements and pinned development profile.

## Engineering notes

- Server-side event handling keeps survival state authoritative.
- Optional magic integrations activate only when their mod is installed.
- Balance settings use NeoForge configuration; recipes, machine temperature and equipment resistance use JSON data.
- Regression tests cover cooldown boundaries/session resets and drink selection/hand restoration. A server GameTest checks all nine Industrial recipes against the real runtime. CI builds both mods and validates resource JSON.

Read the [architecture](docs/ARCHITECTURE.md), [validation scope](docs/VALIDATION.md) and [changes in this export](CHANGELOG.md).

## Development runs

`./gradlew :arcane:runClient` or `./gradlew :industrial:runClient` starts the corresponding development profile. `:arcane:runServer` and `:industrial:runServer` start dedicated development servers. Review and accept the Minecraft EULA yourself before running a server. These tasks create isolated `run/` directories that are ignored by Git.

## Related project

[Village Garrison](https://github.com/Tigertycoon/village-garrison) adds persistent village defense, ranged mage AI and Spell Engine delivery for non-player casters.

## License

Project code and original resources: [MIT](LICENSE). Gradle wrapper: Apache-2.0; see [credits](CREDITS.md). Referenced Minecraft/mod assets and dependencies retain their own licenses.
