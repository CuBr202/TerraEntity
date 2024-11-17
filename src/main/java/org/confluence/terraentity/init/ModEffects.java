package org.confluence.terraentity.init;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.effect.harmful.DemonicThoughtsEffect;

public class ModEffects {
    public static final DeferredRegister<MobEffect> EFFECTS =
            DeferredRegister.create(BuiltInRegistries.MOB_EFFECT, TerraEntity.MODID);

    public static final DeferredHolder<MobEffect, DemonicThoughtsEffect> DEMONIC_THOUGHTS =
            EFFECTS.register("demonic_thoughts", DemonicThoughtsEffect::new);
}
