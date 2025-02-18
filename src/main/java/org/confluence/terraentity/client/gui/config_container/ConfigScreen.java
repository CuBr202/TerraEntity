package org.confluence.terraentity.client.gui.config_container;

import com.mojang.blaze3d.pipeline.TextureTarget;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.layouts.*;
import net.minecraft.client.gui.screens.*;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.client.ModRenderTypes;
import org.confluence.terraentity.client.util.ShaderUtil;
import org.confluence.terraentity.config.*;
import org.confluence.terraentity.mixinauxiliary.IShaderInstance;
import org.joml.Matrix4f;

@OnlyIn(Dist.CLIENT)
public class ConfigScreen extends Screen {

    private final Screen lastScreen;
    private GridLayout gridlayout;
    public GridLayout.RowHelper grid;
    private ConfigScreenBuilder builder;
    public TextureTarget target;

    public ConfigScreen(Screen screen) {
        super(Component.translatable("terra_entity.options.title"));
        this.lastScreen = screen;
    }

    protected void init() {
        gridlayout = new GridLayout();
        gridlayout.defaultCellSetting().paddingHorizontal(5).paddingBottom(4).alignHorizontallyCenter();
        grid = gridlayout.createRowHelper(2);

        builder = ConfigContainerRegister.init(this);

        grid.addChild(Button.builder(CommonComponents.GUI_DONE, (p_280809_) -> {
            this.minecraft.setScreen(this.lastScreen);
        }).width(200).build(), 2, grid.newCellSettings().paddingTop(6));
        gridlayout.arrangeElements();
        FrameLayout.alignInRectangle(gridlayout, 0, this.height / 6 - 12, this.width, this.height, 0.5F, 0.0F);
        gridlayout.visitWidgets(this::addRenderableWidget);
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

        super.render(g, mouseX, mouseY, partialTicks);

        var opts = builder.options;
        for (ConfigOption<?, ?> opt : opts) {
//            opt.setY((int) getScrollAmount());
            gridlayout.setY(40 - (int) getScrollAmount());
            var widget = opt.widget;
            if (widget.isHovered() && opt.displayText != null) {
                g.renderTooltip(this.font, Component.literal(opt.displayText), mouseX, mouseY);
                g.bufferSource().endBatch();
                break;
            }
        }


        target.setClearColor(0 ,0, 0, 0);
        target.clear(false);
        target.bindWrite(false);

        g.pose().pushPose();
        g.pose().translate(0, -getScrollAmount() ,0);

        g.drawCenteredString(this.font, this.title, this.width / 2, 15, 0x1234ab);

        g.pose().popPose();
//        minecraft.getMainRenderTarget().bindWrite(true);
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
//        target.blitToScreen(minecraft.getWindow().getWidth(),minecraft.getWindow().getHeight(),false);

        // 流动速度
        float speed = 0.005f;
        ((IShaderInstance) ModRenderTypes.Shaders.floatBarShader).getTerra_entity$Time().set(System.currentTimeMillis() % 100000 * speed);
        // 噪声强度
        ((IShaderInstance) ModRenderTypes.Shaders.floatBarShader).getTerra_entity$Radius().set(0.5f);
        ModRenderTypes.Shaders.floatBarShader.COLOR_MODULATOR.set(1f,0f,0f,1f);

        RenderSystem.setShaderTexture(0, target.getColorTextureId());
        RenderSystem.setShaderTexture(1, TerraEntity.space("textures/gui/noise.png"));
        RenderSystem.setShader(() -> ModRenderTypes.Shaders.floatBarShader);

        // 不知道为什么y会是反的
        ShaderUtil.shaderBlit(new Matrix4f()
                ,
                0, 0,
                0, 0,
                minecraft.getMainRenderTarget().width, minecraft.getMainRenderTarget().height,
                (int)( minecraft.getMainRenderTarget().width/Minecraft.getInstance().getWindow().getGuiScale()),
                (int)( minecraft.getMainRenderTarget().height/Minecraft.getInstance().getWindow().getGuiScale())
        );

        minecraft.getMainRenderTarget().bindWrite(true);

        ShaderUtil.shaderBlit(new Matrix4f()
                ,
                0, 0,
                0, 0,
                minecraft.getMainRenderTarget().width, 50,
                (int)( minecraft.getMainRenderTarget().width/Minecraft.getInstance().getWindow().getGuiScale()),
                (int)( minecraft.getMainRenderTarget().height/Minecraft.getInstance().getWindow().getGuiScale())
        );


//        target.blitToScreen(minecraft.getWindow().getWidth(),minecraft.getWindow().getHeight(),false);


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
