package org.confluence.terraentity.entity.ai.keyframe.interpolator;



import org.confluence.terraentity.entity.ai.keyframe.Keyframe;

import java.util.List;
import java.util.function.Supplier;

/**
 * <h2>Interface for interpolators</h2>
 */
public interface IInterpolator {
    double cal(List<Keyframe> keyframes, int position, double t);

    void init(List<Keyframe> keyframes);

    /**
     * 线性插值器
     */
    Supplier<LinearInterpolator> linear = LinearInterpolator::new;

    /**
     * 三次样条插值器
     */
    Supplier<CubicSplineInterpolator> cubicSpline = ()->new CubicSplineInterpolator();

    /**
     * Catmull-Rom样条插值器
     */
    Supplier<CatmullRomInterpolator> catmullRom = CatmullRomInterpolator::new;

}
