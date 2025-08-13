package org.confluence.terraentity.api.entity;

import net.minecraft.world.entity.Mob;

/**
 * 宠物接口
 */
public interface IPetMob<T extends Mob> extends ISummonMob<T> {

    @Override
    default int getCost(){
        return 0;
    }

    @Override
    default boolean isPet(){
        return true;
    }
}
