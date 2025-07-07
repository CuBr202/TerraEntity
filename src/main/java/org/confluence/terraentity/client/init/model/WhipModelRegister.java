package org.confluence.terraentity.client.init.model;

import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.init.item.TEWhipItems;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.Reader;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * 鞭子模型注册管理类
 */
public class WhipModelRegister extends AbstractModelRegister<Item> {

    Map<Item, ResourceLocation> additionalModels;

    static Codec<Map<Item, ResourceLocation>> CODEC = Codec.unboundedMap(ForgeRegistries.ITEMS.getCodec(), ResourceLocation.CODEC);

    private static WhipModelRegister instance;
    public static WhipModelRegister getInstance() {
        if(instance == null) {
            instance = new WhipModelRegister();
        }
        return instance;
    }

    public @Nullable ModelResourceLocation process(ResourceLocation location){
        String[] splits = location.getPath().split("[./]");
        int len = splits.length;
        String name = splits[len - 2];
        if(location.getPath().endsWith("config.json")){
            ResourceManager provider = Minecraft.getInstance().getResourceManager();

            try{
                Reader reader = provider.openAsReader(location);
                JsonObject jsonobject = GsonHelper.parse(reader);
                this.additionalModels = CODEC.decode(JsonOps.INSTANCE, jsonobject).result().get().getFirst();
            }catch (IOException e){
                TerraEntity.LOGGER.error("Can't open config file: {}", location, e);
                return null;
            }catch (NoSuchElementException e){
                TerraEntity.LOGGER.error("Failed to load model config: {}", location, e);
                return null;
            }catch (Exception e){
                TerraEntity.LOGGER.error("Failed to load model config: {}", location, e);
            }
            return null;
        }

        for( RegistryObject<Item> item : TEWhipItems.ITEMS.getEntries()){
            String itemName = item.getId().toString();
            if(itemName.equals(location.getNamespace()+ ":" + name)){
                ResourceLocation modelLocation = TerraEntity.fromSpaceAndPath(location.getNamespace(), location.getPath().substring(12).replace(".json", ""));
                ModelResourceLocation modelResourceLocation = new ModelResourceLocation(modelLocation, "inventory");

                put(item.get(), modelResourceLocation);
                return modelResourceLocation;
            }
        }
        return null;
    }

    public void register(ModelEvent.RegisterAdditional event){
        super.register(event);
        this.additionalModels.forEach((item, location) -> {
            ResourceLocation modelLocation = TerraEntity.fromSpaceAndPath(location.getNamespace(), location.getPath().substring(12).replace(".json", ""));
            ModelResourceLocation modelResourceLocation = new ModelResourceLocation(modelLocation, "inventory");
//                event.register(modelResourceLocation);
            put(item, modelResourceLocation);
            TerraEntity.LOGGER.info("Registering whip model for {}: {}", item.getDescriptionId(), modelResourceLocation);
        });

    }

    @Override
    protected void outputLog(ResourceLocation location){
        if(!location.getPath().endsWith("config.json")) {
            super.outputLog(location);
        }
    }

    @Override
    protected String getFolder() {
        return "item/whip";
    }

}
