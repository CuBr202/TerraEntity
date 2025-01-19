package org.confluence.terraentity;


import net.minecraftforge.common.ForgeConfigSpec;

public class Config{

    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    public static ForgeConfigSpec.ConfigValue<Boolean> BOSS_CLEAR_WHEN_NO_TARGET  = BUILDER.comment("When a boss has no target, should it be cleared?").define("boss_clear_when_no_target", false);
    public static ForgeConfigSpec.ConfigValue<Double> BOSS_ATTRIBUTES_MULTIPLIER_HEALTH  = BUILDER.comment("Multiplier for boss attributes health. > 0").define("boss_attributes_multiplier_health", 0.5D);
    public static ForgeConfigSpec.ConfigValue<Double> BOSS_ATTRIBUTES_MULTIPLIER_DAMAGE  = BUILDER.comment("Multiplier for boss attributes damage. > 0").define("boss_attributes_multiplier_damage", 0.7D);

    public static boolean bossClearWhenNoTarget;
    public static double boss_attributes_multiplier_health;
    public static double boss_attributes_multiplier_damage;



    public static void init() {
        bossClearWhenNoTarget = BOSS_CLEAR_WHEN_NO_TARGET.get();
        boss_attributes_multiplier_health = BOSS_ATTRIBUTES_MULTIPLIER_HEALTH.get();
        boss_attributes_multiplier_damage = BOSS_ATTRIBUTES_MULTIPLIER_DAMAGE.get();
    }

    public static final ForgeConfigSpec SPEC = BUILDER.build();

}
