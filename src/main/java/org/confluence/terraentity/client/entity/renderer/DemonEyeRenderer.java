package org.confluence.terraentity.client.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.util.Mth;

import org.confluence.terraentity.client.entity.model.DemonEyeModel;
import org.confluence.terraentity.entity.monster.demoneye.DemonEye;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class DemonEyeRenderer extends GeoEntityRenderer<DemonEye> {
    public DemonEyeRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new DemonEyeModel());
    }

    @Override
    public void preRender(PoseStack poseStack, DemonEye animatable, BakedGeoModel model, @Nullable MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay,  float r, float g, float b, float a) {
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, r,g,b,a);
        poseStack.mulPose(Axis.XP.rotationDegrees(Mth.lerp(partialTick,animatable.xRotO,animatable.getXRot())));
    }

    @Override
    protected float getDeathMaxRotation(DemonEye animatable){
        return 0;
    }
}
