package org.confluence.terraentity.client.gui.config_container;

import com.github.edg_thexu.cafelib.client.gui.config_container.ConfigScreen;
import com.github.edg_thexu.cafelib.client.gui.config_container.ConfigScreenBuilder;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.config.ClientConfig;
import org.confluence.terraentity.config.ServerConfig;

//@SuppressWarnings("all")
public class ConfigContainerRegister {

    public static ConfigScreenBuilder init(ConfigScreen screen){
        ConfigScreenBuilder builder;
        builder = ConfigScreenBuilder.builder(screen);

        builder.addTab(TerraEntity.MODID, "client",40);

        builder.addIntSliderEditBox(ClientConfig.BossBarStyle,0 , 2)
                .comment("0: Default, 1: Still Style, 2: Dynamic Style");
        builder.addIntSliderEditBox(ClientConfig.BossBarNumberOffsetX,-150 , 300)
                .comment("Boss Bar Number Offset X.");
        builder.addIntSliderEditBox(ClientConfig.BossBarNumberOffsetY,-100 , 200)
                .comment("Boss Bar Number Offset Y.");
        builder.addCheckBox(ClientConfig.GENERATE_PROJECTILE_PARTICLE)
                .comment("Generate Whip Particle.");

        builder.addTab(TerraEntity.MODID, "server",165);

        builder.addDoubleEditBox(ServerConfig.BOSS_ATTRIBUTES_MULTIPLIER_DAMAGE)
                .comment("0.0625 ~ 10.0");
        builder.addDoubleEditBox(ServerConfig.BOSS_ATTRIBUTES_MULTIPLIER_HEALTH)
                .comment("0.0625 ~ 10.0");
        builder.addDoubleEditBox(ServerConfig.MONSTER_ATTRIBUTES_MULTIPLIER_DAMAGE)
                .comment("0.0625 ~ 100.0");
        builder.addDoubleEditBox(ServerConfig.MONSTER_ATTRIBUTES_MULTIPLIER_HEALTH)
                .comment("0.0625 ~ 100.0");
        builder.addCheckBox(ServerConfig.ENHANCE_ALL_MONSTER);
        builder.addCheckBox(ServerConfig.DISABLE_BUILTIN_MODIFIER)
                        .comment("For kjs modify");
        builder.addCheckBox(ServerConfig.BOSS_CLEAR_WHEN_NO_TARGET);
        builder.addCheckBox(ServerConfig.BOSS_NO_PHYSICS);
        builder.addCheckBox(ServerConfig.BOSS_LEAVE_ON_DAY);


        builder.build(TerraEntity.MODID);
        return builder;
    }

    @SuppressWarnings("removal")
    public static void registerModsPage(final InterModEnqueueEvent event) {
        event.enqueueWork(() -> {
            if (FMLEnvironment.dist == Dist.CLIENT) {
                ModLoadingContext.get().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class, () ->
                        new ConfigScreenHandler.ConfigScreenFactory((client, parent) -> new TEConfigScreen(parent, ConfigContainerRegister::init)));
            }
        });
    }


}
