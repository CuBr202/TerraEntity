package org.confluence.terraentity.client.block.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Blocks;
import org.confluence.terraentity.block.FigureBlock.FigureBlockEntity;
import org.jetbrains.annotations.NotNull;

public class FigureBlockRenderer <T extends FigureBlockEntity> implements BlockEntityRenderer<T> {
    EntityType<?> entityType;
    public FigureBlockRenderer(BlockEntityRendererProvider.Context context, EntityType<?> entityType) {

        this.entityType = entityType;
    }
    @Override
    public void render(T t, float partialTick, @NotNull PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, int packedLight, int packedOverlay) {
        Minecraft.getInstance().getBlockRenderer().renderSingleBlock(Blocks.BAMBOO_BLOCK.defaultBlockState(), poseStack, multiBufferSource, packedLight, packedOverlay);
        var entity = t.entity;

        poseStack.pushPose();
        poseStack.translate(0.5, 1, 0.5);
        poseStack.mulPose(Axis.YN.rotation((entity.tickCount + (t.turnOn?  partialTick : 0)) * 0.01752f));
        poseStack.scale(0.5f, 0.5f, 0.5f);
        Minecraft.getInstance().getEntityRenderDispatcher().render(entity, 0,0,0,30, 0,poseStack,multiBufferSource,packedLight);
        poseStack.popPose();

    }
}
