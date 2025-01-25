package org.confluence.terraentity.client;

import com.mojang.blaze3d.pipeline.MainTarget;
import com.mojang.blaze3d.pipeline.TextureTarget;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.CustomizeGuiOverlayEvent;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;
import net.neoforged.neoforge.client.event.RenderHandEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.client.gui.CustomizeBossHealthBar;
import org.confluence.terraentity.client.post.BrainTranslucent;
import org.confluence.terraentity.mixin.accessor.GameRendererAccessor;
import org.confluence.terraentity.mixinauxiliary.HotSwap;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

import static org.confluence.terraentity.TerraEntity.MODID;
import static org.confluence.terraentity.client.ClientConfig.bossBarStyle;

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
        }

    }


    @SubscribeEvent
    public static void renderHand(RenderHandEvent event) {
        if(!HotSwap.consume){
            event.setCanceled(true);
            return;
        }
        HotSwap.consume = false;
    }
}
