package org.confluence.terraentity.config;


import net.minecraftforge.common.ForgeConfigSpec;

public class ServerConfig {
    public static ForgeConfigSpec.ConfigValue<Boolean> BOSS_CLEAR_WHEN_NO_TARGET;
    public static ForgeConfigSpec.ConfigValue<Double> BOSS_ATTRIBUTES_MULTIPLIER_HEALTH;
    public static ForgeConfigSpec.ConfigValue<Double> BOSS_ATTRIBUTES_MULTIPLIER_DAMAGE;
    public static ForgeConfigSpec.ConfigValue<Boolean> DISPLAY_SUMMON_ITEMS;

    public static ForgeConfigSpec.ConfigValue<Boolean> ENHANCE_ALL_MONSTER;
    public static ForgeConfigSpec.ConfigValue<Double> MONSTER_ATTRIBUTES_MULTIPLIER_HEALTH;
    public static ForgeConfigSpec.ConfigValue<Double> MONSTER_ATTRIBUTES_MULTIPLIER_DAMAGE ;


    public static ForgeConfigSpec.Builder init(ForgeConfigSpec.Builder BUILDER){
        BUILDER.push("server");


        BOSS_CLEAR_WHEN_NO_TARGET = BUILDER
                .comment("When a boss has no target, should it be cleared?")
                .define("boss_clear_when_no_target", false);
        BOSS_ATTRIBUTES_MULTIPLIER_HEALTH = BUILDER
                .comment("Multiplier for boss attributes health.")
                .defineInRange("boss_attributes_multiplier_health", 0.5F, 0.0625f, 10f);
        BOSS_ATTRIBUTES_MULTIPLIER_DAMAGE = BUILDER
                .comment("Multiplier for boss attributes damage.")
                .defineInRange("boss_attributes_multiplier_damage", 0.7F, 0.0625f, 10f);

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


        BUILDER.pop();
        return BUILDER;
    }
}
