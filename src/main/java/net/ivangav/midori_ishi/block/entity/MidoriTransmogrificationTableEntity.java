package net.ivangav.midori_ishi.block.entity;

import net.ivangav.midori_ishi.item.ModItems;
import net.ivangav.midori_ishi.screen.MidoriTransmogrificationTableMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.obj.ObjLoader;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

import static net.ivangav.midori_ishi.item.custom.MidoriVialItem.NBT_TAG_NAME;
import static net.minecraftforge.common.capabilities.ForgeCapabilities.ITEM_HANDLER;

public class MidoriTransmogrificationTableEntity extends BlockEntity implements MenuProvider {
    private static final int INGREDIENT_SLOT = 2;
    private static final int FUEL_SLOT = 3;
    public static final int DATA_BREW_TIME = 0;
    public static final int DATA_FUEL_USES = 1;
    public static final int NUM_DATA_VALUES = 2;
    public static final int BREW_DURATION = 200; //how long 1 brewing session lasts; 20=1sec
    public static final int MAX_FUEL = 32;
    public static final int FUEL_PER_REFUEL = 1;
    public static final String[] ILLEGAL_NBT_TAGS = {"Items", "LootTable", "Inventory", "inventorySlots"};
    /** The items currently placed in the slots of the brewing stand. */
    private final ItemStackHandler items = new ItemStackHandler(4);
    private Item ingredient = Items.AIR;

    private LazyOptional<IItemHandler> handler = LazyOptional.empty();

    protected final ContainerData dataAccess;
    int brewTime;
    int fuel;

    public MidoriTransmogrificationTableEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.MIDORI_TRANSMOGRIFICATION_TABLE_ENTITY.get(), pos, state);
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
        return Component.translatable("block.midori_ishi.midori_transmogrification_table");
    }

    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new MidoriTransmogrificationTableMenu(pContainerId, pPlayerInventory, this, this.dataAccess);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        handler.invalidate();
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> capability, Direction facing) {
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
        pTag.put("Inventory", items.serializeNBT());
        pTag.putByte("Fuel", (byte)fuel);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
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
            if(fuel >= mGetIngredientCost()) {
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
        }
//        else {
//            mResetProgress();
//        }
    }

    private int mGetIngredientCost() {
        Rarity rarity = ingredient.getRarity(items.getStackInSlot(INGREDIENT_SLOT));
        return switch(rarity) {
            case COMMON -> 16;
            case UNCOMMON -> 20;
            case RARE -> 28;
            case EPIC -> 32;
        };
    }

    private void mCraftItem() {
        ItemStack infusedItem = items.getStackInSlot(INGREDIENT_SLOT);
        ItemStack new1 = infusedItem.copy(); new1.setCount(1);
        ItemStack new2 = infusedItem.copy(); new2.setCount(1);
        //slots are empty
        items.insertItem(0, new1, false);
        if(infusedItem.is(Items.MUSIC_DISC_CAT))
            items.insertItem(1, new ItemStack(ModItems.MAXWELL.get()), false);
        else
            items.insertItem(1, new2, false);
        items.getStackInSlot(INGREDIENT_SLOT).shrink(1);
        fuel -= mGetIngredientCost();
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
        if(ingredient.isEmpty()) return false;
//        System.out.println("--got to checking tags");
//        System.out.println("hasTag = " + ingredient.hasTag());
//        if(ingredient.hasTag()) {
//            System.out.println("getTag = " + ingredient.getTag());
//        }
        if(ingredient.hasTag()) {
            CompoundTag tag = ingredient.getTag();
            CompoundTag beTag = BlockItem.getBlockEntityData(ingredient);
            for (String s : ILLEGAL_NBT_TAGS) {
                //if item tags or "BlockEntityTag" list contain an illegal tag (aka implying they have an inventory), reject the recipe
                if (tag.contains(s) || (beTag != null && beTag.contains(s)))
                    return false;
            }
        }
        ItemStack out1 = items.getStackInSlot(0);
        ItemStack out2 = items.getStackInSlot(1);
        //if output has correct items and a new item would fit
        return  (out1.isEmpty() || (out1.is(ingredient.getItem()) && (out1.getMaxStackSize() >= out1.getCount()+1))) &&
                (out2.isEmpty() || (out2.is(ingredient.getItem()) && (out2.getMaxStackSize() >= out2.getCount()+1)));
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
        //checking for MIDORI_ISHI is not strictly nessesary, as it will be enforced with inventory slot restrictions
        while (fuel+FUEL_PER_REFUEL <= MAX_FUEL && fuel_stack.is(ModItems.MIDORI_ISHI.get())) {
            fuel += FUEL_PER_REFUEL;
            fuel_stack.shrink(1);
        }
    }

    //return slots which are currently water bottles
    private boolean[] getFullPositions() {
        boolean[] slots = new boolean[2];
        for(int i = 0; i < 2; ++i) {
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