package org.confluence.terraentity.entity.monster.prefab;

import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
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

public class MonsterBuilders {
    public static Supplier<AbstractMonster.Builder> SIMPLE_FLY_DASH_MONSTER = () -> new AbstractMonster.Builder()
            .setController((c,e)->c.add(new AnimationController<GeoAnimatable>(e,"move",10,
                    state->{state.setAnimation(RawAnimation.begin().thenLoop("fly"));return PlayState.CONTINUE;})))
            .setNoGravity()
            .setHealth(1)
            .setAttackDamage(1)
            .setFollowRange(20)
            .setKnockBack(0.5f)
            .setSafeFall(1000)
            .setTarget((t,e)->{
                t.addGoal(0,new NearestAttackableTargetGoal<>(e, Villager.class,false));
                t.addGoal(1,new NearestAttackableTargetGoal<>(e, Player.class,false));

            })
            .setGoal((g,e)-> {
                g.addGoal(0,new DashGoal(e, 0.08f,10,10));
                g.addGoal(1, new MeleeAttackNoLookGoal(e, 0.5, false));
                g.addGoal(2, new LookForwardWanderFlyGoal(e,0.3f,10));

            })
            .setNavigation((e)->new FlyingPathNavigation(e,e.level()))

            ;

    public static Supplier<AbstractMonster.Builder> CRIMSON_KEMERA_BUILDER = () -> SIMPLE_FLY_DASH_MONSTER.get()
            .setHealth(20)
            .setArmor(2)
            .setAttackDamage(11)
            .setHurtSound(ModSounds.ROUTINE_HURT)
            .setDeathSound(ModSounds.ROUTINE_DEATH)
            .setFollowRange(30);

    public static Supplier<AbstractMonster.Builder> DRIPPLER_BUILDER = () -> SIMPLE_FLY_DASH_MONSTER.get()
        .setHealth(26)
        .setArmor(3)
        .setAttackDamage(14)
        .setHurtSound(ModSounds.DRIPPLER_HURT)
        .setDeathSound(ModSounds.DRIPPLER_DEATH)
        .setFlyingSpeed(0.2F)
        .setFollowRange(30);

    public static Supplier<AbstractMonster.Builder> FLYING_FISH_BUILDER = () -> SIMPLE_FLY_DASH_MONSTER.get()
        .setHealth(10)
        .setArmor(1)
        .setAttackDamage(4)
        .setHurtSound(ModSounds.ROUTINE_HURT)
        .setDeathSound(ModSounds.ROUTINE_DEATH)
        .setFlyingSpeed(0.4F)
        .setFollowRange(25);
}
