package org.confluence.terraentity.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import org.confluence.terraentity.TerraEntity;

public class TETags {
    public static class Blocks {



    }

    public static class Items {
        public static final TagKey<Item> HONEY_TRANSLATION_BUCKET = registerItem("honey_translation_with_bucket");
        public static final TagKey<Item> HONEY_TRANSLATION = registerItem("honey_translation");
        public static final TagKey<Item> HONEY_TRANSLATION_NOT_CONSUMED = registerItem("honey_translation_not_consumed");

        public static final TagKey<Item> WHIP_ENCHANTABLE = registerItem("whip_enchantable");
        public static final TagKey<Item> BOOMERANG_ENCHANTABLE = registerItem("boomerang_enchantable");

        public static final TagKey<Item> WEAPONS = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", "weapons"));
    }

    public static class EntityTypes {
        public static final TagKey<EntityType<?>> SLIME = registerEntityType("slime");

    }

    public static class DamageTypes {
        // 玩家召唤伤害 如鞭子
        public static final ResourceKey<DamageType> SUMMON = registerDamageType("summon");
        // 召唤物召唤伤害 用于标记伤害增伤
        public static final ResourceKey<DamageType> SUMMONER = registerDamageType("summoner");
        public static final ResourceKey<DamageType> FROST_BURN = registerDamageType("frost_burn");



        public static DamageSource of(Level level, ResourceKey<DamageType> key) {
            return of(level, key, null, null);
        }

        public static DamageSource of(Level level, ResourceKey<DamageType> key, Entity causing) {
            return of(level, key, causing, causing);
        }

        public static DamageSource of(Level level, ResourceKey<DamageType> key, Entity causing, Entity direct) {
            return new DamageSource(level.registryAccess().registry(Registries.DAMAGE_TYPE).orElseThrow().getHolderOrThrow(key), causing, direct);
        }

        public static void createDamageTypes(BootstrapContext<DamageType> context) {
            context.register(SUMMON, new DamageType("summon_damage_type", 0.1F));
            context.register(SUMMONER, new DamageType("summoner_damage_type", 0.1F));
            context.register(FROST_BURN, new DamageType("frost_burn_damage_type", 0.1F));
        }
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
}
