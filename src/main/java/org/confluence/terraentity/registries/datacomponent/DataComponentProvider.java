package org.confluence.terraentity.registries.datacomponent;

import com.mojang.serialization.Codec;
import org.confluence.terraentity.registries.LazyCodecProvider;

import java.util.function.Supplier;

public class DataComponentProvider<T extends IDataComponentType<T>> extends LazyCodecProvider<T> {
    String name;

    public DataComponentProvider(String name, Supplier<Codec<T>> mapCodecSupplier) {
        super(mapCodecSupplier);
        this.name = name;
    }

    public String name(){
        return name;
    }



}
