package org.confluence.terraentity.mixed;

import net.minecraft.world.entity.Entity;
import org.confluence.terraentity.entity.npc.NPCTrades;

public interface IPlayer {
    NPCTrades terra_entity$getDaveTrades(); // getRhyme$daveTrades

    void terra_entity$setDaveTrades(NPCTrades NPCTrades); // setRhyme$daveTrades

    Entity terra_entity$getInteractingEntity(); // getRhyme$dave

    void terra_entity$setInteractingEntity(Entity entity); // setRhyme$dave
}
