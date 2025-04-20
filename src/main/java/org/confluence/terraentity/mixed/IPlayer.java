package org.confluence.terraentity.mixed;

import org.confluence.terraentity.entity.npc.ITradeHolder;

import javax.annotation.Nullable;

public interface IPlayer {

    @Nullable
    ITradeHolder terra_entity$getInteractingEntity(); //

    void terra_entity$setInteractingEntity(ITradeHolder  entity); //
}
