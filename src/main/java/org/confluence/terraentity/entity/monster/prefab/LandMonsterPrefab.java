package org.confluence.terraentity.entity.monster.prefab;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.confluence.terraentity.entity.ai.goal.AccelerateOnSeeingGoal;
import org.confluence.terraentity.entity.ai.goal.JumpAttack;
import org.confluence.terraentity.entity.ai.goal.JumpOverBlockGoal;
import org.confluence.terraentity.init.TESounds;
import org.confluence.terraentity.init.entity.TEMonsterEntities;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.constant.DefaultAnimations;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import static software.bernie.geckolib.constant.DefaultAnimations.genericWalkRunIdleController;

public class LandMonsterPrefab extends AbstractPrefab {

    public static Supplier<AttributeBuilder> FACE_MONSTER_BUILDER =
            ()->new LandMonsterPrefab(36,2,13,64,0.7f,0.1f).getPrefab()
                    .setStepHeight(3.2f)
                    .setSpawnWithoutLight()
                    .setAmbientSound(TESounds.FACE_HOOT)
                    .setDeathSound(TESounds.TR_ZOMBIE_DEATH)
                    .setJumpStrength(0.8f)
                    .addTarget((t,e)-> t.addGoal(1, new NearestAttackableTargetGoal<>(e, Player.class,false, LivingEntity::canBeSeenAsEnemy)))
                    .addGoal((g,e)-> {
                        g.addGoal(1, new JumpAttack(e, 3, 8));
                        g.addGoal(2, new JumpOverBlockGoal(e));
                        g.addGoal(3, new MeleeAttackGoal(e,  1f, true));
                        g.addGoal(7, new WaterAvoidingRandomStrollGoal(e, 1.0));
                        g.addGoal(8, new LookAtPlayerGoal(e, Player.class, 6));

                    })
            ;

    public static Supplier<AttributeBuilder> SPORE_ZOMBIE_BUILDER =
            ()->new LandMonsterPrefab(93,2,20,60,0.6f,0.1f).getPrefab()
                    .setMovementSpeed(0.08f)
                    .setSpawnWithoutLight()
                    .setDeathSound(TESounds.TR_ZOMBIE_DEATH)
                    .addTarget((t,e)-> {
                        t.addGoal(1,new AccelerateOnSeeingGoal(e,0.25f));
                        t.addGoal(2, new NearestAttackableTargetGoal<>(e, Player.class,false, LivingEntity::canBeSeenAsEnemy));

                    })
                    .setController((c,e)->{
                        c.add(genericWalkRunIdleController(e));
                        c.add(new AnimationController<>(e, "Attack", 0, state -> {
                            if (e.swinging) {
                                return state.setAndContinue(DefaultAnimations.ATTACK_STRIKE);
                            }

                            state.getController().forceAnimationReset();

                            return PlayState.STOP;
                        }));
                    })
                    .addGoal((g,e)-> {
                        g.addGoal(2, new JumpOverBlockGoal(e));
                        g.addGoal(3, new MeleeAttackGoal(e,  0.8f, true));
                        g.addGoal(7, new WaterAvoidingRandomStrollGoal(e, 1.0));
                        g.addGoal(8, new LookAtPlayerGoal(e, Player.class, 6));
                    });

    public static Supplier<AttributeBuilder> HAT_SPORE_ZOMBIE_BUILDER =
            ()->new LandMonsterPrefab(114,4,19,60,0.6f,0.72f).getPrefab()
                    .setMovementSpeed(0.08f)
                    .setSpawnWithoutLight()
                    .setDeathSound(TESounds.TR_ZOMBIE_DEATH)
                    .addTarget((t,e)-> {
                        t.addGoal(1,new AccelerateOnSeeingGoal(e,0.25f));
                        t.addGoal(2, new NearestAttackableTargetGoal<>(e, Player.class,false, LivingEntity::canBeSeenAsEnemy));

                    })
                    .setController((c,e)->{
                        c.add(genericWalkRunIdleController(e));
                        c.add(new AnimationController<>(e, "Attack", 0, state -> {
                            if (e.swinging) {
                                return state.setAndContinue(DefaultAnimations.ATTACK_STRIKE);
                            }

                            state.getController().forceAnimationReset();

                            return PlayState.STOP;
                        }));
                    })
                    .addGoal((g,e)-> {
                        g.addGoal(2, new JumpOverBlockGoal(e));
                        g.addGoal(3, new MeleeAttackGoal(e,  0.8f, true));
                        g.addGoal(7, new WaterAvoidingRandomStrollGoal(e, 1.0));
                        g.addGoal(8, new LookAtPlayerGoal(e, Player.class, 6));
                    });


    public static Supplier<AttributeBuilder> BLOOD_TUMORS =
            ()->new LandMonsterPrefab(1,0,0,0,0,0,0).getPrefab()
                    .setSafeFall(80)
                    .setNoAttachAttack()
                    .setAttackDamage((int) (Math.random() * 60 + 100))
                    .setTicker(e->{
                        if(!e.level().isClientSide && e.isAlive() && e.tickCount == e.getAttributeValue(Attributes.ATTACK_DAMAGE)){
                            List<EntityType<? extends Entity>> entities = List.of(TEMonsterEntities.BLOOD_CRAWLER.get(), TEMonsterEntities.FACE_MONSTER.get(), TEMonsterEntities.CRIMSON_KEMERA.get());
                            Entity summon = entities.get(e.getRandom().nextIntBetweenInclusive(0,entities.size()-1)).create(e.level());
                            if(summon!=null) {
                                summon.setPos(e.getX(), e.getY(), e.getZ());
                                summon.setDeltaMovement(new Vec3(0, 0.4f, 0));
                                e.level().addFreshEntity(summon);
                                e.kill();
                            }
                        }
                    })
                    .setController((c,e)->{
                        c.add(DefaultAnimations.genericIdleController(e));
                    })
            ;

    public static Supplier<AttributeBuilder> BLOOD_ZOMBIE_BUILDER =
            ()->new LandMonsterPrefab(39,2,10,60,0.5f,0.1f).getPrefab()
                    .setMovementSpeed(0.15f)
                    .setDeathSound(TESounds.TR_ZOMBIE_DEATH)
                    .addTarget((t,e)-> {
                        t.addGoal(1,new AccelerateOnSeeingGoal(e,0.25f));
                        t.addGoal(2, new NearestAttackableTargetGoal<>(e, Player.class,false, LivingEntity::canBeSeenAsEnemy));

                    })
                    .setController((c,e)->{
                        c.add(genericWalkRunIdleController(e));
                        c.add(new AnimationController<>(e, "Attack", 0, state -> {
                            if (e.swinging) {
                                return state.setAndContinue(DefaultAnimations.ATTACK_STRIKE);
                            }

                            state.getController().forceAnimationReset();

                            return PlayState.STOP;
                        }));
                    })
                    .addGoal((g,e)-> {
                        g.addGoal(2, new JumpOverBlockGoal(e));
                        g.addGoal(3, new MeleeAttackGoal(e,  0.8f, true));
                        g.addGoal(7, new WaterAvoidingRandomStrollGoal(e, 1.0));
                        g.addGoal(8, new LookAtPlayerGoal(e, Player.class, 6));
                    });
    public static Supplier<AttributeBuilder> SNOW_FLINX_BUILDER =
            ()->new LandMonsterPrefab(36,3,13,60,0.1f,0.1f).getPrefab()
                    .addGoal((g,e)-> {
                        g.addGoal(2, new JumpOverBlockGoal(e));
                        g.addGoal(3, new MeleeAttackGoal(e,  0.8f, true));
                        g.addGoal(7, new WaterAvoidingRandomStrollGoal(e, 1.0));
                        g.addGoal(8, new LookAtPlayerGoal(e, Player.class, 6));
                    });



    public LandMonsterPrefab(int health,int armor,int attack,int followRange,float knockBack,float knockbackResistance) {
        this(health,armor,attack,0.3f,followRange,knockBack,knockbackResistance);
    }

    public LandMonsterPrefab(int health,int armor,int attack,float moveSpeed,int followRange,float knockBack,float knockbackResistance) {
        super(health,armor,attack,followRange,knockBack,knockbackResistance);
        modifier = b->b
                .setNavigation((e)->new GroundPathNavigation(e,e.level()))
                .setSafeFall(8)
                .setNoAttachAttack()
                .setMovementSpeed(moveSpeed)
                .addTarget((t,e)->{
                    t.addGoal(2,new HurtByTargetGoal(e, Monster.class));
                })
                .setController((c,e)->{
                    c.add(DefaultAnimations.genericWalkIdleController(e));
                })
        ;
    }

    private final Function<AttributeBuilder, AttributeBuilder> modifier;

    public AttributeBuilder getPrefab() {
        return modifier.apply(super.getPrefab());
    }

}
