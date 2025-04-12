package org.confluence.terraentity.event;

import net.minecraft.server.level.ServerPlayer;

import net.minecraftforge.event.OnDatapackSyncEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.event.server.ServerStoppedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.data.saved_data.HouseStoreSaver;
import org.confluence.terraentity.entity.npc.NPCTrades;
import org.confluence.terraentity.network.s2c.SyncNPCTradesPacketS2C;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, modid = TerraEntity.MODID)
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
    }

    @SubscribeEvent
    public static void serverStarted(ServerStoppedEvent event) {
        HouseStoreSaver.get(event.getServer().overworld());
    }
}
