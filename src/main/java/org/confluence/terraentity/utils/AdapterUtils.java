package org.confluence.terraentity.utils;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;

public class AdapterUtils {
    public static void sendToPlayer(ServerPlayer player, CustomPacketPayload payload){
        PacketDistributor.sendToPlayer(player, payload);

    }
    public static void sendToAllPlayers(CustomPacketPayload payload){
        PacketDistributor.sendToAllPlayers(payload);
    }

    public static void sendToServer(CustomPacketPayload payload){
        PacketDistributor.sendToServer(payload);
    }
}
