package org.confluence.terraentity.client.entity.renderer.mob;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.confluence.terraentity.client.entity.model.GeoNormalModel;
import org.confluence.terraentity.entity.monster.BaseWarmPart;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;


public class GeoWormSegmentRenderer<T extends BaseWarmPart> extends GeoEntityRenderer<T> {

    float scale;
    float offsetY;

    GeoNormalModel<T> tailModel;
    GeoWormRenderer parent;

    public GeoWormSegmentRenderer(EntityRendererProvider.Context renderManager, GeoWormRenderer parent,  ResourceLocation body, ResourceLocation tail) {
        this(renderManager, parent, body, tail,1,0);
    }
    public GeoWormSegmentRenderer(EntityRendererProvider.Context renderManager, GeoWormRenderer parent,  ResourceLocation body, ResourceLocation tail, float scale, float offsetY) {
        super(renderManager, new GeoNormalModel<>(body, false));
        this.scale=scale;
        this.offsetY=offsetY;
        tailModel = new GeoNormalModel<>(tail,false);
        this.parent = parent;
    }


    @Override
    public void render(T part, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {

        double lerpx = Mth.lerp(partialTick, part.xxo, part.getX());
        double lerpy = Mth.lerp(partialTick, part.yyo, part.getY());
        double lerpz = Mth.lerp(partialTick, part.zzo, part.getZ());
        poseStack.translate(lerpx - parent.lerpx, lerpy - parent.lerpy, lerpz - parent.lerpz);

        poseStack.mulPose(Axis.YN.rotationDegrees(entityYaw));
//        poseStack.mulPose(parent.resetX);

        float lerpXRot = Mth.lerp(partialTick, part.xRotO, part.getXRot());
//        poseStack.mulPose(Axis.of(new Vector3f((float) Math.cos(rad1), 0, (float) Math.sin(rad1))).rotationDegrees(-lerpXRot));
        poseStack.mulPose(Axis.XN.rotationDegrees(lerpXRot));
        poseStack.scale(scale, scale, scale);

        super.render(part, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }

    @Override
    public RenderType getRenderType(T animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return RenderType.entityCutoutNoCull(texture);

    }

    @Override
    public GeoModel<T> getGeoModel() {
        return this.animatable!=null && this.animatable.isTail? tailModel : this.model;
    }

    @Override
    public int getPackedOverlay(T animatable, float u, float partialTick) {

        return OverlayTexture.pack(OverlayTexture.u(u),
                OverlayTexture.v(animatable.hurtTime > 0 || animatable.deathTime > 0));
    }

}
