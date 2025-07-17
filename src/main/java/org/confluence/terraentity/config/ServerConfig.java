package org.confluence.terraentity.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ServerConfig {
    public static ForgeConfigSpec.ConfigValue<Boolean> BOSS_CLEAR_WHEN_NO_TARGET;
    public static ForgeConfigSpec.ConfigValue<Double> BOSS_ATTRIBUTES_MULTIPLIER_HEALTH;
    public static ForgeConfigSpec.ConfigValue<Double> BOSS_ATTRIBUTES_MULTIPLIER_DAMAGE;
    public static ForgeConfigSpec.ConfigValue<Boolean> BOSS_NO_PHYSICS;
    public static ForgeConfigSpec.ConfigValue<Boolean> BOSS_LEAVE_ON_DAY;




    public static ForgeConfigSpec.ConfigValue<Boolean> ENHANCE_ALL_MONSTER;
    public static ForgeConfigSpec.ConfigValue<Double> MONSTER_ATTRIBUTES_MULTIPLIER_HEALTH;
    public static ForgeConfigSpec.ConfigValue<Double> MONSTER_ATTRIBUTES_MULTIPLIER_DAMAGE ;
    public static ForgeConfigSpec.ConfigValue<Boolean> DISABLE_BUILTIN_MODIFIER;

    public static ForgeConfigSpec.ConfigValue<Boolean> SPAWN_WITHOUT_LIGHT;

    public static ForgeConfigSpec.Builder init(ForgeConfigSpec.Builder BUILDER){
        BUILDER.push("server");

        BOSS_CLEAR_WHEN_NO_TARGET = BUILDER
                .comment("When a boss has no target, should it be cleared?")
                .define("boss_clear_when_no_target", true);
        BOSS_ATTRIBUTES_MULTIPLIER_HEALTH = BUILDER
                .comment("Multiplier for boss attributes health.")
                .defineInRange("boss_attributes_multiplier_health", 0.5F, 0.0625f, 10f);
        BOSS_ATTRIBUTES_MULTIPLIER_DAMAGE = BUILDER
                .comment("Multiplier for boss attributes damage.")
                .defineInRange("boss_attributes_multiplier_damage", 0.7F, 0.0625f, 10f);

        BOSS_NO_PHYSICS = BUILDER
                .comment("Should the boss have no physics? Only for some bosses.")
                .define("boss_no_physics", true);

        BOSS_LEAVE_ON_DAY = BUILDER
                .comment("Should the boss leave on day? Only for some bosses.")
                .define("boss_leave_on_day", false);

//        RESPAWN_PROTECT = BUILDER
//                .comment("Should players be protected from respawning?.If true, nearby bosses will be discard")
//                .define("respawn_protect", true);

        ENHANCE_ALL_MONSTER = BUILDER
                .comment("Should all monsters be enhanced?\nIf false, only specific monsters in this mod.")
                .define("enhance_all_monster", false);

        MONSTER_ATTRIBUTES_MULTIPLIER_HEALTH = BUILDER
                .comment("Multiplier for monster attributes health.")
                .defineInRange("monster_attributes_multiplier_health", 1F, 0.0625f, 100f);
        MONSTER_ATTRIBUTES_MULTIPLIER_DAMAGE = BUILDER
                .comment("Multiplier for monster attributes damage.")
                .defineInRange("monster_attributes_multiplier_damage", 1F, 0.0625f, 100f);
        DISABLE_BUILTIN_MODIFIER = BUILDER
                .comment("Should the built-in attributes setting be disabled for monsters?")
                .comment("For kjs modify")
                .define("disable_builtin_health_attack_modifier", false);
        SPAWN_WITHOUT_LIGHT = BUILDER
                .comment("Should monsters spawn without light?")
                .define("spawn_without_light", true);
        BUILDER.pop();
        return BUILDER;
    }
}
