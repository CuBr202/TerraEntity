package org.confluence.terraentity.entity.summon;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class Terraprisma extends SummonSword {


    // 客户端动态颜色
    float colorProgress = 0;
    float sliderProgress = 0;
    int colorFrom = new Color(0x1FE6C0).getRGB();
    int colorTo = new Color(0xC67C28).getRGB();

    // 客户端动态颜色，貌似没必要同步
    protected static final EntityDataAccessor<Integer> DATA_COLOR = SynchedEntityData.defineId(Terraprisma.class, EntityDataSerializers.INT);

    public Terraprisma(EntityType<? extends TamableAnimal> entityType, Level level) {
        super(entityType, level, null, 0xFFFFFF, null, 0.25f);

        this.rgb = getRandomColor();
        this.entityData.set(DATA_COLOR, this.rgb);
    }

    @Override
    public void tick() {
        super.tick();
        if(this.level().isClientSide){
            float d = (getRandom().nextFloat() - 0.5f) * 0.05f;

            colorProgress = Mth.clamp(colorProgress + d + this.sliderProgress, 0, 1);
            if(colorProgress >= 1){
                this.sliderProgress = -0.003f;
            }else if(colorProgress <= 0){
                this.sliderProgress = 0.003f;
            }
            this.rgb = lerpColor(colorFrom, colorTo, colorProgress);

        }
    }

    protected int getRandomColor() {
        this.colorProgress = getRandom().nextFloat();
        int color = lerpColor(colorFrom, colorTo,this.colorProgress );

        return color;
    }

    protected int lerpColor(int from, int to, float t) {
        int r = (int) ((from >> 16 & 255) + ((to >> 16 & 255) - (from >> 16 & 255)) * t);
        int g = (int) ((from >> 8 & 255) + ((to >> 8 & 255) - (from >> 8 & 255)) * t);
        int b = (int) ((from & 255) + ((to & 255) - (from & 255)) * t);
        return (r << 16) + (g << 8) + b;
    }


    @Override
    protected void registerGoals() {
        super.registerGoals();
//        TerraprismaSkillAttackGoal skill1 = new TerraprismaSkillAttackGoal(this, 2, 30, 150);
//        this.cooldownManager.addSkill(skill1);
//        this.goalSelector.addGoal(0, skill1);

    }

    static class TerraprismaSkillAttackGoal extends AbstactSkillGoal{

        /**
         * @param skillIndex    技能索引
         * @param ticks         持续时间
         * @param skillCooldown 技能冷却时间
         */
        protected TerraprismaSkillAttackGoal(SummonSword sword, int skillIndex, int ticks, int skillCooldown) {
            super(sword, skillIndex, ticks, skillCooldown);
        }

        @Override
        public void tick(){
            super.tick();
            this.ticks++;
        }

    }


    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_COLOR, 0);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        super.onSyncedDataUpdated(key);
        if(key == DATA_COLOR){
            this.rgb = this.entityData.get(DATA_COLOR);
        }
    }

}
