package org.confluence.terraentity.api.event;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.Event;
import net.neoforged.fml.event.IModBusEvent;
import org.confluence.terraentity.entity.summon.AbstractSummonMob;

public class SummonEvent extends Event implements IModBusEvent {
    ItemStack itemStack;
    Player player;
    AbstractSummonMob summon;
    public SummonEvent(Player player, ItemStack itemStack, AbstractSummonMob summon) {
        this.itemStack = itemStack;
        this.player = player;
        this.summon = summon;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public Player getPlayer() {
        return player;
    }

    public AbstractSummonMob getSummonMob() {
        return summon;
    }

    public static class Pre<T extends AbstractSummonMob> extends Event implements IModBusEvent {
        ItemStack itemStack;
        Player player;
        EntityType<T> summonType;
        boolean cancel;
                public Pre(Player player, ItemStack itemStack, EntityType<T> summonType) {
            this.itemStack = itemStack;
            this.player = player;
            this.summonType = summonType;
        }
        public ItemStack getItemStack() {
            return itemStack;
        }

        public Player getPlayer() {
            return player;
        }

        public EntityType<T> getSummonType() {
            return summonType;
        }

        public boolean isCancel() {
            return cancel;
        }

        public void setCancel(boolean cancel) {
            this.cancel = cancel;
        }
    }


}
