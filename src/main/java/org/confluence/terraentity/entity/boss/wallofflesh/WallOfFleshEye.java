package org.confluence.terraentity.entity.boss.wallofflesh;

import net.minecraft.ChatFormatting;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.confluence.terraentity.entity.proj.TrailProjectile;
import org.confluence.terraentity.entity.util.DifficultSelector;
import org.confluence.terraentity.init.TETags;
import org.confluence.terraentity.utils.TEUtils;
import org.jetbrains.annotations.NotNull;

public class WallOfFleshEye extends WallOfFleshPart implements RangedAttackMob {

    int shootDamage;
    int _shootDelay = 10;
    int _shootInterval = 40;
    final int __shootInterval = _shootInterval;
    int _shootCount = 1;
    int shootDelay;
    int shootCount;

    private static final int summonCDAll = 60;
    private int summonCD = summonCDAll;

    public float calculatedYaw = 0.0f;
    public float calculatedPitch = 0.0f;

    public WallOfFleshEye(WallOfFlesh parentMob, String name, float width, float height) {
        super(parentMob, name, width, height);
        DifficultSelector difficultSelector = parentMob.getDifficultSelector();
        this.shootDamage = difficultSelector.switchBy(8,10,12,15);
        this.shootCount = _shootCount;
        this.shootDelay = _shootDelay;
    }
    
    protected boolean canShoot(Entity target, float range) {
        LivingEntity currentTarget = this.target;
        if (currentTarget == null) return false;

        Vec3 lookAngle = this.getLookAngle().normalize();
        Vec3 toTarget = target.position().subtract(this.position()).normalize();
        double angleDiff = Math.toDegrees(Math.acos(lookAngle.dot(toTarget)));
        return Math.abs(angleDiff) <= Math.toDegrees(range) || 
               this.distanceTo(target) <= 150;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {

    }

    @Override
    public boolean isNoGravity(){ return true; }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        return super.hurt(pSource, pAmount) && parentMob.hurt(this,pSource,pAmount);
    }

    @Override
    public boolean shouldBeSaved(){
        if(this.parentMob != null)return this.parentMob.shouldBeSaved();
        return  false;
    }

    @Override
    public void tickPart(double offsetX, double offsetY, double offsetZ) {
        this.findTarget();
        if (this.parentMob== null || !this.parentMob.isAlive()) return;

        Vec3 forward = this.parentMob.getForward().normalize();
        
        // 检查目标是否为创造模式或观察者模式的玩家
        if(this.target != null && this.target instanceof Player) {
            Player player = (Player) this.target;
            if(player.isCreative() || player.isSpectator()) {
                this.target = null;
            }
        }
        
        // 计算眼睛旋转角度
        if (this.target != null && this.target.isAlive() && !this.target.isRemoved()) {
            WallOfFlesh parentMob = this.parentMob;
            if (parentMob.isAlive()) {
                Vec3 wallForward = parentMob.getForward();
                Vec3 targetPos = this.target.getEyePosition();
                Vec3 entityPos = this.getEyePosition();

                // 计算到目标的方向向量
                Vec3 toTarget = targetPos.subtract(entityPos);

                // 计算水平距离
                double horizontalDistance = Math.sqrt(toTarget.x * toTarget.x + toTarget.z * toTarget.z);

                if (horizontalDistance > 0.001) {
                    // 计算俯仰角（上下看的角度）
                    float pitch = (float) Math.toDegrees(Math.atan2(-toTarget.y, horizontalDistance));
                    pitch = Mth.clamp(pitch, -45.0F, 45.0F);

                    // 计算偏航角（左右看的角度）
                    float yaw = (float) Math.toDegrees(Math.atan2(-toTarget.x, toTarget.z));

                    // 计算墙体的偏航角
                    float wallYaw = (float) Math.toDegrees(Math.atan2(-wallForward.x, wallForward.z));

                    // 计算相对于墙体的角度差
                    float relativeYaw = yaw - wallYaw;
                    relativeYaw = Mth.wrapDegrees(relativeYaw);

                    // 限制头部转动范围
                    relativeYaw = Mth.clamp(relativeYaw, -60.0F, 60.0F);

                    this.calculatedYaw = relativeYaw * 0.017453292F;
                    this.calculatedPitch = pitch * 0.017453292F;
                } else {
                    this.calculatedYaw = 0.0f;
                    this.calculatedPitch = 0.0f;
                }
            } else {
                this.calculatedYaw = 0.0f;
                this.calculatedPitch = 0.0f;
            }
        } else {
            this.calculatedYaw = 0.0f;
            this.calculatedPitch = 0.0f;
        }
        
        if (!this.level().isClientSide) {
            if (--summonCD > 0) return;
            if (canShoot(this.target,0.75F) && this.target.isAlive() && this.stareCount >= 10) {
                this.shoot(this.target);
            }
        }
    }

    public double getEyeY() {
        return this.position().y + this.getBbHeight() / 2;
    }

    private void shoot(LivingEntity target){
        if(--this.shootDelay <= 0){
            --this.shootCount;

            if(this.shootCount <= 0){
                this.shootCount = _shootCount;
                this.shootDelay = _shootInterval + this.getRandom().nextInt(20);

                this.performRangedAttack(target, 1.0f); // 最后一击增加射速
            }else{
                this.shootDelay = _shootDelay;
                this.performRangedAttack(target, 0.5f);
            }
        }
    }

    @Override
    public boolean canUsePortal(boolean allowPassengers) {
        return this.parentMob==null?super.canUsePortal(allowPassengers):this.parentMob.canUsePortal(allowPassengers);
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        if(source.is(DamageTypeTags.IS_FIRE)||source.is(DamageTypeTags.IS_DROWNING)){
            return true;
        }
        return super.isInvulnerableTo(source);
    }

    @Override
    public void performRangedAttack(LivingEntity livingEntity, float v) {
        TrailProjectile proj = new TrailProjectile(this.level(),ChatFormatting.DARK_PURPLE.getColor()) {

            @Override
            protected boolean canHitEntity(@NotNull Entity target) {
                return super.canHitEntity(target) && !target.getType().is(TETags.EntityTypes.FLESH_ALLIANCE);
            }

        };
        proj.setDamage(shootDamage);
        proj.setExistTick(200);
        proj.setOwner(this.parentMob);
        proj.setPos(this.position());
        proj.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST));
        Vec3 dir = this.target.getEyePosition().subtract(this.position());
        /*
            AimUtils.AimHelperOptions aimOptions = new AimUtils.AimHelperOptions()
                    .setProjectileSpeed(0.4)
                    .setProjectileSpeedMulti(1.1)
                    .setProjectileGravity(0)
                    .setTicksTotal(20)
                    .setRandomOffsetRadius(0)
                    .setEpoch(3);
        */
        proj.shoot(dir.x, dir.y, dir.z, 1.5f, 0.015f);
        level().addFreshEntity(proj);
    }

    @Override
    protected void onParentChangeState(int state){
        if(state == 2){
            this._shootInterval = (int) (this.__shootInterval * 0.7f);
            this._shootCount = 3;
            float healthPercent = parentMob.getHealthPercentage();
        }
    }
}