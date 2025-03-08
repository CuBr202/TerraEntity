package org.confluence.terraentity.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class ServerConfig {
    public static ModConfigSpec.ConfigValue<Boolean> BOSS_CLEAR_WHEN_NO_TARGET;
    public static ModConfigSpec.ConfigValue<Double> BOSS_ATTRIBUTES_MULTIPLIER_HEALTH;
    public static ModConfigSpec.ConfigValue<Double> BOSS_ATTRIBUTES_MULTIPLIER_DAMAGE ;
    public static ModConfigSpec.ConfigValue<Boolean> BOSS_NO_PHYSICS;
    public static ModConfigSpec.ConfigValue<Boolean> BOSS_LEAVE_ON_DAY;



    public static ModConfigSpec.ConfigValue<Boolean> DISPLAY_SUMMON_ITEMS;

    public static ModConfigSpec.ConfigValue<Boolean> ENHANCE_ALL_MONSTER;
    public static ModConfigSpec.ConfigValue<Double> MONSTER_ATTRIBUTES_MULTIPLIER_HEALTH;
    public static ModConfigSpec.ConfigValue<Double> MONSTER_ATTRIBUTES_MULTIPLIER_DAMAGE ;


    public static ModConfigSpec init(){
        final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

        BOSS_CLEAR_WHEN_NO_TARGET = BUILDER
                .comment("When a boss has no target, should it be cleared?")
                .define("boss_clear_when_no_target", false);
        BOSS_ATTRIBUTES_MULTIPLIER_HEALTH = BUILDER
                .comment("Multiplier for boss attributes health.")
                .defineInRange("boss_attributes_multiplier_health", 1F, 0.0625f, 10f);
        BOSS_ATTRIBUTES_MULTIPLIER_DAMAGE = BUILDER
                .comment("Multiplier for boss attributes damage.")
                .defineInRange("boss_attributes_multiplier_damage", 1F, 0.0625f, 10f);

        BOSS_NO_PHYSICS = BUILDER
                .comment("Should the boss have no physics? Only for some bosses.")
                .define("boss_no_physics", true);

        BOSS_LEAVE_ON_DAY = BUILDER
                .comment("Should the boss leave on day? Only for some bosses.")
                .define("boss_leave_on_day", false);

        DISPLAY_SUMMON_ITEMS = BUILDER
                .comment("Should summon items be displayed in this mod?")
                .define("display_summon_items", true);

        ENHANCE_ALL_MONSTER = BUILDER
                .comment("Should all monsters be enhanced?\nIf false, only specific monsters in this mod.")
                .define("enhance_all_monster", false);

        MONSTER_ATTRIBUTES_MULTIPLIER_HEALTH = BUILDER
                .comment("Multiplier for monster attributes health.")
                .defineInRange("monster_attributes_multiplier_health", 1F, 0.0625f, 100f);
        MONSTER_ATTRIBUTES_MULTIPLIER_DAMAGE = BUILDER
                .comment("Multiplier for monster attributes damage.")
                .defineInRange("monster_attributes_multiplier_damage", 1F, 0.0625f, 100f);

        return BUILDER.build();
    }
}
