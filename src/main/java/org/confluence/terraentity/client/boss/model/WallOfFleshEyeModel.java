package org.confluence.terraentity.client.boss.model;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.entity.boss.WallOfFleshEye;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animation.Animation;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

@SuppressWarnings("removal")
public class WallOfFleshEyeModel extends DefaultedEntityGeoModel<WallOfFleshEye> {
    public WallOfFleshEyeModel(ResourceLocation assetSubpath) {
        super(assetSubpath,true);
    }

    @Override
    public void setCustomAnimations(WallOfFleshEye animatable, long instanceId,
                                    AnimationState<WallOfFleshEye> animationState) {
        if (this.turnsHead) {
            GeoBone head = this.getAnimationProcessor().getBone(getHead());
            if (head != null) {
                float currentYaw = head.getRotY();
                float currentPitch = head.getRotX();

                if (animatable.hasActiveAttackTarget()) {
                    LivingEntity target = animatable.getActiveAttackTarget();

                    if (target == null || !target.isAlive() || target.isRemoved()) {
                        head.setRotX(0);
                        head.setRotY(0);
                        return;
                    }

                    Vec3 entityPos = animatable.getEyePosition();
                    Vec3 targetPos = target.getEyePosition();
                    Vec3 direction = targetPos.subtract(entityPos);

                    double horizontalDistance = Math.sqrt(direction.x * direction.x + direction.z * direction.z);

                    // 计算Yaw
                    float targetYaw = (float) -Math.toDegrees(Math.atan2(direction.z, direction.x)) - 90.0f;;
                    targetYaw = Mth.wrapDegrees(targetYaw);

                    float lerpSpeed = 0.47F; // 插值速度

                    // 计算俯仰角
                    float targetPitch = (float) Math.toDegrees(Math.atan2(direction.y, horizontalDistance));
                    targetPitch = Mth.clamp(targetPitch, -89.9F, 89.9F); // 限制俯仰范围

                    // 使用角度差值函数平滑过渡
                    float newYaw = Mth.rotLerp(lerpSpeed, currentYaw, targetYaw);
                    float newPitch = Mth.lerp(lerpSpeed, currentPitch, targetPitch);

                    head.setRotY((float) Math.toRadians(newYaw));
                    head.setRotX((float) Math.toRadians(newPitch));
                }
            }
        }
    }

    protected String getHead(){
        return "Head";
    }

    @Override
    public ResourceLocation getModelResource(WallOfFleshEye animatable) {
        return TerraEntity.space("geo/entity/boss/wall_of_flesh_eye.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(WallOfFleshEye animatable) {
        return TerraEntity.space("textures/entity/boss/wall_of_flesh_eye.png");
    }

    @Override
    public ResourceLocation getAnimationResource(WallOfFleshEye animatable) {
        return null;
    }

    @Override
    public @Nullable Animation getAnimation(WallOfFleshEye animatable, String name) {
        return null;
    }
}
