package org.confluence.terraentity.entity.monster.prefab;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.phys.Vec3;
import org.confluence.terraentity.entity.ai.goal.DashGoal;
import org.confluence.terraentity.entity.ai.goal.LookForwardWanderFlyGoal;
import org.confluence.terraentity.entity.monster.AbstractMonster;
import org.confluence.terraentity.init.TESounds;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;

import java.util.function.Supplier;

import static org.confluence.terraentity.entity.monster.AbstractMonster.copyFrom;

/**
 * 飞行怪预制体
 */
public class FlyMonsterPrefab extends AbstractPrefab {

    //在预制体上修改参数
    public static Supplier<AbstractMonster.Builder> CRIMSON_KEMERA_BUILDER =
            ()->new FlyMonsterPrefab(20,2,11,30,0.5f,0.1f).getPrefab()
                    .setHurtSound(TESounds.ROUTINE_HURT)
                    .setDeathSound(TESounds.ROUTINE_DEATH)
                    .addGoal((g,e)->{
                        g.addGoal(0, new DashGoal(e,0.98f,0.4f,15));

                    })
            ;

    public static final Supplier<AbstractMonster.Builder> EATER_OF_SOULS_BUILDER =
            ()->new FlyMonsterPrefab(20,2,11,30,0.5f,0.1f).getPrefab()
                    .setHurtSound(TESounds.ROUTINE_HURT)
                    .setDeathSound(TESounds.ROUTINE_DEATH)
                    .addGoal((g,e)->{
                        g.addGoal(0, new DashGoal(e,0.98f,0.4f,15));

                    })
            ;

    public static final Supplier<AbstractMonster.Builder> DRIPPLER_BUILDER  =
            ()->new FlyMonsterPrefab(26,3,14,64,0.5f,0.2f).getPrefab()
                .setHurtSound(TESounds.DRIPPLER_HURT)
                .setDeathSound(TESounds.DRIPPLER_DEATH)
                .addGoal((g,e)->{
                    g.addGoal(0, new DashGoal(e,0.8f,0.2f,10));

                })
            ;

    public static Supplier<AbstractMonster.Builder> FLYING_FISH_BUILDER  =
            ()->new FlyMonsterPrefab(10,1,2,30,0.5f,0.3f).getPrefab()
                .setHurtSound(TESounds.ROUTINE_HURT)
                .setDeathSound(TESounds.ROUTINE_DEATH)
                .addGoal((g,e)->{
                    g.addGoal(0, new DashGoal(e,0.95f,0.5f,15,
                            0.02f,5,10,45));

                })
            ;

    public static Supplier<AbstractMonster.Builder> CAVE_BAT_BUILDER  =
            ()->new FlyMonsterPrefab(8,1,4,60,0.2f,0.5f).getPrefab()
                    .setHurtSound(TESounds.ROUTINE_HURT)
                    .setDeathSound(TESounds.ROUTINE_DEATH)
                    .setFollowRange(16) // 蝙蝠是瞎子，检测距离近点
                    .addGoal((g,e)->{
                        g.addGoal(0, new DashGoal(e,1f,0.5f,30,
                                0.02f,20,20,45));
                    })
                    .setTicker(e->{
                        e.addDeltaMovement(new Vec3(0, Math.sin(e.tickCount*0.2f) * 0.03f ,0));
                    })
            ;
    public static Supplier<AbstractMonster.Builder> JUNGLE_BAT_BUILDER = ()-> copyFrom(CAVE_BAT_BUILDER).setHealth(17).setAttackDamage(8);
    public static Supplier<AbstractMonster.Builder> HELL_BAT_BUILDER  = ()-> copyFrom(CAVE_BAT_BUILDER).setHealth(23).setArmor(2).setAttackDamage(15)
            .setTicker(e->{
                e.addDeltaMovement(new Vec3(0, Math.sin(e.tickCount*0.2f) * 0.03f ,0));
                if(e.level().isClientSide){
                    int offset = e.getId() * 3;
                    float f = Mth.cos((float)(offset + e.tickCount) * 7.448451F * 0.017453292F + 3.1415927F);
                    float f2 = e.getBbWidth() * 0.5f;
                    float f3 = Mth.cos(e.getYRot() * 0.017453292F) * f2;
                    float f4 = Mth.sin(e.getYRot() * 0.017453292F) * f2;
                    float f5 = (0.3F + f * 0.45F) * e.getBbHeight() * 0.5f;
                    e.level().addParticle(ParticleTypes.LAVA, e.getX() + (double)f3, e.getY() + (double)f5, e.getZ() + (double)f4, 0.0, 0.0, 0.0);
                    e.level().addParticle(ParticleTypes.LAVA, e.getX() - (double)f3, e.getY() + (double)f5, e.getZ() - (double)f4, 0.0, 0.0, 0.0);
                }
            });
    public static Supplier<AbstractMonster.Builder> SPORE_BAT_BUILDER = ()-> copyFrom(CAVE_BAT_BUILDER).setHealth(15).setAttackDamage(7);
    public static Supplier<AbstractMonster.Builder> ICE_BAT_BUILDER  = ()-> copyFrom(CAVE_BAT_BUILDER).setHealth(15).setAttackDamage(7)
            .setTicker(e->{
                e.addDeltaMovement(new Vec3(0, Math.sin(e.tickCount*0.2f) * 0.03f ,0));
                if(e.level().isClientSide){
                    float f2 = e.getBbWidth() * 0.2f;
                    float f3 = Mth.cos(e.getYRot() * 0.017453292F + e.getRandom().nextFloat()) * f2;
                    float f4 = Mth.sin(e.getYRot() * 0.017453292F+ e.getRandom().nextFloat()) * f2;
                    float f5 = (0.3F) * (e.getBbHeight()+ e.getRandom().nextFloat() - 0.5f) * 2f;
                    e.level().addParticle(ParticleTypes.SNOWFLAKE, e.getX() + (double)f3, e.getY() + (double)f5, e.getZ() + (double)f4, 0.0, 0.0, 0.0);
                    e.level().addParticle(ParticleTypes.SNOWFLAKE, e.getX() - (double)f3, e.getY() + (double)f5, e.getZ() - (double)f4, 0.0, 0.0, 0.0);
                }
            });


    public static Supplier<AbstractMonster.Builder> BEE_BUILDER  =
            ()->new AbstractPrefab(23,3,13,32,0,0.55f)
                    .getPrefab()
                    .setHurtSound(TESounds.ROUTINE_HURT)
                    .setDeathSound(TESounds.ROUTINE_DEATH)
                    .setNoAttachAttack()
                    .setMovementSpeed(0.5f)
                    .setNoGravity()
            ;



    //从一个预制体复制参数再调整参数
    public static Supplier<AbstractMonster.Builder> DO_NOTHING  = ()->copyFrom(CRIMSON_KEMERA_BUILDER)
            .setController((c,e)->c.add(new AnimationController<GeoAnimatable>(e,"move",10,s->PlayState.CONTINUE)));





    public FlyMonsterPrefab(int health,int armor,int attack,int followRange,float knockBack,float knockbackResistance) {
        super(health,armor,attack,followRange,knockBack,knockbackResistance);
        SIMPLE_MONSTER
                .setNavigation((e)->new FlyingPathNavigation(e,e.level()))
                .setSafeFall(1000)
                .setNoGravity()
                .setPushable(false)
                .setNoFriction()
                .addGoal((g,e)-> {
//                    g.addGoal(1, new MeleeAttackNoLookGoal(e,  false));
                    g.addGoal(2, new LookForwardWanderFlyGoal(e,0.2f, 0));
                })
                .setController((c,e)->c.add(new AnimationController<GeoAnimatable>(e,"move",10,
                        state->{state.setAnimation(RawAnimation.begin().thenLoop("fly"));return PlayState.CONTINUE;})))
        ;
    }

    public AbstractMonster.Builder getPrefab() {
        return SIMPLE_MONSTER;
    }

}
