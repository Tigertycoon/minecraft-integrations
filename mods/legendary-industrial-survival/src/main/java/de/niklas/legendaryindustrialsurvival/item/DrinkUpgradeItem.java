package de.niklas.legendaryindustrialsurvival.item;

import net.minecraft.world.item.Item;
import net.p3pp3rf1y.sophisticatedcore.upgrades.IUpgradeCountLimitConfig;
import net.p3pp3rf1y.sophisticatedcore.upgrades.UpgradeItemBase;
import net.p3pp3rf1y.sophisticatedcore.upgrades.UpgradeType;

import java.util.List;

public final class DrinkUpgradeItem extends UpgradeItemBase<DrinkUpgradeWrapper> {
    private static final IUpgradeCountLimitConfig LIMITS = new IUpgradeCountLimitConfig() {
        @Override
        public int getMaxUpgradesPerStorage(String storageType, net.minecraft.resources.ResourceLocation upgradeRegistryName) {
            return 1;
        }

        @Override
        public int getMaxUpgradesInGroupPerStorage(String storageType, net.p3pp3rf1y.sophisticatedcore.upgrades.UpgradeGroup upgradeGroup) {
            return 1;
        }
    };

    public static final UpgradeType<DrinkUpgradeWrapper> TYPE = new UpgradeType<>(DrinkUpgradeWrapper::new);
    private final boolean advanced;

    public DrinkUpgradeItem(boolean advanced) {
        super(LIMITS);
        this.advanced = advanced;
    }

    public boolean isAdvanced() {
        return advanced;
    }

    @Override
    public UpgradeType<DrinkUpgradeWrapper> getType() {
        return TYPE;
    }

    @Override
    public List<UpgradeConflictDefinition> getUpgradeConflicts() {
        return List.of(new UpgradeConflictDefinition(
                item -> item instanceof DrinkUpgradeItem,
                0,
                net.minecraft.network.chat.Component.translatable(
                        "gui.legendary_industrial_survival.drink_upgrade_conflict")));
    }
}
