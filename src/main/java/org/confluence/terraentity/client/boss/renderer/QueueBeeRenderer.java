package org.confluence.terraentity.client.boss.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderBuffers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import org.confluence.terraentity.client.boss.model.GeoBossModel;
import org.confluence.terraentity.entity.boss.QueueBee;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;

public class QueueBeeRenderer extends GeoBossRenderer<QueueBee, GeoBossModel<QueueBee>> {
    static RenderBuffers bf = new RenderBuffers(Runtime.getRuntime().availableProcessors());
    public boolean consumeRender = false;
    public QueueBeeRenderer(EntityRendererProvider.Context renderManager, GeoBossModel<QueueBee> model) {
        super(renderManager, model,1.0f,0.5f,false);
    }

    @Override
    public void preRender(PoseStack poseStack, QueueBee animatable, BakedGeoModel model, @Nullable MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, colour);
    }

    @Override
    public void render(QueueBee entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {

        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);

    }

}
