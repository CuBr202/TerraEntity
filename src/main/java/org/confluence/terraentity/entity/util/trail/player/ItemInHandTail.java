package org.confluence.terraentity.entity.util.trail.player;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.confluence.terraentity.attachment.ItemInHandTrailAttachment;
import org.confluence.terraentity.entity.util.trail.PositionPoseProperties;
import org.confluence.terraentity.entity.util.trail.PositionPoseTrail;
import org.confluence.terraentity.entity.util.trail.TrailProperties;

public class ItemInHandTail extends PositionPoseTrail<Player> {

    public ItemInHandTail(int size, float widthScale, int color) {
        super(size, widthScale, color);
    }

    @Override
    public int getRgb(Player holder, TrailProperties properties) {
        return properties.colorFrom();
    }


    @Override
    public void generateTrail(Player holder, int ticks) {
    }

    public void generateTrail(Player player, int ticks, float partialTicks, ItemInHandTrailAttachment data, Vec3 handPos) {
        if(partialTicks > 0.5f && data.trailTicks < player.tickCount) {
            data.trailTicks = player.tickCount;
            PositionPoseProperties properties = new PositionPoseProperties(handPos, 0, 0);
            properties.color = data.tickColor(player);
            trailsQueue.add(properties);

            if (trailsQueue.size() > 10) {
                trailsQueue.poll();
                trailsQueue.poll();
            }
        }
    }
}
