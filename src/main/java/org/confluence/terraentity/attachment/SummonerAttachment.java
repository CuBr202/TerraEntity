package org.confluence.terraentity.attachment;

import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.network.NetworkDirection;
import org.confluence.terraentity.init.TEAttributes;
import org.confluence.terraentity.network.NetworkHandler;
import org.confluence.terraentity.network.s2c.SyncSummonPacket;
import org.confluence.terraentity.registries.TERegistries;
import org.confluence.terraentity.registries.chester.ChesterConditionalType;
import org.confluence.terraentity.registries.chester.ChesterConditionalTypes;

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

    /**
     *  召唤物实体ID列表
     *  <p>只用于服务端，实体进入Level双向绑定</p>
     */
    List<Integer> ids = new CopyOnWriteArrayList<>();

    public Set<Integer> prismaIDs = new HashSet<>(); // 存储棱镜的序列，以确定棱镜的位置

    /**
     * 记录切斯特打开全局存储的类型的索引
     */
    public int chestType = 0;
    /**
     * 记录切斯特绑定块的位置索引
     */
    public int chestTypeAdditional;
    /**
     * 存储切斯特绑定块
     */
    public Map<Key, ChesterConditionalType> boundBlocks = new HashMap<>();

    public int beeFlyTick; // 蜜蜂坐骑飞行时间
    public SummonerAttachment() {}


    public boolean canBind(Key pos, Player player){
        return boundBlocks.isEmpty();
    }

    public record Key(BlockPos pos, ResourceKey<Level> levelId){
        public static final Codec<Key> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
                BlockPos.CODEC.fieldOf("pos").forGetter(Key::pos),
                Level.RESOURCE_KEY_CODEC.fieldOf("levelId").forGetter(Key::levelId)
        ).apply(instance, Key::new));

        @Override
        public boolean equals(Object o) {
            if(this == o){
                return true;
            }else if(o == null || getClass()!= o.getClass()){
                return false;
            }else{
                Key key = (Key) o;
                return pos.equals(key.pos) && levelId.equals(key.levelId);
            }
        }
    }

    record BandedBlockEntry (Key pos, ChesterConditionalType type){
        public static final Codec<BandedBlockEntry> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
                Key.CODEC.fieldOf("pos").forGetter(BandedBlockEntry::pos),
                ChesterConditionalTypes.REGISTRY.get().getCodec().fieldOf("type").forGetter(BandedBlockEntry::type)
        ).apply(instance, BandedBlockEntry::new));
    }

    public static final Codec<Map<Key, ChesterConditionalType>> bandedBlocksCodec = BandedBlockEntry.CODEC.listOf().xmap(ins->{
        Map<Key, ChesterConditionalType> map = new HashMap<>();
        for(BandedBlockEntry entry : ins){
            map.put(entry.pos(), entry.type());
        }
        return map;
    }, outs->{
        List<BandedBlockEntry> list = new ArrayList<>();
        for(Map.Entry<Key, ChesterConditionalType> entry : outs.entrySet()){
            list.add(new BandedBlockEntry(entry.getKey(), entry.getValue()));
        }
        return list;
    });


    public void sync(ServerPlayer player) {
        NetworkHandler.CHANNEL.sendTo(new SyncSummonPacket(currentCapacity), player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
    }

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

    public void removeLast(Player player, int cost){
        var entity = player.level().getEntity(ids.get(ids.size() - 1));
        if(entity!= null && entity.isAlive()){
            entity.discard();
        }
    }


    public boolean canSummon(int cost) {
        return getCurrentCapacity() >= cost;
    }
    public boolean canRemove(int amount) {
        return true;
    }


    public static int getMaxCapacity(Player player) {
        return (int) player.getAttributeValue(TEAttributes.MINION_CAPACITY.get());
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
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("currentCapacity", currentCapacity);
        tag.put("container", JsonOps.INSTANCE.convertTo(NbtOps.INSTANCE, bandedBlocksCodec.encodeStart(JsonOps.INSTANCE, boundBlocks).result().get()));
//        tag.putIntArray("ids", ids);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
        currentCapacity = tag.getInt("currentCapacity");
        if(tag.contains("container")) {
            this.boundBlocks = bandedBlocksCodec.decode(NbtOps.INSTANCE, tag.get("container")).result().get().getFirst();
        }
//        ids = Arrays.stream(tag.getIntArray("ids")).boxed().toList();
    }
}
