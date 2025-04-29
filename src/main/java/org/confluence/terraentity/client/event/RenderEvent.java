package org.confluence.terraentity.client.event;

import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.*;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.client.buffer.DebugBlocksHelper;
import org.confluence.terraentity.client.gui.CustomizeBossHealthBar;
import org.confluence.terraentity.client.post.BrainTranslucent;
import org.confluence.terraentity.item.BaseWhipItem;

import static org.confluence.terraentity.TerraEntity.MODID;
import static org.confluence.terraentity.config.ClientConfig.bossBarStyle;

@EventBusSubscriber(modid = MODID,bus = EventBusSubscriber.Bus.GAME,value = Dist.CLIENT)
public class RenderEvent {
    @SubscribeEvent
    public static void guiEvent( RenderGuiLayerEvent.Pre event){

    }

    @SubscribeEvent
    public static void drawBossBar(CustomizeGuiOverlayEvent.BossEventProgress event) {
//        String name = ((TranslatableContents)event.getBossEvent().getName().getContents()).getKey().split("\\.",2)[1];
        if(bossBarStyle != 0){
            try{
                CustomizeBossHealthBar bar = CustomizeBossHealthBar.getBossHealthBars((event.getBossEvent().getName().getString()));
                if(bar!= null)
                    bar.render(event);
            }catch (Exception e){
                TerraEntity.LOGGER.warn(e.getLocalizedMessage());
            }

        }
    }

    @SubscribeEvent
    public static void renderLevelStage(RenderLevelStageEvent event) {
        if(event.getStage()== RenderLevelStageEvent.Stage.AFTER_LEVEL){
            BrainTranslucent.render(event);
            DebugBlocksHelper.Singleton().render(event);
        }

    }


    @SubscribeEvent
    public static void renderHand(RenderHandEvent event) {
        ItemStack stack = event.getItemStack();
        if (event.getHand() == InteractionHand.MAIN_HAND && stack.getItem() instanceof BaseWhipItem item) {
            // 右手使用鞭子时取消渲染
            Player player = Minecraft.getInstance().player;
            if (player != null && player.getCooldowns().isOnCooldown(item)) {
//                ci.cancel();
                float progress = (player.tickCount - BaseWhipItem.clickTime + event.getPartialTick());
                int cooldown = BaseWhipItem.cooldownTime;
                progress = Math.min(progress, cooldown) / cooldown;

                progress = progress > 0.5? 2 - progress * 2 : progress * 2;
                event.getPoseStack().translate(0, -progress  ,0);
            }
        }


    }



}
