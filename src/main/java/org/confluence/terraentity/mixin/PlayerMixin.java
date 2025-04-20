package org.confluence.terraentity.mixin;

import net.minecraft.world.entity.player.Player;
import org.confluence.lib.mixed.SelfGetter;

import org.confluence.terraentity.entity.npc.ITradeHolder;
import org.confluence.terraentity.mixed.IPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Player.class)
public abstract class PlayerMixin implements IPlayer , SelfGetter<Player> {

    @Unique
    private ITradeHolder terra_entity$tradeHolder;

    @Override
    public ITradeHolder terra_entity$getTradeHolder() {
        return terra_entity$tradeHolder;
    }

    @Override
    public void terra_entity$setTradeHolder(ITradeHolder  entity) {
        this.terra_entity$tradeHolder = entity;
    }

}
