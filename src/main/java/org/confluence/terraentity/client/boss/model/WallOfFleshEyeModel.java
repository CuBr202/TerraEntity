package org.confluence.terraentity.client.boss.model;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.entity.boss.wallofflesh.WallOfFlesh;
import org.confluence.terraentity.entity.boss.wallofflesh.WallOfFleshEye;
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
                head.setRotX(0);
                head.setRotY(0);
                head.setRotZ(0);

                if (animatable.hasActiveAttackTarget()) {
                    LivingEntity target = animatable.getActiveAttackTarget();
                    if (target == null || !target.isAlive() || target.isRemoved()) {
                        return;
                    }

                    WallOfFlesh parentMob = animatable.getParentMob();
                    if (parentMob == null || !parentMob.isAlive()) {
                        return;
                    }

                    Vec3 wallForward = parentMob.getForward();
                    Vec3 targetPos = target.getEyePosition();
                    Vec3 entityPos = animatable.getEyePosition();

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
