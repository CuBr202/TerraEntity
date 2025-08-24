package org.confluence.terraentity.entity.util;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Difficulty;
import net.minecraft.world.level.Level;
import org.confluence.terraentity.utils.TEUtils;

import java.util.List;

public class DifficultSelector {

    private static boolean expert;
    private static boolean master;
    private boolean ftw;

    public DifficultSelector(Level level){
        this.changeDifficulty(level);
    }

    public void changeDifficulty(Level level){
        Difficulty difficulty = level.getDifficulty();
        if(difficulty.equals(Difficulty.PEACEFUL)){
            expert = false;
            master = false;
            ftw = false;
            return;
        }

        expert = true;
        master = true;

        if(difficulty.equals(Difficulty.EASY)) {
            master = false;
            expert = false;
        }else if(difficulty.equals(Difficulty.NORMAL)){
            master = false;
        }else{
            if(level instanceof ServerLevel serverLevel){
                this.setFtb(serverLevel);
            }
        }
    }

    public void setFtb(ServerLevel level){
        ftw = TEUtils.isFTWWorld(level);
    }

    public boolean isMaster() {
        return master;
    }

    public boolean isFtw() {
        return ftw;
    }

    public boolean isExpertise() {
        return expert;
    }

    public <T> T switchBy(T classic, T expert, T master, T ftw){
        if(!DifficultSelector.expert){
            return classic;
        }else if(!DifficultSelector.master){
            return expert;
        }else if(!this.ftw){
            return master;
        }
        return ftw;
    }

    public <T> T switchBy(List<T> list){
        if(!DifficultSelector.expert){
            return list.get(0);
        }else if(!DifficultSelector.master){
            return list.get(1);
        }else if(!this.ftw){
            return list.get(2);
        }
        return list.get(3);
    }
}
