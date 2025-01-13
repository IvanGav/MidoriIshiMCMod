package net.ivangav.midori_ishi.block;

import net.ivangav.midori_ishi.MidoriIshiMod;
import net.ivangav.midori_ishi.block.custom.MidoriInfusionTableBlock;
import net.ivangav.midori_ishi.item.ModItems;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, MidoriIshiMod.MOD_ID);

    //start listing blocks

    public static final RegistryObject<Block> MIDORI_ISHI_ORE = registerBlock("midori_ishi_ore",
            ()->new DropExperienceBlock(
                    BlockBehaviour.Properties.copy(Blocks.DIAMOND_ORE).sound(SoundType.AMETHYST),
                    UniformInt.of(4,10)
            )
    );
    public static final RegistryObject<Block> MIDORI_INFUSION_TABLE = registerBlock("midori_infusion_table",
            ()->new MidoriInfusionTableBlock(BlockBehaviour.Properties.copy(Blocks.BREWING_STAND))
    );

    //end listing blocks

    public static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block) {
        return ModItems.ITEMS.register(name, ()->new BlockItem(block.get(), new Item.Properties()));
    }

    public static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    public static void register(IEventBus ebus) {
        BLOCKS.register(ebus);
    }
}
