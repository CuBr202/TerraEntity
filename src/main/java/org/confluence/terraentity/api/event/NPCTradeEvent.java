package org.confluence.terraentity.api.event;

import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.ICancellableEvent;
import net.neoforged.fml.event.IModBusEvent;
import org.confluence.terraentity.registries.npc_trade.ITrade;

import java.util.function.BiConsumer;

/**
 * 当交易时触发
 */
public class NPCTradeEvent  extends Event implements IModBusEvent, ICancellableEvent {
    ITrade trade;
    Player player;
    boolean alwaysPass = false;
    BiConsumer<Player, ITrade> reDirection;
    public NPCTradeEvent(ITrade trade, Player player) {
        this.trade = trade;
        this.player = player;
    }

    public ITrade getTrade() {
        return trade;
    }

    public Player getPlayer() {
        return player;
    }
    public void setAlwaysPass(boolean alwaysPass) {
        this.alwaysPass = alwaysPass;
    }

    public boolean isAlwaysPass() {
        return alwaysPass;
    }

    public void setRedirection(BiConsumer<Player, ITrade> reDirection) {
        this.reDirection = reDirection;
    }

    public BiConsumer<Player, ITrade> getRedirection() {
        return reDirection;
    }


}
