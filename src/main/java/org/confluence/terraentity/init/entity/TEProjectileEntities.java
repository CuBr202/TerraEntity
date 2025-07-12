package org.confluence.terraentity.init.entity;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.RegistryObject;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.client.entity.model.CabbageProjModel;
import org.confluence.terraentity.client.entity.model.DemonScytheModel;
import org.confluence.terraentity.client.entity.model.HarpyFeatherProjectileModel;
import org.confluence.terraentity.client.entity.model.Stinger;
import org.confluence.terraentity.client.entity.renderer.proj.*;
import org.confluence.terraentity.client.util.RegisterUtils;
import org.confluence.terraentity.entity.proj.*;
import org.confluence.terraentity.init.TEEffectStrategies;
import org.confluence.terraentity.init.TEEntities;
import org.confluence.terraentity.registries.hit_effect.variant.PrefabEffect;

public class TEProjectileEntities {

    // 回旋镖
    public static final RegistryObject<EntityType<BoomerangProjectile>> BOOMERANG_PROJECTILE = TEEntities.ENTITIES.register("boomerang_projectile", () -> EntityType.Builder.<BoomerangProjectile>of(BoomerangProjectile::new, MobCategory.MISC).sized(0.5F, 0.5F).build(TEEntities.Key("boomerang_projectile")));
    public static final RegistryObject<EntityType<ThrowableProj>> CABBAGE_PROJ = registerProj("cabbage_proj",(e, l)->
            new ThrowableProj(e,l),0.5F,0.5F);
    public static final RegistryObject<EntityType<LineProj>> BEE_STICK_PROJ = registerProj("bee_stick_proj",(e, l)->
            new LineProj(e,l).setTexture(TerraEntity.space("textures/entity/model/stinger.png")),0.5F,0.5F);
    public static final RegistryObject<EntityType<LineProj>> SUMMON_BEE_STICK_PROJ = registerProj("summon_bee_stick_proj",(e, l)->
            new SummonBeeStick(e,l).setTexture(TerraEntity.space("textures/entity/model/stinger.png")),0.5F,0.5F);
    public static final RegistryObject<EntityType<SkullProjectile>> SKULL = registerProj("skull_proj", SkullProjectile::new,0.5F,0.5F);
    public static final RegistryObject<EntityType<ParticleLineProj>> VILE_SPIT_PROJ = registerProj("vile_spit",(e, l)->
            (ParticleLineProj) new ParticleLineProj(e,l).addEffect(new MobEffectInstance(MobEffects.HUNGER, 100)),0.5F,0.5F);
    public static final RegistryObject<EntityType<ParticleLineProj>> DARK_CASTER_PROJ = registerProj("dark_caster_proj",(e, l)->
            new ParticleLineProj(e,l).setParticleOptions(ParticleTypes.SOUL),0.5F,0.5F);
    public static final RegistryObject<EntityType<ParticleLineProj>> FIRE_IMP_PROJ = registerProj("fire_imp_proj",(e, l)->
            (ParticleLineProj) new ParticleLineProj(e,l).setParticleOptions(ParticleTypes.FLAME).setEffectStrategy(PrefabEffect.of("set_fire", TEEffectStrategies.SET_FIRE_EFFECT)),0.5F,0.5F);
    public static final RegistryObject<EntityType<LineProj>> HARPY_FEATURE_PROJ = registerProj("harpy_feature_spit",(e, l)->
            new LineProj(e,l).setTexture(TerraEntity.space("textures/entity/model/harpy_feather_projectile.png")),0.5F,0.5F);
    public static final RegistryObject<EntityType<DemonScytheProj>> DEMON_SCYTHE_PROJ = registerProj("demon_scythe_proj",(e, l)->
            (DemonScytheProj) new DemonScytheProj(e,l, null).setTexture(TerraEntity.space("textures/entity/model/demon_scythe_projectile.png")),1.2F,1.2F);

    // 鞭子
    public static final RegistryObject<EntityType<WhipEntity>> WHIP_PROJECTILE = TEEntities.ENTITIES.register("whip_projectile",() -> EntityType.Builder.<WhipEntity>of((e, l)->
            new WhipEntity(e,l) , MobCategory.MISC).updateInterval(1).clientTrackingRange(1).sized(0.5F,0.5F).build(TEEntities.Key("whip_projectile")));

    //子弹
    public static final RegistryObject<EntityType<TrailProjectile>> TRAIL_PROJECTILE = TEEntities.ENTITIES.register("trail_projectile", () -> EntityType.Builder.<TrailProjectile>of(TrailProjectile::new, MobCategory.MISC)
            .sized(0.25F, 0.25F).setUpdateInterval(2).setTrackingRange(64).setShouldReceiveVelocityUpdates(true)
            .build(TEEntities.Key("trail_projectile")));

    // OBB剑气
    public static final RegistryObject<EntityType<TrailSwordProj>> TRAIL_SWORD_PROJECTILE = TEEntities.ENTITIES.register("trail_sword_projectile",() -> EntityType.Builder.<TrailSwordProj>of((e, l)->
            new TrailSwordProj(e,l) , MobCategory.MISC).updateInterval(1).clientTrackingRange(1).sized(0.5F,0.5F).build(TEEntities.Key("trail_sword_projectile")));


    @OnlyIn(Dist.CLIENT)
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        RegisterUtils.registerBaseProjRenderer(event, CABBAGE_PROJ.get(), c->new CabbageProjModel<>(c.bakeLayer(CabbageProjModel.LAYER_LOCATION)));
        RegisterUtils.registerBaseProjRenderer(event, BEE_STICK_PROJ.get(), c->new Stinger<>(c.bakeLayer(Stinger.LAYER_LOCATION)));
        RegisterUtils.registerBaseProjRenderer(event, SUMMON_BEE_STICK_PROJ.get(), c->new Stinger<>(c.bakeLayer(Stinger.LAYER_LOCATION)));
        event.registerEntityRenderer(BOOMERANG_PROJECTILE.get(), BoomerangProjRenderer::new);
        event.registerEntityRenderer(SKULL.get(), SkullProjectileRenderer::new);
        RegisterUtils.registerBaseProjRenderer(event, VILE_SPIT_PROJ.get(), c->new Stinger<>(c.bakeLayer(Stinger.LAYER_LOCATION)));
        RegisterUtils.registerBaseProjRenderer(event, DARK_CASTER_PROJ.get(), c->new Stinger<>(c.bakeLayer(Stinger.LAYER_LOCATION)));
        RegisterUtils.registerBaseProjRenderer(event, FIRE_IMP_PROJ.get(), c->new Stinger<>(c.bakeLayer(Stinger.LAYER_LOCATION)));
        RegisterUtils.registerBaseProjRenderer(event, HARPY_FEATURE_PROJ.get(), c->new HarpyFeatherProjectileModel<>(c.bakeLayer(HarpyFeatherProjectileModel.LAYER_LOCATION)));
//        RegisterUtils.registerBaseProjRenderer(event, DEMON_SCYTHE_PROJ.get(), c->new DemonScytheModel<>(c.bakeLayer(DemonScytheModel.LAYER_LOCATION)));

        event.registerEntityRenderer(DEMON_SCYTHE_PROJ.get(), c->new DemonScytheProjRenderer(c, new DemonScytheModel<>(c.bakeLayer(DemonScytheModel.LAYER_LOCATION))));
        // 子弹
        event.registerEntityRenderer(TEProjectileEntities.TRAIL_PROJECTILE.get(), TrailProjectileRenderer::new);
        // 鞭子
        event.registerEntityRenderer(WHIP_PROJECTILE.get(), WhipEntityRenderer::new);
        event.registerEntityRenderer(TRAIL_SWORD_PROJECTILE.get(), TrailSwordProjectileRenderer::new);
    }

    public static <T extends Projectile> RegistryObject<EntityType<T>> registerProj(String name, EntityType.EntityFactory<T> entityFactory, float w, float h) {
        return TEEntities.ENTITIES.register(name, () -> EntityType.Builder.of(entityFactory , MobCategory.MISC).clientTrackingRange(10).sized(w,h).build(TEEntities.Key(name)));
    }

    public static <T extends Projectile> RegistryObject<EntityType<T>> registerProj(String name, EntityType.EntityFactory<T> entityFactory) {
        return registerProj(name,entityFactory,1,1);
    }

    public static void register(IEventBus bus) {

    }
}
