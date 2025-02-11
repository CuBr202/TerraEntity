package org.confluence.terraentity.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.confluence.terraentity.ServerConfig;
import org.confluence.terraentity.item.DebugItem;
import org.confluence.terraentity.item.SummonItem;

import java.util.function.Supplier;

import static org.confluence.terraentity.TerraEntity.MODID;

public class TEItems {
    public static DeferredRegister.Items SPAWN_EGGS = DeferredRegister.createItems(MODID);
    public static DeferredRegister.Items SUMMON_ITEMS = DeferredRegister.createItems(MODID);

    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);




    public static final DeferredItem<Item> BLUE_SLIME_SPAWN_EGG = registerEgg("blue_slime_spawn_egg", TEEntities.BLUE_SLIME, 0x73bcf4, 0x466CBE);
    public static final DeferredItem<Item> PURPLE_SLIME_SPAWN_EGG = registerEgg("purple_slime_spawn_egg", TEEntities.PURPLE_SLIME, 0xf334f8, 0xA246BE);
    public static final DeferredItem<Item> GREEN_SLIME_SPAWN_EGG = registerEgg("green_slime_spawn_egg", TEEntities.GREEN_SLIME, 0xa2f89f, 0x3de838);
    public static final DeferredItem<Item> RED_SLIME_SPAWN_EGG = registerEgg("red_slime_spawn_egg", TEEntities.RED_SLIME, 0xf83434, 0xA51E1E);
    public static final DeferredItem<Item> YELLOW_SLIME_SPAWN_EGG = registerEgg("yellow_slime_spawn_egg", TEEntities.YELLOW_SLIME, 0xf8e234, 0xd19519);
    public static final DeferredItem<Item> HONEY_SLIME_SPAWN_EGG = registerEgg("honey_slime_spawn_egg", TEEntities.HONEY_SLIME, 0xffffff, 0xffffff);
    public static final DeferredItem<Item> BLACK_SLIME_SPAWN_EGG = registerEgg("black_slime_spawn_egg", TEEntities.BLACK_SLIME, 0x7E7E7E, 0x373535);
    public static final DeferredItem<Item> PINK_SLIME_SPAWN_EGG = registerEgg("pink_slime_spawn_egg", TEEntities.PINK_SLIME, 0xFF87B3, 0xf89fe3);
    public static final DeferredItem<Item> DESERT_SLIME_SPAWN_EGG = registerEgg("desert_slime_spawn_egg", TEEntities.DESERT_SLIME, 0xffffff, 0xffffff);
    public static final DeferredItem<Item> JUNGLE_SLIME_SPAWN_EGG = registerEgg("jungle_slime_spawn_egg", TEEntities.JUNGLE_SLIME, 0x9ae920, 0xC7AB5E);
    public static final DeferredItem<Item> ICE_SLIME_SPAWN_EGG = registerEgg("ice_slime_spawn_egg", TEEntities.ICE_SLIME, 0xB3F0EA, 0x7FDEDF);
    public static final DeferredItem<Item> LAVA_SLIME_SPAWN_EGG = registerEgg("lava_slime_spawn_egg", TEEntities.LAVA_SLIME, 0xffffff, 0xffffff);
    public static final DeferredItem<Item> CRIMSON_SLIME_SPAWN_EGG = registerEgg("crimson_slime_spawn_egg", TEEntities.CRIMSON_SLIME, 0x8B4949, 0x7D1D1D);
    public static final DeferredItem<Item> TROPIC_SLIME_SPAWN_EGG = registerEgg("tropic_slime_spawn_egg", TEEntities.TROPIC_SLIME, 0x73bcf4, 0x7374f4);
    public static final DeferredItem<Item> LUMINOUS_SLIME_SPAWN_EGG = registerEgg("evil_slime_spawn_egg", TEEntities.LUMINOUS_SLIME, 0xFF00FF, 0xEDFFFA);
    public static final DeferredItem<Item> DEMON_EYE_SPAWN_EGG = registerEgg("demon_eye_spawn_egg", TEEntities.DEMON_EYE, 0xffffff, 0xab0d0d);
    public static final DeferredItem<Item> BLOOD_CRAWLER_SPAWN_EGG = registerEgg("blood_crawler_spawn_egg", TEEntities.BLOOD_CRAWLER, 0xffffff, 0xffffff);
    public static final DeferredItem<Item> BLOODY_SPORE_SPAWN_EGG = registerEgg("bloody_spore_spawn_egg", TEEntities.BLOODY_SPORE, 0xffffff, 0xffffff);
    public static final DeferredItem<Item> DECAYEDER_SPAWN_EGG = registerEgg("decayeder_spawn_egg", TEEntities.DECAYEDER, 0xffffff, 0xffffff);
    public static final DeferredItem<Item> DEVOURER_SPAWN_EGG = registerEgg("devourer_spawn_egg", TEEntities.DEVOURER, 0xffffff, 0xffffff);
    public static final DeferredItem<Item> GIANT_SHELLY_SPAWN_EGG = registerEgg("giant_shelly_spawn_egg", TEEntities.GIANT_SHELLY, 0xffffff, 0xffffff);


    public static final DeferredItem<Item> FLYING_FISH_SPAWN_EGG = registerEgg("flying_fish_spawn_egg", TEEntities.FLYING_FISH, 0xffffff, 0xffffff);
    public static final DeferredItem<Item> DRIPPLER_SPAWN_EGG = registerEgg("drippler_spawn_egg", TEEntities.DRIPPLER, 0xffffff, 0xffffff);
    public static final DeferredItem<Item> BLOOD_ZOMBIE_SPAWN_EGG = registerEgg("blood_zombie_spawn_egg", TEEntities.BLOOD_ZOMBIE, 0xffffff, 0xffffff);
    public static final DeferredItem<Item> CRIMSON_KEMERA_EGG = registerEgg("crimson_kemera_egg", TEEntities.CRIMSON_KEMERA, 0xffffff, 0xffffff);
    public static final DeferredItem<Item> EATER_OF_SOULS_SPAWN_EGG = registerEgg("eater_of_souls_spawn_egg", TEEntities.EATER_OF_SOULS, 0xffffff, 0xffffff);
    public static final DeferredItem<Item> FACE_MONSTER_EGG = registerEgg("face_monster_egg", TEEntities.FACE_MONSTER, 0xffffff, 0xffffff);



    public static final DeferredItem<Item> KING_SLIME_SPAWN_EGG = registerEgg("king_slime_spawn_egg", TEEntities.KING_SLIME, 0xffffff, 0xffffff);
    public static final DeferredItem<Item> EYE_OF_CTHULHU_SPAWN_EGG = registerEgg("cthulhu_eye_spawn_egg", TEEntities.EYE_OF_CTHULHU, 0xffffff, 0xffffff);
    public static final DeferredItem<Item> EATER_OF_WORLD_SPAWN_EGG = registerEgg("eater_of_world_spawn_egg", TEEntities.EATER_OF_WORLDS, 0xffffff, 0xffffff);
    public static final DeferredItem<Item> BRAIN_OF_CTHULHU_SPAWN_EGG = registerEgg("brain_of_cthulhu_spawn_egg", TEEntities.BRAIN_OF_CTHULHU, 0xffffff, 0xffffff);
    public static final DeferredItem<Item> QUEUE_BEE_SPAWN_EGG = registerEgg("queue_bee_spawn_egg", TEEntities.QUEUE_BEE, 0xffffff, 0xffffff);



    public static DeferredItem<Item> registerEgg(String name, Supplier<? extends EntityType<? extends Mob>>  entityType, int primaryColor, int secondaryColor){
        return SPAWN_EGGS.register(name, () -> new DeferredSpawnEggItem(entityType, primaryColor, secondaryColor,new Item.Properties()));
    }

    // Summon Items
    public static final DeferredItem<Item> SLIME_STAFF = SUMMON_ITEMS.register("slime_staff", () -> new SummonItem<>(new Item.Properties(), TEEntities.SUMMON_SLIME, 1, 5));
    public static final DeferredItem<Item> IRON_GOLEM_STAFF = SUMMON_ITEMS.register("iron_golem_staff", () -> new SummonItem<>(new Item.Properties(), TEEntities.SUMMON_IRON_GOLEM, 1, 8));


//    public static final DeferredItem<Item> DEBUG_ITEM = SUMMON_ITEMS.register("debug_item", () -> new DebugItem(new Item.Properties().stacksTo(1)));



    public static final DeferredHolder<CreativeModeTab,CreativeModeTab> NEO_TERRA =
            TABS.register(MODID + "_tab", ()-> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.terraentity.title"))
                    .icon(()-> TEItems.KING_SLIME_SPAWN_EGG.asItem().getDefaultInstance())
                    .displayItems((itemDisplayParameters, output) -> {
                        SPAWN_EGGS.getEntries().forEach(item -> output.accept(item.get()));
                        if(ServerConfig.DISPLAY_SUMMON_ITEMS.get())
                            SUMMON_ITEMS.getEntries().forEach(item -> output.accept(item.get()));
                    })
                    .build());


    public static void register(IEventBus bus) {
        SPAWN_EGGS.register(bus);
        SUMMON_ITEMS.register(bus);
        TABS.register(bus);
    }
}
