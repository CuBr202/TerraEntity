package org.confluence.terraentity.client.event;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;
import org.confluence.terraentity.item.BaseWhipItem;

import static org.confluence.terraentity.TerraEntity.MODID;

@EventBusSubscriber(modid = MODID,bus = EventBusSubscriber.Bus.GAME,value = Dist.CLIENT)
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
