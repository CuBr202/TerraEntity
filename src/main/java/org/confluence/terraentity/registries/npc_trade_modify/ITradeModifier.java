package org.confluence.terraentity.registries.npc_trade_modify;

import com.mojang.serialization.Codec;

import net.minecraft.resources.ResourceLocation;
import org.confluence.terraentity.entity.npc.trade.NPCTradeManager;

import java.util.function.BiConsumer;

/**
 * <h1>npc交易修饰器</h1>
 * <p>增删改单个表项或整个交易表</p>
 */
public interface ITradeModifier extends BiConsumer<NPCTradeManager, ResourceLocation> {

    /**
     * 优先级，越小越优先
     */
    int priority();

    ResourceLocation id();


    /**
     * 获取编解码器
     * @return 编解码器
     */
    TradeModifierProvider getCodec();


    Codec<ITradeModifier> TYPED_CODEC = TradeModifierProviderTypes.REGISTRY.get()
            .getCodec()
            .dispatch(ITradeModifier::getCodec, i->i.codec().get().codec());



    enum OperatorType {
        ADD,
        DEL,
        REPLACE;
        public static final Codec<OperatorType> CODEC = Codec.STRING.xmap(s-> OperatorType.valueOf(s.toUpperCase()), e-> e.name().toLowerCase());
    }
}
