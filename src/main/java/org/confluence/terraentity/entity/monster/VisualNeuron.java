package org.confluence.terraentity.entity.monster;

import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.confluence.terraentity.Config;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.entity.boss.BrainOfCthulhu;
import org.confluence.terraentity.entity.monster.prefab.AbstractPrefab;
import org.confluence.terraentity.utils.TEUtils;
import org.jetbrains.annotations.NotNull;

public class VisualNeuron extends AbstractMonster{

    private BrainOfCthulhu owner;
    public Vec3 homePos;
    public LivingEntity target;
    public boolean ready = true;
    private static final float MOVE_SPEED = 0.5f;
    private int backDelay = 5;

    // 0 为攻击， 1 为返回
    public int state = 1;
    public VisualNeuron(EntityType<? extends Monster> type, Level level) {
        super(type, level, new AbstractPrefab(44,2,9,0,0,0.1f)
                .getPrefab().setNoGravity());
        this.noPhysics = true;
        _detectInternal = 1;
    }

    public void setOwner(BrainOfCthulhu owner) {
        this.owner = owner;
    }

    public BrainOfCthulhu getOwner() {
        return owner;
    }

    public void attack(LivingEntity target){
        this.target = target;
        state = 0;
    }

    public void tick(){
        super.tick();

        if(!level().isClientSide &&  (owner == null || !owner.isAlive()))
            discard();

        if(!level().isClientSide && isAlive()){

            if(state == 0){
                ready = false;
                if(target != null && target.isAlive()) {
                    this.addDeltaMovement(target.getEyePosition().subtract(position()).normalize().scale(MOVE_SPEED/ 5));
                    if(TEUtils.angleBetween(getDeltaMovement(), target.getEyePosition().subtract(position())) > Math.PI / 4 ) {
                        state = 1;
                        backDelay = 5;
                    }
                }else {
                    state = 1;
                }
            }else {
                backDelay--;
                if(homePos != null){
                    if(position().distanceToSqr(homePos) < 4f)
                        ready = true;
                    addDeltaMovement(homePos.subtract(position()).normalize().scale(MOVE_SPEED / 10));
                }
            }

            if(target != null && target.isAlive()){
                lookAt(target, 30, 30);
            }else{
                lookAt(EntityAnchorArgument.Anchor.EYES, position().scale(2).subtract(owner.position()));
            }


        }

    }

    @Override
    public void firstSpawn(){
        float multiplier = getAttributeMultiplier(Attributes.MAX_HEALTH);
        int size = level().players().size();
        this.getAttribute(Attributes.MAX_HEALTH).addPermanentModifier(new AttributeModifier(TerraEntity.space("difficulty_modifier_max_health"), multiplier*size - 1, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
        this.getAttribute(Attributes.MAX_HEALTH).addPermanentModifier(new AttributeModifier(TerraEntity.space("server_modifier_max_health"), Config.boss_attributes_multiplier_health-1, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
        this.setHealth(this.getMaxHealth());
        this.getAttribute(Attributes.ATTACK_DAMAGE).addPermanentModifier(new AttributeModifier(TerraEntity.space("difficulty_modifier_attack_damage"), multiplier - 1, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
        this.getAttribute(Attributes.ATTACK_DAMAGE).addPermanentModifier(new AttributeModifier(TerraEntity.space("server_modifier_max_health"), Config.boss_attributes_multiplier_damage-1, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag tag) {
        super.readAdditionalSaveData(tag);

    }

    public boolean isReady() {
        return ready && state == 1;
    }

    public boolean hurt(DamageSource source, float amount) {
        if(state == 0) {
            state = 1;
            setDeltaMovement(owner.position().subtract(position()).normalize());
        }
        return super.hurt(source, amount);
    }

    public void doAttack(LivingEntity entity) {
        if(state == 1 && backDelay <= 0) return;
        super.doAttack(entity);
        if(state == 0) state = 1;
    }
}
