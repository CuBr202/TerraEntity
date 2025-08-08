package org.confluence.terraentity.entity.blur;

import org.confluence.terraentity.api.entity.blur.IMotionBlurContext;
import org.confluence.terraentity.api.entity.blur.IMotionBlurHolder;
import org.confluence.terraentity.api.entity.blur.IMotionBlurManager;

import java.util.ArrayDeque;
import java.util.Deque;

public class MotionBlurManager<C extends IMotionBlurContext> implements IMotionBlurManager<C> {

    private final Deque<C> trails = new ArrayDeque<>();
    private final int maxTrailLength; // 最大残影数量

    public MotionBlurManager(int maxTrailLength) {
        this.maxTrailLength = Math.max(1, maxTrailLength);
    }

    public void update(IMotionBlurHolder<C> entity, C trail) {
        if(entity.isMotionBlurEnabled()) {
            trails.addLast(trail);
        }else{
            trails.pollFirst();
        }

        while (trails.size() > maxTrailLength) {
            trails.pollFirst();
        }

    }

    public Deque<C> getTrails() {
        return trails;
    }

}