package org.confluence.terraentity.client.entity.model;

import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.init.TEItems;
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

    public void register(Item whip, ModelResourceLocation model){
        whipModelMap.put(whip, model);
    }

    public @Nullable ModelResourceLocation process(ResourceLocation location){
        String[] splits = location.getPath().split("[./]");
        int len = splits.length;
        String name = splits[len - 2];
        for( var item : TEItems.WHIP_ITEMS.getEntries()){
            String itemName = item.getId().toString();
            if(itemName.equals(location.getNamespace()+ ":" + name)){
                ResourceLocation modelLocation = TerraEntity.fromSpaceAndPath(location.getNamespace(), location.getPath().substring(12).replace(".json", ""));
                ModelResourceLocation modelResourceLocation = new ModelResourceLocation(modelLocation, "inventory");
                register(item.get(), modelResourceLocation);
                return modelResourceLocation;
            }
        }
        return null;
    }
}
