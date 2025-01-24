package org.confluence.terraentity.client.boss.renderer;

import com.mojang.blaze3d.pipeline.TextureTarget;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import org.confluence.terraentity.client.boss.model.GeoBossModel;
import org.confluence.terraentity.client.post.BrainTranslucent;
import org.confluence.terraentity.entity.boss.BrainOfCthulhu;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import software.bernie.geckolib.cache.object.BakedGeoModel;

public class BrainOfCthulhuRenderer extends GeoBossRenderer<BrainOfCthulhu,GeoBossModel<BrainOfCthulhu>> {

    public BrainOfCthulhuRenderer(EntityRendererProvider.Context renderManager, GeoBossModel<BrainOfCthulhu> model) {
        super(renderManager, model,1.0f,0.5f,false);
    }

    @Override
    public void preRender(PoseStack poseStack, BrainOfCthulhu animatable, BakedGeoModel model, @Nullable MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {
        this.entityRenderTranslations = new Matrix4f(poseStack.last().pose());
        scaleModelForRender(this.scaleWidth, this.scaleHeight, poseStack, animatable, model, isReRender, partialTick, packedLight, packedOverlay);
    }

    @Override
    public void render(BrainOfCthulhu entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {

        //        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
        float alpha = Math.clamp(entity.getFadeProgress(), 0, 1);
        if(alpha < 1.0f){
            Minecraft.getInstance().renderBuffers().bufferSource().endBatch();
            TextureTarget target;
            BrainTranslucent.tuple tuple = BrainTranslucent.entityMap.get(entity);
            if(tuple == null) {
                target = new TextureTarget(Minecraft.getInstance().getMainRenderTarget().width, Minecraft.getInstance().getMainRenderTarget().height, true, false);
                tuple = new BrainTranslucent.tuple(target, 1f);
                BrainTranslucent.entityMap.put(entity, tuple);
            }else {
                target = BrainTranslucent.entityMap.get(entity).target;
            }

            tuple.alpha = alpha;


            target.setClearColor(0, 0, 0, 0);
            target.clear(false);
            target.copyDepthFrom(Minecraft.getInstance().getMainRenderTarget());
            target.bindWrite(true);
            super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
            Minecraft.getInstance().renderBuffers().bufferSource().endBatch();
            target.unbindWrite();
            Minecraft.getInstance().getMainRenderTarget().bindWrite(true);
        }else{
            super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
        }



//        target.blitToScreen(Minecraft.getInstance().getMainRenderTarget().width, Minecraft.getInstance().getMainRenderTarget().height);
    }

}
