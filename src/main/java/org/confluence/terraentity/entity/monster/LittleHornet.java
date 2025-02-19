package org.confluence.terraentity.entity.monster;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import org.confluence.terraentity.entity.ai.IMinion;
import org.confluence.terraentity.entity.boss.QueenBee;
import org.confluence.terraentity.entity.monster.prefab.AbstractPrefab;

import java.util.Optional;
import java.util.UUID;

public class LittleHornet extends Hornet implements IMinion<LittleHornet> {
    QueenBee owner;

    public LittleHornet(EntityType<? extends Monster> type, Level level) {
        super(type, level, new AbstractPrefab(20,1,3,20,0,0.2f)
                .getPrefab()
                .setNoGravity()
        );
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 2, true));
        this.goalSelector.addGoal(9, new FloatGoal(this));
        this.goalSelector.addGoal(8, new BeeWanderGoal());

        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_OWNERUUID_ID, Optional.empty());
    }


    @Override
    public void tick() {
        super.tick();
        if (level() instanceof ServerLevel  sl&& this.owner != null && tickCount % 20 == 0) {
            setTarget(this.owner.getTarget());
            if(distanceTo(this.owner) > 30 && sl.getBlockState(owner.blockPosition()).is(Blocks.AIR))
                setPos(this.owner.position());
        }
    }


    /* Minion API */

    protected static final EntityDataAccessor<Optional<UUID>> DATA_OWNERUUID_ID = SynchedEntityData.defineId(LittleHornet.class, EntityDataSerializers.OPTIONAL_UUID);;

    @Override
    public EntityDataAccessor<Optional<UUID>> getDATA_OWNER_UUID() {
        return DATA_OWNERUUID_ID;
    }

    @Override
    public void minion_setOwner(Entity owner){
        if(owner instanceof QueenBee queenBee) {
            minion_setOwnerUUID(owner.getUUID());
            this.owner = queenBee;
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        minion_saveData(compound);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        minion_readData(compound);
    }
}


