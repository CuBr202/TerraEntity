package org.confluence.terraentity.data.gen;

import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraftforge.registries.ForgeRegistries;
import org.confluence.terraentity.data.gen.biome.TEBiomeModifier;
import org.confluence.terraentity.init.TETags;


public class TERegisterDataPack {
    public static final RegistrySetBuilder DATA_BUILDER = new RegistrySetBuilder()
            .add(Registries.DAMAGE_TYPE, TETags.DamageTypes::createDamageTypes)
            .add(ForgeRegistries.Keys.BIOME_MODIFIERS, TEBiomeModifier::createBiomeModifier)
            ;

}
