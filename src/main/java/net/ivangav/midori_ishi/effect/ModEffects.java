package net.ivangav.midori_ishi.effect;

import net.ivangav.midori_ishi.MidoriIshiMod;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEffects {
    private static final String uuid1 = "bf30adaa-a0c5-4e3f-8c47-6cc69f2f586b";
    public static final DeferredRegister<MobEffect> MOB_EFFECTS =
            DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, MidoriIshiMod.MOD_ID);

    public static final RegistryObject<MobEffect> MIDAS_TOUCH_EFFECT = MOB_EFFECTS.register("midas_touch",
            ()->new MidasTouchEffect(MobEffectCategory.NEUTRAL, 0));

    public static final RegistryObject<MobEffect> GREATER_WITHER_EFFECT = MOB_EFFECTS.register("greater_wither",
            ()->new GreaterWitherEffect(MobEffectCategory.HARMFUL, 0));
//                    .addAttributeModifier(
//                    Attributes.JUMP_STRENGTH,
//                    uuid1,
////                    String.valueOf(ResourceLocation.fromNamespaceAndPath(MidoriIshiMod.MOD_ID, "greater_wither")),
//                    10f,
//                    AttributeModifier.Operation.ADDITION
//            )
//    );

    public static final RegistryObject<MobEffect> SPIDER_EFFECT = MOB_EFFECTS.register("spider",
        ()->new SpiderEffect(MobEffectCategory.NEUTRAL, 0));

    public static final RegistryObject<MobEffect> TELEPORT_DISEASE_EFFECT = MOB_EFFECTS.register("teleport_disease",
            ()->new TeleportDiseaseEffect(MobEffectCategory.HARMFUL, 0));

    public static void register(IEventBus ebus) {
        MOB_EFFECTS.register(ebus);
    }
}

