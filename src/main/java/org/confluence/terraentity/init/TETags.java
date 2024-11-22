package org.confluence.terraentity.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;
import org.confluence.terraentity.TerraEntity;

public class TETags {
    public static class Blocks {
        public static final TagKey<Block> CAN_BALL_REPLACED =
            tag("can_ball_replaced");



        private static TagKey<Block> tag(String name) {
            return BlockTags.create(TerraEntity.space(name));
        }


    }

    public static class Items {
        public static final TagKey<Item> HONEY_TRANSLATION_BUCKET = registerItem("honey_translation_with_bucket");
        public static final TagKey<Item> HONEY_TRANSLATION = registerItem("honey_translation");
        public static final TagKey<Item> HONEY_TRANSLATION_NOT_CONSUMED = registerItem("honey_translation_not_consumed");
        private static TagKey<Item> tag(String name) {
            return ItemTags.create(TerraEntity.space(name));
        }
    }

    public static class EntityTypes {
        public static final TagKey<EntityType<?>> SLIME = registerEntityType("slime");
    }

    private static TagKey<Item> registerItem(String id) {
        return ItemTags.create(TerraEntity.asResource(id));
    }
    private static TagKey<EntityType<?>> registerEntityType(String id) {
        return TagKey.create(Registries.ENTITY_TYPE, TerraEntity.asResource(id));
    }

}
