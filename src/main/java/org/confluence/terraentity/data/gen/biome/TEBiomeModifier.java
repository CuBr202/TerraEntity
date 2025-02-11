package org.confluence.terraentity.data.gen.biome;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.init.TEEntities;


public class TEBiomeModifier {
    private static ResourceKey<BiomeModifier> createModifierKey(String name) {return ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, TerraEntity.space(name));}

    public static void createBiomeModifier(BootstrapContext<BiomeModifier> context) {
        HolderGetter<Biome> biomeLookup = context.lookup(Registries.BIOME);
        HolderGetter<PlacedFeature> placedFeatureLookup = context.lookup(Registries.PLACED_FEATURE);

//        context.register(createModifierKey("spawn/devourer_spawns"),
//                ExtendedAddSpawnsBiomeModifier.singleSpawn(
//                        HolderSet.direct(
//                                biomeLookup.getOrThrow(Biomes.DRIPSTONE_CAVES),
//                                biomeLookup.getOrThrow(Biomes.LUSH_CAVES)),
//                        HolderSet.direct(),
//                        new ExtendedAddSpawnsBiomeModifier.ExtendedSpawnData(TEEntities.DEVOURER.get(),
//                                20, 1, 1,
//                                TEEntities.DEVOURER.get().getCategory())
//                )
//        );



    }
}
