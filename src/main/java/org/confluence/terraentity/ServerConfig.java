package org.confluence.terraentity;

import net.neoforged.neoforge.common.ModConfigSpec;

public class ServerConfig {
    public static ModConfigSpec.ConfigValue<Boolean> BOSS_CLEAR_WHEN_NO_TARGET;
    public static ModConfigSpec.ConfigValue<Double> BOSS_ATTRIBUTES_MULTIPLIER_HEALTH;
    public static ModConfigSpec.ConfigValue<Double> BOSS_ATTRIBUTES_MULTIPLIER_DAMAGE ;

    public static ModConfigSpec init(){
        final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

        BOSS_CLEAR_WHEN_NO_TARGET = BUILDER
                .comment("When a boss has no target, should it be cleared?")
                .define("boss_clear_when_no_target", false);
        BOSS_ATTRIBUTES_MULTIPLIER_HEALTH = BUILDER
                .comment("Multiplier for boss attributes health.")
                .defineInRange("boss_attributes_multiplier_health", 0.5F, 0.0625f, 10f);
        BOSS_ATTRIBUTES_MULTIPLIER_DAMAGE = BUILDER
                .comment("Multiplier for boss attributes damage.")
                .defineInRange("boss_attributes_multiplier_damage", 0.7F, 0.0625f, 10f);

        return BUILDER.build();
    }
}
