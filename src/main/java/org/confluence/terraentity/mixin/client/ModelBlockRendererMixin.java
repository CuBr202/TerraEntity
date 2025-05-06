package org.confluence.terraentity.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.core.BlockPos;
import org.confluence.terraentity.mixed.LightManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ModelBlockRenderer.class)
public class ModelBlockRendererMixin {
    @WrapOperation(method = "putQuadData", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/VertexConsumer;putBulkData(Lcom/mojang/blaze3d/vertex/PoseStack$Pose;Lnet/minecraft/client/renderer/block/model/BakedQuad;[FFFFF[IIZ)V"))
    private void renderQuadList(VertexConsumer instance, PoseStack.Pose f7, BakedQuad f8, float[] f3, float f4, float f5, float f, float f1, int[] f2, int f33, boolean f44, Operation<Void> original, @Local(argsOnly = true)BlockPos blockPos) {
        LightManager.processRenderQuadList(instance, f7, f8, f3, f4, f5, f, f1, f2, f33, f44, original, blockPos);
    }

}
