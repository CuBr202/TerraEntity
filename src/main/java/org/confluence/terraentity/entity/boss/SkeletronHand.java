package org.confluence.terraentity.entity.boss;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;

public class SkeletronHand extends Skeletron {
    public Skeletron owner;
    public HandSide handSide;
    protected final int slapInterval;
    protected final double slapSpeed;
    protected int slapTick;

    public static final EntityDataAccessor<Optional<UUID>> DATA_OWNER = SynchedEntityData.defineId(SkeletronHand.class, EntityDataSerializers.OPTIONAL_UUID);
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
                getEntityData().set(DATA_OWNER, Optional.of(owner.getUUID()));
            }
        }
        slapInterval = (expert ? 37 : 57)+level.random.nextInt(6);
        slapSpeed = expert ? 0.8 : 0.6;
        slapTick = slapInterval;

        // 防止手卡位置导致动不了
        this.noPhysics = true;
        // 防止超出包围盒不渲染
        this.noCulling = true;
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
        targetSelector.addGoal(2, new StandbyGoal());
        targetSelector.addGoal(1, new SlapGoal());
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
        if (level() instanceof ServerLevel) {
            handSide = tag.getBoolean("HandSide") ? HandSide.RIGHT : HandSide.LEFT;
            if (tag.contains("Owner")) {
                getEntityData().set(DATA_OWNER, Optional.of(tag.getUUID("Owner")));
            }
        }
    }

    public void initOwner() {
        if(owner == null && getEntityData().get(DATA_OWNER).isPresent()) {
            owner = (Skeletron) level().getEntities().get(getEntityData().get(DATA_OWNER).get());
        }
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        super.onSyncedDataUpdated(key);
        if (key == DATA_OWNER) {
            initOwner();
        } else if (key == DATA_HAND_SIDE) {
            handSide = getEntityData().get(DATA_HAND_SIDE) ? HandSide.RIGHT : HandSide.LEFT;
        }
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_OWNER, Optional.empty());
        builder.define(DATA_HAND_SIDE, false);
    }

    @Override
    public void tick() {
        if (!level().isClientSide) {
            if (tickCount == 1) {
                initOwner();
                if (owner != null) {
                    owner.attachHand(this);
                }
            }
            if ((owner == null || !owner.isAlive()) && !isNoAi()) {
                discard();
                return;
            }
        }
        super.tick();
        yBodyRot = yHeadRot;
    }

    public class StandbyGoal extends Skeletron.FloatGoal {
        private Vec3 targetPos;
        @Override
        public boolean canUse() {
            return !(owner.getTarget() != null && slapTick >= slapInterval && !owner.getEntityData().get(Skeletron.DATA_SPINNING));
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
                    case LEFT -> new Vec3(Mth.cos(yRot), 0.8, Mth.sin(yRot)).scale(5);
                    case RIGHT -> new Vec3(-Mth.cos(yRot), 0.8, -Mth.sin(yRot)).scale(5);
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
            slapTick++;
//            System.out.print(slapTick+"\r");
            targetPos = getTargetPosition();
//            ((ServerLevel) level()).sendParticles(handSide==HandSide.RIGHT?ParticleTypes.FLAME: ParticleTypes.SOUL_FIRE_FLAME, targetPos.x, targetPos.y, targetPos.z, 10, 0.1, 0.1, 0.1, 0);
            super.tick();
            Vec3 rootPos = getRootPos();
            lookAtPos(rootPos, 90, 90);
            lookControl.setLookAt(rootPos);
//            ((ServerLevel) level()).sendParticles(handSide==HandSide.RIGHT?ParticleTypes.FLAME: ParticleTypes.SOUL_FIRE_FLAME, rootPos.x, rootPos.y, rootPos.z, 10, 0.1, 0.1, 0.1, 0);
            targetPos = null;
        }
    }

    public class SlapGoal extends Goal{
        int phase = 0;  // 0预备 1扇 2结束
        Vec3 startPos;
        Vec3 endPos;

        @Override
        public boolean canUse() {
            return owner.getTarget() != null && slapTick >= slapInterval && !owner.getEntityData().get(Skeletron.DATA_SPINNING);
        }

        @Override
        public boolean canContinueToUse() {
            return phase != 2;
        }

        @Override
        public void start() {
            startPos = position().subtract(owner.getTarget().position()).normalize().scale(6).add(position());
        }

        @Override
        public void stop() {
            slapTick = 0;
            phase = 0;
        }

        @Override
        public void tick() {
            if (phase == 0) {
//                ((ServerLevel) level()).sendParticles(ParticleTypes.FLAME, startPos.x, startPos.y, startPos.z, 10, 0.1, 0.1, 0.1, 0);
                if (distanceToSqr(startPos) > 1) {
                    setDeltaMovement(startPos.subtract(position()).normalize().scale(slapSpeed));
                }else{
                    endPos = owner.getTarget().position().subtract(position()).normalize().scale(4).add(owner.getTarget().position());
                    phase = 1;
                }
            } else if (phase == 1) {
//                ((ServerLevel) level()).sendParticles(ParticleTypes.SOUL_FIRE_FLAME, endPos.x, endPos.y, endPos.z, 10, 0.1, 0.1, 0.1, 0);
                if (distanceToSqr(endPos) > 1) {
                    setDeltaMovement(endPos.subtract(position()).normalize().scale(slapSpeed));
                }else{
                    phase = 2;
                }
            }
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
