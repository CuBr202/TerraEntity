package org.confluence.terraentity.client.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import org.confluence.terraentity.client.entity.model.GeoNormalModel;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class GeoNormalRenderer<T extends Entity & GeoEntity> extends GeoEntityRenderer<T> {
    protected boolean ifRotX;
    protected float scale;
    protected float offsetY;

    /**
     * @param path 实体文件位置 path.namespace/textures/entity/{name}.png
     */
    public GeoNormalRenderer(EntityRendererProvider.Context renderManager, ResourceLocation path) {
        this(renderManager, path, false,1,0);
    }
    public GeoNormalRenderer(EntityRendererProvider.Context renderManager, ResourceLocation path, boolean ifRotX) {
        this(renderManager, path, ifRotX,1,0);
    }
    public GeoNormalRenderer(EntityRendererProvider.Context renderManager, ResourceLocation path, boolean ifRotX, float scale, float offsetY) {
        this(renderManager, new GeoNormalModel<>(path), ifRotX,scale,offsetY);
    }
    public GeoNormalRenderer(EntityRendererProvider.Context renderManager, GeoModel<T> model, boolean ifRotX, float scale, float offsetY) {
        super(renderManager, model);
        this.ifRotX = ifRotX;
        this.scale=scale;
        this.offsetY=offsetY;
        this.shadowRadius = 0.25F;
    }

    @Override
    public void preRender(PoseStack poseStack, T animatable, BakedGeoModel model, @Nullable MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {

        poseStack.scale(scale, scale, scale);
        poseStack.translate(0, offsetY, 0);
        if(ifRotX) {

            double rad = Mth.lerp(partialTick, animatable.yRotO, animatable.getYRot()) * Math.PI / 180;

            poseStack.mulPose(Axis.of(new Vector3f((float) Math.cos(rad), 0, (float) Math.sin(rad))).rotationDegrees(
                    Mth.lerp(partialTick, animatable.xRotO, animatable.getXRot())));
//            poseStack.translate(0, 0, 0);
        }
        this.adjustPose(poseStack, animatable, partialTick);

        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, colour);

    }

    protected void adjustPose(PoseStack poseStack, T animatable, float partialTick){

    }

    @Override
    public float getMotionAnimThreshold(T animatable) {
        return 0.01F;
    }

    public GeoNormalRenderer<T> setShadowRadius(float shadowRadius) {
        this.shadowRadius = shadowRadius;
        return this;
    }

}
