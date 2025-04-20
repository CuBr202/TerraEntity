package org.confluence.terraentity.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.confluence.lib.mixed.SelfGetter;

import org.confluence.terraentity.entity.npc.AbstractTerraNPC;
import org.confluence.terraentity.entity.npc.ITradeHolder;
import org.confluence.terraentity.entity.npc.NPCTrades;
import org.confluence.terraentity.mixed.IPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Player.class)
public abstract class PlayerMixin implements IPlayer , SelfGetter<Player> {

    @Unique
    private ITradeHolder terra_entity$interactingEntity;




    @Override
    public ITradeHolder terra_entity$getInteractingEntity() {
        return terra_entity$interactingEntity;
    }

    @Override
    public void terra_entity$setInteractingEntity(ITradeHolder  entity) {
        this.terra_entity$interactingEntity = entity;
    } // setRhyme$dave


}
