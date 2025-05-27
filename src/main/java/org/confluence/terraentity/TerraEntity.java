package org.confluence.terraentity;


import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import org.confluence.terraentity.api.event.WhipRegisterModifyEvent;
import org.confluence.terraentity.config.ServerConfig;
import org.confluence.terraentity.data.biome.TEBiomes;
import org.confluence.terraentity.event.ModEvent;
import org.confluence.terraentity.init.*;
import org.confluence.terraentity.registries.TERegistries;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(TerraEntity.MODID)
public class TerraEntity {
    public static final String MODID = "terra_entity";
    public static final Logger LOGGER = LoggerFactory.getLogger("TerraEntity");
    public static ResourceLocation space(String path) {return ResourceLocation.fromNamespaceAndPath(MODID, path);}
    public static ResourceLocation parse(String path){return ResourceLocation.parse(path);}
    public static ResourceLocation fromSpaceAndPath(String space, String path){return ResourceLocation.fromNamespaceAndPath(space, path);}
    public static ResourceLocation defaultPath(String path){return ResourceLocation.withDefaultNamespace(path);}

    public static String toLang(ResourceLocation location){return location.toLanguageKey().replace("/",".");}

    public TerraEntity (IEventBus modEventBus, ModContainer modContainer) {

        newListener(modEventBus);
        modEventBus.addListener(TERegistries::newRegistry);

        TEEntities.register(modEventBus);
        TERegistries.register(modEventBus);

        TESounds.SOUNDS.register(modEventBus);
        TEParticles.PARTICLES.register(modEventBus);
        TEItems.register(modEventBus);
        TEEffects.EFFECTS.register(modEventBus);
        TEAttachments.TYPES.register(modEventBus);
        TEAttributes.ATTRIBUTES.register(modEventBus);
        TEDataComponentTypes.TYPES.register(modEventBus);
        TEEntityDataSerializers.SERIALIZERS.register(modEventBus);
        TEBlocks.register(modEventBus);
        TEAi.register(modEventBus);
        TEMenus.TYPES.register(modEventBus);
        TEBiomes.register(modEventBus);

        modContainer.registerConfig(ModConfig.Type.SERVER, ServerConfig.init());
//        modContainer.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }



    public void newListener(IEventBus eventBus){
        eventBus.addListener(WhipRegisterModifyEvent.class, event -> {});
        eventBus.addListener(ModEvent::onCollectBrains);

    }
}
