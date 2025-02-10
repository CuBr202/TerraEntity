package org.confluence.terraentity.init;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.attachment.SummonerAttachment;
import org.confluence.terraentity.attachment.SummonerProvider;

public final class TEAttachments {

    public static final Capability<SummonerAttachment> SUMMONER_STORAGE = CapabilityManager.get(new CapabilityToken<>() {});


    @Mod.EventBusSubscriber(modid = TerraEntity.MODID,bus = Mod.EventBusSubscriber.Bus.MOD)
    private static class registerCapabilities {
        @SubscribeEvent
        public static void registerCapabilities(RegisterCapabilitiesEvent event) {
            event.register(SummonerAttachment.class);
        }
    }

    @Mod.EventBusSubscriber(modid = TerraEntity.MODID,bus = Mod.EventBusSubscriber.Bus.FORGE)
    private static class attachCapabilities {
        @SubscribeEvent
        public static void attachEntityCapabilities(AttachCapabilitiesEvent<Entity> event) {
            if(event.getObject() instanceof Player){
                event.addCapability(TerraEntity.space("summoner_record"), new SummonerProvider());
            }
        }
    }
}
