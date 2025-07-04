package org.confluence.terraentity.client.init.model;

import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.init.item.TEWhipItems;
import org.jetbrains.annotations.Nullable;

/**
 * 鞭子模型注册管理类
 */
public class WhipModelRegister extends AbstractModelRegister<Item> {

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
        for( var item : TEWhipItems.ITEMS.getEntries()){
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

    @Override
    protected String getFolder() {
        return "item/whip";
    }

}
