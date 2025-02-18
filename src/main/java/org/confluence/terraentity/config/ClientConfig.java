package org.confluence.terraentity.config;


import net.minecraftforge.common.ForgeConfigSpec;


public class ClientConfig {

    public static ForgeConfigSpec.ConfigValue<Integer> BossBarStyle;
    public static ForgeConfigSpec SPEC;

    public static ForgeConfigSpec.Builder init(ForgeConfigSpec.Builder BUILDER){
        BUILDER.push("client");

        BossBarStyle  = BUILDER
                .comment("Boss Bar Style.")
                .comment("0: Default, 1: Still Style, 2: Dynamic Style")
                .defineInRange("boss_bar_style", 0, 0, 2);
        SPEC = BUILDER.build();

        BUILDER.pop();
        return BUILDER;
    }

}
