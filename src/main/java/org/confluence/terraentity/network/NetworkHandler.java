package org.confluence.terraentity.network;

import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import org.confluence.terraentity.network.c2s.NPCShopPacket;
import org.confluence.terraentity.network.c2s.ServerBoundHousePacket;
import org.confluence.terraentity.network.c2s.ServerBoundVehicleExtensionPacket;
import org.confluence.terraentity.network.s2c.SyncBossEventHealthPacket;
import org.confluence.terraentity.network.s2c.SyncCameraShakePacket;
import org.confluence.terraentity.network.s2c.SyncNPCTradesPacketS2C;
import org.confluence.terraentity.network.s2c.SyncSummonPacket;

public final class NetworkHandler {

    public static void register(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1.0.0");
        registrar.playToClient(SyncCameraShakePacket.TYPE, SyncCameraShakePacket.STREAM_CODEC, SyncCameraShakePacket::handle);
        registrar.playToClient(SyncSummonPacket.TYPE, SyncSummonPacket.STREAM_CODEC, SyncSummonPacket::handle);
        registrar.playToClient(SyncBossEventHealthPacket.TYPE, SyncBossEventHealthPacket.STREAM_CODEC, SyncBossEventHealthPacket::handle);
        registrar.playToClient(SyncNPCTradesPacketS2C.TYPE, SyncNPCTradesPacketS2C.STREAM_CODEC, SyncNPCTradesPacketS2C::handle);

        registrar.playToServer(ServerBoundVehicleExtensionPacket.TYPE, ServerBoundVehicleExtensionPacket.STREAM_CODEC, ServerBoundVehicleExtensionPacket::handle);
        registrar.playToServer(ServerBoundHousePacket.TYPE, ServerBoundHousePacket.STREAM_CODEC, ServerBoundHousePacket::handle);
        registrar.playToServer(NPCShopPacket.TYPE, NPCShopPacket.STREAM_CODEC, NPCShopPacket::handle);

    }
}
