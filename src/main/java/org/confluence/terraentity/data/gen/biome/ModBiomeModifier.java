package org.confluence.terraentity.data.gen.biome;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.registries.ForgeRegistries;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.init.TEEntities;


public class ModBiomeModifier {
    private static ResourceKey<BiomeModifier> createModifierKey(String name) {return ResourceKey.create(ForgeRegistries.Keys.BIOME_MODIFIERS, TerraEntity.space(name));}

    public static final ResourceKey<BiomeModifier> DEVOURER_SPAWN = createModifierKey("spawn/devourer_spawns");
    public static final ResourceKey<BiomeModifier> GIANT_SHELLY_SPAWN = createModifierKey("spawn/giant_shelly_spawns");
//    public static final ResourceKey<BiomeModifier> IRON_BUCKET_ZOMBIE_SPAWN = createModifierKey("spawn/iron_bucket_zombie_spawns");
//    public static final ResourceKey<BiomeModifier> CRAZY_DAVE_SPAWN = createModifierKey("spawn/crazy_dave_spawns");


    public static void createBiomeModifier(BootstapContext<BiomeModifier> context) {
        HolderGetter<Biome> biomeLookup = context.lookup(Registries.BIOME);
        HolderGetter<PlacedFeature> placedFeatureLookup = context.lookup(Registries.PLACED_FEATURE);

        context.register(DEVOURER_SPAWN, ExtendedAddSpawnsBiomeModifier.singleSpawn(
                biomeLookup.getOrThrow(BiomeTags.IS_OVERWORLD),
                biomeLookup.getOrThrow(BiomeTags.IS_END),
                new ExtendedAddSpawnsBiomeModifier.ExtendedSpawnData(TEEntities.DEVOURER.get(),
                        20, 1, 1,
                        TEEntities.DEVOURER.get().getCategory())
                )
        );

//        context.register(GIANT_SHELLY_SPAWN, ExtendedAddSpawnsBiomeModifier.singleSpawn(
//                HolderSet.direct(
//                        biomeLookup.getOrThrow(Biomes.DRIPSTONE_CAVES),
//                        biomeLookup.getOrThrow(Biomes.LUSH_CAVES)),
//                HolderSet.direct(),
//                        new ExtendedAddSpawnsBiomeModifier.ExtendedSpawnData(TEEntities.GIANT_SHELLY.get(),
//                                40, 1, 1,
//                                TEEntities.GIANT_SHELLY.get().getCategory())
//                )
//        );


    }
}
