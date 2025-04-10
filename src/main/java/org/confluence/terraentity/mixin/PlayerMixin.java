package org.confluence.terraentity.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.confluence.lib.mixed.SelfGetter;

import org.confluence.terraentity.entity.npc.NPCTrades;
import org.confluence.terraentity.mixed.IPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Player.class)
public abstract class PlayerMixin implements IPlayer , SelfGetter<Player> {
    @Unique
    private NPCTrades rhyme$NPCTrades;
    @Unique
    private Entity rhyme$interactingEntity;

    @Override
    public NPCTrades terra_entity$getDaveTrades() {
        return rhyme$NPCTrades;
    }

    @Override
    public void terra_entity$setDaveTrades(NPCTrades NPCTrades) {
        rhyme$NPCTrades = NPCTrades;
    }

    @Override
    public Entity terra_entity$getInteractingEntity() {
        return rhyme$interactingEntity;
    } // getRhyme$dave

    @Override
    public void terra_entity$setInteractingEntity(Entity entity) {
        this.rhyme$interactingEntity = entity;
    } // setRhyme$dave


}
