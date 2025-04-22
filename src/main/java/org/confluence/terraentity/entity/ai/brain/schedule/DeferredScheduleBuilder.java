package org.confluence.terraentity.entity.ai.brain.schedule;

import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.entity.schedule.Schedule;
import net.minecraft.world.entity.schedule.ScheduleBuilder;

import java.util.function.Supplier;

public class DeferredScheduleBuilder {
    ScheduleBuilder builder;
    public DeferredScheduleBuilder(Schedule schedule) {
        builder = new ScheduleBuilder(schedule);
    }
    // 改成函数接口防止提前初始化
    public DeferredScheduleBuilder changeActivityAt(int duration, Supplier<Activity> activity) {
        this.builder.changeActivityAt(duration, activity.get());
        return this;
    }

    public Schedule build(){
        return this.builder.build();
    }
}
