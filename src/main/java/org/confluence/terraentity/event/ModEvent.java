package org.confluence.terraentity.event;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.init.TEAttributes;
import org.confluence.terraentity.init.entity.TEBossEntities;
import org.confluence.terraentity.init.entity.TEMonsterEntities;
import org.confluence.terraentity.init.entity.TERideableEntities;
import org.confluence.terraentity.init.entity.TESummonEntities;
import org.confluence.terraentity.network.NetworkHandler;
import org.confluence.terraentity.network.s2c.SyncBossEventHealthPacket;
import org.confluence.terraentity.network.s2c.SyncCameraShakePacket;
import org.confluence.terraentity.network.s2c.SyncSummonPacket;

import java.util.List;

@EventBusSubscriber(modid = TerraEntity.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ModEvent {

    @SubscribeEvent
    public static void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {

        });
    }

    @SubscribeEvent
    public static void registerPayloadHandlers(RegisterPayloadHandlersEvent event) {
        NetworkHandler.register(event);
    }

    // 注册新属性
    @SubscribeEvent
    public static void registerAttributes(EntityAttributeModificationEvent event) {
        // 召唤师属性
        List.of(TEAttributes.MINION_CAPACITY, TEAttributes.SENTRY_CAPACITY, TEAttributes.SUMMON_DAMAGE, TEAttributes.SUMMON_KNOCKBACK, TEAttributes.WHIP_RANGE, TEAttributes.MARK_DAMAGE)
                .forEach(att-> event.add(EntityType.PLAYER, att));

    }

    // 注册怪物属性
    @SubscribeEvent
    public static void registerEntityAttributes(EntityAttributeCreationEvent event) {
        TEBossEntities.registerEntityAttributes(event);
        TEMonsterEntities.registerEntityAttributes(event);
        TERideableEntities.registerEntityAttributes(event);
        TESummonEntities.registerEntityAttributes(event);
    }

    // 注册生成位置
    @SubscribeEvent
    public static void spawnPlacementRegister(RegisterSpawnPlacementsEvent event) {
        TEMonsterEntities.spawnPlacementRegister(event);
    }
}