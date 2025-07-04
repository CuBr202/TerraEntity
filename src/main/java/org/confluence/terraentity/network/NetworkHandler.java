package org.confluence.terraentity.network;


import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.network.c2s.NPCShopPacket;
import org.confluence.terraentity.network.c2s.ServerBoundEventPacket;
import org.confluence.terraentity.network.c2s.ServerBoundHousePacket;
import org.confluence.terraentity.network.c2s.ServerBoundVehicleExtensionPacket;
import org.confluence.terraentity.network.s2c.*;

public final class NetworkHandler {
    public static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(TerraEntity.space("main"), () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);

    public static void register() {
        int packetId = 0;
        CHANNEL.registerMessage(packetId++,  SyncCameraShakePacket.class,  SyncCameraShakePacket::encode,  SyncCameraShakePacket::decode,  SyncCameraShakePacket::handle);
        CHANNEL.registerMessage(packetId++,  SyncSummonPacket.class,  SyncSummonPacket::encode,  SyncSummonPacket::decode,  SyncSummonPacket::handle);
        CHANNEL.registerMessage(packetId++,  SyncBossEventHealthPacket.class,  SyncBossEventHealthPacket::encode,  SyncBossEventHealthPacket::decode,  SyncBossEventHealthPacket::handle);
        CHANNEL.registerMessage(packetId++,  SyncNPCTradesPacketS2C.class,  SyncNPCTradesPacketS2C::encode,  SyncNPCTradesPacketS2C::decode,  SyncNPCTradesPacketS2C::handle);
        CHANNEL.registerMessage(packetId++, SyncJsonS2C.class,  SyncJsonS2C::encode,  SyncJsonS2C::decode,  SyncJsonS2C::handle);
        CHANNEL.registerMessage(packetId++,  UpdateNPCTradePacket.class,  UpdateNPCTradePacket::encode,  UpdateNPCTradePacket::decode,  UpdateNPCTradePacket::handle);


        CHANNEL.registerMessage(packetId++,  ServerBoundVehicleExtensionPacket.class,  ServerBoundVehicleExtensionPacket::encode,  ServerBoundVehicleExtensionPacket::decode,  ServerBoundVehicleExtensionPacket::handle);
        CHANNEL.registerMessage(packetId++,  ServerBoundHousePacket.class,  ServerBoundHousePacket::encode,  ServerBoundHousePacket::decode,  ServerBoundHousePacket::handle);
        CHANNEL.registerMessage(packetId++,  NPCShopPacket.class,  NPCShopPacket::encode,  NPCShopPacket::decode,  NPCShopPacket::handle);
        CHANNEL.registerMessage(packetId++,  ServerBoundEventPacket.class,  ServerBoundEventPacket::encode,  ServerBoundEventPacket::decode,  ServerBoundEventPacket::handle);



    }
}
