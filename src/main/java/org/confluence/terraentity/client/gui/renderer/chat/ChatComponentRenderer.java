package org.confluence.terraentity.client.gui.renderer.chat;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import org.confluence.terraentity.api.chat.IChatRenderer;

public enum ChatComponentRenderer implements IChatRenderer<Component> {
    INSTANCE;

    @Override
    public void render(Component chat, float x, float y, PoseStack poseStack, GuiGraphics guiGraphics, int packedLight, int packedOverlay, Level level, MultiBufferSource.BufferSource bufferSource) {
        guiGraphics.drawString(Minecraft.getInstance().font, chat, (int) x, (int) y,0xffffffff);

    }
}
