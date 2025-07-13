package org.confluence.terraentity.init.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.animal.Rabbit;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.confluence.terraentity.client.entity.model.GeoNormalModel;
import org.confluence.terraentity.client.entity.model.VariantTexModel;
import org.confluence.terraentity.client.entity.renderer.GeoNormalRenderer;
import org.confluence.terraentity.entity.animal.*;
import org.confluence.terraentity.init.TEEntities;

public class TEAnimals {

    public static final DeferredHolder<EntityType<?>, EntityType<Duck>> DUCK = TEEntities.ENTITIES.register("duck", () -> EntityType.Builder.of(Duck::new, MobCategory.CREATURE).sized(0.4F, 0.7F).eyeHeight(0.644F).passengerAttachments(new Vec3(0.0, 0.7, -0.1)).clientTrackingRange(10).build(TEEntities.Key("duck")));
    public static final DeferredHolder<EntityType<?>, EntityType<Bunny>> BUNNY = TEEntities.ENTITIES.register("bunny", () -> EntityType.Builder.of(Bunny::new, MobCategory.CREATURE).sized(0.4F, 0.5F).clientTrackingRange(8).build(TEEntities.Key("bunny")));
    public static final DeferredHolder<EntityType<?>, EntityType<JewelBunny>> JEWEL_BUNNY = TEEntities.ENTITIES.register("jewel_bunny", () -> EntityType.Builder.of(JewelBunny::new, MobCategory.CREATURE).sized(0.4F, 0.5F).clientTrackingRange(8).build(TEEntities.Key("jewel_bunny")));
    public static final DeferredHolder<EntityType<?>, EntityType<BoomBunny>> BOOM_BUNNY = TEEntities.ENTITIES.register("boom_bunny", () -> EntityType.Builder.of(BoomBunny::new, MobCategory.CREATURE).sized(0.4F, 0.5F).clientTrackingRange(8).build(TEEntities.Key("boom_bunny")));
    public static final DeferredHolder<EntityType<?>, EntityType<Squirrel>> SQUIRREL = TEEntities.ENTITIES.register("squirrel", () -> EntityType.Builder.of(Squirrel::new, MobCategory.CREATURE).sized(0.4F, 0.5F).clientTrackingRange(8).build(TEEntities.Key("squirrel")));
    public static final DeferredHolder<EntityType<?>, EntityType<JewelSquirrel>> JEWEL_SQUIRREL = TEEntities.ENTITIES.register("jewel_squirrel", () -> EntityType.Builder.of(JewelSquirrel::new, MobCategory.CREATURE).sized(0.4F, 0.5F).clientTrackingRange(8).build(TEEntities.Key("jewel_squirrel")));
    public static final DeferredHolder<EntityType<?>, EntityType<Bird>> BIRD = TEEntities.ENTITIES.register("bird", () -> EntityType.Builder.of(Bird::new, MobCategory.CREATURE).sized(0.4F, 0.5F).clientTrackingRange(8).build(TEEntities.Key("squirrel")));
    public static final DeferredHolder<EntityType<?>, EntityType<Bird>> BLUE_JAY = TEEntities.ENTITIES.register("blue_jay", () -> EntityType.Builder.of(Bird::new, MobCategory.CREATURE).sized(0.4F, 0.5F).clientTrackingRange(8).build(TEEntities.Key("squirrel")));
    public static final DeferredHolder<EntityType<?>, EntityType<Bird>> CARDINAL = TEEntities.ENTITIES.register("cardinal", () -> EntityType.Builder.of(Bird::new, MobCategory.CREATURE).sized(0.4F, 0.5F).clientTrackingRange(8).build(TEEntities.Key("squirrel")));


    @OnlyIn(Dist.CLIENT)
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(DUCK.get(), c-> new GeoNormalRenderer<>(c, new VariantTexModel<Duck>(DUCK.getId().withPrefix("animal/"), true).setHeadName("bone3"), false, 1 ,0.15f));
        event.registerEntityRenderer(BUNNY.get(), c-> new GeoNormalRenderer<>(c, new GeoNormalModel<Bunny>(BUNNY.getId().withPrefix("animal/"), true).setHeadName("head"), false, 1, 0));
        event.registerEntityRenderer(JEWEL_BUNNY.get(), c-> new GeoNormalRenderer<>(c, new VariantTexModel<JewelBunny>(BUNNY.getId().withPrefix("animal/"), true).setHeadName("head"), false, 1 ,0));
        event.registerEntityRenderer(BOOM_BUNNY.get(), c-> new GeoNormalRenderer<>(c, new GeoNormalModel<BoomBunny>(BOOM_BUNNY.getId().withPrefix("animal/"), true).setHeadName("head"), false, 1, 0));
        event.registerEntityRenderer(SQUIRREL.get(), c-> new GeoNormalRenderer<>(c, new GeoNormalModel<Squirrel>(SQUIRREL.getId().withPrefix("animal/"), true).setHeadName("head"), false, 1, 0));
        event.registerEntityRenderer(JEWEL_SQUIRREL.get(), c-> new GeoNormalRenderer<>(c, new VariantTexModel<JewelSquirrel>(SQUIRREL.getId().withPrefix("animal/"), true).setHeadName("head"), false, 1, 0));
        event.registerEntityRenderer(BIRD.get(), c-> new GeoNormalRenderer<>(c, new GeoNormalModel<Bird>(BIRD.getId().withPrefix("animal/"), true).setHeadName("head"), false, 1, 0));
        event.registerEntityRenderer(BLUE_JAY.get(), c-> new GeoNormalRenderer<>(c, new GeoNormalModel<Bird>(BLUE_JAY.getId().withPrefix("animal/"), true).setHeadName("head"), false, 1, 0));
        event.registerEntityRenderer(CARDINAL.get(), c-> new GeoNormalRenderer<>(c, new GeoNormalModel<Bird>(CARDINAL.getId().withPrefix("animal/"), true).setHeadName("head"), false, 1, 0));

    }

    public static void registerEntityAttributes(EntityAttributeCreationEvent event) {
        event.put(DUCK.get(), Chicken.createAttributes().build());
        event.put(BUNNY.get(), Rabbit.createAttributes().add(Attributes.SAFE_FALL_DISTANCE, 6).build());
        event.put(JEWEL_BUNNY.get(), Rabbit.createAttributes().add(Attributes.SAFE_FALL_DISTANCE, 6).build());
        event.put(BOOM_BUNNY.get(), Rabbit.createAttributes().add(Attributes.SAFE_FALL_DISTANCE, 6).build());
        event.put(SQUIRREL.get(), Squirrel.createAttributes().build());
        event.put(JEWEL_SQUIRREL.get(), Squirrel.createAttributes().build());
        event.put(BIRD.get(), Bird.createAttributes().build());
        event.put(BLUE_JAY.get(), Bird.createAttributes().build());
        event.put(CARDINAL.get(), Bird.createAttributes().build());
    }

    public static void spawnPlacementRegister(RegisterSpawnPlacementsEvent event) {
        event.register(DUCK.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(BUNNY.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(JEWEL_BUNNY.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(BOOM_BUNNY.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(SQUIRREL.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(JEWEL_SQUIRREL.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(BIRD.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(BLUE_JAY.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(CARDINAL.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);

    }

    public static void register() {
    }
}