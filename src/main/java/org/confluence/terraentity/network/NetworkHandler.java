package org.confluence.terraentity.network;


import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.network.c2s.ServerBoundVehicleExtensionPacket;
import org.confluence.terraentity.network.s2c.SyncBossEventHealthPacket;
import org.confluence.terraentity.network.s2c.SyncCameraShakePacket;
import org.confluence.terraentity.network.s2c.SyncSummonPacket;

public final class NetworkHandler {
    public static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
        TerraEntity.space("main"),
        () -> PROTOCOL_VERSION,
        PROTOCOL_VERSION::equals,
        PROTOCOL_VERSION::equals
    );

    private static int packetId = 0;

    public static void register() {
        CHANNEL.registerMessage(packetId++,  SyncCameraShakePacket.class,  SyncCameraShakePacket::encode,  SyncCameraShakePacket::decode,  SyncCameraShakePacket::handle);
        CHANNEL.registerMessage(packetId++,  SyncSummonPacket.class,  SyncSummonPacket::encode,  SyncSummonPacket::decode,  SyncSummonPacket::handle);
        CHANNEL.registerMessage(packetId++,  SyncBossEventHealthPacket.class,  SyncBossEventHealthPacket::encode,  SyncBossEventHealthPacket::decode,  SyncBossEventHealthPacket::handle);
        CHANNEL.registerMessage(packetId++,  ServerBoundVehicleExtensionPacket.class,  ServerBoundVehicleExtensionPacket::encode,  ServerBoundVehicleExtensionPacket::decode,  ServerBoundVehicleExtensionPacket::handle);



    }
}
