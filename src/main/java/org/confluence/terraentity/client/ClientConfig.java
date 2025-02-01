package org.confluence.terraentity.client;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.common.ModConfigSpec;

@OnlyIn(Dist.CLIENT)
public class ClientConfig {

    public static ModConfigSpec.ConfigValue<Integer> BossBarStyle;
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
                .defineInRange("boss_bar_style", 0, 0, 2);
        SPEC = BUILDER.build();
        return SPEC;
    }

}
