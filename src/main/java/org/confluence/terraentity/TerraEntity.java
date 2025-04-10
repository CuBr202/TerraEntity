package org.confluence.terraentity;


import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import org.confluence.terraentity.api.event.WhipRegisterModifyEvent;
import org.confluence.terraentity.config.ServerConfig;
import org.confluence.terraentity.init.*;
import org.confluence.terraentity.init.TEBlocks;
import org.confluence.terraentity.registries.TERegistries;
import org.slf4j.Logger;

@Mod(TerraEntity.MODID)
public class TerraEntity {
    public static final String MODID = "terra_entity";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static ResourceLocation space(String path) {return ResourceLocation.fromNamespaceAndPath(MODID, path);}
    public static ResourceLocation parse(String path){return ResourceLocation.parse(path);}
    public static ResourceLocation fromSpaceAndPath(String space, String path){return ResourceLocation.fromNamespaceAndPath(space, path);}
    public static String toLang(ResourceLocation location){return location.toLanguageKey().replace("/",".");}


    public TerraEntity (IEventBus modEventBus, ModContainer modContainer) {
        TEEntities.register(modEventBus);
        newListener(modEventBus);
//        modEventBus.register(WhipRegisterModifyEvent);
        TESounds.SOUNDS.register(modEventBus);
        TEParticles.PARTICLES.register(modEventBus);
        TEItems.register(modEventBus);
        TEEffects.EFFECTS.register(modEventBus);
        TEAttachments.TYPES.register(modEventBus);
        TEAttributes.ATTRIBUTES.register(modEventBus);
        TEDataComponentTypes.TYPES.register(modEventBus);
        TEEffectStrategies.EFFECT_STRATEGY.register(modEventBus);
        TEEntityDataSerializers.SERIALIZERS.register(modEventBus);
        TEBlocks.register(modEventBus);
//        TEActivities.ACTIVITIES.register(modEventBus);
        TEAi.register(modEventBus);
        TEMenus.TYPES.register(modEventBus);

//        TEBiomes.register(modEventBus);

        modEventBus.addListener(TERegistries::newRegistry);
        TERegistries.register(modEventBus);
        modContainer.registerConfig(ModConfig.Type.SERVER, ServerConfig.init());
//        modContainer.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);

    }

    public static ResourceLocation asResource(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }

    public static ResourceLocation asResource(String id, String path) {
        return ResourceLocation.fromNamespaceAndPath(id, path);
    }

    public void newListener(IEventBus bus){
        bus.addListener(WhipRegisterModifyEvent.class, event -> {});

    }
}
