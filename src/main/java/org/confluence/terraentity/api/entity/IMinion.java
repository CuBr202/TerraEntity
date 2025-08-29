package org.confluence.terraentity.api.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.players.OldUsersConverter;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import org.confluence.lib.mixed.SelfGetter;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;

/**
 * 仆从接口，这个只适用于玩家的仆从或者仅服务端实体，因为客户端只知道玩家的UUID
 */
public interface IMinion<T extends Mob> extends SelfGetter<T> {

    EntityDataAccessor<Optional<UUID>> getDATA_OWNER_UUID();

    @Contract(pure = true)
    default @Nullable UUID minion_getOwnerUUID() {
        return (confluence$self().getEntityData().get(getDATA_OWNER_UUID())).orElse(null);
    }

    default void minion_setOwnerUUID(UUID uuid) {
        confluence$self().getEntityData().set(getDATA_OWNER_UUID(), Optional.ofNullable(uuid));
    }

    default void minion_setOwner(Entity owner){
        minion_setOwnerUUID(owner.getUUID());
    }

    default void minion_saveData(CompoundTag compound) {
        if (this.minion_getOwnerUUID()!= null) {
            compound.putUUID("Owner", minion_getOwnerUUID());
        }
    }

    default void minion_readData(CompoundTag compound) {
        UUID uuid=null;
        if (compound.hasUUID("Owner")) {
            uuid = compound.getUUID("Owner");
        } else if(confluence$self().getServer()!=null) {
            String s = compound.getString("Owner");
            uuid = OldUsersConverter.convertMobOwnerIfNecessary(confluence$self().getServer(), s);
        }
        if(uuid!=null) {
            this.minion_setOwnerUUID(uuid);
            if (confluence$self().level() instanceof ServerLevel sl) {
                Entity owner = sl.getEntity(uuid);
                if(owner != null)
                    minion_setOwner(owner);
            }
        }
    }

}
