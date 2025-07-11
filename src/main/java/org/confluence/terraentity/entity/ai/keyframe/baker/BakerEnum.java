package org.confluence.terraentity.entity.ai.keyframe.baker;

import com.mojang.serialization.Codec;
import org.confluence.terraentity.entity.ai.keyframe.interpolator.IInterpolator;

import java.util.Locale;
import java.util.function.Supplier;

public enum BakerEnum {
    LINER(()->new LinearBaker()),
    PIECEWISE_BEZIER_LINEAR_INTERPOLATOR(()->new PiecewiseBezierBaker(IInterpolator.linear.get())),
    PIECEWISE_BEZIER_SPLINE2(()->new PiecewiseBezierBaker(IInterpolator.spline2.get()));

    public static Codec<BakerEnum> CODEC = Codec.STRING.xmap(
            name->BakerEnum.valueOf(name.toUpperCase()),
            baker->baker.name().toLowerCase(Locale.ROOT)
    );

    public final Supplier<AbstractKeyframeBaker> baker;
    BakerEnum(Supplier<AbstractKeyframeBaker> baker) {
        this.baker = baker;
    }
}
