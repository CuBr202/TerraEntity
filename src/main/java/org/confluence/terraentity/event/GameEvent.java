package org.confluence.terraentity.event;

import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;
import net.neoforged.neoforge.event.server.ServerAboutToStartEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.api.event.NPCEvent;
import org.confluence.terraentity.data.saved_data.HouseStoreSaver;
import org.confluence.terraentity.entity.npc.misc.NPCDialogs;
import org.confluence.terraentity.entity.npc.misc.NPCNames;
import org.confluence.terraentity.entity.npc.mood.NPCMoods;
import org.confluence.terraentity.entity.npc.trade.NPCTradeManager;
import org.confluence.terraentity.network.s2c.SyncJsonS2C;
import org.confluence.terraentity.network.s2c.SyncNPCTradesPacketS2C;
import org.confluence.terraentity.utils.AdapterUtils;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.GAME, modid = TerraEntity.MODID)
public class GameEvent {

    @SubscribeEvent
    public static void onDatapackSync(OnDatapackSyncEvent event) {
        ServerPlayer serverPlayer = event.getPlayer();
        if (serverPlayer != null) {
            SyncNPCTradesPacketS2C.sync(serverPlayer);
            SyncJsonS2C.syncNpcDialogs(serverPlayer);
            SyncJsonS2C.syncNpcMoods(serverPlayer);
        }
    }

    @SubscribeEvent
    public static void serverStartBefore(ServerAboutToStartEvent event) {
        NPCTradeManager.readTradesFromJson(event.getServer());
        NPCDialogs.loadNPCDialogs(event.getServer().getResourceManager());
        NPCMoods.loadMoods(event.getServer().getResourceManager());
        AdapterUtils.postEvent(new NPCEvent.NPCBrainCollectionEvent());
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
    }
}
