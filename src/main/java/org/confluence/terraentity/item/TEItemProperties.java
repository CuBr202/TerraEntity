package org.confluence.terraentity.item;

import net.minecraft.world.item.Item;
import org.confluence.terraentity.registries.datacomponent.DataComponentProvider;
import org.confluence.terraentity.registries.datacomponent.IDataComponentType;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * 适配1.21.1的组件系统
 */
public class TEItemProperties extends Item.Properties {
    List< IDataComponentType<?>> dataComponentTypeMap = new ArrayList<>();


    public<B extends IDataComponentType<B>> TEItemProperties component(Supplier<DataComponentProvider<B>> provider, B dataComponent){
        dataComponentTypeMap.add(dataComponent);
        return this;
    }
}
