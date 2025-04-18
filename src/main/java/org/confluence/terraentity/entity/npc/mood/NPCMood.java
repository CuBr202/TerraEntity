package org.confluence.terraentity.entity.npc.mood;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import org.confluence.terraentity.entity.npc.AbstractTerraNPC;
import org.confluence.terraentity.entity.npc.NPCMoods;

import java.util.*;

/**
 * 心情系统:数值越大，心情越好，100为正常
 */
public class NPCMood {

    public static final String KEY = "npc_mood";

    public static Codec<NPCMood> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("value").forGetter(i->i.value),
            ResourceLocation.CODEC.listOf().fieldOf("moodIdList").forGetter(i->i.moodInfoList)
    ).apply(instance, (a,b)->{
        NPCMood mood = new NPCMood();
        mood.value = a;
        mood.moodInfoList = b;
        return mood;
    }));
    public static final StreamCodec<ByteBuf, NPCMood> STREAM_CODEC = ByteBufCodecs.fromCodec(CODEC);



    /**
     * 对应影响心情的NPC数量
     */
    protected int[] moodCounts = {0, 0, 0, 0, 0};

    /**
     * 当前心情值
     */
    private int value = 100;

    /**
     * 心情列表
     */
    List<ResourceLocation> moodInfoList = new ArrayList<>();

    /**
     * 对每种npc的心情评估
     */
    Map<EntityType<?>, MoodInfo> moodInfoMap = new HashMap<>();

    /**
     * 将心情值转换为数值
     */
    private EnumMap<Mood,Integer> getMoodValue = new EnumMap<>(Mood.class);

    public NPCMood(){
        getMoodValue.put(Mood.HATE, -20);
        getMoodValue.put(Mood.DISLIKE, -10);
        getMoodValue.put(Mood.NEUTRAL, 0);
        getMoodValue.put(Mood.LIKE, 10);
        getMoodValue.put(Mood.LOVER, 20);
    }

    public int getValue(){
        return Math.max(value, 50);
    }

    /**
     * 获取心情实例表
     */
    public List<ResourceLocation> getMoodInfoList(){
        return moodInfoList;
    }

    /**
     * 复制需要同步的信息
     */
    public void copyFrom(NPCMood mood) {
        this.value = mood.value;
        this.moodInfoList = new ArrayList<>(mood.moodInfoList);
    }

    public MoodInfo test(AbstractTerraNPC entity){
        if(moodInfoMap.containsKey(entity.getType())){
            return moodInfoMap.get(entity.getType());
        }
        return MoodInfo.empty;
    }

    /**
     * 添加心情测试实例
     */
    public void addMoodInfo(MoodInfo moodInfo){
        moodInfoMap.put(moodInfo.entityType(), moodInfo);
    }

    /**
     * 获取心情值转换值
     */
    public int getValue(Mood mood){
        return getMoodValue.get(mood);
    }

    /**
     * 设置心情值转换表
     */
    public void setMoodValueTable(EnumMap<Mood,Integer> moodValue){
        this.getMoodValue = moodValue;
    }

    /**
     * 重新计算心情值
     */
    public int evaluate(List<AbstractTerraNPC> entities){
        int[] counts = {0, 0, 0, 0, 0};
        moodInfoList.clear();
        for(AbstractTerraNPC npc : entities){
            MoodInfo moodInfo = test(npc);
            if(moodInfo != MoodInfo.empty) {
                counts[moodInfo.mood().ordinal()]++;
                ResourceLocation location = NPCMoods.getId(moodInfo);
                if(!moodInfoList.contains(location)) {
                    moodInfoList.add(location);
                }
            }
        }
        this.moodCounts = counts;
        value = 100;
        for(int i = 0; i < moodCounts.length; i++){
            value += getValue(Mood.values()[i]) * moodCounts[i];
        }
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        // 由于mood始终是同一个对象，所以要重写
        if(obj instanceof NPCMood other){
            if(value != other.value || moodInfoList.size() != other.moodInfoList.size()){
                return false;
            }
            for(int i=0;i<moodInfoList.size();i++){
                if(!moodInfoList.get(i).equals( other.moodInfoList.get(i))){
                    return false;
                }
            }
            return true;
        }
        return false;
    }

}
