package org.confluence.terraentity.entity.npc.mood;

import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.registries.DeferredHolder;

/**
 * npc心情类型绑定的信息
 */
public class MoodInfo {

    public String info;
    public Mood mood;
    private final DeferredHolder<EntityType<?>, ?> entityType;

    /**
     * @param entityType 测试对象的实体类型
     * @param info 心情描述
     * @param mood 心情类型
     */
    public MoodInfo(DeferredHolder<EntityType<?>, ?> entityType, String info, Mood mood) {
        this.info = info;
        this.mood = mood;
        this.entityType = entityType;
    }

    public EntityType<?> getEntityType() {
        return entityType.get();
    }

    public static final MoodInfo empty = new MoodInfo(null, "", Mood.NEUTRAL);
}
