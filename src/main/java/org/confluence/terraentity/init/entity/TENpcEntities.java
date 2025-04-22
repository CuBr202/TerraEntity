package org.confluence.terraentity.init.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.registries.RegistryObject;
import org.confluence.terraentity.client.entity.renderer.NPCRenderer;
import org.confluence.terraentity.entity.monster.AbstractMonster;
import org.confluence.terraentity.entity.npc.AbstractTerraNPC;
import org.confluence.terraentity.entity.npc.AnglerNPC;
import org.confluence.terraentity.entity.npc.SimpleNPC;
import org.confluence.terraentity.init.TEEntities;

public class TENpcEntities {

    /** 向导*/
    public static final RegistryObject<EntityType<AbstractTerraNPC>> GUIDE = TEEntities.registerEntity("guide", SimpleNPC::new, MobCategory.CREATURE, 0.6f, 1.85f);
    /** 爆破专家*/
    public static final RegistryObject<EntityType<AbstractTerraNPC>> DEMOLITIONIST = TEEntities.registerEntity("demolitionist", SimpleNPC::new, MobCategory.CREATURE, 0.6f, 1.85f);
    /** 哥布林*/
    public static final RegistryObject<EntityType<AbstractTerraNPC>> GOBLIN_TINKERER = TEEntities.registerEntity("goblin_tinkerer", SimpleNPC::new, MobCategory.CREATURE, 0.6f, 1.85f);
    /** 武器商*/
    public static final RegistryObject<EntityType<AbstractTerraNPC>> ARMS_DEALER = TEEntities.registerEntity("arms_dealer", SimpleNPC::new, MobCategory.CREATURE, 0.6f, 1.85f);
    /** 护士*/
    public static final RegistryObject<EntityType<AbstractTerraNPC>> NURSE = TEEntities.registerEntity("nurse", SimpleNPC::new, MobCategory.CREATURE, 0.6f, 1.85f);
    /**
     * 商人
     */
    public static final RegistryObject<EntityType<AbstractTerraNPC>> MERCHANT = TEEntities.registerEntity("merchant", SimpleNPC::new, MobCategory.CREATURE, 0.6f, 1.85f);
    /**
     * 油漆工
     */
    public static final RegistryObject<EntityType<AbstractTerraNPC>> PAINTER = TEEntities.registerEntity("painter", SimpleNPC::new, MobCategory.CREATURE, 0.6f, 1.85f);
    /**
     * 渔夫
     */
    public static final RegistryObject<EntityType<AbstractTerraNPC>> ANGLER = TEEntities.registerEntity("angler", AnglerNPC::new, MobCategory.CREATURE, 0.6f, 1.85f);
//    public static final RegistryObject<EntityType<AbstractTerraNPC>> FEMALE_ANGLER = TEEntities.registerEntity("", AbstractTerraNPC::new, MobCategory.CREATURE, 0.6f, 1.85f);
    /**
     * 树妖
     */
    public static final RegistryObject<EntityType<AbstractTerraNPC>> DRYAD = TEEntities.registerEntity("dryad", SimpleNPC::new, MobCategory.CREATURE, 0.6f, 1.85f);
    /**
     * 染料商
     */
    public static final RegistryObject<EntityType<AbstractTerraNPC>> DYE_TRADER = TEEntities.registerEntity("dye_trader", SimpleNPC::new, MobCategory.CREATURE, 0.6f, 1.85f);

    @OnlyIn(Dist.CLIENT)
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {

        event.registerEntityRenderer(GUIDE.get(), c -> new NPCRenderer<>(c, GUIDE.getId()));
        event.registerEntityRenderer(DEMOLITIONIST.get(), c -> new NPCRenderer<>(c, DEMOLITIONIST.getId()));
        event.registerEntityRenderer(GOBLIN_TINKERER.get(), c -> new NPCRenderer<>(c, GOBLIN_TINKERER.getId()));
        event.registerEntityRenderer(ARMS_DEALER.get(), c -> new NPCRenderer<>(c, ARMS_DEALER.getId()));
        event.registerEntityRenderer(NURSE.get(), c -> new NPCRenderer<>(c, NURSE.getId()));
        event.registerEntityRenderer(MERCHANT.get(), c -> new NPCRenderer<>(c, MERCHANT.getId()));
        event.registerEntityRenderer(PAINTER.get(), c -> new NPCRenderer<>(c, PAINTER.getId()));
        event.registerEntityRenderer(ANGLER.get(), c -> new NPCRenderer<>(c, ANGLER.getId()));
        event.registerEntityRenderer(DRYAD.get(), c -> new NPCRenderer<>(c, DRYAD.getId()));
        event.registerEntityRenderer(DYE_TRADER.get(), c -> new NPCRenderer<>(c, DYE_TRADER.getId()));

    }

    public static void registerEntityAttributes(EntityAttributeCreationEvent event) {
        event.put(GUIDE.get(), AbstractTerraNPC.createAttributes().build());
        event.put(DEMOLITIONIST.get(), AbstractTerraNPC.createAttributes().build());
        event.put(GOBLIN_TINKERER.get(), AbstractTerraNPC.createAttributes().build());
        event.put(ARMS_DEALER.get(), AbstractTerraNPC.createAttributes().build());
        event.put(NURSE.get(), AbstractTerraNPC.createAttributes().build());
        event.put(MERCHANT.get(), AbstractTerraNPC.createAttributes().build());
        event.put(PAINTER.get(), AbstractTerraNPC.createAttributes().build());
        event.put(ANGLER.get(), AbstractTerraNPC.createAttributes().build());
        event.put(DRYAD.get(), AbstractTerraNPC.createAttributes().build());
        event.put(DYE_TRADER.get(), AbstractTerraNPC.createAttributes().build());


    }

    public static void spawnPlacementRegister(SpawnPlacementRegisterEvent event) {

        event.register(GUIDE.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AbstractMonster::checkRoutineMonsterSpawn, SpawnPlacementRegisterEvent.Operation.REPLACE);
        event.register(DEMOLITIONIST.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AbstractMonster::checkRoutineMonsterSpawn, SpawnPlacementRegisterEvent.Operation.REPLACE);
        event.register(GOBLIN_TINKERER.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AbstractMonster::checkRoutineMonsterSpawn, SpawnPlacementRegisterEvent.Operation.REPLACE);
        event.register(ARMS_DEALER.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AbstractMonster::checkRoutineMonsterSpawn, SpawnPlacementRegisterEvent.Operation.REPLACE);
        event.register(NURSE.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AbstractMonster::checkRoutineMonsterSpawn, SpawnPlacementRegisterEvent.Operation.REPLACE);
        event.register(MERCHANT.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AbstractMonster::checkRoutineMonsterSpawn, SpawnPlacementRegisterEvent.Operation.REPLACE);
        event.register(PAINTER.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AbstractMonster::checkRoutineMonsterSpawn, SpawnPlacementRegisterEvent.Operation.REPLACE);
        event.register(ANGLER.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AbstractMonster::checkRoutineMonsterSpawn, SpawnPlacementRegisterEvent.Operation.REPLACE);
        event.register(DRYAD.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AbstractMonster::checkRoutineMonsterSpawn, SpawnPlacementRegisterEvent.Operation.REPLACE);
        event.register(DYE_TRADER.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AbstractMonster::checkRoutineMonsterSpawn, SpawnPlacementRegisterEvent.Operation.REPLACE);
    }

    public static void register(){

    }
}
