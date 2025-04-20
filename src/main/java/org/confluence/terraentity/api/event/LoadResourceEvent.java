package org.confluence.terraentity.api.event;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.ICancellableEvent;
import net.neoforged.fml.event.IModBusEvent;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * 加载资源文件或数据包
 */
public class LoadResourceEvent extends Event implements IModBusEvent, ICancellableEvent {
    public enum Type {
        NPC_NAMES,
        NPC_DIALOGS,
        NPC_MOODS
    }
    List<ResourceLocation> files = new ArrayList<>();
    boolean replace = false;
    Type type;

    public LoadResourceEvent(@Nullable Type type){
        this.type = type;
    }
    public Type getType(){
        return type;
    }
    public void addFile(ResourceLocation file){
        files.add(file);
    }
    public List<ResourceLocation> getFiles(){
        return files;
    }
    public void setReplace(boolean replace){
        this.replace = replace;
    }
    public boolean isReplace(){
        return replace;
    }


}
