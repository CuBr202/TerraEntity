package org.confluence.terraentity.client.gui.config_container;

import com.mojang.blaze3d.pipeline.TextureTarget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.layouts.*;
import net.minecraft.client.gui.screens.*;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.client.ModRenderTypes;
import org.confluence.terraentity.client.util.ShaderUtil;
import org.confluence.terraentity.config.*;
import org.confluence.terraentity.mixinauxiliary.IShaderInstance;

@OnlyIn(Dist.CLIENT)
public class ConfigScreen extends Screen {

    private final Screen lastScreen;
    private GridLayout gridlayout;
    private GridLayout tabLayout;
    public GridLayout.RowHelper grid;
    public GridLayout.RowHelper tabGrid;
    private ConfigScreenBuilder builder;
    public TextureTarget target;
    public int tabWidth;

    public ConfigScreen(Screen screen) {
        super(Component.translatable("terra_entity.options.title"));
        this.lastScreen = screen;
    }

    protected void init() {
        this.tabWidth = (int) (width * 0.2f);
        // 加载配置项
        gridlayout = new GridLayout();
        gridlayout.defaultCellSetting().paddingHorizontal(20).paddingBottom(4).alignVerticallyMiddle().alignHorizontallyCenter();
        grid = gridlayout.createRowHelper(2);
        // 加载标签页
        tabLayout = new GridLayout();
        tabLayout.defaultCellSetting().paddingHorizontal(20).paddingBottom(4).alignVerticallyMiddle().alignHorizontallyCenter();
        tabGrid = tabLayout.createRowHelper(1);


        builder = ConfigContainerRegister.init(this);


        grid.addChild(Button.builder(CommonComponents.GUI_DONE, (p_280809_) -> {
            this.minecraft.setScreen(this.lastScreen);
        }).width(200).build(), 2, grid.newCellSettings().paddingTop(6));
        gridlayout.arrangeElements();
        FrameLayout.alignInRectangle(gridlayout, 0, this.height / 6 - 12, this.width, this.height, 0.5F, 0.0F);
        gridlayout.visitWidgets(this::addRenderableWidget);

        tabLayout.arrangeElements();
        FrameLayout.alignInRectangle(tabLayout, 0, this.height / 6 - 24, this.width, this.height, 0.5F, 0.0F);
        tabLayout.visitWidgets(this::addRenderableWidget);

        // 初始化帧缓冲
        target = new TextureTarget(minecraft.getMainRenderTarget().width, minecraft.getMainRenderTarget().height,false,true);
        target.setClearColor(0, 0, 0, 0);
        target.clear(true);
        minecraft.getMainRenderTarget().bindWrite(false);
    }

    public void removed() {
        try {
            builder.save();
            ConfigRegistry.SPEC.save();
            super.removed();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void render(GuiGraphics g, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(g);

        g.fillGradient(0, 0, this.tabWidth, this.height, 0x55000000, 0x11000000);
        gridlayout.setX(110);
        tabLayout.setX(-20);

        super.render(g, mouseX, mouseY, partialTicks);

        var opts = builder.options;
        for (ConfigOption<?, ?> opt : opts) {
//            opt.setY((int) getScrollAmount());
            gridlayout.setY(40 - (int) getScrollAmount());
            var widget = opt.widget;
            if(opt instanceof ConfigOption.Separator){
                widget.setX(200);
            }
            if (widget.isHovered() && opt.displayText != null) {
                g.renderTooltip(this.font, Component.literal(opt.displayText), mouseX, mouseY);
                g.bufferSource().endBatch();
                break;
            }
        }


        // 渲染彩色标题

        target.setClearColor(0 ,0, 0, 0);
        target.clear(false);
        target.bindWrite(true);

        g.pose().pushPose();
        g.pose().translate(0, -getScrollAmount() ,0);
        g.drawCenteredString(this.font, this.title, this.width / 2, 15, 0x12a2c6);
        g.pose().popPose();

        minecraft.getMainRenderTarget().bindWrite(true);
//        target.blitToScreen(minecraft.getWindow().getWidth(),minecraft.getWindow().getHeight(),false);
        ShaderUtil.blitScreen( ModRenderTypes.Shaders.floatBarShader, shader->{
            // 流动速度
            float speed = 0.005f;
            ((IShaderInstance) shader).getTerra_entity$Time().set(System.currentTimeMillis() % 100000 * speed);
            // 噪声强度
            ((IShaderInstance) shader).getTerra_entity$Radius().set(0.5f);
            shader.COLOR_MODULATOR.set(0.5f,0.6f,1f,1f);
            shader.setSampler("Sampler0", target);
            shader.setSampler("Sampler1", Minecraft.getInstance().getTextureManager().getTexture(TerraEntity.space("textures/gui/noise.png")));
        });


    }



    public void onClose() {
        this.minecraft.setScreen(this.lastScreen instanceof PauseScreen ? null : this.lastScreen);
    }




    private double scrollAmount;
    protected final int itemHeight = 60;

    public int getMaxScroll() {
        return Math.max(0, 200);
    }

    public double getScrollAmount() {
        return this.scrollAmount;
    }

    public void setScrollAmount(double pScroll) {
        this.scrollAmount = Mth.clamp(pScroll, 0.0, this.getMaxScroll());
    }


    public boolean mouseScrolled(double pMouseX, double pMouseY, double pDelta) {
        this.setScrollAmount(this.getScrollAmount() - pDelta * (double)this.itemHeight / 2.0);
        return true;
    }

}
