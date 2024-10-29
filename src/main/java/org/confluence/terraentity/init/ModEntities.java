package org.confluence.terraentity.init;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.client.boss.renderer.CthulhuEyeRenderer;
import org.confluence.terraentity.client.entity.renderer.BloodCrawlerRenderer;
import org.confluence.terraentity.client.entity.renderer.BloodySporeRenderer;
import org.confluence.terraentity.client.entity.renderer.DemonEyeRenderer;
import org.confluence.terraentity.entity.boss.CthulhuEye;
import org.confluence.terraentity.entity.monster.BloodCrawler;
import org.confluence.terraentity.entity.monster.BloodySpore;
import org.confluence.terraentity.entity.monster.demoneye.DemonEye;

import static org.confluence.terraentity.TerraEntity.MODID;

@SuppressWarnings("unused")
@EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.MOD)

public final class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, MODID);


    //野怪

    public static final DeferredHolder<EntityType<?>, EntityType<DemonEye>> DEMON_EYE = ENTITIES.register("demon_eye", () -> EntityType.Builder.of(DemonEye::new, MobCategory.MONSTER).sized(0.6F, 0.6F).clientTrackingRange(10).build("confluence:demon_eye"));
    public static final DeferredHolder<EntityType<?>, EntityType<BloodySpore>> BLOODY_SPORE = ENTITIES.register("bloody_spore", () -> EntityType.Builder.of(BloodySpore::new, MobCategory.MONSTER).build("confluence:bloody_spore"));
    public static final DeferredHolder<EntityType<?>, EntityType<BloodCrawler>> BLOOD_CRAWLER = ENTITIES.register("blood_crawler", () -> EntityType.Builder.of(BloodCrawler::new, MobCategory.MONSTER).sized(1.8F, 1.2F).clientTrackingRange(10).build("confluence:blood_crawler"));


    //Boss
    public static final DeferredHolder<EntityType<?>, EntityType<CthulhuEye>> CTHULHU_EYE = ENTITIES.register("cthulhu_eye", () -> EntityType.Builder.<CthulhuEye>of(CthulhuEye::new, MobCategory.MONSTER)
            .sized(2.04F, 2.04F)
            .clientTrackingRange(200)
            .setTrackingRange(200)
            .build(MODID + ":cthulhu_eye"));



    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.DEMON_EYE.get(), DemonEyeRenderer::new);
        event.registerEntityRenderer(ModEntities.CTHULHU_EYE.get(), CthulhuEyeRenderer::new);
        event.registerEntityRenderer(ModEntities.BLOOD_CRAWLER.get(), BloodCrawlerRenderer::new);
        event.registerEntityRenderer(ModEntities.BLOODY_SPORE.get(), BloodySporeRenderer::new);
    }

    //渲染器
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

        event.put(CTHULHU_EYE.get(), genericBossAttribs.build());


        event.put(ModEntities.DEMON_EYE.get(), DemonEye.createAttributes().build());
        event.put(ModEntities.BLOOD_CRAWLER.get(), BloodCrawler.createAttributes().build());
        event.put(ModEntities.BLOODY_SPORE.get(), BloodySpore.createAttributes().build());

    }

    //生成位置
    @SubscribeEvent
    public static void spawnPlacementRegister(RegisterSpawnPlacementsEvent event) {
        event.register(ModEntities.BLOOD_CRAWLER.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, BloodCrawler::checkBloodCrawlerSpawn, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(ModEntities.BLOODY_SPORE.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, BloodySpore::checkBloodySporeSpawn, RegisterSpawnPlacementsEvent.Operation.REPLACE);


    }


}
