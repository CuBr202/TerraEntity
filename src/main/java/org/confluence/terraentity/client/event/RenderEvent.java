package org.confluence.terraentity.client.event;



import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.CustomizeGuiOverlayEvent;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.client.buffer.DebugBlocksHelper;
import org.confluence.terraentity.client.gui.CustomizeBossHealthBar;
import org.confluence.terraentity.client.post.BrainTranslucent;
import org.confluence.terraentity.config.ClientConfig;

import static org.confluence.terraentity.TerraEntity.MODID;

@Mod.EventBusSubscriber(modid = MODID,bus = Mod.EventBusSubscriber.Bus.FORGE,value = Dist.CLIENT)
public class RenderEvent {


    @SubscribeEvent
    public static void drawBossBar(CustomizeGuiOverlayEvent.BossEventProgress event) {
//        String name = ((TranslatableContents)event.getBossEvent().getName().getContents()).getKey().split("\\.",2)[1];
        if(ClientConfig.BossBarStyle.get() != 0){
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
//        if(HotSwap.consume > 0){
//            event.setCanceled(true);
//            return;
//        }
//        HotSwap.consume --;
    }
}
