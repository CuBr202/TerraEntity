package org.confluence.terraentity.entity.npc;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.Level;
import org.confluence.terraentity.registries.npc_trade_task.ITradeTask;
import org.jetbrains.annotations.NotNull;

/**
 * 渔夫：可以设置处理交易任务
 */
public class AngleNPC extends AbstractTerraNPC {

    public AngleNPC(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
    }

    private static final EntityDataAccessor<Integer> DATA_DAVE_DATA = SynchedEntityData.defineId( AngleNPC.class, EntityDataSerializers.INT);


    public int getTradeTaskIndex(){
        return this.entityData.get(DATA_DAVE_DATA);
    }

    public int getTradeTaskNext(ITradeTask task){
        return 0;
    }

    public int getTradeTaskCurrent(ITradeTask task){
        return this.entityData.get(DATA_DAVE_DATA);
    }

    public void setTradeTaskIndex(int index){
        this.entityData.set(DATA_DAVE_DATA, index);
    }


    @Override
    public void onSyncedDataUpdated(@NotNull EntityDataAccessor<?> key) {
        super.onSyncedDataUpdated(key);

    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_DAVE_DATA, 0);

    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("TradeTaskIndex")) {
            setTradeTaskIndex( tag.getInt("TradeTaskIndex"));
        }

    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("TradeTaskIndex", getTradeTaskIndex());

    }


    @Override
    public void onAddedToLevel() {
        super.onAddedToLevel();


    }
}
