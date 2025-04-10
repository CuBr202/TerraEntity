package org.confluence.terraentity.entity.npc;

import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.DebugPackets;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.CrafterMenu;
import net.minecraft.world.level.Level;

import org.confluence.terraentity.api.event.InitNPCTradeEvent;
import org.confluence.terraentity.api.event.InteractNPCEvent;
import org.confluence.terraentity.client.buffer.DebugBlocksHelper;
import org.confluence.terraentity.entity.ai.goal.NPCTradeGoal;
import org.confluence.terraentity.init.TEEntityDataSerializers;
import org.confluence.terraentity.utils.AdapterUtils;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiPredicate;

public class AbstractTerraNPC extends PathfinderMob implements GeoEntity {

    public static final Map<MemoryModuleType<GlobalPos>, BiPredicate<AbstractTerraNPC, Holder<PoiType>>> POI_MEMORIES =
            ImmutableMap.of(
                    MemoryModuleType.HOME, (npc, poi) -> poi.is(PoiTypes.HOME),
                    MemoryModuleType.POTENTIAL_JOB_SITE, (npc, poi) -> VillagerProfession.ALL_ACQUIRABLE_JOBS.test(poi),
                    MemoryModuleType.MEETING_POINT, (npc, poi) -> poi.is(PoiTypes.MEETING)
            );


    public NPCTrades trades;
    public Player tradingPlayer;
    public House house = House.EMPTY;



    private static final EntityDataAccessor<NPCTrades> DATA_DAVE_DATA = SynchedEntityData.defineId(AbstractTerraNPC.class, TEEntityDataSerializers.DAVE_TRADES_SERIALIZER.get());
    private static final EntityDataAccessor<House> DATA_HOUSE_DATA = SynchedEntityData.defineId(AbstractTerraNPC.class, TEEntityDataSerializers.DAVE_HOUSE_SERIALIZER.get());


    public AbstractTerraNPC(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);

        if(!level.isClientSide()){
            InitNPCTradeEvent event = new InitNPCTradeEvent(this, BuiltInRegistries.ENTITY_TYPE.getKey(this.getType()));
            AdapterUtils.postEvent(event);
            trades = NPCTrades.getTrade(event.getOrigin());
             if (trades != null) {
                entityData.set(DATA_DAVE_DATA, trades);
            }
        }

        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.3f);
        this.getNavigation().setCanFloat(true);

    }

    public void setHouse(House house){
        this.house = house;
        this.entityData.set(DATA_HOUSE_DATA, house);
    }

    @Override
    protected void registerGoals() {
//        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(2,new NPCTradeGoal(this));
//
//        this.goalSelector.addGoal(5, new AvoidEntityGoal<>(this, Monster.class, 20, 0.3f, 0.3f));
//        this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 1.0));
//        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 10.0F));
//        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));

    }

    @Override
    protected Brain<?> makeBrain(Dynamic<?> dynamic) {
        return NPCAi.makeBrain(this.brainProvider().makeBrain(dynamic));
    }

    @Override
    protected Brain.Provider<AbstractTerraNPC> brainProvider() {
        return NPCAi.brainProvider();
    }

    @Override
    public Brain<AbstractTerraNPC> getBrain() {
        return (Brain<AbstractTerraNPC>) super.getBrain();
    }


    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        super.onSyncedDataUpdated(key);
        if (level().isClientSide() && DATA_DAVE_DATA.equals(key)) {
            this.trades = this.entityData.get(DATA_DAVE_DATA);
        }else if(DATA_HOUSE_DATA.equals(key)){
            this.house = this.entityData.get(DATA_HOUSE_DATA);
        }
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_DAVE_DATA, new NPCTrades(List.of()));
        builder.define(DATA_HOUSE_DATA, House.EMPTY);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (compound.contains("dave_data", 10)) {
            DataResult<NPCTrades> data = NPCTrades.CODEC.parse(NbtOps.INSTANCE, compound.get("dave_data"));
            this.entityData.set(DATA_DAVE_DATA, data.result().get());
            this.trades = data.result().get();
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
//        DataResult<Tag> data = NPCTrades.CODEC.encodeStart(NbtOps.INSTANCE, trades);
//        compound.put("dave_data",data.result().get());
    }


    @Override
    public void onAddedToLevel() {
        super.onAddedToLevel();
        if(level().isClientSide()){
            this.trades = this.entityData.get(DATA_DAVE_DATA);
        }

    }

    @Override
    public void tick(){
        super.tick();
        if(!level().isClientSide()) {
            // TODO 攻击目标
//            LivingEntity entity = TEUtils.getAABBAngleTarget(this.position(), position().add(getLookAngle()), level(), this, 10, 180, e -> true);
//            this.brain.setMemory(MemoryModuleType.ATTACK_TARGET,
//                    entity
//            );
//            if(this.brain.hasMemoryValue(MemoryModuleType.ATTACK_TARGET)){
//                this.brain.getActiveNonCoreActivity().ifPresent(activity -> {
//                    System.out.println(activity);
//                    this.brain.setMemory(MemoryModuleType.NEAREST_ATTACKABLE, entity);
//                });
//            }

//            System.out.println("water: "+ getBrain().getMemory(MemoryModuleType.HOME));
//            System.out.println("long: "+ getBrain().getMemory(MemoryModuleType.LONG_JUMP_MID_JUMP));


        }
    }

    public void setHome(BlockPos pos){
        this.brain.setMemory(MemoryModuleType.HOME,  GlobalPos.of(level().dimension(), pos));
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if(isEffectiveAi()){
            this.getBrain().tick((ServerLevel)this.level(), this);
        }

        // 用于显示房间
        if(!house.isEmpty()){
            if(level().isClientSide && tickCount % 100 == 0){
                DebugBlocksHelper.Singleton().addDebugBlock(List.of(house.min(), house.max()));
            }
        }
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        var event = new InteractNPCEvent(this, player);
        AdapterUtils.postEvent(event);
        event.execute((npc,player1)->{

        });

//        player.openMenu(new SimpleMenuProvider((id, playerInventory, player1) -> new NPCTradesMenu(id,playerInventory, trades, forge), Component.translatable("confluence.menu.npc_shop")));
        tradingPlayer = player;
        return InteractionResult.PASS;
    }


    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

    }

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.ATTACK_DAMAGE)
                .add(Attributes.MAX_HEALTH)
                .add(Attributes.ARMOR)
                .add(Attributes.MOVEMENT_SPEED)
                .add(Attributes.FOLLOW_RANGE)
                .add(Attributes.KNOCKBACK_RESISTANCE)
                ;
    }




    @Override
    public void startSleeping(BlockPos pos) {
        super.startSleeping(pos);
        this.brain.setMemory(MemoryModuleType.LAST_SLEPT, this.level().getGameTime());
        this.brain.eraseMemory(MemoryModuleType.WALK_TARGET);
        this.brain.eraseMemory(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);
    }

    @Override
    public void stopSleeping() {
        super.stopSleeping();
        this.brain.setMemory(MemoryModuleType.LAST_WOKEN, this.level().getGameTime());
    }

    @Override
    public void die(DamageSource cause) {

        this.releaseAllPois();
        super.die(cause);
    }

    private void releaseAllPois() {
        this.releasePoi(MemoryModuleType.HOME);
//        this.releasePoi(MemoryModuleType.JOB_SITE);
//        this.releasePoi(MemoryModuleType.POTENTIAL_JOB_SITE);
//        this.releasePoi(MemoryModuleType.MEETING_POINT);
    }

    public void releasePoi(MemoryModuleType<GlobalPos> moduleType) {
        if (this.level() instanceof ServerLevel) {
            MinecraftServer minecraftserver = ((ServerLevel)this.level()).getServer();
            this.brain.getMemory(moduleType).ifPresent(pos -> {
                ServerLevel serverlevel = minecraftserver.getLevel(pos.dimension());
                if (serverlevel != null) {
                    PoiManager poimanager = serverlevel.getPoiManager();
                    Optional<Holder<PoiType>> optional = poimanager.getType(pos.pos());
                    BiPredicate<AbstractTerraNPC, Holder<PoiType>> bipredicate = POI_MEMORIES.get(moduleType);
                    if (optional.isPresent() && bipredicate.test(this, optional.get())) {
                        poimanager.release(pos.pos());
                        DebugPackets.sendPoiTicketCountPacket(serverlevel, pos.pos());
                    }
                }
            });
        }
    }
}
