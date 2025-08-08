package org.confluence.terraentity.api.entity.blur;

import org.jetbrains.annotations.NotNull;

import java.util.Deque;
import java.util.Iterator;

/**
 * 实体残影管理器接口
 * @param <C> 残影数据类型
 */
public interface IMotionBlurManager <C extends IMotionBlurContext> extends Iterable<C>{

    /**
     * 获取所有残影
     */
    Deque<C> getTrails();

    /**
     * 更新残影
     */
    void update(IMotionBlurHolder<C> entity,C trail);


    default @NotNull Iterator<C> iterator(){
        return getTrails().iterator();
    }

    /**
     * 获取最后一个残影
     */
    default C getLast(){
        return getTrails().getLast();
    }

    /**
     * 获取残影数量
     */
    default int size(){
        return getTrails().size();
    }

    default boolean isEmpty(){
        return getTrails().isEmpty();
    }

    default void clear(){
        getTrails().clear();
    }

}
