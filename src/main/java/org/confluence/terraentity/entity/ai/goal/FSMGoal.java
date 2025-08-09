package org.confluence.terraentity.entity.ai.goal;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import org.confluence.terraentity.entity.ai.AbstractMobSkill;
import org.confluence.terraentity.entity.ai.CircleMobSkills;

import java.util.EnumSet;

public abstract class FSMGoal<T extends Mob> extends Goal {

    Mob mob;
    CircleMobSkills<T> skills;

    public FSMGoal(T mob, EntityDataAccessor<Integer> skillIndexData) {
        this.mob = mob;
        this.skills = new CircleMobSkills<>(mob, skillIndexData);
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        this.init(skills);
    }

    public abstract void init(CircleMobSkills<T> skills);

    public CircleMobSkills<T> getSkills() {
        return skills;
    }

    // 覆盖接口的同名方法
    public void addSkill(AbstractMobSkill<T> skill){
        skills.pushSkill(skill);
    }

    @Override
    public void tick() {
        this.skills.tick();
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public boolean canUse() {
        return true;
    }
}
