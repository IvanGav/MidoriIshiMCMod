package net.ivangav.midori_ishi.screen;

import net.ivangav.midori_ishi.block.ModBlocks;
import net.ivangav.midori_ishi.block.entity.MidoriInfusionTableEntity;
import net.ivangav.midori_ishi.item.ModItems;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class MidoriInfusionTableMenu extends AbstractContainerMenu {
    private static final int BOTTLE_SLOT_START = 0;
    private static final int BOTTLE_SLOT_END = 2;
    private static final int INGREDIENT_SLOT = 3;
    private static final int FUEL_SLOT = 4;
    private static final int SLOT_COUNT = 5;
    private static final int DATA_COUNT = 2;
    private static final int INV_SLOT_START = 5;
    private static final int INV_SLOT_END = 32;
    private static final int USE_ROW_SLOT_START = 32;
    private static final int USE_ROW_SLOT_END = 41;

    private final MidoriInfusionTableEntity blockEntity;
    private final Level level;
    private final ContainerData data;

    //client

    public MidoriInfusionTableMenu(int pContainerId, Inventory inv, FriendlyByteBuf friendlyByteBuf) {
        this(pContainerId, inv, inv.player.level().getBlockEntity(friendlyByteBuf.readBlockPos()), new SimpleContainerData(2));
    }

    //server
    public MidoriInfusionTableMenu(int pContainerId, Inventory playerInv, BlockEntity entity, ContainerData data) {
        super(ModMenuTypes.MIDORI_INFUSION_TABLE_MENU.get(), pContainerId);

        checkContainerSize(playerInv, 5);
        checkContainerDataCount(data, 2);

        if(entity instanceof MidoriInfusionTableEntity be) {
            this.blockEntity = be;
        } else {
            throw new IllegalStateException("Incorrect block entity class passed into MidoriInfusionTableMenu");
        }

        this.level = playerInv.player.level();
        this.data = data;

        this.blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(iItemHandler -> {
            this.addSlot(new PotionSlot(iItemHandler, 0, 56, 51));
            this.addSlot(new PotionSlot(iItemHandler, 1, 79, 58));
            this.addSlot(new PotionSlot(iItemHandler, 2, 102, 51));
            this.addSlot(new IngredientsSlot(iItemHandler, 3, 79, 17)); //ingredient
            this.addSlot(new FuelSlot(iItemHandler, 4, 17, 17)); //fuel
        });

        this.addDataSlots(data);

        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
        for(int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(playerInv, k, 8 + k * 18, 142));
        }
    }

    /**
     * Determines whether supplied player can use this container
     */
    public boolean stillValid(Player pPlayer) {
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()),pPlayer, ModBlocks.MIDORI_INFUSION_TABLE.get());
    }

    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(pIndex);
        if (slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if ((pIndex < 0 || pIndex > 2) && pIndex != 3 && pIndex != 4) {
                if (FuelSlot.mayPlaceItem(itemstack)) {
                    if (this.moveItemStackTo(itemstack1, 4, 5, false) || IngredientsSlot.mayPlaceItem(itemstack1) && !this.moveItemStackTo(itemstack1, 3, 4, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (IngredientsSlot.mayPlaceItem(itemstack1)) {
                    if (!this.moveItemStackTo(itemstack1, 3, 4, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (PotionSlot.mayPlaceItem(itemstack)) {
                    if (!this.moveItemStackTo(itemstack1, 0, 3, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (pIndex >= 5 && pIndex < 32) {
                    if (!this.moveItemStackTo(itemstack1, 32, 41, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (pIndex >= 32 && pIndex < 41) {
                    if (!this.moveItemStackTo(itemstack1, 5, 32, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (!this.moveItemStackTo(itemstack1, 5, 41, false)) {
                    return ItemStack.EMPTY;
                }
            } else {
                if (!this.moveItemStackTo(itemstack1, 5, 41, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickCraft(itemstack1, itemstack);
            }

            if (itemstack1.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(pPlayer, itemstack1);
        }

        return itemstack;
    }

    public int getFuel() {
        return this.data.get(1);
    }

    public int getBrewingTicks() {
        return this.data.get(0);
    }

    static class FuelSlot extends SlotItemHandler {
        public FuelSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
            super(itemHandler, index, xPosition, yPosition);
        }

        @Override
        public boolean mayPlace(ItemStack pStack) {
            return mayPlaceItem(pStack);
        }

        @Override
        public int getMaxStackSize() {
            return 64;
        }

        public static boolean mayPlaceItem(ItemStack pItemStack) {
            return pItemStack.is(ModItems.MIDORI_ISHI.get());
        }
    }

    static class IngredientsSlot extends SlotItemHandler {
        public IngredientsSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
            super(itemHandler, index, xPosition, yPosition);
        }

        @Override
        public boolean mayPlace(ItemStack pStack) {
            return mayPlaceItem(pStack);
        }

        @Override
        public int getMaxStackSize() {
            return 64;
        }

        public static boolean mayPlaceItem(ItemStack pStack) {
            return !(pStack.is(Items.POTION));
        }
    }

    static class PotionSlot extends SlotItemHandler {
        public PotionSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
            super(itemHandler, index, xPosition, yPosition);
        }

        @Override
        public boolean mayPlace(@NotNull ItemStack pStack) {
            return mayPlaceItem(pStack);
        }

        @Override
        public int getMaxStackSize() {
            return 1;
        }

        public static boolean mayPlaceItem(ItemStack pStack) {
            return pStack.is(Items.POTION);
        }

        @Override
        public int getMaxStackSize(@NotNull ItemStack stack) {
            return 1;
        }

        @Override
        public void set(@NotNull ItemStack stack) {
            ItemStack copiedItemStack = stack.copyWithCount(1);
            super.set(copiedItemStack);
        }
    }
}


