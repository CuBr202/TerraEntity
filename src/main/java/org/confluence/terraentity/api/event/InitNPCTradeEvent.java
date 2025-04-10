package org.confluence.terraentity.api.event;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.ICancellableEvent;
import net.neoforged.fml.event.IModBusEvent;
import org.confluence.terraentity.entity.npc.AbstractTerraNPC;

import javax.annotation.Nonnull;

/**
 * 重定向NPC交易列表
 */
public class InitNPCTradeEvent  extends Event implements IModBusEvent, ICancellableEvent {
    private AbstractTerraNPC npc;
    private ResourceLocation origin;
    public InitNPCTradeEvent(AbstractTerraNPC npc, ResourceLocation origin) {
        this.npc = npc;
        this.origin = origin;
    }

    public AbstractTerraNPC getNpc() {
        return npc;
    }

    public void setRedirection(@Nonnull ResourceLocation newResource) {
        this.origin = newResource;
    }

    public ResourceLocation getOrigin() {
        return origin;
    }

}
