package org.confluence.terraentity.entity.animal;

import com.google.common.collect.ImmutableMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.entity.util.IVanillaVariant;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class JewelSquirrel extends Squirrel implements IVanillaVariant<Integer> {

    private boolean initializedVariant = false;

    private static final EntityDataAccessor<Integer> DATA_VARIANT_ID = SynchedEntityData.defineId(JewelSquirrel.class, EntityDataSerializers.INT);

    public JewelSquirrel(EntityType<? extends Squirrel> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public void onAddedToLevel(){
        super.onAddedToLevel();
        if(!level().isClientSide && !initializedVariant){
            this.setVariant(random.nextInt(getTexturesMap().size()));
        }
    }

    @Override
    public void setVariant(@NotNull Integer integer) {
        this.entityData.set(DATA_VARIANT_ID, integer);
    }

    @Override
    public @NotNull Integer getVariant() {
        return this.entityData.get(DATA_VARIANT_ID);
    }
    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_VARIANT_ID, 0);
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putInt("Variant", this.getVariant());
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        if(pCompound.contains("Variant")) {
            this.setVariant(pCompound.getInt("Variant"));
            this.initializedVariant = true;
        }
    }

    static Map<Integer, ResourceLocation> textures = new Int2ObjectOpenHashMap<>(ImmutableMap.<Integer, ResourceLocation>builder()
            .put(0, TerraEntity.space("textures/entity/animal/squirrel/amber_squirrel.png"))
            .put(1, TerraEntity.space("textures/entity/animal/squirrel/amethyst_squirrel.png"))
            .put(2, TerraEntity.space("textures/entity/animal/squirrel/diamond_squirrel.png"))
            .put(3, TerraEntity.space("textures/entity/animal/squirrel/emerald_squirrel.png"))
            .put(4, TerraEntity.space("textures/entity/animal/squirrel/golden_squirrel.png"))
            .put(5, TerraEntity.space("textures/entity/animal/squirrel/ruby_squirrel.png"))
            .put(6, TerraEntity.space("textures/entity/animal/squirrel/sapphire_squirrel.png"))
            .put(7, TerraEntity.space("textures/entity/animal/squirrel/topaz_squirrel.png"))
            .put(8, TerraEntity.space("textures/entity/animal/squirrel/red_squirrel.png"))
            .build()
    );

    @Override
    public Map<Integer, ResourceLocation> getTexturesMap() {
        return textures;
    }

}
