package org.confluence.terraentity.entity.util.trail;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.Queue;

public interface ITrailKind <T, P> {

    void generateTrail(T holder, int ticks);

    TrailProperties getTrailProperties();

    @OnlyIn(Dist.CLIENT)
    void renderTrail(T holder, Queue<P> trailsQueue, Vec3 entityPos, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight);

}
