package org.confluence.terraentity.entity.monster;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.players.OldUsersConverter;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Difficulty;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.util.AirAndWaterRandomPos;
import net.minecraft.world.entity.ai.util.AirRandomPos;
import net.minecraft.world.entity.ai.util.HoverRandomPos;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.confluence.terraentity.entity.boss.QueenBee;
import org.confluence.terraentity.entity.monster.prefab.AbstractPrefab;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.constant.DefaultAnimations;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.Optional;
import java.util.UUID;

public class Hornet extends AbstractMonster implements FlyingAnimal {
    QueenBee owner;

    public Hornet(EntityType<? extends Monster> type, Level level) {
        super(type, level, new AbstractPrefab(20,1,3,20,0,0.2f)
                .getPrefab()
                .setNoGravity()
        );
        this.moveControl = new FlyingMoveControl(this, 20, true);

    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 2, true));
        this.goalSelector.addGoal(9, new FloatGoal(this));
        this.goalSelector.addGoal(8, new BeeWanderGoal());

        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_OWNERUUID_ID, Optional.empty());

    }
    class BeeWanderGoal extends Goal {
        private static final int WANDER_THRESHOLD = 22;

        BeeWanderGoal() {
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        public boolean canUse() {
            return Hornet.this.navigation.isDone() && Hornet.this.random.nextInt(10) == 0;
        }

        public boolean canContinueToUse() {
            return Hornet.this.navigation.isInProgress();
        }

        public void start() {
            Vec3 vec3 = this.findPos();
            if (vec3 != null) {
                Hornet.this.navigation.moveTo(Hornet.this.navigation.createPath(BlockPos.containing(vec3), 1), 1.0);
            }

        }

        @Nullable
        private Vec3 findPos() {
            Vec3 vec3= Hornet.this.getViewVector(0.0F);

            Vec3 vec32 = HoverRandomPos.getPos(Hornet.this, 8, 7, vec3.x, vec3.z, 1.5707964F, 3, 1);
            return vec32 != null ? vec32 : AirAndWaterRandomPos.getPos(Hornet.this, 8, 4, -2, vec3.x, vec3.z, 1.5707963705062866);
        }
    }

    @Override
    protected PathNavigation createNavigation(Level p_level) {
        FlyingPathNavigation flyingpathnavigation = new FlyingPathNavigation(this, p_level) {
            public boolean isStableDestination(BlockPos p_27947_) {
                return !this.level.getBlockState(p_27947_.below()).isAir();
            }

            public void tick() {
                super.tick();
            }
        };
        flyingpathnavigation.setCanOpenDoors(false);
        flyingpathnavigation.setCanFloat(false);
        flyingpathnavigation.setCanPassDoors(true);
        return flyingpathnavigation;
    }

    @Override
    public void tick() {
        super.tick();
        if (level() instanceof ServerLevel  sl&& this.owner != null && tickCount % 20 == 0) {
            setTarget(this.owner.getTarget());
            if(distanceTo(this.owner) > 30 && sl.getBlockState(owner.blockPosition()).is(Blocks.AIR))
                setPos(this.owner.position());
        }
    }

    @Override
    public boolean doHurtTarget(Entity entity) {
        DamageSource damagesource = this.damageSources().sting(this);
        this.swing(InteractionHand.MAIN_HAND);
        boolean flag = entity.hurt(damagesource, (float)((int)this.getAttributeValue(Attributes.ATTACK_DAMAGE)));
        if (flag) {
            Level var5 = this.level();
            if (var5 instanceof ServerLevel) {
                ServerLevel serverlevel = (ServerLevel)var5;
                EnchantmentHelper.doPostAttackEffects(serverlevel, entity, damagesource);
            }
            if (entity instanceof LivingEntity) {
                LivingEntity livingentity = (LivingEntity)entity;
                livingentity.setStingerCount(livingentity.getStingerCount() + 1);
                int i = 0;
                if (this.level().getDifficulty() == Difficulty.NORMAL) {
                    i = 10;
                } else if (this.level().getDifficulty() == Difficulty.HARD) {
                    i = 18;
                }

                if (i > 0) {
                    livingentity.addEffect(new MobEffectInstance(MobEffects.POISON, i * 20, 0), this);
                }
            }
            this.playSound(SoundEvents.BEE_STING, 1.0F, 1.0F);
        }
        return flag;
    }


    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(DefaultAnimations.genericIdleController(this));
        controllers.add(DefaultAnimations.genericAttackAnimation(this, DefaultAnimations.ATTACK_STRIKE));
    }

    public void setOwner(QueenBee owner) {
        this.owner = owner;
        this.setOwnerUUID(owner.getUUID());
    }

    @Override
    protected void checkFallDamage(double y, boolean onGround, BlockState state, BlockPos pos) {
    }

    void pathfindRandomlyTowards(BlockPos pos) {
        Vec3 vec3 = Vec3.atBottomCenterOf(pos);
        int i = 0;
        BlockPos blockpos = this.blockPosition();
        int j = (int)vec3.y - blockpos.getY();
        if (j > 2) {
            i = 4;
        } else if (j < -2) {
            i = -4;
        }

        int k = 6;
        int l = 8;
        int i1 = blockpos.distManhattan(pos);
        if (i1 < 15) {
            k = i1 / 2;
            l = i1 / 2;
        }

        Vec3 vec31 = AirRandomPos.getPosTowards(this, k, l, i, vec3, 0.3141592741012573);
        if (vec31 != null) {
            this.navigation.setMaxVisitedNodesMultiplier(0.5F);
            this.navigation.moveTo(vec31.x, vec31.y, vec31.z, 1.0);
        }

    }

    @Override
    public boolean isFlying() {
        return true;
    }


    protected static final EntityDataAccessor<Optional<UUID>> DATA_OWNERUUID_ID = SynchedEntityData.defineId(Hornet.class, EntityDataSerializers.OPTIONAL_UUID);;

    @Nullable
    public UUID getOwnerUUID() {
        return (UUID)((Optional)this.entityData.get(DATA_OWNERUUID_ID)).orElse((Object)null);
    }

    public void setOwnerUUID(@Nullable UUID uuid) {
        this.entityData.set(DATA_OWNERUUID_ID, Optional.ofNullable(uuid));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        if (this.getOwnerUUID() != null) {
            compound.putUUID("Owner", this.getOwnerUUID());
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        UUID uuid=null;
        if (compound.hasUUID("Owner")) {
            uuid = compound.getUUID("Owner");
        } else if(getServer()!=null) {
            String s = compound.getString("Owner");
            uuid = OldUsersConverter.convertMobOwnerIfNecessary(this.getServer(), s);
        }
        if(uuid!=null) {
            this.setOwnerUUID(uuid);
            if (level() instanceof ServerLevel sl) {
                Entity owner = sl.getEntity(uuid);
                if (owner instanceof QueenBee bee)
                    setOwner(bee);
            }
        }
    }
}


