package org.confluence.terraentity.event;


import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import org.confluence.terraentity.Config;
import org.confluence.terraentity.TerraEntity;

@Mod.EventBusSubscriber(modid = TerraEntity.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEvent {

    @SubscribeEvent
    public static void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
//            Config.init();

        });
    }

    @SubscribeEvent
    public static void onLoad(final ModConfigEvent event) {
        Config.init();
//        ClientConfig.load();
    }
}
