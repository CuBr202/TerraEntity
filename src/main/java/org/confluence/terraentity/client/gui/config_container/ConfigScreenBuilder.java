package org.confluence.terraentity.client.gui.config_container;

import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.gui.widget.ForgeSlider;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.ArrayList;
import java.util.List;


@OnlyIn(Dist.CLIENT)
public class ConfigScreenBuilder {
    public List<ConfigOption<?,?>> options = new ArrayList<>();
    private ConfigOption<?,?> lastOption;
    ConfigScreen screen;

    ConfigScreenBuilder( ConfigScreen screen){
        this.screen = screen;
    }



    public static ConfigScreenBuilder builder( ConfigScreen screen) {
        return new ConfigScreenBuilder(screen);
    }

    public ConfigScreenBuilder comment(String comment){
        lastOption.displayText = comment;
        return this;
    }

    public void build() {
        options.forEach(op->{
            op.label = new StringWidget(Component.translatable(op.name), screen.getMinecraft().font);
            screen.grid.addChild(op.label);
            screen.grid.addChild(op.widget);
        });
    }

    public void save(){
        options.forEach(ConfigOption::onSave);
    }


    public ConfigScreenBuilder add(ConfigOption option) {
        options.add(option);
        lastOption = option;
        return this;
    }

    public ConfigScreenBuilder addIntEditBox(ForgeConfigSpec.ConfigValue<Integer> option) {
        var editBox = new EditBox(screen.getMinecraft().font, 0,0,100,20, Component.empty());
        var opt = new ConfigOption.IntEditBoxModifier(option, editBox);
        options.add(opt);
        lastOption = opt;
        return this;
    }
    public ConfigScreenBuilder addDoubleEditBox(ForgeConfigSpec.ConfigValue<Double> option) {
        var editBox = new EditBox(screen.getMinecraft().font, 0,0,100,20, Component.empty());
        var opt = new ConfigOption.DoubleEditBoxModifier(option, editBox);
        options.add(opt);
        lastOption = opt;
        return this;
    }

    public ConfigScreenBuilder addIntSliderEditBox(ForgeConfigSpec.ConfigValue<Integer> option, int min, int max) {
        var slider = new ForgeSlider(0,0,100,20,Component.literal("value: "), Component.empty(), min, max, option.get(), true);
        var opt = new ConfigOption.IntSliderModifier(option, slider);
//        opt.displayText = "%d ~ %d".formatted(min, max);
        options.add(opt);
        lastOption = opt;
        return this;
    }
    public ConfigScreenBuilder addCheckBox(ForgeConfigSpec.ConfigValue<Boolean> option) {
        var opt = new ConfigOption.BooleanToggleModifier(option, new Checkbox(0,0,100,20,Component.empty(), option.get()));
        options.add(opt);
        lastOption = opt;
        return this;
    }

}
