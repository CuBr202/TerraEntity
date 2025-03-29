package org.confluence.terraentity.entity.monster;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.Pose;
import net.neoforged.neoforge.entity.PartEntity;
import org.confluence.terraentity.entity.ai.ICollisionAttackEntity;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

public class BaseWarmPart extends PartEntity<BaseWarm> implements GeoEntity, ICollisionAttackEntity<BaseWarmPart> {

    private final EntityDimensions size;
    public boolean isTail = false;

    public BaseWarmPart(BaseWarm parent) {
        super(parent);
        this.size = this.getParent().getDimensions(Pose.STANDING);

        this.setBoundingBox(parent.getBoundingBox());
    }
    public final void updateLastPos() {
        this.moveTo(this.getX(), this.getY(), this.getZ());
        this.yRotO = this.getYRot();
        this.xRotO = this.getXRot();
        this.tickCount++;
    }

    public double xxo;
    public double yyo;
    public double zzo;

    public int deathTime;
    public int hurtTime;

    @Override
    public void tick() {
        updateLastPos();
        this.xxo = this.getX();
        this.yyo = this.getY();
        this.zzo = this.getZ();
        this.deathTime = this.getParent().deathTime;
        this.hurtTime = Math.max(0, this.hurtTime - 1);
        super.tick();
    }

    public boolean hurt(DamageSource source, float amount) {
        this.getParent().setHealth(this.getParent().getHealth() - amount);
        SoundEvent hurtSound = this.getParent().getHurtSound(source);
        if(hurtSound!=null) {
            this.getParent().playSound(hurtSound);
        }
        if(this.getParent().getHealth() <= 0) {
            return false;
//            this.getParent().die(source);

        }

        this.hurtTime = 10;
        return true;
    }

    public boolean isPickable() {
        return !isRemoved();
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {

    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag) {

    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag) {

    }

    public boolean is(Entity entity) {
        return this == entity || this.getParent() == entity;
    }


    public EntityDimensions getDimensions(Pose pose) {
        return this.size;
    }

    public boolean shouldBeSaved() {
        return false;
    }

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    CollisionProperties collisionProperties = new CollisionProperties(10,20,0);
    @Override
    public CollisionProperties getCollisionProperties() {
        return collisionProperties;
    }

    @Override
    public boolean shouldDoCollision() {
        return getParent().shouldDoCollision();
    }
}
