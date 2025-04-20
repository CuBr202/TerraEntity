package org.confluence.terraentity.entity.npc.trade;

import net.minecraft.util.RandomSource;
import org.confluence.terraentity.registries.npc_trade.ITrade;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

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
    NPCTradeManager getTradeManager();

    /**
     * 获取随机数生成器
     */
    RandomSource getRandom();

    /**
     * 获取交易参数列表
     */
    TradeParams getTradeParams();

    default Optional<TradeParams.Param> getTradeParam(int key){
        return Optional.ofNullable(getTradeParams().params().get(key));
    }


    /**
     * 获取交易列表
     */
    default List<ITrade> trades(){
        if(getTradeManager() == null) return null;
        return getTradeManager().trades();
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


}
