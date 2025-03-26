package org.confluence.terraentity;


import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import net.minecraft.resources.ResourceLocation;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.NewRegistryEvent;
import net.minecraftforge.registries.RegistryBuilder;
import org.apache.commons.math3.analysis.interpolation.SplineInterpolator;
import org.confluence.terraentity.config.ConfigRegistry;
import org.confluence.terraentity.data.component.EffectStrategyComponent;
import org.confluence.terraentity.data.gen.biome.TEBiomes;
import org.confluence.terraentity.init.*;
import org.confluence.terraentity.registries.TERegistries;
import org.confluence.terraentity.registries.datacomponent.DataComponentProvider;
import org.slf4j.Logger;

import java.util.function.Supplier;

@SuppressWarnings("removal")
@Mod(TerraEntity.MODID)
public class TerraEntity {
    public static final String MODID = "terra_entity";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static ResourceLocation space(String path) {return new ResourceLocation(MODID, path);}
    public static ResourceLocation parse(String path){return ResourceLocation.parse(path);}
    public static ResourceLocation fromSpaceAndPath(String space, String path){return new ResourceLocation(space, path);}
    public static String toLang(ResourceLocation location){return location.toLanguageKey().replace("/",".");}

    public TerraEntity () {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

//        TERegistries.newRegistry(modEventBus);
        TERegistries.register(modEventBus);


        TEEntities.ENTITIES.register(modEventBus);
        TESounds.SOUNDS.register(modEventBus);
        TEParticles.PARTICLES.register(modEventBus);
        TEItems.register(modEventBus);
        TEEffects.EFFECTS.register(modEventBus);
        TEAttributes.ATTRIBUTES.register(modEventBus);
        TEBiomes.register(modEventBus);

        var a = new SplineInterpolator();
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ConfigRegistry.register());
//        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, SPEC);
    }




    public static ResourceLocation asResource(String path) {
        return new ResourceLocation(MODID, path);
    }

    public static ResourceLocation asResource(String id, String path) {
        return new ResourceLocation(id, path);
    }
}
