package org.confluence.terraentity.entity.npc;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.Level;
import org.confluence.terraentity.registries.npc_trade.ITrade;
import org.confluence.terraentity.registries.npc_trade.variant.TradeTask;
import org.confluence.terraentity.registries.npc_trade_task.variant.DynamicAnglerTradeTask;

/**
 * 渔夫：可以设置处理交易任务
 */
public class AngleNPC extends AbstractTerraNPC {

    public AngleNPC(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);


    }

    /**
     * 每天12点重置交易任务
     */
    public void resetFishTask() {
//        this.entityData.set(DATA_TIME_TO_TRADE_FISH_DATA, true);
        int c = 0;
        for(ITrade trade: trades()){
            if(trade instanceof TradeTask task){
                if(task.task() instanceof DynamicAnglerTradeTask d){
                    // 更新参数
                    if(!getTradeParams().isReady(c)) {
                        d.setNext(this, c);

                        getTradeParams().increaseLevel(c);
                        getTradeParams().setIsReady(c, true);
                        syncTradeTasksParams();
                    }
                }
            }
            c++;
        }
        syncTradeTasksParams();
        this.getTradeManager().syncDirtyTrade();
    }

    protected boolean timeToTradeFish(){
        return level().dayTime() % 24000 == 0;
    }

    @Override
    public void onAddedToLevel() {
        super.onAddedToLevel();

    }

    @Override
    public void tick(){
        super.tick();
        if(!level().isClientSide){
            if(timeToTradeFish()){
                resetFishTask();
            }
        }
    }

}
