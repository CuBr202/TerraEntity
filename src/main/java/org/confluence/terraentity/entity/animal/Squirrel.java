package org.confluence.terraentity.entity.animal;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.confluence.terraentity.init.TESounds;
import org.confluence.terraentity.init.entity.TEAnimals;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.constant.DefaultAnimations;
import software.bernie.geckolib.util.GeckoLibUtil;

import javax.annotation.Nullable;

public class Squirrel extends Animal implements  GeoEntity {

    public Squirrel(EntityType<? extends Squirrel> entityType, Level level) {
        super(entityType, level);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal(this, 2.0));
        this.goalSelector.addGoal(2, new BreedGoal(this, 1.0));
//        this.goalSelector.addGoal(3, new TemptGoal(this, 1.25, (p_335386_) -> {
//            return p_335386_.is(ItemTags.COW_FOOD);
//        }, false));
        this.goalSelector.addGoal(4, new FollowParentGoal(this, 1.25));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
    }

    public boolean isFood(ItemStack stack) {
        return false;
//        return stack.is(ItemTags.COW_FOOD);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 10.0).add(Attributes.SAFE_FALL_DISTANCE, 6).add(Attributes.MOVEMENT_SPEED, 0.20000000298023224);
    }

    protected SoundEvent getAmbientSound() {
        return null;
    }

    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return TESounds.ROUTINE_HURT.get();
    }

    protected SoundEvent getDeathSound() {
        return TESounds.ROUTINE_DEATH.get();
    }

    protected float getSoundVolume() {
        return 0.4F;
    }


    @Nullable
    public Squirrel getBreedOffspring(ServerLevel level, AgeableMob otherParent) {
        return TEAnimals.JEWEL_SQUIRREL.get().create(level);
    }


    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(DefaultAnimations.genericWalkIdleController(this));
    }






}
