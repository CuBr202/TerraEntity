package org.confluence.terraentity.api.chat;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.level.Level;

public interface IChatRenderer<T> {

    void render(T chat, float x, float y, PoseStack poseStack, GuiGraphics guiGraphics, int packedLight, int packedOverlay, Level level, MultiBufferSource.BufferSource bufferSource);


}
