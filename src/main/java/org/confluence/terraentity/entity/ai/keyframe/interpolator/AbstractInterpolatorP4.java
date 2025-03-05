package org.confluence.terraentity.entity.ai.keyframe.interpolator;



import org.confluence.terraentity.entity.ai.keyframe.Keyframe;

import java.util.List;

public abstract class AbstractInterpolatorP4 extends AbstractInterpolator {

    @Override
    public double cal(List<Keyframe> keyframes, int position, double t) {
        Keyframe kf0 = keyframes.get(Math.max(0, position - 2));
        Keyframe kf1 = keyframes.get(Math.max(0, position - 1));
        Keyframe kf2 = keyframes.get(Math.min(keyframes.size() - 1, position));
        Keyframe kf3 = keyframes.get(Math.min(keyframes.size() - 1, position + 1));

        double t0 = kf0.time;
        double t1 = kf1.time;
        double t2 = kf2.time;
        double t3 = kf3.time;

        double v0 = kf0.value;
        double v1 = kf1.value;
        double v2 = kf2.value;
        double v3 = kf3.value;

        double u = (t - t1) / (t2 - t1);

        return interpolate(u, t0, t1, t2, t3, v0, v1, v2, v3);

    }

    protected abstract double interpolate(double u, double t0, double t1, double t2, double t3, double v0, double v1, double v2, double v3);

}