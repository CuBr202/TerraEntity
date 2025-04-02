package org.confluence.terraentity.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.confluence.terraentity.registries.datacomponent.DataComponentProvider;
import org.confluence.terraentity.registries.datacomponent.IDataComponentType;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * 适配1.21.1的组件系统
 */
public class TEItemProperties extends Item.Properties {
    Map<Supplier<? extends DataComponentProvider>, IDataComponentType> dataComponentTypeMap = new HashMap<>();


    public<B extends IDataComponentType<B>> TEItemProperties component(Supplier<DataComponentProvider<B>> provider, B dataComponent){
        dataComponentTypeMap.put(provider, dataComponent);
        return this;
    }

    public void init(ItemStack stack){
        this.dataComponentTypeMap.forEach((k,v)->{
            v.writeToNBT(k, stack.getOrCreateTag());
        });
    }
}
