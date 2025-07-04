package org.confluence.terraentity.entity.monster.prefab;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import org.confluence.terraentity.entity.monster.humanoid.HumanoidMonster;
import org.confluence.terraentity.init.TESounds;

import java.util.function.Function;
import java.util.function.Supplier;

public class AbstractPrefab {


    public static Supplier<AttributeBuilder> WARM_BUILDER =
            ()-> new AbstractPrefab(44,2,1,60,0,0.1f).getPrefab().setNoGravity();


    /**
     * @param health 生命值
     * @param armor 防御值
     * @param attack 攻击力
     * @param followRange 索敌距离
     * @param knockBack 击退力
     * @param knockbackResistance 击退抗性
     */
    public AbstractPrefab(int health, int armor, int attack, int followRange, float knockBack, float knockbackResistance) {
        this.modifier = (builder)-> builder.setHealth(health)
                .setArmor(armor)
                .setAttackDamage(attack)
                .setFollowRange(followRange)
                .setKnockBack(knockBack)
                .setHurtSound(TESounds.ROUTINE_HURT)
                .setDeathSound(TESounds.ROUTINE_DEATH)
                .setKnockbackResistance(knockbackResistance)
                .addTarget((t,e)->{
                    t.addGoal(1, new HurtByTargetGoal(e));
                    t.addGoal(2, new NearestAttackableTargetGoal<>(e, Player.class,false, LivingEntity::canBeSeenAsEnemy));
                });
    }
    public AbstractPrefab(int health, int armor, int attack) {
        this(health, armor, attack, 20, 1f, 0.28f);
    }

    private final Function<AttributeBuilder, AttributeBuilder> modifier;

    public AttributeBuilder getPrefab() {
        return modifier.apply(new AttributeBuilder());
    }

    public HumanoidMonster.HumanoidBuilder asHumanoid() {
        return (HumanoidMonster.HumanoidBuilder) modifier.apply(new HumanoidMonster.HumanoidBuilder());
    }

}
