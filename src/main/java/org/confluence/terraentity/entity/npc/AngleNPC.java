package org.confluence.terraentity.entity.npc;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.confluence.terraentity.registries.npc_trade.ITrade;
import org.confluence.terraentity.registries.npc_trade.variant.TradeTask;
import org.confluence.terraentity.registries.npc_trade_task.variant.DynamicAnglerTradeTask;
import org.jetbrains.annotations.NotNull;

/**
 * 渔夫：可以设置处理交易任务
 */
public class AngleNPC extends AbstractTerraNPC {

    public AngleNPC(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);

    }

    private static final EntityDataAccessor<Boolean> DATA_WAKE_UP = SynchedEntityData.defineId(AngleNPC.class, EntityDataSerializers.BOOLEAN);

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

        if(!this.isWakeUp()){
            this.brain = this.brain.copyWithoutBehaviors();
            this.refreshDimensions();
        }
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        super.onSyncedDataUpdated(key);
        if(key == DATA_WAKE_UP){
            if(isWakeUp()){
                this.refreshDimensions();
            }
        }
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if(tag.contains("WakeUp")){
            setWakeUp(tag.getBoolean("WakeUp"));
            if(isWakeUp()){
                this.refreshDimensions();
            }
        }
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("WakeUp", isWakeUp());
    }


    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_WAKE_UP, false);
    }

    public boolean isWakeUp() {
        return this.entityData.get(DATA_WAKE_UP);
    }

    public void setWakeUp(boolean wakeUp) {
        this.entityData.set(DATA_WAKE_UP, wakeUp);
    }

    public boolean isLieDown(){
        return !isWakeUp();
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

    @Override
    protected @NotNull InteractionResult mobInteract(@NotNull Player player, @NotNull InteractionHand hand) {
        if(!isWakeUp() && player.level() instanceof ServerLevel serverLevel){
            setWakeUp(true);
            this.refreshBrain(serverLevel);
            this.refreshDimensions();
            initName();
            return InteractionResult.CONSUME;
        }
        return super.mobInteract(player, hand);
    }

    @Override
    protected EntityDimensions getDefaultDimensions(Pose pose) {
        if(!this.isWakeUp()) {
            return super.getDefaultDimensions(pose).scale(2F, 0.5f);
        }
        return super.getDefaultDimensions(pose);
    }

    protected boolean shouldInitName(){
        return false;
    }

}

