package org.confluence.terraentity.api.event;

import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.ICancellableEvent;
import net.neoforged.fml.event.IModBusEvent;
import org.confluence.terraentity.entity.npc.AbstractTerraNPC;

import javax.annotation.Nonnull;
import java.util.function.BiConsumer;

/**
 * 重定向交互npc事件
 */
public class InteractNPCEvent  extends Event implements IModBusEvent, ICancellableEvent {
    private AbstractTerraNPC npc;
    private Player player;
    BiConsumer<AbstractTerraNPC, Player> reDirection;
    public InteractNPCEvent(AbstractTerraNPC npc, Player player) {
        this.npc = npc;
        this.player = player;
    }

    public AbstractTerraNPC getNpc() {
        return npc;
    }

    public Player getPlayer() {
        return player;
    }

    public void setRedirection(@Nonnull  BiConsumer<AbstractTerraNPC, Player> reDirection) {
        this.reDirection = reDirection;
    }

    public void execute(BiConsumer<AbstractTerraNPC, Player> defaultAction) {
        if(reDirection!= null){
            reDirection.accept(npc, player);
        }else{
            defaultAction.accept(npc, player);
        }
    }



}
