package org.confluence.terraentity.entity.summon;

import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.confluence.terraentity.attachment.SummonerAttachment;
import org.confluence.terraentity.entity.util.trail.SummonSwordTrail;
import org.confluence.terraentity.init.TEAttachments;
import org.confluence.terraentity.registries.hit_effect.IEffectStrategy;
import org.confluence.terraentity.utils.IOriented;
import org.confluence.terraentity.utils.OBB;
import org.confluence.terraentity.utils.TEUtils;
import software.bernie.geckolib.animation.AnimatableManager;

import java.util.*;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class SummonSword extends AbstractSummonMob<SummonSword> implements IOriented, FlyingAnimal {
    public SummonSwordTrail trail;
    public Queue<SummonSwordTrail.PositionProperties> trailQueue;

    public boolean timeToSkillAttack = false;
    int skillCooldown = 0;
    float rotateZTimer;
    int rotateZTick;
    public int sequence;
    public Item modelItem;
    public int backTicks;
    public int backTicksMax = 20;
    IEffectStrategy effectStrategy;

    protected static final EntityDataAccessor<Float> DATA_ROTATE_Z_ID = SynchedEntityData.defineId(SummonSword.class, EntityDataSerializers.FLOAT);
    protected static final EntityDataAccessor<Boolean> DATA_BACK = SynchedEntityData.defineId(SummonSword.class, EntityDataSerializers.BOOLEAN);
    protected static final EntityDataAccessor<Integer> DATA_SEQUENCE = SynchedEntityData.defineId(SummonSword.class, EntityDataSerializers.INT);

    public SummonSword(EntityType<? extends TamableAnimal> entityType,  Level level,Supplier<Item> modelItem,  int rgb) {
        this(entityType, level, modelItem, rgb, null, 0.15f);
    }
    public SummonSword(EntityType<? extends TamableAnimal> entityType, Level level, Supplier<Item> modelItem,  int rgb, IEffectStrategy effectStrategy, float width) {
        super(entityType, level);
        this.noPhysics = true;
//        this.setNoGravity(true);
        this.setDiscardFriction(true);
        this.getAttribute(Attributes.FOLLOW_RANGE).setBaseValue(16);
        this.getAttribute(Attributes.GRAVITY).setBaseValue(0.0);
        this.modelItem = modelItem.get();

//        Color c = new Color(0x8136D2);
//        this.trail = new SummonSwordTrail(1, 0.15f, c.getRGB());

        this.trail = new SummonSwordTrail(1, width, rgb);
        this.trailQueue = new LinkedList<>();
        this.effectStrategy = effectStrategy;

    }

    public float getRotateZTimer(float partialTicks) {
        float ticks = tickCount + partialTicks;
        if(ticks > rotateZTimer) {
            return 0;
        }
//        System.out.println(ticks - rotateZTick);
        return ticks - rotateZTick;
    }

    private void addZRot(float duration){
        this.entityData.set(DATA_ROTATE_Z_ID, duration, true);
    }


    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_ROTATE_Z_ID, 0.0f);
        builder.define(DATA_BACK, false);
        builder.define(DATA_SEQUENCE, 0);

    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        super.onSyncedDataUpdated(key);
        if (key == DATA_ROTATE_Z_ID) {
            this.rotateZTimer = this.entityData.get(DATA_ROTATE_Z_ID) + tickCount;
            this.rotateZTick = tickCount;
        }else if(key == DATA_BACK){
            boolean back = this.entityData.get(DATA_BACK);
            if(!back) {
                this.backTicks = 20;
            }else{
                this.backTicks = 0;
            }

        }else if(key == DATA_SEQUENCE){
            this.sequence = this.entityData.get(DATA_SEQUENCE);
        }

    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new SwordSkillAttackGoal(this));
        this.goalSelector.addGoal(1, new SwordAttackGoal(this));
        this.goalSelector.addGoal(2, new SwordFollowOwnerGoal(this));
        this.summon_registerTargetGoals();
    }

    @Override
    public int getMaxHeadXRot() {
        return 85;
    }

    @Override
    public void tick() {
        super.tick();
        if(level().isClientSide){
            if(this.entityData.get(DATA_BACK)){
                this.backTicks++;
                this.trailQueue.poll();
            }else{
                this.backTicks--;
                this.trail.generateTrail(this, tickCount);
            }
        }

    }

    @Override
    public float summon_getDistanceToTeleportToOwner(){
        return 16 * 16;
    }


    static class SwordAttackGoal extends Goal{

        SummonSword sword;
        protected SwordAttackGoal(SummonSword sword) {
            this.sword = sword;
            this.setFlags(EnumSet.of(Flag.MOVE));

        }
        @Override
        public boolean canUse() {
            return sword.getTarget() != null && !sword.summon_shouldTryTeleportToOwner() && !sword.timeToSkillAttack;
        }

        @Override
        public void start() {
//            sword.setSharedFlag(6, true);
        }

        @Override
        public void tick(){
            LivingEntity target = sword.getTarget();
            if(target == null){
                return;
            }


//            System.out.println("state: attacking: "  + sword.tickCount);
            sword.lookAt(target, 30, 85);
            sword.lookControl.setLookAt(target);
            Vec3 targetPos = target.getEyePosition();
            double dist = targetPos.distanceTo(sword.position());
            if(dist < 2f ){
                if(sword.skillCooldown <= 0) {
                    sword.timeToSkillAttack = true;
//                    System.out.println("trigger skill attack");

                    return;
                }
            }

            Vec3 dir = targetPos.subtract(sword.getEyePosition()).normalize();
            double angle = TEUtils.angleBetween(sword.getLookAngle(), dir);
            if(angle < 0.05f){
                sword.addDeltaMovement(dir);
                sword.setDeltaMovement(sword.getDeltaMovement().normalize().scale(0.6f));

            }else{
                sword.setDeltaMovement(sword.getDeltaMovement().scale(0.9f));
            }

        }
    }

    static class SwordSkillAttackGoal extends Goal{

        SummonSword sword;
        int ticks = 0;


        boolean triggered = false;
        protected SwordSkillAttackGoal(SummonSword sword) {
            this.sword = sword;
            this.setFlags(EnumSet.of(Flag.MOVE));

        }
        @Override
        public boolean canUse() {
            return --sword.skillCooldown <= 0 && sword.getTarget() != null && sword.timeToSkillAttack;
        }


        @Override
        public boolean canContinueToUse() {
            return this.canUse() && ticks < 20;
        }


        @Override
        public void start() {
//            sword.setSharedFlag(6, true);

        }

        @Override
        public void tick(){

            LivingEntity target = sword.getTarget();
            if(target == null){
                return;
            }
//            System.out.println("state: skill attacking" + (10 - ticks));
            Vec3 dist = target.getEyePosition().subtract(sword.position());

            Vec3 skill = new Vec3(0, 10 - ticks, 0);

            Vec3 targetPos = target.getEyePosition().add(skill);
            Vec3 lookDir = targetPos.subtract(sword.getEyePosition());
            sword.lookControl.setLookAt(targetPos);
            sword.lookAt(EntityAnchorArgument.Anchor.EYES, targetPos);

//            sword.setXRot((float) ((10 - ticks) * 4 * 0.0174533));

            if(dist.length() > 0.6 && !triggered){
                sword.setDeltaMovement(dist.normalize().scale(0.5f));
                return;
            }
            triggered = true;
            ticks++;
            sword.setDeltaMovement(sword.getDeltaMovement().scale(0.7f));
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public void stop(){
            sword.timeToSkillAttack = false;
            ticks = 0;
            sword.skillCooldown = 80;
            triggered = false;
            if(sword.random.nextFloat() < 0.5){
                sword.addZRot(40);
            }
        }
    }

    static class SwordFollowOwnerGoal extends Goal{
        SummonSword sword;
        protected SwordFollowOwnerGoal(SummonSword sword) {
            this.sword = sword;
            this.setFlags(EnumSet.of(Flag.MOVE));
        }
        @Override
        public boolean canUse() {
            return sword.getOwner() != null;
        }

        @Override
        public void start() {
//            sword.setSharedFlag(6, false);
            sword.entityData.set(DATA_BACK, true, true);
        }
        @Override
        public void stop() {
//            sword.setSharedFlag(6, false);
            sword.entityData.set(DATA_BACK, false, true);
        }

        @Override
        public void tick(){
            LivingEntity owner = sword.getOwner();
            if(owner == null){
                return;
            }

            Vec3 d = Vec3.directionFromRotation(new Vec2(owner.getXRot(), owner.yBodyRot));
            Vec3 forward = d.multiply(1,0,1).normalize();
            Vec3 right = d.cross(new Vec3(0,1,0)).normalize();
//            Vec3 ownerPos = owner.position().subtract(forward.scale(0.5 + 0.2f * (sword.sequence - 1))).add(0,1 - (sword.sequence - 1) * 0.08f,0);
            Vec3 ownerPos = owner.position().subtract(forward.scale(0.6 - 0.05f * (sword.sequence - 1))).add(0,1,0)
                    .add(right.scale(0.2f * (sword.sequence / 2) * ((sword.sequence & 1) == 0 ? 1 : -1)));

            Vec3 swordPos = sword.position();
            Vec3 dir = ownerPos.subtract(swordPos).normalize();

//            Vec3 lookPos = swordPos.subtract(forward.scale(5)).add(0,-15 + (sword.sequence - 1) * 2,0);
            Vec3 lookPos = swordPos.subtract(forward.scale(5)).add(0,-8 - (sword.sequence - 1)/2 ,0);

            sword.lookControl.setLookAt(lookPos);
            sword.lookAt(EntityAnchorArgument.Anchor.FEET, lookPos);
            double dist = ownerPos.distanceTo(swordPos) * 0.5;

            dist = Math.min(dist, 1);
            if(dist == 0){
                return;
            }
            dist = dist * dist;

            sword.addDeltaMovement(dir);
            sword.setDeltaMovement(sword.getDeltaMovement().normalize().scale(dist));

            Vec3 wiggle = new Vec3(sword.random.nextGaussian() * 0.01, sword.random.nextGaussian() * 0.01, sword.random.nextGaussian() * 0.01);
            sword.addDeltaMovement(wiggle.scale(1));



        }
    }


    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

    }


    @Override
    public OBB getOrientedBoundingBox() {
        Vec3 pos = position();
        return new OBB(pos, 0.75, 0.75, 1.5 * lengthScale(), getXRot(), getYRot()).offsetAlongAxisZ(0.75 * (lengthScale() - 1)).updateVertex();

    }

    @Override
    public void onAddedToLevel() {
        super.onAddedToLevel();
        LivingEntity owner = this.getOwner();
        if(!level().isClientSide && owner instanceof Player player && this.sequence == 0){
            SummonerAttachment data = player.getData(TEAttachments.SUMMONER_STORAGE.get());
            Iterator<Integer> it = data.getIds().iterator();
            List<Integer> ids = new ArrayList<>();
            while(it.hasNext()){
                int id = it.next();
                if(this.getId() != id && level().getEntity(id) instanceof SummonSword sword){
                    ids.add(sword.sequence);
                }
            }
            if(ids.isEmpty()){
                this.sequence = 1;
                data.prismaIDs.add(this.sequence);
//                System.out.println("no sequence found, setting to 1");
                this.entityData.set(DATA_SEQUENCE, this.sequence, true);
                return;
            }
            ids.sort(Comparator.naturalOrder());
            int last = ids.get(0);
            for(int i = 1; i < ids.size(); i++){
                if(ids.get(i) - last > 1){
                    this.sequence = last + 1;
                    data.prismaIDs.add(this.sequence);
//                    System.out.println("sequence found, setting to " + this.sequence);
                    this.entityData.set(DATA_SEQUENCE, this.sequence, true);
                    return;
                }
                last = ids.get(i);
            }
            this.sequence = last + 1;
            data.prismaIDs.add(this.sequence);
//            System.out.println("sequence found, setting to " + this.sequence);
            this.entityData.set(DATA_SEQUENCE, this.sequence, true);
        }
    }

    private double lengthScale() {
        return 1.5;
    }

    @Override
    public boolean isFlying() {
        return true;
    }

    @Override
    public void doCollisionAttack(Predicate<Entity> filter, Consumer<Entity> attackCallback){
        if(!shouldDoCollision() || this.level().isClientSide) return;
        getCollisionProperties().reduceAttackInterval();
        if (canCollisionHurt() && !this.level().isClientSide && getCollisionProperties().canAttack()) {
            OBB obb = getOrientedBoundingBox();
            AABB border = this.getBoundingBox().inflate(lengthScale() * 2);
            var entities = this.level().getEntitiesOfClass(LivingEntity.class, border, EntitySelector.NO_SPECTATORS.and(e -> {
                if (e instanceof Player) return false;
                return filter.test(e) && obb.inflate(10).collide(e.getBoundingBox(), this.getDeltaMovement(), e.getDeltaMovement());
            }));
            for (LivingEntity living : entities) {
                attackCallback.accept(living);
                getCollisionProperties().rewind();
            }
        }
    }

    @Override
    public boolean doHurtTarget(Entity entity) {
        if(effectStrategy != null && entity instanceof LivingEntity living){
            effectStrategy.getEffect().accept(this, living);
        }
        return super.doHurtTarget(entity);
    }
}
