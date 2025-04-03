package org.confluence.terraentity.entity.rideable;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.players.OldUsersConverter;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.*;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.CommonHooks;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import javax.annotation.Nullable;
import java.util.UUID;

public class AbstractRideableEntity extends Animal implements OwnableEntity, PlayerRideableJumping, GeoEntity {

    private static final EntityDataAccessor<Byte> DATA_ID_FLAGS = SynchedEntityData.defineId(AbstractRideableEntity.class, EntityDataSerializers.BYTE);;

    protected boolean isJumping;
    protected float playerJumpPendingScale;

    protected int gallopSoundCounter;
    @Nullable
    private UUID owner;

    public AbstractRideableEntity(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);

        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.5f);
        this.getAttribute(Attributes.JUMP_STRENGTH).setBaseValue(2.0f);
    }


    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_ID_FLAGS, (byte)0);
    }

    protected boolean getFlag(int flagId) {
        return (this.entityData.get(DATA_ID_FLAGS) & flagId) != 0;
    }

    protected void setFlag(int flagId, boolean value) {
        byte b0 = this.entityData.get(DATA_ID_FLAGS);
        if (value) {
            this.entityData.set(DATA_ID_FLAGS, (byte)(b0 | flagId));
        } else {
            this.entityData.set(DATA_ID_FLAGS, (byte)(b0 & ~flagId));
        }
    }

    @Override
    public boolean shouldBeSaved() {
         return false;
    }

    @Nullable
    public UUID getOwnerUUID() {
        return this.owner;
    }

    public void setOwnerUUID(@Nullable UUID uuid) {
        this.owner = uuid;
    }

    public boolean isJumping() {
        return this.getFlag(1);
//        return this.isJumping;
    }

    public void setIsJumping(boolean jumping) {
        this.setFlag(1, jumping);
        this.isJumping = jumping;
    }

    public InteractionResult mobInteract(Player player, InteractionHand hand) {

        this.doPlayerRide(player);
         return InteractionResult.sidedSuccess(this.level().isClientSide);
    }


    public boolean isPushable() {
        return !this.isVehicle();
    }

    protected void checkFallDamage(double y, boolean onGround, BlockState state, BlockPos pos) {
    }


    public boolean hurt(DamageSource source, float amount) {
        if(getOwner() != null){
            getOwner().hurt(source, amount);
        }
        return source.is(DamageTypes.GENERIC_KILL) && super.hurt(source, amount);
    }

    public static AttributeSupplier.Builder createBaseHorseAttributes() {
        return Mob.createMobAttributes().add(Attributes.JUMP_STRENGTH, 0.7)
                .add(Attributes.MAX_HEALTH, 53.0)
                .add(Attributes.MOVEMENT_SPEED, 0.22499999403953552)
                .add(Attributes.STEP_HEIGHT, 1.0)
                .add(Attributes.SAFE_FALL_DISTANCE, 6.0)
                .add(Attributes.FALL_DAMAGE_MULTIPLIER, 0.5);
    }

    protected float getSoundVolume() {
        return 0.8F;
    }

    @Override
    public boolean isFood(ItemStack itemStack) {
        return false;
    }

    public void doPlayerRide(Player player) {

        if (!this.level().isClientSide) {
            player.setYRot(this.getYRot());
            player.setXRot(this.getXRot());
            player.startRiding(this);
        }

    }

    public boolean isImmobile() {
        return true;
    }

    @Nullable
    public LivingEntity getControllingPassenger() {

        Entity entity = this.getFirstPassenger();
        if (entity instanceof Player) {
            return (Player)entity;
        }

        return super.getControllingPassenger();
    }

    public void aiStep() {

        super.aiStep();

    }

    public void tick() {
        super.tick();
        if(!level().isClientSide){
            if(!this.hasControllingPassenger()){
                discard();
            }
        }
    }

    protected void tickRidden(Player player, Vec3 travelVector) {
        super.tickRidden(player, travelVector);
        player.setJumping(isJumping);
        Vec2 vec2 = this.getRiddenRotation(player);
        this.setRot(vec2.y, vec2.x);
        this.yRotO = this.yBodyRot = this.yHeadRot = this.getYRot();
        if (this.isControlledByLocalInstance()) {
            if (travelVector.z <= 0.0) {
                this.gallopSoundCounter = 0;
            }

            if (this.onGround()) {
                this.setIsJumping(false);
                if (this.playerJumpPendingScale > 0.0F && !this.isJumping()) {
                    this.executeRidersJump(this.playerJumpPendingScale, travelVector);
                }
                this.playerJumpPendingScale = 0.0F;
            }
        }

    }

    protected Vec2 getRiddenRotation(LivingEntity entity) {
        return new Vec2(entity.getXRot() * 0.5F, entity.getYRot());
    }

    /**
     * 处理运动
     * @param player
     * @param travelVector
     * @return
     */
    protected Vec3 getRiddenInput(Player player, Vec3 travelVector) {
        float f = player.xxa * 0.5F;
        float f1 = player.zza;

        if (this.onGround()) {
            return new Vec3(f*0.2f,0,f1*0.2f);
        } else {
            return new Vec3(f, 0.0, f1);
        }
    }

    protected float getRiddenSpeed(Player player) {
        return (float)this.getAttributeValue(Attributes.MOVEMENT_SPEED);
    }

    protected void executeRidersJump(float playerJumpPendingScale, Vec3 travelVector) {
        double d0 = this.getJumpPower(playerJumpPendingScale);
        Vec3 vec3 = this.getDeltaMovement();
        this.setDeltaMovement(vec3.x, d0, vec3.z);
        this.setIsJumping(true);
        this.hasImpulse = true;
        CommonHooks.onLivingJump(this);
//        if (travelVector.z > 0.0) {
//            float f = Mth.sin(this.getYRot() * 0.017453292F);
//            float f1 = Mth.cos(this.getYRot() * 0.017453292F);
//            this.setDeltaMovement(this.getDeltaMovement().add(-0.4F * f * playerJumpPendingScale, 0.0, (double)(0.4F * f1 * playerJumpPendingScale)));
//        }

    }

    protected void playJumpSound() {
        this.playSound(SoundEvents.HORSE_JUMP, 0.4F, 1.0F);
    }

    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        if (this.getOwnerUUID() != null) {
            compound.putUUID("Owner", this.getOwnerUUID());
        }

    }

    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        UUID uuid;
        if (compound.hasUUID("Owner")) {
            uuid = compound.getUUID("Owner");
        } else {
            String s = compound.getString("Owner");
            uuid = OldUsersConverter.convertMobOwnerIfNecessary(this.getServer(), s);
        }

        if (uuid != null) {
            this.setOwnerUUID(uuid);
        }


//        this.syncSaddleToClients();
    }

    public boolean canMate(Animal otherAnimal) {
        return false;
    }

    @Nullable
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob otherParent) {
        return null;
    }

    public void onPlayerJump(int jumpPower) {

        if (jumpPower < 0) {
            jumpPower = 0;
        }

        if (jumpPower >= 90) {
            this.playerJumpPendingScale = 1.0F;
        } else {
            this.playerJumpPendingScale = 0.4F + 0.4F * (float)jumpPower / 90.0F;
        }

    }

    public boolean canJump() {
        return true;
    }

    public void handleStartJump(int jumpPower) {
        this.playJumpSound();
    }

    public void handleStopJump() {
    }
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        super.onSyncedDataUpdated(key);
        if(key == DATA_ID_FLAGS){
            if(!level().isClientSide)
                setFlag(1, getFlag(1));

        }
    }

    protected void positionRider(Entity passenger, Entity.MoveFunction callback) {
        super.positionRider(passenger, callback);
        if (passenger instanceof LivingEntity) {
            ((LivingEntity)passenger).yBodyRot = this.yBodyRot;
        }

    }

    public boolean onClimbable() {
        return false;
    }


    protected Vec3 getPassengerAttachmentPoint(Entity entity, EntityDimensions dimensions, float partialTick) {
        return super.getPassengerAttachmentPoint(entity, dimensions, partialTick).add(0,0.2F,0);
    }


    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

    }

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

}
