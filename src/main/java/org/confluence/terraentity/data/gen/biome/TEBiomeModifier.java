package org.confluence.terraentity.data.gen.biome;

import com.mojang.serialization.Lifecycle;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.data.biome.ExtendedAddSpawnsBiomeModifier;
import org.confluence.terraentity.init.entity.TENpcEntities;


public class TEBiomeModifier {

    public static void createBiomeModifier(BootstrapContext<BiomeModifier> context) {
        HolderGetter<Biome> biomeLookup = context.lookup(Registries.BIOME);
        HolderGetter<PlacedFeature> placedFeatureLookup = context.lookup(Registries.PLACED_FEATURE);


        register(context, TENpcEntities.GUIDE,
                biomeLookup.getOrThrow(BiomeTags.IS_FOREST),
                HolderSet.direct(), 2,1,1);
        register(context, TENpcEntities.DEMOLITIONIST,
                HolderSet.direct(biomeLookup.getOrThrow(Biomes.DRIPSTONE_CAVES), biomeLookup.getOrThrow(Biomes.LUSH_CAVES),biomeLookup.getOrThrow(Biomes.NETHER_WASTES)),
                HolderSet.direct(), 2,1,1);
        register(context, TENpcEntities.GOBLIN_TINKERER,
                HolderSet.direct(biomeLookup.getOrThrow(Biomes.DRIPSTONE_CAVES), biomeLookup.getOrThrow(Biomes.LUSH_CAVES),biomeLookup.getOrThrow(Biomes.NETHER_WASTES)),
                HolderSet.direct(), 2,1,1);
        register(context, TENpcEntities.ARMS_DEALER,
                biomeLookup.getOrThrow(BiomeTags.HAS_VILLAGE_DESERT),
                HolderSet.direct(), 2,1,1);
        register(context, TENpcEntities.NURSE,
                HolderSet.direct(biomeLookup.getOrThrow(Biomes.CHERRY_GROVE)),
                HolderSet.direct(), 2,1,1);
        register(context, TENpcEntities.MERCHANT,
                HolderSet.direct(biomeLookup.getOrThrow(Biomes.FOREST)),
                HolderSet.direct(), 2,1,1);
        register(context, TENpcEntities.PAINTER,
                biomeLookup.getOrThrow(BiomeTags.IS_JUNGLE),
                HolderSet.direct(), 2,1,1);
        register(context, TENpcEntities.ANGLER,
                biomeLookup.getOrThrow(BiomeTags.IS_RIVER),
                HolderSet.direct(), 2,1,1);
        register(context, TENpcEntities.DRYAD,
                biomeLookup.getOrThrow(BiomeTags.IS_JUNGLE),
                HolderSet.direct(), 2,1,1);
        register(context, TENpcEntities.DYE_TRADER,
                biomeLookup.getOrThrow(BiomeTags.HAS_VILLAGE_DESERT),
                HolderSet.direct(), 2,1,1);


    }

    private static  ResourceKey<BiomeModifier>  createModifierKey(DeferredHolder<EntityType<?>,?> entityType) {return ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, TerraEntity.fromSpaceAndPath(entityType.getId().getNamespace(),"mob_spawner/" + entityType.getId().getPath()));}

    private static Holder.Reference<BiomeModifier> register(BootstrapContext<BiomeModifier> context, ResourceKey<BiomeModifier> key, BiomeModifier value) {
        return context.register(key, value, Lifecycle.stable());
    }
    private static Holder.Reference<BiomeModifier> register(BootstrapContext<BiomeModifier> context, DeferredHolder<EntityType<?>,?> entityType, HolderSet<Biome> biomes, HolderSet<Biome> excludedBiomes, int weight, int minCount, int maxCount) {
        return register(context, createModifierKey(entityType), ExtendedAddSpawnsBiomeModifier.singleSpawn(biomes, excludedBiomes, new ExtendedAddSpawnsBiomeModifier.ExtendedSpawnData(entityType.get(), weight, minCount, maxCount, entityType.get().getCategory())));
    }
}
