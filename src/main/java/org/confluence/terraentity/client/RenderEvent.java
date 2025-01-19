package org.confluence.terraentity.client;



import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.CustomizeGuiOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.client.gui.CustomizeBossHealthBar;

import static org.confluence.terraentity.TerraEntity.MODID;

@Mod.EventBusSubscriber(modid = MODID,bus = Mod.EventBusSubscriber.Bus.FORGE,value = Dist.CLIENT)
public class RenderEvent {

    @SubscribeEvent
    public static void drawBossBar(CustomizeGuiOverlayEvent.BossEventProgress event) {
//        String name = ((TranslatableContents)event.getBossEvent().getName().getContents()).getKey().split("\\.",2)[1];
        try{
            CustomizeBossHealthBar bar = CustomizeBossHealthBar.getBossHealthBars((event.getBossEvent().getName().getString()));
            if(bar!= null)
                bar.render(event);
        }catch (Exception e){
            TerraEntity.LOGGER.warn(e.getLocalizedMessage());
        }
    }
}
