package org.confluence.terraentity.event;

import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.api.event.NPCEvent;
import org.confluence.terraentity.data.saved_data.HouseStoreSaver;
import org.confluence.terraentity.entity.npc.NPCDialogs;
import org.confluence.terraentity.entity.npc.NPCNames;
import org.confluence.terraentity.entity.npc.NPCTrades;
import org.confluence.terraentity.network.s2c.SyncNPCTradesPacketS2C;
import org.confluence.terraentity.utils.AdapterUtils;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.GAME, modid = TerraEntity.MODID)
public class GameEvent {

    @SubscribeEvent
    public static void onDatapackSync(OnDatapackSyncEvent event) {
        ServerPlayer serverPlayer = event.getPlayer();
        if (serverPlayer != null) {
            SyncNPCTradesPacketS2C.sync(serverPlayer);
        }
    }

    @SubscribeEvent
    public static void serverStarted(ServerStartedEvent event) {
        NPCTrades.readTradesFromJson(event.getServer().getResourceManager());
        HouseStoreSaver.get(event.getServer().overworld());
        NPCNames.loadNPCNames(event.getServer().getResourceManager());
        NPCDialogs.loadNPCDialogs(event.getServer().getResourceManager());
        AdapterUtils.postEvent(new NPCEvent.NPCBrainCollectionEvent());
    }

    @SubscribeEvent
    public static void serverStarted(ServerStoppedEvent event) {
        HouseStoreSaver.get(event.getServer().overworld());
    }
}
