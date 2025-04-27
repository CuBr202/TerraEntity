package org.confluence.terraentity.registries.npc_trade_list.variant;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import org.confluence.terraentity.registries.npc_trade.ITrade;
import org.confluence.terraentity.registries.npc_trade_list.ITradeGenerator;
import org.confluence.terraentity.registries.npc_trade_list.TradeGeneratorProvider;
import org.confluence.terraentity.registries.npc_trade_list.TradeGeneratorProviderTypes;
import org.confluence.terraentity.utils.TEUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 按权重生成交易表,如旅商
 */
public class WeightMapGenerator implements ITradeGenerator {

    record Weight(ITrade trade, int weight){
        private static final Codec<Weight> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                ITrade.TYPED_CODEC.fieldOf("trade").forGetter(Weight::trade),
                Codec.INT.fieldOf("weight").forGetter(Weight::weight)
        ).apply(instance, Weight::new));
    }

    public static final MapCodec<WeightMapGenerator> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Weight.CODEC.listOf().fieldOf("trade_weight_list").forGetter(WeightMapGenerator::getTradeWeightList),
            Codec.INT.fieldOf("count").forGetter(WeightMapGenerator::getCount)
    ).apply(instance, WeightMapGenerator::new));

    List<Weight> tradeWeightList;
    int count;
    Map<ITrade, Integer> tradeWeightMap;

    private Integer getCount() {
        return count;
    }

    private List<Weight> getTradeWeightList() {
        return tradeWeightList;
    }


    public WeightMapGenerator(List<Weight> tradeWeightList, int count) {
        this.tradeWeightList = tradeWeightList;
        this.count = count;
        this.tradeWeightMap = new HashMap<>();
        for(Weight weight : this.tradeWeightList){
            this.tradeWeightMap.put(weight.trade, weight.weight);
        }
    }

    @Override
    public List<ITrade> generateTrades() {
        List<ITrade> trades = new ArrayList<>();
        Map<ITrade, Integer> temp = new HashMap<>(this.tradeWeightMap);
        for(int i = 0; i < count; i++){
            ITrade trade = TEUtils.getRandomByWeightInt(temp);
            trades.add(trade);
            temp.remove(trade);
            if(temp.isEmpty()){
                break;
            }
        }
        return trades;
    }

    @Override
    public TradeGeneratorProvider getCodec() {
        return TradeGeneratorProviderTypes.WEIGHT_MAP.get();
    }

    /**
     * @param count 生成交易项数量
     */
    public static Builder builder(int count){
        return new Builder(count);
    }

    public static class Builder{
        private final List<Weight> tradeWeightList = new ArrayList<>();
        int count;

        /**
         * @param count 生成交易项数量
         */
        public Builder(int count){
            this.count = count;
        }

        public Builder addTrade(ITrade trade, int weight){
            tradeWeightList.add(new Weight(trade, weight));
            return this;
        }

        public WeightMapGenerator build(){
            return new WeightMapGenerator(tradeWeightList, count);
        }
    }
}
