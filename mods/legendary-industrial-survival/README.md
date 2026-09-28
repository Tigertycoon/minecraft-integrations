# Legendary Industrial Survival

NeoForge 1.21.1 integration between Create, industrial technology mods, Sophisticated Backpacks,
Iron's Spells 'n Spellbooks, Mekanism, and Legendary Survival Overhaul.

## Features

- Kinetic Heater and Kinetic Cooler driven by Create rotational power.
- Three configurable RPM tiers with increasing LSO heating or cooling strength.
- Active-machine temperature data for Mekanism, Mekanism Generators, Immersive Engineering,
  PneumaticCraft, Create New Age, and Create: Diesel Generators.
- Thermal resistance data for MekaSuit, Pneumatic Armor, Faraday Armor, and Create diving equipment.
- Basic and Advanced Drink Upgrades that consume LSO-compatible drinks from Sophisticated Backpacks.
- MekaSuit Thermoregulator, Hydration, and Medical Treatment modules.
- Medigel and Nanite Injector localized-body-damage healing items.
- Iron's healing spells also treat the most injured LSO limb.
- Periodic synchronization of external max-health modifiers with LSO.
- Configurable thresholds, energy costs, healing strength, and synchronization intervals.

## Required mods

- Legendary Survival Overhaul 2.4.5+
- Create 6.0.10+
- Mekanism 10.7.19+
- Sophisticated Core 1.4.75+
- Sophisticated Backpacks 3.25.71+
- Iron's Spells 'n Spellbooks 3.16.2+

## Credit

The concept of Create-powered LSO heater and cooler blocks was inspired by
[xGabou/LSOAddon](https://github.com/xGabou/LSOAddon). This implementation and its assets were
written from scratch because that repository does not provide a clear license for direct reuse.

## Build and development

From the repository root, run `./gradlew :industrial:build` (Windows: `gradlew.bat :industrial:build`). Java 21 is required. Dependencies are downloaded automatically; see the [pinned development runtime](docs/DEPENDENCIES.md).

`./gradlew :industrial:runClient` uses the pinned runtime. Optional mods beyond that profile need separate installation. See the collection's [validation scope](../../docs/VALIDATION.md) before treating a build as gameplay verification.
