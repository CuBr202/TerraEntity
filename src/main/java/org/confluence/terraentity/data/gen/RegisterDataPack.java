package org.confluence.terraentity.data.gen;

import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.confluence.terraentity.data.gen.biome.TEBiomeModifier;
import org.confluence.terraentity.init.TETags;


public class RegisterDataPack {
    public static final RegistrySetBuilder DATA_BUILDER = new RegistrySetBuilder()
            .add(Registries.DAMAGE_TYPE, TETags.DamageTypes::createDamageTypes)
//            .add(NeoForgeRegistries.Keys.BIOME_MODIFIERS, TEBiomeModifier::createBiomeModifier)
            ;

}
