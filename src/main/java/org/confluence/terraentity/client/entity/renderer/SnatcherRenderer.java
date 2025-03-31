package org.confluence.terraentity.client.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import org.confluence.terraentity.entity.monster.Snatcher;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.joml.Vector3fc;

public class SnatcherRenderer<T extends Snatcher> extends GeoNormalRenderer<T> {
    public SnatcherRenderer(EntityRendererProvider.Context renderManager, ResourceLocation path) {
        super(renderManager, path, true);
    }

    @Override
    public void render(T entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
        Vec3 init = entity.getInitPos();
        if(init == null){
            return;
        }
        int count = 5;
        double dx = (Mth.lerp(partialTick, entity.xOld, entity.getX()) - init.x) / count;
        double dy = (Mth.lerp(partialTick, entity.yOld, entity.getY()) - init.y + entity.getBbHeight() ) / count;
        double dz = (Mth.lerp(partialTick, entity.zOld, entity.getZ()) - init.z) / count;
        Quaternionf rotate = rotateFromV1ToV2(new Vector3f(0,1,0),new Vector3f((float) dx, (float) dy, (float) dz));
        poseStack.pushPose();

        poseStack.translate(-0.5f,1.5,-0.5f);

        for (int i = 1; i <= count; i++) {
            Vec3 pos = new Vec3(-i * dx, -i * dy, -i * dz);
            poseStack.pushPose();
            poseStack.translate(pos.x, pos.y, pos.z);
            poseStack.mulPose(rotate);

            Minecraft.getInstance().getBlockRenderer().renderSingleBlock(
                    Blocks.CHAIN.defaultBlockState(), poseStack, bufferSource,packedLight, OverlayTexture.NO_OVERLAY
            );
            poseStack.popPose();
        }
        poseStack.popPose();

    }
    public static Quaternionf rotateFromV1ToV2(Vector3fc v1, Vector3fc v2) {
        // 1. 归一化向量
        Vector3f v1Norm = new Vector3f(v1).normalize();
        Vector3f v2Norm = new Vector3f(v2).normalize();

        // 2. 计算点积和叉乘
        float dot = v1Norm.dot(v2Norm);
        Vector3f cross = new Vector3f();
        v1Norm.cross(v2Norm, cross);

        // 3. 处理特殊情况
        if (Math.abs(dot) >= 1.0f - 1e-6f) { // 平行或反平行
            return dot > 0 ? new Quaternionf() : // 同向返回单位四元数
                    new Quaternionf().fromAxisAngleRad(new Vector3f(1, 0, 0), (float) Math.PI); // 反向旋转180度
        }

        // 4. 计算旋转轴和角度
        float sinTheta = cross.length();
        float theta = (float) Math.atan2(sinTheta, dot);
        cross.normalize(); // 归一化旋转轴

        // 5. 构造四元数
        return new Quaternionf().fromAxisAngleRad(cross, theta);
    }
}