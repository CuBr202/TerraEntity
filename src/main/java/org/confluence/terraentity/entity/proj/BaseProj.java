package org.confluence.terraentity.entity.proj;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.RegistryObject;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.entity.summon.ISummonMob;
import org.confluence.terraentity.registries.generation.IGeneration;
import org.confluence.terraentity.registries.track.ITrackType;
import org.confluence.terraentity.utils.TEUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;


public abstract class BaseProj<T extends BaseProj<T>> extends AbstractHurtingProjectile {
    public float damage = 1;
    private List<Integer> hitList = new ArrayList<>();
    public int penetration =1;
    protected List<MobEffectInstance> effects = new ArrayList<>();
    public ResourceLocation texture = TerraEntity.space("textures/entity/projectile/default.png");
    protected RegistryObject<SoundEvent> hitSound;
    public Consumer<BaseProj> clientTickCallback;
    public ITrackType trackType;
    public IGeneration generation;

    public BaseProj(EntityType<? extends AbstractHurtingProjectile> pEntityType, Level pLevel, MobEffectInstance pEffect) {
        super(pEntityType, pLevel);
        if (pEffect != null){
            this.effects.add(pEffect);
        }
    }
    public BaseProj(EntityType<? extends AbstractHurtingProjectile> pEntityType, Level pLevel, List<MobEffectInstance> pEffects) {
        super(pEntityType, pLevel);
        this.effects = pEffects;
    }

    public T setHitSound(RegistryObject<SoundEvent> hitSound){
        this.hitSound = hitSound;
        return (T) this;
    }
    public T setEffect(List<MobEffectInstance> effects) {
        this.effects = effects;
        return (T) this;
    }
    public T addEffect(MobEffectInstance effect) {
        if (effect != null) {
            this.effects.add(effect);
        }
        return (T) this;
    }
    public float getDamage() {return damage;}
    public void addDamage(float damage) {this.damage += damage;}
    public T setDamage(float damage) {
        this.damage = damage;
        return (T) this;
    }
    public T setPenetrate(int penetration){
        this.penetration = penetration;
        return (T) this;
    }
    public T setClientTickCallback(Consumer<BaseProj> clientTickCallback){
        this.clientTickCallback = clientTickCallback;
        return (T) this;
    }
    public T setTexture(ResourceLocation texture){
        this.texture = texture;
        return (T) this;
    }

    public ResourceLocation getTexture(){return texture;}
    public abstract int getLifetime();
    public boolean shouldBeSaved(){
        return false;
    }
    public void doAABBHurt(){
        //包围盒检测造成伤害
        var entities = level().getEntities(this, this.getBoundingBox());
        if(!entities.isEmpty() && penetration > 0){
            for (var e:entities) {
                int id = e.getId();
                if(canHitEntity(e)) {
                    if(e instanceof LivingEntity living) {
                        doHurt(living);
                        doKnockBack(living);
                    }
                }
            }
        }
    }

    protected void doKnockBack(LivingEntity entity) {
        double d1 = Math.max(0.0, 1.0 - entity.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE));
        Vec3 vec3 = this.getDeltaMovement().multiply(1.0, 0.0, 1.0).normalize().scale(((LivingEntity)getOwner()).getAttributeBaseValue(Attributes.ATTACK_KNOCKBACK) * 0.6 * d1);
        if (vec3.lengthSqr() > 0.0) {
            entity.push(vec3.x, 0.1, vec3.z);
        }
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
    }


    @Override
    public void tick() {
        super.tick();
        if(!level().isClientSide){
            if (tickCount > getLifetime()) {
                discard();
                return;
            }
//            doAABBHurt();
        }else if(clientTickCallback!= null){
            clientTickCallback.accept(this);
        }

    }

    //弹幕设置
    @Override//取消射击惯性
    public void shootFromRotation(Entity pShooter, float pX, float pY, float pZ, float pVelocity, float pInaccuracy) {
        float f = -Mth.sin(pY * (float) (Math.PI / 180.0)) * Mth.cos(pX * (float) (Math.PI / 180.0));
        float f1 = -Mth.sin((pX + pZ) * (float) (Math.PI / 180.0));
        float f2 = Mth.cos(pY * (float) (Math.PI / 180.0)) * Mth.cos(pX * (float) (Math.PI / 180.0));
        this.shoot(f, f1, f2, pVelocity, pInaccuracy);
    }
    @Override
    public void onAddedToWorld(){
        super.onAddedToWorld();
        if(!level().isClientSide()){
            if(getOwner()==null){
                discard();
                return;
            }
            this.damage += defaultDamage();
        }
    }
    @Override
    protected void onHitEntity(@NotNull EntityHitResult pResult) {
        Entity hurter = pResult.getEntity();
        if(hurter instanceof LivingEntity living && canHitEntity(living)) {
            doHurt(living);
        }
    }

    public float defaultDamage(){
//        if(getOwner() != null)
//            return (int) ((LivingEntity)getOwner()).getAttribute(Attributes.ATTACK_DAMAGE).getValue();
        return 0;
    }

    protected void doHurt(LivingEntity hurter){
        Entity entity = this.getOwner();
        hitList.add(hurter.getId());
        for (MobEffectInstance effect : effects) {
            hurter.addEffect(effect);
        }
        if(hitSound != null)
            level().playSound(this,this.blockPosition(), hitSound.get(), SoundSource.AMBIENT, 1.0f, 1.0f);

        hurter.hurt(getDamageSource(hurter), damage);

        if(this.level() instanceof ServerLevel serverlevel){
            penetration--;
            if(penetration <= 0) {
                discard();
            }
        }
    }

    public DamageSource getDamageSource(LivingEntity hurter){
        if(getOwner() != null && getOwner() instanceof LivingEntity living){
            return damageSources().mobProjectile(this, living);
        }
        return this.damageSources().generic();
    }



    @Override//设置粒子效果
    protected ParticleOptions getTrailParticle() {
        return super.getTrailParticle();
    }

    @Override
    protected boolean canHitEntity(@NotNull Entity target) {
        // 不能攻击自己和不能被弹幕攻击的实体
        if(target == getOwner() || !target.canBeHitByProjectile()){
            return false;
        }
        // 不能攻击已经被弹幕攻击过的实体
        if(hitList.contains(target.getId()))
            return false;
        // 召唤物不能攻击主人的仆从
        if(!TEUtils.attackTamableTest.test(getOwner(), target)
        ){
            return false;
        }
        // 有主人的弹幕只能攻击主人可以攻击的目标
        if(getOwner()!=null && getOwner() instanceof LivingEntity living && target instanceof LivingEntity tar)
            return living.canAttack(tar);
        return true;
    }

//    @Override//流体阻力
//    protected float getLiquidInertia() {
//        return 1;
//    }

    @Override//火焰效果
    protected boolean shouldBurn() {
        return false;
    }

    @Override
    public boolean isPickable() {
        return false;
    }

    @Override//空气阻力
    protected float getInertia() {
        return 1;
    }
    @Override
    protected void onHitBlock(@NotNull BlockHitResult pResult) {
        super.onHitBlock(pResult);
        if(!this.level().isClientSide()) discard();
    }
}
