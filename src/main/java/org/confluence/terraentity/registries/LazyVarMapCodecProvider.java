package org.confluence.terraentity.registries;

import com.mojang.serialization.MapCodec;

import java.util.function.Supplier;

/**
 * 懒加载的MapCodec提供者
 * <P>传入函数接口而不是值类型，防止codec被提前加载</P>
 * @param <T> 接口类型
 */
public class LazyVarMapCodecProvider<T> {
    private MapCodec<? extends T> codec;
    private final Supplier<MapCodec<? extends T>> codecSupplier;
    public MapCodec<? extends T> codec(){
        if(codec == null){
            codec = codecSupplier.get();
        }
        return codec;
    }
    public LazyVarMapCodecProvider(Supplier<MapCodec<? extends T>> codecSupplier) {
        this.codecSupplier = codecSupplier;
    }

}
