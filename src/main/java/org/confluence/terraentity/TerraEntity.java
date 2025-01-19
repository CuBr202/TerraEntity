package org.confluence.terraentity;


import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.confluence.terraentity.init.*;
import org.slf4j.Logger;

@SuppressWarnings("removal")
@Mod(TerraEntity.MODID)
public class TerraEntity {
    public static final String MODID = "terra_entity";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static ResourceLocation space(String path) {return ResourceLocation.fromNamespaceAndPath(MODID, path);}

    public TerraEntity (FMLJavaModLoadingContext context) {
        IEventBus modEventBus = context.getModEventBus();

        TEEntities.ENTITIES.register(modEventBus);
        TESounds.SOUNDS.register(modEventBus);
        TEParticles.PARTICLES.register(modEventBus);
        TEItems.SPAWN_EGGS.register(modEventBus);
        TEItems.TABS.register(modEventBus);
        TEEffects.EFFECTS.register(modEventBus);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON,Config.SPEC);

//        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, SPEC);
    }

    public static ResourceLocation asResource(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }

    public static ResourceLocation asResource(String id, String path) {
        return ResourceLocation.fromNamespaceAndPath(id, path);
    }
}
