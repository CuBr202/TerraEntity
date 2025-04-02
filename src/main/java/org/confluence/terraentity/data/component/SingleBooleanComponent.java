package org.confluence.terraentity.data.component;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;

import org.confluence.terraentity.registries.datacomponent.IDataComponentType;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;


public record SingleBooleanComponent(boolean value) implements IDataComponentType<SingleBooleanComponent> {

    public static final SingleBooleanComponent TRUE = new SingleBooleanComponent(true);
    public static final SingleBooleanComponent FALSE = new SingleBooleanComponent(false);

    public static final Supplier<Codec<SingleBooleanComponent>> CODEC = ()->Codec.BOOL.xmap(SingleBooleanComponent::new, SingleBooleanComponent::value);



    @Override
    public @Nullable Codec<SingleBooleanComponent> codec() {return CODEC.get();}

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if(object instanceof SingleBooleanComponent other){
            return value == other.value;
        }
        return false;
    }

}
