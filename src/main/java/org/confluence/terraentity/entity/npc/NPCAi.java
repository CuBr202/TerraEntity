//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.confluence.terraentity.entity.npc;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;

import java.util.Optional;

import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.*;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.phys.Vec3;
import org.confluence.terraentity.entity.ai.behavior.NpcHomeNearbyStroll;
import org.confluence.terraentity.init.TEAi;

public class NPCAi {

    protected static final ImmutableList<SensorType<? extends Sensor<? super AbstractTerraNPC>>> SENSOR_TYPES = ImmutableList.of(
            SensorType.NEAREST_LIVING_ENTITIES,
            SensorType.HURT_BY,
            SensorType.FROG_ATTACKABLES,
            SensorType.FROG_TEMPTATIONS,
            SensorType.IS_IN_WATER
    );

    protected static final ImmutableList<MemoryModuleType<?>> MEMORY_TYPES = ImmutableList.of(
            MemoryModuleType.LOOK_TARGET,
            MemoryModuleType.NEAREST_LIVING_ENTITIES,
            MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES,
            MemoryModuleType.WALK_TARGET,
            MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE,
            MemoryModuleType.PATH,
            MemoryModuleType.BREED_TARGET,
            MemoryModuleType.LONG_JUMP_COOLDOWN_TICKS,
            MemoryModuleType.LONG_JUMP_MID_JUMP,
            MemoryModuleType.ATTACK_TARGET,
            MemoryModuleType.TEMPTING_PLAYER,
            MemoryModuleType.TEMPTATION_COOLDOWN_TICKS,
            MemoryModuleType.IS_TEMPTED,
            MemoryModuleType.HURT_BY,
            MemoryModuleType.HURT_BY_ENTITY,
            MemoryModuleType.NEAREST_ATTACKABLE,
            MemoryModuleType.IS_IN_WATER,
            MemoryModuleType.IS_PREGNANT,
            MemoryModuleType.IS_PANICKING,
            MemoryModuleType.UNREACHABLE_TONGUE_TARGETS,
            MemoryModuleType.HOME,
            MemoryModuleType.LAST_WOKEN
    );

    protected static Brain.Provider<AbstractTerraNPC> brainProvider() {
        return Brain.provider(MEMORY_TYPES, SENSOR_TYPES);
    }

    protected static Brain<?> makeBrain(Brain<AbstractTerraNPC> brain) {
        brain.setSchedule(TEAi.NPC_SCHEDULE.get());
        initCoreActivity(brain);
        initIdleActivity(brain);

        brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
        brain.setDefaultActivity(Activity.IDLE);
        brain.useDefaultActivity();
        return brain;
    }

    private static void initCoreActivity(Brain<AbstractTerraNPC> brain) {
        brain.addActivity(Activity.CORE, 0,
                ImmutableList.of(
                        new Swim(0.8F),
                        new LookAtTargetSink(45, 90),
                        new MoveToTargetSink(),
                        new CountDownCooldownTicks(MemoryModuleType.TEMPTATION_COOLDOWN_TICKS),
                        new CountDownCooldownTicks(MemoryModuleType.LONG_JUMP_COOLDOWN_TICKS),
                        NPCHouseBehaviors.FindHouse(MemoryModuleType.HOME) // 寻找家

                )
        );

    }

    private static void initIdleActivity(Brain<AbstractTerraNPC> brain) {
        brain.addActivity(Activity.IDLE, getIdlePackage(1.0F));
        brain.addActivity(TEAi.Activities.STAY_HOME, getRestPackage(1.0F));
    }

    /**
     * 空闲状态的行为包
     * @param speedModifier 速度
     */
    public static ImmutableList<Pair<Integer, ? extends BehaviorControl<? super AbstractTerraNPC>>> getIdlePackage( float speedModifier) {
        return ImmutableList.of(
                Pair.of(2, new RunOne<>(
                        ImmutableList.of(
                                Pair.of(InteractWith.of(EntityType.CAT, 8, MemoryModuleType.INTERACTION_TARGET, speedModifier, 2), 1),
                                Pair.of(VillageBoundRandomStroll.create(speedModifier), 1),
                                Pair.of(SetWalkTargetFromLookTarget.create(speedModifier, 2), 1),
                                Pair.of(new JumpOnBed(speedModifier), 1),
                                Pair.of(new DoNothing(30, 60), 1)))),
                Pair.of(0, SetEntityLookTargetSometimes.create(EntityType.PLAYER, 6.0F, UniformInt.of(30, 60))),
                Pair.of(99, UpdateActivityFromSchedule.create()));
    }


    /**
     * 休息状态的行为包
     * @param speedModifier 速度
     */
    public static ImmutableList<Pair<Integer, ? extends BehaviorControl<? super AbstractTerraNPC>>> getRestPackage(float speedModifier) {
        return ImmutableList.of(
                Pair.of(2, createPoi(MemoryModuleType.HOME, speedModifier, 5, 150, 1200)),
//                Pair.of(3, new SleepInBed()),

                Pair.of(5, new RunOne<>( // 离开家时尝试回到家
                        ImmutableMap.of(MemoryModuleType.HOME, MemoryStatus.VALUE_ABSENT),
                        ImmutableList.of(
                                Pair.of(NPCHouseBehaviors.walkToHouse(speedModifier), 1), // 走向家
                                Pair.of(InsideBrownianWalk.create(speedModifier), 4), // 随机运动

//                                Pair.of(GoToClosestVillage.create(speedModifier, 4), 2),
                                Pair.of(new DoNothing(20, 40), 2)))),
                Pair.of(5, new RunOne<>( // 视觉感知
                        ImmutableList.of(
                                Pair.of(SetEntityLookTarget.create(EntityType.VILLAGER, 8.0F), 2),
                                Pair.of(SetEntityLookTarget.create(EntityType.PLAYER, 8.0F), 2),
                                Pair.of(new DoNothing(30, 60), 8)
                        )
                )),
                Pair.of(5, new RunOne<>( // 在家里时随机走动
                        ImmutableList.of(
                                Pair.of(InteractWith.of(EntityType.CAT, 8, MemoryModuleType.INTERACTION_TARGET, speedModifier, 2), 1),
//                                Pair.of(VillageBoundRandomStroll.create(speedModifier), 1),
                                Pair.of(NpcHomeNearbyStroll.create(speedModifier), 1),

                                Pair.of(new DoNothing(30, 60), 1)))),
                Pair.of(99, UpdateActivityFromSchedule.create()));
    }

    /**
     * 创建寻家行为
     * @param blockTargetMemory 家的位置
     * @param speedModifier 速度
     * @param closeEnoughDist 到达距离
     * @param tooFarDistance  最大寻家距离
     * @param tooLongUnreachableDuration 寻家超时
     */
    public static OneShot<AbstractTerraNPC> createPoi(MemoryModuleType<GlobalPos> blockTargetMemory, float speedModifier, int closeEnoughDist, int tooFarDistance, int tooLongUnreachableDuration) {
        return BehaviorBuilder.create((instance) -> instance.group(
                instance.registered(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE),
                instance.absent(MemoryModuleType.WALK_TARGET),
                instance.present(blockTargetMemory)
        ).apply(instance, (Length , walkTarget, pos) -> (serverLevel, npc, length) -> {
            GlobalPos globalpos = instance.get(pos);
            Optional<Long> optional = instance.tryGet(Length);
            if (globalpos.dimension() != serverLevel.dimension() || optional.isPresent() && serverLevel.getGameTime() - optional.get() > (long)tooLongUnreachableDuration) {
                npc.releasePoi(blockTargetMemory);
                pos.erase();
                Length.set(length);
            } else if (globalpos.pos().distManhattan(npc.blockPosition()) > tooFarDistance) {
                Vec3 vec3 = null;
                int i = 0;

                while(vec3 == null || BlockPos.containing(vec3).distManhattan(npc.blockPosition()) > tooFarDistance) {
                    vec3 = DefaultRandomPos.getPosTowards(npc, 15, 7, Vec3.atBottomCenterOf(globalpos.pos()), 1.5707963705062866);
                    ++i;
                    if (i == 1000) {
                        npc.releasePoi(blockTargetMemory);
                        pos.erase();
                        Length.set(length);
                        return true;
                    }
                }

                walkTarget.set(new WalkTarget(vec3, speedModifier, closeEnoughDist));
            } else if (globalpos.pos().distManhattan(npc.blockPosition()) > closeEnoughDist) {
                walkTarget.set(new WalkTarget(globalpos.pos(), speedModifier, closeEnoughDist));
            }

            return true;
        }));
    }

}
