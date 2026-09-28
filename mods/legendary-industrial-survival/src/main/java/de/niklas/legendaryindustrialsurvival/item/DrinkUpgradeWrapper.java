package de.niklas.legendaryindustrialsurvival.item;

import de.niklas.legendaryindustrialsurvival.config.IndustrialConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.event.EventHooks;
import net.p3pp3rf1y.sophisticatedcore.api.IStorageWrapper;
import net.p3pp3rf1y.sophisticatedcore.inventory.ITrackedContentsItemHandler;
import net.p3pp3rf1y.sophisticatedcore.upgrades.ITickableUpgrade;
import net.p3pp3rf1y.sophisticatedcore.upgrades.UpgradeWrapperBase;
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonThirstConsumable;
import sfiomn.legendarysurvivaloverhaul.api.data.manager.ThirstDataManager;
import sfiomn.legendarysurvivaloverhaul.api.thirst.ThirstUtil;
import sfiomn.legendarysurvivaloverhaul.util.AttachmentUtil;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public final class DrinkUpgradeWrapper extends UpgradeWrapperBase<DrinkUpgradeWrapper, DrinkUpgradeItem>
        implements ITickableUpgrade {
    private static final int NORMAL_COOLDOWN = 40;
    private static final int RETRY_COOLDOWN = 10;

    public DrinkUpgradeWrapper(IStorageWrapper storageWrapper, ItemStack upgrade, Consumer<ItemStack> saveHandler) {
        super(storageWrapper, upgrade, saveHandler);
    }

    @Override
    public boolean hideSettingsTab() {
        return true;
    }

    @Override
    public void tick(@Nullable Entity entity, Level level, BlockPos pos) {
        if (level.isClientSide || isInCooldown(level) || !(entity instanceof Player player)
                || player.isUsingItem() || !ThirstUtil.isThirstActive(player)) {
            return;
        }

        int hydration = AttachmentUtil.getThirstAttachment(player).getHydrationLevel();
        int threshold = upgradeItem.isAdvanced()
                ? IndustrialConfig.ADVANCED_DRINK_THRESHOLD.get()
                : IndustrialConfig.BASIC_DRINK_THRESHOLD.get();
        if (hydration > threshold) {
            setCooldown(level, NORMAL_COOLDOWN);
            return;
        }

        boolean drank = drinkFromStorage(player, level, hydration);
        setCooldown(level, drank ? NORMAL_COOLDOWN : RETRY_COOLDOWN);
    }

    private boolean drinkFromStorage(Player player, Level level, int hydration) {
        ITrackedContentsItemHandler inventory = storageWrapper.getInventoryForUpgradeProcessing();
        int selectedSlot = -1;
        int selectedHydration = -1;

        for (int slot = 0; slot < inventory.getSlots(); slot++) {
            ItemStack stack = inventory.getStackInSlot(slot);
            if (stack.isEmpty()) {
                continue;
            }
            JsonThirstConsumable thirst = ThirstDataManager.getConsumable(stack);
            if (thirst == null || thirst.hydration <= 0) {
                continue;
            }
            if (!upgradeItem.isAdvanced()) {
                selectedSlot = slot;
                break;
            }

            int missing = 20 - hydration;
            if (DrinkSelection.isBetter(thirst.hydration, selectedHydration, missing)) {
                selectedHydration = thirst.hydration;
                selectedSlot = slot;
            }
        }

        return selectedSlot >= 0 && consumeOne(player, level, inventory, selectedSlot);
    }

    private boolean consumeOne(Player player, Level level, ITrackedContentsItemHandler inventory, int slot) {
        ItemStack drink = inventory.extractItem(slot, 1, false);
        if (drink.isEmpty()) {
            return false;
        }

        int selected = player.getInventory().selected;
        return DrinkConsumption.consume(drink,
                () -> player.getInventory().items.get(selected),
                stack -> player.getInventory().items.set(selected, stack),
                held -> {
                    var useResult = held.use(level, player, InteractionHand.MAIN_HAND);
                    if (!useResult.getResult().consumesAction()) {
                        return new DrinkConsumption.Result<>(false, held);
                    }
                    ItemStack used = useResult.getObject();
                    ItemStack beforeFinish = used.copy();
                    ItemStack remainder = EventHooks.onItemUseFinish(player, beforeFinish, 0,
                            used.getItem().finishUsingItem(used, level, player));
                    return new DrinkConsumption.Result<>(true, remainder);
                },
                player::stopUsingItem,
                remainder -> {
                    if (!remainder.isEmpty()) {
                        returnToStorageOrPlayer(player, inventory, remainder);
                    }
                });
    }

    private static void returnToStorageOrPlayer(
            Player player,
            ITrackedContentsItemHandler inventory,
            ItemStack stack) {
        ItemStack remainder = inventory.insertItem(stack, false);
        if (!remainder.isEmpty() && !player.getInventory().add(remainder)) {
            player.drop(remainder, false);
        }
    }
}
