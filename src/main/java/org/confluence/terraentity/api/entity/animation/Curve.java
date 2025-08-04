package org.confluence.terraentity.api.entity.animation;

import net.minecraft.world.phys.Vec3;

public interface Curve {
    Vec3 cal(double t);
}
