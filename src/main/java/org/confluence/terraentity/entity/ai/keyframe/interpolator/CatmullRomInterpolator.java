package org.confluence.terraentity.entity.ai.keyframe.interpolator;

import org.confluence.terraentity.entity.ai.keyframe.Keyframe;

import java.util.List;

/**
 * Catmull-Rom样条插值
 */
public class CatmullRomInterpolator extends AbstractInterpolatorP4 {

    @Override
    protected double interpolate(double u, double t0, double t1, double t2, double t3, double v0, double v1, double v2, double v3) {
        // Catmull-Rom 样条插值公式
        return 0.5 * ((2 * v1) +
                (-v0 + v2) * u +
                (2 * v0 - 5 * v1 + 4 * v2 - v3) * u * u +
                (-v0 + 3 * v1 - 3 * v2 + v3) * u * u * u);
    }

    @Override
    public void init(List<Keyframe> keyframes) {

    }
}
