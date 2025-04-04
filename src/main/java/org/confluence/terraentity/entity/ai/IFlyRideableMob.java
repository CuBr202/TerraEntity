package org.confluence.terraentity.entity.ai;

import net.minecraft.world.entity.PlayerRideableJumping;

public interface IFlyRideableMob extends PlayerRideableJumping {

    float calJumpingScale(float jumpTick);

}
