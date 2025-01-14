package net.ivangav.midori_ishi.block.entity;

import net.ivangav.midori_ishi.MidoriIshiMod;
import net.ivangav.midori_ishi.block.ModBlocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, MidoriIshiMod.MOD_ID);

    //start listing blocks

    public static final RegistryObject<BlockEntityType<MidoriInfusionTableEntity>> MIDORI_INFUSION_TABLE_ENTITY = BLOCK_ENTITIES.register(
            "midori_infusion_table_entity",
            ()->BlockEntityType.Builder.of(MidoriInfusionTableEntity::new, ModBlocks.MIDORI_INFUSION_TABLE.get()).build(null)
    );

    public static final RegistryObject<BlockEntityType<MidoriTransmogrificationTableEntity>> MIDORI_TRANSMOGRIFICATION_TABLE_ENTITY = BLOCK_ENTITIES.register(
            "midori_transmogrification_table_entity",
            ()->BlockEntityType.Builder.of(MidoriTransmogrificationTableEntity::new, ModBlocks.MIDORI_TRANSMOGRIFICATION_TABLE.get()).build(null)
    );

    //end listing blocks

    public static void register(IEventBus ebus) {
        BLOCK_ENTITIES.register(ebus);
    }
}
