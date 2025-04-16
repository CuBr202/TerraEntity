package org.confluence.terraentity.entity.npc;

import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.DebugPackets;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;
import net.minecraft.world.entity.npc.Npc;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.confluence.terraentity.api.event.NPCEvent;
import org.confluence.terraentity.client.buffer.DebugBlocksHelper;
import org.confluence.terraentity.entity.ai.goal.NPCTradeGoal;
import org.confluence.terraentity.entity.npc.brain.NPCAi;
import org.confluence.terraentity.entity.npc.house.House;
import org.confluence.terraentity.entity.npc.house.HouseManager;
import org.confluence.terraentity.init.TEEntityDataSerializers;
import org.confluence.terraentity.init.TEItems;
import org.confluence.terraentity.item.HouseDetectItem;
import org.confluence.terraentity.menu.SimpleTradeMenu;
import org.confluence.terraentity.utils.AdapterUtils;
import org.confluence.terraentity.utils.TEUtils;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.constant.DefaultAnimations;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

/**
 * 泰拉风格的npc，集成远程攻击，交易菜单，房屋系统
 */
public class AbstractTerraNPC extends PathfinderMob implements GeoEntity ,  Npc    {

    public static final Map<MemoryModuleType<GlobalPos>, BiPredicate<AbstractTerraNPC, Holder<PoiType>>> POI_MEMORIES =
            ImmutableMap.of(
                    MemoryModuleType.HOME, (npc, poi) -> poi.is(PoiTypes.HOME),
                    MemoryModuleType.POTENTIAL_JOB_SITE, (npc, poi) -> VillagerProfession.ALL_ACQUIRABLE_JOBS.test(poi),
                    MemoryModuleType.MEETING_POINT, (npc, poi) -> poi.is(PoiTypes.MEETING)
            );


    public NPCTrades trades;
    public Player tradingPlayer;
    public House house = House.EMPTY;
    private NPCAi ai;
    private float rangeDistance = 8;
    private Predicate<AbstractTerraNPC> canPerformerAttackTest;
    public int cooldownTick = 0;
    private int _cooldownTicks;

    private static final EntityDataAccessor<NPCTrades> DATA_DAVE_DATA = SynchedEntityData.defineId(AbstractTerraNPC.class, TEEntityDataSerializers.DAVE_TRADES_SERIALIZER.get());
    private static final EntityDataAccessor<House> DATA_HOUSE_DATA = SynchedEntityData.defineId(AbstractTerraNPC.class, TEEntityDataSerializers.DAVE_HOUSE_SERIALIZER.get());
    private static final EntityDataAccessor<Boolean> DATA_RANGE_ATTACK_COOLDOWN = SynchedEntityData.defineId(AbstractTerraNPC.class, EntityDataSerializers.BOOLEAN);


    public AbstractTerraNPC(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);

        if(!level.isClientSide()){
            NPCEvent.InitNPCTradeEvent event = new NPCEvent.InitNPCTradeEvent(this, BuiltInRegistries.ENTITY_TYPE.getKey(this.getType()));
            AdapterUtils.postEvent(event);
            trades = NPCTrades.getTrade(event.getOrigin());
             if (trades != null) {
                entityData.set(DATA_DAVE_DATA, trades);
             }
             String name = NPCNames.getRandomName(BuiltInRegistries.ENTITY_TYPE.getKey(this.getType()));
             if(name!= null) {
                 this.setCustomName(Component.literal(name));
             }
        }

        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.3f);
        this.getNavigation().setCanFloat(true);

        this.setCustomNameVisible(true);
        ((GroundPathNavigation)this.getNavigation()).setCanOpenDoors(true);
        ((GroundPathNavigation)this.getNavigation()).setCanPassDoors(true);
        if(canPerformerAttackTest == null){
            canPerformerAttackTest = npc->npc.getMainHandItem().getItem() instanceof BowItem;
        }
    }

    /**
     * <p>设置npc的房屋
     * <p>使用前需要使用HouseManager.getInstance().tryAddHouse检查房屋是否可以添加</p>
     */
    public void setHouse(House house){
        this.house = house;
        this.entityData.set(DATA_HOUSE_DATA, house);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(2,new NPCTradeGoal(this));
    }

    @Override
    protected Brain<AbstractTerraNPC> makeBrain(Dynamic<?> dynamic) {
        return initAI().makeBrain(this.brainProvider().makeBrain(dynamic));
    }

    @Override
    protected Brain.Provider<AbstractTerraNPC> brainProvider() {
        return ai.brainProvider();
    }

    @Override
    public Brain<AbstractTerraNPC> getBrain() {
        return (Brain<AbstractTerraNPC>) super.getBrain();
    }

    public void refreshBrain(ServerLevel serverLevel) {
        Brain<AbstractTerraNPC> brain = this.getBrain();
        brain.stopAll(serverLevel, this);
        this.brain = brain.copyWithoutBehaviors();
        initAI().makeBrain(this.getBrain());
    }

    protected NPCAi initAI(){
        NPCEvent.NPCBrainRegisterEvent event = new NPCEvent.NPCBrainRegisterEvent(this);
        AdapterUtils.postEvent(event);
        setAttackRange(8); // 初始化晚于父类，手动提前初始化
        setCooldownTicks(20);
        if(event.getReplace() != null){
            ai = event.getReplace();
        }else {
            ai = new NPCAi(this);
        }
        return ai;
    }

    /**
     * 能否攻击敌怪，如果否，则会经常远离敌怪
     */
    public boolean canPerformerAttack(){
        return canPerformerAttackTest != null && canPerformerAttackTest.test(this);
    }

    /**
     * 设置能触发攻击状态的条件
     */
    public void setCanPerformerAttackTest(Predicate<AbstractTerraNPC> canPerformerAttackTest){
        this.canPerformerAttackTest = canPerformerAttackTest;
    }

    /**
     * 远程攻击的npc ai的走位距离
     */
    public float getAttackRange(){
        return rangeDistance;
    }

    public void setAttackRange(float rangeDistance){
        this.rangeDistance = rangeDistance;
    }

    public int getCooldownTicks(){
        return _cooldownTicks;
    }

    public void setCooldownTicks(int cooldownTicks){
        this._cooldownTicks = cooldownTicks;
    }

    public boolean isCooledDown(){
        return this.entityData.get(DATA_RANGE_ATTACK_COOLDOWN);
    }

    public void setCooledDown(boolean cooldown){
        this.entityData.set(DATA_RANGE_ATTACK_COOLDOWN, cooldown);
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
        builder.define(DATA_RANGE_ATTACK_COOLDOWN, false);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (compound.contains("te_npc_data", 10)) {
            DataResult<NPCTrades> data = NPCTrades.CODEC.parse(NbtOps.INSTANCE, compound.get("te_npc_data"));
            this.entityData.set(DATA_DAVE_DATA, data.result().get());
            this.trades = data.result().get();
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        if(trades != null) {
            DataResult<Tag> data = NPCTrades.CODEC.encodeStart(NbtOps.INSTANCE, trades);
            compound.put("te_npc_data", data.result().get());
        }
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
        this.updateSwingTime();
        if(isCooledDown()){
            this.cooldownTick++;
        }else{
            this.cooldownTick = 0;
        }
    }

    public int getCurrentSwingDuration() {
        return super.getCurrentSwingDuration();
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if(isEffectiveAi()){
            this.getBrain().tick((ServerLevel)this.level(), this);
        }

        // 用于显示房间
        if(level().isClientSide && (tickCount & 127) == 0 && !house.isEmpty()){
            if(Minecraft.getInstance().player.getMainHandItem().getItem() instanceof HouseDetectItem){
                DebugBlocksHelper.Singleton().addDebugBlock(List.of(house.min(), house.max()));
            }
        }
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        if(hand == InteractionHand.OFF_HAND){
            return super.mobInteract(player, hand);
        }


        var event = new NPCEvent.InteractNPCEvent(this, player);
        AdapterUtils.postEvent(event);
        ItemStack stack = player.getItemInHand(hand);

        if(stack.is(TEItems.HOUSE_DETECTOR.get())){
            return InteractionResult.PASS;
        }


        if(stack.getItem() instanceof ArmorItem armorItem){
            // 如果是装备，则穿上
            if(armorItem.getEquipmentSlot() == EquipmentSlot.BODY){
                this.setItemSlot(EquipmentSlot.BODY, stack.copy());
            }else if(armorItem.getEquipmentSlot() == EquipmentSlot.LEGS){
                this.setItemSlot(EquipmentSlot.LEGS, stack.copy());
            }else if(armorItem.getEquipmentSlot() == EquipmentSlot.FEET){
                this.setItemSlot(EquipmentSlot.FEET, stack.copy());
            }else if(armorItem.getEquipmentSlot() == EquipmentSlot.CHEST){
                this.setItemSlot(EquipmentSlot.CHEST, stack.copy());
            }else if(armorItem.getEquipmentSlot() == EquipmentSlot.HEAD){
                this.setItemSlot(EquipmentSlot.HEAD, stack.copy());
            }
            stack.shrink(1);
            return InteractionResult.SUCCESS;
        }
        else if(!stack.isEmpty()){
            // 如果是物品，则拿在手上
            ItemStack stack1 = stack.copy();
            this.dropEquipmentToHand(EquipmentSlot.MAINHAND, player, hand);
            this.setItemSlot(EquipmentSlot.MAINHAND, stack1.copy());
            stack.shrink(1);
            return InteractionResult.SUCCESS;
        }else if(player.isShiftKeyDown()){
            // 如果是空手按下shift，则取下装备
            Vec3 hit = TEUtils.calRayToAABB(player.getEyePosition(), player.getViewVector(0.5f), this.getBoundingBox());
            if(hit != null) {
                double dx = hit.y - position().y;
                if(dx > 1.2){
                    dropEquipmentToHand(EquipmentSlot.HEAD, player, hand);
                }else if(dx > 0.7f){
                    double dminx = Math.max(hit.x - getBoundingBox().minX, getBoundingBox().maxX - hit.x);
                    double dminz = Math.max(hit.z - getBoundingBox().minZ, getBoundingBox().maxZ - hit.z);
                    double dmin = Math.min(dminx, dminz);
                    if(dmin > 0.5f){
                        // 命中包围盒侧边，去下手中物品
                        dropEquipmentToHand(EquipmentSlot.MAINHAND, player, hand);
                    }else {
                        // 命中包围盒正面，去下胸甲
                        dropEquipmentToHand(EquipmentSlot.CHEST, player, hand);
                    }
                }else if(dx > 0.3f){
                    dropEquipmentToHand(EquipmentSlot.LEGS, player, hand);
                }else{
                    dropEquipmentToHand(EquipmentSlot.FEET, player, hand);
                }
            }
            return InteractionResult.PASS;
        }

        event.execute((npc, player1) -> {
            if(trades != null) {
                player.openMenu(new SimpleMenuProvider((id, playerInventory, player2) ->
                        new SimpleTradeMenu(id, playerInventory, trades), Component.translatable("title.terra_entity.npc_trade")));
            }
        });

//        player.openMenu(new SimpleMenuProvider((id, playerInventory, player1) -> new NPCTradesMenu(id,playerInventory, trades, forge), Component.translatable("confluence.menu.npc_shop")));
        tradingPlayer = player;
        return InteractionResult.PASS;
    }

    private void dropEquipmentToHand(EquipmentSlot slot, Player player, InteractionHand hand){
        ItemStack drop = this.getItemBySlot(slot);
        if(!drop.isEmpty()){
            player.setItemInHand(hand, drop.copy());
            this.setItemSlot(slot, ItemStack.EMPTY);
        }
    }

    public boolean isAllianceTo(LivingEntity entity){
        return entity instanceof AbstractTerraNPC || entity instanceof Player;
    }


    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "Walk/Idle", 5, state ->
                state.setAndContinue(state.isMoving() ? DefaultAnimations.WALK : DefaultAnimations.IDLE)
        ));
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
        HouseManager.getInstance().removeHouse(uuid);

    }

    protected void dropEquipment() {
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            ItemStack stack = this.getItemBySlot(slot);
            if (!stack.isEmpty()) {
                this.spawnAtLocation(stack);
                this.setItemSlot(slot, ItemStack.EMPTY);
            }
        }
    }

    private void releaseAllPois() {
        this.releasePoi(MemoryModuleType.HOME);
//        this.releasePoi(MemoryModuleType.JOB_SITE);
//        this.releasePoi(MemoryModuleType.POTENTIAL_JOB_SITE);
//        this.releasePoi(MemoryModuleType.MEETING_POINT);
    }

    protected void hurtArmor(DamageSource damageSource, float damage) {
        this.doHurtEquipment(damageSource, damage, EquipmentSlot.FEET, EquipmentSlot.LEGS, EquipmentSlot.CHEST, EquipmentSlot.HEAD);
    }

    // poi暂时没用上
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

    protected Vec3 getLeashOffset() {
        return new Vec3(-0.3, this.getEyeHeight() * 0.5f, this.getBbWidth() * 0.1F);
    }

}
