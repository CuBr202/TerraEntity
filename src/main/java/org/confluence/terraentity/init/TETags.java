package org.confluence.terraentity.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import org.confluence.terraentity.TerraEntity;

public class TETags {
    public static class Blocks {



    }

    public static class Items {
        public static final TagKey<Item> HONEY_TRANSLATION_BUCKET = registerItem("honey_translation_with_bucket");
        public static final TagKey<Item> HONEY_TRANSLATION = registerItem("honey_translation");
        public static final TagKey<Item> HONEY_TRANSLATION_NOT_CONSUMED = registerItem("honey_translation_not_consumed");

    }

    public static class EntityTypes {
        public static final TagKey<EntityType<?>> SLIME = registerEntityType("slime");

    }

    public static class DamageTypes {
        public static final ResourceKey<DamageType> SUMMONER = registerDamageType("summoner");




        public static DamageSource of(Level level, ResourceKey<DamageType> key) {
            return of(level, key, null, null);
        }

        public static DamageSource of(Level level, ResourceKey<DamageType> key, Entity causing) {
            return of(level, key, causing, causing);
        }

        public static DamageSource of(Level level, ResourceKey<DamageType> key, Entity causing, Entity direct) {
            return new DamageSource(level.registryAccess().registry(Registries.DAMAGE_TYPE).orElseThrow().getHolderOrThrow(key), causing, direct);
        }

        public static void createDamageTypes(BootstapContext<DamageType> context) {
            context.register(SUMMONER, new DamageType("summoner_damage_type", 0.1F));

        }
    }

    public static class Biomes{
//        public static final TagKey<Biome> UNDERGROUND = registerBiome("underground");


    }


    private static TagKey<Item> registerItem(String id) {
        return ItemTags.create(TerraEntity.asResource(id));
    }
    private static TagKey<EntityType<?>> registerEntityType(String id) {
        return TagKey.create(Registries.ENTITY_TYPE, TerraEntity.asResource(id));
    }
    private static ResourceKey<DamageType> registerDamageType(String id) {
        return ResourceKey.create(Registries.DAMAGE_TYPE, TerraEntity.space(id));
    }
    private static TagKey<Biome> registerBiome(String id) {
        return TagKey.create(Registries.BIOME, TerraEntity.asResource(id));
    }
}
