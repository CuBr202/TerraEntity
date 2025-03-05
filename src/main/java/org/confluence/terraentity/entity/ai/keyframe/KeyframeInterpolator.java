package org.confluence.terraentity.entity.ai.keyframe;



import org.confluence.terraentity.entity.ai.keyframe.interpolator.IInterpolator;

import java.util.*;
import java.util.function.Supplier;

/**
 * 2D 关键帧插值管理类
 */
public class KeyframeInterpolator{

    public List<Keyframe> keyframes;
    public List<IInterpolator> interpolators;
    double cacheValue;
    double cacheOldValue;

    public KeyframeInterpolator(List<Keyframe> keyframes, List<IInterpolator> interpolators, Supplier<? extends IInterpolator> defaultInterpolator) {
        if (keyframes == null || keyframes.size() < 2) {
            throw new IllegalArgumentException("Keyframes list must not be null and must contain at least 2 keyframes.");
        }
        this.keyframes = new ArrayList<>(keyframes);
        // Sort keyframes by time
        this.keyframes.sort(Comparator.comparingDouble(kf -> kf.time));
        if (interpolators == null || interpolators.isEmpty()) {
            throw new IllegalArgumentException("Interpolator list must not be null and must contain at least 1 interpolator.");
        }
        this.interpolators = new ArrayList<>(interpolators);
        interpolators.forEach(interpolator -> {
            interpolator.init(keyframes);
        });
        if(interpolators.size() < keyframes.size() - 1){
            for(int i = interpolators.size(); i < keyframes.size() - 1; i++){
                interpolators.add(defaultInterpolator.get());
            }
        }
    }

    public KeyframeInterpolator(List<Keyframe> keyframes, List<IInterpolator> interpolators) {
        this(keyframes, interpolators, IInterpolator.linear);
    }

    /**
     * 计算 t 处的值
     * @param t 时间

     * @return t 处的值
     */
    public double cal(double t){
        KeyframeInterval interval = findKeyframeInterval(t);
        if (interval.isBoundary()) {
            return interval.boundaryValue();
        }
        if(interval.insertPoint < 0) return keyframes.get(-interval.insertPoint - 1).value;
        IInterpolator interpolator = interpolators.get(interval.insertPoint - 1);
        cacheOldValue = cacheValue;
        cacheValue = interpolator.cal(keyframes, interval.insertPoint, t);
        return cacheValue;
    }

    /**
     * @param u 时间百分比
     * @return 插值
     */
    public double interpolator(float u){
        return cacheOldValue + u * (cacheValue - cacheOldValue);
    }

    // 辅助方法：查找关键帧区间
    private KeyframeInterval findKeyframeInterval(double t) {
        if (t <= keyframes.getFirst().time) {
            return new KeyframeInterval(0, keyframes.getFirst().value, true);
        }
        if (t >= keyframes.getLast().time) {
            return new KeyframeInterval(keyframes.size() - 1, keyframes.getLast().value, true);
        }

        // 使用二分查找找到 t 所在的区间
        int index = Collections.binarySearch(keyframes, new Keyframe(t, 0), Comparator.comparingDouble(kf -> kf.time));
        int insertPoint = -(index + 1);

        return new KeyframeInterval(insertPoint, 0, false);
    }

    // 辅助类：表示关键帧区间
    record KeyframeInterval(int insertPoint, double boundaryValue, boolean isBoundary) {
    }


    /**
     * 创建 Builder
     */
    public static Builder Builder() {
        return new Builder(IInterpolator.linear);
    }

    /**
     * 创建 Builder
     * @param defaultInterpolator 默认插值器
     */
    public static Builder Builder(Supplier<? extends IInterpolator> defaultInterpolator) {
        return new Builder(defaultInterpolator);
    }


    /**
     * Builder 类
     */
    public static class Builder {
        private final List<Keyframe> keyframes = new ArrayList<>();
        private final Map<Integer, IInterpolator> interpolatorMap = new HashMap<>();
        Supplier<? extends IInterpolator> defaultInterpolator;

        Builder(Supplier<? extends IInterpolator> defaultInterpolator){
            this.defaultInterpolator = defaultInterpolator;
        }

        /**
         * 添加关键帧
         * @param time 时间
         * @param value 值
         */
        public Builder addKeyframe(double time, double value) {
            keyframes.add(new Keyframe(time, value));
            return this;
        }

        /**
         * 添加插值器
         * @param index 区间位置 > 0
         * @param interpolator 插值器
         */
        public Builder addInterpolator(int index, IInterpolator interpolator) {
            interpolatorMap.put(index, interpolator);
            return this;
        }

        public KeyframeInterpolator build() {
            int size = keyframes.size();
            List<IInterpolator> interpolators = new ArrayList<>(size - 1);
            for (int i = 1; i < size; i++) {
                interpolators.add(interpolatorMap.getOrDefault(i, defaultInterpolator.get()));
            }
            return new KeyframeInterpolator(keyframes, interpolators);
        }
    }



}