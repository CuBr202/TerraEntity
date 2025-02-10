package org.confluence.terraentity.client.entity.renderer;


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.entity.proj.BaseProj;


public class ProjRenderer<T extends BaseProj, M extends EntityModel<T>> extends BaseEntityRenderer<T, T, M> {

    public ProjRenderer(EntityRendererProvider.Context pContext, M pModel, float size, float offsetY) {
        super(pContext, pModel, size, offsetY);
    }

    public ProjRenderer(EntityRendererProvider.Context pContext, M pModel) {
        super(pContext, pModel);
    }

    @Override
    public ResourceLocation getTextureLocation(BaseProj pEntity) {
        if(pEntity.getTexture() == null)return TerraEntity.space("textures/entity/proj/missing.png");
        return pEntity.getTexture();
    }

    @Override
    public void render(T pEntity, float pEntityYaw, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        super.render(pEntity, pEntityYaw, pPartialTick, pPoseStack, pBuffer, pPackedLight);
        pPoseStack.pushPose();

        preRender(pEntity, pEntityYaw, pPartialTick, pPoseStack, pPackedLight);

        VertexConsumer buffer = pBuffer.getBuffer(this.model.renderType(this.getTextureLocation(pEntity)));
        this.model.renderToBuffer(pPoseStack,buffer,pPackedLight, OverlayTexture.NO_OVERLAY,1,1,1,1);
        pPoseStack.popPose();
    }

    public void preRender(T pEntity, float pEntityYaw, float pPartialTick, PoseStack pPoseStack, int pPackedLight){
        pPoseStack.translate(0,offsetY,0);
        pPoseStack.scale(size,size,size);
    }

}
