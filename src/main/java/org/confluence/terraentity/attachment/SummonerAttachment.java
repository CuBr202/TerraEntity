package org.confluence.terraentity.attachment;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.common.util.INBTSerializable;
import net.neoforged.neoforge.network.PacketDistributor;
import org.confluence.terraentity.network.s2c.SyncSummonPacket;

public class SummonerAttachment implements INBTSerializable<CompoundTag> {
    int currentCapacity = 1;
    int maxCapacity = 1;
    int additionalCapacity = 0;

    public void sync(ServerPlayer player) {
        PacketDistributor.sendToPlayer(player, new SyncSummonPacket(currentCapacity, maxCapacity, additionalCapacity));
    }

    public boolean canSummon(int cost) {
        return getCurrentCapacity() >= cost;
    }

    public boolean canRemove(int amount) {
        return currentCapacity + amount <= maxCapacity;
    }

    public void summon(int cost) {
        currentCapacity -= cost;
    }

    public void remove(int amount) {
        currentCapacity += amount;
    }

    public int getCurrentCapacity() {
        return currentCapacity + additionalCapacity;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }
    public void setCurrentCapacity(int currentCapacity) {
        this.currentCapacity = currentCapacity;
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public void setAdditionalCapacity(int additionalCapacity) {
        this.additionalCapacity = additionalCapacity;
    }

    public void addAdditionalCapacity(int additionalCapacity) {
        this.additionalCapacity += additionalCapacity;
    }

    public int getAdditionalCapacity() {
        return additionalCapacity;
    }

    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        tag.putInt("currentCapacity", currentCapacity);
        tag.putInt("maxCapacity", maxCapacity);
        return tag;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag tag) {
        currentCapacity = tag.getInt("currentCapacity");
        maxCapacity = tag.getInt("maxCapacity");
    }
}
