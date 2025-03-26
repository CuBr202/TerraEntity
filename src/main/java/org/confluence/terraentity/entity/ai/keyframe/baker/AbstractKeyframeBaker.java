package org.confluence.terraentity.entity.ai.keyframe.baker;



import org.confluence.terraentity.entity.ai.keyframe.Keyframe;
import org.confluence.terraentity.entity.ai.keyframe.interpolator.IInterpolator;

import java.util.List;
import java.util.function.Supplier;

/**
 * 分段关键帧烘焙器
 */
public abstract class AbstractKeyframeBaker {
    protected IInterpolator interpolator;
    public AbstractKeyframeBaker(IInterpolator interpolator) {
        this.interpolator = interpolator;

    }
    /**
     * 初始化烘焙器
     * @param keyframes 关键帧列表
     * @param position 起始位置
     */
    public abstract void init(List<Keyframe> keyframes, int position);

    /**
     * 计算指定时间点的属性值
     * @param time 时间点
     * @return 属性值
     */
    public abstract double calculate(double time);

    /**
     * 获取贝塞尔曲线分段烘焙器
     */
    public static Supplier<PiecewiseBezierBaker> PIECEWISE_BEZIER_BAKER = ()->new PiecewiseBezierBaker(IInterpolator.spline2.get());
    /**
     * 获取贝塞尔曲线分段烘焙器
     */
    public static Supplier<PiecewiseBezierBaker> PIECEWISE_BEZIER_LINEAR_INTERPOLATOR_BAKER = ()->new PiecewiseBezierBaker(IInterpolator.linear.get());

    /**
     * 获取线性烘焙器
     */
    public static Supplier<LinearBaker> LINEAR_BAKER = ()->new LinearBaker();
}
