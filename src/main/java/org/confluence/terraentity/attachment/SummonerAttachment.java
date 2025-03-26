package org.confluence.terraentity.attachment;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.common.util.INBTSerializable;
import net.neoforged.neoforge.network.PacketDistributor;
import org.confluence.terraentity.init.TEAttributes;
import org.confluence.terraentity.network.s2c.SyncSummonPacket;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 召唤师附件
 */
public class SummonerAttachment implements INBTSerializable<CompoundTag> {
    /**
     * 当前仆从栏容量
     */
    int currentCapacity = 1;
    SummonerType type;
    /**
     *  召唤物实体ID列表
     *  <p>只用于服务端，实体进入Level双向绑定</p>
     */
    List<Integer> ids = new CopyOnWriteArrayList<>();

    public SummonerAttachment(SummonerType type) {
        this.type = type;
    }

    public enum SummonerType {
        MINION,
        SENTRY
    }

    /**
     * 默认同步仆从栏数据
     * @param player 玩家
     */
    public void sync(ServerPlayer player) {
        PacketDistributor.sendToPlayer(player, new SyncSummonPacket(currentCapacity, (byte) type.ordinal()));
    }

//    /**
//     * 同步哨兵栏数据
//     * @param player 玩家
//     * @param type 哨兵类型 type = SENTRY
//     */
//    public void sync(ServerPlayer player, SummonerType type) {
//        PacketDistributor.sendToPlayer(player, new SyncSummonPacket(currentCapacity, (byte) type.ordinal()));
//    }

    /**
     * 移除死亡的实体，刷新仆从栏错误数据
     */
    public void refresh(ServerPlayer player) {
        for(int id : this.getIds()){
            Entity entity = player.level().getEntity(id);
            if(entity == null || !entity.isAlive()){
                this.getIds().remove(id);
            }
        }
        this.setCurrentCapacity(getMaxCapacity(player) - this.getIds().size());
    }

    /**
     * 清除所有仆从
     */
    public void clear(ServerPlayer player){
        // 不知道为什么会导致线程安全问题
        for(int id : ids){
            var entity = player.level().getEntity(id);
            if(entity!= null && entity.isAlive()){
                entity.discard();
            }
        }
        this.ids.clear();
        this.setCurrentCapacity(getMaxCapacity(player));
    }

    /**
     * 召唤仆从
     * @param cost 栏位
     * @param id 实体ID
     */
    public void summon(int cost, int id) {
        currentCapacity -= cost;
        if(!ids.contains(id))
            ids.add(id);

    }

    /**
     * 移除仆从
     * @param cost 栏位
     * @param id 实体ID
     */
    public void remove(Player player, int cost, int id) {
        currentCapacity += cost;
        if(ids.contains(id))
            ids.remove(Integer.valueOf(id));
        if(currentCapacity > getMaxCapacity(player)){
            currentCapacity = getMaxCapacity(player);
        }
    }


    public boolean canSummon(int cost) {
        return getCurrentCapacity() >= cost;
    }
    public boolean canRemove(int amount) {
        return true;
    }


    public static int getMaxCapacity(Player player) {
        return (int) player.getAttributeValue(TEAttributes.MINION_CAPACITY);
    }


    public int getCurrentCapacity() {
        return currentCapacity;
    }
    public void setCurrentCapacity(int currentCapacity) {
        this.currentCapacity = currentCapacity;
    }



    public List<Integer> getIds() {
        return ids;
    }
    public void setIds(List<Integer> ids) {
        this.ids = ids;
    }



    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        tag.putInt("currentCapacity", currentCapacity);

//        tag.putIntArray("ids", ids);
        return tag;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag tag) {
        currentCapacity = tag.getInt("currentCapacity");

//        ids = Arrays.stream(tag.getIntArray("ids")).boxed().toList();
    }
}
