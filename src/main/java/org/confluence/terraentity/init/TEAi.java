package org.confluence.terraentity.init;

import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.entity.schedule.Schedule;
import net.minecraft.world.entity.schedule.ScheduleBuilder;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.confluence.terraentity.TerraEntity;

import java.util.function.Supplier;

public class TEAi {
    public static final DeferredRegister<Schedule> SCHEDULE = DeferredRegister.create(ForgeRegistries.SCHEDULES, TerraEntity.MODID);

    public static class Activities{
        public static final DeferredRegister<Activity> ACTIVITY = DeferredRegister.create(ForgeRegistries.ACTIVITIES, TerraEntity.MODID);

        public static RegistryObject<Activity> STAY_HOME = registerActivity("stay_home");

        private static RegistryObject<Activity> registerActivity(String key) {
            return ACTIVITY.register(key, () -> new Activity(key));
        }
    }

    public static Supplier<Schedule> NPC_SCHEDULE = SCHEDULE.register("npc_schedule", ()->new ScheduleBuilder(new Schedule())
            .changeActivityAt(0, Activity.WORK)
            .changeActivityAt(12000, Activity.REST)
            .build());



    public static void register(IEventBus bus){
        Activities.ACTIVITY.register(bus);
        SCHEDULE.register(bus);
    }
}
