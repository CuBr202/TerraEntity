package org.confluence.terraentity.client.animation.bone.animator.harpy;

import net.minecraft.world.entity.LivingEntity;
import org.confluence.terraentity.client.animation.bone.GeoBoneAnimator;
import org.confluence.terraentity.entity.animation.BoneStates;
import software.bernie.geckolib.cache.object.GeoBone;

public class RightWingGeoBone <T extends LivingEntity> extends GeoBoneAnimator<T> {

    public RightWingGeoBone(GeoBone bone) {
        super(bone);
    }

    @Override
    protected void init() {

    }

    @Override
    protected BoneStates defaultState() {
        return BoneStates.IDLE;
    }
}
