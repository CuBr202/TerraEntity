package org.confluence.terraentity.client.boss.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.confluence.terraentity.client.boss.model.GeoBossModel;
import org.confluence.terraentity.entity.boss.WallOfFlesh;

public class WallOfFleshRenderer extends GeoBossRenderer<WallOfFlesh,GeoBossModel<WallOfFlesh>> {

    public WallOfFleshRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new GeoBossModel<>("wall_of_flesh"), 3.0f, 0.5f, false);
    }

    @Override
    public void render(WallOfFlesh wall, float entityYaw, float partialTick,
                       PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        final int gridX = wall.getGridSizeX();
        final int gridY = wall.getGridSizeY();

        poseStack.pushPose();
        for (int x = 0; x < gridX; x++) {
            for (int y = 0; y < gridY; y++) {
                Vec3 offset = new Vec3(
                        (x - gridX / 2.0) * wall.gridSpacing,
                        y * wall.gridSpacing,
                        0
                );
                poseStack.pushPose();
                poseStack.translate(offset.x, offset.y, 0);
                super.render(wall, entityYaw, partialTick, poseStack, bufferSource, packedLight); // 调用父类渲染方法
                poseStack.popPose();
            }
        }
        boolean renderHitBoxes = Minecraft.getInstance().getEntityRenderDispatcher().shouldRenderHitBoxes();

        if (renderHitBoxes && !wall.isInvisible() && !Minecraft.getInstance().showOnlyReducedInfo() && !wall.isRemoved()) {
            AABB aabb = wall.getOutsideCollisionBox().move(-wall.getX(), -wall.getY(), -wall.getZ());
            LevelRenderer.renderLineBox(poseStack, bufferSource.getBuffer(RenderType.lines()), aabb, 1 , 0, 0, 1.0F);
            AABB bbaa = wall.getInsideBox().move(-wall.getX(), -wall.getY(), -wall.getZ());
            LevelRenderer.renderLineBox(poseStack, bufferSource.getBuffer(RenderType.lines()), bbaa, 0 , 0, 1, 1.0F);
        }
            poseStack.popPose();
    }

    @Override
    protected void applyRotations(WallOfFlesh animatable, PoseStack poseStack, float ageInTicks, float rotationYaw, float partialTick, float nativeScale) {
        if (isShaking(animatable))
            rotationYaw += (float)(Math.cos(animatable.tickCount * 3.25d) * Math.PI * 0.4d);

        if (!animatable.hasPose(Pose.SLEEPING))
            poseStack.mulPose(Axis.YP.rotationDegrees(180f - rotationYaw));

        if (animatable.deathTime <= 0 && animatable.isAutoSpinAttack()) {
            poseStack.mulPose(Axis.XP.rotationDegrees(-90f - animatable.getXRot()));
            poseStack.mulPose(Axis.YP.rotationDegrees((animatable.tickCount + partialTick) * -75f));
        }
    }

    @Override
    protected int getSkyLightLevel(WallOfFlesh entity, BlockPos pos) {
        Vec3 potionPos = new Vec3(pos.getX(), entity.level().getMaxBuildHeight()+1, pos.getZ());
        return super.getSkyLightLevel(entity, BlockPos.containing(potionPos));
    }

    @Override
    protected int getBlockLightLevel(WallOfFlesh entity, BlockPos pos) {
        Vec3 potionPos = new Vec3(pos.getX(), entity.level().getMaxBuildHeight()+1, pos.getZ());
        return super.getBlockLightLevel(entity, BlockPos.containing(potionPos));
    }

    public boolean shouldRender(WallOfFlesh livingEntity, Frustum camera, double camX, double camY, double camZ) {
       return true;
    }
}
