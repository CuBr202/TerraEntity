package org.confluence.terraentity.entity.npc.trade;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import org.confluence.terraentity.entity.npc.mood.NPCMood;
import org.confluence.terraentity.init.TEEntityDataSerializers;
import org.confluence.terraentity.registries.npc_trade.ITrade;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

/**
 * <p>持有交易列表的接口，便于扩展方块实体的交易</p>
 * <p>对于不用使用task的交易，只需要重写{@link ITradeHolder#getTradeManager() 获取交易管理器}</p>
 * <p>对于使用金钱交易，可以重写{@link ITradeHolder#getMood() 获取心情}，在Confluence中使用</p>
 * <p>对于使用task的交易，都需要重写{@link ITradeHolder#getTradeParams() 获取参数}，{@link ITradeHolder#syncTradeTasksParams() 同步task参数}来更新任务参数</p>
 * <p>对于使用动态task的交易，需要重写{@link ITradeHolder#syncNpcTrade(int index) 局部更新NPC交易列表}来同步动态的交易项</p>
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
     * 获取npc心情，只对Confluence的钱币交易生效，返回空时不会影响钱币打折
     */
    @Nullable NPCMood getMood();


    /**
     * 以下三个方法，实体不用重写
     */
    RandomSource getRandom();
    Level level();
    BlockPos blockPos();

    /**
     * 获取所有交易列表
     */
    default List<ITrade> trades(){
        if(getTradeManager() == null) return null;
        return getTradeManager().trades();
    }

//    /**
//     * 获取可用的交易列表
//     */
//    default List<ITrade> availableTrades(){
//        if(getTradeManager() == null) return null;
//        return getTradeManager().availableTrades();
//    }

    /**
     * <P>需要使用task时重写</P>
     * <p>获取交易参数列表</p>
     * 当使用<b>task</b>时使用
     */
    @Nullable TradeParams getTradeParams();


    /**
     * <p>需要使用task时重写</p>
     * <p>同步{@link TradeParams 交易参数列表}</p>
     * <p>实体使用{@link TEEntityDataSerializers#NPC_TRADE_PARAMS_SERIALIZER EntityData}同步</p>
     */
    void syncTradeTasksParams();

    /**
     * <p>需要<b> 动态task </b>时重写</p>
     * <p>局部更新NPC交易列表</p>
     * <p>实体使用{@link org.confluence.terraentity.network.s2c.UpdateNPCTradePacket 网络包}更新交易表</p>
     * @param index 交易索引
     */
    void syncNpcTrade(int index);


    default Optional<TradeParams.Param> getTradeParam(int key){
        return Optional.ofNullable(getTradeParams()).isPresent()?
                Optional.ofNullable(getTradeParams().params().get(key)) :
                Optional.empty();
    }
}
