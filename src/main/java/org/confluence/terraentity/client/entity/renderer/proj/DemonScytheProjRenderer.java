package org.confluence.terraentity.client.entity.renderer.proj;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.util.Mth;
import org.confluence.terraentity.entity.proj.DemonScytheProj;

public class DemonScytheProjRenderer extends ProjRenderer<DemonScytheProj> {
    public DemonScytheProjRenderer(EntityRendererProvider.Context pContext, EntityModel<DemonScytheProj> pModel, float size, float offsetY) {
        super(pContext, pModel, size, offsetY);
    }

    public DemonScytheProjRenderer(EntityRendererProvider.Context pContext, EntityModel<DemonScytheProj> pModel) {
        super(pContext, pModel);
    }

    @Override
    protected void adjustPosePost(PoseStack poseStack, DemonScytheProj entity, float partialTick){
        poseStack.translate(0, 0.75F, 0);
        poseStack.mulPose(Axis.YP.rotationDegrees(entity.getYRot() - 180));
        poseStack.mulPose(Axis.ZP.rotation(-Mth.lerp(partialTick, entity.rotate.old, entity.rotate.neo)));
        poseStack.mulPose(Axis.YP.rotation(-Mth.HALF_PI));
    }
}
