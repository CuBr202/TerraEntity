package org.confluence.terraentity.entity.npc.mood;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import org.confluence.terraentity.TerraEntity;

/**
 * npc心情类型绑定的信息
 */
public class MoodInfo {

    public String info;
    public Mood mood;
//    private final DeferredHolder<EntityType<?>, ?> entityType;
    private final EntityType<?> entityType;
    private ResourceLocation id;

    public static Codec<MoodInfo> CODEC = RecordCodecBuilder.create(instance -> instance.group(
//            ResourceLocation.CODEC.fieldOf("id").forGetter(info -> info.id),
            ResourceLocation.CODEC.fieldOf("entity_type").forGetter(info -> BuiltInRegistries.ENTITY_TYPE.getKey(info.entityType)),
            Codec.STRING.fieldOf("info").forGetter(info -> info.info),
            Mood.CODEC.fieldOf("mood").forGetter(info -> info.mood)
    ).apply(instance,(a,b,c)-> new MoodInfo(BuiltInRegistries.ENTITY_TYPE.get(a),b,c)));

    /**
     * @param entityType 测试对象的实体类型
     * @param info 心情描述
     * @param mood 心情类型
     */
//    public MoodInfo(DeferredHolder<EntityType<?>, ?> entityType, String info, Mood mood) {
//        this.info = info;
//        this.mood = mood;
//        this.entityType = entityType;
//    }

    public MoodInfo(EntityType<?> entityType, String info, Mood mood) {
        this.info = info;
        this.mood = mood;
        this.entityType = entityType;
    }

    public static MoodInfo of(EntityType<?> entityType, String info, Mood mood){
        return new MoodInfo(entityType, info, mood);
    }

    public EntityType<?> getEntityType() {
        return entityType;
    }

    public ResourceLocation getId() {
        return id;
    }

    public static final MoodInfo empty = new MoodInfo(null, "", Mood.NEUTRAL);
}
