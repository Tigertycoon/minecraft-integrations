# Validation

Baseline: Minecraft 1.21.1, NeoForge 21.1.236, Java 21 and each mod's pinned development runtime.

```sh
./gradlew clean build :industrial:runGameTestServer
python3 scripts/check_resources.py
```

On Windows use `gradlew.bat` and `python`. GitHub Actions runs the same checks on Linux.

## Automated coverage

| Suite | Coverage |
|---|---|
| Arcane — 7 JUnit cases | Duplicate and interleaved casts, player isolation, cooldown boundaries, earlier world clocks, logout/server reset and zero cooldown. |
| Industrial — 9 JUnit cases | Drink fit/overflow selection, large custom drinks, rejected consumption, hand restoration and bottle-return ordering. |
| Industrial — 1 server GameTest | Loads a real test world with the baseline mods and checks registration and recipe loading for all nine crafted items. |
| Resources | Parses 61 JSON files and rejects duplicate keys. |

Both JUnit suites load their real dependencies through NeoForge. The server GameTest is kept in a separate `gametest` source set; its code and empty test structure are not included in the published mod JAR. Mod dependency binaries are not bundled either.

The local preparation run passed all checks above. Build artifacts and reports are also available from the GitHub Actions workflow linked in the README.

## Limits and known upstream diagnostics

The checks do not prove multiplayer behavior, all optional mod combinations, long-running performance, client rendering or gameplay balance. They cover the explicit cases above. No visual demo is included.

The pinned Iron's Spells release logs an invalid upstream `test/ring_gen_break_me` loot table during datapack loading. That diagnostic is outside these mods' resources; it does not prevent the nine project recipes from loading or the GameTest from passing. Upstream update-check and development-environment warnings can also appear in logs.

Mekanism requires an actual world for its tick handlers, so the Industrial content check runs in a GameTest world rather than the NeoForge ephemeral JUnit datapack server.

For manual gameplay verification, use a separate instance and exercise active spells, thirst/temperature changes, full backpack/player inventories, MekaSuit energy usage, heater/cooler speed changes and world reloads. Keep ordinary savegames separate.

## Tooling

- [ModDevGradle](https://github.com/neoforged/ModDevGradle)
- [NeoForge GameTest configuration](https://github.com/NeoForgeMDKs/MDK-1.21.1-ModDevGradle/blob/main/build.gradle)
