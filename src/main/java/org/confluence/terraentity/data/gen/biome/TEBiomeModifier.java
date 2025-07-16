package org.confluence.terraentity.data.gen.biome;

import com.mojang.serialization.Lifecycle;
import net.minecraft.core.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.placement.CavePlacements;
import net.minecraft.data.worldgen.placement.TreePlacements;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.BiomeModifiers;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.data.biome.ExtendedAddSpawnsBiomeModifier;
import org.confluence.terraentity.init.TEEntities;
import org.confluence.terraentity.init.TETags;
import org.confluence.terraentity.init.entity.TEMonsterEntities;
import org.confluence.terraentity.init.entity.TENpcEntities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Stream;


public class TEBiomeModifier {

    public static void createBiomeModifier(BootstrapContext<BiomeModifier> context) {
        HolderGetter<Biome> biomeLookup = context.lookup(Registries.BIOME);
        HolderGetter<PlacedFeature> placedFeatureLookup = context.lookup(Registries.PLACED_FEATURE);





    }

    private static  ResourceKey<BiomeModifier>  createModifierKey(DeferredHolder<EntityType<?>,?> entityType) {return ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, TerraEntity.fromSpaceAndPath(entityType.getId().getNamespace(),"mob_spawner/" + entityType.getId().getPath()));}
    private static  ResourceKey<BiomeModifier>  createModifierKey(String name) {return ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, TerraEntity.space("mob_spawner/" + name));}

    private static Holder.Reference<BiomeModifier> register(BootstrapContext<BiomeModifier> context, ResourceKey<BiomeModifier> key, BiomeModifier value) {
        return context.register(key, value, Lifecycle.stable());
    }
    private static Holder.Reference<BiomeModifier> register(BootstrapContext<BiomeModifier> context, DeferredHolder<EntityType<?>,?> entityType, HolderSet<Biome> biomes, HolderSet<Biome> excludedBiomes, int weight, int minCount, int maxCount) {
        return register(context, createModifierKey(entityType), ExtendedAddSpawnsBiomeModifier.singleSpawn(biomes, excludedBiomes, new ExtendedAddSpawnsBiomeModifier.ExtendedSpawnData(entityType.get(), weight, minCount, maxCount, entityType.get().getCategory())));
    }

    private HolderSet.Direct<Biome> getHolderSet(HolderGetter<Biome> biomeLookup, ResourceKey<Biome>... biomeNames){
        return HolderSet.direct(Arrays.stream(biomeNames).map(biomeLookup::getOrThrow).toList());
    }



}
