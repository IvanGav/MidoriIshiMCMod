package net.ivangav.midori_ishi.block.entity;

import net.ivangav.midori_ishi.block.custom.MidoriInfusionTableBlock;
import net.ivangav.midori_ishi.item.ModItems;
import net.ivangav.midori_ishi.item.custom.MidoriVialItem;
import net.ivangav.midori_ishi.screen.MidoriInfusionTableMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import javax.annotation.Nullable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

import static net.minecraftforge.common.capabilities.ForgeCapabilities.ITEM_HANDLER;

public class MidoriInfusionTableEntity extends BlockEntity implements MenuProvider {
    private static final int INGREDIENT_SLOT = 3;
    private static final int FUEL_SLOT = 4;
    public static final int FUEL_USES = 2;
    public static final int DATA_BREW_TIME = 0;
    public static final int DATA_FUEL_USES = 1;
    public static final int NUM_DATA_VALUES = 2;
    public static final int BREW_DURATION = 100; //how long 1 brewing session lasts
    /** The items currently placed in the slots of the brewing stand. */
//    private NonNullList<ItemStack> items = NonNullList.withSize(5, ItemStack.EMPTY);
    private final ItemStackHandler items = new ItemStackHandler(5);
    private Item ingredient = Items.AIR;
    private boolean[] potSpot = {false,false,false}; //purely for visual stuff; doesn't provide any other functionality

    private LazyOptional<IItemHandler> handler = LazyOptional.empty();

    protected final ContainerData dataAccess;
    int brewTime;
    int fuel;

    public MidoriInfusionTableEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.MIDORI_INFUSION_TABLE_ENTITY.get(), pos, state);
        dataAccess = new ContainerData() {
            public int get(int index) {
                return switch (index) {
                    case DATA_BREW_TIME -> brewTime;
                    case DATA_FUEL_USES -> fuel;
                    default -> 0;
                };
            }
            public void set(int index, int value) {
                switch (index) {
                    case DATA_BREW_TIME:
                        brewTime = value;
                        break;
                    case DATA_FUEL_USES:
                        fuel = value;
                }
            }
            public int getCount() {
                return NUM_DATA_VALUES;
            }
        };
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable("block.midori_ishi.midori_infusion_table");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new MidoriInfusionTableMenu(pContainerId, pPlayerInventory, this, this.dataAccess);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        handler.invalidate();
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing) {
        if (capability == ITEM_HANDLER) {
            return handler.cast();
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        handler = LazyOptional.of(()->items);
    }

    public int getContainerSize() {
        return this.items.getSlots();
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.putShort("BrewTime", (short)brewTime);
//        ContainerHelper.saveAllItems(pTag, this.items);
        pTag.put("Inventory", items.serializeNBT());
        pTag.putByte("Fuel", (byte)fuel);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
//        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
//        ContainerHelper.loadAllItems(pTag, this.items);
        this.brewTime = pTag.getShort("BrewTime");
        this.items.deserializeNBT(pTag.getCompound("Inventory"));
        this.fuel = pTag.getByte("Fuel");
    }

    //called 20 times/sec
    public void tick(Level pLevel, BlockPos pPos, BlockState pState) {
        if(mIngredientChanged()) {
            mSetIngredient();
            mResetProgress();
            setChanged(pLevel, pPos, pState);
        }
        if(mHasRecipe()) {
            mCheckFuel();
            if(fuel > 0) {
                if(mCraftingFinished()) {
                    //recipe hasn't started yet
                    mStartCrafting();
                    setChanged(pLevel, pPos, pState);
                }
                mDoCraftingProgress();
                if (mCraftingFinished()) {
                    mCraftItem();
                    mSetIngredient();
                    mResetProgress();
                    setChanged(pLevel, pPos, pState);
                }
            }
        } else {
            mResetProgress();
        }

        //for updating the texture with bottles
        if (mPotSpotUpdate()) {
            BlockState blockstate = pState;
            if (!(pState.getBlock() instanceof MidoriInfusionTableBlock)) {return;}

            for(int i = 0; i < MidoriInfusionTableBlock.HAS_BOTTLE.length; ++i) {
                blockstate = blockstate.setValue(MidoriInfusionTableBlock.HAS_BOTTLE[i], potSpot[i]);
            }

            pLevel.setBlock(pPos, blockstate, 2);
        }
    }

    private boolean mPotSpotUpdate() {
        if(Arrays.equals(potSpot, getOccupiedPotionSlots())) {
            return false; //no update
        }
        potSpot = getOccupiedPotionSlots();
        return true;
    }

    private void mCraftItem() {
        ItemStack infusedItem = items.getStackInSlot(INGREDIENT_SLOT);
        boolean[] pot_pos = getPotionBits();
        if(pot_pos[0]) {
            items.extractItem(0, 1, false);
            items.insertItem(0, new ItemStack(ModItems.MIDORI_VIAL.get()), false);
            MidoriVialItem.giveNBT(items.getStackInSlot(0), infusedItem);
        }
        if(pot_pos[1]) {
            items.extractItem(1, 1, false);
            items.insertItem(1, new ItemStack(ModItems.MIDORI_VIAL.get()), false);
            MidoriVialItem.giveNBT(items.getStackInSlot(1), infusedItem);
        }
        if(pot_pos[2]) {
            items.extractItem(2, 1, false);
            items.insertItem(2, new ItemStack(ModItems.MIDORI_VIAL.get()), false);
            MidoriVialItem.giveNBT(items.getStackInSlot(2), infusedItem);
        }
        items.getStackInSlot(INGREDIENT_SLOT).shrink(1);
        fuel--;
    }

    private void mStartCrafting() {
        mSetIngredient();
        brewTime = BREW_DURATION;
    }

    private boolean mIngredientChanged() {
        return !(ingredient.equals(items.getStackInSlot(INGREDIENT_SLOT).getItem()));
    }

    private void mDoCraftingProgress() {
        brewTime--;
    }

    private boolean mCraftingFinished() {
        return brewTime == 0;
    }

    private boolean mHasRecipe() {
        ItemStack ingredient = items.getStackInSlot(INGREDIENT_SLOT);
        boolean[] potion_slots_filled = this.getPotionBits();
        boolean has_available_water_bottles = potion_slots_filled[0] || potion_slots_filled[1] || potion_slots_filled[2];
        //if empty, no recipe set
        return !(ingredient.isEmpty()) && has_available_water_bottles;
    }

    private void mSetIngredient() {
        ingredient = items.getStackInSlot(INGREDIENT_SLOT).getItem();
    }

    private void mResetProgress() {
        brewTime = 0;
    }

    private void mCheckFuel() {
        //if ran out of fuel, refill the fuel as long as has midori ishi inserted
        ItemStack fuel_stack = items.getStackInSlot(FUEL_SLOT);
        if (fuel <= 0 && fuel_stack.is(ModItems.MIDORI_ISHI.get())) {
            System.out.println("refueling");
            fuel = FUEL_USES;
            fuel_stack.shrink(1);
        }
    }

    //return slots which are currently water bottles
    private boolean[] getPotionBits() {
        boolean[] slots = new boolean[3];
        for(int i = 0; i < 3; ++i) {
            if (this.items.getStackInSlot(i).is(Items.POTION)) {
                slots[i] = true;
            }
        }
        return slots;
    }

    //return slots which are currently filled with anything
    private boolean[] getOccupiedPotionSlots() {
        boolean[] slots = new boolean[3];
        for (int i = 0; i < 3; ++i) {
            if (!this.items.getStackInSlot(i).isEmpty()) {
                slots[i] = true;
            }
        }
        return slots;
    }

    //to drop items when broken =(
    public void drops() {
        SimpleContainer inventory = new SimpleContainer(items.getSlots());
        for (int i = 0; i < items.getSlots(); i++) {
            inventory.setItem(i, items.getStackInSlot(i));
        }
        Containers.dropContents(this.level, this.worldPosition, inventory);
    }
}