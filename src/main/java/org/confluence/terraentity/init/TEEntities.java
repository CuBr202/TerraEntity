package org.confluence.terraentity.init;

import net.minecraft.client.renderer.entity.IronGolemRenderer;
import net.minecraft.client.renderer.entity.SkeletonRenderer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.levelgen.Heightmap;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.client.boss.model.GeoBossModel;
import org.confluence.terraentity.client.boss.renderer.BrainOfCthulhuRenderer;
import org.confluence.terraentity.client.boss.renderer.GeoBossRenderer;
import org.confluence.terraentity.client.boss.renderer.EaterOfWorldSegmentRenderer;
import org.confluence.terraentity.client.boss.renderer.QueenBeeRenderer;
import org.confluence.terraentity.client.entity.model.GiantShellyModel;
import org.confluence.terraentity.client.entity.renderer.*;
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
import org.confluence.terraentity.entity.proj.BaseProj;
import org.confluence.terraentity.entity.proj.LineProj;
import org.confluence.terraentity.entity.proj.ThrowableProj;
import org.confluence.terraentity.entity.summon.SummonIronGolem;
import org.confluence.terraentity.entity.summon.SummonSlime;

import java.util.function.Supplier;

import static org.confluence.terraentity.TerraEntity.MODID;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class TEEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, MODID);


    // tip 史莱姆
    public static final RegistryObject<EntityType<BaseSlime>> BLUE_SLIME = registerSlime("blue", 0x73bcf4, 2);
    public static final RegistryObject<EntityType<BaseSlime>> GREEN_SLIME = registerSlime("green", 0x48E920, 2);
    public static final RegistryObject<EntityType<BaseSlime>> PINK_SLIME = registerSlime("pink", 0xFF87B3, 1);
    public static final RegistryObject<EntityType<BaseSlime>> CORRUPTED_SLIME = registerSlime("corrupted", 0xC91717, 2);
    public static final RegistryObject<EntityType<BaseSlime>> DESERT_SLIME = registerSlime("desert", 0xDCC59a, 2);
    public static final RegistryObject<EntityType<BaseSlime>> JUNGLE_SLIME = registerSlime("jungle", 0x9ae920, 2);
    public static final RegistryObject<EntityType<BaseSlime>> EVIL_SLIME = registerSlime("evil", 0xFF00FF, 2);
    public static final RegistryObject<EntityType<BaseSlime>> ICE_SLIME = registerSlime("ice", 0xB3F0EA, 2);
    public static final RegistryObject<EntityType<BaseSlime>>  LAVA_SLIME = ENTITIES.register("lava_slime", () -> EntityType.Builder.<BaseSlime>of((entityType, level) -> new BaseSlime(entityType, level, 0xFFB150, 2), MobCategory.MONSTER).sized(0.6F, 0.6F).clientTrackingRange(10).fireImmune().build(Key("lava_slime")));
    public static final RegistryObject<EntityType<BaseSlime>> LUMINOUS_SLIME = registerSlime("luminous", 0xFFFFFF, 2);
    public static final RegistryObject<EntityType<BaseSlime>> CRIMSON_SLIME = registerSlime("crimson", 0x8B4949, 2);
    public static final RegistryObject<EntityType<BaseSlime>> PURPLE_SLIME = registerSlime("purple", 0xf334f8, 2);
    public static final RegistryObject<EntityType<BaseSlime>> RED_SLIME = registerSlime("red", 0xf83434, 2);
    public static final RegistryObject<EntityType<BaseSlime>> TROPIC_SLIME = registerSlime("tropic", 0x73bcf4, 2);
    public static final RegistryObject<EntityType<BaseSlime>> YELLOW_SLIME = registerSlime("yellow", 0xf8e234, 2);
    public static final RegistryObject<EntityType<HoneySlime>> HONEY_SLIME = ENTITIES.register("honey_slime", () -> EntityType.Builder.<HoneySlime>of((entityType, level) -> new HoneySlime(entityType, level, 0xf8e234), MobCategory.MONSTER).sized(2.04F, 2.04F).clientTrackingRange(10).build(Key("honey_slime")));
    public static final RegistryObject<EntityType<BlackSlime>> BLACK_SLIME = ENTITIES.register("black_slime", () -> EntityType.Builder.of(BlackSlime::new, MobCategory.MONSTER).sized(2.04F, 2.04F).clientTrackingRange(10).build(Key("black_slime")));
    private static RegistryObject<EntityType<BaseSlime>> registerSlime(String prefix, int color, int size) {
        return ENTITIES.register(prefix + "_slime", () -> EntityType.Builder.<BaseSlime>of((entityType, level) -> new BaseSlime(entityType, level, color, size), MobCategory.MONSTER).sized(2.04F, 2.04F).clientTrackingRange(10).build(Key("" + prefix + "_slime")));
    }



    // tip 飞行怪
    public static final RegistryObject<EntityType<DemonEye>> DEMON_EYE = ENTITIES.register("demon_eye", () -> EntityType.Builder.of(DemonEye::new, MobCategory.MONSTER).sized(0.6F, 0.6F).clientTrackingRange(10).build(Key("demon_eye")));
    public static final RegistryObject<EntityType<AbstractMonster>> CRIMSON_KEMERA = registerSimpleMonster("crimson_kemera", FlyMonsterPrefab.CRIMSON_KEMERA_BUILDER,1.2f,1.2f);
    public static final RegistryObject<EntityType<AbstractMonster>> EATER_OF_SOULS = registerSimpleMonster("eater_of_souls", FlyMonsterPrefab.EATER_OF_SOULS_BUILDER,1.2f,1.2f);
    public static final RegistryObject<EntityType<AbstractMonster>> DRIPPLER = registerSimpleMonster("drippler", FlyMonsterPrefab.DRIPPLER_BUILDER,1.6f,1.6f);
    public static final RegistryObject<EntityType<AbstractMonster>> FLYING_FISH = registerSimpleMonster("flying_fish", FlyMonsterPrefab.FLYING_FISH_BUILDER,0.75F,0.75F);
    public static final RegistryObject<EntityType<VisualNeuron>> VISUAL_NEURON = registerEntity("visual_neuron", VisualNeuron::new, 1.2f, 1.2f);
    // 蜜蜂
    public static final RegistryObject<EntityType<Hornet>> HORNET = registerEntity("hornet", (e,l)->new Hornet(e,l, FlyMonsterPrefab.BEE_BUILDER.get().setHealth(32)), 0.8f, 1.2f);
    public static final RegistryObject<EntityType<LittleHornet>> LITTLE_HORNET = registerEntity("little_hornet", LittleHornet::new, 0.8f, 1.2f);
    // 蝙蝠
    public static final RegistryObject<EntityType<AbstractMonster>> CAVE_BAT = registerSimpleMonster("cave_bat", FlyMonsterPrefab.CAVE_BAT_BUILDER,1.6f,1.6f);
    public static final RegistryObject<EntityType<AbstractMonster>> JUNGLE_BAT = registerSimpleMonster("jungle_bat", FlyMonsterPrefab.JUNGLE_BAT_BUILDER,1.6f,1.6f);
    public static final RegistryObject<EntityType<AbstractMonster>> HELL_BAT = registerSimpleMonster("hell_bat", FlyMonsterPrefab.HELL_BAT_BUILDER,1.6f,1.6f);
    public static final RegistryObject<EntityType<AbstractMonster>> ICE_BAT = registerSimpleMonster("ice_bat", FlyMonsterPrefab.ICE_BAT_BUILDER,1.6f,1.6f);
    public static final RegistryObject<EntityType<AbstractMonster>> SPORE_BAT = registerSimpleMonster("spore_bat", FlyMonsterPrefab.CAVE_BAT_BUILDER,1.6f,1.6f);

    // tip 陆生怪
    public static final RegistryObject<EntityType<Decayeder>> DECAYEDER = ENTITIES.register("decayeder", () -> EntityType.Builder.of(Decayeder::new, MobCategory.MONSTER).build(Key("decayeder")));
    public static final RegistryObject<EntityType<BloodySpore>> BLOODY_SPORE = ENTITIES.register("bloody_spore", () -> EntityType.Builder.of(BloodySpore::new, MobCategory.MONSTER).build(Key("bloody_spore")));
    public static final RegistryObject<EntityType<BloodCrawler>> BLOOD_CRAWLER = ENTITIES.register("blood_crawler", () -> EntityType.Builder.of(BloodCrawler::new, MobCategory.MONSTER).sized(1.8F, 1.2F).clientTrackingRange(10).build(Key("blood_crawler")));
    public static final RegistryObject<EntityType<AbstractMonster>> FACE_MONSTER = registerSimpleMonster("face_monster", LandMonsterPrefab.FACE_MONSTER_BUILDER,0.75F,1.95F);
    public static final RegistryObject<EntityType<AbstractMonster>> BLOOD_TUMORS = registerSimpleMonster("blood_tumors", LandMonsterPrefab.BLOOD_TUMORS,0.5F,0.5F);
    public static final RegistryObject<EntityType<AbstractMonster>> BLOOD_ZOMBIE = registerSimpleMonster("blood_zombie", LandMonsterPrefab.BLOOD_ZOMBIE_BUILDER,0.75F,1.95F);
    // 蠕虫
    public static final RegistryObject<EntityType<BaseWarm>> DEVOURER = registerEntity("devourer", (e,l)->new BaseWarm(e,l, AbstractPrefab.WARM_BUILDER.get()),2F,2F);
    public static final RegistryObject<EntityType<BaseWarm>> TOMB_CRAWLER = registerEntity("tomb_crawler", (e,l)->new BaseWarm(e,l, AbstractPrefab.WARM_BUILDER.get().setHealth(16).setAttackDamage(4).setArmor(2)),2F,2F);
    public static final RegistryObject<EntityType<BaseWarm>> GIANT_WORM = registerEntity("giant_worm", (e,l)->new BaseWarm(e,l, AbstractPrefab.WARM_BUILDER.get().setHealth(31).setAttackDamage(9).setArmor(3)),2F,2F);

    public static final RegistryObject<EntityType<GiantShelly>> GIANT_SHELLY = registerEntity("giant_shelly", GiantShelly::new,0.8F,0.8F);




    // 用于调整包围盒
    public static RegistryObject<EntityType<AbstractMonster>> registerSimpleMonster(String name, Supplier<AbstractMonster.Builder> builder, float width, float height) {
        return ENTITIES.register(name, () -> EntityType.Builder.<AbstractMonster>of((type,level)->new AbstractMonster(type,level,builder.get()), MobCategory.MONSTER).clientTrackingRange(10).setTrackingRange(50).sized(width,height).build(Key(name)));
    }
    public static RegistryObject<EntityType<AbstractMonster>> registerSimpleMonster(String name, Supplier<AbstractMonster.Builder> builder) {
        return registerSimpleMonster(name, builder, 1, 1);
    }

/* *********************************************************************** */

    // tip 召唤物
    public static final RegistryObject<EntityType<SummonSlime>> SUMMON_SLIME = registerEntity("slime_baby", SummonSlime::new ,0.5F,0.5F);
    public static final RegistryObject<EntityType<SummonIronGolem>> SUMMON_IRON_GOLEM = registerEntity("i_32_iron_golem", SummonIronGolem::new,1.5F,3F);



    // tip Boss
    public static final RegistryObject<EntityType<KingSlime>> KING_SLIME = ENTITIES.register("king_slime", () -> EntityType.Builder.<KingSlime>of(KingSlime::new, MobCategory.MONSTER).sized(2.04F, 2.04F).clientTrackingRange(10).build(Key("king_slime")));
    public static final RegistryObject<EntityType<CrownOfKingSlimeModelEntity>> CROWN_OF_KING_SLIME_MODEL = ENTITIES.register("crown_of_king_slime_model", () -> EntityType.Builder.<CrownOfKingSlimeModelEntity>of(CrownOfKingSlimeModelEntity::new, MobCategory.MISC).sized(0.0F, 0.0F).clientTrackingRange(10).build(Key("crown_of_king_slime_model")));

    public static final RegistryObject<EntityType<EyeOfCthulhu>> EYE_OF_CTHULHU = registerEntity("eye_of_cthulhu", EyeOfCthulhu::new, 2.04F, 2.04F);
    public static final RegistryObject<EntityType<EaterOfWorldsSegment>> EATER_OF_WORLD_SEGMENT = registerEntity("eater_of_worlds_segment", EaterOfWorldsSegment::new, 2F, 2F);
    public static final RegistryObject<EntityType<EaterOfWorlds>> EATER_OF_WORLDS = registerEntity("eater_of_worlds", EaterOfWorlds::new, 3F, 2F);
    public static final RegistryObject<EntityType<BrainOfCthulhu>> BRAIN_OF_CTHULHU = registerEntity("brain_of_cthulhu", BrainOfCthulhu::new, 4F, 4F);
    public static final RegistryObject<EntityType<BrainFake>> BRAIN_FAKE = registerEntity("brain_fake", BrainFake::new, 4F, 4F);
    public static final RegistryObject<EntityType<QueenBee>> QUEEN_BEE = registerEntity("queen_bee", QueenBee::new, 2.5F, 2.5F);



    public static <T extends Mob> RegistryObject<EntityType<T>> registerEntity(String name, EntityType.EntityFactory<T> entityFactory, float width, float height){
        return ENTITIES.register(name, () -> EntityType.Builder.of(entityFactory, MobCategory.MONSTER).sized(width, height).clientTrackingRange(10).build(Key(name)));
    }

    public static <T extends Mob> RegistryObject<EntityType<T>> registerEntity(String name, EntityType.EntityFactory<T> entityFactory, MobCategory category, float width, float height){
        return ENTITIES.register(name, () -> EntityType.Builder.of(entityFactory, category).sized(width, height).clientTrackingRange(10).build(Key(name)));
    }


/* *********************************************************************** */

    // tip 弹幕
    public static final RegistryObject<EntityType<ThrowableProj>> CABBAGE_PROJ = registerProj("cabbage_proj",(e,l)->
            new ThrowableProj(e,l),0.5F,0.5F);

    public static final RegistryObject<EntityType<LineProj>> BEE_STICK_PROJ = registerProj("bee_stick_proj",(e, l)->
            new LineProj(e,l).setTexture(TerraEntity.space("textures/entity/model/stinger.png")),0.5F,0.5F);



    public static <T extends BaseProj<T>> RegistryObject<EntityType<T>> registerProj(String name, EntityType.EntityFactory<T> entityFactory,float w,float h) {
        return ENTITIES.register(name, () -> EntityType.Builder.of(entityFactory , MobCategory.MISC).clientTrackingRange(10).sized(w,h).build("rhyme:entity.proj."+name));
    }
    public static <T extends BaseProj<T>> RegistryObject<EntityType<T>> registerProj(String name, EntityType.EntityFactory<T> entityFactory) {
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
        event.registerEntityRenderer(FLYING_FISH.get(), c-> new GeoNormalRenderer<>(c,FLYING_FISH.getId(),true,0.75f,0));


        event.registerEntityRenderer(DEMON_EYE.get(), DemonEyeRenderer::new);
        event.registerEntityRenderer(BLOOD_CRAWLER.get(), c-> new GeoNormalRenderer<>(c,BLOOD_CRAWLER.getId()));
        event.registerEntityRenderer(BLOODY_SPORE.get(), BloodySporeRenderer::new);
        event.registerEntityRenderer(DECAYEDER.get(), SkeletonRenderer::new);  //todo


        event.registerEntityRenderer(FACE_MONSTER.get(), c-> new GeoNormalRenderer<>(c,FACE_MONSTER.getId(),false));
        event.registerEntityRenderer(BLOOD_TUMORS.get(), c-> new GeoNormalRenderer<>(c,BLOOD_TUMORS.getId(),false));
        event.registerEntityRenderer(BLOOD_ZOMBIE.get(), c-> new GeoNormalRenderer<>(c,BLOOD_ZOMBIE.getId(),false));
        event.registerEntityRenderer(DEVOURER.get(), c-> new GeoWormRenderer<>(c, DEVOURER.getId(),2.0f, 0.0f));
        event.registerEntityRenderer(GIANT_WORM.get(), c-> new GeoWormRenderer<>(c, GIANT_WORM.getId(),2.0f, 0.0f));
        event.registerEntityRenderer(TOMB_CRAWLER.get(), c-> new GeoWormRenderer<>(c, TOMB_CRAWLER.getId(),2.0f, 0.0f));
        event.registerEntityRenderer(GIANT_SHELLY.get(), c-> new GeoNormalRenderer<>(c, new GiantShellyModel<>(GIANT_SHELLY.getId()),false,2,0));
        // 蝙蝠
        event.registerEntityRenderer(CAVE_BAT.get(), c-> new GeoNormalRenderer<>(c,CAVE_BAT.getId(),false));
        event.registerEntityRenderer(JUNGLE_BAT.get(), c-> new GeoNormalRenderer<>(c, JUNGLE_BAT.getId(),false));
        event.registerEntityRenderer(HELL_BAT.get(), c-> new GeoNormalRenderer<>(c,HELL_BAT.getId(),false));
        event.registerEntityRenderer(ICE_BAT.get(), c-> new GeoNormalRenderer<>(c,ICE_BAT.getId(),false));
        event.registerEntityRenderer(SPORE_BAT.get(), c-> new GeoNormalRenderer<>(c,SPORE_BAT.getId(),false));

        // boss
        event.registerEntityRenderer(KING_SLIME.get(), KingSlimeRenderer::new);
        event.registerEntityRenderer(EYE_OF_CTHULHU.get(), c->new GeoBossRenderer<>(c,new GeoBossModel<>(EYE_OF_CTHULHU),1,0.5f, true));
        event.registerEntityRenderer(EATER_OF_WORLD_SEGMENT.get(), c-> new EaterOfWorldSegmentRenderer(c,2.2f, 0f));
        event.registerEntityRenderer(EATER_OF_WORLDS.get(), c->new GeoBossRenderer<>(c,new GeoBossModel<>(EATER_OF_WORLDS),2.2f,0, true));
        event.registerEntityRenderer(BRAIN_OF_CTHULHU.get(), c->new BrainOfCthulhuRenderer(c,new GeoBossModel<>(BRAIN_OF_CTHULHU)));
        event.registerEntityRenderer(VISUAL_NEURON.get(), c->new GeoNormalRenderer<>(c, VISUAL_NEURON.getId(),true));
        event.registerEntityRenderer(BRAIN_FAKE.get(), c->new BrainOfCthulhuRenderer(c,new GeoBossModel<>(BRAIN_OF_CTHULHU)));
        event.registerEntityRenderer(QUEEN_BEE.get(), c->new QueenBeeRenderer(c,new GeoBossModel<>(QUEEN_BEE)));
        event.registerEntityRenderer(LITTLE_HORNET.get(), c->new GeoNormalRenderer<>(c, LITTLE_HORNET.getId(),true, 1, 0.5f));
        event.registerEntityRenderer(HORNET.get(), c->new GeoNormalRenderer<>(c, HORNET.getId(),true, 1, 0.5f));

        // sommon
        event.registerEntityRenderer(SUMMON_SLIME.get(), c-> new GeoNormalRenderer<>(c, SUMMON_SLIME.getId().withPrefix("summon/"),false));
        event.registerEntityRenderer(SUMMON_IRON_GOLEM.get(), IronGolemRenderer::new);

    }

    // tip 属性
    @SubscribeEvent
    public static void registerEntityAttributes(EntityAttributeCreationEvent event) {
        AttributeSupplier.Builder genericBossAttribs = Monster.createMobAttributes()
                .add(Attributes.FOLLOW_RANGE, 100)
                .add(Attributes.MAX_HEALTH, 100)
//                .add(Attributes.GRAVITY, 0)
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

        // monster
        event.put(DEMON_EYE.get(), DemonEye.createAttributes().build());
        event.put(BLOOD_CRAWLER.get(), BloodCrawler.createAttributes().build());
        event.put(DECAYEDER.get(), Decayeder.createAttributes().build());
        event.put(BLOODY_SPORE.get(), BloodySpore.createAttributes().build());
        event.put(CRIMSON_KEMERA.get(), AbstractMonster.createAttributes().build());
        event.put(EATER_OF_SOULS.get(), AbstractMonster.createAttributes().build());
        event.put(DRIPPLER.get(), AbstractMonster.createAttributes().build());
        event.put(FLYING_FISH.get(), AbstractMonster.createAttributes().build());
        event.put(FACE_MONSTER.get(), AbstractMonster.createAttributes().build());
        event.put(BLOOD_TUMORS.get(), AbstractMonster.createAttributes().build());
        event.put(BLOOD_ZOMBIE.get(), AbstractMonster.createAttributes().build());
        event.put(DEVOURER.get(), AbstractMonster.createAttributes().build());
        event.put(TOMB_CRAWLER.get(), AbstractMonster.createAttributes().build());
        event.put(GIANT_WORM.get(), AbstractMonster.createAttributes().build());
        event.put(GIANT_SHELLY.get(), AbstractMonster.createAttributes().build());
        event.put(CAVE_BAT.get(), AbstractMonster.createAttributes().build());
        event.put(JUNGLE_BAT.get(), AbstractMonster.createAttributes().build());
        event.put(HELL_BAT.get(), AbstractMonster.createAttributes().build());
        event.put(ICE_BAT.get(), AbstractMonster.createAttributes().build());
        event.put(SPORE_BAT.get(), AbstractMonster.createAttributes().build());

        // boss
        event.put(KING_SLIME.get(), KingSlime.createSlimeAttributes().build());
        event.put(EYE_OF_CTHULHU.get(), AbstractTerraBossBase.createAttributes().build());
        event.put(EATER_OF_WORLD_SEGMENT.get(), AbstractTerraBossBase.createAttributes().build());
        event.put(EATER_OF_WORLDS.get(), AbstractTerraBossBase.createAttributes().build());
        event.put(BRAIN_OF_CTHULHU.get(), AbstractTerraBossBase.createAttributes().build());
        event.put(VISUAL_NEURON.get(), AbstractMonster.createAttributes().build());
        event.put(BRAIN_FAKE.get(), AbstractTerraBossBase.createAttributes().build());
        event.put(QUEEN_BEE.get(), AbstractTerraBossBase.createAttributes().build());
        event.put(LITTLE_HORNET.get(), AbstractMonster.createAttributes().build());
        event.put(HORNET.get(), AbstractMonster.createAttributes().build());

        // sommon
        event.put(SUMMON_SLIME.get(), AbstractTerraBossBase.createAttributes().build());
        event.put(SUMMON_IRON_GOLEM.get(), IronGolem.createAttributes().build());

    }

    //tip 生成位置
    @SubscribeEvent
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

        event.register(DEMON_EYE.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, DemonEye::checkDemonEyeSpawn,  SpawnPlacementRegisterEvent.Operation.REPLACE);
        event.register(BLOOD_CRAWLER.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, BloodCrawler::checkBloodCrawlerSpawn, SpawnPlacementRegisterEvent.Operation.REPLACE);
        event.register(BLOODY_SPORE.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, BloodySpore::checkBloodySporeSpawn, SpawnPlacementRegisterEvent.Operation.REPLACE);
        event.register(CRIMSON_KEMERA.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AbstractMonster::checkRoutineMonsterSpawn, SpawnPlacementRegisterEvent.Operation.REPLACE);
        event.register(FACE_MONSTER.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AbstractMonster::checkRoutineMonsterSpawn, SpawnPlacementRegisterEvent.Operation.REPLACE);
        event.register(DECAYEDER.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AbstractMonster::checkRoutineMonsterSpawn, SpawnPlacementRegisterEvent.Operation.REPLACE);
        event.register(DEVOURER.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AbstractMonster::checkRoutineMonsterSpawn, SpawnPlacementRegisterEvent.Operation.REPLACE);
        event.register(GIANT_WORM.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AbstractMonster::checkUndergroundMonsterSpawn, SpawnPlacementRegisterEvent.Operation.REPLACE);
        event.register(TOMB_CRAWLER.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AbstractMonster::checkUndergroundMonsterSpawn, SpawnPlacementRegisterEvent.Operation.REPLACE);
        event.register(HORNET.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AbstractMonster::checkUndergroundMonsterSpawn, SpawnPlacementRegisterEvent.Operation.REPLACE);

        event.register(FLYING_FISH.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AbstractMonster::checkFlyingFishSpawn, SpawnPlacementRegisterEvent.Operation.REPLACE);
        event.register(GIANT_SHELLY.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AbstractMonster::checkRoutineMonsterSpawn, SpawnPlacementRegisterEvent.Operation.REPLACE);
        event.register(CAVE_BAT.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AbstractMonster::checkUndergroundMonsterSpawn, SpawnPlacementRegisterEvent.Operation.REPLACE);
        event.register(JUNGLE_BAT.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AbstractMonster::checkRoutineMonsterSpawn, SpawnPlacementRegisterEvent.Operation.REPLACE);
        event.register(HELL_BAT.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AbstractMonster::checkNetherMonsterSpawn, SpawnPlacementRegisterEvent.Operation.REPLACE);
        event.register(ICE_BAT.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AbstractMonster::checkUndergroundMonsterSpawn, SpawnPlacementRegisterEvent.Operation.REPLACE);
        event.register(SPORE_BAT.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AbstractMonster::checkUndergroundMonsterSpawn, SpawnPlacementRegisterEvent.Operation.REPLACE);

        event.register(EATER_OF_SOULS.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AbstractMonster::checkRoutineMonsterSpawn, SpawnPlacementRegisterEvent.Operation.REPLACE);

        event.register(BLOOD_ZOMBIE.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AbstractMonster::checkRoutineMonsterSpawn, SpawnPlacementRegisterEvent.Operation.REPLACE);


    }


    public static String Key(String key){
        return MODID + ":" + key;
    }
}
