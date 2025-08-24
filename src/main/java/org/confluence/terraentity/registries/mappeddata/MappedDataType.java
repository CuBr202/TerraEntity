package org.confluence.terraentity.registries.mappeddata;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.resources.ResourceLocation;

import java.util.*;
import java.util.function.Function;

/**
 * MapCodec的类型，也是codecProvider
 */
public class MappedDataType {

    MapCodec<? extends MappedData> codec;
    MappedData defaultData;
    MappedDataConstructor constructor;
    Set<MappedKey<?>> keySet;


    MappedDataType(MapCodec<? extends MappedData> codec,
                   MappedData defaultData,
                   MappedDataConstructor constructor,
                   Set<MappedKey<?>> keySet) {
        this.codec = codec;
        this.defaultData = defaultData;
        this.constructor = constructor;
        this.keySet = keySet;
    }

    /**
     * codec provider
     */
    public MapCodec<? extends MappedData> getCodec(){
        return codec;
    }

    /**
     * 获取值
     * @param key 绑定的键
     * @return 值
     * @param <V> 值类型
     */
    public <V> V getData(MappedKey<V> key){
        return defaultData.getData(key);
    }

    /**
     * 把neoData的数据添加到或覆盖原始data
     * @param neoData 新的数据
     */
    public void updateData(MappedData neoData){
        for(Map.Entry<MappedKey<?>, Object> entry : neoData.data.entrySet()){
            defaultData.put(entry.getKey(), entry.getValue());
            entry.getKey().onReload(entry.getValue());
        }
    }

    /**
     * 获取默认的MappedData，用于一键数据生成默认的数据
     */
    public MappedData getDefaultValue(){
        return generateDefaultValue(constructor, keySet);
    }

    private static MappedData generateDefaultValue(MappedDataConstructor constructor,
                                                   Collection<MappedKey<?>> codecSet){
        MappedData data = constructor.create(new HashMap<>());
        for(MappedKey<?> key : codecSet){
            Object defaultValue = key.defaultValue().get();
            if(defaultValue!= null){
                data.put(key, defaultValue);
            }
        }
        return data;
    }

    public static MappedDataType.Builder builder() {
        return new MappedDataType.Builder();
    }

    public static class Builder {

        Map<String, MappedKey<?>> keyMap;
        Set<MappedKey<?>> keySet;
        public Builder() {
            keySet = new HashSet<>();
            keyMap = new HashMap<>();
        }

        /**
         * 注册key对应的值的codec
         */
        public <V> MappedKey<V> registerCodec(String key, Codec<V> codec) {
            MappedKey<V> mappedKey = new MappedKey<>(key);
            if(keySet.contains(mappedKey)){
                throw new IllegalArgumentException("Codec already registered for key: " + key);
            }
            keyMap.put(mappedKey.toString(), mappedKey);
            keySet.add(mappedKey);
            mappedKey.valueCodec = codec;
            return mappedKey;
        }

        /**
         * 注册key对应的值的codec
         */
        public <V> MappedKey<V> registerCodec(ResourceLocation key, Codec<V> codec) {
            return registerCodec(key.toString(), codec);
        }

        /**
         * 绑定codec
         * @param constructor MappedData的构造函数
         * @return 绑定后的MappedDataType
         */
        public MappedDataType build(MappedDataConstructor constructor) {
            // 为了获取唯一实例
            Codec<MappedKey<?>> keyCodec = MappedKey.ID_CODEC.xmap(i->keyMap.get(i.toString()), Function.identity());
            MapCodec<? extends MappedData> codec = Codec.<MappedKey<?>, Object>dispatchedMap(keyCodec, MappedKey::getValueCodec)
                    .xmap(constructor::create, map->map.data).fieldOf("data");
            MappedData data = generateDefaultValue(constructor, keySet);
            keySet.forEach(key -> key.keyCodec = keyCodec);
            return new MappedDataType(codec, data, constructor, keySet);
        }
    }

    @FunctionalInterface
    public interface MappedDataConstructor {
        MappedData create(Map<MappedKey<?>, Object> data);
    }

}
