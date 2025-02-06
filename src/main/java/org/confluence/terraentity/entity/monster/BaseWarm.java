package org.confluence.terraentity.entity.monster;

import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.entity.PartEntity;
import org.confluence.terraentity.entity.ai.goal.AccelerateOnSeeingGoal;
import org.confluence.terraentity.entity.monster.prefab.AbstractPrefab;
import org.confluence.terraentity.utils.TEUtils;
import org.jetbrains.annotations.Nullable;

/**
 * 不可分裂的蠕虫类
 */
public class BaseWarm extends AbstractMonster {

    private int currentSegmentCount = 5;
    public BaseWarmPart[] bodySegments;

    public BaseWarm(EntityType<? extends Monster> type, Level level) {
        super(type, level, new AbstractPrefab(44,2,1,60,0,0.1f)
                .getPrefab()
                .setNoGravity()
        );
        this._detectInternal = 3;
        this._attackInternal = 3;
        bodySegments = new BaseWarmPart[currentSegmentCount];
        for (int i = 0; i < currentSegmentCount; i++) {
            bodySegments[i] = new BaseWarmPart(this);

        }
        bodySegments[currentSegmentCount - 1].isTail = true;
        this.noPhysics = true;
        this.noCulling = true;
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new WarmAttackGoal(this));
        this.goalSelector.addGoal(5, new WarmWanderGoal(this));

        this.targetSelector.addGoal(1,new AccelerateOnSeeingGoal(this,0.25f));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class,false, LivingEntity::canBeSeenAsEnemy));
    }

    static class WarmAttackGoal extends Goal{
        private boolean randomDirection;
        private final BaseWarm warm;
        WarmAttackGoal(BaseWarm warm){
            this.warm = warm;

        }

        @Override
        public boolean canUse() {
            return warm.getTarget()!=null ;
        }

        public boolean canContinueToUse() {
            return this.canUse() && warm.getTarget().isAlive();
        }

        public void tick() {
            LivingEntity target = warm.getTarget();
            if(target == null) return;
            double distance = warm.distanceToSqr(target);
            double angle = TEUtils.angleBetween(warm.getLookAngle(), target.position().subtract(warm.position()));

            if(distance > 16.0 * 16) {
                // 转向
                warm.lookAt(target, 5,5);
                if(angle > Math.PI / 6.0D) {
                    warm.addDeltaMovement(warm.getLookAngle().scale(0.06f).add(0,0.01f * (randomDirection?1:-1),0));
                    return;
                }
            }
            // 攻击
            if(angle < Math.PI / 2)
                warm.lookAt(target, 2f,2f);
            warm.addDeltaMovement(warm.getLookAngle().scale(0.1f));
            randomDirection = !randomDirection;

        }
    }

    static class WarmWanderGoal extends Goal{
        private Vec3 randomTarget;
        private int tickToChangeTarget;
        private boolean randomDirection;
        private final BaseWarm warm;

        WarmWanderGoal(BaseWarm warm){
            this.warm = warm;
            findWanderTarget();
            randomDirection = warm.random.nextBoolean();
        }

        protected Vec3 findWanderTarget() {
            randomTarget = warm.position().add(Math.random() * 20 - 10, Math.random() * 20 - 8, Math.random() * 20 - 10);
            BlockPos pos = new BlockPos((int) randomTarget.x, (int) randomTarget.y, (int) randomTarget.z);
            int delta = 0;
            while(warm.level().getBlockState(pos).isAir() && pos.getY() > -65) {
                pos = pos.below();
                delta++;
            }
            float f0 = delta + warm.random.nextIntBetweenInclusive(-3,5);
            float f1 = f0 < -65? -130 - f0: f0;
            randomTarget = randomTarget.subtract(0, f1, 0);

            randomDirection = !randomDirection;
            return randomTarget;
        }
        protected Vec3 getWanderTarget() {
            return randomTarget;
        }

        @Override
        public boolean canUse() {
            if(--tickToChangeTarget <= 0){
                tickToChangeTarget = 80 + warm.random.nextIntBetweenInclusive(0, 20);
                findWanderTarget();
            }
            return warm.getTarget()==null ;
        }

        public boolean canContinueToUse() {
            return this.canUse();
        }

        public void tick() {
            double distance = warm.distanceToSqr(randomTarget);

            if(distance > 16.0 * 16) {
                warm.lookAt(EntityAnchorArgument.Anchor.EYES, randomTarget);
//                warm.lookAt(EntityAnchorArgument.Anchor.EYES, randomTarget);
            }
            warm.addDeltaMovement(warm.getLookAngle().scale(0.02f).add(0,0.01f * (randomDirection? 1: -1),0));
        }
    }

    public void tick() {
        super.tick();
        for (int i = 0; i < this.bodySegments.length; i++) {


            Entity leader = i == 0 ? this : this.bodySegments[i - 1];
            BaseWarmPart cur = this.bodySegments[i];
            cur.tick();

            double followX = leader.getX();
            double followY = leader.getY();
            double followZ = leader.getZ();

            // 方向

            Vec3 diff = new Vec3(cur.getX() - followX, cur.getY() - followY, cur.getZ() - followZ);
            diff = diff.normalize();

            // 弹簧恢复力
//            float angle = (((leader.getYRot() + 180) * Mth.PI) / 180.0F);
//            double straightenForce = 0.05D + (1.0D / (i + 1)) * 0.5D;
//            if (this.isDeadOrDying()) straightenForce = 0.0D; //Dead snakes don't move
//            double idealX = -Mth.sin(angle) * straightenForce;
//            double idealZ = Mth.cos(angle) * straightenForce;
//            double groundY = cur.isInWall() ? followY + 2.0F : followY;
//            double idealY = (groundY - followY) * straightenForce;
//            diff = diff.add(idealX, idealY, idealZ).normalize();

            double f = 1.0D;

            double destX = followX + f * diff.x();
            double destY = followY + f * diff.y();
            double destZ = followZ + f * diff.z();

//            cur.setPos(destX, destY, destZ);

            double distance = Mth.sqrt((float) (diff.x() * diff.x() + diff.z() * diff.z()));
            float yaw = (float) (Math.atan2(diff.z(), diff.x()) * 180.0D / Math.PI) + 90.0F;
            float pitch = -(float) (Math.atan2(diff.y(), distance) * 180.0D / Math.PI);

            cur.setYRot(yaw);
            cur.setXRot(pitch);


            cur.setDeltaMovement(destX - cur.getX(), destY - cur.getY(), destZ - cur.getZ());
            cur.moveTo(destX, destY, destZ,yaw,pitch);

            if(attackInternal == 0)
                doCollisionAttack(cur, builder.attackIncrease);
        }
    }


    public boolean isMultipartEntity() {
        return true;
    }
    public boolean hurt(DamageSource source, float amount) {
        return super.hurt(source, amount);
    }
    public @Nullable PartEntity<?>[] getParts() {
        return bodySegments;
    }

    public void die(DamageSource damageSource) {
        this.setDeltaMovement(0,0,0);
        super.die(damageSource);

    }
    public void recreateFromPacket(ClientboundAddEntityPacket packet) {
        super.recreateFromPacket(packet);
        for (int i = 0; i < this.bodySegments.length; i++) {
            this.bodySegments[i].setId(this.getId() + i + 1);
        }
    }

    protected boolean isAlwaysExperienceDropper() {
        return true;
    }
}
