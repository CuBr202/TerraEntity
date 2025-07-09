package org.confluence.terraentity.client.entity.renderer.mob;

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
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;
import org.confluence.terraentity.entity.summon.SummonSword;

public class SummonSwordRenderer<T extends SummonSword> extends EntityRenderer<T> {

    public SummonSwordRenderer(EntityRendererProvider.Context context) {
        super(context);
    }


    @Override
    public void render(T entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        float pitch;
        float yaw;
        if(entity.getOwner() != null && entity.tickCount > 1){
            pitch = Mth.lerp(partialTick, entity.xRotO, entity.getXRot());
            yaw = entityYaw;

        }else{
            return;
        }
        // 位置插值
//        double lerpX = Mth.lerp(partialTick, entity.xo - entity.getOwner().xo, entity.getX() - entity.getOwner().getX());
//        double lerpY = Mth.lerp(partialTick, entity.yo - entity.getOwner().yo, entity.getY() - entity.getOwner().getY());
//        double lerpZ = Mth.lerp(partialTick, entity.zo - entity.getOwner().zo, entity.getZ() - entity.getOwner().getZ());

        entity.trail.renderTrail(entity, entity.trailQueue, entity.position(), poseStack, bufferSource, packedLight);

        poseStack.pushPose();

        // 旋转到正前方yaw
        poseStack.mulPose(Axis.YN.rotationDegrees(yaw - 90));

        // 旋转到正前方pitch
        poseStack.mulPose(Axis.ZN.rotationDegrees(-pitch));

        float progress =  Mth.clamp((entity.backTicks + partialTick) / entity.backTicksMax,0,1);

        poseStack.mulPose(Axis.XP.rotationDegrees(90 * progress));
        poseStack.mulPose(Axis.ZP.rotationDegrees(entity.sequence /2 * ((entity.sequence & 1) == 0? -1 : 1) * 15 * progress));

        poseStack.mulPose(Axis.ZN.rotationDegrees(-45 + entity.getRotateZTimer(partialTick) * 30
//                + (entity.tickCount + partialTick) * 30
                ));


        Minecraft.getInstance().getItemRenderer().renderStatic(entity.modelItem.getDefaultInstance(), ItemDisplayContext.FIXED, packedLight, OverlayTexture.NO_OVERLAY, poseStack, bufferSource, entity.level(), 0);
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
        poseStack.popPose();    }

        @Override
    public ResourceLocation getTextureLocation(SummonSword summonSword) {
        return null;
    }

}
