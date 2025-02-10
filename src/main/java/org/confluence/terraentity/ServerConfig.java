package org.confluence.terraentity;


import net.minecraftforge.common.ForgeConfigSpec;

public class ServerConfig {

    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    public static ForgeConfigSpec.ConfigValue<Boolean> BOSS_CLEAR_WHEN_NO_TARGET;
    public static ForgeConfigSpec.ConfigValue<Double> BOSS_ATTRIBUTES_MULTIPLIER_HEALTH;
    public static ForgeConfigSpec.ConfigValue<Double> BOSS_ATTRIBUTES_MULTIPLIER_DAMAGE;
    public static ForgeConfigSpec.ConfigValue<Boolean> DISPLAY_SUMMON_ITEMS;

    public static ForgeConfigSpec init(){
        final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

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
        return BUILDER.build();
    }
    public static final ForgeConfigSpec SPEC = BUILDER.build();
}
