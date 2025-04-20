package org.confluence.terraentity.mixed;

import org.confluence.terraentity.entity.npc.trade.ITradeHolder;

import javax.annotation.Nullable;

public interface IPlayer {

    @Nullable
    ITradeHolder terra_entity$getTradeHolder(); //

    void terra_entity$setTradeHolder(ITradeHolder  entity); //
}
