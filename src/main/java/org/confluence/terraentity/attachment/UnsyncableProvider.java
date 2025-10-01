package org.confluence.terraentity.attachment;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.confluence.terraentity.init.TEAttachments;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class UnsyncableProvider implements ICapabilitySerializable<CompoundTag> {


    private UnSyncableAttachment playerAbility;
    private final LazyOptional<UnSyncableAttachment> abilityLazyOptional = LazyOptional.of(this::getOrCreateStorage);

    private UnSyncableAttachment getOrCreateStorage() {
        if (playerAbility == null) {
            this.playerAbility = new UnSyncableAttachment();
        }
        return playerAbility;
    }


    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction direction) {
        return TEAttachments.UNSYNC.orEmpty(capability, abilityLazyOptional);
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
