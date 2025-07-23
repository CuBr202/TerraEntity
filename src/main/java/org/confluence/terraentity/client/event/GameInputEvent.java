package org.confluence.terraentity.client.event;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;
import org.confluence.terraentity.api.ILeftClickStateItem;
import org.confluence.terraentity.attachment.WeaponStorage;
import org.confluence.terraentity.init.TEAttachments;
import org.confluence.terraentity.item.BaseWhipItem;
import org.confluence.terraentity.network.c2s.ServerBoundEventPacket;

import static org.confluence.terraentity.TerraEntity.MODID;

@EventBusSubscriber(modid = MODID,bus = EventBusSubscriber.Bus.GAME,value = Dist.CLIENT)
public class GameInputEvent {

    @SubscribeEvent
    public static void mouseScroll(InputEvent.MouseScrollingEvent event) {
        // 防止切鞭，非常超模
        Player player = Minecraft.getInstance().player;
        if(player == null){
            return;
        }
        ItemStack stack = player.getMainHandItem();
        Item item = stack.getItem();

        if (item  instanceof BaseWhipItem whipItem
                && player.getCooldowns().isOnCooldown(whipItem)) {
            event.setCanceled(true);
            return;
        }

        if(item instanceof ILeftClickStateItem item1){
            if(!item1.canSwitchWithoutRelease(player, stack) && player.getData(TEAttachments.WEAPON_STORAGE).leftClicking){
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void mouseClick(InputEvent.MouseButton.Pre event) {
        Player player = Minecraft.getInstance().player;
        if (player != null && !Minecraft.getInstance().isPaused() && Minecraft.getInstance().screen == null) {
            WeaponStorage data = player.getData(TEAttachments.WEAPON_STORAGE);
            boolean clicking = data.leftClicking;
            if(event.getButton() == 0){ // 左键
                if (event.getAction() == 0) { // 松开
                    data.leftClicking = false;
                    if(clicking) {
//                        player.sendSystemMessage(Component.literal("not clicking"));
                        ServerBoundEventPacket.mouseRelease();

                    }
                    return;
                }
                ItemStack stack = player.getMainHandItem();
                if(stack.getItem() instanceof ILeftClickStateItem) {
                    if (event.getAction() == 1) { // 按下
                        if (!clicking) {
                            data.leftClicking = true;
//                            player.sendSystemMessage(Component.literal("clicking"));
                            ServerBoundEventPacket.mouseLeftClick();
                        }
                        return;
                    }
                }
            }
        }

    }
}
