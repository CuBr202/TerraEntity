package org.confluence.terraentity.entity.monster;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.entity.ai.MobSkill;
import org.confluence.terraentity.entity.monster.prefab.AbstractPrefab;
import org.confluence.terraentity.api.entity.IVanillaVariant;
import org.confluence.terraentity.init.TESounds;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animation.RawAnimation;

import java.util.Map;
import java.util.Random;

/**
 * 巨大卷壳虫
 */
public class GiantShelly extends AbstractFSMMonster<GiantShelly> implements IVanillaVariant<Integer> {

    private static final EntityDataAccessor<Integer> DATA_VARIANT_ID = SynchedEntityData.defineId(GiantShelly.class, EntityDataSerializers.INT);

    Vec3 cachedTarget = null;
    static final ResourceLocation armorAddition = TerraEntity.space("shrink");
    public GiantShelly(EntityType<? extends Monster> type, Level level) {
        super(type, level, new AbstractPrefab(26,3,9,10,0,0.4f)
                .getPrefab()
                .setMovementSpeed(0.1f)
                .setAttachIncrease(0.2f)
        );
        this.collisionProperties = new CollisionProperties(1,20,1);
    }
    MobSkill<GiantShelly> walk;
    MobSkill<GiantShelly> free;
    MobSkill<GiantShelly> shrinking_shell;
    MobSkill<GiantShelly> turn;
    MobSkill<GiantShelly> turn2;

    @Override
    public void addSkills() {
        free = new MobSkill<GiantShelly>(RawAnimation.begin().thenLoop("free"), 40, 0)
                .onInit(e->{
                    if(e.getAttribute(Attributes.ARMOR).hasModifier(armorAddition)){
                        e.getAttribute(Attributes.ARMOR).removeModifier(armorAddition);
                    }
                })
        ;

        walk = new MobSkill<GiantShelly>(RawAnimation.begin().thenLoop("walk"), 60, 0)
                .onInit(e->{
                    cachedTarget = LandRandomPos.getPos(e, 15, 7);
                })
                .onTick(e->{
                    if(this.hurtTime > 0){
                        skills.forceStartIndex(2);
                    }
                    if(e.getTarget() != null){
                        e.getNavigation().moveTo(e.getTarget(), 1.0f);
                    }else if(cachedTarget!= null){
                        e.moveControl.setWantedPosition(cachedTarget.x, cachedTarget.y, cachedTarget.z, 1.0f);
                    }
                })
                .onOver(e->{
                    if(e.getTarget() == null) {
                        skills.forceStartIndex(0);
                        return;
                    }
                })

        ;

        shrinking_shell = new MobSkill<GiantShelly>(RawAnimation.begin().thenPlay("shrinking_shell"), 50, 0)
                .onInit(e->{
                    e.navigation.stop();
                    if(!e.getAttribute(Attributes.ARMOR).hasModifier(armorAddition)){
                        e.getAttribute(Attributes.ARMOR).addTransientModifier(new AttributeModifier(armorAddition, 2, AttributeModifier.Operation.ADD_VALUE));
                    }
                })

        ;

        turn = new MobSkill<GiantShelly>(RawAnimation.begin().thenLoop("turn"), 50, 20)
                .onTick(e->{
                    if(e.getTarget() == null) {
//                        skills.forceStartIndex(0);
                        return;
                    }
                    if(skills.canTrigger() && e.getTarget() != null){
                        Vec3 dir = e.getTarget().position().add(0,1,0).subtract(e.position());
                        this.setDeltaMovement(dir.normalize().scale(dir.length()*0.5f));
                    }
                })
        ;
        turn2 = new MobSkill<GiantShelly>(RawAnimation.begin().thenPlay("turn2"), 15, 0)


        ;

        addSkill(free);
        addSkill(walk);
        addSkill(shrinking_shell);
        addSkill(turn);
        addSkill(turn2);

    }

/* Variant */

    @Override
    public void onAddedToLevel(){
        super.onAddedToLevel();
        this.setVariant(random.nextInt(getTexturesMap().size()));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_VARIANT_ID, 0);
    }

    @Override
    public void setVariant(Integer integer) {
        this.entityData.set(DATA_VARIANT_ID, integer);
    }

    @Override
    public Integer getVariant() {
        return this.entityData.get(DATA_VARIANT_ID);
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putInt("Variant", this.getVariant());
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.setVariant(pCompound.getInt("Variant"));
    }

    @Override
    protected SoundEvent getDeathSound() {
        return TESounds.GIANT_SHELLY_DEATH.get();
    }
    @Override
    protected SoundEvent getAmbientSound() {
        Random rand = new Random();
        SoundEvent sound1 = TESounds.GIANT_SHELLY_FREE_0.get();
        SoundEvent sound2 = TESounds.GIANT_SHELLY_FREE_1.get();

        // 随机选择音效
        return rand.nextBoolean() ? sound1 : sound2;
    }
    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource pDamageSource) {
        return TESounds.GIANT_SHELLY_HURT.get();
    }


    static Map<Integer, ResourceLocation> textures = Map.of(
        0, TerraEntity.space("textures/entity/giant_shelly/purple.png"),
        1, TerraEntity.space("textures/entity/giant_shelly/yellow.png")
    );

    @Override
    public Map<Integer, ResourceLocation> getTexturesMap() {
        return textures;
    }
}
