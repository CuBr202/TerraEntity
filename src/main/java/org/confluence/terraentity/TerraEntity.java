package org.confluence.terraentity;


import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import org.confluence.terraentity.config.ServerConfig;
import org.confluence.terraentity.data.gen.biome.TEBiomes;
import org.confluence.terraentity.init.*;
import org.confluence.terraentity.registries.EffectStrategies;
import org.slf4j.Logger;

@Mod(TerraEntity.MODID)
public class TerraEntity {
    public static final String MODID = "terra_entity";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static ResourceLocation space(String path) {return ResourceLocation.fromNamespaceAndPath(MODID, path);}


    public TerraEntity (IEventBus modEventBus, ModContainer modContainer) {
        TEEntities.ENTITIES.register(modEventBus);
        TESounds.SOUNDS.register(modEventBus);
        TEParticles.PARTICLES.register(modEventBus);
        TEItems.register(modEventBus);
        TEEffects.EFFECTS.register(modEventBus);
        TEAttachments.TYPES.register(modEventBus);
        TEAttributes.ATTRIBUTES.register(modEventBus);
        TEBiomes.register(modEventBus);
        modEventBus.addListener(TerraEntity::newRegistry);

        modContainer.registerConfig(ModConfig.Type.SERVER, ServerConfig.init());
//        modContainer.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);

    }

    public static void newRegistry(NewRegistryEvent event) {
        event.register(EffectStrategies.EFFECT_STRATEGY_REGISTRY);

    }

    public static ResourceLocation asResource(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }

    public static ResourceLocation asResource(String id, String path) {
        return ResourceLocation.fromNamespaceAndPath(id, path);
    }
}
