package org.confluence.terraentity.init.entity;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.projectile.Projectile;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.client.entity.model.CabbageProjModel;
import org.confluence.terraentity.client.entity.model.Stinger;
import org.confluence.terraentity.client.entity.renderer.BoomerangProjRenderer;
import org.confluence.terraentity.client.entity.renderer.SkullProjectileRenderer;
import org.confluence.terraentity.client.entity.renderer.WhipEntityRenderer;
import org.confluence.terraentity.client.util.RegisterUtils;
import org.confluence.terraentity.entity.proj.*;
import org.confluence.terraentity.init.TEEntities;
import org.confluence.terraentity.init.TEParticles;

public class TEProjectileEntities {

    // 回旋镖
    public static final DeferredHolder<EntityType<?>, EntityType<BoomerangProjectile>> BOOMERANG_PROJECTILE = TEEntities.ENTITIES.register("boomerang_projectile", () -> EntityType.Builder.<BoomerangProjectile>of(BoomerangProjectile::new, MobCategory.MISC).sized(0.5F, 0.5F).build(TEEntities.Key("boomerang_projectile")));
    public static final DeferredHolder<EntityType<?>, EntityType<ThrowableProj>> CABBAGE_PROJ = registerProj("cabbage_proj",(e, l)->
            new ThrowableProj(e,l),0.5F,0.5F);
    public static final DeferredHolder<EntityType<?>, EntityType<LineProj>> BEE_STICK_PROJ = registerProj("bee_stick_proj",(e, l)->
            new LineProj(e,l).setTexture(TerraEntity.space("textures/entity/model/stinger.png")),0.5F,0.5F);
    public static final DeferredHolder<EntityType<?>, EntityType<LineProj>> SUMMON_BEE_STICK_PROJ = registerProj("summon_bee_stick_proj",(e, l)->
            new SummonBeeStick(e,l).setTexture(TerraEntity.space("textures/entity/model/stinger.png")),0.5F,0.5F);
    public static final DeferredHolder<EntityType<?>, EntityType<SkullProjectile>> SKULL = registerProj("skull", SkullProjectile::new,0.5F,0.5F);
    public static final DeferredHolder<EntityType<?>, EntityType<VileSpitProj>> VILE_SPIT_PROJ = registerProj("vile_spit",(e, l)->
            (VileSpitProj) new VileSpitProj(e,l).addEffect(new MobEffectInstance(MobEffects.HUNGER, 100)),0.5F,0.5F);
    public static final DeferredHolder<EntityType<?>, EntityType<VileSpitProj>> DARK_CASTER_PROJ = registerProj("dark_caster_spit",(e, l)->
            new VileSpitProj(e,l).setParticleOptions(ParticleTypes.SOUL),0.5F,0.5F);

    // 鞭子
    public static final DeferredHolder<EntityType<?>,EntityType<WhipEntity>> WHIP_PROJECTILE = TEEntities.ENTITIES.register("whip_projectile",() -> EntityType.Builder.<WhipEntity>of((e, l)->
            new WhipEntity(e,l) , MobCategory.MISC).updateInterval(1).clientTrackingRange(1).sized(0.5F,0.5F).build(TEEntities.Key("whip_projectile")));

    @OnlyIn(Dist.CLIENT)
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        RegisterUtils.registerBaseProjRenderer(event, TEProjectileEntities.CABBAGE_PROJ.get(), c->new CabbageProjModel<>(c.bakeLayer(CabbageProjModel.LAYER_LOCATION)));
        RegisterUtils.registerBaseProjRenderer(event, TEProjectileEntities.BEE_STICK_PROJ.get(), c->new Stinger<>(c.bakeLayer(Stinger.LAYER_LOCATION)));
        RegisterUtils.registerBaseProjRenderer(event, TEProjectileEntities.SUMMON_BEE_STICK_PROJ.get(), c->new Stinger<>(c.bakeLayer(Stinger.LAYER_LOCATION)));
        event.registerEntityRenderer(TEProjectileEntities.BOOMERANG_PROJECTILE.get(), BoomerangProjRenderer::new);
        event.registerEntityRenderer(TEProjectileEntities.SKULL.get(), SkullProjectileRenderer::new);
        RegisterUtils.registerBaseProjRenderer(event, TEProjectileEntities.VILE_SPIT_PROJ.get(), c->new Stinger<>(c.bakeLayer(Stinger.LAYER_LOCATION)));

        // 鞭子
        event.registerEntityRenderer(TEProjectileEntities.WHIP_PROJECTILE.get(), WhipEntityRenderer::new);
    }

    public static <T extends Projectile> DeferredHolder<EntityType<?>, EntityType<T>> registerProj(String name, EntityType.EntityFactory<T> entityFactory, float w, float h) {
        return TEEntities.ENTITIES.register(name, () -> EntityType.Builder.of(entityFactory , MobCategory.MISC).clientTrackingRange(10).sized(w,h).build(TEEntities.Key(name)));
    }

    public static <T extends Projectile> DeferredHolder<EntityType<?>, EntityType<T>> registerProj(String name, EntityType.EntityFactory<T> entityFactory) {
        return registerProj(name,entityFactory,1,1);
    }

    public static void register(){

    }
}
