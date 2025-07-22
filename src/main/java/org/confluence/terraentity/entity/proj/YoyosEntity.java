package org.confluence.terraentity.entity.proj;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.confluence.terraentity.api.ILeftClickReceiver;
import org.confluence.terraentity.attachment.WeaponStorage;
import org.confluence.terraentity.entity.summon.AbstractSummonMob;
import org.confluence.terraentity.init.TEAttachments;
import org.confluence.terraentity.item.YoyosItem;
import org.confluence.terraentity.utils.TEUtils;
import software.bernie.geckolib.animation.AnimatableManager;

/**
 * 悠悠球
 */
public class YoyosEntity<T extends YoyosEntity<T>> extends AbstractSummonMob<T> implements ILeftClickReceiver {

    boolean isBacking = false;
    int maxRetrieveTicks = 40;
    int retrieveTicks = 0;

    protected static final EntityDataAccessor<ItemStack> DATA_WEAPON_ITEM = SynchedEntityData.defineId(YoyosEntity.class, EntityDataSerializers.ITEM_STACK);

    public YoyosEntity(EntityType<? extends YoyosEntity<?>> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);

    }

    @Override
    public void tick() {
        super.tick();
        Entity owner = getOwner();
        if (owner == null) {
            discard();
            return;
        }
        Vec3 lookVec = owner.getLookAngle().normalize();
        int maxRange = 10;
        Vec3 targetPos;

        float speedModifier = 1.0f;
        if(this.isBacking){
            targetPos = owner.position().add(0,owner.getBbHeight() * 0.5f,0);
            if(!level().isClientSide) {
                if (targetPos.distanceTo(position()) < 0.5F) {
                    discard();
                }
            }
        }else{
            EntityHitResult result = TEUtils.getEyeTraceHitResult(owner, maxRange);
            if(result != null && result.getEntity() instanceof LivingEntity living && this.canAttack(living)){
                targetPos = living.position().add(0,living.getBbHeight() * 0.5f,0);
                if(this.position().distanceTo(targetPos) < 0.5F) {
                    this.noPhysics = true;
                }
                speedModifier = 4f;
            }else{
                targetPos = owner.getEyePosition().add(lookVec.scale(maxRange));
                this.noPhysics = false;
            }


        }
        Vec3 startPos = position();
        Vec3 dist = targetPos.subtract(startPos);


        this.setDeltaMovement(dist.scale(0.2f * speedModifier));
        if(this.isBacking){
            this.retrieveTicks++;
            Vec3 force = dist.normalize().scale(this.retrieveTicks * 1.0f / this.maxRetrieveTicks);
            this.addDeltaMovement(force);
        }

    }


    @Override
    public float summon_getKnockback(Entity attacker, DamageSource damageSource) {
         if(this.getOwner() == null){
             return 0.0f;
         }
         return (float) getOwner().getAttributeValue(Attributes.ATTACK_KNOCKBACK) + 0.1f;
    }

    @Override
    public float summon_getAttackDamage(Entity entity, ServerLevel serverLevel, DamageSource damageSource){
        if(this.getOwner() == null || !(getWeaponItem().getItem() instanceof YoyosItem yoyo)){
            return 0.0f;
        }
        float f = (float) getOwner().getAttributeValue(Attributes.ATTACK_DAMAGE) + yoyo.getAttackDamage();
        f = EnchantmentHelper.modifyDamage(serverLevel, asEntity().getWeaponItem(), entity, damageSource, f);
        return f;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_WEAPON_ITEM, ItemStack.EMPTY);

    }

    public void setWeaponItem(ItemStack itemStack) {
        this.entityData.set(DATA_WEAPON_ITEM, itemStack);
    }

    @Override
    public ItemStack getWeaponItem() {
        return this.entityData.get(DATA_WEAPON_ITEM);
    }

    @Override
    public void onRemovedFromLevel() {
        super.onRemovedFromLevel();
        Entity owner = getOwner();
        if (owner != null) {
            WeaponStorage data = owner.getData(TEAttachments.WEAPON_STORAGE);
            data.yoyosEntity = null;
        }
    }

    @Override
    public int getMaxHeadXRot() {
        return 85;
    }

    @Override
    public boolean shouldBeSaved() {
        return false;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

    }

    @Override
    public void onReceiveLeftClick(Player player, ItemStack itemStack) {
        this.isBacking = false;
        this.noPhysics = false;
        this.retrieveTicks = 0;
    }

    @Override
    public void onReceiveLeftRelease(Player player, ItemStack itemStack) {
        this.isBacking = true;
        this.noPhysics = true;
    }

}
