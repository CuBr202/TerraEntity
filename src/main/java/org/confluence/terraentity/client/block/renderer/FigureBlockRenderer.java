package org.confluence.terraentity.client.block.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.Blocks;
import org.confluence.terraentity.block.FigureBlock.FigureBlockEntity;
import org.jetbrains.annotations.NotNull;

public class FigureBlockRenderer <T extends FigureBlockEntity> implements BlockEntityRenderer<T> {
    public FigureBlockRenderer(BlockEntityRendererProvider.Context context) {

    }
    @Override
    public void render(T t, float partialTick, @NotNull PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, int packedLight, int packedOverlay) {
        Minecraft.getInstance().getBlockRenderer().renderSingleBlock(Blocks.GLASS.defaultBlockState(), poseStack, multiBufferSource, packedLight, packedOverlay);
        var entity = t.entity;

        poseStack.pushPose();
        float y = entity.tickCount + (t.turnOn?  partialTick : 0);
        poseStack.translate(0.5, Math.sin(y * 0.05f) * 0.15 + 0.15f, 0.5);
        poseStack.mulPose(Axis.YN.rotation(y * 0.01752f));
        poseStack.scale(0.5f, 0.5f, 0.5f);

        Minecraft.getInstance().getEntityRenderDispatcher().render(
                entity, 0,0,0,0, 0,poseStack,multiBufferSource,packedLight);



        poseStack.popPose();

    }
}
