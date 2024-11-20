package org.confluence.terraentity.init;

import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
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
        public static final TagKey<Item> HONEY_TRANSLATION_BUCKET = register("honey_translation_with_bucket");
        public static final TagKey<Item> HONEY_TRANSLATION = register("honey_translation");
        public static final TagKey<Item> HONEY_TRANSLATION_NOT_CONSUMED = register("honey_translation_not_consumed");
        private static TagKey<Item> tag(String name) {
            return ItemTags.create(TerraEntity.space(name));
        }

    }

    private static TagKey<Item> register(String id) {
        return ItemTags.create(TerraEntity.asResource(id));
    }

}
