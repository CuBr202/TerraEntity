package org.confluence.terraentity.entity.ai.keyframe.interpolator;


import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;
import org.confluence.terraentity.entity.ai.keyframe.Keyframe;

import java.util.List;


/**
 * 线性插值
 */
public class LinearInterpolator extends AbstractInterpolator {
    PolynomialSplineFunction interpolator;
    @Override
    public double cal(List<Keyframe> keyframes, int position, double t) {
        return interpolator.value(t);
    }

    @Override
    public void init(List<Keyframe> keyframes) {
        var interpolator = new org.apache.commons.math3.analysis.interpolation.LinearInterpolator();
        double[] x = new double[keyframes.size()];
        double[] y = new double[keyframes.size()];
        for (int i = 0; i < keyframes.size(); i++) {
            x[i] = keyframes.get(i).time;
            y[i] = keyframes.get(i).value;
        }
        this.interpolator = interpolator.interpolate(x, y);
    }
}
