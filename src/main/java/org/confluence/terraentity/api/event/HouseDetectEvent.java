package org.confluence.terraentity.api.event;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import net.minecraftforge.eventbus.api.Event;
import org.confluence.terraentity.entity.npc.HouseDetectInfo;
import org.confluence.terraentity.entity.npc.IHouseDetector;


/**
 * 替换房屋检测
 */
public class HouseDetectEvent extends Event {
    BlockPos pos;
    Level level;
    IHouseDetector detector;

    public HouseDetectEvent(BlockPos pos, Level level) {
        this.pos = pos;
        this.level = level;
    }

    public void replace(IHouseDetector detector){
        this.detector = detector;
    }

    public IHouseDetector getDetector(){
        if(detector != null){
            return detector;
        }
        return HouseDetectInfo.detect(pos, level);
    }

}
