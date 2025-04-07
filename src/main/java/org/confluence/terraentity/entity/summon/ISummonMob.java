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
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import net.neoforged.fml.ModLoader;
import org.confluence.lib.mixed.SelfGetter;
import org.confluence.terraentity.api.event.SummonEvent;
import org.confluence.terraentity.entity.ai.goal.summon.SummonFollowOwnerGoal;
import org.confluence.terraentity.entity.ai.goal.summon.SummonOwnerHurtByTargetGoal;
import org.confluence.terraentity.entity.ai.goal.summon.SummonOwnerHurtTargetGoal;
import org.confluence.terraentity.entity.ai.goal.summon.SummonPriorAttackGoal;
import org.confluence.terraentity.init.TEAttachments;
import org.confluence.terraentity.init.TETags;
import org.confluence.terraentity.item.SummonItem;

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
        return uuid == null ? null : confluence$self().level().getPlayerByUUID(uuid);
    }

    default void summon_setOwnerUUID(@Nullable UUID uuid){
        confluence$self().getEntityData().set(get_DATA_OWNERUUID_ID(), Optional.ofNullable(uuid));
    }

    default UUID summon_getOwnerUUID(){
        return (UUID)((Optional) confluence$self().getEntityData().get(get_DATA_OWNERUUID_ID())).orElse(null);
    }

    default boolean summon_unableToMoveToOwner(){
        return this.summon_getOwner() != null && this.summon_getOwner().isSpectator();
    }

    default boolean summon_shouldTryTeleportToOwner(){
        LivingEntity livingentity = summon_getOwner();
        return livingentity != null && confluence$self().distanceToSqr(summon_getOwner()) >= summon_getDistanceToTeleportToOwner();
    }

    default void summon_tryToTeleportToOwner(){
        LivingEntity livingentity = summon_getOwner();
        if (livingentity != null) {
            this.summon_teleportToAroundBlockPos(livingentity.blockPosition());
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
            MinecraftServer server = confluence$self().getServer();
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

    default void summon_teleportToAroundBlockPos(BlockPos pos) {
        for(int i = 0; i < 10; ++i) {
            int j = confluence$self().getRandom().nextIntBetweenInclusive(-3, 3);
            int k = confluence$self().getRandom().nextIntBetweenInclusive(-3, 3);
            if (Math.abs(j) >= 2 || Math.abs(k) >= 2) {
                int l = confluence$self().getRandom().nextIntBetweenInclusive(-1, 1);
                if (this.summon_maybeTeleportTo(pos.getX() + j, pos.getY() + l, pos.getZ() + k)) {
                    return;
                }
            }
        }
    }

    default boolean summon_maybeTeleportTo(int x, int y, int z) {
        if (!this.summon_canTeleportTo(new BlockPos(x, y, z))) {
            return false;
        } else {
            confluence$self().moveTo((double)x + 0.5, y, (double)z + 0.5, confluence$self().getYRot(), confluence$self().getXRot());
            confluence$self().getNavigation().stop();
            return true;
        }
    }

    default boolean summon_canTeleportTo(BlockPos pos) {
        PathType pathtype = WalkNodeEvaluator.getPathTypeStatic(confluence$self(), pos);
        if (pathtype != PathType.WALKABLE && !summon_canFlyToOwner()) {
            return false;
        } else {
            BlockState blockstate = confluence$self().level().getBlockState(pos.below());
            if (!this.summon_canFlyToOwner() && blockstate.getBlock() instanceof LeavesBlock) {
                return false;
            } else {
                BlockPos blockpos = pos.subtract(confluence$self().blockPosition());
                return confluence$self().level().noCollision(confluence$self(), confluence$self().getBoundingBox().move(blockpos));
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
            confluence$self().getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(summonItem.baseAttackDamage);
        ModLoader.postEvent(new SummonEvent<>(player, stack, this));
    }

    /* Attack API */
    /**简单攻击*/
    default boolean summon_doHurtTarget(Entity entity) {
        float f = (float)confluence$self().getAttributeValue(Attributes.ATTACK_DAMAGE);
        DamageSource damagesource = confluence$self().damageSources().source(TETags.DamageTypes.SUMMONER, summon_getOwner());
        Level var5 = confluence$self().level();
        if (var5 instanceof ServerLevel serverlevel) {
            f = EnchantmentHelper.modifyDamage(serverlevel, confluence$self().getWeaponItem(), entity, damagesource, f);
        }
        // 事件统一处理
//        f += (float) summon_getOwner().getAttributeValue(TEAttributes.MARK_DAMAGE);
        boolean flag = entity.hurt(damagesource, f);
        if (flag) {
            float f1 = summon_getKnockback(entity, damagesource);
            if (f1 > 0.0F && entity instanceof LivingEntity) {
                LivingEntity livingentity = (LivingEntity)entity;
                livingentity.knockback(f1 * 0.5F, Mth.sin(confluence$self().getYRot() * 0.017453292F), -Mth.cos(confluence$self().getYRot() * 0.017453292F));
                confluence$self().setDeltaMovement(confluence$self().getDeltaMovement().multiply(0.6, 1.0, 0.6));
            }

            Level var7 = confluence$self().level();
            if (var7 instanceof ServerLevel) {
                ServerLevel serverlevel1 = (ServerLevel)var7;
                EnchantmentHelper.doPostAttackEffects(serverlevel1, entity, damagesource);
            }

            confluence$self().setLastHurtMob(entity);
//            confluence$self().playAttackSound();
        }

        return flag;
    }

    default float summon_getKnockback(Entity attacker, DamageSource damageSource) {
        float f = (float)confluence$self().getAttributeValue(Attributes.ATTACK_KNOCKBACK);
        Level var5 = confluence$self().level();
        float var10000;
        if (var5 instanceof ServerLevel serverlevel) {
            var10000 = EnchantmentHelper.modifyKnockback(serverlevel, confluence$self().getWeaponItem(), attacker, damageSource, f);
        } else {
            var10000 = f;
        }

        return var10000;
    }




    /* 以下方法需要被写入对应重写方法 */

    default void summon_registerCommonGoals(){
        summon_registerMoveGoal();
        confluence$self().goalSelector.addGoal(10, new LookAtPlayerGoal(confluence$self(), Player.class, 8.0F));
        confluence$self().goalSelector.addGoal(10, new RandomLookAroundGoal(confluence$self()));

        confluence$self().targetSelector.addGoal(1, new SummonPriorAttackGoal<>(confluence$self(), false));
        confluence$self().targetSelector.addGoal(2, new SummonOwnerHurtByTargetGoal(confluence$self()));
        confluence$self().targetSelector.addGoal(3, new SummonOwnerHurtTargetGoal(confluence$self()));
        confluence$self().targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(confluence$self(), Monster.class, 10, true, true, living -> (living instanceof Enemy && !(living instanceof NeutralMob))));
        confluence$self().targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(confluence$self(), Slime.class, 10, true, true, living -> (living instanceof Enemy && !(living instanceof NeutralMob))));
    }

    default void summon_registerMoveGoal(){
        confluence$self().goalSelector.addGoal(6, new SummonFollowOwnerGoal(confluence$self(), 1.0, 10.0F, 2.0F));
    }

    default void summon_onAddedToLevel() {
        if(!confluence$self().level().isClientSide){
            if(summon_getOwner()== null) {
                confluence$self().discard();
                return;
            }
            var data = summon_getOwner().getData(TEAttachments.SUMMONER_STORAGE.get());
            data.getIds().add(confluence$self().getId());
        }
    }


    default void summon_onRemovedFromLevel() {
        if(summon_getOwner() instanceof ServerPlayer owner){
            var data = summon_getOwner().getData(TEAttachments.SUMMONER_STORAGE.get());
            if(data.canRemove(getCost())){
                data.remove(owner, getCost(), confluence$self().getId());
                if(summon_getOwner() instanceof ServerPlayer serverPlayer)
                    data.sync(serverPlayer);
            }
        }
    }

    default boolean summon_discardWhenOwnerDie(){
        if(confluence$self().level().isClientSide) return false;
        if(summon_getOwner() != null) {
            Entity entity = confluence$self().level().getEntity(summon_getOwner().getId());
            if(entity == null || !entity.isAlive()) {
                confluence$self().discard();
                return true;
            }
        }
        return false;
    }

}
