package org.confluence.terraentity.entity.blur;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import org.confluence.terraentity.api.entity.blur.IMotionBlurHolder;
import org.confluence.terraentity.api.entity.blur.IMotionBlurRenderer;

import java.util.Iterator;
import java.util.function.Consumer;

/**
 * 位置旋转运动模糊子渲染器
 * @param <T> 实体类型
 */
public class PosRotMotionBlurRenderer<T extends Entity & IMotionBlurHolder<PosRotMotionBlurContext>> implements IMotionBlurRenderer<T, PosRotMotionBlurContext> {

    boolean ifRotX;
    float offsetY;

    public PosRotMotionBlurRenderer(boolean ifRotX, float offsetY) {
        this.ifRotX = ifRotX;
        this.offsetY = offsetY;
    }

    // 这是最基础的信息，将来可以用装饰器添加新渲染内容
    @Override
    public void renderBlur(PoseStack poseStack,T animatable, float partialTick, Consumer<Integer> renderCallback) {

        MotionBlurManager<PosRotMotionBlurContext> manager = animatable.getMotionBlurManager();
        Iterator<PosRotMotionBlurContext> trails = manager.iterator();
        double x = Mth.lerp(partialTick, animatable.xo, animatable.getX());
        double y = Mth.lerp(partialTick, animatable.yo, animatable.getY());
        double z = Mth.lerp(partialTick, animatable.zo, animatable.getZ());

        int maxAge = manager.size();
        int age = maxAge;
        while(trails.hasNext()) {
            age--;
            PosRotMotionBlurContext trail = trails.next();
            if (manager.getLast() == trail) continue;

            poseStack.pushPose();
            double progress = (double) (age + partialTick) / (double) maxAge;
            double pr = 1 - progress;
            pr = Math.pow(pr, 2);
            poseStack.translate((trail.pos().x - x) * pr, (trail.pos().y - y) * pr + offsetY, (trail.pos().z - z) * pr);

            float xrot = trail.xRot();
            float yrot = trail.yRot();
            poseStack.mulPose(Axis.YP.rotationDegrees(-yrot + 180));
            if (ifRotX) {
                poseStack.mulPose(Axis.XP.rotationDegrees(-xrot));
            }

            float alpha = (float) (Math.pow(progress, 2));
            int color = 0xFFFFFF | (int) ((alpha) * 0xFF * 0.3F) << 24;
            float minSize = 0.95f- 0.3f;
            float size = (float) (0.95 - minSize * pr);
            poseStack.scale(size, size, size);

            renderCallback.accept(color);

            poseStack.popPose();

        }
    }
}
