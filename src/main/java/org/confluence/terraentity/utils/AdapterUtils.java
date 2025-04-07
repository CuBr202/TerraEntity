package org.confluence.terraentity.utils;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.Event;
import net.neoforged.fml.ModLoader;
import net.neoforged.fml.event.IModBusEvent;
import net.neoforged.neoforge.common.NeoForge;
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

    public static <T extends Event> T postModEvent(T event){
        return NeoForge.EVENT_BUS.post(event);
    }

    public static <T extends Event & IModBusEvent> void postEvent(T event){
        ModLoader.postEvent(event);
    }
}
