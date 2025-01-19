package org.confluence.terraentity.client.gui;

import net.minecraft.client.Options;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.layouts.*;
import net.minecraft.client.gui.screens.*;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.gui.widget.ForgeSlider;
import org.confluence.terraentity.client.ClientConfig;

import java.io.File;

@OnlyIn(Dist.CLIENT)
public class ConfigScreen extends Screen {


    private final Screen lastScreen;

    ForgeSlider styleSlider;

    public ConfigScreen(Screen screen) {
        super(Component.translatable("terra_entity.options.title"));
        this.lastScreen = screen;
    }

    protected void init() {
        GridLayout gridlayout = new GridLayout();
        gridlayout.defaultCellSetting().paddingHorizontal(5).paddingBottom(4).alignHorizontallyCenter();
        GridLayout.RowHelper gridlayout$rowhelper = gridlayout.createRowHelper(2);
//        gridlayout$rowhelper.addChild(this.options.fov().createButton(this.minecraft.options, 0, 0, 150));
        Options options = new Options(minecraft,new File("config/terraentity/options.txt"));

        styleSlider = new ForgeSlider(0,0,100,20,Component.literal("style:   "),Component.empty(),0,1,ClientConfig.bossBarStyle,true);


        gridlayout$rowhelper.addChild(styleSlider);

//        gridlayout$rowhelper.addChild(Button.builder(Component.literal("style"),p->{}).build());

        gridlayout$rowhelper.addChild(Button.builder(CommonComponents.GUI_DONE, (p_280809_) -> {
            this.minecraft.setScreen(this.lastScreen);
        }).width(200).build(), 2, gridlayout$rowhelper.newCellSettings().paddingTop(6));
        gridlayout.arrangeElements();
        FrameLayout.alignInRectangle(gridlayout, 0, this.height / 6 - 12, this.width, this.height, 0.5F, 0.0F);
        gridlayout.visitWidgets(this::addRenderableWidget);
    }



    public void removed() {
        ClientConfig.BossBarStyle.set(styleSlider.getValueInt());
        super.removed();
    }

    public void render(GuiGraphics g, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(g);
        g.drawCenteredString(this.font, this.title, this.width / 2, 15, 16777215);
        super.render(g, mouseX, mouseY, partialTicks);
    }

    public void onClose() {
        this.minecraft.setScreen(this.lastScreen instanceof PauseScreen ? null : this.lastScreen);
    }

}
