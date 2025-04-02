package org.confluence.terraentity.attachment;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.confluence.terraentity.init.TEAttachments;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class WeaponStorageProvider implements ICapabilitySerializable<CompoundTag> {


    private WeaponStorage playerAbility;
    private final LazyOptional<WeaponStorage> abilityLazyOptional = LazyOptional.of(this::getOrCreateStorage);

    private WeaponStorage getOrCreateStorage() {
        if (playerAbility == null) {
            this.playerAbility = new WeaponStorage();
        }
        return playerAbility;
    }


    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction direction) {
        return TEAttachments.WEAPON_STORAGE.orEmpty(capability, abilityLazyOptional);
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
