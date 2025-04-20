package org.confluence.terraentity.registries.npc_trade_task;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.NonNullList;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import org.confluence.terraentity.entity.npc.ITradeHolder;
import org.confluence.terraentity.registries.TERegistries;
import org.confluence.terraentity.registries.npc_trade.ITrade;
import org.confluence.terraentity.registries.npc_trade_task.variant.DynamicAnglerTradeTask;

import javax.annotation.Nullable;
import java.util.List;

/**
 * <h1>npc交易任务接口</h1>
 * <p>用一个交易格按照情况生成不同的交易
 */
public interface ITradeTask {

    /**
     * 获取当前交易格的交易项
     * @param npc npc实体
     * @param index 当前交易格的索引,可能为负数或者超界
     * @return 当前交易格的交易项
     */
    @Nullable
    ITrade getSelected(ITradeHolder npc, int index);

    /**
     * 设置动态设置下一次的交易物品
     * @param npc NPC实体，用来获取交易参数或者根据npc的情况生成下一个交易
     * @param index 当前交易格的索引
     */
    void setNext(ITradeHolder npc, int index);

    /**
     * <P>对交易进行额外的优先判断
     * <P>如：{@link DynamicAnglerTradeTask#canTrade(ITradeHolder, int) 渔夫任务} 要先判断是否准备好
     */
    boolean canTrade(ITradeHolder npc, int index);

    /**
     * 交易任务项完成后自动调用，默认生成下一次的交易
     * @param npc npc实体
     * @param index 当前交易格的索引
     */
    default void afterTrade(ITradeHolder npc, int index) {
        setNext(npc, index);
    }
    /**
     * 获取编解码器
     * @return 编解码器
     */
    TradeTaskProvider getCodec();


    Codec<ITradeTask> TYPED_CODEC = TERegistries.TradeTaskProviders.REGISTRY
            .byNameCodec()
            .dispatch(ITradeTask::getCodec, TradeTaskProvider::codec);

    StreamCodec<ByteBuf, ITradeTask> STREAM_CODEC = ByteBufCodecs.fromCodec(TYPED_CODEC);

    StreamCodec<ByteBuf, List<ITradeTask>> LIST_STREAM_CODEC = STREAM_CODEC.apply(ByteBufCodecs.collection(NonNullList::createWithCapacity));

}
