package org.confluence.terraentity.client;


import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;

import static org.confluence.terraentity.TerraEntity.MODID;

@OnlyIn(Dist.CLIENT)
public class ClientConfig {

    public static ForgeConfigSpec.ConfigValue<Integer> BossBarStyle;
    public static ForgeConfigSpec SPEC;
    public static int bossBarStyle;

    public static void load(){
        bossBarStyle = BossBarStyle.get();
    }

    public static ForgeConfigSpec init(){
        final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
        BossBarStyle  = BUILDER
                .comment("Boss Bar Style.")
                .comment("0: Default, 1: Still Style, 2: Dynamic Style")
                .defineInRange("boss_bar_style", 0, 0, 2);
        SPEC = BUILDER.build();
        return SPEC;
    }

}
