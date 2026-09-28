# Architecture

## Arcane Survival

`IronsSpellsIntegration` and `SpellEngineIntegration` translate events into common survival effects. `SurvivalEffects` applies configured exhaustion, temperature and wetness on the server. `SurvivalCooldowns` prevents duplicate events without continually extending the cooldown; player logout and server shutdown discard state, and earlier world clocks are accepted.

Spell Engine integration handles active released casts. Direct-target spells can affect their selected player targets; this is not a general listener for every Spell Engine projectile impact. Iron's integration listens to its spell-damage events. Neutral schools do not consume an elemental target cooldown.

## Industrial Survival

Kinetic climate block entities translate Create rotational speed into block-state power tiers. LSO data files map those tiers to temperature contributions. Backpack upgrades select an LSO-compatible drink, temporarily expose it to the vanilla item-use path, restore the original hand and then return the resulting container. Restoring first matters when a full backpack sends the bottle into an otherwise empty selected player slot.

MekaSuit modules use server ticks and configurable energy costs. Body-health integration chooses the most injured limb by health ratio. Runtime mods are required for those API-backed features; optional machine/equipment compatibility is expressed through data files.

## Build boundary

The two Gradle subprojects produce independent mod JARs. Dependencies use fixed Modrinth version IDs or fixed upstream Maven coordinates. Compile/runtime dependencies are not bundled in the output. Each subproject can also be built independently with its own wrapper.
