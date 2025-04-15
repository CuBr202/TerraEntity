package org.confluence.terraentity.api.event;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.ICancellableEvent;
import net.neoforged.fml.event.IModBusEvent;

import java.util.ArrayList;
import java.util.List;

public class LoadNPCNamesEvent extends Event implements IModBusEvent, ICancellableEvent {
    List<ResourceLocation> files = new ArrayList<>();
    boolean replace = false;
    public LoadNPCNamesEvent(){

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
