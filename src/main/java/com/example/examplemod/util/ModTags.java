package com.example.examplemod.util;

import com.example.examplemod.MidoriIshiMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class ModTags {
    public static class Blocks {
//        public static final TagKey<Block> MIDORI_SCANNER_VALUABLES = tag("midori_scanner_valuables");

        public static TagKey<Block> tag(String name) {
            return BlockTags.create(new ResourceLocation(MidoriIshiMod.MOD_ID, name));
        }
    }

    public static class Items {
        public static TagKey<Item> tag(String name) {
            return ItemTags.create(new ResourceLocation(MidoriIshiMod.MOD_ID, name));
        }
    }
}
