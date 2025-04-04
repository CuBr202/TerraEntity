package org.confluence.terraentity.network;

import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import org.confluence.terraentity.network.c2s.ServerBoundVehicleExtensionPacket;
import org.confluence.terraentity.network.s2c.SyncBossEventHealthPacket;
import org.confluence.terraentity.network.s2c.SyncCameraShakePacket;
import org.confluence.terraentity.network.s2c.SyncSummonPacket;

public final class NetworkHandler {

    public static void register(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1.0.0");
        registrar.playToClient(SyncCameraShakePacket.TYPE, SyncCameraShakePacket.STREAM_CODEC, SyncCameraShakePacket::handle);
        registrar.playToClient(SyncSummonPacket.TYPE, SyncSummonPacket.STREAM_CODEC, SyncSummonPacket::handle);
        registrar.playToClient(SyncBossEventHealthPacket.TYPE, SyncBossEventHealthPacket.STREAM_CODEC, SyncBossEventHealthPacket::handle);

        registrar.playToServer(ServerBoundVehicleExtensionPacket.TYPE, ServerBoundVehicleExtensionPacket.STREAM_CODEC, ServerBoundVehicleExtensionPacket::handle);

    }
}
