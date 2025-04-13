package org.confluence.terraentity.init;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.entity.schedule.Schedule;
import net.minecraft.world.entity.schedule.ScheduleBuilder;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.confluence.terraentity.TerraEntity;

import java.util.function.Supplier;

public class TEAi {
    public static final DeferredRegister<Schedule> SCHEDULE = DeferredRegister.create(BuiltInRegistries.SCHEDULE, TerraEntity.MODID);

    public static class Activities{
        public static Activity STAY_HOME = registerActivity("stay_home");
        public static Activity RANGE_ATTACK = registerActivity("range_attack");

        private static Activity registerActivity(String key) {
            return Registry.register(BuiltInRegistries.ACTIVITY, key, new Activity(key));
        }
    }

    public static Supplier<Schedule> NPC_SCHEDULE = SCHEDULE.register("npc_schedule", ()->new ScheduleBuilder(new Schedule())
            .changeActivityAt(0, Activity.WORK)
            .changeActivityAt(12000, Activities.STAY_HOME)
            .build());




    private static Schedule registerSchedule(String key, Schedule schedule) {
        return Registry.register(BuiltInRegistries.SCHEDULE,  key, schedule);
    }

    public static void register(IEventBus bus){
        SCHEDULE.register(bus);

    }
}
