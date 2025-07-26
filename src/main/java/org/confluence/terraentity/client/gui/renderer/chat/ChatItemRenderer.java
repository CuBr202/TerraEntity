package org.confluence.terraentity.client.gui.renderer.chat;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.confluence.terraentity.api.chat.IChatRenderer;

public enum ChatItemRenderer implements IChatRenderer<ItemStack> {
    INSTANCE;

    final float scale = 10f;

    @Override
    public void render(ItemStack chat, float x, float y, PoseStack poseStack, GuiGraphics guiGraphics, int packedLight, int packedOverlay, Level level, MultiBufferSource.BufferSource bufferSource) {
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(x + 6, y + 6,0f);
        guiGraphics.pose().scale(-scale,-scale,-scale);
//            guiGraphics.renderItem(Items.BOW.getDefaultInstance(), 0,0);
        Minecraft.getInstance().getItemRenderer().renderStatic(chat, ItemDisplayContext.FIXED,
                packedLight,packedOverlay, guiGraphics.pose(), bufferSource, level, 0
        );
        guiGraphics.pose().popPose();
    }
}
