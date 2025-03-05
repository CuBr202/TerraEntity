package org.confluence.terraentity.entity.boss;

import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.entity.PersistentEntitySectionManager;
import net.minecraft.world.phys.Vec3;
import net.neoforged.fml.ModLoader;
import org.confluence.terraentity.config.ServerConfig;
import org.confluence.terraentity.api.event.BossDeathEvent;
import org.confluence.terraentity.client.gui.CustomizeBossHealthBar;
import org.confluence.terraentity.entity.ai.Boss;
import org.confluence.terraentity.entity.ai.CircleMobSkills;
import org.confluence.terraentity.entity.ai.ICollisionAttackEntity;
import org.confluence.terraentity.entity.ai.IFSMGeoMob;
import org.confluence.terraentity.entity.ai.goal.LookForwardWanderFlyGoal;
import org.confluence.terraentity.init.TETags;
import org.confluence.terraentity.utils.TEUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.Predicate;

import static org.confluence.terraentity.utils.TEUtils.getMultiple;


@SuppressWarnings("all")
public abstract class AbstractTerraBossBase<T extends AbstractTerraBossBase> extends Monster implements GeoEntity, IFSMGeoMob<T>, ICollisionAttackEntity<T> {

/* 属性 */

    public float ironGlomResistance = 0.4f;
    public float explosionResistance = 0.5f;

    protected boolean dirty = true;
    protected ServerBossEvent bossEvent = (ServerBossEvent) new ServerBossEvent(getDisplayName(), BossEvent.BossBarColor.RED, BossEvent.BossBarOverlay.PROGRESS).setDarkenScreen(true);
    private final float baseHealth;
    private final int baseArmor;

    public AbstractTerraBossBase(EntityType<? extends Monster> type, Level level, float health, int armor) {
        super(type, level);
        this.moveControl = new FlyingMoveControl(this, 10, false);
        setNoGravity(true);
        this.baseHealth = health;
        this.baseArmor = armor;
        var a = bossEvent.getOverlay();
        if(level().isClientSide){
            CustomizeBossHealthBar.registerBossHealthBar(getDisplayName().getString(),this.getType());
        }
    }

    public float getAttributeMultiplier(Holder<Attribute> attribute){
        return getMultiple(level(), attribute);
    }

    public void firstSpawn(){};

    @Override
    public void onAddedToLevel(){
        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(this.baseHealth);
        this.getAttribute(Attributes.ARMOR).setBaseValue(baseArmor);

        if(!level().isClientSide){
            TEUtils.multiplePlayerEnhance(this,dirty);
            if(dirty)
                firstSpawn();
        }

        super.onAddedToLevel();
        this.addSkills();
        if(skills.count() > 0)
            skills.forceStartIndex(0);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MOVEMENT_SPEED, 1)
                .add(Attributes.ATTACK_DAMAGE, 1)
                .add(Attributes.ATTACK_KNOCKBACK, 2.2)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0)
                .add(Attributes.FOLLOW_RANGE, 300.0);

    }

    protected void setAttactDamage(float damage){
        this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(damage);
    }


/* 攻击目标 */

    private static final Predicate<LivingEntity> LIVING_ENTITY_SELECTOR = entity -> entity instanceof Player;

    protected void registerGoals() {
        //this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 100F));

        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));

        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Player.class, false));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, IronGolem.class, false));

        if(!ServerConfig.BOSS_CLEAR_WHEN_NO_TARGET.get() && !(this instanceof EaterOfWorldsSegment))
            this.goalSelector.addGoal(10, new LookForwardWanderFlyGoal(this,0.3f, 0));

    }



/* FSM */

    public CircleMobSkills skills = new CircleMobSkills(this, DATA_SKILL_INDEX);
    public static final EntityDataAccessor<Integer> DATA_SKILL_INDEX = SynchedEntityData.defineId(AbstractTerraBossBase.class, EntityDataSerializers.INT);
    protected ClientBoundAnimationMessage skillMessage = new ClientBoundAnimationMessage();
    protected int lastSkillTick;
    @Override
    public CircleMobSkills getSkills() {
        return skills;
    }

    @Override
    public ClientBoundAnimationMessage getAnimationMessage() {
        return skillMessage;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_SKILL_INDEX, 0);
//        builder.define(DATA_SKILL_TICK, 0);
    }
    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        super.onSyncedDataUpdated(key);
        syncSkills(DATA_SKILL_INDEX);
    }


/* Collision */

    CollisionProperties collisionProperties = new CollisionProperties(5, 10, 0);

    @Override
    public CollisionProperties getCollisionProperties() {
        return collisionProperties;
    }

    @Override
    public boolean shouldDoCollision(){
        return getTarget() != null;
    }

/* discard */

    LivingEntity target;
    protected static final int DISCARD_TICK = 100;
    protected int discardTick = 0;

    @Override
    public void tick() {
        super.tick();

        if (!level().isClientSide){
            target = getTarget();
            if(this.isAlive())
                skills.tick();
            //没有目标禁止行为

            if(target==null){
                var entity = ((ServerLevel)level()).getNearestPlayer(this, getAttributeValue(Attributes.FOLLOW_RANGE));
                if(entity!= null){
                    if(entity.canBeSeenAsEnemy()){
                        setTarget(entity);
                        return;
                    }
                }

                discardTick++;
                if(!level().isClientSide && discardTick > DISCARD_TICK && ServerConfig.BOSS_CLEAR_WHEN_NO_TARGET.get()){
                    this.bossEvent.getPlayers().forEach(p->p.sendSystemMessage(this.getDisplayName().copy().append(Component.translatable("message.terraentity.boss_discard"))));
                    this.discard();
                }
                return;
            }
            discardTick = 0;

            doCollisionAttack(
                    e->canAttack(e) && e!= this && e.canBeSeenAsEnemy(),
                    this::doHurtTarget
            );

        }

        this.setDeltaMovement(getDeltaMovement().scale(0.95));//空气阻力
    }

    @Override
    public boolean doHurtTarget(Entity entity) {
        return entity.hurt(TETags.DamageTypes.of(level(), DamageTypes.GENERIC, this), (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE));
    }

    /* func */

    public void lookAtPos(Vec3 target, float pMaxYRotIncrease, float pMaxXRotIncrease) {
        double d0 = target.x - this.getX();
        double d2 = target.z - this.getZ();
        double d1 = target.y - this.getEyeY();

        double d3 = Math.sqrt(d0 * d0 + d2 * d2);
        float f = (float)(Mth.atan2(d2, d0) * 57.2957763671875) - 90.0F;
        float f1 = (float)(-(Mth.atan2(d1, d3) * 57.2957763671875));
        this.setXRot(this.rotlerp(this.getXRot(), f1, pMaxXRotIncrease));
        this.setYRot(this.rotlerp(this.getYRot(), f, pMaxYRotIncrease));
    }

    private float rotlerp(float pAngle, float pTargetAngle, float pMaxIncrease) {
        float f = Mth.wrapDegrees(pTargetAngle - pAngle);
        if (f > pMaxIncrease) {
            f = pMaxIncrease;
        }

        if (f < -pMaxIncrease) {
            f = -pMaxIncrease;
        }

        return pAngle + f;
    }

    public void LookAt(float maxAngleY) {
        var pEntity = getTarget();
        if (pEntity != null) {
            lookAt(getTarget(), maxAngleY, 85);
            this.lookControl.setLookAt(getTarget());
        }
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        if(pSource.getEntity() instanceof IronGolem){
            pAmount *= ironGlomResistance;
        }
        if(pSource.is(DamageTypes.EXPLOSION)){
            pAmount *= explosionResistance;
        }

        return super.hurt(pSource,pAmount);
    }

    public boolean canAttack(LivingEntity entity) {
        return super.canAttack(entity)&&
                (
                        entity instanceof Player ||
                                        entity != this
//                                        &&!(entity instanceof AbstractTerraBossBase)
                                        && entity instanceof LivingEntity living && living.canBeSeenAsEnemy()
                );
    }

    public float getHealthPercentage(){
        return this.getHealth() / this.getMaxHealth();
    }

    public float getMoveSpeed(){
        return (float) this.getAttributeValue(Attributes.MOVEMENT_SPEED);
    }

/* boss条 */

    public boolean shouldShowBossBar() {
        return true;
    }

    @Override // boss条显示
    public void startSeenByPlayer(ServerPlayer player) {
        super.startSeenByPlayer(player);
        if (shouldShowBossBar()){
            this.bossEvent.addPlayer(player);
        }
    }

    @Override // boss条消失
    public void stopSeenByPlayer(ServerPlayer player) {
        super.stopSeenByPlayer(player);
        if (shouldShowBossBar())
            this.bossEvent.removePlayer(player);
    }

    public float getBossEventProgress(){
        return this.getHealth() / this.getMaxHealth();
    }

    @Override // boss条更新
    protected void customServerAiStep() {
        super.customServerAiStep();
        if (shouldShowBossBar())
            this.bossEvent.setProgress(getBossEventProgress());
    }

    @Override // 取消墙体窒息伤害
    public boolean isInWall() {
        return false;
    }

    @Override // 是否免疫摔伤
    public boolean causeFallDamage(float fallDistance, float multiplier, DamageSource damageSource) {
        return false;
    }

    @Override // 是否在实体上渲染着火效果
    public boolean displayFireAnimation() {
        return false;
    }

    @Override // 受伤音效
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.SKELETON_HURT;
    }

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public boolean canCollideWith(Entity entity) {
        return false;
    }

    @Override
    public boolean canBeCollidedWith() {
        return false;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("dirty", false);
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (hasCustomName()) {
            bossEvent.setName(getDisplayName());
        }
        if (tag.contains("dirty")) {
            dirty = false;
        }
    }

    @Override
    public void setCustomName(@Nullable Component pName) {
        super.setCustomName(pName);
        bossEvent.setName(getDisplayName());
    }

    protected void postDeath(){
        if(this instanceof Boss boss && boss.isMainBody())
            ModLoader.postEvent(new BossDeathEvent(this));
    }
}