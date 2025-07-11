package org.confluence.terraentity.entity.summon;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.level.Level;
import org.confluence.terraentity.entity.ai.goal.skill.SkillCooldownManager;
import org.confluence.terraentity.entity.ai.keyframe.Keyframe;
import org.confluence.terraentity.entity.ai.keyframe.animation.KeyframeAnimation;
import org.confluence.terraentity.entity.util.KeyframeAnimationCounter;
import org.confluence.terraentity.init.TEEntityDataSerializers;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class Terraprisma extends SummonSword {


    // 客户端动态颜色
    float colorProgress = 0;
    float sliderProgress = 0;
    int colorFrom = new Color(0x1FE6C0).getRGB();
    int colorTo = new Color(0xC67C28).getRGB();

    public KeyframeAnimationCounter anim_y;
    public KeyframeAnimationCounter anim_z;

    // 客户端动态颜色，貌似没必要同步
    protected static final EntityDataAccessor<Integer> DATA_COLOR = SynchedEntityData.defineId(Terraprisma.class, EntityDataSerializers.INT);

    protected static final EntityDataAccessor<KeyframeAnimationCounter> DATA_KEYFRAME_Y = SynchedEntityData.defineId(Terraprisma.class, TEEntityDataSerializers.KEYFRAME_ANIMATION_SERIALIZER.get());
    protected static final EntityDataAccessor<KeyframeAnimationCounter> DATA_KEYFRAME_Z = SynchedEntityData.defineId(Terraprisma.class, TEEntityDataSerializers.KEYFRAME_ANIMATION_SERIALIZER.get());


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
        this.cooldownManager = new SkillCooldownManager();

        TerraprismaSlashGoal slash = new TerraprismaSlashGoal(this, 1, 10, 120);
        this.cooldownManager.addSkill(slash);
        this.goalSelector.addGoal(0, slash);


        TerraprismaRotateGoal rotate = new TerraprismaRotateGoal(this, 1, 10, 80);
        this.cooldownManager.addSkill(rotate);
        this.goalSelector.addGoal(0, rotate);

        registerCommonSwordGoals();
    }

    protected static class TerraprismaSlashGoal extends SwordSlashGoal{


        protected TerraprismaSlashGoal(SummonSword sword, int skillIndex, int ticks, int skillCooldown) {
            super(sword, skillIndex, ticks, skillCooldown);

        }

        // 由于棱镜拖尾贴合比较好，可以多旋转几圈
        @Override
        protected void triggerZRot(){
            if(sword.getRandom().nextFloat() < 0.8f){
                sword.getEntityData().set(DATA_KEYFRAME_X, new KeyframeAnimationCounter(KeyframeAnimation.builder()
                        .addKeyframe(new Keyframe(20, 90, 0, 0.5f, 1, 0.8f))
                        .addKeyframe(60, 360 * 5)
                        .build()), true);

            }
        }
    }


    static class TerraprismaRotateGoal extends AbstactSkillGoal{

        /**
         * @param skillIndex    技能索引
         * @param ticks         持续时间
         * @param skillCooldown 技能冷却时间
         */
        protected TerraprismaRotateGoal(SummonSword sword, int skillIndex, int ticks, int skillCooldown) {
            super(sword, skillIndex, ticks, skillCooldown);
        }

        @Override
        public void tick(){
            super.tick();
            this.ticks++;
        }

        @Override
        public void start() {
            super.start();
            this.sword.getEntityData().set(DATA_KEYFRAME_Z, new KeyframeAnimationCounter(KeyframeAnimation.builder()
                    .addKeyframe(0,0)
                    .addKeyframe(10,1080)
                    .build()), true);
        }


        @Override
        public void stop(){
            super.stop();
            if(sword.getRandom().nextFloat() < 0.5f) {
                this.sword.getEntityData().set(DATA_KEYFRAME_Y, new KeyframeAnimationCounter(KeyframeAnimation.builder()
                        .addKeyframe(0, 0)
                        .addKeyframe(30, 720)
                        .build()), true);
                this.sword.getEntityData().set(DATA_KEYFRAME_Z, new KeyframeAnimationCounter(KeyframeAnimation.builder()
                        .addKeyframe(0, 0)
                        .addKeyframe(5, 90)
                        .addKeyframe(25, 90)
                        .addKeyframe(30, 0)
                        .build()), true);
            }
        }

    }


    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_COLOR, 0);
        builder.define(DATA_KEYFRAME_Y, new KeyframeAnimationCounter(0, KeyframeAnimation.builder()
                .addKeyframe(0,0)
                .addKeyframe(1,0)
                .build()));
        builder.define(DATA_KEYFRAME_Z, new KeyframeAnimationCounter(0, KeyframeAnimation.builder()
                .addKeyframe(0,0)
                .addKeyframe(1,0)
                .build()));
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        super.onSyncedDataUpdated(key);
        if(key == DATA_COLOR){
            this.rgb = this.entityData.get(DATA_COLOR);
        }else if(key == DATA_KEYFRAME_Y){
            this.anim_y = this.entityData.get(DATA_KEYFRAME_Y);
            this.anim_y.setStartTime(tickCount);
        }else if(key == DATA_KEYFRAME_Z){
            this.anim_z = this.entityData.get(DATA_KEYFRAME_Z);
            this.anim_z.setStartTime(tickCount);
        }
    }

}
