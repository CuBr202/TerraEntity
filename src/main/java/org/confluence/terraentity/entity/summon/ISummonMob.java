package org.confluence.terraentity.entity.summon;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.OldUsersConverter;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import net.minecraftforge.fml.ModLoader;
import org.confluence.terraentity.api.event.SummonEvent;
import org.confluence.terraentity.entity.ai.goal.summon.SummonFollowOwnerGoal;
import org.confluence.terraentity.entity.ai.goal.summon.SummonOwnerHurtByTargetGoal;
import org.confluence.terraentity.entity.ai.goal.summon.SummonOwnerHurtTargetGoal;
import org.confluence.terraentity.entity.ai.goal.summon.SummonPriorAttackGoal;
import org.confluence.terraentity.init.TEAttachments;
import org.confluence.terraentity.init.TETags;
import org.confluence.terraentity.item.SummonItem;
import org.confluence.terraentity.mixinauxiliary.SelfGetter;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;

public interface ISummonMob<T extends Mob> extends SelfGetter<T> {

    int getCost();

    void setCost(int cost);
    
    /*Tamed Animals**/
    
    EntityDataAccessor<Optional<UUID>> get_DATA_OWNERUUID_ID();

    default LivingEntity summon_getOwner(){
        UUID uuid = this.summon_getOwnerUUID();
        return uuid == null ? null : te$getSelf().level().getPlayerByUUID(uuid);
    }

    default void summon_setOwnerUUID(@Nullable UUID uuid){
        te$getSelf().getEntityData().set(get_DATA_OWNERUUID_ID(), Optional.ofNullable(uuid));
    }

    default UUID summon_getOwnerUUID(){
        return (UUID)((Optional) te$getSelf().getEntityData().get(get_DATA_OWNERUUID_ID())).orElse(null);
    }
    
    default boolean summon_unableToMoveToOwner(){
        return this.summon_getOwner() != null && this.summon_getOwner().isSpectator();
    }

    default boolean summon_shouldTryTeleportToOwner(){
        LivingEntity livingentity = summon_getOwner();
        return livingentity != null && te$getSelf().distanceToSqr(summon_getOwner()) >= summon_getDistanceToTeleportToOwner();
    }

    default void summon_tryToTeleportToOwner(boolean canFly){
        LivingEntity livingentity = summon_getOwner();
        if (livingentity != null) {
            this.summon_teleportToAroundBlockPos(livingentity.blockPosition(), canFly);
        }
    }

    default boolean summon_wantsToAttack(LivingEntity ownerLastHurtBy, LivingEntity livingentity){
        return true;
    }

    default boolean summon_isTame(){
        return true;
    }


    default void summon_setTame(boolean tame, boolean applyTamingSideEffects){
    }

    default void summon_addData(CompoundTag compound) {
        if (this.summon_getOwnerUUID() != null) {
            compound.putUUID("Owner", this.summon_getOwnerUUID());
        }
        compound.putInt("cost", getCost());

    }

    default void summon_readData(CompoundTag compound) {
        UUID uuid=null;
        if (compound.hasUUID("Owner")) {
            uuid = compound.getUUID("Owner");
        } else {
            String s = compound.getString("Owner");
            MinecraftServer server = te$getSelf().getServer();
            if(server!=null) {
                uuid = OldUsersConverter.convertMobOwnerIfNecessary(server, s);
            }
        }
        if (uuid != null) {
            this.summon_setOwnerUUID(uuid);
        }
        setCost(compound.getInt("cost"));
    }

    /* Teleport API */

    default float summon_getDistanceToTeleportToOwner(){
        return 30 * 30;
    }

    default void summon_teleportToAroundBlockPos(BlockPos pos, boolean canFly) {
        for(int i = 0; i < 10; ++i) {
            int j = te$getSelf().getRandom().nextIntBetweenInclusive(-3, 3);
            int k = te$getSelf().getRandom().nextIntBetweenInclusive(-3, 3);
            if (Math.abs(j) >= 2 || Math.abs(k) >= 2) {
                int l = te$getSelf().getRandom().nextIntBetweenInclusive(-1, 1);
                if (this.summon_maybeTeleportTo(pos.getX() + j, pos.getY() + l, pos.getZ() + k, canFly)) {
                    return;
                }
            }
        }
    }

    default boolean summon_maybeTeleportTo(int x, int y, int z, boolean canFly) {
        if (!this.summon_canTeleportTo(new BlockPos(x, y, z), canFly)) {
            return false;
        } else {
            te$getSelf().moveTo((double)x + 0.5, y, (double)z + 0.5, te$getSelf().getYRot(), te$getSelf().getXRot());
            te$getSelf().getNavigation().stop();
            return true;
        }
    }

    default boolean summon_canTeleportTo(BlockPos pos, boolean canFly) {
        BlockPathTypes pathtype = WalkNodeEvaluator.getBlockPathTypeStatic(te$getSelf().level(), pos.mutable());
        if (pathtype != BlockPathTypes.WALKABLE && !summon_canFlyToOwner()) {
            return false;
        } else {
            BlockState blockstate = te$getSelf().level().getBlockState(pos.below());
            if (canFly && blockstate.getBlock() instanceof LeavesBlock) {
                return false;
            } else {
                BlockPos blockpos = pos.subtract(te$getSelf().blockPosition());
                return te$getSelf().level().noCollision(te$getSelf(), te$getSelf().getBoundingBox().move(blockpos));
            }
        }
    }

    default boolean summon_canFlyToOwner(){
        return false;
    }
    
    /* Summoning API */
    
    default void summon(Player player, ItemStack stack) {
        summon_setOwnerUUID(player.getUUID());
        summon_setTame(true, true);
        if(stack.getItem() instanceof SummonItem<?> summonItem)
            te$getSelf().getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(summonItem.baseAttackDamage);
        ModLoader.get().postEvent(new SummonEvent(player, stack, this));
    }

    /* Attack API */
    /**简单攻击*/
    default boolean summon_doHurtTarget(LivingEntity me , Entity entity) {
        float f = (float)te$getSelf().getAttributeValue(Attributes.ATTACK_DAMAGE);
        DamageSource damagesource = TETags.DamageTypes.of(entity.level(), TETags.DamageTypes.SUMMONER, summon_getOwner());





//        f *= (float) summon_getOwner().getAttributeValue(TEAttributes.SUMMON_DAMAGE.get());
        boolean flag = entity.hurt(damagesource, f);
        if (flag) {
            float f1 = summon_getKnockback(entity, damagesource);
            if (f1 > 0.0F && entity instanceof LivingEntity) {
                LivingEntity livingentity = (LivingEntity)entity;
                livingentity.knockback(f1 * 0.5F, Mth.sin(te$getSelf().getYRot() * 0.017453292F), -Mth.cos(te$getSelf().getYRot() * 0.017453292F));
                te$getSelf().setDeltaMovement(te$getSelf().getDeltaMovement().multiply(0.6, 1.0, 0.6));
            }

            Level var7 = te$getSelf().level();
            if (var7 instanceof ServerLevel) {
                ServerLevel serverlevel1 = (ServerLevel)var7;
                me.doEnchantDamageEffects(te$getSelf(), entity);
            }

            te$getSelf().setLastHurtMob(entity);
//            te$getSelf().playAttackSound();
        }

        return flag;
    }

    default float summon_getKnockback(Entity attacker, DamageSource damageSource) {
        float f = (float)te$getSelf().getAttributeValue(Attributes.ATTACK_KNOCKBACK);
        Level var5 = te$getSelf().level();
        float var10000;
        if (var5 instanceof ServerLevel serverlevel && attacker instanceof LivingEntity living) {
            var10000 = EnchantmentHelper.getKnockbackBonus(living);
        } else {
            var10000 = f;
        }

        return var10000;
    }



    
    /* 以下方法需要被写入对应重写方法 */
    
    default void summon_registerCommonGoals(){
        summon_registerMoveGoal();
        te$getSelf().goalSelector.addGoal(10, new LookAtPlayerGoal(te$getSelf(), Player.class, 8.0F));
        te$getSelf().goalSelector.addGoal(10, new RandomLookAroundGoal(te$getSelf()));

        te$getSelf().targetSelector.addGoal(1, new SummonPriorAttackGoal<>(te$getSelf(), false));
        te$getSelf().targetSelector.addGoal(2, new SummonOwnerHurtByTargetGoal(te$getSelf()));
        te$getSelf().targetSelector.addGoal(3, new SummonOwnerHurtTargetGoal(te$getSelf()));
        te$getSelf().targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(te$getSelf(), Monster.class, 10, true, true, living -> (living instanceof Enemy && !(living instanceof NeutralMob))));
        te$getSelf().targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(te$getSelf(), Slime.class, 10, true, true, living -> (living instanceof Enemy && !(living instanceof NeutralMob))));
    }

    default void summon_registerMoveGoal(){
        te$getSelf().goalSelector.addGoal(6, new SummonFollowOwnerGoal(te$getSelf(), 1.0, 10.0F, 2.0F, false));
    }

    default void summon_onAddedToLevel() {
        if(!te$getSelf().level().isClientSide){
            if(summon_getOwner()== null) {
                te$getSelf().discard();
                return;
            }
            summon_getOwner().getCapability(TEAttachments.SUMMONER_STORAGE).ifPresent(cap->{
                cap.getIds().add(te$getSelf().getId());
            });
        }
    }


    default void summon_onRemovedFromLevel() {
        if(summon_getOwner() instanceof ServerPlayer owner){
            summon_getOwner().getCapability(TEAttachments.SUMMONER_STORAGE).resolve().ifPresent(data-> {
                if (data.canRemove(getCost())) {
                    data.remove(owner, getCost(), te$getSelf().getId());
                    if (summon_getOwner() instanceof ServerPlayer serverPlayer)
                        data.sync(serverPlayer);
                }
            });
        }
    }

    default boolean summon_discardWhenOwnerDie(){
        if(te$getSelf().level().isClientSide) return false;
        if(summon_getOwner() != null) {
            Entity entity = te$getSelf().level().getEntity(summon_getOwner().getId());
            if(entity == null || !entity.isAlive()) {
                te$getSelf().discard();
                return true;
            }
        }
        return false;
    }

}
