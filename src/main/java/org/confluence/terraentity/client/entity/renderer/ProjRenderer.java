package org.confluence.terraentity.client.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
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
        VertexConsumer buffer = pBuffer.getBuffer(this.bulletModel.renderType(this.getTextureLocation(pEntity)));
        this.bulletModel.renderToBuffer(pPoseStack,buffer,pPackedLight, OverlayTexture.NO_OVERLAY);
        pPoseStack.popPose();
    }

}
