package org.confluence.terraentity.client.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.entity.monster.BaseWarm;
import org.confluence.terraentity.entity.monster.BaseWarmPart;
import org.jetbrains.annotations.Nullable;


public class GeoWormRenderer<T extends BaseWarm> extends GeoNormalRenderer<T> {

    GeoWormSegmentRenderer<BaseWarmPart> partRenderer;
    public double lerpx;
    public double lerpy;
    public double lerpz;

    /**
     * 文件命名：
     * <p>{path}.geo.json</p>
     * <p>{path}_segment.geo.json</p>
     * <p>{path}_tail.geo.json</p>
     * @param path entity
     */
    public GeoWormRenderer(EntityRendererProvider.Context renderManager, ResourceLocation path) {
        this(renderManager, path, 1.0f, 0.0f);

    }
    /**
     * 文件命名：
     * <p>{path}.geo.json</p>
     * <p>{path}_segment.geo.json</p>
     * <p>{path}_tail.geo.json</p>
     * @param path entity
     */
    public GeoWormRenderer(EntityRendererProvider.Context renderManager, ResourceLocation path, float scale, float offsetY) {
        super(renderManager, path, true, scale, offsetY);
        String name = path.getPath();
        String segment = name + "_segment";
        String tail = name + "_tail";
        partRenderer = new GeoWormSegmentRenderer<>(renderManager,this,
                TerraEntity.asResource(segment),
                TerraEntity.asResource(tail),scale,offsetY);
    }

    @Override
    public void render(T entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {

        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);

        lerpx = Mth.lerp(partialTick, entity.xOld, entity.getX());
        lerpy = Mth.lerp(partialTick, entity.yOld, entity.getY());
        lerpz = Mth.lerp(partialTick, entity.zOld, entity.getZ());
        for(BaseWarmPart part : entity.bodySegments){
            poseStack.pushPose();
            float lerpYRot = Mth.lerp(partialTick, part.yRotO, part.getYRot());
            partRenderer.render(part, lerpYRot, partialTick, poseStack, bufferSource, packedLight);
            poseStack.popPose();
        }
    }

    @Override
    public RenderType getRenderType(T animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return RenderType.entityCutoutNoCull(texture);

    }

}
