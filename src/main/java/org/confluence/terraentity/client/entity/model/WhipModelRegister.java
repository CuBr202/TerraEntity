package org.confluence.terraentity.client.entity.model;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.event.ModelEvent;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.init.item.TEWhipItems;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * 鞭子模型注册管理类
 */
public class WhipModelRegister {

    private static WhipModelRegister instance;
    public static WhipModelRegister getInstance() {
        if(instance == null) {
            instance = new WhipModelRegister();
        }
        return instance;
    }

    public Map<Item, ModelResourceLocation> whipModelMap = new HashMap<>();

    public ModelResourceLocation getModelResourceLocation(Item item) {
        return whipModelMap.get(item);
    }

    private void put(Item whip, ModelResourceLocation model){
        whipModelMap.put(whip, model);
    }

    public @Nullable ModelResourceLocation process(ResourceLocation location){
        String[] splits = location.getPath().split("[./]");
        int len = splits.length;
        String name = splits[len - 2];
        for( var item : TEWhipItems.ITEMS.getEntries()){
            String itemName = item.getId().toString();
            if(itemName.equals(location.getNamespace()+ ":" + name)){
                ResourceLocation modelLocation = TerraEntity.fromSpaceAndPath(location.getNamespace(), location.getPath().substring(7).replace(".json", ""));
                ModelResourceLocation modelResourceLocation = ModelResourceLocation.standalone(modelLocation);
//                event.register(modelResourceLocation);
                put(item.get(), modelResourceLocation);
                return modelResourceLocation;
            }
        }
        return null;
    }
    public static void register(ModelEvent.RegisterAdditional event){
        ResourceManager provider = Minecraft.getInstance().getResourceManager();
        provider.listResources("models/item/whip", s -> s.getPath().endsWith(".json")).forEach((location, resource) -> {
            var f = WhipModelRegister.getInstance().process(location);
            if(f!= null){
                event.register(f);
            }else{
                TerraEntity.LOGGER.warn("Failed to load whip model: {}", location);
            }
        });
    }
}
