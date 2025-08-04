package org.confluence.terraentity.api.entity.animation;



import org.confluence.terraentity.entity.ai.keyframe.Keyframe;
import org.confluence.terraentity.entity.ai.keyframe.interpolator.KeyframeLinearInterpolator;
import org.confluence.terraentity.entity.ai.keyframe.interpolator.KeyframeSplineInterpolator;

import java.util.List;
import java.util.function.Supplier;

/**
 * <h2>Interface for interpolators</h2>
 */
public interface IInterpolator {

    void init(List<Keyframe> keyframes, int position);

    double cal(double t);

    /**
     * 线性插值器
     */
    Supplier<KeyframeLinearInterpolator> linear = KeyframeLinearInterpolator::new;

    /**
     * 二次插值器(不保证二阶连续)
     */
//    Supplier<QuadraticInterpolator> quadratic = QuadraticInterpolator::new;

    /**
     * 二次样条插值器
     */
//    Supplier<QuadraticSplineInterpolator> quadraticSpline = QuadraticSplineInterpolator::new;

    /**
     * 三次贝塞尔曲线插值器
     */
//    Supplier<CubicBezierInterpolator> cubicBezier = CubicBezierInterpolator::new;

    /**
     * 三次贝塞尔样条插值器
     */
//    Supplier<CubicBezierSplineInterpolator> cubicBezierSpline = CubicBezierSplineInterpolator::new;

    /**
     * 三次样条插值器
     */
//    Supplier<CubicSplineInterpolator> cubicSpline = ()->new CubicSplineInterpolator();
    /**
     * 三次样条插值器
     */
    Supplier<KeyframeSplineInterpolator> spline2 = ()->new KeyframeSplineInterpolator();
    /**
     * 三次样条插值器
     */
//    Supplier<HermiteInterpolator2> hermite2 = ()->new HermiteInterpolator2();
    /**
     * Catmull-Rom样条插值器
     */
//    Supplier<CatmullRomInterpolator> catmullRom = CatmullRomInterpolator::new;

    /**
     * B样条插值器(不强制经过控制点)
     */
//    Supplier<BSplineInterpolator> bSpline = BSplineInterpolator::new;

}
