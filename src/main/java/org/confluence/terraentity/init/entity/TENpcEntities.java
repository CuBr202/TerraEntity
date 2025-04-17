package org.confluence.terraentity.init.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.confluence.terraentity.client.entity.renderer.NPCRenderer;
import org.confluence.terraentity.entity.monster.AbstractMonster;
import org.confluence.terraentity.entity.npc.AbstractTerraNPC;
import org.confluence.terraentity.init.TEEntities;

public class TENpcEntities {

    /** 向导*/
    public static final DeferredHolder<EntityType<?>, EntityType<AbstractTerraNPC>> GUIDE = TEEntities.registerEntity("guide", AbstractTerraNPC::new, MobCategory.CREATURE, 0.6f, 1.85f);
    /** 爆破专家*/
    public static final DeferredHolder<EntityType<?>, EntityType<AbstractTerraNPC>> DEMOLITIONIST = TEEntities.registerEntity("demolitionist", AbstractTerraNPC::new, MobCategory.CREATURE, 0.6f, 1.85f);
    /** 哥布林*/
    public static final DeferredHolder<EntityType<?>, EntityType<AbstractTerraNPC>> GOBLIN_TINKERER = TEEntities.registerEntity("goblin_tinkerer", AbstractTerraNPC::new, MobCategory.CREATURE, 0.6f, 1.85f);
    /** 武器商*/
    public static final DeferredHolder<EntityType<?>, EntityType<AbstractTerraNPC>> ARMS_DEALER = TEEntities.registerEntity("arms_dealer", AbstractTerraNPC::new, MobCategory.CREATURE, 0.6f, 1.85f);
    /** 护士*/
    public static final DeferredHolder<EntityType<?>, EntityType<AbstractTerraNPC>> NURSE = TEEntities.registerEntity("nurse", AbstractTerraNPC::new, MobCategory.CREATURE, 0.6f, 1.85f);
    /**
     * 商人
     */
    public static final DeferredHolder<EntityType<?>, EntityType<AbstractTerraNPC>> MERCHANT = TEEntities.registerEntity("merchant", AbstractTerraNPC::new, MobCategory.CREATURE, 0.6f, 1.85f);

    @OnlyIn(Dist.CLIENT)
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {

        event.registerEntityRenderer(GUIDE.get(), c -> new NPCRenderer<>(c, GUIDE.getId()));
        event.registerEntityRenderer(DEMOLITIONIST.get(), c -> new NPCRenderer<>(c, DEMOLITIONIST.getId()));
        event.registerEntityRenderer(GOBLIN_TINKERER.get(), c -> new NPCRenderer<>(c, GOBLIN_TINKERER.getId()));
        event.registerEntityRenderer(ARMS_DEALER.get(), c -> new NPCRenderer<>(c, ARMS_DEALER.getId()));
        event.registerEntityRenderer(NURSE.get(), c -> new NPCRenderer<>(c, NURSE.getId()));
        event.registerEntityRenderer(MERCHANT.get(), c -> new NPCRenderer<>(c, MERCHANT.getId()));


    }

    public static void registerEntityAttributes(EntityAttributeCreationEvent event) {
        event.put(GUIDE.get(), AbstractTerraNPC.createAttributes().build());
        event.put(DEMOLITIONIST.get(), AbstractTerraNPC.createAttributes().build());
        event.put(GOBLIN_TINKERER.get(), AbstractTerraNPC.createAttributes().build());
        event.put(ARMS_DEALER.get(), AbstractTerraNPC.createAttributes().build());
        event.put(NURSE.get(), AbstractTerraNPC.createAttributes().build());
        event.put(MERCHANT.get(), AbstractTerraNPC.createAttributes().build());


    }

    public static void spawnPlacementRegister(RegisterSpawnPlacementsEvent event) {

        event.register(GUIDE.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AbstractMonster::checkRoutineMonsterSpawn, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(DEMOLITIONIST.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AbstractMonster::checkRoutineMonsterSpawn, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(GOBLIN_TINKERER.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AbstractMonster::checkRoutineMonsterSpawn, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(ARMS_DEALER.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AbstractMonster::checkRoutineMonsterSpawn, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(NURSE.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AbstractMonster::checkRoutineMonsterSpawn, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(MERCHANT.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AbstractMonster::checkRoutineMonsterSpawn, RegisterSpawnPlacementsEvent.Operation.REPLACE);

    }

    public static void register(){

    }
}
