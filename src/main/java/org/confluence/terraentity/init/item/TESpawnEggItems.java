package org.confluence.terraentity.init.item;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.init.entity.TEBossEntities;
import org.confluence.terraentity.init.entity.TEMonsterEntities;
import org.confluence.terraentity.init.entity.TENpcEntities;

import java.util.function.Supplier;

import static org.confluence.terraentity.TerraEntity.MODID;

public class TESpawnEggItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);

    public static final RegistryObject<Item> BLUE_SLIME_SPAWN_EGG = registerEgg("blue_slime_spawn_egg", TEMonsterEntities.BLUE_SLIME, 0x73bcf4, 0x466CBE);
    public static final RegistryObject<Item> PURPLE_SLIME_SPAWN_EGG = registerEgg("purple_slime_spawn_egg", TEMonsterEntities.PURPLE_SLIME, 0xf334f8, 0xA246BE);
    public static final RegistryObject<Item> GREEN_SLIME_SPAWN_EGG = registerEgg("green_slime_spawn_egg", TEMonsterEntities.GREEN_SLIME, 0xa2f89f, 0x3de838);
    public static final RegistryObject<Item> RED_SLIME_SPAWN_EGG = registerEgg("red_slime_spawn_egg", TEMonsterEntities.RED_SLIME, 0xf83434, 0xA51E1E);
    public static final RegistryObject<Item> YELLOW_SLIME_SPAWN_EGG = registerEgg("yellow_slime_spawn_egg", TEMonsterEntities.YELLOW_SLIME, 0xf8e234, 0xd19519);
    public static final RegistryObject<Item> HONEY_SLIME_SPAWN_EGG = registerEgg("honey_slime_spawn_egg", TEMonsterEntities.HONEY_SLIME, 0xffffff, 0xffffff);
    public static final RegistryObject<Item> BLACK_SLIME_SPAWN_EGG = registerEgg("black_slime_spawn_egg", TEMonsterEntities.BLACK_SLIME, 0x7E7E7E, 0x373535);
    public static final RegistryObject<Item> PINK_SLIME_SPAWN_EGG = registerEgg("pink_slime_spawn_egg", TEMonsterEntities.PINK_SLIME, 0xFF87B3, 0xf89fe3);
    public static final RegistryObject<Item> DUNGEON_SLIME_SPAWN_EGG = registerEgg("dungeon_slime_spawn_egg", TEMonsterEntities.DUNGEON_SLIME, 0x6d697b, 0x6d697b);
    public static final RegistryObject<Item> DESERT_SLIME_SPAWN_EGG = registerEgg("desert_slime_spawn_egg", TEMonsterEntities.DESERT_SLIME, 0xffffff, 0xffffff);
    public static final RegistryObject<Item> GREEN_DUMPLING_SLIME_SPAWN_EGG = registerEgg("green_dumpling_slime_spawn_egg", TEMonsterEntities.GREEN_DUMPLING_SLIME, 0xa2f89f, 0x3de838);
    public static final RegistryObject<Item> SWAMP_SLIME_SPAWN_EGG = registerEgg("swamp_slime_spawn_egg", TEMonsterEntities.SWAMP_SLIME, 0xa2f89f, 0x3de838);
    public static final RegistryObject<Item> JUNGLE_SLIME_SPAWN_EGG = registerEgg("jungle_slime_spawn_egg", TEMonsterEntities.JUNGLE_SLIME, 0x9ae920, 0xC7AB5E);
    public static final RegistryObject<Item> GOLDEN_SLIME_SPAWN_EGG = registerEgg("golden_slime_spawn_egg", TEMonsterEntities.GOLDEN_SLIME, 0xfcf8bd, 0xf8e234);
    public static final RegistryObject<Item> JUNGLE_BAT_SPAWN_EGG = registerEgg("jungle_bat_spawn_egg", TEMonsterEntities.JUNGLE_BAT, 0xffffff, 0xffffff);
    public static final RegistryObject<Item> SNATCHER_SPAWN_EGG = registerEgg("snatcher_spawn_egg", TEMonsterEntities.SNATCHER, 0xffffff, 0xffffff);
    public static final RegistryObject<Item> MAN_EATER_SPAWN_EGG = registerEgg("man_eater_spawn_egg", TEMonsterEntities.MAN_EATER, 0xffffff, 0xffffff);
    public static final RegistryObject<Item> HORNET_SPAWN_EGG = registerEgg("hornet_spawn_egg", TEMonsterEntities.HORNET, 0xffffff, 0xffffff);
    public static final RegistryObject<Item> ICE_SLIME_SPAWN_EGG = registerEgg("ice_slime_spawn_egg", TEMonsterEntities.ICE_SLIME, 0xB3F0EA, 0x7FDEDF);
    public static final RegistryObject<Item> ICE_BAT_SPAWN_EGG = registerEgg("ice_bat_spawn_egg", TEMonsterEntities.ICE_BAT, 0xffffff, 0xffffff);
    public static final RegistryObject<Item> LAVA_SLIME_SPAWN_EGG = registerEgg("lava_slime_spawn_egg", TEMonsterEntities.LAVA_SLIME, 0xffffff, 0xffffff);
    public static final RegistryObject<Item> HELL_BAT_SPAWN_EGG = registerEgg("hell_bat_spawn_egg", TEMonsterEntities.HELL_BAT, 0xffffff, 0xffffff);
    public static final RegistryObject<Item> CRIMSLIME_SPAWN_EGG = registerEgg("crimslime_spawn_egg", TEMonsterEntities.CRIMSLIME, 0x8B4949, 0x7D1D1D);
    public static final RegistryObject<Item> CORRUPT_SLIME_SPAWN_EGG = registerEgg("corrupt_slime_spawn_egg", TEMonsterEntities.CORRUPT_SLIME, 0x3f3885, 0x625b9f);
    public static final RegistryObject<Item> TROPIC_SLIME_SPAWN_EGG = registerEgg("tropic_slime_spawn_egg", TEMonsterEntities.TROPIC_SLIME, 0x73bcf4, 0x7374f4);
    public static final RegistryObject<Item> LUMINOUS_SLIME_SPAWN_EGG = registerEgg("evil_slime_spawn_egg", TEMonsterEntities.LUMINOUS_SLIME, 0xFF00FF, 0xEDFFFA);
    public static final RegistryObject<Item> DEMON_EYE_SPAWN_EGG = registerEgg("demon_eye_spawn_egg", TEMonsterEntities.DEMON_EYE, 0xffffff, 0xab0d0d);
    public static final RegistryObject<Item> BLOOD_CRAWLER_SPAWN_EGG = registerEgg("blood_crawler_spawn_egg", TEMonsterEntities.BLOOD_CRAWLER, 0xffffff, 0xffffff);
    public static final RegistryObject<Item> BLOODY_SPORE_SPAWN_EGG = registerEgg("bloody_spore_spawn_egg", TEMonsterEntities.BLOODY_SPORE, 0xffffff, 0xffffff);
    public static final RegistryObject<Item> MUSHROOM_SKELETON_SPAWN_EGG = registerEgg("mushroom_skeleton_spawn_egg", TEMonsterEntities.MUSHROOM_SKELETON, 0xffffff, 0xffffff);
    public static final RegistryObject<Item> MUSHROOM_ZOMBIE_SPAWN_EGG = registerEgg("mushroom_zombie_spawn_egg", TEMonsterEntities.MUSHROOM_ZOMBIE, 0xffffff, 0xffffff);
    public static final RegistryObject<Item> DECAYEDER_SPAWN_EGG = registerEgg("decayeder_spawn_egg", TEMonsterEntities.DECAYEDER, 0xffffff, 0xffffff);
    public static final RegistryObject<Item> DEVOURER_SPAWN_EGG = registerEgg("devourer_spawn_egg", TEMonsterEntities.DEVOURER, 0xffffff, 0xffffff);
    public static final RegistryObject<Item> GIANT_SHELLY_SPAWN_EGG = registerEgg("giant_shelly_spawn_egg", TEMonsterEntities.GIANT_SHELLY, 0xffffff, 0xffffff);
    public static final RegistryObject<Item> GIANT_WORM_SPAWN_EGG = registerEgg("giant_worm_spawn_egg", TEMonsterEntities.GIANT_WORM, 0xffffff, 0xffffff);
    public static final RegistryObject<Item> NYMPH_SPAWN_EGG = registerEgg("nymph_spawn_egg", TEMonsterEntities.NYMPH, 0xffffff, 0xffffff);
    public static final RegistryObject<Item> CAVE_BAT_SPAWN_EGG = registerEgg("cave_bat_spawn_egg", TEMonsterEntities.CAVE_BAT, 0xffffff, 0xffffff);
    public static final RegistryObject<Item> SPORE_BAT_SPAWN_EGG = registerEgg("spore_bat_spawn_egg", TEMonsterEntities.SPORE_BAT, 0xffffff, 0xffffff);
    public static final RegistryObject<Item> TOMB_CRAWLER_SPAWN_EGG = registerEgg("tomb_crawler_spawn_egg", TEMonsterEntities.TOMB_CRAWLER, 0xffffff, 0xffffff);
    public static final RegistryObject<Item> ANTLION_SWARMER_SPAWN_EGG = registerEgg("antlion_swarmer_spawn_egg", TEMonsterEntities.ANTLION_SWARMER, 0xffffff, 0xffffff);
    public static final RegistryObject<Item> GIANT_ANTLION_SWARMER_SPAWN_EGG = registerEgg("giant_antlion_spawn_egg", TEMonsterEntities.GIANT_ANTLION_SWARMER, 0xffffff, 0xffffff);
    public static final RegistryObject<Item> FLYING_FISH_SPAWN_EGG = registerEgg("flying_fish_spawn_egg", TEMonsterEntities.FLYING_FISH, 0xffffff, 0xffffff);
    public static final RegistryObject<Item> HARPY_SPAWN_EGG = registerEgg("harpy_spawn_egg", TEMonsterEntities.HARPY, 0xffffff, 0xffffff);
    public static final RegistryObject<Item> DRIPPLER_SPAWN_EGG = registerEgg("drippler_spawn_egg", TEMonsterEntities.DRIPPLER, 0xffffff, 0xffffff);
    public static final RegistryObject<Item> BLOOD_ZOMBIE_SPAWN_EGG = registerEgg("blood_zombie_spawn_egg", TEMonsterEntities.BLOOD_ZOMBIE, 0xffffff, 0xffffff);
    public static final RegistryObject<Item> WANDERING_EYE_FISH_SPAWN_EGG = registerEgg("wandering_eye_fish_spawn_egg", TEMonsterEntities.WANDERING_EYE_FISH, 0xffffff, 0xffffff);
    public static final RegistryObject<Item> CRIMSON_KEMERA_EGG = registerEgg("crimson_kemera_egg", TEMonsterEntities.CRIMSON_KEMERA, 0xffffff, 0xffffff);
    public static final RegistryObject<Item> EATER_OF_SOULS_SPAWN_EGG = registerEgg("eater_of_souls_spawn_egg", TEMonsterEntities.EATER_OF_SOULS, 0xffffff, 0xffffff);
    public static final RegistryObject<Item> FACE_MONSTER_EGG = registerEgg("face_monster_egg", TEMonsterEntities.FACE_MONSTER, 0xffffff, 0xffffff);
    public static final RegistryObject<Item> KING_SLIME_SPAWN_EGG = registerEgg("king_slime_spawn_egg", TEBossEntities.KING_SLIME, 0xffffff, 0xffffff);
    public static final RegistryObject<Item> EYE_OF_CTHULHU_SPAWN_EGG = registerEgg("cthulhu_eye_spawn_egg", TEBossEntities.EYE_OF_CTHULHU, 0xffffff, 0xffffff);
    public static final RegistryObject<Item> EATER_OF_WORLD_SPAWN_EGG = registerEgg("eater_of_world_spawn_egg", TEBossEntities.EATER_OF_WORLDS, 0xffffff, 0xffffff);
    public static final RegistryObject<Item> BRAIN_OF_CTHULHU_SPAWN_EGG = registerEgg("brain_of_cthulhu_spawn_egg", TEBossEntities.BRAIN_OF_CTHULHU, 0xffffff, 0xffffff);
    public static final RegistryObject<Item> QUEEN_BEE_SPAWN_EGG = registerEgg("queen_bee_spawn_egg", TEBossEntities.QUEEN_BEE, 0xffffff, 0xffffff);
    public static final RegistryObject<Item> SKELETRON_SPAWN_EGG = registerEgg("skeletron_spawn_egg", TEBossEntities.SKELETRON, 0xffffff, 0xffffff);
    public static final RegistryObject<Item> DUNGEON_GUARDIAN_SPAWN_EGG = registerEgg("dungeon_guardian_spawn_egg", TEBossEntities.DUNGEON_GUARDIAN, 0xffffff, 0xffffff);
//    public static final RegistryObject<Item> WALL_OF_FLESH_SPAWN_EGG = registerEgg("wall_of_flesh_spawn_egg", TEBossEntities.WALL_OF_FLESH, 0xffffff, 0xffffff);

    // 地牢骷髅

    public static final RegistryObject<Item> ANGER_BONES_SPAWN_EGG = registerEgg("anger_bones_spawn_egg", TEMonsterEntities.ANGER_BONES, 0xffffff);
    public static final RegistryObject<Item> SHORT_BONES_SPAWN_EGG = registerEgg("short_bones_spawn_egg", TEMonsterEntities.SHORT_BONES, 0xffffff);
    public static final RegistryObject<Item> BIG_BONES_SPAWN_EGG = registerEgg("big_bones_spawn_egg", TEMonsterEntities.BIG_BONES, 0xffffff);
    public static final RegistryObject<Item> BIG_ANGER_BONES_SPAWN_EGG = registerEgg("big_anger_bones_spawn_egg", TEMonsterEntities.BIG_ANGER_BONES, 0xffffff);
    public static final RegistryObject<Item> BIG_MUSCLE_ANGER_BONES_SPAWN_EGG = registerEgg("big_muscle_anger_bones_spawn_egg", TEMonsterEntities.BIG_MUSCLE_ANGER_BONES, 0xffffff);
    public static final RegistryObject<Item> BIG_HELMET_ANGER_BONES_SPAWN_EGG = registerEgg("big_helmet_anger_bones_spawn_egg", TEMonsterEntities.BIG_HELMET_ANGER_BONES, 0xffffff);
    public static final RegistryObject<Item> CURSED_SKULL_SPAWN_EGG = registerEgg("cursed_skull_spawn_egg", TEMonsterEntities.CURSED_SKULL, 0xffffff);
    public static final RegistryObject<Item> DARK_CASTER_SPAWN_EGG = registerEgg("dark_caster_spawn_egg", TEMonsterEntities.DARK_CASTER, 0xffffff);
    public static final RegistryObject<Item> UNDEAD_VIKING_SPAWN_EGG = registerEgg("undead_viking_spawn_egg", TEMonsterEntities.UNDEAD_VIKING, 0xffffff);

    // 哥布林军队

    public static final RegistryObject<Item> GOBLIN_SORCERER_SPAWN_EGG = registerEgg("goblin_sorcerer_spawn_egg", TEMonsterEntities.GOBLIN_SORCERER, 0xffffff);
    public static final RegistryObject<Item> GOBLIN_ARCHER_SPAWN_EGG = registerEgg("goblin_archer_spawn_egg", TEMonsterEntities.GOBLIN_ARCHER, 0xffffff);
    public static final RegistryObject<Item> GOBLIN_PEON_SPAWN_EGG = registerEgg("goblin_peon_spawn_egg", TEMonsterEntities.GOBLIN_PEON, 0xffffff);
    public static final RegistryObject<Item> GOBLIN_WARRIOR_SPAWN_EGG = registerEgg("goblin_warrior_spawn_egg", TEMonsterEntities.GOBLIN_WARRIOR, 0xffffff);
    public static final RegistryObject<Item> GOBLIN_THIEF_SPAWN_EGG = registerEgg("goblin_thief_spawn_egg", TEMonsterEntities.GOBLIN_THIEF, 0xffffff);
    public static final RegistryObject<Item> GOBLIN_SCOUT_SPAWN_EGG = registerEgg("goblin_scout_spawn_egg", TEMonsterEntities.GOBLIN_SCOUT, 0xffffff);
    public static final RegistryObject<Item> ANGER_GOBLIN_SPAWN_EGG = registerEgg("anger_goblin_spawn_egg", TEMonsterEntities.ANGER_GOBLIN, 0xffffff);

    // NPC
    public static final RegistryObject<Item> GUIDE_SPAWN_EGG = registerEgg("guide_spawn_egg", TENpcEntities.GUIDE, 0xffffff);
    public static final RegistryObject<Item> DEMOLITIONIST_SPAWN_EGG = registerEgg("demolitionist_spawn_egg", TENpcEntities.DEMOLITIONIST, 0xffffff);
    public static final RegistryObject<Item> GOBLIN_TINKERER_SPAWN_EGG = registerEgg("goblin_tinkerer_spawn_egg", TENpcEntities.GOBLIN_TINKERER, 0xffffff);
    public static final RegistryObject<Item> ARMS_DEALER_SPAWN_EGG = registerEgg("arms_dealer_spawn_egg", TENpcEntities.ARMS_DEALER, 0xffffff);
    public static final RegistryObject<Item> NURSE_SPAWN_EGG = registerEgg("nurse_spawn_egg", TENpcEntities.NURSE, 0xffffff);
    public static final RegistryObject<Item> MERCHANT_SPAWN_EGG = registerEgg("merchant_spawn_egg", TENpcEntities.MERCHANT, 0xffffff);
    public static final RegistryObject<Item> PAINTER_SPAWN_EGG = registerEgg("painter_spawn_egg", TENpcEntities.PAINTER, 0xffffff);
    public static final RegistryObject<Item> DRYAD_SPAWN_EGG = registerEgg("dryad_spawn_egg", TENpcEntities.DRYAD, 0xffffff);
    public static final RegistryObject<Item> DYE_TRADER_SPAWN_EGG = registerEgg("dye_trader_spawn_egg", TENpcEntities.DYE_TRADER, 0xffffff);
    public static final RegistryObject<Item> ANGLER_SPAWN_EGG = registerEgg("angler_spawn_egg", TENpcEntities.ANGLER, 0xffffff);
    public static final RegistryObject<Item> OLD_MAN_SPAWN_EGG = registerEgg("old_man_spawn_egg", TENpcEntities.OLD_MAN, 0xffffff);




    public static RegistryObject<Item> registerEgg(String name, Supplier<? extends EntityType<? extends Mob>> entityType, int primaryColor, int secondaryColor){
        return ITEMS.register(name, () -> new ForgeSpawnEggItem(entityType, primaryColor, secondaryColor,new Item.Properties()));
    }
    public static RegistryObject<Item> registerEgg(String name, Supplier<? extends EntityType<? extends Mob>> entityType, int primaryColor){
        return ITEMS.register(name, () -> new ForgeSpawnEggItem(entityType, primaryColor, 0xffffff,new Item.Properties()));
    }
}
