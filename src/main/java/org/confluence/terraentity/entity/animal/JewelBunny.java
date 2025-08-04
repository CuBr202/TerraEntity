package org.confluence.terraentity.entity.animal;

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.data.init.loot.TELootParams;
import org.confluence.terraentity.api.entity.IVariant;
import org.confluence.terraentity.init.entity.TEAnimals;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Map;

public class JewelBunny extends Bunny implements IVariant<Integer> {
    private boolean initializedVariant = false;

    private static final EntityDataAccessor<Integer> DATA_VARIANT_ID = SynchedEntityData.defineId(JewelBunny.class, EntityDataSerializers.INT);

    public JewelBunny(EntityType<? extends JewelBunny> entityType, Level level) {
        super(entityType, level);
    }

    public void onAddedToWorld() {
        super.onAddedToWorld();
        if(!level().isClientSide && !initializedVariant){
            this.setTEVariant(random.nextInt(getTexturesMap().size()));
        }
    }
    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putInt("TEVariant", this.getTEVariant());
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        if(pCompound.contains("TEVariant")) {
            this.setTEVariant(pCompound.getInt("TEVariant"));
            this.initializedVariant = true;
        }
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_VARIANT_ID, random.nextInt(getTexturesMap().size()));
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        super.onSyncedDataUpdated(key);
        if (key == DATA_VARIANT_ID) {
            this.setTEVariant(this.entityData.get(DATA_VARIANT_ID));
        }
    }

    static Map<Integer, ResourceLocation> textures = new Int2ObjectOpenHashMap<>(Map.of(
            0, TerraEntity.space("textures/entity/animal/bunny/amber_bunny.png"),
            1, TerraEntity.space("textures/entity/animal/bunny/amethyst_bunny.png"),
            2, TerraEntity.space("textures/entity/animal/bunny/diamond_bunny.png"),
            3, TerraEntity.space("textures/entity/animal/bunny/emerald_bunny.png"),
            4, TerraEntity.space("textures/entity/animal/bunny/golden_bunny.png"),
            5, TerraEntity.space("textures/entity/animal/bunny/ruby_bunny.png"),
            6, TerraEntity.space("textures/entity/animal/bunny/sapphire_bunny.png"),
            7, TerraEntity.space("textures/entity/animal/bunny/topaz_bunny.png")
    ));


    @Override
    protected void dropFromLootTable(DamageSource damageSource, boolean hitByPlayer) {
        ResourceLocation resourcelocation = this.getLootTable();
        LootTable loottable = this.level().getServer().getLootData().getLootTable(resourcelocation);
        LootParams.Builder lootparams$builder = (new LootParams.Builder((ServerLevel)this.level())).withParameter(LootContextParams.THIS_ENTITY, this).withParameter(LootContextParams.ORIGIN, this.position()).withParameter(LootContextParams.DAMAGE_SOURCE, damageSource).withOptionalParameter(LootContextParams.KILLER_ENTITY, damageSource.getEntity()).withOptionalParameter(LootContextParams.DIRECT_KILLER_ENTITY, damageSource.getDirectEntity());
        if (hitByPlayer && this.lastHurtByPlayer != null) {
            lootparams$builder = lootparams$builder.withParameter(TELootParams.VARIANT, this.getTEVariant()).withParameter(LootContextParams.LAST_DAMAGE_PLAYER, this.lastHurtByPlayer).withLuck(this.lastHurtByPlayer.getLuck());
        }

        LootParams lootparams = lootparams$builder.create(LootContextParamSets.ENTITY);
        loottable.getRandomItems(lootparams, this.getLootTableSeed(), this::spawnAtLocation);

    }

    @Override
    public Map<Integer, ResourceLocation> getTexturesMap() {
        return textures;
    }

    @Override
    public Integer getTEVariant() {
        return this.entityData.get(DATA_VARIANT_ID);
    }

    @Override
    public void setTEVariant(Integer variant) {
        this.entityData.set(DATA_VARIANT_ID, variant);
    }

    @Nullable
    public Bunny getBreedOffspring(ServerLevel level, AgeableMob otherParent) {
        return TEAnimals.JEWEL_BUNNY.get().create(level);
    }
}
