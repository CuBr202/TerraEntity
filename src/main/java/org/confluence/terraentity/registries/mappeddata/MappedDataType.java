package org.confluence.terraentity.registries.mappeddata;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * MapCodec的类型，也是codecProvider
 */
public class MappedDataType{

    MapCodec<? extends MappedData> codec;
    MappedData data;
    Function<? super Map<MappedKey<?>, Object>, ? extends MappedData> constructor;
    Map<MappedKey<?>, Codec<Object>> codecMap;


    MappedDataType(MapCodec<? extends MappedData> codec,
                   MappedData data,
                   Function<? super Map<MappedKey<?>, Object>, ? extends MappedData> constructor,
                   Map<MappedKey<?>, Codec<Object>> codecMap) {
        this.codec = codec;
        this.data = data;
        this.constructor = constructor;
        this.codecMap = codecMap;
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
        return data.getData(key);
    }

    /**
     * 把neoData的数据添加到或覆盖原始data
     * @param neoData 新的数据
     */
    public void updateData(MappedData neoData){
        for(Map.Entry<MappedKey<?>, Object> entry : neoData.data.entrySet()){
            data.put(entry.getKey(), entry.getValue());
            entry.getKey().onReload(entry.getValue());
        }
    }

    /**
     * 获取默认的MappedData，用于一键数据生成默认的数据
     */
    public MappedData getDefaultValue(){
        return generateDefaultValue(constructor, codecMap.keySet());
    }

    private static MappedData generateDefaultValue(Function<? super Map<MappedKey<?>, Object>, ? extends MappedData> constructor,
                                                   Collection<MappedKey<?>> codecSet){
        MappedData data = constructor.apply(new HashMap<>());
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

        Map<MappedKey<?>, Codec<Object>> codecMap;
        Map<String, MappedKey<?>> keyMap;
        public Builder() {
            codecMap = new HashMap<>();
            keyMap = new HashMap<>();
        }

        /**
         * 注册key对应的值的codec
         */
        public <V> MappedKey<V> registerCodec(String key, Codec<V> codec) {
            MappedKey<V> mappedKey = new MappedKey<V>(key);
            if(codecMap.containsKey(mappedKey)){
                throw new IllegalArgumentException("Codec already registered for key: " + key);
            }
            codecMap.put(mappedKey, (Codec<Object>) codec);
            keyMap.put(mappedKey.toString(), mappedKey);
            return mappedKey;
        }

        /**
         * 绑定codec
         * @param constructor MappedData的构造函数
         * @return 绑定后的MappedDataType
         */
        public MappedDataType build(Function<? super Map<MappedKey<?>, Object>, ? extends MappedData> constructor) {
            // 为了获取唯一实例
            Codec<MappedKey<?>> keyCodec = MappedKey.CODEC.xmap(i->keyMap.get(i.toString()), Function.identity());
            MapCodec<? extends MappedData> codec = Codec.dispatchedMap(keyCodec, key->codecMap.get(key))
                    .xmap(constructor, map->map.data).fieldOf("data");
            MappedData data = generateDefaultValue(constructor, codecMap.keySet());
            return new MappedDataType(codec, data, constructor, codecMap);
        }
    }


}
