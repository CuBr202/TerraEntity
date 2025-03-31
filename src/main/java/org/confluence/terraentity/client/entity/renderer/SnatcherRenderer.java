package org.confluence.terraentity.client.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.confluence.terraentity.client.entity.model.EntityBlockModelRegister;
import org.confluence.terraentity.entity.monster.Snatcher;
import org.confluence.terraentity.init.item.TEBoomerangItems;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.joml.Vector3fc;

public class SnatcherRenderer<T extends Snatcher> extends GeoNormalRenderer<T> {

    BakedModel model;
    public SnatcherRenderer(EntityRendererProvider.Context renderManager, ResourceLocation path) {
        super(renderManager, path, true, 1, 0.2f);
        model = Minecraft.getInstance().getModelManager().getModel(EntityBlockModelRegister.SNATCHER_LEAF);

    }

    @Override
    public void render(T entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        Vec3 init = entity.getInitPos();
        if(init == null){
            return;
        }

        poseStack.pushPose();
        if (Minecraft.getInstance().player != null) {
            Vec3 p = Minecraft.getInstance().player.position().subtract(init);
            double a = p.cross(new Vec3(0, 1, 0)).dot(Minecraft.getInstance().player.position().subtract(entity.position()));
            poseStack.mulPose(Axis.YN.rotation(a>0?0.5f:-0.5f));
        }

        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
        poseStack.popPose();


        Vec3 lerpPos = new Vec3(
                Mth.lerp(partialTick, entity.xOld, entity.getX()),
                Mth.lerp(partialTick, entity.yOld, entity.getY()),
                Mth.lerp(partialTick, entity.zOld, entity.getZ())
        );

        Vec3 _diff = lerpPos.subtract(init);
        Vec3 diffNorm = _diff.normalize();

        // 让叶子紧贴实体
        Vec3 offset = diffNorm.scale(-1.5F);
        Vec3 diff = _diff.subtract(offset);
        int count = 10;
        double dx = diff.x / count;
        double dy = diff.y / count;
        double dz = diff.z / count;
        Quaternionf rotate = rotateFromV1ToV2(new Vector3f(0,0,1),new Vector3f((float) dx, (float) dy, (float) dz));

        for (int i = 0; i <= count; i++) {
            Vec3 pos = new Vec3(-i * dx, -i * dy, -i * dz);
            poseStack.pushPose();

            poseStack.translate(pos.x, pos.y, pos.z);
            poseStack.translate(offset.x, offset.y, offset.z);

            poseStack.mulPose(rotate);

//            poseStack.mulPose(Axis.ZN.rotation(i * 0.5f));
            poseStack.translate(-0.5,0,0);


            ItemStack stack = TEBoomerangItems.WOOD_BOOMERANG.toStack();
            for (RenderType rendertype : model.getRenderTypes(stack, false)) {
                VertexConsumer vertexconsumer = ItemRenderer.getFoilBuffer(bufferSource, rendertype, false, stack.isEnchanted());
                Minecraft.getInstance().getItemRenderer().renderModelLists(
                        model, stack, packedLight, OverlayTexture.NO_OVERLAY,
                        poseStack, vertexconsumer);
            }

            poseStack.popPose();
        }

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