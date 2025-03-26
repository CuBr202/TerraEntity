package org.confluence.terraentity.registries.datacomponent;

import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.MapCodec;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import org.confluence.terraentity.init.TEDataComponentTypes;
import org.confluence.terraentity.registries.TERegistries;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

/**
 * 数据组件接口
 * @param <T> 数据组件类型
 */
public interface IDataComponentType<T extends IDataComponentType<T>> {

    /**
     * NBT 名称id
     */
    default String name(){
        return getCodec().get().name();
    }

    /**
     * NBT 编解码器
     */
    default Codec<T> codec(){
        return getCodec().get().codec().get();
    }

    /**
     * 写入NBT
     * @param tag 待写入的NBT
     */
    default void writeToNBT(CompoundTag tag){
        JsonElement obj = codec().encodeStart(JsonOps.INSTANCE, (T) this).result().get();
        tag.putString(name(), obj.toString());
    }


    Supplier<DataComponentProvider<T>> getCodec();

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
        return component.codec().get().decode(JsonOps.INSTANCE, GsonHelper.parseArray(tag1)).result().get().getFirst();
    }

}
