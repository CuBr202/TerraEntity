package org.confluence.terraentity.entity.npc;

import net.minecraft.util.RandomSource;
import org.confluence.terraentity.registries.npc_trade.ITrade;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * 持有交易列表的接口，便于扩展方块实体的交易
 */
public interface ITradeHolder {

    class _util {
        static int selectTradeIndex = 0;
    }

    /**
     * 获取当前客户端菜单选中的交易索引
     * @return 当前客户端菜单选中的交易索引
     */
    static int selectTradeIndex(){
        return _util.selectTradeIndex;
    }

    /**
     * 设置当前客户端菜单选中的交易索引
     * @param index 当前客户端菜单选中的交易索引
     */
    static void setSelectTradeIndex(int index){
        _util.selectTradeIndex = index;
    }
    /**
     * 获取交易管理器
     */
    @Nullable
    NPCTradeManager getTrades();

    /**
     * 获取随机数生成器
     */
    RandomSource getRandom();

    /**
     * 获取交易参数列表
     */
    TradeParams getTradeParams();



    /**
     * 获取交易列表
     */
    @Nullable
    default List<ITrade> trades(){
        if(getTrades() == null) return null;
        return getTrades().trades();
    }

    /**
     * 局部更新NPC交易列表
     * @param index 交易索引
     */
    void syncNpcTrade(int index);

    /**
     * 同步{@link TradeParams 交易参数列表}
     */
    void syncTradeTasksParams();

    /**
     * 渔夫任务，当渔夫交易时调用，用于同步实体readyToTradeFishTask的数据
     */
    default void onTradeFishTask(){
    }

    /**
     * 是否准备好进行渔夫交易，当不是渔夫的时候不应该重写这个方法
     */
    default boolean readyToTradeFishTask(){
        return false;
    }
}
