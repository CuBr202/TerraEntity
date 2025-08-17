package org.confluence.terraentity.attachment;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.confluence.terraentity.entity.boss.hillofflesh.HillOfFlesh;
import org.confluence.terraentity.network.s2c.ClientBoundEventPacket;
import org.jetbrains.annotations.UnknownNullability;

public class UnSyncableAttachment implements INBTSerializable<CompoundTag> {

    HillOfFlesh fightingHillOfFlesh;
    /**
     * 防止在风暴中走不出来
     */
    int invulnerableStormTime = 20;


    public void setFightingHillOfFlesh(HillOfFlesh fightingHillOfFlesh) {
        this.fightingHillOfFlesh = fightingHillOfFlesh;
    }

    public HillOfFlesh getFightingHillOfFlesh() {
        return fightingHillOfFlesh;
    }

    public void triggerInvulnerableStorm(LivingEntity entity) {
        invulnerableStormTime = 40;
        if(entity instanceof ServerPlayer player){
            ClientBoundEventPacket.resetCrimsonStorm(player);
        }
    }

    public void tickInvulnerableStorm() {
        if (invulnerableStormTime > 0) {
            invulnerableStormTime--;
        }
    }

    public boolean isInvulnerableStormActive() {
        return invulnerableStormTime > 0;
    }



    @Override
    public @UnknownNullability CompoundTag serializeNBT(HolderLookup.Provider provider) {
        return new CompoundTag();
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag tag) {
    }
}
