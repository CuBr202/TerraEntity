package org.confluence.terraentity.entity.npc;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * npc交易表参数
 * @param params 交易表序列号对应的参数表
 */
public record TradeParams(Map<Integer, Param> params) {

    public static final String KEY = "npc_trade_params";

    public static TradeParams create(){
        return new TradeParams(new HashMap<>());
    }

    // 可以扩展参数数量
    public record Param(int current){

        public static Codec<Param> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.INT.fieldOf("current").forGetter(Param::current)
        ).apply(instance, Param::new));
    }

    public static Codec<TradeParams> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.unboundedMap(Codec.STRING, Param.CODEC).fieldOf("params").forGetter(param->param.params.entrySet().stream()
                    .map(entry->new AbstractMap.SimpleEntry<>(entry.getKey().toString(), entry.getValue()))
                    .collect(Collectors.toMap(AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue)))
    ).apply(instance, (params)->{
                return new TradeParams(params.entrySet()
                        .stream()
                        .map(entry->new AbstractMap.SimpleEntry<>(Integer.parseInt(entry.getKey()), entry.getValue()))
                        .collect(Collectors.toMap(AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue))
                );
            }));

    public static StreamCodec<ByteBuf, TradeParams> STREAM_CODEC = ByteBufCodecs.fromCodec(CODEC);

    public void setParam(int index, int value){
        params.put(index, new Param(value));
    }

    public void increase(int index){
        if(!params.containsKey(index)){
            params.put(index, new Param(0));
        }
        setParam(index, params.get(index).current+1);
    }

    public int getParam(int index){
        if(!params.containsKey(index)){
            return 0;
        }
        return params.get(index).current;
    }


}
