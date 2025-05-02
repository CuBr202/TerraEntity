package org.confluence.terraentity.entity.npc;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import org.confluence.terraentity.registries.npc_trade.ITrade;
import org.confluence.terraentity.registries.npc_trade.variant.TradeTask;
import org.confluence.terraentity.registries.npc_trade_task.variant.DynamicAnglerTradeTask;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * 渔夫：可以设置处理交易任务
 */
public class AnglerNPC extends AbstractTerraNPC {

    public AnglerNPC(EntityType<? extends AbstractTerraNPC> entityType, Level level) {
        super(entityType, level);

    }

    private static final EntityDataAccessor<Boolean> DATA_WAKE_UP = SynchedEntityData.defineId(AnglerNPC.class, EntityDataSerializers.BOOLEAN);

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
                    if(!Objects.requireNonNull(getTradeParams()).isReady(c)) {
                        d.setNext(this, c);

                        getTradeParams().increaseLevel(c);
                        getTradeParams().setIsReady(c, true);
                    }
                }
            }
            c++;
        }
        syncTradeTasksParams();
        this.getTradeManager().syncDirtyTrade();
    }

    // 渔夫初始化时随机设置交易任务
    protected void onInitTrades(){
        int c = 0;
        for(ITrade trade: trades()){
            if(trade instanceof TradeTask task){
                if(task.task() instanceof DynamicAnglerTradeTask d){
                    // 更新参数
                    d.setNext(this, c);
                    getTradeParams().increaseLevel(c);
                }
            }
            c++;
        }
        syncTradeTasksParams();
    }

    protected boolean timeToTradeFish(){
        return level().dayTime() % 24000 == 0;
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return !isWakeUp();
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
    public void onSyncedDataUpdated(@NotNull EntityDataAccessor<?> key) {
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

    Vec3 dir = Vec3.ZERO;
    Vec3 speed = Vec3.ZERO;
    @Override
    public void tick(){
        super.tick();
        if(!level().isClientSide){
            if(timeToTradeFish()){
                this.resetFishTask();
            }
            if(!this.isWakeUp()){
                if(this.isInWater() ){
                    this.setDeltaMovement(0,0.02f,0);

                }
                if(this.isInWater() || level().getBlockState(this.blockPosition()).is(Blocks.WATER)){
                    if(this.dir == null){
                        this.dir = Vec3.ZERO;
                    }
                    if(this.speed == null){
                        this.speed = Vec3.ZERO;
                    }
                    float f = 0.001f;
                    float maxSpeed = 0.008f;
                    speed = new Vec3(Math.random() * 2 * f - f, 0, Math.random() * 2 * f - f);
                    this.dir = this.dir.add(speed);
                    if(dir.length() > maxSpeed * 1.4F){
                        dir = dir.scale(0.9F);
                    }

                    this.addDeltaMovement(dir);

                }
            }
        }
    }

    @Override
    protected @NotNull InteractionResult mobInteract(@NotNull Player player, @NotNull InteractionHand hand) {
        if(!isWakeUp() && player.level() instanceof ServerLevel serverLevel){
            setWakeUp(true);
            this.refreshBrain(serverLevel);
            this.refreshDimensions();
            // confluence mixin here
            return InteractionResult.CONSUME;
        }
        return super.mobInteract(player, hand);
    }

    @Override
    protected @NotNull EntityDimensions getDefaultDimensions(@NotNull Pose pose) {
        if(!this.isWakeUp()) {
            return super.getDefaultDimensions(pose).scale(2F, 0.5f);
        }
        return super.getDefaultDimensions(pose);
    }

}

