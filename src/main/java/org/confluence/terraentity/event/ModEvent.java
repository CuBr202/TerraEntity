package org.confluence.terraentity.event;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.attachment.SummonerProvider;
import org.confluence.terraentity.init.TEAttributes;
import org.confluence.terraentity.network.NetworkHandler;

import java.util.List;

@Mod.EventBusSubscriber(modid = TerraEntity.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEvent {

    @SubscribeEvent
    public static void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
//            Config.init();
            NetworkHandler.register();
        });
    }

    @SubscribeEvent
    public static void onLoad(final ModConfigEvent event) {
//        ServerConfig.init();
//        ClientConfig.load();


    }

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeModificationEvent event) {
        // 召唤师属性
        List.of(TEAttributes.MINION_CAPACITY, TEAttributes.SENTRY_CAPACITY, TEAttributes.SUMMON_DAMAGE, TEAttributes.SUMMON_KNOCKBACK, TEAttributes.WHIP_RANGE)
                .forEach(att-> event.add(EntityType.PLAYER, att.get()));

    }


}