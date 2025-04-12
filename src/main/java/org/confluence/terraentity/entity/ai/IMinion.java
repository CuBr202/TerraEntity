package org.confluence.terraentity.entity.ai;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.players.OldUsersConverter;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import org.confluence.terraentity.mixed.SelfGetter;

import java.util.Optional;
import java.util.UUID;

public interface IMinion<T extends Mob> extends SelfGetter<T> {

    EntityDataAccessor<Optional<UUID>> getDATA_OWNER_UUID();

    default UUID minion_getOwnerUUID() {
        return (te$getSelf().getEntityData().get(getDATA_OWNER_UUID())).orElse(null);
    }

    default void minion_setOwnerUUID(UUID uuid) {
        te$getSelf().getEntityData().set(getDATA_OWNER_UUID(), Optional.ofNullable(uuid));
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
        } else if(te$getSelf().getServer()!=null) {
            String s = compound.getString("Owner");
            uuid = OldUsersConverter.convertMobOwnerIfNecessary(te$getSelf().getServer(), s);
        }
        if(uuid!=null) {
            this.minion_setOwnerUUID(uuid);
            if (te$getSelf().level() instanceof ServerLevel sl) {
                Entity owner = sl.getEntity(uuid);
                if(owner != null)
                    minion_setOwner(owner);
            }
        }
    }

}
