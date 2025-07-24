package org.confluence.terraentity.entity.monster;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.confluence.terraentity.entity.ai.CircleMobSkills;
import org.confluence.terraentity.api.entity.ai.IFSMGeoMob;
import org.confluence.terraentity.entity.monster.prefab.AttributeBuilder;
import software.bernie.geckolib.animation.AnimatableManager;

public abstract class AbstractFSMMonster<T extends AbstractFSMMonster<T>> extends AbstractMonster implements IFSMGeoMob<T> {

    protected CircleMobSkills<T> skills;
    protected ClientBoundAnimationMessage clientBoundAnimationMessage = new ClientBoundAnimationMessage();

    public static final EntityDataAccessor<Integer> DATA_SKILL_INDEX = SynchedEntityData.defineId(AbstractFSMMonster.class, EntityDataSerializers.INT);

    public AbstractFSMMonster(EntityType<? extends Monster> type, Level level, AttributeBuilder builder) {
        super(type, level, builder);

        skills = new CircleMobSkills(this, DATA_SKILL_INDEX);
    }

    @Override
    protected void registerGoals() {

        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));

    }

    @Override
    public void tick() {
         super.tick();
         skills.tick();
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_SKILL_INDEX, 0);
    }
    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        super.onSyncedDataUpdated(key);
        syncSkills(key);
    }


    @Override
    public void onAddedToLevel(){
        super.onAddedToLevel();
        addToLevel();
    }

    @Override
    public CircleMobSkills<T> getSkills() {
        return skills;
    }

    @Override
    public ClientBoundAnimationMessage getAnimationMessage() {
        return clientBoundAnimationMessage;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        IFSMGeoMob.super.registerControllers(controllers);
    }
}
