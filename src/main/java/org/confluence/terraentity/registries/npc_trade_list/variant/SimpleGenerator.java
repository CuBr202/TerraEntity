package org.confluence.terraentity.registries.npc_trade_list.variant;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import org.confluence.terraentity.api.trade.ITrade;
import org.confluence.terraentity.api.trade.ITradeGenerator;
import org.confluence.terraentity.registries.npc_trade_list.TradeGeneratorProvider;
import org.confluence.terraentity.registries.npc_trade_list.TradeGeneratorProviderTypes;

import java.util.List;

/**
 * 简单的交易表，不做任何处理
 */
public class SimpleGenerator implements ITradeGenerator {

    public static final MapCodec<SimpleGenerator> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ITrade.TYPED_CODEC.listOf().fieldOf("trades").forGetter(SimpleGenerator::generateTrades)
    ).apply(instance, SimpleGenerator::new));

    private final List<ITrade> trades;
    public SimpleGenerator(List<ITrade> trades) {
        this.trades = trades;
    }


    @Override
    public List<ITrade> generateTrades() {
        return trades;
    }

    @Override
    public List<ITrade> getAllSupportedTrades() {
        return trades.stream().flatMap(t->t.getAllSupportedTrades().stream()).toList();
    }

    @Override
    public TradeGeneratorProvider getCodec() {
        return TradeGeneratorProviderTypes.SIMPLE_LIST.get();
    }
}
