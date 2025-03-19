package org.confluence.terraentity.entity.boss;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class SkeletronHand extends Skeletron {
    public Skeletron owner;
    public HandSide handSide;
    public static final EntityDataAccessor<Integer> DATA_OWNER = SynchedEntityData.defineId(SkeletronHand.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Boolean> DATA_HAND_SIDE = SynchedEntityData.defineId(SkeletronHand.class, EntityDataSerializers.BOOLEAN);
    public SkeletronHand(EntityType<? extends Monster> entityType, Level level) {
        this(entityType, level, null, HandSide.LEFT);
    }
    public SkeletronHand(EntityType<? extends Monster> entityType, Level level, Skeletron owner, HandSide handSide) {
        super(entityType, level);
        this.handSide = handSide;
        this.owner = owner;
        acceleration=10;
        if (!level.isClientSide) {
            getEntityData().set(DATA_HAND_SIDE, handSide == HandSide.RIGHT);
            if (owner != null) {
                getEntityData().set(DATA_OWNER, owner.getId());
            }
        }
    }

    @Override
    public boolean shouldShowBossBar() {
        return false;
    }

    @Override
    public boolean shouldEscape() {
        return false;
    }

    @Override
    public boolean isMainBody() {
        return false;
    }

    @Override
    protected void registerGoals() {
        targetSelector.addGoal(1, new StandbyGoal());
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        if(owner != null) {
            compound.putUUID("Owner", owner.getUUID());
        }
        compound.putBoolean("HandSide", handSide == HandSide.RIGHT);
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (level() instanceof ServerLevel serverLevel
            && tag.contains("Owner")
            && serverLevel.getEntity(tag.getUUID("Owner")) instanceof Skeletron skeletron) {
            this.owner = skeletron;
            skeletron.attachHand(this);
            if (tag.getBoolean("Hand")) {
                handSide = HandSide.RIGHT;
            }
            getEntityData().set(DATA_OWNER, owner.getId());
        }
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        super.onSyncedDataUpdated(key);
        if (key == DATA_OWNER && level().getEntity(getEntityData().get(DATA_OWNER)) instanceof Skeletron skeletron) {
            owner = skeletron;
        } else if (key == DATA_HAND_SIDE) {
            handSide = getEntityData().get(DATA_HAND_SIDE) ? HandSide.RIGHT : HandSide.LEFT;
        }
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_OWNER, -1);
        builder.define(DATA_HAND_SIDE, false);
    }

    @Override
    public void tick() {
        if (!level().isClientSide && (owner == null || !owner.isAlive()) && !isNoAi()) {
            kill();
            return;
        }
        super.tick();
        yBodyRot = yHeadRot;
    }

    private class StandbyGoal extends Skeletron.FloatGoal {
        private Vec3 targetPos;
        @Override
        public boolean canUse() {
            return true;
        }

        @Override
        public void start() {
        }

        @Override
        public double getDamping() {
            return 10 / Math.max(Math.sqrt(distanceToSqr(getTargetPosition())), 0.1) + 10;
        }

        @Override
        public Vec3 getTargetPosition() {
            if (this.targetPos != null) {
                return this.targetPos;  // 每刻只算一次
            }
            boolean spinning = owner.getEntityData().get(Skeletron.DATA_SPINNING);
            float yRot = owner.yBodyRot * Mth.DEG_TO_RAD;
            Vec3 targetPosition;
            if (spinning) {
                targetPosition = switch (handSide) {
                    case LEFT -> new Vec3(Mth.cos(yRot), 1, Mth.sin(yRot)).scale(4);
                    case RIGHT -> new Vec3(-Mth.cos(yRot), 1, -Mth.sin(yRot)).scale(4);
                };
            } else {
                targetPosition = switch (handSide) {
                    case LEFT -> new Vec3(Mth.cos(yRot), -0.7, Mth.sin(yRot)).scale(5);
                    case RIGHT -> new Vec3(-Mth.cos(yRot), -0.7, -Mth.sin(yRot)).scale(5);
                };
            }
            this.targetPos = targetPosition.add(owner.position());
            return this.targetPos;
        }

        @Override
        public void tick() {
            targetPos = getTargetPosition();
            ((ServerLevel) level()).sendParticles(handSide==HandSide.RIGHT?ParticleTypes.FLAME: ParticleTypes.SOUL_FIRE_FLAME, targetPos.x, targetPos.y, targetPos.z, 10, 0.1, 0.1, 0.1, 0);
//            super.tick();
            setPos(targetPos);
            Vec3 rootPos = getRootPos();
            lookAtPos(rootPos, 90, 90);
            lookControl.setLookAt(rootPos);
            ((ServerLevel) level()).sendParticles(handSide==HandSide.RIGHT?ParticleTypes.FLAME: ParticleTypes.SOUL_FIRE_FLAME, rootPos.x, rootPos.y, rootPos.z, 10, 0.1, 0.1, 0.1, 0);

            targetPos = null;
//            System.out.println(handSide + " " + targetPos.add(owner.position()));
//            setDeltaMovement(targetPos.subtract(position()).normalize().scale(0.3));
        }
    }
    public Vec3 getRootPos() {
        float yRot = owner.yBodyRot * Mth.DEG_TO_RAD;
        return switch (handSide) {
            case LEFT -> new Vec3(Mth.cos(yRot), 0, Mth.sin(yRot)).scale(2).add(owner.position());
            case RIGHT -> new Vec3(-Mth.cos(yRot), 0, -Mth.sin(yRot)).scale(2).add(owner.position());
        };
    }


    public enum HandSide{
        LEFT, RIGHT
    }

}
