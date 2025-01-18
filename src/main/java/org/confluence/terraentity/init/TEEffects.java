package org.confluence.terraentity.init;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;

import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.effect.harmful.DemonicThoughtsEffect;

public class TEEffects {
    public static final DeferredRegister<MobEffect> EFFECTS =
            DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, TerraEntity.MODID);

    public static final RegistryObject<DemonicThoughtsEffect> DEMONIC_THOUGHTS =
            EFFECTS.register("demonic_thoughts", DemonicThoughtsEffect::new);
}
