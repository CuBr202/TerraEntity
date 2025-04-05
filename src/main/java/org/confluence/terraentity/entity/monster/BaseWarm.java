package org.confluence.terraentity.entity.monster;

import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.neoforged.neoforge.entity.PartEntity;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.entity.ai.goal.AccelerateOnSeeingGoal;
import org.confluence.terraentity.entity.ai.goal.ComeAndBackDashAttackGoal;
import org.confluence.terraentity.entity.ai.goal.RandomWanderGoal;
import org.jetbrains.annotations.Nullable;

/**
 * 不可分裂的蠕虫类
 */
public class BaseWarm extends AbstractMonster {

    private int currentSegmentCount = 12;
    private float segInternal = 1.6f;
    public BaseWarmPart[] bodySegments;

    public BaseWarm(EntityType<? extends Monster> type, Level level, AbstractMonster.Builder builder) {
        super(type, level, builder);
        this.collisionProperties = new CollisionProperties(3,3,0);
        bodySegments = new BaseWarmPart[currentSegmentCount];
        for (int i = 0; i < currentSegmentCount; i++) {
            bodySegments[i] = new BaseWarmPart(this);

        }
        bodySegments[currentSegmentCount - 1].isTail = true;
        this.noPhysics = true;
        this.noCulling = true;
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new ComeAndBackDashAttackGoal(this, 16));
        this.goalSelector.addGoal(5, new RandomWanderGoal(this, 80));

        this.targetSelector.addGoal(1,new AccelerateOnSeeingGoal(this,0.25f));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class,false, LivingEntity::canBeSeenAsEnemy));
    }

    @Override
    public void tick() {
        super.tick();
        if(!isAlive()) {
            this.noPhysics = false;
            addDeltaMovement(new Vec3(0,-0.05f,0));
//            return;
        }
        for (int i = 0; i < this.bodySegments.length; i++) {

            Entity leader = i == 0 ? this : this.bodySegments[i - 1];
            BaseWarmPart cur = this.bodySegments[i];
            cur.tick();

            double followX = leader.getX();
            double followY = leader.getY();
            double followZ = leader.getZ();

            // 方向

            Vec3 diff = new Vec3(cur.getX() - followX, cur.getY() - followY, cur.getZ() - followZ);
            diff = diff.normalize().scale(segInternal);

            // 弹簧恢复力
//            if(!this.isAlive()) {
//                float angle = (((leader.getYRot() + 180) * Mth.PI) / 180.0F);
//                double straightenForce = 0.05D + (1.0D / (i + 1)) * 0.5D;
//                if (this.isDeadOrDying()) straightenForce = 0.0D; //Dead snakes don't move
//                double idealX = -Mth.sin(angle) * straightenForce;
//                double idealZ = Mth.cos(angle) * straightenForce;
//                double groundY = cur.isInWall() ? followY + 2.0F : followY;
//                double idealY = (groundY - followY) * straightenForce;
//                diff = diff.add(idealX, idealY, idealZ).normalize();
//            }
            if(!this.isAlive()){
                float dy = (float) (this.getY() - followY);
                if(dy < 0)
                    diff = diff.add(new Vec3(0,dy * this.deathTime / 100,0 ));
            }

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
            cur.moveTo(destX, destY, destZ, yaw, pitch);

            cur.doCollisionAttack(e->e instanceof LivingEntity living && canAttack(living),
                    e->doHurtTarget(e)
                    );

        }
    }
    @Override
    protected void tickDeath() {
        if(this.onGround()) {
            super.tickDeath();
        }
    }
    @Override
    public boolean isMultipartEntity() {
        return true;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {

        if(this.getHealth() <= 0) return false;
        return super.hurt(source, amount);
    }

    @Override
    public @Nullable PartEntity<?>[] getParts() {
        return bodySegments;
    }

    @Override
    public void die(DamageSource damageSource) {
        this.setDeltaMovement(0,0,0);
        super.die(damageSource);

    }

    @Override
    public void recreateFromPacket(ClientboundAddEntityPacket packet) {
        super.recreateFromPacket(packet);
        for (int i = 0; i < this.bodySegments.length; i++) {
            this.bodySegments[i].setId(this.getId() + i + 1);
        }
    }

    @Override
    protected boolean isAlwaysExperienceDropper() {
        return true;
    }

    @Override
    public boolean isInWall() {
        float f = this.getDefaultDimensions(Pose.STANDING).width() * 0.8F;
        AABB aabb = AABB.ofSize(this.getEyePosition(), (double)f, 1.0E-6, (double)f);
        return BlockPos.betweenClosedStream(aabb).anyMatch((p_201942_) -> {
            BlockState blockstate = this.level().getBlockState(p_201942_);

            return !blockstate.isAir() && blockstate.isSuffocating(this.level(), p_201942_) && Shapes.joinIsNotEmpty(blockstate.getCollisionShape(this.level(), p_201942_).move((double)p_201942_.getX(), (double)p_201942_.getY(), (double)p_201942_.getZ()), Shapes.create(aabb), BooleanOp.AND);
        });
    }
    public boolean isInvulnerableTo(DamageSource source) {
        return super.isInvulnerableTo(source) || source.is(DamageTypes.IN_WALL);
    }

}
