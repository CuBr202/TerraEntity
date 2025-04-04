package org.confluence.terraentity.init;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.init.entity.*;


public final class TEEntities {

    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, TerraEntity.MODID);

    public static String Key(String key){
        return TerraEntity.MODID + ":" + key;
    }

    public static <T extends Mob> DeferredHolder<EntityType<?>,EntityType<T>> registerEntity(String name, EntityType.EntityFactory<T> entityFactory, float width, float height){
        return registerEntity(name, entityFactory, MobCategory.MONSTER, width, height);
    }

    public static <T extends Mob> DeferredHolder<EntityType<?>,EntityType<T>> registerEntity(String name, EntityType.EntityFactory<T> entityFactory, MobCategory category, float width, float height){
        return ENTITIES.register(name, () -> EntityType.Builder.of(entityFactory, category).sized(width, height).clientTrackingRange(10).build(Key(name)));
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
