package org.confluence.terraentity.entity.util.trail.player;

import net.minecraft.world.entity.player.Player;
import org.confluence.terraentity.attachment.ItemInHandTrailAttachment;
import org.confluence.terraentity.entity.util.trail.TrailProperties;
import org.confluence.terraentity.init.TEAttachments;

public class ColorfulItemInHandTrail extends ItemInHandTail  {

    public ColorfulItemInHandTrail(int size, float widthScale, int length) {
        super(size, widthScale, 0, length);
    }

    @Override
    public int getRgb(Player holder, TrailProperties properties) {
        var attachment = holder.getCapability(TEAttachments.TRAIL_STORAGE).orElseGet(() -> new ItemInHandTrailAttachment());
        return attachment.tickColor(holder);
    }

}
