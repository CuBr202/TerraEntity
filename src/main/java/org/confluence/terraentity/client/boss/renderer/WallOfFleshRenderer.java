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
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.confluence.terraentity.client.boss.model.GeoBossModel;
import org.confluence.terraentity.entity.boss.WallOfFlesh;
import software.bernie.geckolib.model.GeoModel;

import java.util.HashMap;
import java.util.Map;

public class WallOfFleshRenderer extends GeoBossRenderer<WallOfFlesh,GeoBossModel<WallOfFlesh>> {

    public WallOfFleshRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new GeoBossModel<>(MODEL_NAMES[0]), 3.0f, 0.5f, false);
    }

    GeoBossModel<WallOfFlesh> currentModel;
    private static final String[] MODEL_NAMES = {
            "wall_of_flesh0",
            "wall_of_flesh1",
            "wall_of_flesh2",
            "wall_of_flesh3",
            "wall_of_flesh4"
    };
    private static final int[] MODEL_WEIGHTS = {
            1,  // wall_of_flesh0
            1,  // wall_of_flesh1
            1,  // wall_of_flesh2
            1,  // wall_of_flesh3
            5   // wall_of_flesh4
    };
    private final Map<String, GeoBossModel<WallOfFlesh>> modelCache = new HashMap<>();

    @Override
    public void render(WallOfFlesh wall, float entityYaw, float partialTick,
                       PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        int collisionWidth = Mth.floor(150*2 / wall.gridSpacing);
        int collisionHeight = Mth.floor(150 / wall.gridSpacing);
        final int gridX = wall.getGridSizeX()+collisionWidth;
        final int gridY = wall.getGridSizeY()+collisionHeight;

        poseStack.pushPose();
        for (int x = 0; x < gridX; x++) {
            for (int y = 0; y < gridY; y++) {
                Vec3 offset;
                if(wall.isMovingAlongX()){
                    offset = new Vec3(
                            0,
                            y * wall.gridSpacing,
                            (x - gridX / 2.0) * wall.gridSpacing
                    );
                }else {
                    offset = new Vec3(
                            (x - gridX / 2.0) * wall.gridSpacing,
                            y * wall.gridSpacing,
                            0
                    );
                }
                poseStack.pushPose();
                poseStack.translate(offset.x, offset.y, offset.z);

                GeoBossModel<WallOfFlesh> cellModel;
                String key = x + ":" + y;
                if(!modelCache.containsKey(key)){
                    // 根据权重随机选择模型
                    int totalWeight = 0;
                    for (int weight : MODEL_WEIGHTS) {
                        totalWeight += weight;
                    }

                    int randomWeight = wall.getRandom().nextInt(totalWeight);
                    int selectedIndex = 0;
                    int cumulativeWeight = MODEL_WEIGHTS[0];

                    while (randomWeight >= cumulativeWeight && selectedIndex < MODEL_NAMES.length - 1) {
                        selectedIndex++;
                        cumulativeWeight += MODEL_WEIGHTS[selectedIndex];
                    }

                    cellModel = new GeoBossModel<>(MODEL_NAMES[selectedIndex]);
                    currentModel = cellModel;
                    modelCache.put(key, cellModel);
                }else {
                    cellModel = modelCache.get(key);
                    currentModel = cellModel;
                }
                super.render(wall, entityYaw, partialTick, poseStack, bufferSource, packedLight);
                poseStack.popPose();
            }
        }
        boolean renderHitBoxes = Minecraft.getInstance().getEntityRenderDispatcher().shouldRenderHitBoxes();

        if (renderHitBoxes && !wall.isInvisible() && !Minecraft.getInstance().showOnlyReducedInfo() && !wall.isRemoved()) {
            AABB aabb = wall.getOutsideCollisionBox().move(-wall.getX(), -wall.getY(), -wall.getZ());
            LevelRenderer.renderLineBox(poseStack, bufferSource.getBuffer(RenderType.lines()), aabb, 1, 0, 0, 1.0F);
            AABB bbaa = wall.getInsideBox().move(-wall.getX(), -wall.getY(), -wall.getZ());
            LevelRenderer.renderLineBox(poseStack, bufferSource.getBuffer(RenderType.lines()), bbaa, 0, 0, 1, 1.0F);
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

    @Override
    public GeoModel<WallOfFlesh> getGeoModel() {
        return this.currentModel;
    }
}
