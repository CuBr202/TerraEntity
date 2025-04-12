package org.confluence.terraentity.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

import org.confluence.terraentity.entity.npc.NPCTrades;
import org.confluence.terraentity.mixed.IPlayer;
import org.confluence.terraentity.mixed.SelfGetter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Player.class)
public abstract class PlayerMixin implements IPlayer , SelfGetter<Player> {
    @Unique
    private NPCTrades terra_entity$NPCTrades;
    @Unique
    private Entity terra_entity$interactingEntity;

    @Override
    public NPCTrades terra_entity$getDaveTrades() {
        return terra_entity$NPCTrades;
    }

    @Override
    public void terra_entity$setDaveTrades(NPCTrades NPCTrades) {
        terra_entity$NPCTrades = NPCTrades;
    }

    @Override
    public Entity terra_entity$getInteractingEntity() {
        return terra_entity$interactingEntity;
    } // getRhyme$dave

    @Override
    public void terra_entity$setInteractingEntity(Entity entity) {
        this.terra_entity$interactingEntity = entity;
    } // setRhyme$dave


}
