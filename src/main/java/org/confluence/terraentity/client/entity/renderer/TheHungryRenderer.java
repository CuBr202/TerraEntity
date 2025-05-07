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
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;

import org.confluence.terraentity.client.init.model.EntityBlockModelRegister;
import org.confluence.terraentity.entity.monster.TheHungry;
import org.confluence.terraentity.init.item.TEBoomerangItems;
import org.confluence.terraentity.utils.TEUtils;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class TheHungryRenderer<T extends TheHungry> extends GeoNormalRenderer<T> {

    public TheHungryRenderer(EntityRendererProvider.Context renderManager, ResourceLocation path) {
        super(renderManager, path, true, 1, 0);

    }

    @Override
    public void render(T entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
        Vec3 init = entity.getInitPos();
        if(init == null){
            return;
        }

        poseStack.pushPose();
        if (Minecraft.getInstance().player != null) {
            // 错位渲染，防止正对的时候看到的是片面
            Vec3 p = Minecraft.getInstance().player.position().subtract(init);
            double a = p.cross(new Vec3(0, 1, 0)).dot(Minecraft.getInstance().player.position().subtract(entity.position()));
            poseStack.mulPose(Axis.YN.rotation(a>0?0.5f:-0.5f));
        }

        poseStack.popPose();

        Vec3 lerpPos = new Vec3(
                Mth.lerp(partialTick, entity.xOld, entity.getX()),
                Mth.lerp(partialTick, entity.yOld, entity.getY()),
                Mth.lerp(partialTick, entity.zOld, entity.getZ())
        );

        Vec3 _diff = lerpPos.subtract(init);
        Vec3 diffNorm = _diff.normalize();

        // 让叶子紧贴实体
        Vec3 offset = diffNorm.scale(-1);
        Vec3 diff = _diff.subtract(offset);
        double distance = _diff.length(); // 获取实体与初始点的直线距离[6](@ref)
        float length = 2;
        int baseCount = 5; // 基础数量
        float densityFactor = 0.8f; // 每米增加的数量密度

        int count = Mth.clamp(
                (int)(distance * densityFactor) + baseCount,
                baseCount,
                50
        );
        double dx = diff.x / count;
        double dy = diff.y / count;
        double dz = diff.z / count;
        Quaternionf rotate = TEUtils.rotateFromV1ToV2(new Vector3f(0,1,0),new Vector3f((float) dx, (float) dy, (float) dz));
        BakedModel model = Minecraft.getInstance().getModelManager().getModel(EntityBlockModelRegister.getInstance().getModelResourceLocation(entity.getType()));;
        for (int i = 0; i < count; i++) {
            Vec3 pos = new Vec3(-i * dx + offset.x, -i * dy + offset.y, -i * dz + offset.z);
            poseStack.pushPose();
            poseStack.translate(-0.5f,0.5f,-0.5f);
            poseStack.translate(pos.x, pos.y, pos.z);
//            poseStack.translate(offset.x, offset.y, offset.z);
            poseStack.translate(0.5,0,0.5);


            poseStack.mulPose(rotate);
            poseStack.mulPose(Axis.YN.rotation(i * 0.5f));
            poseStack.translate(-0.5,0,-0.5);

//            poseStack.translate(-0.5,0,0);

            ItemStack stack = Items.AIR.getDefaultInstance();
            for (RenderType rendertype : model.getRenderTypes(stack, false)) {
                VertexConsumer vertexconsumer = ItemRenderer.getFoilBuffer(bufferSource, rendertype, false, stack.isEnchanted());
                Minecraft.getInstance().getItemRenderer().renderModelLists(
                        model, stack, packedLight, OverlayTexture.NO_OVERLAY,
                        poseStack, vertexconsumer);
            }

            poseStack.popPose();
        }
    }
}