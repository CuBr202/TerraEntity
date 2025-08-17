package org.confluence.terraentity.entity.boss.hillofflesh;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.JsonOps;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.Mth;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.frog.Frog;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.entity.ai.fsm.CircleMobSkills;
import org.confluence.terraentity.entity.ai.fsm.MobSkill;
import org.confluence.terraentity.entity.ai.goal.*;
import org.confluence.terraentity.entity.ai.keyframe.animation.Vec3KeyframeAnimation;
import org.confluence.terraentity.entity.animation.ModelPositionTable;
import org.confluence.terraentity.entity.boss.AbstractTerraBossBase;
import org.confluence.terraentity.entity.monster.BaseWorm;
import org.confluence.terraentity.entity.monster.BaseWormPart;
import org.confluence.terraentity.entity.monster.TheHungry;
import org.confluence.terraentity.entity.monster.prefab.AbstractPrefab;
import org.confluence.terraentity.init.TEAttachments;
import org.confluence.terraentity.init.TEEffects;
import org.confluence.terraentity.init.TETags;
import org.confluence.terraentity.init.entity.TEMonsterEntities;
import org.confluence.terraentity.utils.TEUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.constant.DefaultAnimations;

import java.util.*;

public class HillOfFlesh extends AbstractTerraBossBase<HillOfFlesh>  {

    public float innerRadius = 10;
    public float outerRadius = 50;
    public static int spawnTick = 150;
    HillOfFleshPart[] subEntities;
    Map<String, HillOfFleshPart> namePartMap;
    static final BiMap<EntityDataAccessor<Integer>,Integer> targetMap  = HashBiMap.create();
//    FSMGoal<HillOfFlesh> fsmGoal;
    Set<LivingEntity> innerEntities;
    List<LivingEntity> nearbyLivings;

    private static JsonElement animJson;
    static ModelPositionTable table;


    private static final EntityDataAccessor<Boolean> DATA_INIT = SynchedEntityData.defineId(HillOfFlesh.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Integer> DATA_TARGET_0 = SynchedEntityData.defineId(HillOfFlesh.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DATA_TARGET_1 = SynchedEntityData.defineId(HillOfFlesh.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DATA_TARGET_2 = SynchedEntityData.defineId(HillOfFlesh.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DATA_TARGET_3 = SynchedEntityData.defineId(HillOfFlesh.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DATA_TARGET_4 = SynchedEntityData.defineId(HillOfFlesh.class, EntityDataSerializers.INT);

    static {
        targetMap.put(DATA_TARGET_0,0);
        targetMap.put(DATA_TARGET_1,1);
        targetMap.put(DATA_TARGET_2,2);
        targetMap.put(DATA_TARGET_3,3);
        targetMap.put(DATA_TARGET_4,4);
    }

    public HillOfFlesh(EntityType<? extends HillOfFlesh> type, Level level) {
        super(type, level, 100, 10);
        this.subEntities = new HillOfFleshPart[10];
        this.namePartMap = new HashMap<>();
        this.addPart(new HillOfFleshEye(this, "Eye0", 3, 3));
        this.addPart(new HillOfFleshEye(this, "Eye1", 3, 3));
        this.addPart(new HillOfFleshEye(this, "Eye2", 3, 3));
        this.addPart(new HillOfFleshEye(this, "Eye3", 3, 3));
        this.addPart(new HillOfFleshEye(this, "Eye4", 3, 3));

        this.addPart(new HillOfFleshMouse(this, "Mouth0", 4, 4));
        this.addPart(new HillOfFleshMouse(this, "Mouth1", 4, 4));
        this.addPart(new HillOfFleshMouse(this, "Mouth2", 4, 4));
        this.addPart(new HillOfFleshMouse(this, "Mouth3", 4, 4));
        this.addPart(new HillOfFleshMouse(this, "Mouth4", 4, 4));
        this.setId(ENTITY_COUNTER.getAndAdd(this.subEntities.length + 1) + 1);

        setNoGravity(false);
//        this.fsmGoal = new HillOfFleshFSMGoal(this, DATA_SKILL_INDEX);
        this.innerEntities = new HashSet<>();
        this.nearbyLivings = new ArrayList<>();

        if(animJson!=null && table == null){
            table = ModelPositionTable.CODEC.decode(JsonOps.INSTANCE, animJson).result().get().getFirst();
        }

    }


    public static void readAnimJson(ResourceManager manager){
        try{
            animJson = JsonParser.parseReader(manager.getResource(TerraEntity.space("animationtable/hill_of_flesh_parts.json")).get().openAsReader());
        }catch (Exception e){
            TerraEntity.LOGGER.error("Error reading animation table for HillOfFlesh: {0}", e);

        }
    }

    @Override
    public boolean canBeSeenAsEnemy() {
        return super.canBeSeenAsEnemy() && !this.isSpawning();
    }

    @Override
    public boolean canAttack(LivingEntity target) {
        return this.level().getDifficulty() != Difficulty.PEACEFUL && target.canBeSeenAsEnemy()
                && !(target instanceof TheHungry || target instanceof BaseWorm<?>);
    }

//    static class HillOfFleshFSMGoal extends FSMGoal<HillOfFlesh> {
//
//
//        public HillOfFleshFSMGoal(HillOfFlesh mob, EntityDataAccessor<Integer> skillIndexData) {
//            super(mob, skillIndexData);
//        }
//
//        static class Spawn extends MobSkill<HillOfFlesh>{
//            public Spawn() {
//                super(null, spawnTick, 0);
//            }
//            @Override
//            public void start(HillOfFlesh mob){
//
//            }
//            @Override
//            public void tick(HillOfFlesh mob, int time){
//
//
//            }
//            @Override
//            public void stop(HillOfFlesh mob){
//
//            }
//        }
//
//        @Override
//        public void init(CircleMobSkills<HillOfFlesh> skills) {
//
//
//
//        }
//    }

    static class SummonGoal extends Goal{

        Mob mob;
        int interval;
        int time;
        int maxCount;
        int count;

        public SummonGoal(Mob mob, int interval, int maxCount){
            this.mob = mob;
            this.interval = interval / 2;
            this.time = interval;
            this.maxCount = maxCount;
            this.count = 0;
        }

        @Override
        public void tick(){
            if(this.count < maxCount && --time <= 0){
                this.spawnLeech(this.mob.getTarget());
                time = interval;
            }
        }

        private void spawnLeech(LivingEntity target) {
            if (this.mob.level() instanceof ServerLevel serverLevel) {
                BaseWorm warm = TEUtils.spawnEntity(()->new BaseWorm(TEMonsterEntities.LEECH.get(), this.mob.level(), AbstractPrefab.WARM_BUILDER.get()){
                    @Override
                    protected BaseWormPart createPart(int index) {
                        return new BaseWormPart(this, index);
                    }

                    @Override
                    public boolean hurt(DamageSource source, float amount) {
                        if(source.getEntity() != null && source.getEntity().is(SummonGoal.this.mob)) {
                            return false;
                        }
                        return super.hurt(source, amount);
                    }

                    @Override
                    public boolean canAttack(LivingEntity entity) {
                        return this.getType() != entity.getType() && SummonGoal.this.mob.canAttack(entity);
                    }

                    @Override
                    public void onRemovedFromLevel() {
                        super.onRemovedFromLevel();
                        SummonGoal.this.count--;
                    }
                    @Override
                    protected void registerGoals() {
                        super.registerGoals();
                        this.targetSelector.addGoal(2, new MutableRangeNearestAttackableTargetGoal<>(this, Player.class,false, LivingEntity::canBeSeenAsEnemy));
                    }
                    @Override
                    protected float getMoveSpeedModifier(){
                        return 2.0f;
                    }

                    @Override
                    public boolean fireImmune() {
                        return true;
                    }

                }, serverLevel, this.mob.position().add(this.mob.getForward().normalize().scale(1)));
                if(warm!= null) {
                    warm.setTarget(target);
                    warm.getAttribute(Attributes.FOLLOW_RANGE).addPermanentModifier(new AttributeModifier(
                            TerraEntity.space("hill"), 50, AttributeModifier.Operation.ADD_VALUE
                    ));
                    serverLevel.addFreshEntity(warm);
                    this.count++;
                }
            }
        }

        @Override
        public boolean canUse() {
            return mob.getTarget() != null && mob.tickCount > HillOfFlesh.spawnTick;
        }
    }

    @Override
    protected @NotNull Brain<?> makeBrain(@NotNull Dynamic<?> dynamic) {
        return this.brainProvider().makeBrain(dynamic);
    }


    @Override
    protected Brain.@NotNull Provider<Frog> brainProvider() {
        return Brain.provider(List.of(), List.of());
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new SummonGoal(this, 300, 5));

        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Player.class, false));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, IronGolem.class, false));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_INIT, true);
        builder.define(DATA_TARGET_0, -1);
        builder.define(DATA_TARGET_1, -1);
        builder.define(DATA_TARGET_2, -1);
        builder.define(DATA_TARGET_3, -1);
        builder.define(DATA_TARGET_4, -1);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        super.onSyncedDataUpdated(key);
        Integer index = targetMap.get(key);
        if(index != null){
            if(level().isClientSide) {
                int id =(int) this.entityData.get(key);
                if(id != -1) {
                    if(this.level().getEntity(id) instanceof LivingEntity living){
                        this.subEntities[index].changeTarget(living);
                        if(living instanceof Player player){
                            player.getData(TEAttachments.UNSYNC).setFightingHillOfFlesh(this);
                            player.addEffect(new MobEffectInstance(TEEffects.CRIMSON_STORM, -1, 0));
                        }
                    }else {
                        this.subEntities[index].target = null;
                    }
                }else{
                    this.subEntities[index].target = null;
                }
            }
        }
    }

    @Override
    public void onAddedToLevel(){
        super.onAddedToLevel();
//        if(!level().isClientSide){
//            this.goalSelector.addGoal(0, fsmGoal);
//        }
        for(var entity : this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(this.innerRadius))){
            entity.getData(TEAttachments.UNSYNC).setFightingHillOfFlesh(this);
            entity.addEffect(new MobEffectInstance(TEEffects.CRIMSON_STORM, -1, 0),this);
        }
    }

    void setTarget(int index, @Nullable LivingEntity target) {
        if(!level().isClientSide) {
            this.subEntities[index].target = target;
            this.entityData.set(targetMap.inverse().get(index), target == null ? -1 : target.getId());
            if(target != null){
                this.innerEntities.add(target);
                target.addEffect(new MobEffectInstance(TEEffects.CRIMSON_STORM, -1),this);
                target.getData(TEAttachments.UNSYNC).setFightingHillOfFlesh(this);
            }
        }
    }

    @Override
    public void aganinSpawn(){
        this.entityData.set(DATA_INIT, false);

    }

    public boolean isSpawning(){
        return this.getSpawnProgress(1) < 1.0f;
    }

    public float getSpawnProgress(float partialTicks){
        if(!this.entityData.get(DATA_INIT)){
            return 1.0f;
        }
        float progress = (this.tickCount + partialTicks) / spawnTick;
        return Math.min(progress, 1.0f);
    }


    private void addPart(HillOfFleshPart part) {
        this.subEntities[this.namePartMap.size()] = part;
        this.namePartMap.put(part.name, part);
    }

    public HillOfFleshPart getPart(String name){
        return this.namePartMap.get(name);
    }

    @Override
    public void setId(int id) {
        super.setId(id);
        for(int i = 0; i < this.subEntities.length; ++i) {
            this.subEntities[i].setId(id + i + 1);
        }
    }



    @Override
    public void addSkills() {

    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "Idle", 10, state -> {
            if(this.tickCount % 40 == 0){
                state.resetCurrentAnimation();
            }
            return state.setAndContinue(DefaultAnimations.IDLE);
        }));

    }

    public boolean hurt(HillOfFleshPart part, DamageSource source, float damage) {
//        this.setHealth(this.getHealth() - damage);
//        return true;
        return this.hurt(source, damage * 0.5f);
    }

    @Override
    public boolean addEffect(MobEffectInstance effectInstance, @Nullable Entity entity) {
        if(effectInstance.getEffect() == TEEffects.CRIMSON_STORM){
            return false;
        }
        return super.addEffect(effectInstance, entity);
    }


    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        // 必须进入过肉山空间才能造成伤害
        if(pSource.getEntity() != null && !this.innerEntities.contains(pSource.getEntity())){
            return false;
        }
        return super.hurt(pSource, pAmount * 0.5f);// 用于打到嘴和眼睛增伤
    }

    private void doMagicDamage(){
        if(this.tickCount % 32 == 0) {
            List<LivingEntity> toRemove = new ArrayList<>();
            for (LivingEntity living : this.innerEntities) {
                double distance = living.position().subtract(this.position()).horizontalDistanceSqr();
                if (distance > this.outerRadius * this.outerRadius) {
                    living.hurt(TETags.DamageTypes.of(level(), DamageTypes.MAGIC), 5);
                    this.generateParticles(living, 30);

                }else if(distance < 5 * 5){
                    living.hurt(TETags.DamageTypes.of(level(), DamageTypes.MAGIC), 20);
                    living.getData(TEAttachments.UNSYNC).triggerInvulnerableStorm(living);
                    this.generateParticles(living, 80);

                }else if(distance < this.innerRadius * this.innerRadius){
                    living.hurt(TETags.DamageTypes.of(level(), DamageTypes.MAGIC), 10);
                    living.getData(TEAttachments.UNSYNC).triggerInvulnerableStorm(living);
                    this.generateParticles(living, 50);

                }
                if(!living.isAlive()){
                    toRemove.add(living);
                }
            }
            toRemove.forEach(this.innerEntities::remove);
        }

    }


    private void generateParticles(LivingEntity living, int count) {
        float w = living.getBbWidth();
        float h = living.getBbHeight();
        ((ServerLevel)level()).sendParticles(ParticleTypes.FLAME,
                living.getRandomX(0.5),
                living.getRandomY(),
                living.getRandomZ(0.5), (int)(w * h * w * count),w, h, w,0);
    }

    float circleDx = 0.0f;
    float circleOuterDx = 0.0f;
    private void showCircleParticle(Vec3 center, float radius, float angle, float da) {
        float x = (float) (center.x + radius * Math.cos(angle + da));
        float y = (float) (center.y);
        float z = (float) (center.z + radius * Math.sin(angle + da));
        this.level().addParticle(ParticleTypes.FLAME, x, y, z, 0.0, 0.0, 0.0);
    }

    private void showCircleParticles(Vec3 center, float radius, float angle, float da, int interval) {
        float d = (float) (Math.PI * 2 / interval);
        for(int i = 0; i < interval; ++i) {
            this.showCircleParticle(center, radius, angle, da + i * d);
        }
    }

    private void getNearbyLivings(){
        if(this.tickCount % 64 == 0){
            this.nearbyLivings = new ArrayList<>(level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(outerRadius * 1.2f), e->{
                if(!(e instanceof Player) && !this.hasLineOfSight(e) ){
                    return false;
                }
                float d = outerRadius * outerRadius;
                return e.isAlive() && this.canAttack(e)
                        && e.position().distanceToSqr(position().add(0,10,0)) <  d * 1.2
                        && e.position().subtract(position()).horizontalDistanceSqr() <= d;
            }));
            nearbyLivings.sort(Comparator.comparingDouble(this::distanceToSqr));
        }
    }

    @Override
    public @NotNull AABB getBoundingBoxForCulling() {
        return super.getBoundingBoxForCulling().inflate(30.0F);
    }

    @Override
    public void aiStep() {
        super.aiStep();
//        if(this.onGround()) {
//            this.jumpFromGround();
//        }
        if(level().isClientSide){
            this.circleDx += 0.1f;
            this.showCircleParticles(this.position().add(0,0.2,0), this.innerRadius, this.circleDx, 0, 2);
            this.circleOuterDx += 0.02f;
            if (Minecraft.getInstance().player != null) {
                Vec3 playerPos = Minecraft.getInstance().player.position();
                float height = (float) (playerPos.y - this.position().y);
                for(int i=-8;i<=8;i++) {
                    this.showCircleParticles(this.position().add(0, i + height, 0), this.outerRadius - 0.2F , this.circleOuterDx+ i * 0.03f, (float) Math.PI, 3);
                }
            }
        }else{
            this.doMagicDamage();
            this.getNearbyLivings();
        }

        if (this.isDeadOrDying()) {
            if(this.deathTime / this.subEntities.length % 5 == this.deathTime % this.subEntities.length) {
                int index = this.deathTime % this.subEntities.length;
                float f = (this.random.nextFloat() - 0.5F);
                float f1 = (this.random.nextFloat() - 0.5F);
                float f2 = (this.random.nextFloat() - 0.5F);
                var part = this.subEntities[index];
                this.level().addParticle(ParticleTypes.EXPLOSION_EMITTER, part.getX() + f, part.getY() + 2.0 + f1, part.getZ() + f2, 0.0, 0.0, 0.0);
            }
        } else {

            this.setYRot(Mth.wrapDegrees(this.getYRot()));
            if (this.isNoAi()) {

            } else {

                this.yBodyRot = this.getYRot();
                Vec3[] avec3 = new Vec3[this.subEntities.length];

                for(int j = 0; j < this.subEntities.length; ++j) {
                    avec3[j] = new Vec3(this.subEntities[j].getX(), this.subEntities[j].getY(), this.subEntities[j].getZ());

                }
                if(!this.isSpawning()){
                    this.tickPart(this.subEntities[0], 8,13,5,0);
                    this.tickPart(this.subEntities[1], -8,10,8,1);
                    this.tickPart(this.subEntities[2], 7,8,-7,2);
                    this.tickPart(this.subEntities[3], 0.5,6.5,8,3);
                    this.tickPart(this.subEntities[4], -3,5.5,-6,4);

                    this.tickPart(this.subEntities[5], 0,11,0,0);
                    this.tickPart(this.subEntities[6], -6,9,-3,2);
                    this.tickPart(this.subEntities[7], 8,8,5,2);
                    this.tickPart(this.subEntities[8], -6.5,4,8.5,2);
                    this.tickPart(this.subEntities[9], 7,3,-7,4);
                }

                for(int k = 0; k < this.subEntities.length; ++k) {
                    this.subEntities[k].tick();
                    this.subEntities[k].xo = avec3[k].x;
                    this.subEntities[k].yo = avec3[k].y;
                    this.subEntities[k].zo = avec3[k].z;
                    this.subEntities[k].xOld = avec3[k].x;
                    this.subEntities[k].yOld = avec3[k].y;
                    this.subEntities[k].zOld = avec3[k].z;
                }
            }
        }

    }

    private void tickPart(HillOfFleshPart part, double offsetX, double offsetY, double offsetZ, int index) {
        part.setPos(this.getX() + offsetX, this.getY() + offsetY, this.getZ() + offsetZ);
        part.setModelOffset(new Vec3(offsetX, offsetY, offsetZ));
        part.tickPart(offsetX, offsetY, offsetZ, index);

        if(table != null){
            Vec3KeyframeAnimation animation = table.getPositions(part.name);
            if(animation != null){
                part.setPos(position().add(0,-1.5,0).add(animation.cal((this.tickCount + 10 ) % 40)));
            }
        }

    }


    @Override
    public boolean isMultipartEntity() {
        return true;
    }

    @Override
    public HillOfFleshPart @NotNull [] getParts() {
        return this.subEntities;
    }


    @Override
    protected void tickDeath() {
        ++this.deathTime;
        if (this.deathTime >= 180 && this.deathTime <= 200) {
            float f = (this.random.nextFloat() - 0.5F);
            float f1 = (this.random.nextFloat() - 0.5F);
            float f2 = (this.random.nextFloat() - 0.5F);
            Arrays.stream(this.subEntities).forEach(part->{
                this.level().addParticle(ParticleTypes.EXPLOSION_EMITTER, part.getX() + f, part.getY() + 2.0 + f1, part.getZ() + f2, 0.0, 0.0, 0.0);
            });
        }
        if (this.level() instanceof ServerLevel) {
            if (this.deathTime == 1 && !this.isSilent()) {
                this.level().globalLevelEvent(1028, this.blockPosition(), 0);
            }
        }
        this.move(MoverType.SELF, new Vec3(0.0, 0.10000000149011612, 0.0));
        if (this.deathTime == this.getMaxDeathTime() && this.level() instanceof ServerLevel) {
            this.remove(RemovalReason.KILLED);
            this.gameEvent(GameEvent.ENTITY_DIE);
        }
    }

    public int getMaxDeathTime(){
        return 200;
    }
}
