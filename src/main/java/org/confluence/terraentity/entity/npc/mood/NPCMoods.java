package org.confluence.terraentity.entity.npc.mood;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.EntityType;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.api.event.LoadResourceEvent;
import org.confluence.terraentity.utils.AdapterUtils;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.Reader;
import java.util.*;

/**
 * npc心情加载器
 */
public class NPCMoods {

    public static final String KEY = "npc_mood";
    public static final String FILE_NAME = "mood_infos";

    /**
     * 心情的信息
     * @param moodId 心情id，用来查询对应的信息
     * @param moodInfo 心情信息
     */
    public record MoodInfoData(ResourceLocation moodId, MoodInfo moodInfo){
        public static Codec<MoodInfoData> CODEC = RecordCodecBuilder.create(instance->instance.group(
                ResourceLocation.CODEC.fieldOf("moodId").forGetter(MoodInfoData::moodId),
                MoodInfo.CODEC.fieldOf("moodInfo").forGetter(MoodInfoData::moodInfo)
        ).apply(instance, MoodInfoData::new));

    }

    /**
     * 心情的设置选项，可扩展
     * @param toIntMap
     */
    public record MoodSetting(Map<Mood, Integer> toIntMap){
        public static Codec<MoodSetting> CODEC = RecordCodecBuilder.create(instance->instance.group(
                Codec.unboundedMap(Mood.CODEC, Codec.INT).fieldOf("moodToValue").forGetter(MoodSetting::toIntMap)
        ).apply(instance, MoodSetting::new));

        MoodSetting(int a,int b,int c,int d,int e){
            this(Map.of(Mood.HATE,a, Mood.DISLIKE,b, Mood.NEUTRAL,c, Mood.LIKE,d, Mood.LOVER,e));
        }

        public static MoodSetting of(int hate, int dislike, int neutral, int like, int lover){
            return new MoodSetting(hate, dislike, neutral, like, lover);
        }

        public static MoodSetting DEFAULT = new MoodSetting(-20, -10, 0, 10, 20);

        public EnumMap<Mood, Integer> createEnumMap(){
            return new EnumMap<>(toIntMap);
        }

    }

    /**
     * npc的心情设置和具体信息
     */
    public record EntityMood(Optional<MoodSetting> setting, List<MoodInfoData> moodInfos){
        public static Codec<EntityMood> CODEC = RecordCodecBuilder.create(instance->instance.group(
                MoodSetting.CODEC.optionalFieldOf("setting").forGetter(EntityMood::setting),
                Codec.list(MoodInfoData.CODEC).fieldOf("moodInfos").forGetter(EntityMood::moodInfos)
        ).apply(instance, EntityMood::new));

        /**
         * 安全获取setting，如果没有设置，则返回默认设置
         */
        public MoodSetting getSetting(){
            return setting.orElse(MoodSetting.DEFAULT);
        }

        public static class Builder{
            private MoodSetting setting;
            private final List<MoodInfoData> moodInfos;

            public Builder(){
                this.moodInfos = new ArrayList<>();
            }

            public Builder setSetting(MoodSetting setting){
                this.setting = setting;
                return this;
            }

            public Builder addMoodInfo(ResourceLocation moodId, MoodInfo moodInfo){
                moodInfos.add(new MoodInfoData(moodId, moodInfo));
                return this;
            }

            public EntityMood build(){
                return new EntityMood(Optional.ofNullable(setting), moodInfos);
            }
        }
    }

    // 用于给客户端通信
    public static BiMap<ResourceLocation, MoodInfo> BY_ID = ImmutableBiMap.of();


    // 用于从json读取
    public static Map<EntityType<?>, EntityMood> BY_ENTITY_TYPE = new HashMap<>();

    public static Codec<Map<EntityType<?>, EntityMood>> MAP_LIST_CODEC = Codec.unboundedMap(BuiltInRegistries.ENTITY_TYPE.byNameCodec(), EntityMood.CODEC);


    public static void clear(){
        BY_ENTITY_TYPE.clear();
    }

    public static void loadMoods(ResourceManager manager){
        LoadResourceEvent event = new LoadResourceEvent(LoadResourceEvent.Type.NPC_MOODS);
        AdapterUtils.postEvent(event);
        if(!event.isCanceled()) {
            if(!event.isReplace()) {
                ResourceLocation defaultFile = TerraEntity.space(KEY + "/" + FILE_NAME + ".json");
                readNamesFromJson(manager, defaultFile);
            }
            for(ResourceLocation file : event.getFiles()) {
                readNamesFromJson(manager, file);
            }
        }
    }


    public static void readNamesFromJson(ResourceManager manager, ResourceLocation file) {
        manager.getResource(file).ifPresentOrElse(
                resource -> {

                    try {
                        BY_ENTITY_TYPE = new HashMap<>();
                        Map<ResourceLocation, MoodInfo> tempMap = new HashMap<>();
                        Reader reader = resource.openAsReader();
                        JsonObject jsonobject = GsonHelper.parse(reader);
                        Map<EntityType<?>, EntityMood> map = MAP_LIST_CODEC.decode(JsonOps.INSTANCE, jsonobject).result().get().getFirst();
                        for (Map.Entry<EntityType<?>,EntityMood> entry : map.entrySet()) {
                            EntityType<?> entityType = entry.getKey();
                            EntityMood entityMood = entry.getValue();
                            BY_ENTITY_TYPE.put(entityType, entityMood);
                            List<MoodInfoData> moodInfoDataList = entityMood.moodInfos();
                            for (MoodInfoData moodInfoData : moodInfoDataList) {
                                MoodInfo moodInfo = moodInfoData.moodInfo();
                                tempMap.put(moodInfoData.moodId(), moodInfo);
                            }
                        }
                        BY_ID = ImmutableBiMap.copyOf(tempMap);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                },
                ()->{
                    TerraEntity.LOGGER.warn("No NPC mood data found for NPCs");
                    BY_ID = ImmutableBiMap.of();
                }
        );
//        TerraEntity.LOGGER.info("Loaded {} NPC names", dialog_map.size());
    }


    public static @Nullable MoodInfo getMoodInfo(ResourceLocation id){
        return BY_ID.get(id);
    }

    public static ResourceLocation getId(MoodInfo moodInfo){
        return BY_ID.inverse().get(moodInfo);
    }

    public static void loadFromServer(JsonElement json){
        BY_ENTITY_TYPE.clear();
        MAP_LIST_CODEC.decode(JsonOps.INSTANCE, json).result().ifPresent(js->{
            BY_ENTITY_TYPE.putAll(js.getFirst());
            Map<ResourceLocation, MoodInfo> tempMap = new HashMap<>();
            for (Map.Entry<EntityType<?>,EntityMood> entry : BY_ENTITY_TYPE.entrySet()) {
                EntityType<?> entityType = entry.getKey();
                EntityMood entityMood = entry.getValue();
                List<MoodInfoData> moodInfoDataList = entityMood.moodInfos();
                for (MoodInfoData moodInfoData : moodInfoDataList) {
                    MoodInfo moodInfo = moodInfoData.moodInfo();
                    tempMap.put(moodInfoData.moodId(), moodInfo);
                }
            }
            BY_ID = ImmutableBiMap.copyOf(tempMap);
        });
    }


}
