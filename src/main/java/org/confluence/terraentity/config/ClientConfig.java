package org.confluence.terraentity.config;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.common.ModConfigSpec;

@OnlyIn(Dist.CLIENT)
public class ClientConfig {

    public static ModConfigSpec.ConfigValue<Integer> BossBarStyle;
    public static ModConfigSpec.ConfigValue<Integer> BossBarNumberOffsetX;
    public static ModConfigSpec.ConfigValue<Integer> BossBarNumberOffsetY;

    public static ModConfigSpec.BooleanValue GENERATE_WHIP_PARTICLE;

    public static ModConfigSpec.BooleanValue ENABLE_NON_SPIDER_MODEL;
    public static ModConfigSpec SPEC;
    public static int bossBarStyle;

    public static void load(){
        bossBarStyle = BossBarStyle.get();
    }

    public static ModConfigSpec init(){
        final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
        BossBarStyle  = BUILDER
                .comment("Boss Bar Style.")
                .comment("0: Default, 1: Still Style, 2: Dynamic Style")
                .defineInRange("boss_bar_style", 1, 0, 2);
        BossBarNumberOffsetX = BUILDER
                .comment("Boss Bar Number Offset X.")
                .defineInRange("boss_bar_number_offset_x", 0, -150, 300);
        BossBarNumberOffsetY = BUILDER
                .comment("Boss Bar Number Offset Y.")
                .defineInRange("boss_bar_number_offset_y", 0, -30, 200);
        GENERATE_WHIP_PARTICLE = BUILDER
                .comment("Generate Whip Particle.")
                .define("generate_whip_particle", true);
        ENABLE_NON_SPIDER_MODEL = BUILDER.define("enableNonSpiderModel", false);
        SPEC = BUILDER.build();
        return SPEC;
    }

}
