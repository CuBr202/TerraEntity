package org.confluence.terraentity.init;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

import static org.confluence.terraentity.TerraEntity.MODID;

public class ModItems {
    public static DeferredRegister.Items SPAWN_EGGS = DeferredRegister.createItems(MODID);
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);




    public static final DeferredItem<Item> BLUE_SLIME_SPAWN_EGG = register("blue_slime_spawn_egg", ModEntities.BLUE_SLIME, 0x73bcf4, 0x466CBE);
    public static final DeferredItem<Item> PURPLE_SLIME_SPAWN_EGG = register("purple_slime_spawn_egg", ModEntities.PURPLE_SLIME, 0xf334f8, 0xA246BE);
    public static final DeferredItem<Item> GREEN_SLIME_SPAWN_EGG = register("green_slime_spawn_egg", ModEntities.GREEN_SLIME, 0xa2f89f, 0x3de838);
    public static final DeferredItem<Item> RED_SLIME_SPAWN_EGG = register("red_slime_spawn_egg", ModEntities.RED_SLIME, 0xf83434, 0xA51E1E);
    public static final DeferredItem<Item> YELLOW_SLIME_SPAWN_EGG = register("yellow_slime_spawn_egg", ModEntities.YELLOW_SLIME, 0xf8e234, 0xd19519);
    public static final DeferredItem<Item> HONEY_SLIME_SPAWN_EGG = register("honey_slime_spawn_egg", ModEntities.HONEY_SLIME, 0xfed167, 0xfcd58c);
    public static final DeferredItem<Item> BLACK_SLIME_SPAWN_EGG = register("black_slime_spawn_egg", ModEntities.BLACK_SLIME, 0x7E7E7E, 0x373535);
    public static final DeferredItem<Item> PINK_SLIME_SPAWN_EGG = register("pink_slime_spawn_egg", ModEntities.PINK_SLIME, 0xFF87B3, 0xf89fe3);
    public static final DeferredItem<Item> DESERT_SLIME_SPAWN_EGG = register("desert_slime_spawn_egg", ModEntities.DESERT_SLIME, 0xDCC59a, 0xC7AB5E);
    public static final DeferredItem<Item> JUNGLE_SLIME_SPAWN_EGG = register("jungle_slime_spawn_egg", ModEntities.JUNGLE_SLIME, 0x9ae920, 0xC7AB5E);
    public static final DeferredItem<Item> ICE_SLIME_SPAWN_EGG = register("ice_slime_spawn_egg", ModEntities.ICE_SLIME, 0xB3F0EA, 0x7FDEDF);
    public static final DeferredItem<Item> LAVA_SLIME_SPAWN_EGG = register("lava_slime_spawn_egg", ModEntities.LAVA_SLIME, 0xFFB150, 0xC45737);
    public static final DeferredItem<Item> CRIMSON_SLIME_SPAWN_EGG = register("crimson_slime_spawn_egg", ModEntities.CRIMSON_SLIME, 0x8B4949, 0x7D1D1D);
    public static final DeferredItem<Item> TROPIC_SLIME_SPAWN_EGG = register("tropic_slime_spawn_egg", ModEntities.TROPIC_SLIME, 0x73bcf4, 0x7374f4);
    public static final DeferredItem<Item> LUMINOUS_SLIME_SPAWN_EGG = register("evil_slime_spawn_egg", ModEntities.LUMINOUS_SLIME, 0xFF00FF, 0xEDFFFA);
    public static final DeferredItem<Item> DEMON_EYE_SPAWN_EGG = register("demon_eye_spawn_egg", ModEntities.DEMON_EYE, 0xffffff, 0xab0d0d);
    public static final DeferredItem<Item> BLOOD_CRAWLER_SPAWN_EGG = register("blood_crawler_spawn_egg", ModEntities.BLOOD_CRAWLER, 0xf2d4ca, 0xa75049);
    public static final DeferredItem<Item> BLOODY_SPORE_SPAWN_EGG = register("bloody_spore_spawn_egg", ModEntities.BLOODY_SPORE, 0xa75049, 0x65292c);

    public static final DeferredItem<Item> CRIMSON_KEMERA_EGG = register("crimson_kemera_egg", ModEntities.SIMPLE_MONSTER, 0xa75049, 0x65292c);


    public static final DeferredItem<Item> KING_SLIME_SPAWN_EGG = register("king_slime_spawn_egg", ModEntities.KING_SLIME, 0x73bcf4, 0xf8e234);
    public static final DeferredItem<Item> CTHULHU_EYE_SPAWN_EGG = register("cthulhu_eye_spawn_egg", ModEntities.CTHULHU_EYE, 0xffffff, 0xab0d0d);


    public static DeferredItem<Item> register(String name, Supplier<? extends EntityType<? extends Mob>>  entityType, int primaryColor, int secondaryColor){


        return SPAWN_EGGS.register(name, () -> new DeferredSpawnEggItem(entityType, primaryColor, secondaryColor,new Item.Properties()));
    }

    public static final DeferredHolder<CreativeModeTab,CreativeModeTab> NEO_TERRA =
            TABS.register(MODID + "_tab", ()-> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.terraentity.title"))
                    .icon(()-> ModItems.KING_SLIME_SPAWN_EGG.asItem().getDefaultInstance())
                    .displayItems((itemDisplayParameters, output) -> {
                        SPAWN_EGGS.getEntries().forEach(item -> output.accept(item.get()));
                    })
                    .build());

}
