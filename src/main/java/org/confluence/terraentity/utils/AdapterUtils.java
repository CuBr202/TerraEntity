package org.confluence.terraentity.utils;

import com.mojang.serialization.Codec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.registries.ForgeRegistries;
import org.confluence.terraentity.network.NetworkHandler;


public class AdapterUtils {
    public static <MSG> void  sendToPlayer(ServerPlayer player, MSG payload){
        NetworkHandler.CHANNEL.sendTo(payload, player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
    }
    public static <MSG> void sendToAllPlayers(MSG payload){
        NetworkHandler.CHANNEL.send(PacketDistributor.ALL.noArg(), payload);
    }

    public static <MSG> void sendToServer(MSG payload){
        NetworkHandler.CHANNEL.sendToServer(payload);
    }

    public static Codec<MobEffect> getEffectCodec(){
        return ForgeRegistries.MOB_EFFECTS.getCodec();
    }
}
