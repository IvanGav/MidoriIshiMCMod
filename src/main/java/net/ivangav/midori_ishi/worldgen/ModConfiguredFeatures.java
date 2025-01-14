package net.ivangav.midori_ishi.worldgen;

import net.ivangav.midori_ishi.MidoriIshiMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;

import net.ivangav.midori_ishi.block.ModBlocks;

import java.util.List;

public class ModConfiguredFeatures {
    public static final int MIDORI_ISHI_VEIN_SIZE = 16;
    public static final ResourceKey<ConfiguredFeature<?, ?>> OVERWORLD_MIDORI_ISHI_ORE_KEY = registerKey("midori_ishi_ore");

    public static void bootstrap(BootstapContext<ConfiguredFeature<?, ?>> context) {
        RuleTest stoneReplaceable = new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES);
        RuleTest deepslateReplaceables = new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES);

        List<OreConfiguration.TargetBlockState> overworldMidoriIshiOres = List.of(
                OreConfiguration.target(stoneReplaceable, ModBlocks.MIDORI_ISHI_ORE.get().defaultBlockState()),
                OreConfiguration.target(deepslateReplaceables, ModBlocks.DEEPSLATE_MIDORI_ISHI_ORE.get().defaultBlockState())
        );

        register(context, OVERWORLD_MIDORI_ISHI_ORE_KEY, Feature.ORE, new OreConfiguration(overworldMidoriIshiOres, MIDORI_ISHI_VEIN_SIZE));
    }


    public static ResourceKey<ConfiguredFeature<?, ?>> registerKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation(MidoriIshiMod.MOD_ID, name));
    }

    private static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(BootstapContext<ConfiguredFeature<?, ?>> context,
                                                                                          ResourceKey<ConfiguredFeature<?, ?>> key, F feature, FC configuration) {
        context.register(key, new ConfiguredFeature<>(feature, configuration));
    }
}
