package org.confluence.terraentity.client.gui.config_container;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.confluence.terraentity.config.ClientConfig;
import org.confluence.terraentity.config.ServerConfig;

//@SuppressWarnings("all")
public class ConfigContainerRegister {

    public static ConfigScreenBuilder init(ConfigScreen screen){
        ConfigScreenBuilder builder;
        builder = ConfigScreenBuilder.builder(screen);

        builder.addTab("client",45);

        builder.addIntSliderEditBox(ClientConfig.BossBarStyle,0 , 2)
                .comment("0: Default, 1: Still Style, 2: Dynamic Style");

        builder.addTab("server",98);

        builder.addDoubleEditBox(ServerConfig.BOSS_ATTRIBUTES_MULTIPLIER_DAMAGE)
                .comment("0.0625 ~ 10.0");
        builder.addDoubleEditBox(ServerConfig.BOSS_ATTRIBUTES_MULTIPLIER_HEALTH)
                .comment("0.0625 ~ 10.0");
        builder.addDoubleEditBox(ServerConfig.MONSTER_ATTRIBUTES_MULTIPLIER_DAMAGE)
                .comment("0.0625 ~ 100.0");
        builder.addDoubleEditBox(ServerConfig.MONSTER_ATTRIBUTES_MULTIPLIER_HEALTH)
                .comment("0.0625 ~ 100.0");
        builder.addCheckBox(ServerConfig.DISPLAY_SUMMON_ITEMS);
        builder.addCheckBox(ServerConfig.ENHANCE_ALL_MONSTER);
        builder.addCheckBox(ServerConfig.BOSS_CLEAR_WHEN_NO_TARGET);
        builder.addCheckBox(ServerConfig.BOSS_NO_PHYSICS);
        builder.addCheckBox(ServerConfig.BOSS_LEAVE_ON_DAY);


        builder.build();
        return builder;
    }

    @SuppressWarnings("removal")
    public static void registerModsPage(final InterModEnqueueEvent event) {
        event.enqueueWork(() -> {
            if (FMLEnvironment.dist == Dist.CLIENT) {
                ModLoadingContext.get().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class, () ->
                        new ConfigScreenHandler.ConfigScreenFactory((client, parent) -> new ConfigScreen(parent)));
            }
        });
    }


}
