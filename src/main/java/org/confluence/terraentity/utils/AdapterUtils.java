package org.confluence.terraentity.utils;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.component.Fireworks;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.neoforged.bus.api.Event;
import net.neoforged.fml.ModLoader;
import net.neoforged.fml.event.IModBusEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.List;

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

    public static void enchant(ItemStack stack, ResourceKey<Enchantment> enchantment, int level, HolderLookup.RegistryLookup<Enchantment> enchantLookup){
          stack.enchant(enchantLookup.get(enchantment).get(),level);
    }

    public static void setPotion(ItemStack stack, Holder<Potion> potion){
        stack.set(DataComponents.POTION_CONTENTS, PotionContents.EMPTY.withPotion(potion));
    }

    public static void setFirework(ItemStack stack, int duration){
        stack.set(DataComponents.FIREWORKS, new Fireworks(duration, List.of()));
    }
}
