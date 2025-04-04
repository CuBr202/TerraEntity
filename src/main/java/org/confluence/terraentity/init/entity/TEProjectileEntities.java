package org.confluence.terraentity.init.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.RegistryObject;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.entity.proj.*;
import org.confluence.terraentity.init.TEEntities;

public class TEProjectileEntities {
    // tip 弹幕
    // 回旋镖
    public static final RegistryObject<EntityType<BoomerangProjectile>> BOOMERANG_PROJECTILE = TEEntities.ENTITIES.register("boomerang_projectile", () -> EntityType.Builder.<BoomerangProjectile>of(BoomerangProjectile::new, MobCategory.MISC).sized(0.5F, 0.5F).build(TEEntities.Key("boomerang_projectile")));
    public static final RegistryObject<EntityType<ThrowableProj>> CABBAGE_PROJ = registerProj("cabbage_proj",(e, l)->
            new ThrowableProj(e,l),0.5F,0.5F);
    public static final RegistryObject<EntityType<LineProj>> BEE_STICK_PROJ = registerProj("bee_stick_proj",(e, l)->
            new LineProj(e,l).setTexture(TerraEntity.space("textures/entity/model/stinger.png")),0.5F,0.5F);
    public static final RegistryObject<EntityType<LineProj>> SUMMON_BEE_STICK_PROJ = registerProj("summon_bee_stick_proj",(e, l)->
            new SummonBeeStick(e,l).setTexture(TerraEntity.space("textures/entity/model/stinger.png")),0.5F,0.5F);
    public static final RegistryObject<EntityType<SkullProjectile>> SKULL = registerProj("skull", SkullProjectile::new,0.5F,0.5F);
    // 鞭子
    public static final RegistryObject<EntityType<WhipEntity>> WHIP_PROJECTILE = TEEntities.ENTITIES.register("whip_projectile",() -> EntityType.Builder.<WhipEntity>of((e, l)->
            new WhipEntity(e,l) , MobCategory.MISC).updateInterval(1).clientTrackingRange(1).sized(0.5F,0.5F).build(TEEntities.Key("whip_projectile")));

    public static <T extends BaseProj<T>> RegistryObject<EntityType<T>> registerProj(String name, EntityType.EntityFactory<T> entityFactory, float w, float h) {
        return TEEntities.ENTITIES.register(name, () -> EntityType.Builder.of(entityFactory , MobCategory.MISC).clientTrackingRange(10).sized(w,h).build(TEEntities.Key(name)));
    }

    public static <T extends BaseProj<T>> RegistryObject<EntityType<T>> registerProj(String name, EntityType.EntityFactory<T> entityFactory) {
        return registerProj(name,entityFactory,1,1);
    }
}
