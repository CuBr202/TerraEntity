package org.confluence.terraentity.client.entity.renderer.proj;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import org.confluence.terraentity.entity.proj.TrailProjectile;
import org.joml.Matrix4f;

import java.util.List;

public class TrailProjectileRenderer extends EntityRenderer<TrailProjectile> {
    public TrailProjectileRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(TrailProjectile entity) {
        return TextureAtlas.LOCATION_BLOCKS;
    }

    @Override
    public void render(TrailProjectile entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        List<Vec3> trails = entity.getTrails();
        if (trails.isEmpty()) return;
        int trailColor = entity.getTrailColor();
        float r = (trailColor & 255) / 255.0F;
        float g = (trailColor >> 8 & 255) / 255.0F;
        float b = (trailColor >> 16 & 255) / 255.0F;
        Vec3 pos0;
        Vec3 pos1;
        poseStack.pushPose();
        Matrix4f matrix4f = poseStack.last().pose();
        VertexConsumer bufferbuilder = bufferSource.getBuffer(RenderType.lightning());

        for (int i = 1; i < trails.size(); i++) {
            pos0 = trails.get(i - 1).subtract(entity.position());
            pos1 = trails.get(i).subtract(entity.position());

            float x1 = (float) pos0.x;
            float y1 = (float) pos0.y;
            float z1 = (float) pos0.z;
            float x2 = (float) pos1.x;
            float y2 = (float) pos1.y;
            float z2 = (float) pos1.z;
            float baseWidth = 0.3f;
            float width0 = baseWidth / trails.size() * (i - 1);
            float width1 = baseWidth / trails.size() * i;

            bufferbuilder.addVertex(matrix4f, x1, y1, z1 - width0).setColor(r, g, b, 0.8f);
            bufferbuilder.addVertex(matrix4f, x1, y1, z1 + width0).setColor(r, g, b, 0.8f);
            bufferbuilder.addVertex(matrix4f, x2, y2, z2 + width1).setColor(r, g, b, 0.8f);
            bufferbuilder.addVertex(matrix4f, x2, y2, z2 - width1).setColor(r, g, b, 0.8f);

            bufferbuilder.addVertex(matrix4f, x1, y1, z1 + width0).setColor(r, g, b, 0.8f);
            bufferbuilder.addVertex(matrix4f, x1, y1, z1 - width0).setColor(r, g, b, 0.8f);
            bufferbuilder.addVertex(matrix4f, x2, y2, z2 - width1).setColor(r, g, b, 0.8f);
            bufferbuilder.addVertex(matrix4f, x2, y2, z2 + width1).setColor(r, g, b, 0.8f);
        }
        poseStack.popPose();
    }
}
