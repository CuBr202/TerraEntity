
package org.confluence.terraentity.entity.summon;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FollowOwnerGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.fml.ModLoader;
import org.confluence.terraentity.api.event.SummonEvent;
import org.confluence.terraentity.init.TEAttachments;
import org.confluence.terraentity.init.TEAttributes;
import org.confluence.terraentity.init.TETags;
import org.confluence.terraentity.item.SummonItem;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.util.GeckoLibUtil;

public abstract class AbstractSummonMob extends TamableAnimal implements GeoEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public int cost;
    protected float distanceToOwner;

    private int _detectInternal = 5;
    private int _attackInternal = 5;
    public int attackInternal = 5;
    public float attackRange = 0.75f;

    private float distanceToTeleportToOwner = 30.0f;


    public AbstractSummonMob(EntityType<? extends TamableAnimal> entityType, Level level) {
        super(entityType, level);
    }

    public void summon(Player player, ItemStack stack) {
        this.setOwnerUUID(player.getUUID());
        this.setTame(true, true);
        if(stack.getItem() instanceof SummonItem<?> summonItem)
            this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(summonItem.baseAttackDamage);
        ModLoader.postEvent(new SummonEvent(player, stack, this));

    }


    protected void registerGoals() {

        this.goalSelector.addGoal(6, new FollowOwnerGoal(this, 1.0, 10.0F, 2.0F));
        this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(10, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(2, new OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(3, new OwnerHurtTargetGoal(this));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, Monster.class, 10, true, true, living -> (living instanceof Enemy && !(living instanceof NeutralMob))));
        this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, Slime.class, 10, true, true, living -> (living instanceof Enemy && !(living instanceof NeutralMob))));

    }

    @Override
    public boolean shouldTryTeleportToOwner() {
        LivingEntity livingentity = this.getOwner();
        return livingentity != null && distanceToOwner >= distanceToTeleportToOwner;
    }


    // 开启碰撞伤害
    public boolean canCollisionHurt() {
        return true;
    }

    public int getDetectInternal() {
        return _detectInternal;
    }
    public void collisionHurt() {
        if (canCollisionHurt() && !level().isClientSide && --attackInternal <= 0) {
            attackInternal = getDetectInternal();
            // 包围盒检测造成伤害
            var entities = level().getEntities(this, this.getBoundingBox(). inflate(attackRange), e->e instanceof LivingEntity&& e!= this );
            if (!entities.isEmpty() && getOwner() instanceof LivingEntity owner) {
                for (var e : entities) {
                    if ( e instanceof LivingEntity living && canAttack(living) && (living instanceof Enemy && !(living instanceof NeutralMob) || living == getTarget()) ){
                        attackInternal = _attackInternal;
                        float damage = (float) (this.getAttribute(Attributes.ATTACK_DAMAGE).getValue()
                                * owner.getAttributeValue(TEAttributes.SUMMON_DAMAGE));

                        e.hurt(TETags.DamageTypes.of(level(), TETags.DamageTypes.SUMMONER, owner), damage);
                    }
                }
            }
        }
    }

    @Override
    public boolean canAttack(LivingEntity target) {
        if(target == getOwner()) return false;
        return super.canAttack(target);
    }

    @Override
    public boolean canBeSeenAsEnemy() {
        return false;
    }

    @Override
    public void tick() {
        super.tick();

        if(!level().isClientSide){
            collisionHurt();
        }
        if(this.getOwner() != null)
            this.distanceToOwner = this.distanceTo(this.getOwner());
    }

    @Override
    public void onRemovedFromLevel() {
        if(getOwner() instanceof ServerPlayer owner){
            var data = getOwner().getData(TEAttachments.SUMMONER_STORAGE.get());
            if(data.canRemove(cost)){
                data.remove(owner, cost, this.getId());
                if(getOwner() instanceof ServerPlayer serverPlayer)
                    data.sync(serverPlayer);
            }

        }
    }

    public void onAddedToLevel() {
        super.onAddedToLevel();
        if(!level().isClientSide){
            var data = getOwner().getData(TEAttachments.SUMMONER_STORAGE.get());
            data.getIds().add(this.getId());
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("cost", cost);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        cost = compound.getInt("cost");
    }
    @Override
    public boolean hurt(DamageSource source, float amount) {
        return source.is(DamageTypes.GENERIC_KILL) && super.hurt(source, amount);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public boolean isFood(ItemStack itemStack) {
        return false;
    }

    @Override
    public @Nullable AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
        return null;
    }
}
