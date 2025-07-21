package org.confluence.terraentity.registries.npc_trade_list;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import org.confluence.terraentity.registries.TERegistries;
import org.confluence.terraentity.registries.npc_trade.ITrade;

import java.util.List;

/**
 * <h1>npc交易列表生成器</h1>
 * <p>用于生成子表项</p>
 */
public interface ITradeGenerator {

    /**
     * <P>初始化可以交易的物品列表</P>
     */
    List<ITrade> generateTrades();

    /**
     * 用于jei自定义配方显示
     */
    List<ITrade> getAllSupportedTrades();

    /**
     * 获取编解码器
     * @return 编解码器
     */
    TradeGeneratorProvider getCodec();


    Codec<ITradeGenerator> TYPED_CODEC = TERegistries.TradeGeneratorProviders.REGISTRY
            .byNameCodec()
            .dispatch(ITradeGenerator::getCodec, TradeGeneratorProvider::codec);

    StreamCodec<ByteBuf, ITradeGenerator> STREAM_CODEC = ByteBufCodecs.fromCodec(TYPED_CODEC);
}
