package org.confluence.terraentity.init;

import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.effect.harmful.DemonicThoughtsEffect;
import org.confluence.terraentity.effect.harmful.FrostburnEffect;
import org.confluence.terraentity.effect.harmful.HellFireEffect;
import org.confluence.terraentity.effect.harmful.SummonFocusEffect;

public class TEEffects {

    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, TerraEntity.MODID);

    public static final RegistryObject<DemonicThoughtsEffect> DEMONIC_THOUGHTS = EFFECTS.register("demonic_thoughts", DemonicThoughtsEffect::new);
    public static final RegistryObject<SummonFocusEffect> SUMMON_FOCUS = EFFECTS.register("summon_mark", SummonFocusEffect::new);
    public static final RegistryObject<MobEffect> FROST_BURN = EFFECTS.register("frost_burn", FrostburnEffect::new);
    public static final RegistryObject<MobEffect> HELLFIRE = EFFECTS.register("hellfire", HellFireEffect::new);


}
