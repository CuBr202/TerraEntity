package org.confluence.terraentity.data.gen.biome;

import com.mojang.serialization.Lifecycle;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ForgeBiomeModifiers;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.init.TETags;
import org.confluence.terraentity.init.entity.TEMonsterEntities;
import org.confluence.terraentity.init.entity.TENpcEntities;

import java.util.List;


public class TEBiomeModifier {

    public static void createBiomeModifier(BootstapContext<BiomeModifier> context) {
        HolderGetter<Biome> biomeLookup = context.lookup(Registries.BIOME);
        HolderGetter<PlacedFeature> placedFeatureLookup = context.lookup(Registries.PLACED_FEATURE);

        int minute = 20 * 60;

        register(context, TENpcEntities.GUIDE,
                biomeLookup.getOrThrow(BiomeTags.IS_FOREST),
                HolderSet.direct(), minute * 5,1);
        register(context, TENpcEntities.DEMOLITIONIST,
                HolderSet.direct(biomeLookup.getOrThrow(Biomes.DRIPSTONE_CAVES), biomeLookup.getOrThrow(Biomes.LUSH_CAVES),biomeLookup.getOrThrow(Biomes.NETHER_WASTES)),
                HolderSet.direct(), minute * 5,1);
        register(context, TENpcEntities.GOBLIN_TINKERER,
                HolderSet.direct(biomeLookup.getOrThrow(Biomes.DRIPSTONE_CAVES), biomeLookup.getOrThrow(Biomes.LUSH_CAVES),biomeLookup.getOrThrow(Biomes.NETHER_WASTES)),
                HolderSet.direct(), minute * 5,1);
        register(context, TENpcEntities.ARMS_DEALER,
                biomeLookup.getOrThrow(BiomeTags.HAS_VILLAGE_DESERT),
                HolderSet.direct(), minute * 5,1);
        register(context, TENpcEntities.NURSE,
                HolderSet.direct(biomeLookup.getOrThrow(Biomes.CHERRY_GROVE)),
                HolderSet.direct(), minute * 5,1);
        register(context, TENpcEntities.MERCHANT,
                HolderSet.direct(biomeLookup.getOrThrow(Biomes.FOREST)),
                HolderSet.direct(), minute * 5,1);
        register(context, TENpcEntities.PAINTER,
                biomeLookup.getOrThrow(BiomeTags.IS_JUNGLE),
                HolderSet.direct(), minute * 5,1);
        register(context, TENpcEntities.ANGLER,
                biomeLookup.getOrThrow(BiomeTags.IS_RIVER),
                HolderSet.direct(), minute * 5,1);
        register(context, TENpcEntities.DRYAD,
                biomeLookup.getOrThrow(BiomeTags.IS_JUNGLE),
                HolderSet.direct(), minute * 5,1);
        register(context, TENpcEntities.DYE_TRADER,
                biomeLookup.getOrThrow(BiomeTags.HAS_VILLAGE_DESERT),
                HolderSet.direct(), minute * 5,1);
        register(context, TENpcEntities.WITCH_DOCTOR,
                biomeLookup.getOrThrow(BiomeTags.IS_JUNGLE),
                HolderSet.direct(), minute * 5,1);
        register(context, TENpcEntities.TRUFFLE,
                HolderSet.direct(biomeLookup.getOrThrow(Biomes.MUSHROOM_FIELDS)),
                HolderSet.direct(), minute * 5,1);
        register(context, TENpcEntities.CLOTHIER,
                biomeLookup.getOrThrow(BiomeTags.HAS_VILLAGE_PLAINS),
                HolderSet.direct(), minute * 5,1);
        register(context, TENpcEntities.TRAVELING_MERCHANT,
                biomeLookup.getOrThrow(TETags.Biomes.IS_EVER_WHERE),
                HolderSet.direct(), minute * 5,1);
        register(context, TENpcEntities.PARTY_GIRL,
                HolderSet.direct(biomeLookup.getOrThrow(Biomes.BEACH)),
                HolderSet.direct(), minute * 5,1);

        register(context, createModifierKey("goblin_arm"), new ForgeBiomeModifiers.AddSpawnsBiomeModifier(
                HolderSet.direct(biomeLookup.get(Biomes.PLAINS).get()), List.of(
                new MobSpawnSettings.SpawnerData(TEMonsterEntities.GOBLIN_WARRIOR.get(), 20,1,2),
                new MobSpawnSettings.SpawnerData(TEMonsterEntities.GOBLIN_ARCHER.get(), 20,2,3),
                new MobSpawnSettings.SpawnerData(TEMonsterEntities.GOBLIN_PEON.get(), 20,2,3),
                new MobSpawnSettings.SpawnerData(TEMonsterEntities.GOBLIN_THIEF.get(), 20,1,1),
                new MobSpawnSettings.SpawnerData(TEMonsterEntities.GOBLIN_SORCERER.get(), 20,1,1)
        )));
        register(context, createModifierKey("skeleton_arm"), new ForgeBiomeModifiers.AddSpawnsBiomeModifier(
                biomeLookup.getOrThrow(BiomeTags.IS_OVERWORLD), List.of(
                new MobSpawnSettings.SpawnerData(TEMonsterEntities.BIG_HELMET_ANGER_BONES.get(), 40,1,1),
                new MobSpawnSettings.SpawnerData(TEMonsterEntities.BIG_MUSCLE_ANGER_BONES.get(), 40,1,1),
                new MobSpawnSettings.SpawnerData(TEMonsterEntities.BIG_BONES.get(), 90,1,2),
                new MobSpawnSettings.SpawnerData(TEMonsterEntities.SHORT_BONES.get(), 90,1,2),
                new MobSpawnSettings.SpawnerData(TEMonsterEntities.ANGER_BONES.get(), 90,1,2),
                new MobSpawnSettings.SpawnerData(TEMonsterEntities.BIG_ANGER_BONES.get(), 70,1,2),
                new MobSpawnSettings.SpawnerData(TEMonsterEntities.CURSED_SKULL.get(), 30,1,1)
        )));

    }


    private static <T extends Entity>  ResourceKey<BiomeModifier>  createModifierKey(RegistryObject<EntityType<T>> entityType) {return ResourceKey.create(ForgeRegistries.Keys.BIOME_MODIFIERS, TerraEntity.fromSpaceAndPath(entityType.getId().getNamespace(),"mob_spawner/" + entityType.getId().getPath()));}
    private static  ResourceKey<BiomeModifier>  createModifierKey(String name) {return ResourceKey.create(ForgeRegistries.Keys.BIOME_MODIFIERS, TerraEntity.space("mob_spawner/" + name));}

    private static <T extends Entity>Holder.Reference<BiomeModifier> register(BootstapContext<BiomeModifier> context, ResourceKey<BiomeModifier> key, BiomeModifier value) {
        return context.register(key, value, Lifecycle.stable());
    }
    private static <T extends Entity>Holder.Reference<BiomeModifier> register(BootstapContext<BiomeModifier> context, RegistryObject<EntityType<T>> entityType, HolderSet<Biome> biomes, HolderSet<Biome> excludedBiomes, int weight, int minCount, int maxCount) {
        return register(context, createModifierKey(entityType), ExtendedAddSpawnsBiomeModifier.singleSpawn(biomes, excludedBiomes, new ExtendedAddSpawnsBiomeModifier.ExtendedSpawnData(entityType.get(), weight, minCount, maxCount, entityType.get().getCategory())));
    }
    private static <T extends Entity>Holder.Reference<BiomeModifier> register(BootstapContext<BiomeModifier> context, RegistryObject<EntityType<T>> entityType, HolderSet<Biome> biomes, HolderSet<Biome> excludedBiomes, int delay, int weight) {
        return register(context, createModifierKey(entityType), new NPCAddSpawnsBiomeModifier(biomes, excludedBiomes, entityType.get(), delay, weight));
    }
    private static <T extends Entity>Holder.Reference<BiomeModifier> register(BootstapContext<BiomeModifier> context, RegistryObject<EntityType<T>> entityType, HolderSet<Biome> biomes, int delay) {
        return register(context, createModifierKey(entityType), new NPCAddSpawnsBiomeModifier(biomes, HolderSet.direct(), entityType.get(), delay, 1));
    }
}
