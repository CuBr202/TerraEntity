package org.confluence.terraentity.entity.animal;

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.animal.Rabbit;
import net.minecraft.world.level.Level;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.entity.util.IVariant;
import org.confluence.terraentity.init.entity.TEAnimals;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.constant.DefaultAnimations;
import software.bernie.geckolib.util.GeckoLibUtil;

import javax.annotation.Nullable;
import java.util.Map;

public class Bunny extends Rabbit implements GeoEntity, IVariant<Integer>  {


    int idleTick = 0;
    int nextRandomWatch = 0;
    int watchCount = 20;
    int watchType = 0; // in client

    public Bunny(EntityType<? extends Bunny> entityType, Level level) {
        super(entityType, level);
        this.getAttribute(Attributes.JUMP_STRENGTH).setBaseValue(0.6f);
    }


    @Override
    public void tick() {
        super.tick();
        --this.watchCount;
        if(!level().isClientSide()) {
            if (!this.navigation.isDone()) {
                this.idleTick = 0;
            } else {
                if(++this.idleTick % 100 == 99){
                    this.nextRandomWatch = this.tickCount + this.random.nextInt(20);
                }
                if(tickCount == this.nextRandomWatch){
                    this.watchCount = 50 + 1000 * this.random.nextInt(2);
                    this.entityData.set(DATA_RANDOM_WATCH_COUNT, this.watchCount);
                }
            }

            if(this.watchCount >= 0){
                this.navigation.stop();
            }

        }

    }

    static RawAnimation random_watch_1 = RawAnimation.begin().thenPlay("watch_1");
    static RawAnimation random_watch_2 = RawAnimation.begin().thenPlay("watch_2");

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "Idle/Move", 5, state -> {

            if(this.watchCount > 0){
                if(this.watchType == 0){
                    return state.setAndContinue(random_watch_1);
                }else {
                    return state.setAndContinue(random_watch_2);
                }
            }
            return state.setAndContinue(state.isMoving() ? DefaultAnimations.WALK : DefaultAnimations.IDLE);
        }));
    }

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Nullable
    public Bunny getBreedOffspring(ServerLevel level, AgeableMob otherParent) {
        return TEAnimals.BUNNY.get().create(level);
    }

    private static final EntityDataAccessor<Integer> DATA_VARIANT_ID = SynchedEntityData.defineId(Bunny.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DATA_RANDOM_WATCH_COUNT = SynchedEntityData.defineId(Bunny.class, EntityDataSerializers.INT);


    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_VARIANT_ID, random.nextInt(getTexturesMap().size()));
        builder.define(DATA_RANDOM_WATCH_COUNT, 0);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        super.onSyncedDataUpdated(key);
        if (key == DATA_VARIANT_ID) {
            this.setTEVariant(this.entityData.get(DATA_VARIANT_ID));
        }else if(key == DATA_RANDOM_WATCH_COUNT){
            this.watchCount = this.entityData.get(DATA_RANDOM_WATCH_COUNT);
            this.watchType = this.watchCount / 1000;
            this.watchCount %= 1000;
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
        this.setTEVariant(pCompound.getInt("TEVariant"));
    }

    static Map<Integer, ResourceLocation> textures = new Int2ObjectOpenHashMap<>(Map.of(
            0, TerraEntity.space("textures/entity/animal/bunny/bunny.png"),
            1, TerraEntity.space("textures/entity/animal/bunny/amber_bunny.png"),
            2, TerraEntity.space("textures/entity/animal/bunny/amethyst_bunny.png"),
            3, TerraEntity.space("textures/entity/animal/bunny/diamond_bunny.png"),
            4, TerraEntity.space("textures/entity/animal/bunny/emerald_bunny.png"),
            5, TerraEntity.space("textures/entity/animal/bunny/golden_bunny.png"),
            6, TerraEntity.space("textures/entity/animal/bunny/ruby_bunny.png"),
            7, TerraEntity.space("textures/entity/animal/bunny/sapphire_bunny.png"),
            8, TerraEntity.space("textures/entity/animal/bunny/topaz_bunny.png")
    ));


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
}
