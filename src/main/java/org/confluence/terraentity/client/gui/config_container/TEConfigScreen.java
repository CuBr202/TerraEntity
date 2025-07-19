package org.confluence.terraentity.client.gui.config_container;

import com.github.edg_thexu.cafelib.client.gui.config_container.ConfigScreen;
import com.github.edg_thexu.cafelib.client.gui.config_container.ConfigScreenBuilder;
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
import net.minecraftforge.common.ForgeConfigSpec;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.client.ModRenderTypes;
import org.confluence.terraentity.client.util.ShaderUtil;
import org.confluence.terraentity.config.*;
import org.confluence.terraentity.mixed.IShaderInstance;

import java.util.function.Function;

@OnlyIn(Dist.CLIENT)
public class TEConfigScreen extends ConfigScreen {

    public TEConfigScreen(Screen screen, Function<ConfigScreen, ConfigScreenBuilder> builderFunction) {
        super(screen, Component.translatable("terra_entity.options.title"), builderFunction);
    }

    @Override
    protected ForgeConfigSpec getSpec() {
        return ConfigRegistry.SPEC;
    }

    @Override
    public void render(GuiGraphics g, int mouseX, int mouseY, float partialTicks) {
        super.render(g, mouseX, mouseY, partialTicks);
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

    @Override
    public int getMaxScroll() {
        return Math.max(0, 250);
    }
}
