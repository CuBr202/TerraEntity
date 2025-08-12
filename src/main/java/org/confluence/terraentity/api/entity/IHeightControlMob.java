package org.confluence.terraentity.api.entity;

import net.minecraft.world.phys.Vec3;

/**
 * 蠕虫需要控制飞行高度、索敌高度
 */
public interface IHeightControlMob {

    double wrapWanderHeight(Vec3 pos);

    boolean isAttackableHeight(float originalHeight);

}
