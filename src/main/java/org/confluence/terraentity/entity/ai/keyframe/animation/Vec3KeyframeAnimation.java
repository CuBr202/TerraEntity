package org.confluence.terraentity.entity.ai.keyframe.animation;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.animation.AnimationChannel;
import net.minecraft.world.phys.Vec3;
import org.confluence.terraentity.api.entity.animation.IKeyframeAnimation;
import org.confluence.terraentity.entity.ai.keyframe.Keyframe;

import java.util.*;

public class Vec3KeyframeAnimation implements IKeyframeAnimation<Vec3> {

    private final double length;

    List<Keyframe> x;
    List<Keyframe> y;
    List<Keyframe> z;

    KeyframeAnimation xInterpolator;
    KeyframeAnimation yInterpolator;
    KeyframeAnimation zInterpolator;

    Map<Double, Vec3> cache;
    private final double endTime;

    public Vec3KeyframeAnimation(List<Keyframe> x, List<Keyframe> y, List<Keyframe> z){
        this.x = x;
        this.y = y;
        this.z = z;

        xInterpolator = new KeyframeAnimation(x);
        yInterpolator = new KeyframeAnimation(y);
        zInterpolator = new KeyframeAnimation(z);
        length = Math.min(xInterpolator.getLength(), Math.min(yInterpolator.getLength(), zInterpolator.getLength()));
        cache = new HashMap<>();
        this.endTime = Math.max(xInterpolator.getEndTime(), Math.max(yInterpolator.getEndTime(), zInterpolator.getEndTime()));
    }

    public static final Codec<Vec3KeyframeAnimation> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Keyframe.CODEC.listOf().fieldOf("x").forGetter(i->i.x),
            Keyframe.CODEC.listOf().fieldOf("y").forGetter(i->i.y),
            Keyframe.CODEC.listOf().fieldOf("z").forGetter(i->i.z)
    ).apply(instance, Vec3KeyframeAnimation::new));

    public double getEndTime() {
        return endTime;
    }


    @Override
    public Vec3 cal(double t) {
        return cache.computeIfAbsent(t, k -> new Vec3(xInterpolator.cal(t),yInterpolator.cal(t),zInterpolator.cal(t)));
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
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder 类
     */
    public static class Builder {
        private final List<Keyframe> x = new ArrayList<>();
        private final List<Keyframe> y = new ArrayList<>();
        private final List<Keyframe> z = new ArrayList<>();
        Set<Double> times;

        Builder(){
            times = new HashSet<>();
        }

        /**
         * 添加关键帧
         */
        public Builder addKeyframe(double time, Vec3 value) {
            x.add(new Keyframe(time, value.x));
            y.add(new Keyframe(time, value.y));
            z.add(new Keyframe(time, value.z));
            times.add(time);
            return this;
        }

        public Builder deleteKeyframe(double time) {
            x.removeIf(kf -> kf.time == time);
            y.removeIf(kf -> kf.time == time);
            z.removeIf(kf -> kf.time == time);
            times.remove(time);
            return this;
        }
        /**
         * 添加block bench反y方向关键帧
         */
        public Builder addKeyframeTimeStamp(double time, Vec3 value) {
            x.add(new Keyframe(time * 20, value.x));
            y.add(new Keyframe(time * 20, -value.y));
            z.add(new Keyframe(time * 20, -value.z));
            times.add(time);
            return this;
        }

        /**
         * 添加关键帧
         */
        public Builder addKeyframe(Keyframe keyframe, Vec3 value) {
            x.add(keyframe.setValue(value.x));
            y.add(keyframe.copy().setValue(value.y));
            z.add(keyframe.copy().setValue(value.z));
            times.add(keyframe.time);
            return this;
        }

        public boolean hasTime(double time) {
            return times.contains(time);
        }

        public Vec3KeyframeAnimation build() {
            return new Vec3KeyframeAnimation(x, y, z);
        }
    }

}
