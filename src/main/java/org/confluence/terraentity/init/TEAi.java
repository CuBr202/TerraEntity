package org.confluence.terraentity.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.entity.schedule.Schedule;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.IModBusEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.entity.ai.brain.schedule.DeferredScheduleBuilder;
import org.confluence.terraentity.entity.ai.brain.sensor.NPCHostilesSensor;
import org.confluence.terraentity.entity.ai.brain.sensor.NPCNearbyOthersSensor;
import org.confluence.terraentity.entity.ai.brain.sensor.NPCNearestVisibleAllianceSensor;
import org.confluence.terraentity.entity.ai.brain.sensor.NPCNurseTargetSensor;
import org.confluence.terraentity.entity.npc.AbstractTerraNPC;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class TEAi {
    public static final DeferredRegister<Schedule> SCHEDULES = DeferredRegister.create(ForgeRegistries.SCHEDULES, TerraEntity.MODID);
    public static final DeferredRegister<SensorType<?>> SENSORS = DeferredRegister.create(ForgeRegistries.SENSOR_TYPES, TerraEntity.MODID);
    public static final DeferredRegister<MemoryModuleType<?>> MEMORY_MODULES = DeferredRegister.create(Registries.MEMORY_MODULE_TYPE, TerraEntity.MODID);
//    public static final DeferredRegister<Activity> ACTIVITIES = DeferredRegister.create(Registries.ACTIVITY, TerraEntity.MODID);


    public static class Activities{
        public static final DeferredRegister<Activity> ACTIVITY = DeferredRegister.create(ForgeRegistries.ACTIVITIES, TerraEntity.MODID);

        public static Supplier<Activity> STAY_HOME = ()->Activity.REST; // 会被schedule提前访问
        public static RegistryObject<Activity> RANGE_ATTACK = registerActivity("range_attack");

        private static RegistryObject<Activity> registerActivity(String key) {
            return ACTIVITY.register(key, () -> new Activity(key));
        }
        public static void register(IEventBus bus){
            ACTIVITY.register(bus);
        }
    }

    public static class MemoryModules {
        public static Supplier<MemoryModuleType<LivingEntity>> NEAREST_VISIBLE_ALLIANCE = MEMORY_MODULES.register("nearest_visible_alliance", () -> new MemoryModuleType<>(Optional.empty()));
        public static Supplier<MemoryModuleType<LivingEntity>> NEAREST_VISIBLE_ALLIANCE_NURSE_TARGET = MEMORY_MODULES.register("nearest_visible_alliance_nurse_target", () -> new MemoryModuleType<>(Optional.empty()));
        public static Supplier<MemoryModuleType<List<AbstractTerraNPC>>> NEARBY_NPC = MEMORY_MODULES.register("nearby_npc", () -> new MemoryModuleType<>(Optional.empty()));

        public static void register(IEventBus bus){
            MEMORY_MODULES.register(bus);
        }
    }


    public static class Schedules{
        public static Supplier<Schedule> NPC_SCHEDULE = SCHEDULES.register("npc_schedule", ()->new DeferredScheduleBuilder(new Schedule())
                .changeActivityAt(0, ()->Activity.WORK)
                .changeActivityAt(12000, Activities.STAY_HOME)
                .build());
        public static void register(IEventBus bus){
            SCHEDULES.register(bus);
        }

    }


    public static class Sensors{
        public static Supplier<SensorType<NPCHostilesSensor<AbstractTerraNPC>>> NPC_HOSTILES_SENSOR = SENSORS.register("npc_hostiles_sensor", ()-> new SensorType<>(() -> new NPCHostilesSensor<>(10)));
        public static Supplier<SensorType<NPCNearestVisibleAllianceSensor<AbstractTerraNPC>>> NEAREST_VISIBLE_ALLIANCE_SENSOR = SENSORS.register("nearest_visible_alliance_sensor", ()-> new SensorType<>(() -> new NPCNearestVisibleAllianceSensor<>(10)));
        public static Supplier<SensorType<NPCNurseTargetSensor<AbstractTerraNPC>>> NEAREST_NURSE_TARGET_SENSOR = SENSORS.register("nearest_nurse_target_sensor", ()-> new SensorType<>(() -> new NPCNurseTargetSensor<>(10)));
        public static Supplier<SensorType<NPCNearbyOthersSensor>> NEARBY_NPC_SENSOR = SENSORS.register("nearby_npc_sensor", ()-> new SensorType<>(() -> new NPCNearbyOthersSensor(16)));


        public static void register(IEventBus bus){
            SENSORS.register(bus);
        }
    }



    public static void register(IEventBus bus){
        Activities.register(bus);
        MemoryModules.register(bus);
        Schedules.register(bus);
        Sensors.register(bus);

    }
}
