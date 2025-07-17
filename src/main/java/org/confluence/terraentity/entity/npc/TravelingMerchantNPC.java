package org.confluence.terraentity.entity.npc;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class TravelingMerchantNPC extends AbstractTerraNPC {
    private long spawnDayTime;

    public TravelingMerchantNPC(EntityType<? extends AbstractTerraNPC> entityType, Level level) {
        super(entityType, level);
        if (!level.isClientSide) {
            this.spawnDayTime = level.getDayTime();
        }
    }

    @Override
    protected void customServerAiStep() {
        long dayTime = level().getDayTime();
        if (dayTime < spawnDayTime || dayTime % 24000 >= 12000) {
            // confluence mixin here
            discard();
        }
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.spawnDayTime = tag.getLong("SpawnDayTime");
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putLong("SpawnDayTime", spawnDayTime);
    }
}
