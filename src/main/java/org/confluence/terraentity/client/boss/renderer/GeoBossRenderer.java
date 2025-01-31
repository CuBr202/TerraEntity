package org.confluence.terraentity.client.boss.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

import net.minecraft.util.Mth;
import org.confluence.terraentity.client.boss.model.GeoBossModel;
import org.confluence.terraentity.entity.boss.AbstractTerraBossBase;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class GeoBossRenderer<T extends AbstractTerraBossBase, M extends GeoBossModel<T>> extends GeoEntityRenderer<T> {
    float scale;
    float yOffset;
    boolean rotX;
    public GeoBossRenderer(EntityRendererProvider.Context renderManager, M model) {
        this(renderManager, model, 1.0f,0, true);
    }

    public GeoBossRenderer(EntityRendererProvider.Context renderManager, M model, float scale, float yOffset, boolean rotX) {
        super(renderManager, model);
        this.scale = scale;
        this.yOffset = yOffset;
        this.rotX = rotX;
    }

    @Override
    public void preRender(PoseStack poseStack, T entity, BakedGeoModel model, @org.jetbrains.annotations.Nullable MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {
        poseStack.scale(scale, scale, scale);
        float yRot = Mth.lerp(partialTick, entity.yBodyRotO, entity.yBodyRot);
        double rad = yRot*Math.PI/180;
        float xRot = Mth.lerp(partialTick, entity.xRotO, entity.getXRot());
        poseStack.mulPose(Axis.of(new Vector3f((float) Math.cos(rad), 0, (float) Math.sin(rad))).rotationDegrees(xRot));
        poseStack.translate(0,yOffset,0);
        super.preRender(poseStack, entity, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, colour);

    }

    public float getYOffset(){
        return yOffset;
    }
}
