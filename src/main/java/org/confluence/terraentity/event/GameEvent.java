package org.confluence.terraentity.event;

import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.OnDatapackSyncEvent;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.event.server.ServerStoppedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.api.event.NPCEvent;
import org.confluence.terraentity.config.TEAttributeModifierConfig;
import org.confluence.terraentity.data.saved_data.HouseStoreSaver;
import org.confluence.terraentity.entity.animation.HillOfFleshModelAnimationTable;
import org.confluence.terraentity.entity.npc.chat.ChatManager;
import org.confluence.terraentity.entity.npc.misc.NPCDialogs;
import org.confluence.terraentity.entity.npc.misc.NPCNames;
import org.confluence.terraentity.entity.npc.mood.NPCMood;
import org.confluence.terraentity.entity.npc.trade.NPCTradeManager;
import org.confluence.terraentity.entity.npc.trade.TradeModifiers;
import org.confluence.terraentity.network.s2c.SyncDataS2C;
import org.confluence.terraentity.network.s2c.SyncNPCTradesPacketS2C;
import org.confluence.terraentity.registries.mappeddata.MappedDataLoader;
import org.confluence.terraentity.utils.AdapterUtils;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, modid = TerraEntity.MODID)
public class GameEvent {
    @SubscribeEvent
    public static void onDatapackSync(OnDatapackSyncEvent event) {
        ServerPlayer serverPlayer = event.getPlayer();
        if (serverPlayer != null) {
            SyncNPCTradesPacketS2C.sync(serverPlayer);
            SyncDataS2C.syncAll(serverPlayer);
        }
    }

    @SubscribeEvent
    public static void serverStartBefore(ServerAboutToStartEvent event) {
        NPCTradeManager.readTradesFromJson(event.getServer());
        TradeModifiers.readTradesFromJson(event.getServer());
        ModEvent.onCollectBrains(new NPCEvent.NPCBrainCollectionEvent()); // 本模组优先注册
        AdapterUtils.postEvent(new NPCEvent.NPCBrainCollectionEvent());
        TEAttributeModifierConfig.getInstance().loadConfig();
        ChatManager.readChatsFromJson(event.getServer(), event.getServer().registryAccess());
    }

    @SubscribeEvent
    public static void serverStarted(ServerStartedEvent event) {
        HouseStoreSaver.get(event.getServer().overworld());
    }

    @SubscribeEvent
    public static void serverStopped(ServerStoppedEvent event) {
        HouseStoreSaver.get(event.getServer().overworld());
    }

    @SubscribeEvent
    public static void addReloadListener(AddReloadListenerEvent event) {
        event.addListener(NPCNames.Loader.getInstance());
        event.addListener(NPCMood.Loader.getInstance());
        event.addListener(NPCDialogs.Loader.getInstance());
        event.addListener(HillOfFleshModelAnimationTable.getInstance());
        event.addListener(new MappedDataLoader());

    }

}
