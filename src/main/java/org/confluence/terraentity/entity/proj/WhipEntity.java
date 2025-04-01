package org.confluence.terraentity.entity.proj;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.entity.PartEntity;
import org.confluence.terraentity.data.component.EffectStrategyComponent;
import org.confluence.terraentity.data.enchantment.TEEnchantmentHelper;
import org.confluence.terraentity.data.enchantment.TEEnchantments;
import org.confluence.terraentity.entity.ai.keyframe.animation.Vec3KeyframeAnimation;
import org.confluence.terraentity.entity.ai.keyframe.dynamic_curve.SplineKeyframeDynamicCurve;
import org.confluence.terraentity.entity.summon.ISummonMob;
import org.confluence.terraentity.init.TEAttributes;
import org.confluence.terraentity.init.TEDataComponentTypes;
import org.confluence.terraentity.init.TETags;
import org.confluence.terraentity.init.item.TEWhipItems;
import org.confluence.terraentity.utils.TEUtils;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WhipEntity extends AbstractHurtingProjectile {

    Map<Entity, Integer> hitEntities = new HashMap<>();

    int existTick = 22;
    int _existTick = 22; // 基础存在时间
    int drawBackTick = 10; // 返回到player的过渡时间

    protected float _damageDeclineStep = 0.1f; // 基础伤害衰减系数
    protected float _damageDeclineMax = 0.5f; // 最大伤害衰减系数
    protected float damageDecline = 1f; // 伤害衰减

    protected float _rangeFactor = 0.5f; // 基础鞭范围
    public int hitCooldown = 5; // 击中冷却时间
    public EffectStrategyComponent hiteffect; // 击中特效
    public EffectStrategyComponent hiteffect_beneficial; // 农场主增益


    // 初始位置
    public Vec3 initialPosition;
    // 初始方向
    public Vec3 initDirection;
    // 关键点位置，用于插值
    public List<Vector3f> keyPositions;
    // 用于客户端插值
    public List<Vector3f> keyPositionsO;
    // 关键点的本地坐标关键帧
    List<Vec3KeyframeAnimation> parts;
    // 关键点插值器
    public SplineKeyframeDynamicCurve<Vec3KeyframeAnimation> interpolator;
    Vec3KeyframeAnimation tail;
    // 攻速
    public double speed = 1;
    float weepDamage = 1;
    ItemStack weapon;
    float serverRandom = -1;

    protected static final EntityDataAccessor<Vector3f> DATA_INITIAL_POSITION = SynchedEntityData.defineId(WhipEntity.class, EntityDataSerializers.VECTOR3);
    protected static final EntityDataAccessor<Vector3f> DATA_INITIAL_DIRECTION = SynchedEntityData.defineId(WhipEntity.class, EntityDataSerializers.VECTOR3);
    protected static final EntityDataAccessor<Integer> DATA_INITIAL_EXISTING_TIME = SynchedEntityData.defineId(WhipEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<ItemStack> DATA_WEAPON = SynchedEntityData.defineId(WhipEntity.class, EntityDataSerializers.ITEM_STACK);
    private static final EntityDataAccessor<Float> DATA_SERVER_RANDOM = SynchedEntityData.defineId(WhipEntity.class, EntityDataSerializers.FLOAT);

    public WhipEntity(EntityType<? extends WhipEntity> entityType, Level level) {
        super(entityType, level);

//        try {
            // 这里只能单人测试用，发布版本要改用服务端builder!
//        tail = Vec3KeyframeAnimation.fromAnimation(LashAnimation.animation.boneAnimations().get("bone1").getFirst());
//        parts = List.of(
//                tail,
//                Vec3KeyframeAnimation.fromAnimation(LashAnimation.animation.boneAnimations().get("bone4").getFirst())
//        );

//        }catch (NoClassDefFoundError e){
//            TerraEntity.LOGGER.warn("You Forget To Change Debug Code To Release Version!\n", e);
//        }

        this.noPhysics = true;
        this.noCulling = true;
    }

    /**
     * 设置鞭的武器
     */
    public void setWeapon(ItemStack weapon) {
        this.entityData.set(DATA_WEAPON, weapon);
        var data = weapon.get(TEDataComponentTypes.EFFECT_STRATEGY);
        if (data != null)
            hiteffect = data;
        var data1 = weapon.get(TEDataComponentTypes.EFFECT_STRATEGY_BENEFICIAL);
        if (data1 != null)
            hiteffect_beneficial = data1;
        boolean triggerSweep = random.nextFloat() < 0.2f;
        serverRandom =  triggerSweep? 1: 0;
        this.entityData.set(DATA_SERVER_RANDOM, serverRandom);
        updateWeapon(weapon, triggerSweep);
    }

    private void updateWeapon(ItemStack weapon, boolean sweep){

        if(sweep) {
            int sweepLevel = TEEnchantmentHelper.getEnchantmentLevel(TEEnchantments.WHIP_SWEEP, weapon);
            // 横扫之鞭
            parts = List.of(
                    Vec3KeyframeAnimation.Builder()
                            .addKeyframeTimeStamp(0, new Vec3(0, 0, 0))
                            .addKeyframeTimeStamp(0.1667, new Vec3(-2, 1, 3))
                            .addKeyframeTimeStamp(0.375, new Vec3(-6, 2, 4))
                            .addKeyframeTimeStamp(0.5417, new Vec3(-8, 1, 0))
                            .addKeyframeTimeStamp(0.7083, new Vec3(-6, 0, -4))
                            .addKeyframeTimeStamp(0.875, new Vec3(-2, 0, -3))
                            .addKeyframeTimeStamp(1, new Vec3(0, 0, 0))
                            .build(),
                    Vec3KeyframeAnimation.Builder()
                            .addKeyframeTimeStamp(0, new Vec3(0, 0, 0))
                            .addKeyframeTimeStamp(0.1667, new Vec3(0, 0, 1))
                            .addKeyframeTimeStamp(0.375, new Vec3(-2, 0, 3))
                            .addKeyframeTimeStamp(0.5417, new Vec3(-3, 0, 0))
                            .addKeyframeTimeStamp(0.7083, new Vec3(-2, 0, -2))
                            .addKeyframeTimeStamp(0.875, new Vec3(-1, 0, -1))
                            .addKeyframeTimeStamp(1, new Vec3(0, 0, 0))
                            .build()
            );
            this.weepDamage += sweepLevel * 0.2f;
        }else {
            parts = List.of(
                    Vec3KeyframeAnimation.Builder()
                            .addKeyframeTimeStamp(0, new Vec3(0, 0, 0))
                            .addKeyframeTimeStamp(0.25, new Vec3(-4, 3, 0))
                            .addKeyframeTimeStamp(0.5, new Vec3(-14, 3, 0))
                            .addKeyframeTimeStamp(0.75, new Vec3(-16, -4, 0))
                            .addKeyframeTimeStamp(1, new Vec3(0, 0, 0))
                            .build(),
                    Vec3KeyframeAnimation.Builder()
                            .addKeyframeTimeStamp(0, new Vec3(0, 0, 0))
                            .addKeyframeTimeStamp(0.25, new Vec3(-1, 0, 0))
                            .addKeyframeTimeStamp(0.5, new Vec3(-4, 0, 0))
                            .addKeyframeTimeStamp(0.75, new Vec3(-5, 0, 0))
                            .addKeyframeTimeStamp(1, new Vec3(0, 0, 0))
                            .build()

            );
        }
        keyPositions = new ArrayList<>();
        keyPositionsO = new ArrayList<>();
        for (int i = 0; i < parts.size(); i++) {
            keyPositions.add(new Vector3f());
            keyPositionsO.add(new Vector3f());
        }
        interpolator = new SplineKeyframeDynamicCurve<>(parts);
    }
    /**
     * 获取鞭的武器
     */
    public ItemStack getWeapon() {
        return this.entityData.get(DATA_WEAPON);
    }

    /**
     * 设置鞭存在时间
     */
    public void setExistTick(int existTick) {
        this.entityData.set(DATA_INITIAL_EXISTING_TIME, existTick);
        this.speed = (double) this.existTick / existTick;
        this.existTick = existTick;
        this.drawBackTick = (int) (existTick * 0.5F);
    }

    /**
     * 获取鞭范围
     */
    public double getRange(Player player) {
        return _rangeFactor * player.getAttribute(TEAttributes.WHIP_RANGE).getValue();
    }


    @Override
    public void tick() {
        if (!level().isClientSide) {
            if (existTick < tickCount) {
                discard();
                return;
            }
        }
        if(parts == null || parts.isEmpty()) return;
        this.speed = (double) _existTick / this.existTick;
//        this.move(MoverType.SELF, this.getDeltaMovement());
        if (getOwner() instanceof Player owner) {
            if (initialPosition != null && initDirection != null) {
                // 计算关键点位置
                float yaw = (float) (Math.PI - Math.atan2(initDirection.z, initDirection.x));
                float pitch = (float) (-Math.atan2(initDirection.y,
                        Math.sqrt(initDirection.x * initDirection.x + initDirection.z * initDirection.z)));
                Quaternionf q = new Quaternionf()
                        .rotateY(yaw)
                        .rotateZ(pitch);
                for (int i = 0; i < parts.size(); i++) {
                    // 世界坐标变换
                    Vec3KeyframeAnimation p = parts.get(i);
                    Vec3 pos = p.cal(tickCount * speed).multiply(getRange(owner), -1, 1);
                    Vector3f lp = pos.toVector3f();
                    q.transform(lp);
                    keyPositionsO.set(i, keyPositions.get(i));
                    keyPositions.set(i, lp);
                }

                if(tickCount > drawBackTick){
                    // 过渡到player位置

                    double delta = (double) (tickCount - drawBackTick) / (existTick - drawBackTick);
                    double lerpx = Mth.lerp(delta, getX(), getOwner().getEyePosition().x);
                    double lerpy = Mth.lerp(delta, getY(), getOwner().position().y + getOwner().getEyeHeight() * 0.5f);
                    double lerpz = Mth.lerp(delta, getZ(), getOwner().getEyePosition().z);
//                    setDeltaMovement(0,0,0);
//                    setPos(lerpx, lerpy, lerpz);

                    Vec3 dir = new Vec3(lerpx - getX(), lerpy - getY(), lerpz - getZ());
                    setDeltaMovement(0,0,0);
                    move(MoverType.SELF, dir.scale(0.5f));


//                    Vec3 dir = getOwner().position().add(0, getOwner().getEyeHeight() * 0.5f, 0).subtract(position()).scale(0.3f);
//                    setDeltaMovement(0,0,0);
//                    move(MoverType.SELF, dir);

                }
//                if(!level().isClientSide){
                    boolean trigger = false;
                // 可以插值让攻击更准确
                    List<Vec3> attackPoints = keyPositions.stream().map(Vec3::new).toList();

                    float additionalRange = serverRandom == 1? 0.5f: 0;
                    // 攻击
                    float range = 1.5f + additionalRange;
                    for (Vec3 attackPoint : attackPoints) {
                        Vec3 pos = attackPoint.add(initialPosition);
                        AABB aabb = new AABB(pos.x - range, pos.y - range, pos.z - range,
                                pos.x + range, pos.y + range, pos.z + range);
                        for (var entity : level().getEntities(this, aabb, e -> e != getOwner())) {
                            if(!hitEntities.containsKey(entity)){
                                if (entity instanceof LivingEntity hurter) {
                                    // 命中无多体节敌人
                                    if(owner.canAttack(hurter) && hurter.canBeSeenAsEnemy()) {
                                        hitEntities.put(entity, hitCooldown);
                                        trigger = doHurt(owner, hurter, hurter);
                                    }
                                    if(hurter instanceof ISummonMob<?>){
                                        if(hiteffect_beneficial != null){
                                            hiteffect_beneficial.applyAll( owner, hurter);
                                        }
                                    }

                                }else if(entity instanceof PartEntity<?> partEntity){
                                    // 名字多体节敌人
                                    if(partEntity.getParent() instanceof LivingEntity hurter){
                                        if(owner.canAttack(hurter) && hurter.canBeSeenAsEnemy()) {
                                            hitEntities.put(entity, hitCooldown);
                                            trigger = doHurt(owner, hurter, partEntity);
                                        }
                                    }
                                }
                            }else{
                                hitEntities.put(entity, hitEntities.get(entity) - 1);
                                if(hitEntities.get(entity) <= 0){
                                    hitEntities.remove(entity);
                                }
                                return;
                            }
                        }
                    }
                    if(trigger){
                        // 命中敌人造成伤害才消耗耐久
                        getWeapon().hurtAndBreak(1, owner, EquipmentSlot.MAINHAND);
                    }
//                }
            }
        }
        super.tick();
    }

    protected boolean doHurt(LivingEntity owner, LivingEntity hurter, Entity actualHurter){
        double damage = owner.getAttributeValue(TEAttributes.SUMMON_DAMAGE);
        boolean trigger = false;
        if(TEUtils.attackTamableTest.test(owner, hurter)){
            trigger = true;
            damage *= damageDecline;
            damageDecline = Math.max(_damageDeclineMax, damageDecline - _damageDeclineStep);
            if (hiteffect != null) {
                hiteffect.applyAll( owner, hurter);
            }
        }else{
            // 当命中宠物时
            if(getWeapon().getItem() == TEWhipItems.LEATHER_WHIP.get()){
                if(hiteffect_beneficial != null){
                    hiteffect_beneficial.applyAll( owner, hurter);
                }
                // 如果是皮鞭
                damage *= 0.2F;
            }else{
                return false;
            }
        }
        actualHurter.hurt(TETags.DamageTypes.of(level(), TETags.DamageTypes.SUMMON,  owner), (float) damage * weepDamage);
        return trigger;
    }

    @Override
    public boolean shouldBeSaved() {
        return false;
    }

    @Override
    public boolean isOnFire() {
        return false;
    }

    @Nullable
    protected ParticleOptions getTrailParticle() {
        return null;
    }
    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(DATA_INITIAL_POSITION, new Vector3f(0, 0, 0));
        builder.define(DATA_INITIAL_DIRECTION, new Vector3f(0, 0, 0));
        builder.define(DATA_WEAPON, ItemStack.EMPTY);
        builder.define(DATA_INITIAL_EXISTING_TIME, 22);
        builder.define(DATA_SERVER_RANDOM, -1f);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> var1){
        if(level().isClientSide){

            if (var1 == DATA_INITIAL_POSITION) {
                initialPosition = new Vec3(this.entityData.get(DATA_INITIAL_POSITION));
            } else if (var1 == DATA_INITIAL_DIRECTION) {
                initDirection = new Vec3(this.entityData.get(DATA_INITIAL_DIRECTION));
            }else if (var1 == DATA_INITIAL_EXISTING_TIME) {
                existTick = this.entityData.get(DATA_INITIAL_EXISTING_TIME);
                this.drawBackTick = (int) (existTick * 0.5F);
            }else if(var1 == DATA_WEAPON){
                weapon = this.entityData.get(DATA_WEAPON);
            }else if(var1 == DATA_SERVER_RANDOM){
                this.serverRandom = this.entityData.get(DATA_SERVER_RANDOM);
            }
            boolean keyframeRelated = var1 == DATA_WEAPON || var1 == DATA_SERVER_RANDOM;
            if(keyframeRelated){
                if(weapon != null && serverRandom != -1){
                    updateWeapon(this.entityData.get(DATA_WEAPON), serverRandom == 1);
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
//        this.setDeltaMovement(0,0,0);
        this.initialPosition = position();
        this.initDirection = new Vec3(f, f1, f2);
        this.entityData.set(DATA_INITIAL_POSITION, initialPosition.toVector3f());
        this.entityData.set(DATA_INITIAL_DIRECTION, initDirection.toVector3f());
    }

    @Override
    protected boolean canHitEntity(Entity target) {
        return false;
    }

}
