package net.ivangav.midori_ishi.screen;

import net.ivangav.midori_ishi.block.ModBlocks;
import net.ivangav.midori_ishi.block.entity.MidoriTransmogrificationTableEntity;
import net.ivangav.midori_ishi.item.ModItems;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class MidoriTransmogrificationTableMenu extends AbstractContainerMenu {
    private static final int BOTTLE_SLOT_START = 0;
    private static final int BOTTLE_SLOT_END = 2;
    private static final int INGREDIENT_SLOT = 2;
    private static final int FUEL_SLOT = 3;
    private static final int SLOT_COUNT = 4;
    private static final int DATA_COUNT = 2;
    private static final int USE_ROW_SLOT_START = 31;
    private static final int USE_ROW_SLOT_END = 40;

    private final MidoriTransmogrificationTableEntity blockEntity;
    private final Level level;
    private final ContainerData data;

    //client
    public MidoriTransmogrificationTableMenu(int pContainerId, Inventory inv, FriendlyByteBuf friendlyByteBuf) {
        this(pContainerId, inv, inv.player.level().getBlockEntity(friendlyByteBuf.readBlockPos()), new SimpleContainerData(2));
    }

    //server
    public MidoriTransmogrificationTableMenu(int pContainerId, Inventory playerInv, BlockEntity entity, ContainerData data) {
        super(ModMenuTypes.MIDORI_TRANSMOGRIFICATION_TABLE_MENU.get(), pContainerId);

        checkContainerSize(playerInv, 4);
        checkContainerDataCount(data, 2);

        if(entity instanceof MidoriTransmogrificationTableEntity be) {
            this.blockEntity = be;
        } else {
            throw new IllegalStateException("Incorrect block entity class passed into MidoriTransmogrificationTableMenu");
        }

        this.level = playerInv.player.level();
        this.data = data;

        this.blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(iItemHandler -> {
            this.addSlot(new PotionSlot(iItemHandler, 0, 56, 51));
//            this.addSlot(new PotionSlot(iItemHandler, 1, 79, 58));
            this.addSlot(new PotionSlot(iItemHandler, 1, 102, 51));
            this.addSlot(new IngredientsSlot(iItemHandler, 2, 79, 17)); //ingredient
            this.addSlot(new FuelSlot(iItemHandler, 3, 17, 17)); //fuel
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
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()),pPlayer, ModBlocks.MIDORI_TRANSMOGRIFICATION_TABLE.get());
    }

    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(pIndex);
        if (slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if ((pIndex < 0 || pIndex > 1) && pIndex != 2 && pIndex != 3) {
                if (FuelSlot.mayPlaceItem(itemstack)) {
                    if (this.moveItemStackTo(itemstack1, 3, 4, false) || IngredientsSlot.mayPlaceItem(itemstack1) && !this.moveItemStackTo(itemstack1, 3, 4, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (IngredientsSlot.mayPlaceItem(itemstack1)) {
                    if (!this.moveItemStackTo(itemstack1, 2, 3, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (PotionSlot.mayPlaceItem(itemstack)) {
                    if (!this.moveItemStackTo(itemstack1, 0, 2, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (pIndex >= 4 && pIndex < 31) {
                    if (!this.moveItemStackTo(itemstack1, 31, 40, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (pIndex >= 31 && pIndex < 40) {
                    if (!this.moveItemStackTo(itemstack1, 4, 31, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (!this.moveItemStackTo(itemstack1, 4, 40, false)) {
                    return ItemStack.EMPTY;
                }
            } else {
                if (!this.moveItemStackTo(itemstack1, 4, 40, true)) {
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
            return true;
        }
    }

    static class PotionSlot extends SlotItemHandler {
        public PotionSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
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
            return false;
        }
    }
}


