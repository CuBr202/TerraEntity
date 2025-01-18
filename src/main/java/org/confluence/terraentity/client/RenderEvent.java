package org.confluence.terraentity.client;



import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static org.confluence.terraentity.TerraEntity.MODID;

@Mod.EventBusSubscriber(modid = MODID,bus = Mod.EventBusSubscriber.Bus.FORGE,value = Dist.CLIENT)
public class RenderEvent {
//    @SubscribeEvent
//    public static void guiEvent( RenderGuiLayerEvent.Pre event){
//        if(event.getName().getPath().equals("boss_overlay")){
////            event.getGuiGraphics().setColor(0,1,0,1);
//        }
//    }
}
