package org.confluence.terraentity.entity.npc;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

/**
 * 渔夫：可以设置处理交易任务
 */
public class AngleNPC extends AbstractTerraNPC {

    public AngleNPC(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);


    }

    private static final EntityDataAccessor<ItemStack> DATA_TASK_ITEM_DATA = SynchedEntityData.defineId(AngleNPC.class, EntityDataSerializers.ITEM_STACK);
    private static final EntityDataAccessor<Boolean> DATA_TIME_TO_TRADE_FISH_DATA = SynchedEntityData.defineId(AngleNPC.class, EntityDataSerializers.BOOLEAN);


    /**
     * 渔夫独有的钓鱼系统
     * 获取每天的交易任务物品，仅渔夫可以使用
     * @return 交易任务物品
     */
    public ItemStack getTradeTaskItem(){
        return this.entityData.get(DATA_TASK_ITEM_DATA);
    }

    public void setTradeTaskItem(ItemStack tradeTaskItem){
        this.entityData.set(DATA_TASK_ITEM_DATA, tradeTaskItem);
    }

    @Override
    public boolean readyToTradeFishTask() {
        return this.entityData.get(DATA_TIME_TO_TRADE_FISH_DATA);
    }

    public void onTradeFishTask() {
        this.entityData.set(DATA_TIME_TO_TRADE_FISH_DATA, false);

    }

    /**
     * 每天12点重置交易任务
     */
    public void resetFishTask() {
        this.entityData.set(DATA_TIME_TO_TRADE_FISH_DATA, true);

        syncTradeTasksParams();
        this.getTrades().syncDirtyTrade();
    }

    protected boolean timeToTradeFish(){
        return level().dayTime() % 24000 == 0;
    }

    @Override
    public void onSyncedDataUpdated(@NotNull EntityDataAccessor<?> key) {
        super.onSyncedDataUpdated(key);

    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_TASK_ITEM_DATA, ItemStack.EMPTY);
        builder.define(DATA_TIME_TO_TRADE_FISH_DATA, true);

    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("TradeTaskItem")) {
            this.setTradeTaskItem(ItemStack.parseOptional(this.registryAccess(), tag.getCompound("TradeTaskItem")));
        }


    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        if (!this.getTradeTaskItem().isEmpty()) {
            tag.put("TradeTaskItem", this.getTradeTaskItem().save(this.registryAccess()));
        }
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
