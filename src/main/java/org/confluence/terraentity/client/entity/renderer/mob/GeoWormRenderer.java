package org.confluence.terraentity.client.entity.renderer.mob;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.client.entity.renderer.GeoNormalRenderer;
import org.confluence.terraentity.entity.monster.BaseWorm;
import org.confluence.terraentity.entity.monster.BaseWormPart;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;


public class GeoWormRenderer<T extends BaseWorm<S>, S extends BaseWormPart> extends GeoNormalRenderer<T> {

    GeoWormSegmentRenderer partRenderer;
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
        partRenderer = createPartRenderer(renderManager, path);
    }

    protected GeoWormSegmentRenderer createPartRenderer(EntityRendererProvider.Context renderManager, ResourceLocation path) {
        String name = path.getPath();
        String segment = name + "_segment";
        String tail = name + "_tail";
        return new GeoWormSegmentRenderer<>(renderManager,this,
                TerraEntity.space(segment),
                TerraEntity.space(tail),scale,offsetY);
    }

    @Override
    public void render(T entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {

        S part1 = entity.bodySegments.get(0);
        poseStack.pushPose();

        lerpx = Mth.lerp(partialTick, entity.xOld, entity.getX());
        lerpy = Mth.lerp(partialTick, entity.yOld, entity.getY());
        lerpz = Mth.lerp(partialTick, entity.zOld, entity.getZ());
        double lerpDx = lerpx - Mth.lerp(partialTick, part1.xOld, part1.getX());
        double lerpDy = lerpy - Mth.lerp(partialTick, part1.yOld, part1.getY());
        double lerpDz = lerpz - Mth.lerp(partialTick, part1.zOld, part1.getZ());

        float yRot = Mth.lerp(partialTick, entity.yBodyRotO, entity.yBodyRot);
        double rad = yRot*Math.PI/180;
        float pitch = (float) (Math.atan2(lerpDy,
                Math.sqrt(lerpDx * lerpDx + lerpDz * lerpDz)));
        poseStack.mulPose(Axis.of(new Vector3f((float) Math.cos(rad), 0, (float) Math.sin(rad))).rotation(-pitch));

        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
        poseStack.popPose();

        for(S part : entity.bodySegments){
            poseStack.pushPose();
            float lerpYRot = Mth.lerp(partialTick, part.yRotOO, part.getYRot());
            partRenderer.render(part, lerpYRot, partialTick, poseStack, bufferSource, Minecraft.getInstance().getEntityRenderDispatcher().getPackedLightCoords(part, partialTick));
            poseStack.popPose();
        }
    }

    protected void rotateX(PoseStack poseStack, T animatable, float partialTick){

    }

    @Override
    public RenderType getRenderType(T animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return RenderType.entityCutoutNoCull(texture);
    }

}
