package org.confluence.terraentity.client.boss.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.entity.PartEntity;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.client.boss.model.GeoBossModel;
import org.confluence.terraentity.client.entity.renderer.GeoNormalRenderer;
import org.confluence.terraentity.entity.boss.wallofflesh.WallOfFlesh;
import org.confluence.terraentity.entity.boss.wallofflesh.WallOfFleshEye;
import org.confluence.terraentity.entity.boss.wallofflesh.WallOfFleshMouse;
import org.confluence.terraentity.entity.boss.wallofflesh.WallOfFleshPart;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animation.Animation;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.model.GeoModel;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.util.Tuple;
import java.util.List;

public class WallOfFleshRenderer extends GeoNormalRenderer<WallOfFlesh> {

    public WallOfFleshRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new GeoBossModel<>(MODEL_NAMES[0]), false, 1.0f, 0.5f);
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
        int collisionHeight = Mth.floor(150*2 / wall.gridSpacing);
        final int gridX = wall.getGridSizeX()+collisionWidth;
        final int gridY = wall.getGridSizeY()+collisionHeight;

        poseStack.pushPose();
        for (int x = 0; x < gridX; x++) {
            for (int y = 0; y < gridY; y++) {
                Vec3 offset;
                if(wall.isMovingAlongX()){
                    offset = new Vec3(
                            0,
                            (y - gridY / 2.0) * wall.gridSpacing,
                            (x - gridX / 2.0) * wall.gridSpacing
                    );
                }else {
                    offset = new Vec3(
                            (x - gridX / 2.0) * wall.gridSpacing,
                            (y - gridY / 2.0) * wall.gridSpacing,
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
                poseStack.scale(3.0f, 3.0f, 3.0f);
                super.render(wall, entityYaw, partialTick, poseStack, bufferSource, packedLight);
                poseStack.popPose();
            }
        }

        WallOfFleshPart @NotNull [] part = wall.getParts();

        for (WallOfFleshPart modelPart : part) {
            if (modelPart != null && modelPart.isAlive()) {
                poseStack.pushPose();

                Vec3 localOffset = null;

                int partIndex = -1;
                for (int i = 0; i < wall.subEntities.size(); i++) {
                    if (wall.subEntities.get(i) == modelPart) {
                        partIndex = i;
                        break;
                    }
                }
                
                if (partIndex >= 0) {
                    List<Tuple<Integer, Vec3>> offsets = wall.getLocalOffsets();
                    if (partIndex < offsets.size()) {
                        localOffset = offsets.get(partIndex).getB();
                    }
                }

                if (localOffset != null) {
                    poseStack.translate(localOffset.x, localOffset.y, localOffset.z);

                    if (modelPart instanceof WallOfFleshEye eye) {
                        currentModel = new GeoBossModel<>("wall_of_flesh_eye") {
                            @Override
                            public void setCustomAnimations(WallOfFlesh animatable, long instanceId,
                                                            AnimationState<WallOfFlesh> animationState) {
                                GeoBone head = this.getAnimationProcessor().getBone("Head");
                                if (head != null) {
                                    head.setRotX(0);
                                    head.setRotY(0);
                                    head.setRotZ(0);

                                    LivingEntity target = eye.target;
                                    if (target == null || !target.isAlive() || target.isRemoved()) {
                                        return;
                                    }

                                    WallOfFlesh parentMob = eye.parentMob;
                                    if (parentMob == null || !parentMob.isAlive()) {
                                        return;
                                    }

                                    Vec3 wallForward = parentMob.getForward();
                                    Vec3 targetPos = target.getEyePosition();
                                    Vec3 entityPos = eye.getEyePosition();

                                    // 计算到目标的方向向量
                                    Vec3 toTarget = targetPos.subtract(entityPos);

                                    // 计算水平距离
                                    double horizontalDistance = Math.sqrt(toTarget.x * toTarget.x + toTarget.z * toTarget.z);

                                    if (horizontalDistance > 0.001) {
                                        // 计算俯仰角（上下看的角度）
                                        float pitch = (float) Math.toDegrees(Math.atan2(-toTarget.y, horizontalDistance));
                                        pitch = Mth.clamp(pitch, -45.0F, 45.0F);

                                        // 计算偏航角（左右看的角度）
                                        float yaw = (float) Math.toDegrees(Math.atan2(toTarget.z, toTarget.x));

                                        // 计算墙体的偏航角
                                        float wallYaw = (float) Math.toDegrees(Math.atan2(wallForward.z, wallForward.x));

                                        // 计算相对于墙体的角度差
                                        float relativeYaw = yaw - wallYaw;
                                        relativeYaw = Mth.wrapDegrees(relativeYaw);

                                        // 限制头部转动范围
                                        relativeYaw = Mth.clamp(relativeYaw, -60.0F, 60.0F);

                                        // 转换为弧度并应用旋转
                                        float finalYaw = relativeYaw * 0.017453292F;
                                        float finalPitch = pitch * 0.017453292F;

                                        head.setRotY(-finalYaw);
                                        head.setRotX(-finalPitch);
                                    }
                                }
                            }

                            @Override
                            public ResourceLocation getModelResource(WallOfFlesh animatable) {
                                return TerraEntity.space("geo/entity/boss/wall_of_flesh_eye.geo.json");
                            }

                            @Override
                            public ResourceLocation getTextureResource(WallOfFlesh animatable) {
                                return TerraEntity.space("textures/entity/boss/wall_of_flesh_eye.png");
                            }

                            @Override
                            public ResourceLocation getAnimationResource(WallOfFlesh animatable) {
                                return null;
                            }

                            @Override
                            public @Nullable Animation getAnimation(WallOfFlesh animatable, String name) {
                                return null;
                            }

                        };
                    } else if (modelPart instanceof WallOfFleshMouse) {
                        currentModel = new GeoBossModel<>("wall_of_flesh_mouse");
                    }

                    poseStack.mulPose(Axis.YP.rotationDegrees(wall.getYRot()));
                    poseStack.scale(1.75f, 1.75f, 1.75f);
                    super.render(wall, entityYaw, partialTick, poseStack, bufferSource, packedLight);
                }

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
