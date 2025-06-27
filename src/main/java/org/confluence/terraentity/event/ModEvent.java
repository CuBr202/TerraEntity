package org.confluence.terraentity.event;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.CrossbowItem;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.event.ModifyDefaultComponentsEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.api.event.HouseDetectEvent;
import org.confluence.terraentity.api.event.NPCEvent;
import org.confluence.terraentity.entity.npc.brain.ArmDealerNPCAi;
import org.confluence.terraentity.entity.npc.brain.DemolitionistNPCAi;
import org.confluence.terraentity.entity.npc.brain.NurseAi;
import org.confluence.terraentity.entity.npc.brain.OldManAi;
import org.confluence.terraentity.init.TEAttributes;
import org.confluence.terraentity.init.TEEntities;
import org.confluence.terraentity.init.entity.TENpcEntities;
import org.confluence.terraentity.integration.ItemComponentModify;
import org.confluence.terraentity.integration.ModChecker;
import org.confluence.terraentity.network.NetworkHandler;

import java.util.List;

@EventBusSubscriber(modid = TerraEntity.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ModEvent {

    @SubscribeEvent
    public static void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(ModChecker::check);
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
        TEEntities.registerEntityAttributes(event);
    }

    // 注册生成位置
    @SubscribeEvent
    public static void spawnPlacementRegister(RegisterSpawnPlacementsEvent event) {
        TEEntities.spawnPlacementRegister(event);
    }

    // 修改物品组件
    @SubscribeEvent
    public static void modifyDefaultComponents(ModifyDefaultComponentsEvent event) {
        ItemComponentModify.modifyDefaultComponents(event);
    }

    @SubscribeEvent
    public static void detectHouseEvent(HouseDetectEvent event) {
//        System.out.println("HouseDetectEvent");

    }

    @SubscribeEvent
    public static void onCollectBrains(NPCEvent.NPCBrainCollectionEvent event) {
        if(!ModChecker.confluence) {
            event.register(TENpcEntities.DEMOLITIONIST.get(), (collector)->{
                collector.setReplace(new DemolitionistNPCAi(collector.getNPC()));
            });
            event.register(TENpcEntities.GUIDE.get(), (collector)->{
                collector.getNPC().setCanPerformerAttackTest(e->e.getMainHandItem().getItem() instanceof BowItem);
//                collector.getNPC().getMood().addMoodInfo(MoodInfos.GUILD1.get());
//                collector.getNPC().getMood().addMoodInfo(MoodInfos.GUILD2.get());

            });
            event.register(TENpcEntities.ARMS_DEALER.get(), (collector)->{
                collector.setReplace(new ArmDealerNPCAi(collector.getNPC()));
                collector.getNPC().setCanPerformerAttackTest(e->e.getMainHandItem().getItem() instanceof CrossbowItem);
            });
            event.register(TENpcEntities.NURSE.get(), (collector)->{
                collector.setReplace(new NurseAi(collector.getNPC()));
            });
            event.register(TENpcEntities.GOBLIN_TINKERER.get(), (collector)->{
                collector.getNPC().setCanPerformerAttackTest(e->e.getMainHandItem().getItem() instanceof BowItem);
            });
        }

        event.register(TENpcEntities.OLD_MAN.get(), collector -> collector.setReplace(new OldManAi(collector.getNPC())));
    }

}