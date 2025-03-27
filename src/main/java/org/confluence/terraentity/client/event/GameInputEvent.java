package org.confluence.terraentity.client.event;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.confluence.terraentity.item.BaseWhipItem;

import static org.confluence.terraentity.TerraEntity.MODID;

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class GameInputEvent {

    @SubscribeEvent
    public static void mouseScroll(InputEvent.MouseScrollingEvent event) {
        // 防止切鞭，非常超模
        Player player = Minecraft.getInstance().player;
        if (player!= null && player.getMainHandItem().getItem() instanceof BaseWhipItem whipItem
                && player.getCooldowns().isOnCooldown(whipItem)) {
            event.setCanceled(true);
        }
    }

}