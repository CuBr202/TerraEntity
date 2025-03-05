package org.confluence.terraentity.entity.ai.keyframe;

public class FrameUtil {

    // 辅助方法：在两个关键帧之间进行线性插值
    public static double linearInterpolateBetween(Keyframe kf0, Keyframe kf1, double t) {
        double t0 = kf0.time;
        double t1 = kf1.time;
        double v0 = kf0.value;
        double v1 = kf1.value;
        return v0 + (v1 - v0) * (t - t0) / (t1 - t0);
    }

}
