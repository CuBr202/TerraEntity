package org.confluence.terraentity.init;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.confluence.terraentity.entity.boss.*;
import org.confluence.terraentity.entity.monster.*;
import org.confluence.terraentity.entity.monster.demoneye.DemonEye;
import org.confluence.terraentity.entity.monster.slime.BaseSlime;
import org.confluence.terraentity.entity.monster.slime.HoneySlime;
import org.confluence.terraentity.init.entity.*;

import static org.confluence.terraentity.TerraEntity.MODID;


@EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.MOD)
public final class TEEntities {

    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, MODID);

    public static String Key(String key){
        return MODID + ":" + key;
    }

    public static <T extends Mob> DeferredHolder<EntityType<?>,EntityType<T>> registerEntity(String name, EntityType.EntityFactory<T> entityFactory, float width, float height){
        return registerEntity(name, entityFactory, MobCategory.MONSTER, width, height);
    }

    public static <T extends Mob> DeferredHolder<EntityType<?>,EntityType<T>> registerEntity(String name, EntityType.EntityFactory<T> entityFactory, MobCategory category, float width, float height){
        return ENTITIES.register(name, () -> EntityType.Builder.of(entityFactory, category).sized(width, height).clientTrackingRange(10).build(Key(name)));
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
        event.put(TEMonsterEntities.BLUE_SLIME.get(), BaseSlime.createSlimeAttributes(4.0F, 0, 16.0F).build());
        event.put(TEMonsterEntities.GREEN_SLIME.get(), BaseSlime.createSlimeAttributes(3.0F, 0, 9.0F).build());
        event.put(TEMonsterEntities.PINK_SLIME.get(), BaseSlime.createSlimeAttributes(2.0F, 2, 97.0F).build());
        event.put(TEMonsterEntities.CORRUPTED_SLIME.get(), BaseSlime.createSlimeAttributes(35.0F, 6, 110.0F).build());
        event.put(TEMonsterEntities.DESERT_SLIME.get(), BaseSlime.createSlimeAttributes(6.0F, 0, 21.0F).build());
        event.put(TEMonsterEntities.JUNGLE_SLIME.get(), BaseSlime.createSlimeAttributes(12.0F, 0, 46.0F).build());
        event.put(TEMonsterEntities.EVIL_SLIME.get(), BaseSlime.createSlimeAttributes(29.0F, 2, 58.0F).build());
        event.put(TEMonsterEntities.ICE_SLIME.get(), BaseSlime.createSlimeAttributes(5.0F, 0, 13.0F).build());
        event.put(TEMonsterEntities.LAVA_SLIME.get(), BaseSlime.createSlimeAttributes(10.0F, 2, 30.0F).build());
        event.put(TEMonsterEntities.LUMINOUS_SLIME.get(), BaseSlime.createSlimeAttributes(45.0F, 6, 117.0F).build());
        event.put(TEMonsterEntities.CRIMSON_SLIME.get(), BaseSlime.createSlimeAttributes(39.0F, 5, 130.0F).build());
        event.put(TEMonsterEntities.PURPLE_SLIME.get(), BaseSlime.createSlimeAttributes(5.0F, 1, 25.0F).build());
        event.put(TEMonsterEntities.RED_SLIME.get(), BaseSlime.createSlimeAttributes(5.0F, 1, 25.0F).build());
        event.put(TEMonsterEntities.TROPIC_SLIME.get(), BaseSlime.createSlimeAttributes(5.0F, 0, 13.0F).build());
        event.put(TEMonsterEntities.YELLOW_SLIME.get(), BaseSlime.createSlimeAttributes(6.0F, 2, 25.0F).build());
        event.put(TEMonsterEntities.HONEY_SLIME.get(), HoneySlime.createSlimeAttributes(0F, 0, 16.0F).build());
        event.put(TEMonsterEntities.BLACK_SLIME.get(), Monster.createMonsterAttributes().build()); // 由finalizeSpawn设置
        // land
        event.put(TEMonsterEntities.BLOOD_CRAWLER.get(), BloodCrawler.createAttributes().build());
        event.put(TEMonsterEntities.DECAYEDER.get(), Decayeder.createAttributes().build());
        event.put(TEMonsterEntities.BLOODY_SPORE.get(), BloodySpore.createAttributes().build());
        event.put(TEMonsterEntities.FACE_MONSTER.get(), AbstractMonster.createAttributes().build());
        event.put(TEMonsterEntities.BLOOD_TUMORS.get(), AbstractMonster.createAttributes().build());
        event.put(TEMonsterEntities.BLOOD_ZOMBIE.get(), AbstractMonster.createAttributes().build());
        event.put(TEMonsterEntities.GIANT_SHELLY.get(), AbstractMonster.createAttributes().build());
        event.put(TEMonsterEntities.NYMPH.get(), AbstractMonster.createAttributes().build());
        event.put(TEMonsterEntities.SNATCHER.get(), AbstractMonster.createAttributes().build());
        event.put(TEMonsterEntities.MAN_EATER.get(), AbstractMonster.createAttributes().build());

        // fly
        event.put(TEMonsterEntities.DEMON_EYE.get(), DemonEye.createAttributes().build());
        event.put(TEMonsterEntities.FLYING_FISH.get(), AbstractMonster.createAttributes().build());
        event.put(TEMonsterEntities.CRIMSON_KEMERA.get(), AbstractMonster.createAttributes().build());
        event.put(TEMonsterEntities.DRIPPLER.get(), AbstractMonster.createAttributes().build());
        event.put(TEMonsterEntities.WANDERING_EYE_FISH.get(), AbstractMonster.createAttributes().build());
        event.put(TEMonsterEntities.EATER_OF_SOULS.get(), AbstractMonster.createAttributes().build());


        // bat
        event.put(TEMonsterEntities.CAVE_BAT.get(), AbstractMonster.createAttributes().build());
        event.put(TEMonsterEntities.JUNGLE_BAT.get(), AbstractMonster.createAttributes().build());
        event.put(TEMonsterEntities.HELL_BAT.get(), AbstractMonster.createAttributes().build());
        event.put(TEMonsterEntities.ICE_BAT.get(), AbstractMonster.createAttributes().build());
        event.put(TEMonsterEntities.SPORE_BAT.get(), AbstractMonster.createAttributes().build());
        // worm
        event.put(TEMonsterEntities.GIANT_WORM.get(), AbstractMonster.createAttributes().build());
        event.put(TEMonsterEntities.DEVOURER.get(), AbstractMonster.createAttributes().build());
        event.put(TEMonsterEntities.TOMB_CRAWLER.get(), AbstractMonster.createAttributes().build());

        // bee
        event.put(TEMonsterEntities.HORNET.get(), AbstractMonster.createAttributes().build());
        event.put(TEMonsterEntities.LITTLE_HORNET.get(), AbstractMonster.createAttributes().build());



        // boss
        event.put(TEBossEntities.KING_SLIME.get(), KingSlime.createSlimeAttributes().build());
        event.put(TEBossEntities.EYE_OF_CTHULHU.get(), AbstractTerraBossBase.createAttributes().build());
        event.put(TEBossEntities.EATER_OF_WORLD_SEGMENT.get(), AbstractTerraBossBase.createAttributes().build());
        event.put(TEBossEntities.EATER_OF_WORLDS.get(), AbstractTerraBossBase.createAttributes().build());
        event.put(TEBossEntities.BRAIN_OF_CTHULHU.get(), AbstractTerraBossBase.createAttributes().build());
        event.put(TEMonsterEntities.VISUAL_NEURON.get(), AbstractMonster.createAttributes().build());
        event.put(TEBossEntities.BRAIN_FAKE.get(), AbstractTerraBossBase.createAttributes().build());
        event.put(TEBossEntities.QUEEN_BEE.get(), AbstractTerraBossBase.createAttributes().build());
        event.put(TEBossEntities.SKELETRON.get(), AbstractTerraBossBase.createAttributes().build());
        event.put(TEBossEntities.SKELETRON_HAND.get(), AbstractTerraBossBase.createAttributes().build());

        // sommon
        event.put(TESummonEntities.SUMMON_SLIME.get(), AbstractTerraBossBase.createAttributes().build());
        event.put(TESummonEntities.SUMMON_IRON_GOLEM.get(), IronGolem.createAttributes().build());
        event.put(TESummonEntities.SUMMON_HORNET.get(), AbstractMonster.createAttributes().build());

        // ridable
        event.put(TERideableEntities.RIDEABLE_SLIME.get(), AbstractMonster.createAttributes().build());
        event.put(TERideableEntities.RIDEABLE_BEE.get(), AbstractMonster.createAttributes().build());


    }

    //tip 生成位置
    @SubscribeEvent
    public static void spawnPlacementRegister(RegisterSpawnPlacementsEvent event) {
        event.register(TEMonsterEntities.BLUE_SLIME.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, BaseSlime::checkSlimeSpawn, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(TEMonsterEntities.GREEN_SLIME.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, BaseSlime::checkSlimeSpawn, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(TEMonsterEntities.PURPLE_SLIME.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, BaseSlime::checkSlimeSpawn, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(TEMonsterEntities.PINK_SLIME.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, BaseSlime::checkSlimeSpawn, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(TEMonsterEntities.CORRUPTED_SLIME.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, BaseSlime::checkSlimeSpawn, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(TEMonsterEntities.DESERT_SLIME.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, BaseSlime::checkSlimeSpawn, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(TEMonsterEntities.JUNGLE_SLIME.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, BaseSlime::checkSlimeSpawn, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(TEMonsterEntities.ICE_SLIME.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, BaseSlime::checkSlimeSpawn, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(TEMonsterEntities.TROPIC_SLIME.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, BaseSlime::checkSlimeSpawn, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(TEMonsterEntities.CRIMSON_SLIME.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, BaseSlime::checkSlimeSpawn, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(TEMonsterEntities.YELLOW_SLIME.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, BaseSlime::checkSlimeSpawn, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(TEMonsterEntities.RED_SLIME.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, BaseSlime::checkSlimeSpawn, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(TEMonsterEntities.BLACK_SLIME.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, BaseSlime::checkSlimeSpawn, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(TEMonsterEntities.LAVA_SLIME.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, BaseSlime::checkSlimeSpawn, RegisterSpawnPlacementsEvent.Operation.REPLACE);


        // land
        event.register(TEMonsterEntities.BLOOD_CRAWLER.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, BloodCrawler::checkBloodCrawlerSpawn, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(TEMonsterEntities.BLOOD_ZOMBIE.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AbstractMonster::checkRoutineMonsterSpawn, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(TEMonsterEntities.BLOODY_SPORE.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, BloodySpore::checkBloodySporeSpawn, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(TEMonsterEntities.FACE_MONSTER.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AbstractMonster::checkRoutineMonsterSpawn, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(TEMonsterEntities.DECAYEDER.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AbstractMonster::checkRoutineMonsterSpawn, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(TEMonsterEntities.GIANT_SHELLY.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AbstractMonster::checkUndergroundMonsterSpawn, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(TEMonsterEntities.NYMPH.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AbstractMonster::checkUndergroundMonsterSpawn, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(TEMonsterEntities.SNATCHER.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AbstractMonster::checkGroundSpawn, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(TEMonsterEntities.MAN_EATER.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AbstractMonster::checkUndergroundMonsterSpawn, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        // fly
        event.register(TEMonsterEntities.DEMON_EYE.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, DemonEye::checkDemonEyeSpawn,  RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(TEMonsterEntities.FLYING_FISH.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AbstractMonster::checkFlyingFishSpawn, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(TEMonsterEntities.CRIMSON_KEMERA.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AbstractMonster::checkRoutineMonsterSpawn, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(TEMonsterEntities.EATER_OF_SOULS.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AbstractMonster::checkRoutineMonsterSpawn, RegisterSpawnPlacementsEvent.Operation.REPLACE);

        // worm
        event.register(TEMonsterEntities.DEVOURER.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AbstractMonster::checkRoutineMonsterSpawn, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(TEMonsterEntities.GIANT_WORM.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AbstractMonster::checkUndergroundMonsterSpawn, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(TEMonsterEntities.TOMB_CRAWLER.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AbstractMonster::checkUndergroundMonsterSpawn, RegisterSpawnPlacementsEvent.Operation.REPLACE);

        // bee
        event.register(TEMonsterEntities.HORNET.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AbstractMonster::checkUndergroundMonsterSpawn, RegisterSpawnPlacementsEvent.Operation.REPLACE);

        // bat
        event.register(TEMonsterEntities.CAVE_BAT.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AbstractMonster::checkUndergroundMonsterSpawn, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(TEMonsterEntities.JUNGLE_BAT.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AbstractMonster::checkRoutineMonsterSpawn, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(TEMonsterEntities.HELL_BAT.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AbstractMonster::checkNetherMonsterSpawn, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(TEMonsterEntities.ICE_BAT.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AbstractMonster::checkUndergroundMonsterSpawn, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(TEMonsterEntities.SPORE_BAT.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AbstractMonster::checkUndergroundMonsterSpawn, RegisterSpawnPlacementsEvent.Operation.REPLACE);



    }


    public static void register(IEventBus bus){
        TEBossEntities.register();
        TESummonEntities.register();
        TERideableEntities.register();
        TEMonsterEntities.register();
        TEProjectileEntities.register();
        ENTITIES.register(bus);
    }
}
