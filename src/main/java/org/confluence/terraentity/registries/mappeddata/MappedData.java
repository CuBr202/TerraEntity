package org.confluence.terraentity.registries.mappeddata;

import com.mojang.serialization.Codec;
import org.confluence.terraentity.registries.TERegistries;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.Map;

/**
 * 类似于DataMap，但不同的是这个类可以存储值类型不相同的map，且不需要对每个类型都注册到注册表，只需要注册MappedDataType即可。
 */
public abstract class MappedData implements Iterable<Map.Entry<MappedKey<?>, Object>> {
    Map<MappedKey<?>, Object> data;

    /**
     * 只是为了生成数据提供信息，游戏中不需要使用
     */
    String comment;

    protected MappedData(Map<MappedKey<?>, Object> data, @Nullable String comment) {
        this.data = data;
        this.comment = comment;
    }

    public <V> V getData(MappedKey<V> key) {
        return (V) this.data.get(key);
    }

    public static Codec<MappedData> CODEC = TERegistries.MAPPED_DATAS.byNameCodec()
            .dispatch(MappedData::getType, MappedDataType::getCodec);


    public abstract MappedDataType getType();

    @Override
    public @NotNull Iterator<Map.Entry<MappedKey<?>, Object>> iterator() {
        return data.entrySet().iterator();
    }

    public int size(){
        return data.size();
    }

    public boolean isEmpty(){
        return data.isEmpty();
    }

    public Object put(MappedKey<?> key, Object value){
        return data.put(key, value);
    }

    public boolean containsKey(MappedKey<?> key){
        return data.containsKey(key);
    }

    public boolean containsValue(Object value){
        return data.containsValue(value);
    }

    public String getComment() {
        return comment;
    }

}
