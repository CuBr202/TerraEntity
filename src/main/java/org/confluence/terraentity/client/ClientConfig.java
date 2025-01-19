package org.confluence.terraentity.client;


import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;

import static org.confluence.terraentity.TerraEntity.MODID;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientConfig {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    public static ForgeConfigSpec.ConfigValue<Integer> BossBarStyle  = BUILDER.comment("boss bar style").define("boss_bar_style", 0);

    public static int bossBarStyle;

    public static void load(){
        bossBarStyle = BossBarStyle.get();
    }
    public static final ForgeConfigSpec SPEC = BUILDER.build();

}
