package org.confluence.terraentity.client;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import org.confluence.terraentity.config.ClientConfig;

import java.util.function.Supplier;

import static org.confluence.terraentity.TerraEntity.MODID;

@Mod(value = MODID, dist = Dist.CLIENT)
public class ClientTerraEntity {

    public ClientTerraEntity(IEventBus modEventBus, ModContainer container) {

        container.registerConfig(ModConfig.Type.CLIENT, ClientConfig.init());
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    public static Supplier<String> seKey;
    public static boolean shouldSe(ResourceLocation location, String result){
        String namespace = location.getNamespace();
        if(!namespace.equals(MODID)){
            return false;
        }
//        String path = location.getPath();
//        if(path.startsWith("geo/entity/boss")){
//            return true;
//        }
        if(!result.startsWith("{")){
            return true;
        }
        return false;
    }
}
