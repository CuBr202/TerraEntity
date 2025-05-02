package org.confluence.terraentity.entity.proj;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import net.minecraftforge.entity.PartEntity;
import org.confluence.terraentity.config.ClientConfig;
import org.confluence.terraentity.data.component.SingleBooleanComponent;
import org.confluence.terraentity.init.TEAttachments;
import org.confluence.terraentity.init.TEDataComponentTypes;
import org.confluence.terraentity.init.entity.TEProjectileEntities;
import org.confluence.terraentity.item.Boomerang;
import org.confluence.terraentity.item.Boomerang.BoomerangModifier;
import org.confluence.terraentity.registries.datacomponent.IDataComponentType;
import org.confluence.terraentity.utils.TEUtils;
import java.util.UUID;

public class BoomerangProjectile extends AbstractHurtingProjectile {

    public ItemStack weapon = ItemStack.EMPTY;
    private BoomerangModifier modifier;
    public int randomRotation;//随机初始旋转角度
    private float backSpeed;//碰撞返回的速度
    public boolean isBacking;//正在返回
    private int backTime;//返回时间
    private int penetrationCount;

    public BoomerangProjectile(EntityType<? extends AbstractHurtingProjectile> entityType, Level level) {
        super(entityType, level);
        this.modifier = new BoomerangModifier();
        this.randomRotation = this.random.nextInt(114514);
        //BoomerangItems.DEVELOPER_BOOMERANG.get().boomerangModifier
    }

    public BoomerangProjectile(LivingEntity owner, BoomerangModifier modifier, ItemStack weapon) {
        this(TEProjectileEntities.BOOMERANG_PROJECTILE.get(), owner.level());
        this.setOwner(owner);
        this.modifier = modifier;
        this.weapon = weapon;
        this.entityData.set(DATA_WEAPON, weapon.copy());
        this.penetrationCount = modifier.maxPenetration;

    }

    //同步客户端数据
    public static final EntityDataAccessor<ItemStack> DATA_WEAPON = SynchedEntityData.defineId(BoomerangProjectile.class, EntityDataSerializers.ITEM_STACK);
    public static final EntityDataAccessor<Boolean> DATA_BACKING = SynchedEntityData.defineId(BoomerangProjectile.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Integer> DATA_BACKING_TIME = SynchedEntityData.defineId(BoomerangProjectile.class, EntityDataSerializers.INT);

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_WEAPON, ItemStack.EMPTY);
        this.entityData.define(DATA_BACKING, false);
        this.entityData.define(DATA_BACKING_TIME, 0);
    }
    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> var1){
        if(var1 == DATA_BACKING_TIME){
            this.backTime = this.entityData.get(DATA_BACKING_TIME);
        }else if(var1 == DATA_BACKING){
            this.isBacking = this.entityData.get(DATA_BACKING);
        }else if(var1 == DATA_WEAPON){
            weapon = this.entityData.get(DATA_WEAPON);
            modifier = ((Boomerang) weapon.getItem()).boomerangModifier;
        }
    }
    @Override
    public void onAddedToWorld(){
        super.onAddedToWorld();

    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
//        if(!level().isClientSide){
            Entity hurter = result.getEntity();
            Entity actualHurter = hurter;
            if(hurter instanceof PartEntity<?> part){
                hurter = part.getParent();
            }
            if(this.getOwner() instanceof LivingEntity owner && this.getOwner() != actualHurter) {
                if (hurter instanceof LivingEntity living && actualHurter.isAlive()) {
                    penetrationCount--;
                    UUID effectionUUID = UUID.fromString("c1adc95b-0ff9-49ca-b7a3-ee087005b7b0");
                    owner.getAttribute(Attributes.ATTACK_DAMAGE).addTransientModifier(new AttributeModifier(effectionUUID,"temp_boomerang", modifier.damage - 1, AttributeModifier.Operation.ADDITION));
                    float damage = (float) owner.getAttributeValue(Attributes.ATTACK_DAMAGE);
                    owner.getAttribute(Attributes.ATTACK_DAMAGE).removeModifier(effectionUUID);
                    var data = IDataComponentType.getData(weapon, TEDataComponentTypes.EFFECT_STRATEGY);
                    if (data != null) {
                        data.applyAll((LivingEntity) this.getOwner(), living);
                    }
                    actualHurter.hurt(this.damageSources().mobProjectile(this, owner), damage);
                    //击退
                    doKnockback(living);
                }
                if (!modifier.canPenetrate && penetrationCount <= 0 && modifier.forwardTick - tickCount > 10) {
                    if (!isBacking) {
                        backTime = this.tickCount;
                        this.entityData.set(DATA_BACKING_TIME, backTime);
                        this.entityData.set(DATA_BACKING, true);
                        backSpeed = (float) this.getDeltaMovement().length();
                    }
                    isBacking = true;
                }
            }
//        }
    }

    @Override
    protected boolean canHitEntity(Entity target) {

        return TEUtils.projectileCanHitEntityTest.test(this, target);
    }

    protected void doKnockback(LivingEntity entity) {

        double d1 = Math.max(0.0, 1.0 - entity.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE));
        Vec3 vec3 = this.getDeltaMovement().multiply(1.0, 0.0, 1.0).normalize().scale(modifier.knockback * d1);
        if (vec3.lengthSqr() > 0.0) {
            entity.push(vec3.x, 0.1, vec3.z);
        }

    }
    @Override
    protected void onHitBlock(BlockHitResult result) {
        isBacking = true;
        this.noPhysics = true;
        entityData.set(DATA_BACKING, true);
        super.onHitBlock(result);
        if(level().isClientSide) {
            BlockPos blockpos = result.getBlockPos();
            BlockState blockstate = this.level().getBlockState(blockpos);
            Vec3 dir = this.getDeltaMovement().normalize().scale(2);
            Vec3 mid = new Vec3(blockpos.getX()+0.5f , blockpos.getY()+0.5f, blockpos.getZ()+0.5f).add(Vec3.atLowerCornerOf(result.getDirection().getNormal()));
            this.level().addParticle((new BlockParticleOption(ParticleTypes.BLOCK, blockstate)).setPos(blockpos), mid.x, mid.y, mid.z, -dir.x, -dir.y, -dir.z);
            this.level().addParticle((new BlockParticleOption(ParticleTypes.BLOCK, blockstate)).setPos(blockpos), mid.x, mid.y, mid.z, -dir.x, -dir.y, -dir.z);
        }
    }
    @Override
    public void tick(){
        super.tick();

        if(this.getOwner()!= null && this.getOwner() instanceof LivingEntity living){
            if(!isBacking){
                int delta = 10;
                double actualSpeed = Math.min(
                        Mth.lerp((float) (modifier.forwardTick - tickCount) / delta,0.01F,modifier.flySpeed),
                        modifier.flySpeed
                );
//                this.setDeltaMovement(getDeltaMovement().normalize().scale(actualSpeed));
                Vec3 dir = getDeltaMovement().normalize();
                Vec3 motion = dir.scale(actualSpeed );
                this.setDeltaMovement(motion);

                if(this.modifier.forwardTick <= this.tickCount) {
                    isBacking = true;
                    backTime = this.tickCount;
                    this.noPhysics = true;
                    entityData.set(DATA_BACKING, true);
                }
            }else{
                Vec3 distinct = living.position().add(0,1F,0);
                Vec3 dir = distinct.subtract(this.position()).normalize();
                int delta = 10;
                double actualSpeed = Math.min(Mth.lerp((float) (tickCount - backTime) / delta,backSpeed+0.01F,modifier.backSpeed),modifier.backSpeed);
                Vec3 motion = dir.scale(actualSpeed);
                this.setDeltaMovement(motion);
//                this.move(MoverType.SELF, this.getDeltaMovement());
                if(this.distanceToSqr(distinct) <  modifier.backSpeed * 0.8F){
                    discard();
                }
            }
        }
        if(level().isClientSide){
            if(ClientConfig.GENERATE_PROJECTILE_PARTICLE.get() && modifier.particle != null) {
                ParticleOptions particle = modifier.particle.get();
                for (int i = 0; i < modifier.particleCount; i++) {
                    level().addParticle(particle, this.getX() + random.nextFloat() - 0.5f, this.getY() + random.nextFloat() - 0.5f, this.getZ() + random.nextFloat() - 0.5f, 0, 0, 0);
                }
            }
        }

    }
    @Override
    public void shootFromRotation(Entity shooter, float x, float y, float z, float velocity, float inaccuracy) {
        float f = -Mth.sin(y * 0.017453292F) * Mth.cos(x * 0.017453292F);
        float f1 = -Mth.sin((x + z) * 0.017453292F);
        float f2 = Mth.cos(y * 0.017453292F) * Mth.cos(x * 0.017453292F);
        this.shoot(f, f1, f2, velocity, inaccuracy);
        this.setDeltaMovement(this.getDeltaMovement());
    }
    @Override
    protected ParticleOptions getTrailParticle() {
        return null;
    }
    @Override
    public boolean isOnFire() {
        return modifier.fire && (this.level().isClientSide && this.getSharedFlag(0));
    }

    @Override
    public void onRemovedFromWorld(){
        if(!level().isClientSide && !weapon.isEmpty() && getOwner() != null) {
            Boomerang.setBacked(weapon, SingleBooleanComponent.TRUE);

            getOwner().getCapability(TEAttachments.WEAPON_STORAGE).ifPresent(c->{
                c.tryReduce(weapon.getItem());
            });
            //  提前部署
            if(getOwner() instanceof Player player
//                    && (modifier.shouldWaitForBack && !modifier.shouldApplyCd || modifier.maxCount - 1 == count)
            ){

                player.getCooldowns().removeCooldown(weapon.getItem());

            }

        }
        super.onRemovedFromWorld();
    }

    @Override
    public boolean shouldRender(double x, double y, double z) {
        return true;
    }


    @Override
    public boolean ignoreExplosion() {
        return true;
    }
}
