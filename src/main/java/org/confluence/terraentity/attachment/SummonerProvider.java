package org.confluence.terraentity.attachment;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.confluence.terraentity.init.TEAttachments;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SummonerProvider implements ICapabilitySerializable<CompoundTag> {


    private SummonerAttachment playerAbility;
    private final LazyOptional<SummonerAttachment> abilityLazyOptional = LazyOptional.of(this::getOrCreateStorage);

    private SummonerAttachment getOrCreateStorage() {
        if (playerAbility == null) {
            this.playerAbility = new SummonerAttachment();
        }
        return playerAbility;
    }


    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction direction) {
        return TEAttachments.SUMMONER_STORAGE.orEmpty(capability, abilityLazyOptional);
    }

    @Override
    public CompoundTag serializeNBT() {
        return getOrCreateStorage().serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        getOrCreateStorage().deserializeNBT(nbt);
    }
}
