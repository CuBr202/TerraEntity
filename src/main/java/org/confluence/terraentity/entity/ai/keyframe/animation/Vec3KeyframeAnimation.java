package org.confluence.terraentity.entity.ai.keyframe.animation;

import net.minecraft.client.animation.AnimationChannel;
import net.minecraft.world.phys.Vec3;
import org.confluence.terraentity.entity.ai.keyframe.Keyframe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Vec3KeyframeAnimation implements IKeyframeAnimation<Vec3> {

    KeyframeAnimation xInterpolator;
    KeyframeAnimation yInterpolator;
    KeyframeAnimation zInterpolator;
    private double length;
    public Vec3KeyframeAnimation(List<Keyframe> x, List<Keyframe> y, List<Keyframe> z){
        xInterpolator = new KeyframeAnimation(x);
        yInterpolator = new KeyframeAnimation(y);
        zInterpolator = new KeyframeAnimation(z);
        length = Math.min(xInterpolator.getLength(), Math.min(yInterpolator.getLength(), zInterpolator.getLength()));
    }


    @Override
    public Vec3 cal(double t) {
        return new Vec3(xInterpolator.cal(t),yInterpolator.cal(t),zInterpolator.cal(t));
    }

    @Override
    public double getLength() {
        return length;
    }

    public static Vec3KeyframeAnimation fromAnimation(AnimationChannel channel) {
        Builder builder = new Builder();
        Arrays.stream(channel.keyframes()).forEach(kf->{
            builder.addKeyframe(new Keyframe(kf.timestamp() * 20, 0), new Vec3(kf.target()));
        });
        return builder.build();
    }

    /**
     * 创建 Builder
     */
    public static Builder Builder() {
        return new Builder();
    }

    /**
     * Builder 类
     */
    public static class Builder {
        private final List<Keyframe> x = new ArrayList<>();
        private final List<Keyframe> y = new ArrayList<>();
        private final List<Keyframe> z = new ArrayList<>();

        Builder(){
        }

        /**
         * 添加关键帧
         */
        public Builder addKeyframe(double time, Vec3 value) {
            x.add(new Keyframe(time, value.x));
            y.add(new Keyframe(time, value.y));
            z.add(new Keyframe(time, value.z));
            return this;
        }
        /**
         * 添加block bench反y方向关键帧
         */
        public Builder addKeyframeTimeStamp(double time, Vec3 value) {
            x.add(new Keyframe(time * 20, value.x));
            y.add(new Keyframe(time * 20, -value.y));
            z.add(new Keyframe(time * 20, -value.z));
            return this;
        }

        /**
         * 添加关键帧
         */
        public Builder addKeyframe(Keyframe keyframe, Vec3 value) {
            x.add(keyframe.setValue(value.x));
            y.add(keyframe.copy().setValue(value.y));
            z.add(keyframe.copy().setValue(value.z));
            return this;
        }

        public Vec3KeyframeAnimation build() {
            return new Vec3KeyframeAnimation(x, y, z);
        }
    }

}
