package org.confluence.terraentity.client.gui.config_container;

import net.minecraft.client.gui.components.*;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.gui.widget.ForgeSlider;
import net.minecraftforge.common.ForgeConfigSpec;

@OnlyIn(Dist.CLIENT)
public abstract class ConfigOption<V, W extends AbstractWidget> {
    public String name;
    public ForgeConfigSpec.ConfigValue<V> value;
    public W widget;
    public StringWidget label;
    public String displayText;

    public ConfigOption(ForgeConfigSpec.ConfigValue<V> value, W widget) {
//            this.name = String.join(".", value.getPath().stream().flatMap(s -> Arrays.stream(s.split("\\.")).collect(Collectors.toList())));
        this.name = "terra_entity.configuration.";
        value.getPath().forEach(s -> this.name += s + ".");
        this.name = this.name.substring(0, this.name.length() - 1);
        this.value = value;
        this.widget = widget;
        this.onInit();
    }

    public abstract void onSave();

    public abstract void onInit() ;

    public void setDisplayText(String displayText) {
        this.displayText = displayText;
    }

    // 整数编辑框
    public static class IntEditBoxModifier extends ConfigOption<Integer, EditBox> {
        public IntEditBoxModifier(ForgeConfigSpec.ConfigValue<Integer> value, EditBox widget) {
            super(value, widget);
        }

        @Override
        public void onSave() {
            value.set(Integer.parseInt(widget.getValue()));
        }

        @Override
        public void onInit() {
            widget.setValue(value.get().toString());
        }
    }

    // 整数滑块
    public static class IntSliderModifier extends ConfigOption<Integer, ForgeSlider> {
        public IntSliderModifier(ForgeConfigSpec.ConfigValue<Integer> value, ForgeSlider widget) {
            super(value, widget);
        }

        @Override
        public void onSave() {
            value.set(widget.getValueInt());
        }

        @Override
        public void onInit() {
            widget.setValue(value.get());
        }
    }

    // 浮点数编辑框
    public static class DoubleEditBoxModifier extends ConfigOption<Double, EditBox> {
        public DoubleEditBoxModifier(ForgeConfigSpec.ConfigValue<Double> value, EditBox widget) {
            super(value, widget);
        }

        @Override
        public void onSave() {
            value.set(Double.parseDouble(widget.getValue()));
        }

        @Override
        public void onInit() {
            widget.setValue(value.get().toString());
        }
    }

    // 选择框
    public static class BooleanToggleModifier extends ConfigOption<Boolean, Checkbox> {
        public BooleanToggleModifier(ForgeConfigSpec.ConfigValue<Boolean> value, Checkbox widget) {
            super(value, widget);
        }

        @Override
        public void onSave() {
            value.set(widget.selected());
        }

        @Override
        public void onInit() {
            if(value.get())
                widget.selected();
        }
    }




}
