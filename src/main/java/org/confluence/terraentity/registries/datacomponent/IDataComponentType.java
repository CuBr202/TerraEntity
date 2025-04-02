package org.confluence.terraentity.registries.datacomponent;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

/**
 * 数据组件接口
 * @param <T> 数据组件类型
 */
public interface IDataComponentType<T extends IDataComponentType<T>> {



    /**
     * 写入NBT
     * @param tag 待写入的NBT,由框架自动调用
     */
    default void writeToNBT(Supplier<DataComponentProvider<T>> provider, CompoundTag tag){
        JsonElement obj = provider.get().codec().get().encodeStart(JsonOps.INSTANCE, (T) this).result().get();
        tag.putString(provider.get().name(), obj.toString());
    }


    Codec<T> codec();

//    MapCodec<? extends IDataComponentType<?>> TYPED_CODEC = TERegistries.DataComponentProviders.REGISTRY.get()
//            .getCodec()
//            .dispatchMap(IDataComponentType::getCodec, i->i.codec().get());


    /**
     * 从NBT中读取数据组件
     * @param tag tag
     * @param provider 组件CODEC
     * @return 数据组件
     * @param <B> 数据组件类型
     */
    static <B extends IDataComponentType<B>> B readFromNBT(CompoundTag tag, DataComponentProvider<B> provider){
        String name = provider.name();
        return provider.codec().get().decode(JsonOps.INSTANCE, GsonHelper.parse(tag.getString(name))).result().get().getFirst();
    }

    /**
     * 从ItemStack中读取数据组件的NBT
     * @param itemStack ItemStack
     * @param provider 组件CODEC
     * @return 数据组件的NBT
     * @param <B> 数据组件类型
     */
    static <B extends IDataComponentType<B>> String getNBT(ItemStack itemStack, DataComponentProvider<B> provider){
        return getNBT(itemStack.getOrCreateTag(), provider);
    }

    /**
     * 从NBT中读取数据组件的NBT
     * @param tag NBT
     * @param component 组件CODEC
     * @return 数据组件的NBT
     * @param <B> 数据组件类型
     */
    static <B extends IDataComponentType<B>> String getNBT(CompoundTag tag, DataComponentProvider<B> component){
        if(!tag.contains(component.name()))
            tag.put(component.name(), new CompoundTag());
        return tag.getString(component.name());
    }

    static <B extends IDataComponentType<B>> @Nullable B getData(ItemStack stack, DataComponentProvider<B> component){
        String tag1 = getNBT(stack, component);
        if(tag1.isEmpty()){
            return null;
        }

        return component.codec().get().decode(JsonOps.INSTANCE, JsonParser.parseString(tag1)).result().get().getFirst();
    }

    static <B extends IDataComponentType<B>> @Nullable B getData(ItemStack stack, Supplier<DataComponentProvider<B>> component){
        return getData(stack, component.get());
    }
}
