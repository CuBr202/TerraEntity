package org.confluence.terraentity.entity.monster;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.Pose;
import net.neoforged.neoforge.entity.PartEntity;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

public class BaseWarmPart extends PartEntity<BaseWarm> implements GeoEntity {

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


    protected int newPosRotationIncrements;
    protected double interpTargetX;
    protected double interpTargetY;
    protected double interpTargetZ;
    protected double interpTargetYaw;
    protected double interpTargetPitch;
    public float renderYawOffset;
    public float prevRenderYawOffset;

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
        this.yRotO = this.getYRot();
        this.xRotO = this.getXRot();
        this.xOld = this.getX();
        this.yOld = this.getY();
        this.zOld = this.getZ();
        super.tick();
        if (this.newPosRotationIncrements > 0) {
            double d0 = this.getX() + (this.interpTargetX - this.getX()) / (double) this.newPosRotationIncrements;
            double d2 = this.getY() + (this.interpTargetY - this.getY()) / (double) this.newPosRotationIncrements;
            double d4 = this.getZ() + (this.interpTargetZ - this.getZ()) / (double) this.newPosRotationIncrements;
            double d6 = Mth.wrapDegrees(this.interpTargetYaw - (double) this.getYRot());
            this.setYRot((float) ((double) this.getYRot() + d6 / (double) this.newPosRotationIncrements));
            this.setXRot((float) ((double) this.getXRot() + (this.interpTargetPitch - (double) this.getXRot()) / (double) this.newPosRotationIncrements));
            --this.newPosRotationIncrements;
            this.setPos(d0, d2, d4);
            this.setRot(this.getYRot(), this.getXRot());
        }

        while (getYRot() - this.yRotO < -180F) this.yRotO -= 360F;
        while (getYRot() - this.yRotO >= 180F) this.yRotO += 360F;

        while (this.renderYawOffset - this.prevRenderYawOffset < -180F) this.prevRenderYawOffset -= 360F;
        while (this.renderYawOffset - this.prevRenderYawOffset >= 180F) this.prevRenderYawOffset += 360F;

        while (getXRot() - this.xRotO < -180F) this.xRotO -= 360F;
        while (getXRot() - this.xRotO >= 180F) this.xRotO += 360F;

    }

    public boolean hurt(DamageSource source, float amount) {
        this.getParent().setHealth(this.getParent().getHealth() - amount);
        this.getParent().playSound(this.getParent().getHurtSound(source));
        if(this.getParent().getHealth() <= 0) {
            this.getParent().die(source);
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
}
