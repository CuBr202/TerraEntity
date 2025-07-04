package org.confluence.terraentity.client.entity.renderer.proj;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.phys.Vec3;
import org.confluence.terraentity.entity.proj.BoomerangProjectile;

public class BoomerangProjRenderer extends EntityRenderer<BoomerangProjectile> {

    public BoomerangProjRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
    }

    @Override
    public void render(BoomerangProjectile entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
//        int pack = OverlayTexture.pack((int) (partialTick*Math.sin(partialTick/10)), (int) (partialTick*Math.cos(partialTick/10)));


        // 位置插值
        Vec3 v = entity.getDeltaMovement();

        if(entity.trail != null) {
            double x =  Mth.lerp(partialTick, entity.xo, entity.getX());
            double y =  Mth.lerp(partialTick, entity.yo, entity.getY());
            double z =  Mth.lerp(partialTick, entity.zo, entity.getZ());

            Vec3 pos = new Vec3(x, y, z);
//        renderTrail(entity.trailQueue.iterator(), pos , poseStack, bufferSource, color, entity.trailQueue.size());
//        renderTrail(entity.trailQueue2.iterator(), pos , poseStack, bufferSource, color, entity.trailQueue2.size());

            entity.trail.renderTrail(entity, entity.trailQueue, pos, poseStack, bufferSource);
            entity.trail.renderTrail(entity, entity.trailQueue2, pos, poseStack, bufferSource);
        }

        float yaw = (float) Math.atan2(v.z, v.x);
        // 旋转到正前方yaw
        poseStack.mulPose(Axis.YN.rotation( (yaw + (float) Math.PI * (entity.isBacking?1:0))));
        float pitch = (float) Math.atan2(v.y, Math.sqrt(v.x*v.x + v.z*v.z));
        // 旋转到正前方pitch
        poseStack.mulPose(Axis.ZN.rotation( pitch * (entity.isBacking? 1:-1) ));

        // 旋转到水平位置
        poseStack.mulPose(Axis.XN.rotationDegrees((float) (90 -  20 *Math.cos((entity.tickCount + entity.randomRotation) / 10.0)  )));

        // 随时间旋转
        poseStack.mulPose(Axis.ZN.rotation((entity.tickCount + partialTick)));

        Minecraft.getInstance().getItemRenderer().renderStatic(entity.weapon, ItemDisplayContext.FIXED, packedLight, OverlayTexture.NO_OVERLAY,poseStack, bufferSource,  entity.level(),0);
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(BoomerangProjectile baseArrowEntity) {
        return null;
    }

}
