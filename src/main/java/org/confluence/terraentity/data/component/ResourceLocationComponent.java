package org.confluence.terraentity.data.component;

import com.github.edg_thexu.cafelib.api.datacomponent.IDataComponentType;
import com.github.edg_thexu.cafelib.data.codec.DataComponentProvider;
import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Supplier;

public record ResourceLocationComponent(ResourceLocation location) implements IDataComponentType<ResourceLocationComponent> {

    public static Supplier<Codec<ResourceLocationComponent>> CODEC = Suppliers.memoize(()->ResourceLocation.CODEC.xmap(ResourceLocationComponent::new, ResourceLocationComponent::location));
//    public static StreamCodec<ByteBuf, ResourceLocationComponent> STREAM_CODEC = ByteBufCodecs.fromCodec(CODEC);


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResourceLocationComponent that = (ResourceLocationComponent) o;
        return Objects.equals(location, that.location);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(location);
    }


    @Override
    public DataComponentProvider<ResourceLocationComponent> provider() {
        return null;
    }
}
