package org.confluence.terraentity.init.item;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.confluence.terraentity.init.entity.TEBossEntities;
import org.confluence.terraentity.init.entity.TEMonsterEntities;
import org.confluence.terraentity.init.entity.TENpcEntities;

import java.util.function.Supplier;

import static org.confluence.terraentity.TerraEntity.MODID;

public class TESpawnEggItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);

    public static final DeferredItem<Item> BLUE_SLIME_SPAWN_EGG = registerEgg("blue_slime_spawn_egg", TEMonsterEntities.BLUE_SLIME, 0x73bcf4, 0x466CBE);
    public static final DeferredItem<Item> PURPLE_SLIME_SPAWN_EGG = registerEgg("purple_slime_spawn_egg", TEMonsterEntities.PURPLE_SLIME, 0xf334f8, 0xA246BE);
    public static final DeferredItem<Item> GREEN_SLIME_SPAWN_EGG = registerEgg("green_slime_spawn_egg", TEMonsterEntities.GREEN_SLIME, 0xa2f89f, 0x3de838);
    public static final DeferredItem<Item> RED_SLIME_SPAWN_EGG = registerEgg("red_slime_spawn_egg", TEMonsterEntities.RED_SLIME, 0xf83434, 0xA51E1E);
    public static final DeferredItem<Item> YELLOW_SLIME_SPAWN_EGG = registerEgg("yellow_slime_spawn_egg", TEMonsterEntities.YELLOW_SLIME, 0xf8e234, 0xd19519);
    public static final DeferredItem<Item> HONEY_SLIME_SPAWN_EGG = registerEgg("honey_slime_spawn_egg", TEMonsterEntities.HONEY_SLIME, 0xffffff, 0xffffff);
    public static final DeferredItem<Item> BLACK_SLIME_SPAWN_EGG = registerEgg("black_slime_spawn_egg", TEMonsterEntities.BLACK_SLIME, 0x7E7E7E, 0x373535);
    public static final DeferredItem<Item> PINK_SLIME_SPAWN_EGG = registerEgg("pink_slime_spawn_egg", TEMonsterEntities.PINK_SLIME, 0xFF87B3, 0xf89fe3);
    public static final DeferredItem<Item> DESERT_SLIME_SPAWN_EGG = registerEgg("desert_slime_spawn_egg", TEMonsterEntities.DESERT_SLIME, 0xffffff, 0xffffff);
    public static final DeferredItem<Item> GREEN_DUMPLING_SLIME_SPAWN_EGG = registerEgg("green_dumpling_slime_spawn_egg", TEMonsterEntities.GREEN_DUMPLING_SLIME, 0xa2f89f, 0x3de838);
    public static final DeferredItem<Item> SWAMP_SLIME_SPAWN_EGG = registerEgg("swamp_slime_spawn_egg", TEMonsterEntities.SWAMP_SLIME, 0xa2f89f, 0x3de838);
    public static final DeferredItem<Item> JUNGLE_SLIME_SPAWN_EGG = registerEgg("jungle_slime_spawn_egg", TEMonsterEntities.JUNGLE_SLIME, 0x9ae920, 0xC7AB5E);
    public static final DeferredItem<Item> JUNGLE_BAT_SPAWN_EGG = registerEgg("jungle_bat_spawn_egg", TEMonsterEntities.JUNGLE_BAT, 0xffffff, 0xffffff);
    public static final DeferredItem<Item> SNATCHER_SPAWN_EGG = registerEgg("snatcher_spawn_egg", TEMonsterEntities.SNATCHER, 0xffffff, 0xffffff);
    public static final DeferredItem<Item> MAN_EATER_SPAWN_EGG = registerEgg("man_eater_spawn_egg", TEMonsterEntities.MAN_EATER, 0xffffff, 0xffffff);
    public static final DeferredItem<Item> HORNET_SPAWN_EGG = registerEgg("hornet_spawn_egg", TEMonsterEntities.HORNET, 0xffffff, 0xffffff);
    public static final DeferredItem<Item> ICE_SLIME_SPAWN_EGG = registerEgg("ice_slime_spawn_egg", TEMonsterEntities.ICE_SLIME, 0xB3F0EA, 0x7FDEDF);
    public static final DeferredItem<Item> ICE_BAT_SPAWN_EGG = registerEgg("ice_bat_spawn_egg", TEMonsterEntities.ICE_BAT, 0xffffff, 0xffffff);
    public static final DeferredItem<Item> LAVA_SLIME_SPAWN_EGG = registerEgg("lava_slime_spawn_egg", TEMonsterEntities.LAVA_SLIME, 0xffffff, 0xffffff);
    public static final DeferredItem<Item> HELL_BAT_SPAWN_EGG = registerEgg("hell_bat_spawn_egg", TEMonsterEntities.HELL_BAT, 0xffffff, 0xffffff);
    public static final DeferredItem<Item> CRIMSON_SLIME_SPAWN_EGG = registerEgg("crimson_slime_spawn_egg", TEMonsterEntities.CRIMSON_SLIME, 0x8B4949, 0x7D1D1D);
    public static final DeferredItem<Item> TROPIC_SLIME_SPAWN_EGG = registerEgg("tropic_slime_spawn_egg", TEMonsterEntities.TROPIC_SLIME, 0x73bcf4, 0x7374f4);
    public static final DeferredItem<Item> LUMINOUS_SLIME_SPAWN_EGG = registerEgg("evil_slime_spawn_egg", TEMonsterEntities.LUMINOUS_SLIME, 0xFF00FF, 0xEDFFFA);
    public static final DeferredItem<Item> DEMON_EYE_SPAWN_EGG = registerEgg("demon_eye_spawn_egg", TEMonsterEntities.DEMON_EYE, 0xffffff, 0xab0d0d);
    public static final DeferredItem<Item> BLOOD_CRAWLER_SPAWN_EGG = registerEgg("blood_crawler_spawn_egg", TEMonsterEntities.BLOOD_CRAWLER, 0xffffff, 0xffffff);
    public static final DeferredItem<Item> BLOODY_SPORE_SPAWN_EGG = registerEgg("bloody_spore_spawn_egg", TEMonsterEntities.BLOODY_SPORE, 0xffffff, 0xffffff);
    public static final DeferredItem<Item> DECAYEDER_SPAWN_EGG = registerEgg("decayeder_spawn_egg", TEMonsterEntities.DECAYEDER, 0xffffff, 0xffffff);
    public static final DeferredItem<Item> DEVOURER_SPAWN_EGG = registerEgg("devourer_spawn_egg", TEMonsterEntities.DEVOURER, 0xffffff, 0xffffff);
    public static final DeferredItem<Item> GIANT_SHELLY_SPAWN_EGG = registerEgg("giant_shelly_spawn_egg", TEMonsterEntities.GIANT_SHELLY, 0xffffff, 0xffffff);
    public static final DeferredItem<Item> GIANT_WORM_SPAWN_EGG = registerEgg("giant_worm_spawn_egg", TEMonsterEntities.GIANT_WORM, 0xffffff, 0xffffff);
    public static final DeferredItem<Item> NYMPH_SPAWN_EGG = registerEgg("nymph_spawn_egg", TEMonsterEntities.NYMPH, 0xffffff, 0xffffff);
    public static final DeferredItem<Item> CAVE_BAT_SPAWN_EGG = registerEgg("cave_bat_spawn_egg", TEMonsterEntities.CAVE_BAT, 0xffffff, 0xffffff);
    public static final DeferredItem<Item> SPORE_BAT_SPAWN_EGG = registerEgg("spore_bat_spawn_egg", TEMonsterEntities.SPORE_BAT, 0xffffff, 0xffffff);
    public static final DeferredItem<Item> TOMB_CRAWLER_SPAWN_EGG = registerEgg("tomb_crawler_spawn_egg", TEMonsterEntities.TOMB_CRAWLER, 0xffffff, 0xffffff);
    public static final DeferredItem<Item> FLYING_FISH_SPAWN_EGG = registerEgg("flying_fish_spawn_egg", TEMonsterEntities.FLYING_FISH, 0xffffff, 0xffffff);
    public static final DeferredItem<Item> DRIPPLER_SPAWN_EGG = registerEgg("drippler_spawn_egg", TEMonsterEntities.DRIPPLER, 0xffffff, 0xffffff);
    public static final DeferredItem<Item> BLOOD_ZOMBIE_SPAWN_EGG = registerEgg("blood_zombie_spawn_egg", TEMonsterEntities.BLOOD_ZOMBIE, 0xffffff, 0xffffff);
    public static final DeferredItem<Item> WANDERING_EYE_FISH_SPAWN_EGG = registerEgg("wandering_eye_fish_spawn_egg", TEMonsterEntities.WANDERING_EYE_FISH, 0xffffff, 0xffffff);
    public static final DeferredItem<Item> CRIMSON_KEMERA_EGG = registerEgg("crimson_kemera_egg", TEMonsterEntities.CRIMSON_KEMERA, 0xffffff, 0xffffff);
    public static final DeferredItem<Item> EATER_OF_SOULS_SPAWN_EGG = registerEgg("eater_of_souls_spawn_egg", TEMonsterEntities.EATER_OF_SOULS, 0xffffff, 0xffffff);
    public static final DeferredItem<Item> FACE_MONSTER_EGG = registerEgg("face_monster_egg", TEMonsterEntities.FACE_MONSTER, 0xffffff, 0xffffff);
    public static final DeferredItem<Item> KING_SLIME_SPAWN_EGG = registerEgg("king_slime_spawn_egg", TEBossEntities.KING_SLIME, 0xffffff, 0xffffff);
    public static final DeferredItem<Item> EYE_OF_CTHULHU_SPAWN_EGG = registerEgg("cthulhu_eye_spawn_egg", TEBossEntities.EYE_OF_CTHULHU, 0xffffff, 0xffffff);
    public static final DeferredItem<Item> EATER_OF_WORLD_SPAWN_EGG = registerEgg("eater_of_world_spawn_egg", TEBossEntities.EATER_OF_WORLDS, 0xffffff, 0xffffff);
    public static final DeferredItem<Item> BRAIN_OF_CTHULHU_SPAWN_EGG = registerEgg("brain_of_cthulhu_spawn_egg", TEBossEntities.BRAIN_OF_CTHULHU, 0xffffff, 0xffffff);
    public static final DeferredItem<Item> QUEEN_BEE_SPAWN_EGG = registerEgg("queen_bee_spawn_egg", TEBossEntities.QUEEN_BEE, 0xffffff, 0xffffff);

    public static final DeferredItem<Item> GUILD_EGG = registerEgg("guild_spawn_egg", TENpcEntities.GUIDE, 0xffffff, 0xffffff);
    public static final DeferredItem<Item> DEMOLITIONIST_EGG = registerEgg("demolitionist_spawn_egg", TENpcEntities.DEMOLITIONIST, 0xffffff, 0xffffff);
    public static final DeferredItem<Item> GOBLIN_TINKERER_EGG = registerEgg("goblin_tinkerer_spawn_egg", TENpcEntities.GOBLIN_TINKERER, 0xffffff, 0xffffff);


    public static DeferredItem<Item> registerEgg(String name, Supplier<? extends EntityType<? extends Mob>> entityType, int primaryColor, int secondaryColor){
        return ITEMS.register(name, () -> new DeferredSpawnEggItem(entityType, primaryColor, secondaryColor,new Item.Properties()));
    }
}
