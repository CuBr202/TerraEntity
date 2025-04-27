package org.confluence.terraentity.init.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.RegistryObject;
import org.confluence.terraentity.client.entity.model.GiantShellyModel;
import org.confluence.terraentity.client.entity.model.NymphModel;
import org.confluence.terraentity.client.entity.renderer.*;
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
    // 史莱姆
    public static final RegistryObject<EntityType<BaseSlime>> BLUE_SLIME = registerSlime("blue_slime", 0x73bcf4, 2);
    public static final RegistryObject<EntityType<BaseSlime>> GREEN_SLIME = registerSlime("green_slime", 0x48E920, 2);
    public static final RegistryObject<EntityType<BaseSlime>> PINK_SLIME = registerSlime("pink_slime", 0xFF87B3, 1);
    public static final RegistryObject<EntityType<BaseSlime>> CORRUPTED_SLIME = registerSlime("corrupted_slime", 0xC91717, 2);
    public static final RegistryObject<EntityType<BaseSlime>> DESERT_SLIME = registerSlime("desert_slime", 0xDCC59a, 2);
    public static final RegistryObject<EntityType<BaseSlime>> JUNGLE_SLIME = registerSlime("jungle_slime", 0x9ae920, 2);
    public static final RegistryObject<EntityType<BaseSlime>> EVIL_SLIME = registerSlime("evil_slime", 0xFF00FF, 2);
    public static final RegistryObject<EntityType<BaseSlime>> ICE_SLIME = registerSlime("ice_slime", 0xB3F0EA, 2);
    public static final RegistryObject<EntityType<BaseSlime>>  LAVA_SLIME = TEEntities.ENTITIES.register("lava_slime", () -> EntityType.Builder.<BaseSlime>of((entityType, level) -> new BaseSlime(entityType, level, 0xFFB150, 2), MobCategory.MONSTER).sized(0.6F, 0.6F).clientTrackingRange(10).fireImmune().build(TEEntities.Key("lava_slime")));
    public static final RegistryObject<EntityType<BaseSlime>> LUMINOUS_SLIME = registerSlime("luminous_slime", 0xFFFFFF, 2);
    public static final RegistryObject<EntityType<BaseSlime>> CRIMSON_SLIME = registerSlime("crimson_slime", 0x8B4949, 2);
    public static final RegistryObject<EntityType<BaseSlime>> PURPLE_SLIME = registerSlime("purple_slime", 0xf334f8, 2);
    public static final RegistryObject<EntityType<BaseSlime>> RED_SLIME = registerSlime("red_slime", 0xf83434, 2);
    public static final RegistryObject<EntityType<BaseSlime>> TROPIC_SLIME = registerSlime("tropic_slime", 0x73bcf4, 2);
    public static final RegistryObject<EntityType<BaseSlime>> YELLOW_SLIME = registerSlime("yellow_slime", 0xf8e234, 2);
    public static final RegistryObject<EntityType<HoneySlime>> HONEY_SLIME = TEEntities.ENTITIES.register("honey_slime", () -> EntityType.Builder.<HoneySlime>of((entityType, level) -> new HoneySlime(entityType, level, 0xf8e234), MobCategory.MONSTER).sized(2.04F, 2.04F).clientTrackingRange(10).build(TEEntities.Key("honey_slime")));
    public static final RegistryObject<EntityType<BlackSlime>> BLACK_SLIME = TEEntities.ENTITIES.register("black_slime", () -> EntityType.Builder.of(BlackSlime::new, MobCategory.MONSTER).sized(2.04F, 2.04F).clientTrackingRange(10).build(TEEntities.Key("black_slime")));
    public static final RegistryObject<EntityType<BaseSlime>> GREEN_DUMPLING_SLIME = registerSlime("green_dumpling_slime", 0x32CD32 , 2);
    public static final RegistryObject<EntityType<BaseSlime>> SWAMP_SLIME = registerSlime("swamp_slime", 0x556B2F, 2);
    // 飞行怪
    public static final RegistryObject<EntityType<DemonEye>> DEMON_EYE = TEEntities.registerEntity("demon_eye", DemonEye::new,1.1F, 1.1F);
    public static final RegistryObject<EntityType<AbstractMonster>> CRIMSON_KEMERA = registerSimpleMonster("crimson_kemera", FlyMonsterPrefab.CRIMSON_KEMERA_BUILDER,1.2f,1.2f);
    public static final RegistryObject<EntityType<AbstractMonster>> EATER_OF_SOULS = registerSimpleMonster("eater_of_souls", FlyMonsterPrefab.EATER_OF_SOULS_BUILDER,1.2f,1.2f);
    public static final RegistryObject<EntityType<AbstractMonster>> DRIPPLER = registerSimpleMonster("drippler", FlyMonsterPrefab.DRIPPLER_BUILDER,1.6f,1.6f);
    public static final RegistryObject<EntityType<AbstractMonster>> WANDERING_EYE_FISH = registerSimpleMonster("wandering_eye_fish", FlyMonsterPrefab.WANDERING_EYE_FISH_BUILDER,1.4f,1.4f);
    public static final RegistryObject<EntityType<AbstractMonster>> FLYING_FISH = registerSimpleMonster("flying_fish", FlyMonsterPrefab.FLYING_FISH_BUILDER,0.75F,0.75F);
    public static final RegistryObject<EntityType<VisualNeuron>> VISUAL_NEURON = TEEntities.registerEntity("visual_neuron", VisualNeuron::new, 1.2f, 1.2f);
    // 陆行怪
    public static final RegistryObject<EntityType<Decayeder>> DECAYEDER = TEEntities.registerEntity("decayeder", Decayeder::new,1,1.8f);
    public static final RegistryObject<EntityType<BloodySpore>> BLOODY_SPORE = TEEntities.registerEntity("bloody_spore", BloodySpore::new, 1,1.5f);
    public static final RegistryObject<EntityType<BloodCrawler>> BLOOD_CRAWLER = TEEntities.registerEntity("blood_crawler", BloodCrawler::new, 1.8F, 1.2F);
    public static final RegistryObject<EntityType<AbstractMonster>> FACE_MONSTER = registerSimpleMonster("face_monster", LandMonsterPrefab.FACE_MONSTER_BUILDER,0.75F,1.95F);
    public static final RegistryObject<EntityType<AbstractMonster>> BLOOD_TUMORS = registerSimpleMonster("blood_tumors", LandMonsterPrefab.BLOOD_TUMORS,0.5F,0.5F);
    public static final RegistryObject<EntityType<AbstractMonster>> BLOOD_ZOMBIE = registerSimpleMonster("blood_zombie", LandMonsterPrefab.BLOOD_ZOMBIE_BUILDER,0.75F,1.95F);
    // 蜜蜂
    public static final RegistryObject<EntityType<Hornet>> HORNET = TEEntities.registerEntity("hornet", (e, l)->new Hornet(e,l, FlyMonsterPrefab.BEE_BUILDER.get().setHealth(32)), 0.8f, 1.2f);
    public static final RegistryObject<EntityType<LittleHornet>> LITTLE_HORNET = TEEntities.registerEntity("little_hornet", LittleHornet::new,MobCategory.CREATURE, 0.8f, 1.2f);
    // 蝙蝠
    public static final RegistryObject<EntityType<AbstractMonster>> CAVE_BAT = registerSimpleMonster("cave_bat", FlyMonsterPrefab.CAVE_BAT_BUILDER,1.6f,1.6f);
    public static final RegistryObject<EntityType<AbstractMonster>> JUNGLE_BAT = registerSimpleMonster("jungle_bat", FlyMonsterPrefab.JUNGLE_BAT_BUILDER,1.6f,1.6f);
    public static final RegistryObject<EntityType<AbstractMonster>> HELL_BAT = registerSimpleMonster("hell_bat", FlyMonsterPrefab.HELL_BAT_BUILDER,1.6f,1.6f);
    public static final RegistryObject<EntityType<AbstractMonster>> ICE_BAT = registerSimpleMonster("ice_bat", FlyMonsterPrefab.ICE_BAT_BUILDER,1.6f,1.6f);
    public static final RegistryObject<EntityType<AbstractMonster>> SPORE_BAT = registerSimpleMonster("spore_bat", FlyMonsterPrefab.SPORE_BAT_BUILDER,1.6f,1.6f);
  // 蠕虫
    public static final RegistryObject<EntityType<BaseWarm>> DEVOURER = TEEntities.registerEntity("devourer", (e, l)->new BaseWarm(e,l, AbstractPrefab.WARM_BUILDER.get()),2F,2F);
    public static final RegistryObject<EntityType<BaseWarm>> TOMB_CRAWLER = TEEntities.registerEntity("tomb_crawler", (e, l)->new BaseWarm(e,l, AbstractPrefab.WARM_BUILDER.get().setHealth(16).setAttackDamage(4).setArmor(2)),2F,2F);
    public static final RegistryObject<EntityType<BaseWarm>> GIANT_WORM = TEEntities.registerEntity("giant_worm", (e, l)->new BaseWarm(e,l, AbstractPrefab.WARM_BUILDER.get().setHealth(31).setAttackDamage(9).setArmor(3)),2F,2F);
    // 卷壳怪
    public static final RegistryObject<EntityType<GiantShelly>> GIANT_SHELLY = TEEntities.registerEntity("giant_shelly", GiantShelly::new,0.8F,0.8F);
    // 宁芙
    public static final RegistryObject<EntityType<Nymph>> NYMPH = TEEntities.registerEntity("nymph", Nymph::new,0.8F,1.95F);
    // 抓人草
    public static final RegistryObject<EntityType<Snatcher>> SNATCHER = TEEntities.registerEntity("snatcher", (e, l)->new Snatcher(e,l, new AbstractPrefab(31,2,13,20,1,1).getPrefab()),1F,1F);
    public static final RegistryObject<EntityType<Snatcher>> MAN_EATER = TEEntities.registerEntity("man_eater", (e, l)->new Snatcher(e,l, new AbstractPrefab(57,2,15,20,1,1).getPrefab()),1F,1F);

    @OnlyIn(Dist.CLIENT)
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(TEMonsterEntities.BLUE_SLIME.get(), c -> new CustomSlimeRenderer(c, "blue"));
        event.registerEntityRenderer(TEMonsterEntities.GREEN_SLIME.get(), c -> new CustomSlimeRenderer(c, "green"));
        event.registerEntityRenderer(TEMonsterEntities.PINK_SLIME.get(), c -> new CustomSlimeRenderer(c, "pink"));
        event.registerEntityRenderer(TEMonsterEntities.CORRUPTED_SLIME.get(), c -> new CustomSlimeRenderer(c, "corrupted"));
        event.registerEntityRenderer(TEMonsterEntities.DESERT_SLIME.get(), c -> new CustomSlimeRenderer(c, "desert"));
        event.registerEntityRenderer(TEMonsterEntities.JUNGLE_SLIME.get(), c -> new CustomSlimeRenderer(c, "jungle"));
        event.registerEntityRenderer(TEMonsterEntities.EVIL_SLIME.get(), c -> new CustomSlimeRenderer(c, "evil"));
        event.registerEntityRenderer(TEMonsterEntities.ICE_SLIME.get(), c -> new CustomSlimeRenderer(c, "ice"));
        event.registerEntityRenderer(TEMonsterEntities.LAVA_SLIME.get(), c -> new CustomSlimeRenderer(c, "lava"));
        event.registerEntityRenderer(TEMonsterEntities.LUMINOUS_SLIME.get(), c -> new CustomSlimeRenderer(c, "luminous"));
        event.registerEntityRenderer(TEMonsterEntities.CRIMSON_SLIME.get(), c -> new CustomSlimeRenderer(c, "crimson"));
        event.registerEntityRenderer(TEMonsterEntities.PURPLE_SLIME.get(), c -> new CustomSlimeRenderer(c, "purple"));
        event.registerEntityRenderer(TEMonsterEntities.RED_SLIME.get(), c -> new CustomSlimeRenderer(c, "red"));
        event.registerEntityRenderer(TEMonsterEntities.TROPIC_SLIME.get(), c -> new CustomSlimeRenderer(c, "tropic"));
        event.registerEntityRenderer(TEMonsterEntities.YELLOW_SLIME.get(), c -> new CustomSlimeRenderer(c, "yellow"));
        event.registerEntityRenderer(TEMonsterEntities.HONEY_SLIME.get(), c -> new CustomSlimeRenderer(c, "honey"));
        event.registerEntityRenderer(TEMonsterEntities.BLACK_SLIME.get(), c -> new CustomSlimeRenderer(c, "black"));
        event.registerEntityRenderer(TEMonsterEntities.GREEN_DUMPLING_SLIME.get(), c -> new CustomSlimeRenderer(c, "green_dumpling"));
        event.registerEntityRenderer(TEMonsterEntities.SWAMP_SLIME.get(), c -> new CustomSlimeRenderer(c, "swamp"));


        event.registerEntityRenderer(TEMonsterEntities.CRIMSON_KEMERA.get(), c-> new GeoNormalRenderer<>(c, TEMonsterEntities.CRIMSON_KEMERA.getId(),true));
        event.registerEntityRenderer(TEMonsterEntities.EATER_OF_SOULS.get(), c-> new GeoNormalRenderer<>(c, TEMonsterEntities.EATER_OF_SOULS.getId(),true));
        event.registerEntityRenderer(TEMonsterEntities.DRIPPLER.get(), c-> new GeoNormalRenderer<>(c, TEMonsterEntities.DRIPPLER.getId(),false,2f,0));
        event.registerEntityRenderer(TEMonsterEntities.WANDERING_EYE_FISH.get(), c-> new GeoNormalRenderer<>(c, TEMonsterEntities.WANDERING_EYE_FISH.getId(),false,1.5f,0));
        event.registerEntityRenderer(TEMonsterEntities.FLYING_FISH.get(), c-> new GeoNormalRenderer<>(c, TEMonsterEntities.FLYING_FISH.getId(),true,0.75f,0));


        event.registerEntityRenderer(TEMonsterEntities.DEMON_EYE.get(), DemonEyeRenderer::new);
//        if (!ClientConfig.ENABLE_NON_SPIDER_MODEL.get()) {
        event.registerEntityRenderer(TEMonsterEntities.BLOOD_CRAWLER.get(), c -> new GeoNormalRenderer<>(c, TEMonsterEntities.BLOOD_CRAWLER.getId()));
//        }
        event.registerEntityRenderer(TEMonsterEntities.BLOODY_SPORE.get(), BloodySporeRenderer::new);
        event.registerEntityRenderer(TEMonsterEntities.DECAYEDER.get(), c -> new HumanoidRenderer<>(c, TEMonsterEntities.DECAYEDER.getId()));


        event.registerEntityRenderer(TEMonsterEntities.FACE_MONSTER.get(), c-> new GeoNormalRenderer<>(c, TEMonsterEntities.FACE_MONSTER.getId(),false));
        event.registerEntityRenderer(TEMonsterEntities.BLOOD_TUMORS.get(), c-> new GeoNormalRenderer<>(c, TEMonsterEntities.BLOOD_TUMORS.getId(),false));
        event.registerEntityRenderer(TEMonsterEntities.BLOOD_ZOMBIE.get(), c-> new GeoNormalRenderer<>(c, TEMonsterEntities.BLOOD_ZOMBIE.getId(),false));
        event.registerEntityRenderer(TEMonsterEntities.DEVOURER.get(), c-> new GeoWormRenderer<>(c, TEMonsterEntities.DEVOURER.getId(),2.0f, 0.0f));
        event.registerEntityRenderer(TEMonsterEntities.GIANT_WORM.get(), c-> new GeoWormRenderer<>(c, TEMonsterEntities.GIANT_WORM.getId(),2.0f, 0.0f));
        event.registerEntityRenderer(TEMonsterEntities.TOMB_CRAWLER.get(), c-> new GeoWormRenderer<>(c, TEMonsterEntities.TOMB_CRAWLER.getId(),2.0f, 0.0f));
        event.registerEntityRenderer(TEMonsterEntities.GIANT_SHELLY.get(), c-> new GeoNormalRenderer<>(c, new GiantShellyModel<>(TEMonsterEntities.GIANT_SHELLY.getId()),false,2,0));
        // bat
        event.registerEntityRenderer(TEMonsterEntities.CAVE_BAT.get(), c-> new GeoNormalRenderer<>(c, TEMonsterEntities.CAVE_BAT.getId(),false));
        event.registerEntityRenderer(TEMonsterEntities.JUNGLE_BAT.get(), c-> new GeoNormalRenderer<>(c, TEMonsterEntities.JUNGLE_BAT.getId(),false));
        event.registerEntityRenderer(TEMonsterEntities.HELL_BAT.get(), c-> new GeoNormalRenderer<>(c, TEMonsterEntities.HELL_BAT.getId(),false));
        event.registerEntityRenderer(TEMonsterEntities.ICE_BAT.get(), c-> new GeoNormalRenderer<>(c, TEMonsterEntities.ICE_BAT.getId(),false));
        event.registerEntityRenderer(TEMonsterEntities.SPORE_BAT.get(), c-> new GeoNormalRenderer<>(c, TEMonsterEntities.SPORE_BAT.getId(),false));

        // bee
        event.registerEntityRenderer(TEMonsterEntities.LITTLE_HORNET.get(), c->new GeoNormalRenderer<>(c, TEMonsterEntities.LITTLE_HORNET.getId(),true, 1, 0.5f));
        event.registerEntityRenderer(TEMonsterEntities.HORNET.get(), c->new GeoNormalRenderer<>(c, TEMonsterEntities.HORNET.getId(),true, 1, 0.5f));

        event.registerEntityRenderer(TEMonsterEntities.NYMPH.get(), c->new GeoNormalRenderer<>(c, new NymphModel<>(TEMonsterEntities.NYMPH.getId()),false,1,0f));
        event.registerEntityRenderer(TEMonsterEntities.SNATCHER.get(), c->new SnatcherRenderer<>(c, TEMonsterEntities.SNATCHER.getId()));
        event.registerEntityRenderer(TEMonsterEntities.MAN_EATER.get(), c->new SnatcherRenderer<>(c, TEMonsterEntities.MAN_EATER.getId()));

    }

    public static void registerEntityAttributes(EntityAttributeCreationEvent event) {

        // slime
        event.put(BLUE_SLIME.get(), BaseSlime.createSlimeAttributes(4.0F, 0, 16.0F).build());
        event.put(GREEN_SLIME.get(), BaseSlime.createSlimeAttributes(3.0F, 0, 9.0F).build());
        event.put(PINK_SLIME.get(), BaseSlime.createSlimeAttributes(2.0F, 2, 97.0F).build());
        event.put(CORRUPTED_SLIME.get(), BaseSlime.createSlimeAttributes(35.0F, 6, 110.0F).build());
        event.put(DESERT_SLIME.get(), BaseSlime.createSlimeAttributes(6.0F, 0, 21.0F).build());
        event.put(JUNGLE_SLIME.get(), BaseSlime.createSlimeAttributes(12.0F, 0, 46.0F).build());
        event.put(EVIL_SLIME.get(), BaseSlime.createSlimeAttributes(29.0F, 2, 58.0F).build());
        event.put(ICE_SLIME.get(), BaseSlime.createSlimeAttributes(5.0F, 0, 13.0F).build());
        event.put(LAVA_SLIME.get(), BaseSlime.createSlimeAttributes(10.0F, 2, 30.0F).build());
        event.put(LUMINOUS_SLIME.get(), BaseSlime.createSlimeAttributes(45.0F, 6, 117.0F).build());
        event.put(CRIMSON_SLIME.get(), BaseSlime.createSlimeAttributes(39.0F, 5, 130.0F).build());
        event.put(PURPLE_SLIME.get(), BaseSlime.createSlimeAttributes(5.0F, 1, 25.0F).build());
        event.put(RED_SLIME.get(), BaseSlime.createSlimeAttributes(5.0F, 1, 25.0F).build());
        event.put(TROPIC_SLIME.get(), BaseSlime.createSlimeAttributes(5.0F, 0, 13.0F).build());
        event.put(YELLOW_SLIME.get(), BaseSlime.createSlimeAttributes(6.0F, 2, 25.0F).build());
        event.put(HONEY_SLIME.get(), HoneySlime.createSlimeAttributes(0F, 0, 16.0F).build());
        event.put(GREEN_DUMPLING_SLIME.get(), BaseSlime.createSlimeAttributes(5.0F, 1, 25.0F).build());
        event.put(SWAMP_SLIME.get(), BaseSlime.createSlimeAttributes(5.0F, 1, 25.0F).build());
        event.put(BLACK_SLIME.get(), Monster.createMonsterAttributes().build()); // 由finalizeSpawn设置
        // land
        event.put(BLOOD_CRAWLER.get(), BloodCrawler.createAttributes().build());
        event.put(DECAYEDER.get(), Decayeder.createAttributes().build());
        event.put(BLOODY_SPORE.get(), BloodySpore.createAttributes().build());
        event.put(FACE_MONSTER.get(), AbstractMonster.createAttributes().build());
        event.put(BLOOD_TUMORS.get(), AbstractMonster.createAttributes().build());
        event.put(BLOOD_ZOMBIE.get(), AbstractMonster.createAttributes().build());
        event.put(GIANT_SHELLY.get(), AbstractMonster.createAttributes().build());
        event.put(NYMPH.get(), AbstractMonster.createAttributes().build());
        event.put(SNATCHER.get(), AbstractMonster.createAttributes().build());
        event.put(MAN_EATER.get(), AbstractMonster.createAttributes().build());

        // fly
        event.put(DEMON_EYE.get(), DemonEye.createAttributes().build());
        event.put(FLYING_FISH.get(), AbstractMonster.createAttributes().build());
        event.put(CRIMSON_KEMERA.get(), AbstractMonster.createAttributes().build());
        event.put(DRIPPLER.get(), AbstractMonster.createAttributes().build());
        event.put(WANDERING_EYE_FISH.get(), AbstractMonster.createAttributes().build());
        event.put(EATER_OF_SOULS.get(), AbstractMonster.createAttributes().build());


        // bat
        event.put(CAVE_BAT.get(), AbstractMonster.createAttributes().build());
        event.put(JUNGLE_BAT.get(), AbstractMonster.createAttributes().build());
        event.put(HELL_BAT.get(), AbstractMonster.createAttributes().build());
        event.put(ICE_BAT.get(), AbstractMonster.createAttributes().build());
        event.put(SPORE_BAT.get(), AbstractMonster.createAttributes().build());
        // worm
        event.put(GIANT_WORM.get(), AbstractMonster.createAttributes().build());
        event.put(DEVOURER.get(), AbstractMonster.createAttributes().build());
        event.put(TOMB_CRAWLER.get(), AbstractMonster.createAttributes().build());

        // bee
        event.put(HORNET.get(), AbstractMonster.createAttributes().build());
        event.put(LITTLE_HORNET.get(), AbstractMonster.createAttributes().build());
    }


    public static void spawnPlacementRegister(SpawnPlacementRegisterEvent event) {

        event.register(BLUE_SLIME.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, BaseSlime::checkSlimeSpawn, SpawnPlacementRegisterEvent.Operation.REPLACE);
        event.register(GREEN_SLIME.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, BaseSlime::checkSlimeSpawn, SpawnPlacementRegisterEvent.Operation.REPLACE);
        event.register(PURPLE_SLIME.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, BaseSlime::checkSlimeSpawn, SpawnPlacementRegisterEvent.Operation.REPLACE);
        event.register(PINK_SLIME.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, BaseSlime::checkSlimeSpawn, SpawnPlacementRegisterEvent.Operation.REPLACE);
        event.register(CORRUPTED_SLIME.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, BaseSlime::checkSlimeSpawn, SpawnPlacementRegisterEvent.Operation.REPLACE);
        event.register(DESERT_SLIME.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, BaseSlime::checkSlimeSpawn, SpawnPlacementRegisterEvent.Operation.REPLACE);
        event.register(JUNGLE_SLIME.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, BaseSlime::checkSlimeSpawn, SpawnPlacementRegisterEvent.Operation.REPLACE);
        event.register(ICE_SLIME.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, BaseSlime::checkSlimeSpawn, SpawnPlacementRegisterEvent.Operation.REPLACE);
        event.register(TROPIC_SLIME.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, BaseSlime::checkSlimeSpawn, SpawnPlacementRegisterEvent.Operation.REPLACE);
        event.register(CRIMSON_SLIME.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, BaseSlime::checkSlimeSpawn, SpawnPlacementRegisterEvent.Operation.REPLACE);
        event.register(YELLOW_SLIME.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, BaseSlime::checkSlimeSpawn, SpawnPlacementRegisterEvent.Operation.REPLACE);
        event.register(RED_SLIME.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, BaseSlime::checkSlimeSpawn, SpawnPlacementRegisterEvent.Operation.REPLACE);
        event.register(BLACK_SLIME.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, BaseSlime::checkSlimeSpawn, SpawnPlacementRegisterEvent.Operation.REPLACE);
        event.register(LAVA_SLIME.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, BaseSlime::checkSlimeSpawn, SpawnPlacementRegisterEvent.Operation.REPLACE);
        event.register(SWAMP_SLIME.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, BaseSlime::checkSlimeSpawn, SpawnPlacementRegisterEvent.Operation.REPLACE);


        // land
        event.register(BLOOD_CRAWLER.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, BloodCrawler::checkBloodCrawlerSpawn, SpawnPlacementRegisterEvent.Operation.REPLACE);
        event.register(BLOOD_ZOMBIE.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AbstractMonster::checkRoutineMonsterSpawn, SpawnPlacementRegisterEvent.Operation.REPLACE);
        event.register(BLOODY_SPORE.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, BloodySpore::checkBloodySporeSpawn, SpawnPlacementRegisterEvent.Operation.REPLACE);
        event.register(FACE_MONSTER.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AbstractMonster::checkRoutineMonsterSpawn, SpawnPlacementRegisterEvent.Operation.REPLACE);
        event.register(DECAYEDER.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AbstractMonster::checkRoutineMonsterSpawn, SpawnPlacementRegisterEvent.Operation.REPLACE);
        event.register(GIANT_SHELLY.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AbstractMonster::checkUndergroundMonsterSpawn, SpawnPlacementRegisterEvent.Operation.REPLACE);
        event.register(NYMPH.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AbstractMonster::checkUndergroundMonsterSpawn, SpawnPlacementRegisterEvent.Operation.REPLACE);
        event.register(SNATCHER.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AbstractMonster::checkGroundSpawn, SpawnPlacementRegisterEvent.Operation.REPLACE);
        event.register(MAN_EATER.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AbstractMonster::checkUndergroundMonsterSpawn, SpawnPlacementRegisterEvent.Operation.REPLACE);
        // fly
        event.register(DEMON_EYE.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, DemonEye::checkDemonEyeSpawn,  SpawnPlacementRegisterEvent.Operation.REPLACE);
        event.register(FLYING_FISH.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AbstractMonster::checkFlyingFishSpawn, SpawnPlacementRegisterEvent.Operation.REPLACE);
        event.register(CRIMSON_KEMERA.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AbstractMonster::checkRoutineMonsterSpawn, SpawnPlacementRegisterEvent.Operation.REPLACE);
        event.register(EATER_OF_SOULS.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AbstractMonster::checkRoutineMonsterSpawn, SpawnPlacementRegisterEvent.Operation.REPLACE);

        // worm
        event.register(DEVOURER.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AbstractMonster::checkRoutineMonsterSpawn, SpawnPlacementRegisterEvent.Operation.REPLACE);
        event.register(GIANT_WORM.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AbstractMonster::checkUndergroundMonsterSpawn, SpawnPlacementRegisterEvent.Operation.REPLACE);
        event.register(TOMB_CRAWLER.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AbstractMonster::checkUndergroundMonsterSpawn, SpawnPlacementRegisterEvent.Operation.REPLACE);

        // bee
        event.register(HORNET.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AbstractMonster::checkUndergroundMonsterSpawn, SpawnPlacementRegisterEvent.Operation.REPLACE);

        // bat
        event.register(CAVE_BAT.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AbstractMonster::checkUndergroundMonsterSpawn, SpawnPlacementRegisterEvent.Operation.REPLACE);
        event.register(JUNGLE_BAT.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AbstractMonster::checkRoutineMonsterSpawn, SpawnPlacementRegisterEvent.Operation.REPLACE);
        event.register(HELL_BAT.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AbstractMonster::checkNetherMonsterSpawn, SpawnPlacementRegisterEvent.Operation.REPLACE);
        event.register(ICE_BAT.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AbstractMonster::checkUndergroundMonsterSpawn, SpawnPlacementRegisterEvent.Operation.REPLACE);
        event.register(SPORE_BAT.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AbstractMonster::checkUndergroundMonsterSpawn, SpawnPlacementRegisterEvent.Operation.REPLACE);



    }

    private static RegistryObject<EntityType<BaseSlime>> registerSlime(String name, int color, int size) {
        return TEEntities.ENTITIES.register(name, () -> EntityType.Builder.<BaseSlime>of((entityType, level) -> new BaseSlime(entityType, level, color, size), MobCategory.MONSTER).sized(2.04F, 2.04F).clientTrackingRange(10).build(TEEntities.Key(name)));
    }

    // 用于调整包围盒
    public static RegistryObject<EntityType<AbstractMonster>> registerSimpleMonster(String name, Supplier<AbstractMonster.Builder> builder, float width, float height) {
        return TEEntities.ENTITIES.register(name, () -> EntityType.Builder.<AbstractMonster>of((type, level)->new AbstractMonster(type,level,builder.get()), MobCategory.MONSTER).clientTrackingRange(10).setTrackingRange(50).sized(width,height).build(TEEntities.Key(name)));
    }

    public static RegistryObject<EntityType<AbstractMonster>> registerSimpleMonster(String name, Supplier<AbstractMonster.Builder> builder) {
        return registerSimpleMonster(name, builder, 1, 1);
    }

    public static void register(IEventBus bus) {

    }
}
