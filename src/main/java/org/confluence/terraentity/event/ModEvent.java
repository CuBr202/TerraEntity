package org.confluence.terraentity.event;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.CrossbowItem;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.api.event.NPCEvent;
import org.confluence.terraentity.entity.npc.brain.ArmDealerNPCAi;
import org.confluence.terraentity.entity.npc.brain.DemolitionistNPCAi;
import org.confluence.terraentity.entity.npc.brain.NurseAi;
import org.confluence.terraentity.init.TEAttributes;
import org.confluence.terraentity.init.TEEntities;
import org.confluence.terraentity.init.entity.TENpcEntities;
import org.confluence.terraentity.integration.ModChecker;
import org.confluence.terraentity.network.NetworkHandler;

import java.util.List;
@SuppressWarnings("all")
@Mod.EventBusSubscriber(modid = TerraEntity.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEvent {

    @SubscribeEvent
    public static void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            ModChecker.check();
            NetworkHandler.register();
        });
    }

    @SubscribeEvent
    public static void onLoad(final ModConfigEvent event) {
//        ServerConfig.init();
//        ClientConfig.load();

    }

    // 注册新属性
    @SubscribeEvent
    public static void registerAttributes(EntityAttributeModificationEvent event) {
        // 召唤师属性
        List.of(TEAttributes.MINION_CAPACITY, TEAttributes.SENTRY_CAPACITY, TEAttributes.SUMMON_DAMAGE, TEAttributes.SUMMON_KNOCKBACK, TEAttributes.WHIP_RANGE, TEAttributes.MARK_DAMAGE)
                .forEach(att-> event.add(EntityType.PLAYER, att.get()));

    }

    // 注册怪物属性
    @SubscribeEvent
    public static void registerEntityAttributes(EntityAttributeCreationEvent event) {
        TEEntities.registerEntityAttributes(event);
    }

    // 注册生成位置
    @SubscribeEvent
    public static void spawnPlacementRegister(SpawnPlacementRegisterEvent event) {
        TEEntities.spawnPlacementRegister(event);
    }


    public static void onCollectBrains(NPCEvent.NPCBrainCollectionEvent event) {
//        if(!ModChecker.confluence) {
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
//        }
    }

    private static void checkModLoad(String modId, Runnable runnable) {
        if (ModList.get().isLoaded(modId)) {
            runnable.run();
        }
    }
}