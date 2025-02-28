package org.confluence.terraentity.client.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import com.mojang.math.Axis;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.confluence.terraentity.entity.proj.BaseProj;


public class ProjRenderer<T extends BaseProj> extends EntityRenderer<T> {
    private final EntityModel<T> bulletModel;
    float size;
    float offsetY;
    public ProjRenderer(EntityRendererProvider.Context pContext, EntityModel<T> pModel, float size,float offsetY) {
        super(pContext);
        bulletModel = pModel;
        this.size = size;
        this.offsetY = offsetY;
    }

    public ProjRenderer(EntityRendererProvider.Context pContext, EntityModel<T> pModel) {
        this(pContext, pModel, 1.0f, 0.0f);
    }

    @Override
    public ResourceLocation getTextureLocation(BaseProj entity) {
        return entity.getTexture();
    }

    @Override
    public void render(T pEntity, float pEntityYaw, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        if(pEntity.getTexture()==null) return;

        super.render(pEntity, pEntityYaw, pPartialTick, pPoseStack, pBuffer, pPackedLight);
        pPoseStack.pushPose();
        pPoseStack.scale(size,size,size);

        pPoseStack.translate(0,offsetY,0);


        Vec3 v = pEntity.getDeltaMovement();

        float yaw = (float) Math.atan2(v.z, v.x);
        pPoseStack.mulPose(Axis.YN.rotation(yaw + Mth.HALF_PI));
        pPoseStack.mulPose(Axis.XN.rotationDegrees(-pEntity.xRotO));

//        if(rotateZ) {
//            float pitch = -(float) Math.atan2(v.y, Math.sqrt(v.x * v.x + v.z * v.z));
//            pPoseStack.mulPose(Axis.XN.rotation(pitch));
//            pPoseStack.mulPose(Axis.ZN.rotation((pEntity.tickCount + pPartialTick) * rotateZSpeed));
//        }

        VertexConsumer buffer = pBuffer.getBuffer(this.bulletModel.renderType(this.getTextureLocation(pEntity)));
        this.bulletModel.renderToBuffer(pPoseStack,buffer,pPackedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        pPoseStack.popPose();
    }

}
