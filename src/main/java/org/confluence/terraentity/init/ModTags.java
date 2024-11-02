package org.confluence.terraentity.init;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.confluence.terraentity.TerraEntity;

public class ModTags {
    public static class Blocks {
        public static final TagKey<Block> CAN_BALL_REPLACED =
            tag("can_ball_replaced");



        private static TagKey<Block> tag(String name) {
            return BlockTags.create(TerraEntity.space(name));
        }


    }

    public static class Items {
        public static final TagKey<Item> BOTTOMLESS_HONEY_BUCKET =
                tag("bottomless_honey_bucket");

        public static final TagKey<Item> HONEY_BUCKET =
                tag("honey_bucket");


        private static TagKey<Item> tag(String name) {
            return ItemTags.create(TerraEntity.space(name));
        }

    }

}
