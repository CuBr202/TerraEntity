package org.confluence.terraentity.init.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.RegistryObject;
import org.confluence.terraentity.entity.monster.*;
import org.confluence.terraentity.entity.monster.demoneye.DemonEye;
import org.confluence.terraentity.entity.monster.prefab.AbstractPrefab;
import org.confluence.terraentity.entity.monster.prefab.FlyMonsterPrefab;
import org.confluence.terraentity.entity.monster.prefab.LandMonsterPrefab;
import org.confluence.terraentity.entity.monster.slime.BaseSlime;
import org.confluence.terraentity.entity.monster.slime.BlackSlime;
import org.confluence.terraentity.entity.monster.slime.HoneySlime;
import org.confluence.terraentity.init.TEEntities;

import java.util.function.Supplier;

public class TEMonsterEntities {
    // tip 史莱姆
    public static final RegistryObject<EntityType<BaseSlime>> BLUE_SLIME = registerSlime("blue", 0x73bcf4, 2);
    public static final RegistryObject<EntityType<BaseSlime>> GREEN_SLIME = registerSlime("green", 0x48E920, 2);
    public static final RegistryObject<EntityType<BaseSlime>> PINK_SLIME = registerSlime("pink", 0xFF87B3, 1);
    public static final RegistryObject<EntityType<BaseSlime>> CORRUPTED_SLIME = registerSlime("corrupted", 0xC91717, 2);
    public static final RegistryObject<EntityType<BaseSlime>> DESERT_SLIME = registerSlime("desert", 0xDCC59a, 2);
    public static final RegistryObject<EntityType<BaseSlime>> JUNGLE_SLIME = registerSlime("jungle", 0x9ae920, 2);
    public static final RegistryObject<EntityType<BaseSlime>> EVIL_SLIME = registerSlime("evil", 0xFF00FF, 2);
    public static final RegistryObject<EntityType<BaseSlime>> ICE_SLIME = registerSlime("ice", 0xB3F0EA, 2);
    public static final RegistryObject<EntityType<BaseSlime>>  LAVA_SLIME = TEEntities.ENTITIES.register("lava_slime", () -> EntityType.Builder.<BaseSlime>of((entityType, level) -> new BaseSlime(entityType, level, 0xFFB150, 2), MobCategory.MONSTER).sized(0.6F, 0.6F).clientTrackingRange(10).fireImmune().build(TEEntities.Key("lava_slime")));
    public static final RegistryObject<EntityType<BaseSlime>> LUMINOUS_SLIME = registerSlime("luminous", 0xFFFFFF, 2);
    public static final RegistryObject<EntityType<BaseSlime>> CRIMSON_SLIME = registerSlime("crimson", 0x8B4949, 2);
    public static final RegistryObject<EntityType<BaseSlime>> PURPLE_SLIME = registerSlime("purple", 0xf334f8, 2);
    public static final RegistryObject<EntityType<BaseSlime>> RED_SLIME = registerSlime("red", 0xf83434, 2);
    public static final RegistryObject<EntityType<BaseSlime>> TROPIC_SLIME = registerSlime("tropic", 0x73bcf4, 2);
    public static final RegistryObject<EntityType<BaseSlime>> YELLOW_SLIME = registerSlime("yellow", 0xf8e234, 2);
    public static final RegistryObject<EntityType<HoneySlime>> HONEY_SLIME = TEEntities.ENTITIES.register("honey_slime", () -> EntityType.Builder.<HoneySlime>of((entityType, level) -> new HoneySlime(entityType, level, 0xf8e234), MobCategory.MONSTER).sized(2.04F, 2.04F).clientTrackingRange(10).build(TEEntities.Key("honey_slime")));
    public static final RegistryObject<EntityType<BlackSlime>> BLACK_SLIME = TEEntities.ENTITIES.register("black_slime", () -> EntityType.Builder.of(BlackSlime::new, MobCategory.MONSTER).sized(2.04F, 2.04F).clientTrackingRange(10).build(TEEntities.Key("black_slime")));
    public static final RegistryObject<EntityType<BaseSlime>> GREEN_DUMPLING_SLIME = registerSlime("green_dumpling_slime", 0x32CD32 , 2);
    public static final RegistryObject<EntityType<BaseSlime>> SWAMP_SLIME = registerSlime("swamp_slime", 0x556B2F, 2);
    // tip 飞行怪
    public static final RegistryObject<EntityType<DemonEye>> DEMON_EYE = TEEntities.registerEntity("demon_eye", DemonEye::new,1.1F, 1.1F);
    public static final RegistryObject<EntityType<AbstractMonster>> CRIMSON_KEMERA = registerSimpleMonster("crimson_kemera", FlyMonsterPrefab.CRIMSON_KEMERA_BUILDER,1.2f,1.2f);
    public static final RegistryObject<EntityType<AbstractMonster>> EATER_OF_SOULS = registerSimpleMonster("eater_of_souls", FlyMonsterPrefab.EATER_OF_SOULS_BUILDER,1.2f,1.2f);
    public static final RegistryObject<EntityType<AbstractMonster>> DRIPPLER = registerSimpleMonster("drippler", FlyMonsterPrefab.DRIPPLER_BUILDER,1.6f,1.6f);
    public static final RegistryObject<EntityType<AbstractMonster>> WANDERING_EYE_FISH = registerSimpleMonster("wandering_eye_fish", FlyMonsterPrefab.WANDERING_EYE_FISH_BUILDER,1.4f,1.4f);
    public static final RegistryObject<EntityType<AbstractMonster>> FLYING_FISH = registerSimpleMonster("flying_fish", FlyMonsterPrefab.FLYING_FISH_BUILDER,0.75F,0.75F);
    public static final RegistryObject<EntityType<VisualNeuron>> VISUAL_NEURON = TEEntities.registerEntity("visual_neuron", VisualNeuron::new, 1.2f, 1.2f);
    // 蜜蜂
    public static final RegistryObject<EntityType<Hornet>> HORNET = TEEntities.registerEntity("hornet", (e, l)->new Hornet(e,l, FlyMonsterPrefab.BEE_BUILDER.get().setHealth(32)), 0.8f, 1.2f);
    public static final RegistryObject<EntityType<LittleHornet>> LITTLE_HORNET = TEEntities.registerEntity("little_hornet", LittleHornet::new,MobCategory.CREATURE, 0.8f, 1.2f);
    // 蝙蝠
    public static final RegistryObject<EntityType<AbstractMonster>> CAVE_BAT = registerSimpleMonster("cave_bat", FlyMonsterPrefab.CAVE_BAT_BUILDER,1.6f,1.6f);
    public static final RegistryObject<EntityType<AbstractMonster>> JUNGLE_BAT = registerSimpleMonster("jungle_bat", FlyMonsterPrefab.JUNGLE_BAT_BUILDER,1.6f,1.6f);
    public static final RegistryObject<EntityType<AbstractMonster>> HELL_BAT = registerSimpleMonster("hell_bat", FlyMonsterPrefab.HELL_BAT_BUILDER,1.6f,1.6f);
    public static final RegistryObject<EntityType<AbstractMonster>> ICE_BAT = registerSimpleMonster("ice_bat", FlyMonsterPrefab.ICE_BAT_BUILDER,1.6f,1.6f);
    public static final RegistryObject<EntityType<AbstractMonster>> SPORE_BAT = registerSimpleMonster("spore_bat", FlyMonsterPrefab.SPORE_BAT_BUILDER,1.6f,1.6f);
    // tip 陆生怪
    public static final RegistryObject<EntityType<Decayeder>> DECAYEDER = TEEntities.registerEntity("decayeder", Decayeder::new,1,1.8f);
    public static final RegistryObject<EntityType<BloodySpore>> BLOODY_SPORE = TEEntities.registerEntity("bloody_spore", BloodySpore::new, 1,1.5f);
    public static final RegistryObject<EntityType<BloodCrawler>> BLOOD_CRAWLER = TEEntities.registerEntity("blood_crawler", BloodCrawler::new, 1.8F, 1.2F);
    public static final RegistryObject<EntityType<AbstractMonster>> FACE_MONSTER = registerSimpleMonster("face_monster", LandMonsterPrefab.FACE_MONSTER_BUILDER,0.75F,1.95F);
    public static final RegistryObject<EntityType<AbstractMonster>> BLOOD_TUMORS = registerSimpleMonster("blood_tumors", LandMonsterPrefab.BLOOD_TUMORS,0.5F,0.5F);
    public static final RegistryObject<EntityType<AbstractMonster>> BLOOD_ZOMBIE = registerSimpleMonster("blood_zombie", LandMonsterPrefab.BLOOD_ZOMBIE_BUILDER,0.75F,1.95F);
    // 蠕虫
    public static final RegistryObject<EntityType<BaseWarm>> DEVOURER = TEEntities.registerEntity("devourer", (e, l)->new BaseWarm(e,l, AbstractPrefab.WARM_BUILDER.get()),2F,2F);
    public static final RegistryObject<EntityType<BaseWarm>> TOMB_CRAWLER = TEEntities.registerEntity("tomb_crawler", (e, l)->new BaseWarm(e,l, AbstractPrefab.WARM_BUILDER.get().setHealth(16).setAttackDamage(4).setArmor(2)),2F,2F);
    public static final RegistryObject<EntityType<BaseWarm>> GIANT_WORM = TEEntities.registerEntity("giant_worm", (e, l)->new BaseWarm(e,l, AbstractPrefab.WARM_BUILDER.get().setHealth(31).setAttackDamage(9).setArmor(3)),2F,2F);
    public static final RegistryObject<EntityType<GiantShelly>> GIANT_SHELLY = TEEntities.registerEntity("giant_shelly", GiantShelly::new,0.8F,0.8F);
    // 宁芙
    public static final RegistryObject<EntityType<Nymph>> NYMPH = TEEntities.registerEntity("nymph", Nymph::new,0.8F,1.95F);
    // 抓人草
    public static final RegistryObject<EntityType<Snatcher>> SNATCHER = TEEntities.registerEntity("snatcher", (e, l)->new Snatcher(e,l, new AbstractPrefab(31,2,13,20,1,1).getPrefab()),1F,1F);
    public static final RegistryObject<EntityType<Snatcher>> MAN_EATER = TEEntities.registerEntity("man_eater", (e, l)->new Snatcher(e,l, new AbstractPrefab(57,2,15,20,1,1).getPrefab()),1F,1F);

    private static RegistryObject<EntityType<BaseSlime>> registerSlime(String prefix, int color, int size) {
        return TEEntities.ENTITIES.register(prefix + "_slime", () -> EntityType.Builder.<BaseSlime>of((entityType, level) -> new BaseSlime(entityType, level, color, size), MobCategory.MONSTER).sized(2.04F, 2.04F).clientTrackingRange(10).build(TEEntities.Key("" + prefix + "_slime")));
    }

    // 用于调整包围盒
    public static RegistryObject<EntityType<AbstractMonster>> registerSimpleMonster(String name, Supplier<AbstractMonster.Builder> builder, float width, float height) {
        return TEEntities.ENTITIES.register(name, () -> EntityType.Builder.<AbstractMonster>of((type, level)->new AbstractMonster(type,level,builder.get()), MobCategory.MONSTER).clientTrackingRange(10).setTrackingRange(50).sized(width,height).build(TEEntities.Key(name)));
    }

    public static RegistryObject<EntityType<AbstractMonster>> registerSimpleMonster(String name, Supplier<AbstractMonster.Builder> builder) {
        return registerSimpleMonster(name, builder, 1, 1);
    }
}
