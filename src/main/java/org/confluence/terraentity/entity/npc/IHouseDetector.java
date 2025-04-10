package org.confluence.terraentity.entity.npc;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.confluence.terraentity.api.event.HouseDetectEvent;
import org.confluence.terraentity.utils.AdapterUtils;

import java.util.List;

public interface IHouseDetector {

    BlockPos min();

    BlockPos max();

    List<BlockPos> list();

    boolean isError();

    String message();

    static IHouseDetector detect(BlockPos pos, Level level){
        var event = new HouseDetectEvent(pos, level);
        AdapterUtils.postEvent(event);
        return event.getDetector();
    }

}
