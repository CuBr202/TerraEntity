package org.confluence.terraentity.entity.monster.prefab;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import org.confluence.terraentity.entity.ai.goal.DashGoal;
import org.confluence.terraentity.entity.ai.goal.LookForwardWanderFlyGoal;
import org.confluence.terraentity.entity.ai.goal.MeleeAttackNoLookGoal;
import org.confluence.terraentity.entity.monster.AbstractMonster;
import org.confluence.terraentity.init.ModSounds;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;

import java.util.function.Supplier;

import static org.confluence.terraentity.entity.monster.AbstractMonster.copyFrom;

/**
 * 飞行怪预制体
 */
public class FlyMonsterPrefab {

    //在预制体上修改参数
    public static Supplier<AbstractMonster.Builder> CRIMSON_KEMERA_BUILDER =
            ()->new FlyMonsterPrefab(20,2,11,30,0.5f,0.1f).getPrefab()
                    .setHurtSound(ModSounds.ROUTINE_HURT)
                    .setDeathSound(ModSounds.ROUTINE_DEATH)
                    .addGoal((g,e)->{
                        g.addGoal(0, new DashGoal(e,0.98f,0.4f,15));

                    })
            ;

    public static Supplier<AbstractMonster.Builder> DRIPPLER_BUILDER  =
            ()->new FlyMonsterPrefab(26,3,14,30,0.5f,0.2f).getPrefab()
                .setHurtSound(ModSounds.DRIPPLER_HURT)
                .setDeathSound(ModSounds.DRIPPLER_DEATH)
                .addGoal((g,e)->{
                    g.addGoal(0, new DashGoal(e,0.8f,0.2f,10));

                })
            ;

    public static Supplier<AbstractMonster.Builder> FLYING_FISH_BUILDER  =
            ()->new FlyMonsterPrefab(10,1,4,30,0.5f,0.3f).getPrefab()
                .setHurtSound(ModSounds.ROUTINE_HURT)
                .setDeathSound(ModSounds.ROUTINE_DEATH)
                .addGoal((g,e)->{
                    g.addGoal(0, new DashGoal(e,0.95f,0.5f,15,
                            0.02f,5,10,45));

                })
            ;


    //从一个预制体复制参数再调整参数
    public static Supplier<AbstractMonster.Builder> DO_NOTHING  = ()->copyFrom(CRIMSON_KEMERA_BUILDER)
            .setController((c,e)->c.add(new AnimationController<GeoAnimatable>(e,"move",10,s->PlayState.CONTINUE)));


    /**
     * @param health 生命值
     * @param armor 防御值
     * @param attack 攻击力
     * @param followRange 跟随距离
     * @param knockBack 击退力
     * @param knockbackResistance 击退抗性
     */
    public FlyMonsterPrefab(int health,int armor,int attack,int followRange,float knockBack,float knockbackResistance) {
        SIMPLE_FLY_DASH_MONSTER = new AbstractMonster.Builder()
                .setHealth(health)
                .setArmor(armor)
                .setAttackDamage(attack)
                .setFollowRange(followRange)
                .setKnockBack(knockBack)
                .setKnockbackResistance(knockbackResistance)
                .setNoGravity()
                .setSafeFall(1000)
                .addGoal((g,e)-> {
                    g.addGoal(1, new MeleeAttackNoLookGoal(e,  false));
                    g.addGoal(2, new LookForwardWanderFlyGoal(e,0.3f));
                })
                .addTarget((t,e)->{
                    t.addGoal(0, new HurtByTargetGoal(e));
                    t.addGoal(1, new NearestAttackableTargetGoal<>(e, Player.class,false, LivingEntity::canBeSeenAsEnemy));
                })
                .setNavigation((e)->new FlyingPathNavigation(e,e.level()))
                .setController((c,e)->c.add(new AnimationController<GeoAnimatable>(e,"move",10,
                        state->{state.setAnimation(RawAnimation.begin().thenLoop("fly"));return PlayState.CONTINUE;})))
        ;
    }


    protected final AbstractMonster.Builder SIMPLE_FLY_DASH_MONSTER;

    public AbstractMonster.Builder getPrefab() {
        return SIMPLE_FLY_DASH_MONSTER;
    }

}
