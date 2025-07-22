package org.confluence.terraentity.api;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public interface ILeftClickReceiver {

    void onReceiveLeftClick(Player player, ItemStack itemStack);

    void onReceiveLeftRelease(Player player, ItemStack itemStack);

}
