package org.confluence.terraentity.client.entity.renderer.mob;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.irisshaders.iris.pipeline.programs.ExtendedShader;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.neoforged.fml.ModList;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.client.entity.model.TerraprismaModel;
import org.confluence.terraentity.entity.summon.SummonSword;
import org.confluence.terraentity.entity.summon.Terraprisma;
import org.jetbrains.annotations.NotNull;

public class TerraprismaRenderer extends SummonSwordRenderer<Terraprisma> {
    TerraprismaModel model;
    public TerraprismaRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model= new TerraprismaModel(context.bakeLayer(TerraprismaModel.LAYER_LOCATION));
    }



    protected void preRender(Terraprisma entity, float yaw,float pitch, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight){
        this.setupPose(entity, yaw, pitch, partialTick, poseStack);
        this.additionPose(entity, yaw, pitch, partialTick, poseStack);
        this.customPose(entity, yaw, pitch, partialTick, poseStack);
    }


    protected void setupPose(Terraprisma entity, float yaw, float pitch, float partialTick, PoseStack poseStack){
        poseStack.mulPose(Axis.YN.rotationDegrees(yaw));
        poseStack.mulPose(Axis.XN.rotationDegrees(-pitch+180));
    }


    protected void additionPose(Terraprisma entity, float yaw, float pitch, float partialTick, PoseStack poseStack){
        float x =  Mth.clamp((entity.backTicks + partialTick) / entity.backTicksMax,0,1);
        x = x < 0.5 ? 2 * x * x : (float) (1 - Math.pow(-2 * x + 2, 2) / 2);
        poseStack.mulPose(Axis.ZP.rotationDegrees(90 * x));
        poseStack.mulPose(Axis.XP.rotationDegrees(entity.sequence /2 * ((entity.sequence & 1) == 0? -1 : 1) * 15 * x));

    }


    protected void customPose(Terraprisma entity, float yaw, float pitch, float partialTick, PoseStack poseStack){
//        poseStack.mulPose(Axis.XN.rotationDegrees(entity.getRotateZTimer(partialTick) * 30));
        if(entity.anim_x != null) {
            poseStack.mulPose(Axis.XN.rotationDegrees((float) entity.anim_x.cal(entity.tickCount, partialTick)));
        }
//        poseStack.mulPose(Axis.XN.rotationDegrees((entity.tickCount + partialTick) * 30));

//        poseStack.mulPose(Axis.ZN.rotationDegrees(90));
//        poseStack.mulPose(Axis.XN.rotationDegrees((entity.tickCount + partialTick) * 15));

    }

    protected void renderTrail(Terraprisma entity, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, float partialTick){
        entity.trail.renderTrail(entity, entity.trailQueue, entity.position(), poseStack, bufferSource, packedLight);
    }
    @Override
    public ResourceLocation getTextureLocation(@NotNull SummonSword summonSword) {
        return TerraEntity.space("textures/entity/model/terraprisma_gray.png");
    }

    @Override
    protected void renderModel(Terraprisma entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight){
        if(RenderSystem.getShader() == null){
            return;
        }
        if(ModList.get().isLoaded("iris") && RenderSystem.getShader() instanceof ExtendedShader) {
            model.renderToBuffer(poseStack, bufferSource.getBuffer(RenderType.entityTranslucentEmissive(getTextureLocation(entity))), packedLight, OverlayTexture.NO_OVERLAY, entity.getRgb() | 0xFF000000);
        }else{
            // 原版这个效果好一点
            model.renderToBuffer(poseStack, bufferSource.getBuffer(RenderType.energySwirl(getTextureLocation(entity),0,0)), packedLight, OverlayTexture.NO_OVERLAY, entity.getRgb() | 0xFF000000);
        }
    }
}
