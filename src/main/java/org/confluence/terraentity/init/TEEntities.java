package org.confluence.terraentity.init;

import net.minecraft.client.renderer.entity.IronGolemRenderer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.client.boss.model.GeoBossModel;
import org.confluence.terraentity.client.boss.model.SkeletronHandModel;
import org.confluence.terraentity.client.boss.renderer.*;
import org.confluence.terraentity.client.entity.model.GiantShellyModel;
import org.confluence.terraentity.client.entity.model.NymphModel;
import org.confluence.terraentity.client.entity.renderer.*;
import org.confluence.terraentity.config.ClientConfig;
import org.confluence.terraentity.entity.boss.*;
import org.confluence.terraentity.entity.model.CrownOfKingSlimeModelEntity;
import org.confluence.terraentity.entity.monster.*;
import org.confluence.terraentity.entity.monster.demoneye.DemonEye;
import org.confluence.terraentity.entity.monster.prefab.AbstractPrefab;
import org.confluence.terraentity.entity.monster.prefab.FlyMonsterPrefab;
import org.confluence.terraentity.entity.monster.prefab.LandMonsterPrefab;
import org.confluence.terraentity.entity.monster.slime.BaseSlime;
import org.confluence.terraentity.entity.monster.slime.BlackSlime;
import org.confluence.terraentity.entity.monster.slime.HoneySlime;
import org.confluence.terraentity.entity.proj.*;
import org.confluence.terraentity.entity.rideable.RideableBee;
import org.confluence.terraentity.entity.rideable.RideableSlime;
import org.confluence.terraentity.entity.summon.SummonHornet;
import org.confluence.terraentity.entity.summon.SummonIronGolem;
import org.confluence.terraentity.entity.summon.SummonSlime;

import java.util.function.Supplier;

import static org.confluence.terraentity.TerraEntity.MODID;

@SuppressWarnings("unused")
@EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.MOD)
public final class TEEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, MODID);


    // tip 史莱姆
    public static final DeferredHolder<EntityType<?>,EntityType<BaseSlime>> BLUE_SLIME = registerSlime("blue", 0x73bcf4, 2);
    public static final DeferredHolder<EntityType<?>,EntityType<BaseSlime>> GREEN_SLIME = registerSlime("green", 0x48E920, 2);
    public static final DeferredHolder<EntityType<?>,EntityType<BaseSlime>> PINK_SLIME = registerSlime("pink", 0xFF87B3, 1);
    public static final DeferredHolder<EntityType<?>,EntityType<BaseSlime>> CORRUPTED_SLIME = registerSlime("corrupted", 0xC91717, 2);
    public static final DeferredHolder<EntityType<?>,EntityType<BaseSlime>> DESERT_SLIME = registerSlime("desert", 0xDCC59a, 2);
    public static final DeferredHolder<EntityType<?>,EntityType<BaseSlime>> JUNGLE_SLIME = registerSlime("jungle", 0x9ae920, 2);
    public static final DeferredHolder<EntityType<?>,EntityType<BaseSlime>> EVIL_SLIME = registerSlime("evil", 0xFF00FF, 2);
    public static final DeferredHolder<EntityType<?>,EntityType<BaseSlime>> ICE_SLIME = registerSlime("ice", 0xB3F0EA, 2);
    public static final DeferredHolder<EntityType<?>,EntityType<BaseSlime>> LAVA_SLIME = ENTITIES.register("lava_slime", () -> EntityType.Builder.<BaseSlime>of((entityType, level) -> new BaseSlime(entityType, level, 0xFFB150, 2), MobCategory.MONSTER).sized(0.6F, 0.6F).clientTrackingRange(10).fireImmune().build(Key("lava_slime")));
    public static final DeferredHolder<EntityType<?>,EntityType<BaseSlime>> LUMINOUS_SLIME = registerSlime("luminous", 0xFFFFFF, 2);
    public static final DeferredHolder<EntityType<?>,EntityType<BaseSlime>> CRIMSON_SLIME = registerSlime("crimson", 0x8B4949, 2);
    public static final DeferredHolder<EntityType<?>,EntityType<BaseSlime>> PURPLE_SLIME = registerSlime("purple", 0xf334f8, 2);
    public static final DeferredHolder<EntityType<?>,EntityType<BaseSlime>> RED_SLIME = registerSlime("red", 0xf83434, 2);
    public static final DeferredHolder<EntityType<?>,EntityType<BaseSlime>> TROPIC_SLIME = registerSlime("tropic", 0x73bcf4, 2);
    public static final DeferredHolder<EntityType<?>,EntityType<BaseSlime>> YELLOW_SLIME = registerSlime("yellow", 0xf8e234, 2);
    public static final DeferredHolder<EntityType<?>,EntityType<HoneySlime>> HONEY_SLIME = ENTITIES.register("honey_slime", () -> EntityType.Builder.<HoneySlime>of((entityType, level) -> new HoneySlime(entityType, level, 0xf8e234), MobCategory.MONSTER).sized(0.6F, 0.6F).clientTrackingRange(10).build(Key("honey_slime")));
    public static final DeferredHolder<EntityType<?>,EntityType<BlackSlime>> BLACK_SLIME = ENTITIES.register("black_slime", () -> EntityType.Builder.of(BlackSlime::new, MobCategory.MONSTER).sized(0.6F, 0.6F).clientTrackingRange(10).build(Key("black_slime")));
    public static final DeferredHolder<EntityType<?>, EntityType<BaseSlime>> GREEN_DUMPLING_SLIME = registerSlime("green_dumpling_slime", 0x32CD32 , 2);
    public static final DeferredHolder<EntityType<?>, EntityType<BaseSlime>> SWAMP_SLIME = registerSlime("swamp_slime", 0x556B2F, 2);
    private static DeferredHolder<EntityType<?>, EntityType<BaseSlime>> registerSlime(String prefix, int color, int size) {
        return ENTITIES.register(
                prefix + "_slime",
                () -> EntityType.Builder.<BaseSlime>of((entityType, level) -> new BaseSlime(entityType, level, color, size), MobCategory.MONSTER)
                        .sized(0.6f, 0.6f).clientTrackingRange(10)
                        .build(Key("" + prefix + "_slime")));
    }



    // tip 飞行怪
    public static final DeferredHolder<EntityType<?>, EntityType<DemonEye>> DEMON_EYE = registerEntity("demon_eye", DemonEye::new,1.1F, 1.1F);
    public static final DeferredHolder<EntityType<?>, EntityType<AbstractMonster>> CRIMSON_KEMERA = registerSimpleMonster("crimson_kemera", FlyMonsterPrefab.CRIMSON_KEMERA_BUILDER,1.2f,1.2f);
    public static final DeferredHolder<EntityType<?>, EntityType<AbstractMonster>> EATER_OF_SOULS = registerSimpleMonster("eater_of_souls", FlyMonsterPrefab.EATER_OF_SOULS_BUILDER,1.2f,1.2f);
    public static final DeferredHolder<EntityType<?>, EntityType<AbstractMonster>> DRIPPLER = registerSimpleMonster("drippler", FlyMonsterPrefab.DRIPPLER_BUILDER,1.6f,1.6f);
    public static final DeferredHolder<EntityType<?>, EntityType<AbstractMonster>> WANDERING_EYE_FISH = registerSimpleMonster("wandering_eye_fish", FlyMonsterPrefab.WANDERING_EYE_FISH_BUILDER,1.4f,1.4f);
    public static final DeferredHolder<EntityType<?>, EntityType<AbstractMonster>> FLYING_FISH = registerSimpleMonster("flying_fish", FlyMonsterPrefab.FLYING_FISH_BUILDER,0.9F,0.9F);
    public static final DeferredHolder<EntityType<?>, EntityType<VisualNeuron>> VISUAL_NEURON = registerEntity("visual_neuron", VisualNeuron::new, 1.2f, 1.2f);
        // 蜜蜂
    public static final DeferredHolder<EntityType<?>, EntityType<Hornet>> HORNET = registerEntity("hornet", (e,l)->new Hornet(e,l, FlyMonsterPrefab.BEE_BUILDER.get().setHealth(32)), 0.8f, 1.2f);
    public static final DeferredHolder<EntityType<?>, EntityType<LittleHornet>> LITTLE_HORNET = registerEntity("little_hornet", LittleHornet::new,MobCategory.CREATURE, 0.8f, 1.2f);
        // 蝙蝠
    public static final DeferredHolder<EntityType<?>, EntityType<AbstractMonster>> CAVE_BAT = registerSimpleMonster("cave_bat", FlyMonsterPrefab.CAVE_BAT_BUILDER,1.6f,1.6f);
    public static final DeferredHolder<EntityType<?>, EntityType<AbstractMonster>> JUNGLE_BAT = registerSimpleMonster("jungle_bat", FlyMonsterPrefab.JUNGLE_BAT_BUILDER,1.6f,1.6f);
    public static final DeferredHolder<EntityType<?>, EntityType<AbstractMonster>> HELL_BAT = registerSimpleMonster("hell_bat", FlyMonsterPrefab.HELL_BAT_BUILDER,1.6f,1.6f);
    public static final DeferredHolder<EntityType<?>, EntityType<AbstractMonster>> ICE_BAT = registerSimpleMonster("ice_bat", FlyMonsterPrefab.ICE_BAT_BUILDER,1.6f,1.6f);
    public static final DeferredHolder<EntityType<?>, EntityType<AbstractMonster>> SPORE_BAT = registerSimpleMonster("spore_bat", FlyMonsterPrefab.SPORE_BAT_BUILDER,1.6f,1.6f);

    // tip 陆生怪
    public static final DeferredHolder<EntityType<?>, EntityType<Decayeder>> DECAYEDER = registerEntity("decayeder", Decayeder::new,1,1.8f);
    public static final DeferredHolder<EntityType<?>, EntityType<BloodySpore>> BLOODY_SPORE = registerEntity("bloody_spore", BloodySpore::new, 1,1.5f);
    public static final DeferredHolder<EntityType<?>, EntityType<BloodCrawler>> BLOOD_CRAWLER = registerEntity("blood_crawler", BloodCrawler::new, 1.8F, 1.2F);
    public static final DeferredHolder<EntityType<?>, EntityType<AbstractMonster>> FACE_MONSTER = registerSimpleMonster("face_monster", LandMonsterPrefab.FACE_MONSTER_BUILDER,0.75F,1.95F);
    public static final DeferredHolder<EntityType<?>, EntityType<AbstractMonster>> BLOOD_TUMORS = registerSimpleMonster("blood_tumors", LandMonsterPrefab.BLOOD_TUMORS,0.5F,0.5F);
    public static final DeferredHolder<EntityType<?>, EntityType<AbstractMonster>> BLOOD_ZOMBIE = registerSimpleMonster("blood_zombie", LandMonsterPrefab.BLOOD_ZOMBIE_BUILDER,0.75F,1.95F);
        // 蠕虫
    public static final DeferredHolder<EntityType<?>, EntityType<BaseWarm>> DEVOURER = registerEntity("devourer", (e,l)->new BaseWarm(e,l, AbstractPrefab.WARM_BUILDER.get().setHealth(52).setAttackDamage(8).setArmor(2)),2F,2F);
    public static final DeferredHolder<EntityType<?>, EntityType<BaseWarm>> TOMB_CRAWLER = registerEntity("tomb_crawler", (e,l)->new BaseWarm(e,l, AbstractPrefab.WARM_BUILDER.get().setHealth(16).setAttackDamage(4).setArmor(2)),2F,2F);
    public static final DeferredHolder<EntityType<?>, EntityType<BaseWarm>> GIANT_WORM = registerEntity("giant_worm", (e,l)->new BaseWarm(e,l, AbstractPrefab.WARM_BUILDER.get().setHealth(31).setAttackDamage(9).setArmor(3)),2F,2F);

    public static final DeferredHolder<EntityType<?>, EntityType<GiantShelly>> GIANT_SHELLY = registerEntity("giant_shelly", GiantShelly::new,0.8F,0.8F);
    // 宁芙
    public static final DeferredHolder<EntityType<?>, EntityType<Nymph>> NYMPH = registerEntity("nymph", Nymph::new,0.8F,1.95F);
    // 抓人草
    public static final DeferredHolder<EntityType<?>, EntityType<Snatcher>> SNATCHER = registerEntity("snatcher", (e,l)->new Snatcher(e,l, new AbstractPrefab(31,2,13,20,1,1).getPrefab()),1F,1F);
    public static final DeferredHolder<EntityType<?>, EntityType<Snatcher>> MAN_EATER = registerEntity("man_eater", (e,l)->new Snatcher(e,l, new AbstractPrefab(57,2,15,20,1,1).getPrefab()),1F,1F);



    // 用于调整包围盒
    public static DeferredHolder<EntityType<?>, EntityType<AbstractMonster>> registerSimpleMonster(String name, Supplier<AbstractMonster.Builder> builder, float width, float height) {
        return ENTITIES.register(name, () -> EntityType.Builder.<AbstractMonster>of((type,level)->new AbstractMonster(type,level,builder.get()), MobCategory.MONSTER).clientTrackingRange(10).setTrackingRange(50).sized(width,height).build(Key(name)));
    }
    public static DeferredHolder<EntityType<?>, EntityType<AbstractMonster>> registerSimpleMonster(String name, Supplier<AbstractMonster.Builder> builder) {
        return registerSimpleMonster(name, builder, 1, 1);
    }

/* *********************************************************************** */

    // tip 召唤物
    public static final DeferredHolder<EntityType<?>, EntityType<SummonSlime>> SUMMON_SLIME = registerEntity("slime_baby", SummonSlime::new ,0.5F,0.5F);
    public static final DeferredHolder<EntityType<?>, EntityType<SummonIronGolem>> SUMMON_IRON_GOLEM = registerEntity("i_32_iron_golem", SummonIronGolem::new,1.5F,3F);
    public static final DeferredHolder<EntityType<?>, EntityType<SummonHornet>> SUMMON_HORNET = registerEntity("hornet_baby", SummonHornet::new,0.5F,0.8F);


    // 坐骑
    public static final DeferredHolder<EntityType<?>, EntityType<RideableSlime>> RIDEABLE_SLIME = registerEntity("rideable_slime", RideableSlime::new,0.5F,0.5F);
    public static final DeferredHolder<EntityType<?>, EntityType<RideableBee>> RIDEABLE_BEE = registerEntity("rideable_bee", RideableBee::new,0.5F,0.5F);

    // tip Boss
    public static final DeferredHolder<EntityType<?>, EntityType<KingSlime>> KING_SLIME = ENTITIES.register("king_slime", () -> EntityType.Builder.<KingSlime>of(KingSlime::new, MobCategory.MONSTER).sized(0.6f, 0.6f).clientTrackingRange(10).build(Key("king_slime")));
    public static final DeferredHolder<EntityType<?>, EntityType<CrownOfKingSlimeModelEntity>> CROWN_OF_KING_SLIME_MODEL = ENTITIES.register("crown_of_king_slime_model", () -> EntityType.Builder.<CrownOfKingSlimeModelEntity>of(CrownOfKingSlimeModelEntity::new, MobCategory.MISC).sized(0.0F, 0.0F).clientTrackingRange(10).build(Key("crown_of_king_slime_model")));

    public static final DeferredHolder<EntityType<?>, EntityType<EyeOfCthulhu>> EYE_OF_CTHULHU = registerEntity("eye_of_cthulhu", EyeOfCthulhu::new, 2.04F, 2.04F);
    public static final DeferredHolder<EntityType<?>, EntityType<EaterOfWorldsSegment>> EATER_OF_WORLD_SEGMENT = registerEntity("eater_of_worlds_segment", EaterOfWorldsSegment::new, 2F, 2F);
    public static final DeferredHolder<EntityType<?>, EntityType<EaterOfWorlds>> EATER_OF_WORLDS = registerEntity("eater_of_worlds", EaterOfWorlds::new, 3F, 2F);
    public static final DeferredHolder<EntityType<?>, EntityType<BrainOfCthulhu>> BRAIN_OF_CTHULHU = registerEntity("brain_of_cthulhu", BrainOfCthulhu::new, 4F, 4F);
    public static final DeferredHolder<EntityType<?>, EntityType<BrainFake>> BRAIN_FAKE = registerEntity("brain_fake", BrainFake::new, 4F, 4F);
    public static final DeferredHolder<EntityType<?>, EntityType<QueenBee>> QUEEN_BEE = registerEntity("queen_bee", QueenBee::new, 2.5F, 2.5F);
    public static final DeferredHolder<EntityType<?>, EntityType<Skeletron>> SKELETRON = registerEntity("skeletron", Skeletron::new, 2.3F, 2.3F);
    public static final DeferredHolder<EntityType<?>, EntityType<SkeletronHand>> SKELETRON_HAND = registerEntity("skeletron_hand", SkeletronHand::new, 2F, 1F);


    public static <T extends Mob> DeferredHolder<EntityType<?>,EntityType<T>> registerEntity(String name, EntityType.EntityFactory<T> entityFactory, float width, float height){
        return registerEntity(name, entityFactory, MobCategory.MONSTER, width, height);
    }

    public static <T extends Mob> DeferredHolder<EntityType<?>,EntityType<T>> registerEntity(String name, EntityType.EntityFactory<T> entityFactory, MobCategory category, float width, float height){
        return ENTITIES.register(name, () -> EntityType.Builder.of(entityFactory, category).sized(width, height).clientTrackingRange(10).build(Key(name)));
    }


/* *********************************************************************** */

    // tip 弹幕
    // 回旋镖
    public static final DeferredHolder<EntityType<?>, EntityType<BoomerangProjectile>> BOOMERANG_PROJECTILE = ENTITIES.register("boomerang_projectile", () -> EntityType.Builder.<BoomerangProjectile>of(BoomerangProjectile::new, MobCategory.MISC).sized(0.5F, 0.5F).build(Key("boomerang_projectile")));


    public static final DeferredHolder<EntityType<?>, EntityType<ThrowableProj>> CABBAGE_PROJ = registerProj("cabbage_proj",(e,l)->
            new ThrowableProj(e,l),0.5F,0.5F);

    public static final DeferredHolder<EntityType<?>, EntityType<LineProj>> BEE_STICK_PROJ = registerProj("bee_stick_proj",(e, l)->
            new LineProj(e,l).setTexture(TerraEntity.space("textures/entity/model/stinger.png")),0.5F,0.5F);
    public static final DeferredHolder<EntityType<?>, EntityType<LineProj>> SUMMON_BEE_STICK_PROJ = registerProj("summon_bee_stick_proj",(e, l)->
            new SummonBeeStick(e,l).setTexture(TerraEntity.space("textures/entity/model/stinger.png")),0.5F,0.5F);
    public static final DeferredHolder<EntityType<?>, EntityType<SkullProjectile>> SKULL = registerProj("skull", SkullProjectile::new,0.5F,0.5F);

    // 鞭子
    public static final DeferredHolder<EntityType<?>,EntityType<WhipEntity>> WHIP_PROJECTILE = ENTITIES.register("whip_projectile",() -> EntityType.Builder.<WhipEntity>of((e, l)->
            new WhipEntity(e,l) , MobCategory.MISC).updateInterval(1).clientTrackingRange(1).sized(0.5F,0.5F).build(Key("whip_projectile")));



    public static <T extends Projectile> DeferredHolder<EntityType<?>, EntityType<T>> registerProj(String name, EntityType.EntityFactory<T> entityFactory, float w, float h) {
        return ENTITIES.register(name, () -> EntityType.Builder.of(entityFactory , MobCategory.MISC).clientTrackingRange(10).sized(w,h).build(Key(name)));
    }
    public static <T extends Projectile> DeferredHolder<EntityType<?>, EntityType<T>> registerProj(String name, EntityType.EntityFactory<T> entityFactory) {
        return registerProj(name,entityFactory,1,1);
    }

    // tip 渲染器
    @OnlyIn(Dist.CLIENT)
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(BLUE_SLIME.get(), c -> new CustomSlimeRenderer(c, "blue"));
        event.registerEntityRenderer(GREEN_SLIME.get(), c -> new CustomSlimeRenderer(c, "green"));
        event.registerEntityRenderer(PINK_SLIME.get(), c -> new CustomSlimeRenderer(c, "pink"));
        event.registerEntityRenderer(CORRUPTED_SLIME.get(), c -> new CustomSlimeRenderer(c, "corrupted"));
        event.registerEntityRenderer(DESERT_SLIME.get(), c -> new CustomSlimeRenderer(c, "desert"));
        event.registerEntityRenderer(JUNGLE_SLIME.get(), c -> new CustomSlimeRenderer(c, "jungle"));
        event.registerEntityRenderer(EVIL_SLIME.get(), c -> new CustomSlimeRenderer(c, "evil"));
        event.registerEntityRenderer(ICE_SLIME.get(), c -> new CustomSlimeRenderer(c, "ice"));
        event.registerEntityRenderer(LAVA_SLIME.get(), c -> new CustomSlimeRenderer(c, "lava"));
        event.registerEntityRenderer(LUMINOUS_SLIME.get(), c -> new CustomSlimeRenderer(c, "luminous"));
        event.registerEntityRenderer(CRIMSON_SLIME.get(), c -> new CustomSlimeRenderer(c, "crimson"));
        event.registerEntityRenderer(PURPLE_SLIME.get(), c -> new CustomSlimeRenderer(c, "purple"));
        event.registerEntityRenderer(RED_SLIME.get(), c -> new CustomSlimeRenderer(c, "red"));
        event.registerEntityRenderer(TROPIC_SLIME.get(), c -> new CustomSlimeRenderer(c, "tropic"));
        event.registerEntityRenderer(YELLOW_SLIME.get(), c -> new CustomSlimeRenderer(c, "yellow"));
        event.registerEntityRenderer(HONEY_SLIME.get(), c -> new CustomSlimeRenderer(c, "honey"));
        event.registerEntityRenderer(BLACK_SLIME.get(), c -> new CustomSlimeRenderer(c, "black"));


        event.registerEntityRenderer(CRIMSON_KEMERA.get(), c-> new GeoNormalRenderer<>(c,CRIMSON_KEMERA.getId(),true));
        event.registerEntityRenderer(EATER_OF_SOULS.get(), c-> new GeoNormalRenderer<>(c,EATER_OF_SOULS.getId(),true));
        event.registerEntityRenderer(DRIPPLER.get(), c-> new GeoNormalRenderer<>(c,DRIPPLER.getId(),false,2f,0));
        event.registerEntityRenderer(WANDERING_EYE_FISH.get(), c-> new GeoNormalRenderer<>(c,WANDERING_EYE_FISH.getId(),false,1.5f,0));
        event.registerEntityRenderer(FLYING_FISH.get(), c-> new GeoNormalRenderer<>(c,FLYING_FISH.getId(),true,0.75f,0));


        event.registerEntityRenderer(DEMON_EYE.get(), DemonEyeRenderer::new);
        if (!ClientConfig.ENABLE_NON_SPIDER_MODEL.get()) {
            event.registerEntityRenderer(BLOOD_CRAWLER.get(), c -> new GeoNormalRenderer<>(c, BLOOD_CRAWLER.getId()));
        }
        event.registerEntityRenderer(BLOODY_SPORE.get(), BloodySporeRenderer::new);
        event.registerEntityRenderer(DECAYEDER.get(), DecayederRenderer::new);


        event.registerEntityRenderer(FACE_MONSTER.get(), c-> new GeoNormalRenderer<>(c,FACE_MONSTER.getId(),false));
        event.registerEntityRenderer(BLOOD_TUMORS.get(), c-> new GeoNormalRenderer<>(c,BLOOD_TUMORS.getId(),false));
        event.registerEntityRenderer(BLOOD_ZOMBIE.get(), c-> new GeoNormalRenderer<>(c,BLOOD_ZOMBIE.getId(),false));
        event.registerEntityRenderer(DEVOURER.get(), c-> new GeoWormRenderer<>(c, DEVOURER.getId(),2.0f, 0.0f));
        event.registerEntityRenderer(GIANT_WORM.get(), c-> new GeoWormRenderer<>(c, GIANT_WORM.getId(),2.0f, 0.0f));
        event.registerEntityRenderer(TOMB_CRAWLER.get(), c-> new GeoWormRenderer<>(c, TOMB_CRAWLER.getId(),2.0f, 0.0f));
        event.registerEntityRenderer(GIANT_SHELLY.get(), c-> new GeoNormalRenderer<>(c, new GiantShellyModel<>(GIANT_SHELLY.getId()),false,2,0));
        // bat
        event.registerEntityRenderer(CAVE_BAT.get(), c-> new GeoNormalRenderer<>(c,CAVE_BAT.getId(),false));
        event.registerEntityRenderer(JUNGLE_BAT.get(), c-> new GeoNormalRenderer<>(c, JUNGLE_BAT.getId(),false));
        event.registerEntityRenderer(HELL_BAT.get(), c-> new GeoNormalRenderer<>(c,HELL_BAT.getId(),false));
        event.registerEntityRenderer(ICE_BAT.get(), c-> new GeoNormalRenderer<>(c,ICE_BAT.getId(),false));
        event.registerEntityRenderer(SPORE_BAT.get(), c-> new GeoNormalRenderer<>(c,SPORE_BAT.getId(),false));

        // bee
        event.registerEntityRenderer(LITTLE_HORNET.get(), c->new GeoNormalRenderer<>(c, LITTLE_HORNET.getId(),true, 1, 0.5f));
        event.registerEntityRenderer(HORNET.get(), c->new GeoNormalRenderer<>(c, HORNET.getId(),true, 1, 0.5f));

        event.registerEntityRenderer(NYMPH.get(), c->new GeoNormalRenderer<>(c, new NymphModel<>(NYMPH.getId()),false,1,0f));
        event.registerEntityRenderer(SNATCHER.get(), c->new SnatcherRenderer<>(c, SNATCHER.getId()));
        event.registerEntityRenderer(MAN_EATER.get(), c->new SnatcherRenderer<>(c, MAN_EATER.getId()));


        // boss
        event.registerEntityRenderer(KING_SLIME.get(), KingSlimeRenderer::new);
        event.registerEntityRenderer(EYE_OF_CTHULHU.get(), c->new GeoBossRenderer<>(c,new GeoBossModel<>(EYE_OF_CTHULHU),1,0.5f, true));
        event.registerEntityRenderer(EATER_OF_WORLD_SEGMENT.get(), c-> new EaterOfWorldSegmentRenderer(c,2.2f, 0f));
        event.registerEntityRenderer(EATER_OF_WORLDS.get(), c->new GeoBossRenderer<>(c,new GeoBossModel<>(EATER_OF_WORLDS),2.2f,0, true));
        event.registerEntityRenderer(BRAIN_OF_CTHULHU.get(), c->new BrainOfCthulhuRenderer(c,new GeoBossModel<>(BRAIN_OF_CTHULHU)));
        event.registerEntityRenderer(VISUAL_NEURON.get(), c->new GeoNormalRenderer<>(c, VISUAL_NEURON.getId(),true));
        event.registerEntityRenderer(BRAIN_FAKE.get(), c->new BrainOfCthulhuRenderer(c,new GeoBossModel<>(BRAIN_OF_CTHULHU)));
        event.registerEntityRenderer(QUEEN_BEE.get(), c->new QueenBeeRenderer(c,new GeoBossModel<>(QUEEN_BEE)));
        event.registerEntityRenderer(SKELETRON.get(), c->new SkeletronRenderer(c,new GeoBossModel<>(SKELETRON)));
        event.registerEntityRenderer(SKELETRON_HAND.get(), c->new SkeletronHandRenderer(c,new SkeletronHandModel()));
        event.registerEntityRenderer(SKULL.get(), SkullProjectileRenderer::new);

        // sommon
        event.registerEntityRenderer(SUMMON_SLIME.get(), c-> new GeoNormalRenderer<>(c, SUMMON_SLIME.getId().withPrefix("summon/"),false));
        event.registerEntityRenderer(SUMMON_IRON_GOLEM.get(), IronGolemRenderer::new);
        event.registerEntityRenderer(SUMMON_HORNET.get(), c->new GeoNormalRenderer<>(c, HORNET.getId(),true, 0.6f, 0.5f));

        // ridable
        event.registerEntityRenderer(RIDEABLE_SLIME.get(), c->new GeoNormalRenderer<>(c, RIDEABLE_SLIME.getId().withPrefix("rideable/"),false, 1.6f,0));
        event.registerEntityRenderer(RIDEABLE_BEE.get(), c->new GeoNormalRenderer<>(c, RIDEABLE_BEE.getId().withPrefix("rideable/"),false));


        // 鞭子
        event.registerEntityRenderer(WHIP_PROJECTILE.get(), WhipEntityRenderer::new);
    }

    // tip 属性
    @SubscribeEvent
    public static void registerEntityAttributes(EntityAttributeCreationEvent event) {
        AttributeSupplier.Builder genericBossAttribs = Monster.createMobAttributes()
                .add(Attributes.FOLLOW_RANGE, 100)
                .add(Attributes.MAX_HEALTH, 100)
                .add(Attributes.GRAVITY, 0)
                .add(Attributes.MOVEMENT_SPEED, 0.15f)
                .add(Attributes.ATTACK_DAMAGE, 1)
                .add(Attributes.KNOCKBACK_RESISTANCE, 10)
                .add(Attributes.ATTACK_KNOCKBACK, 1);



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



        // boss
        event.put(KING_SLIME.get(), KingSlime.createSlimeAttributes().build());
        event.put(EYE_OF_CTHULHU.get(), AbstractTerraBossBase.createAttributes().build());
        event.put(EATER_OF_WORLD_SEGMENT.get(), AbstractTerraBossBase.createAttributes().build());
        event.put(EATER_OF_WORLDS.get(), AbstractTerraBossBase.createAttributes().build());
        event.put(BRAIN_OF_CTHULHU.get(), AbstractTerraBossBase.createAttributes().build());
        event.put(VISUAL_NEURON.get(), AbstractMonster.createAttributes().build());
        event.put(BRAIN_FAKE.get(), AbstractTerraBossBase.createAttributes().build());
        event.put(QUEEN_BEE.get(), AbstractTerraBossBase.createAttributes().build());
        event.put(SKELETRON.get(), AbstractTerraBossBase.createAttributes().build());
        event.put(SKELETRON_HAND.get(), AbstractTerraBossBase.createAttributes().build());

        // sommon
        event.put(SUMMON_SLIME.get(), AbstractTerraBossBase.createAttributes().build());
        event.put(SUMMON_IRON_GOLEM.get(), IronGolem.createAttributes().build());
        event.put(SUMMON_HORNET.get(), AbstractMonster.createAttributes().build());

        // ridable
        event.put(RIDEABLE_SLIME.get(), AbstractMonster.createAttributes().build());
        event.put(RIDEABLE_BEE.get(), AbstractMonster.createAttributes().build());


    }

    //tip 生成位置
    @SubscribeEvent
    public static void spawnPlacementRegister(RegisterSpawnPlacementsEvent event) {
        event.register(BLUE_SLIME.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, BaseSlime::checkSlimeSpawn, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(GREEN_SLIME.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, BaseSlime::checkSlimeSpawn, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(PURPLE_SLIME.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, BaseSlime::checkSlimeSpawn, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(PINK_SLIME.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, BaseSlime::checkSlimeSpawn, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(CORRUPTED_SLIME.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, BaseSlime::checkSlimeSpawn, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(DESERT_SLIME.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, BaseSlime::checkSlimeSpawn, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(JUNGLE_SLIME.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, BaseSlime::checkSlimeSpawn, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(ICE_SLIME.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, BaseSlime::checkSlimeSpawn, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(TROPIC_SLIME.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, BaseSlime::checkSlimeSpawn, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(CRIMSON_SLIME.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, BaseSlime::checkSlimeSpawn, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(YELLOW_SLIME.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, BaseSlime::checkSlimeSpawn, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(RED_SLIME.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, BaseSlime::checkSlimeSpawn, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(BLACK_SLIME.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, BaseSlime::checkSlimeSpawn, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(LAVA_SLIME.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, BaseSlime::checkSlimeSpawn, RegisterSpawnPlacementsEvent.Operation.REPLACE);


        // land
        event.register(BLOOD_CRAWLER.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, BloodCrawler::checkBloodCrawlerSpawn, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(BLOOD_ZOMBIE.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AbstractMonster::checkRoutineMonsterSpawn, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(BLOODY_SPORE.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, BloodySpore::checkBloodySporeSpawn, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(FACE_MONSTER.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AbstractMonster::checkRoutineMonsterSpawn, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(DECAYEDER.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AbstractMonster::checkRoutineMonsterSpawn, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(GIANT_SHELLY.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AbstractMonster::checkUndergroundMonsterSpawn, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(NYMPH.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AbstractMonster::checkUndergroundMonsterSpawn, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(SNATCHER.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AbstractMonster::checkGroundSpawn, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(MAN_EATER.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AbstractMonster::checkUndergroundMonsterSpawn, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        // fly
        event.register(DEMON_EYE.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, DemonEye::checkDemonEyeSpawn,  RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(FLYING_FISH.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AbstractMonster::checkFlyingFishSpawn, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(CRIMSON_KEMERA.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AbstractMonster::checkRoutineMonsterSpawn, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(EATER_OF_SOULS.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AbstractMonster::checkRoutineMonsterSpawn, RegisterSpawnPlacementsEvent.Operation.REPLACE);

        // worm
        event.register(DEVOURER.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AbstractMonster::checkRoutineMonsterSpawn, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(GIANT_WORM.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AbstractMonster::checkUndergroundMonsterSpawn, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(TOMB_CRAWLER.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AbstractMonster::checkUndergroundMonsterSpawn, RegisterSpawnPlacementsEvent.Operation.REPLACE);

        // bee
        event.register(HORNET.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AbstractMonster::checkUndergroundMonsterSpawn, RegisterSpawnPlacementsEvent.Operation.REPLACE);

        // bat
        event.register(CAVE_BAT.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AbstractMonster::checkUndergroundMonsterSpawn, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(JUNGLE_BAT.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AbstractMonster::checkRoutineMonsterSpawn, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(HELL_BAT.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AbstractMonster::checkNetherMonsterSpawn, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(ICE_BAT.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AbstractMonster::checkUndergroundMonsterSpawn, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(SPORE_BAT.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AbstractMonster::checkUndergroundMonsterSpawn, RegisterSpawnPlacementsEvent.Operation.REPLACE);



    }


    public static String Key(String key){
        return MODID + ":" + key;
    }
}
