package org.confluence.terraentity.network;


import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.network.c2s.ServerBoundHousePacket;
import org.confluence.terraentity.network.c2s.ServerBoundVehicleExtensionPacket;
import org.confluence.terraentity.network.s2c.SyncBossEventHealthPacket;
import org.confluence.terraentity.network.s2c.SyncCameraShakePacket;
import org.confluence.terraentity.network.s2c.SyncNPCTradesPacketS2C;
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
        CHANNEL.registerMessage(packetId++,  SyncNPCTradesPacketS2C.class,  SyncNPCTradesPacketS2C::encode,  SyncNPCTradesPacketS2C::decode,  SyncNPCTradesPacketS2C::handle);

        CHANNEL.registerMessage(packetId++,  ServerBoundVehicleExtensionPacket.class,  ServerBoundVehicleExtensionPacket::encode,  ServerBoundVehicleExtensionPacket::decode,  ServerBoundVehicleExtensionPacket::handle);
        CHANNEL.registerMessage(packetId++,  ServerBoundHousePacket.class,  ServerBoundHousePacket::encode,  ServerBoundHousePacket::decode,  ServerBoundHousePacket::handle);



    }
}
