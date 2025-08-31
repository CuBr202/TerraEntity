package org.confluence.terraentity.entity.boss.wallofflesh;

import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.util.Tuple;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.confluence.terraentity.api.entity.Boss;
import org.confluence.terraentity.effect.harmful.HorrifiedEffect;
import org.confluence.terraentity.entity.boss.AbstractTerraBossBase;
import org.confluence.terraentity.entity.monster.TheHungry;
import org.confluence.terraentity.entity.monster.prefab.AbstractPrefab;
import org.confluence.terraentity.init.TEEffects;
import org.confluence.terraentity.init.TESounds;
import org.confluence.terraentity.init.entity.TEMonsterEntities;
import org.confluence.terraentity.utils.CameraShakeData;
import org.confluence.terraentity.utils.CameraShakeManager;
import org.confluence.terraentity.utils.TEUtils;
import org.jetbrains.annotations.NotNull;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;

import javax.annotation.Nullable;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.confluence.terraentity.init.TEEntityDataSerializers.TUPLET_VEC3_INT_LIST_SERIALIZER;
import static org.confluence.terraentity.init.TEEntityDataSerializers.TUPLE_INT_VEC3_LIST_SERIALIZER;

public class WallOfFlesh extends AbstractTerraBossBase implements Boss {
    private static final float DAMAGE = 39f;
    static float moveSpeedBase = 0.15f;

    boolean genSegments = true;
    int genTick = 20;
    boolean shouldMove = true;

    float inverseFactor = Mth.clamp(0.5F - getHealthPercentage(),0.0F,1.0F);
    float moveSpeed = getHealthPercentage()<=0.5f ? moveSpeedBase * 1.5f + moveSpeedBase * inverseFactor : moveSpeedBase;
    Vec3 InitPos = Vec3.ZERO;

    public AABB insideCollisionBox;
    public AABB outsideCollisionBox;

    private final int gridSizeX = 50;
    private final int gridSizeY = 30;
    public float gridSpacing = 15.0f;

    private static final int summonCDAll = 1200; //饿鬼召唤cd
    private int summonCD = summonCDAll;

    List<LivingEntity> nearbyLivings;

    private static final double FINISH_LINE_DISTANCE = 2000;

    public List<WallOfFleshPart> subEntities = new CopyOnWriteArrayList<>();

    public final List<Tuple<Integer, Vec3>> localOffsets = new CopyOnWriteArrayList<>(); // 存储子实体相对坐标
    public final List<Tuple<Vec3, Integer>> theHungryList = new CopyOnWriteArrayList<>();

    private static final EntityDataAccessor<List<Tuple<Integer, Vec3>>> DATA_LOCAL_OFFSETS =
            SynchedEntityData.defineId(WallOfFlesh.class, TUPLE_INT_VEC3_LIST_SERIALIZER.get());
    private static final EntityDataAccessor<List<Tuple<Vec3, Integer>>> DATA_HUNGRY_OFFSETS =
            SynchedEntityData.defineId(WallOfFlesh.class, TUPLET_VEC3_INT_LIST_SERIALIZER.get());

    public WallOfFlesh(EntityType<? extends Monster> type, Level level) {
        super(type, level);
        this.noPhysics = true;
        this.noCulling = true;
        this.nearbyLivings = new ArrayList<>();
        this.setNoGravity(true);
        genGridWall();
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_LOCAL_OFFSETS, new CopyOnWriteArrayList<>());
        builder.define(DATA_HUNGRY_OFFSETS, new CopyOnWriteArrayList<>());
    }

    public List<Tuple<Integer, Vec3>> getLocalOffsets() {
        return this.entityData.get(DATA_LOCAL_OFFSETS);
    }

    public void setLocalOffsets(int entityId, Vec3 offset) {
        if (!this.level().isClientSide) {
            this.localOffsets.add(new Tuple<>(entityId, offset));
            this.entityData.set(DATA_LOCAL_OFFSETS, new CopyOnWriteArrayList<>(this.localOffsets));
        }
    }

    public List<Tuple<Vec3, Integer>> getHungryOffsets() {
        return this.entityData.get(DATA_HUNGRY_OFFSETS);
    }

    public void setHungryOffsets(Vec3 offset,int entityId) {
        if (!this.level().isClientSide) {
            this.theHungryList.add(new Tuple<>(offset, entityId));
            this.entityData.set(DATA_HUNGRY_OFFSETS, new CopyOnWriteArrayList<>(this.theHungryList));
        }
    }

    @Override
    public boolean isNoGravity(){
        return true;
    }

    @Override
    protected void registerGoals() {}

    public void addChild(Entity child, Vec3 localOffset) {
        if (child instanceof WallOfFleshPart part) {
            subEntities.add(part);
            if(!this.level().isClientSide) {
                int index = subEntities.size() - 1;
                this.setLocalOffsets(index, localOffset);
            }
        }else if(child instanceof TheHungry hungry && !this.level().isClientSide){
            this.setHungryOffsets(localOffset, hungry.getId());
        }

        child.setPos(this.position().add(localOffset));
    }

    private void genGridWall() {
        Vec3 baseOffset = this.getForward().scale(7.0F);

        List<Vec3> eyePositions = new ArrayList<>();
        List<Vec3> mouthPositions = new ArrayList<>();
        List<Vec3> hungryPositions = new ArrayList<>();

        generateSimpleGrid(eyePositions, mouthPositions, hungryPositions, baseOffset);

        // 生成眼睛
        for (int i = 0; i < eyePositions.size(); i++) {
            Vec3 pos = eyePositions.get(i);
            WallOfFleshEye eye = new WallOfFleshEye(this, "WallOfFleshEye" + (i + 1), 4.0f, 4.0f);
            addChild(eye, pos);
            eye.setYRot(this.getYRot());
        }

        // 生成嘴巴
        for (int i = 0; i < mouthPositions.size(); i++) {
            Vec3 pos = mouthPositions.get(i);
            WallOfFleshMouse mouth = new WallOfFleshMouse(this, "WallOfFleshMouse" + i, 3.0f, 4.0f);
            addChild(mouth, pos);
            mouth.setYRot(this.getYRot());
        }
        if (!this.level().isClientSide && this.level() instanceof ServerLevel serverLevel) {
            for (Vec3 pos : hungryPositions) {
                TheHungry hungry = TEUtils.spawnEntity(() -> new TheHungry(TEMonsterEntities.THE_HUNGRY.get(), level(),
                    new AbstractPrefab().getPrefab()) {
                            @Override
                            protected boolean shouldDropLoot() {
                                return false;
                            }
                }, serverLevel, pos);

                if (hungry != null) {
                    addChild(hungry, pos);
                    this.theHungryList.add(new Tuple<>(pos, hungry.getId()));
                    hungry.minion_setOwner(this);
                    hungry.setYRot(this.getYRot());
                    hungry.setInitPos(this.position().add(pos).toVector3f());
                }
            }
        }

        this.setId(ENTITY_COUNTER.getAndAdd(this.subEntities.size() + 1) + 1);
    }

    private void generateSimpleGrid(List<Vec3> eyePositions, List<Vec3> mouthPositions, List<Vec3> hungryPositions, Vec3 baseOffset) {
        final int EYE_CHANCE = 40;
        final int MOUTH_CHANCE = 25;
        final int HUNGRY_CHANCE = 15;

        final double halfGridX = gridSizeX / 2.0;
        final double halfGridY = gridSizeY / 2.0;
        final double spacing = gridSpacing;
        final double conflictDistanceSqr = spacing * spacing * 0.6;

        final long seed = random.nextLong();

        for (int x = 0; x < gridSizeX; x++) {
            for (int y = 0; y < gridSizeY; y++) {
                long hash = x * 73856093L + y * 19349663L + seed;
                hash = hash ^ (hash >>> 16);
                hash = hash * 0x85ebca6bL;
                hash = hash ^ (hash >>> 13);
                hash = hash * 0xc2b2ae35L;
                hash = hash ^ (hash >>> 16);
                double noise = (hash & 0xFFFFFFFFL) / 4294967296.0;

                if (noise > 0.8) continue;

                int rand = random.nextInt(100);
                if (rand >= 80) continue;

                double offsetX = (random.nextDouble() - 0.5) * spacing * 0.4;
                double offsetY = (random.nextDouble() - 0.5) * spacing * 0.4;

                Vec3 worldPos;
                if (isMovingAlongX()) {
                    worldPos = new Vec3(0,
                                       (y - halfGridY) * spacing + offsetY,
                                       (x - halfGridX) * spacing + offsetX)
                                       .add(baseOffset);
                } else {
                    worldPos = new Vec3((x - halfGridX) * spacing + offsetX,
                                       (y - halfGridY) * spacing + offsetY,
                                       0).add(baseOffset);
                }

                boolean hasConflict = false;
                int maxCheck = 4;

                for (int i = Math.max(0, eyePositions.size() - maxCheck); i < eyePositions.size(); i++) {
                    if (eyePositions.get(i).distanceToSqr(worldPos) < conflictDistanceSqr) {
                        hasConflict = true;
                        break;
                    }
                }

                if (!hasConflict) {
                    for (int i = Math.max(0, mouthPositions.size() - maxCheck); i < mouthPositions.size(); i++) {
                        if (mouthPositions.get(i).distanceToSqr(worldPos) < conflictDistanceSqr) {
                            hasConflict = true;
                            break;
                        }
                    }
                }

                if (!hasConflict) {
                    for (int i = Math.max(0, hungryPositions.size() - maxCheck); i < hungryPositions.size(); i++) {
                        if (hungryPositions.get(i).distanceToSqr(worldPos) < conflictDistanceSqr) {
                            hasConflict = true;
                            break;
                        }
                    }
                }

                if (!hasConflict) {
                    if (rand < EYE_CHANCE) {
                        eyePositions.add(worldPos);
                    } else if (rand < EYE_CHANCE + MOUTH_CHANCE) {
                        mouthPositions.add(worldPos);
                    } else {
                        hungryPositions.add(worldPos);
                    }
                }
            }
        }
    }

    protected void dropAllDeathLoot(ServerLevel level, DamageSource damageSource) {
        super.dropAllDeathLoot(level, damageSource);

        BlockPos centerPos = this.blockPosition().below(1);

        for (int x = -4; x <= 4; x++) {
            for (int y = -4; y <= 4; y++) {
                for (int z = -4; z <= 4; z++) {
                    BlockPos framePos = centerPos.offset(x, y, z);
                    if ((Math.abs(x) == 4 || Math.abs(y) == 4 || Math.abs(z) == 4) && level.getBlockState(framePos).isAir()) {
                        level.setBlockAndUpdate(framePos, Blocks.OBSIDIAN.defaultBlockState());
                    }
                }
            }
        }
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public boolean isPickable() {
        return false;
    }

    public boolean isMovingAlongX() {
        Direction dir = this.getDirection();
        return dir == Direction.EAST || dir == Direction.WEST;
    }

    @Override
    public Vec3 getForward() {
        float yawRad = (float) Math.toRadians(this.getYRot());
        return new Vec3(-Math.sin(yawRad), 0, Math.cos(yawRad));
    }

    public void setForward(Direction direction) {
        switch (direction) {
            case NORTH -> this.setYRot(0.0F);   // 北: 0°
            case EAST -> this.setYRot(90.0F);   // 东: 90°
            case SOUTH -> this.setYRot(180.0F); // 南: 180°
            case WEST -> this.setYRot(270.0F);  // 西: 270°
            default -> throw new IllegalArgumentException("Invalid direction: " + direction);
        }
    }

    private void updateChildPosition(Entity child) {
        List<Tuple<Integer, Vec3>> syncedOffsets = getLocalOffsets();
        List<Tuple<Vec3, Integer>> hungrySyncedOffsets = getHungryOffsets();

        if (child instanceof TheHungry hungry) {
            Vec3 localOffset = Vec3.ZERO;
            for (Tuple<Vec3, Integer> tuple : hungrySyncedOffsets) {
                if (tuple.getB().equals(hungry.getId())) {
                    localOffset = tuple.getA();
                    break;
                }
            }
            
            Vec3 childPos = this.position().add(localOffset);
            Vec3 vec3 = hungry.position().subtract(hungry.getInitPos());
            Vec3 summonPos = childPos.add(vec3);
            hungry.setPos(summonPos);
            hungry.setInitPos(childPos.toVector3f());
        } else if (child instanceof WallOfFleshPart part) {
            int childIndex = subEntities.indexOf(child);
            Vec3 localOffset = (childIndex >= 0 && childIndex < syncedOffsets.size()) ? 
                    syncedOffsets.get(childIndex).getB() : Vec3.ZERO;
            Vec3 childPos = this.position().add(localOffset);
            part.moveTo(childPos);
        }
    }

    @Override
    public void onAddedToLevel(){
        super.onAddedToLevel();
        this.noPhysics = true;
        this.noCulling = true;
        this.setNoGravity(true);
        double summonDir = 50;

        Vec3 summonPos = new Vec3(this.position().x, this.level().getMinBuildHeight() + (gridSizeY * gridSpacing)/2, this.position().z).add(getForward().scale(-summonDir));
        this.moveTo(summonPos);
        this.InitPos = summonPos;
        this.setAttactDamage(DAMAGE);
    }

    @Override
    public boolean shouldBeSaved(){
        return false;
    }

    @Override
    public void aiStep() {
        super.aiStep();

        for (Entity child : subEntities) {
            updateChildPosition(child);
        }
        for (Tuple<Vec3, Integer> tuple : theHungryList) {
            Entity child = level().getEntity(tuple.getB());
            if (child != null) {
                updateChildPosition(child);
            }
        }

    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide) {
            if (--summonCD <= 0) {
                 summonCD = summonCDAll;
                for (int i = 0; i < theHungryList.size(); i++) {
                    Tuple<Vec3, Integer> tuple = theHungryList.get(i);
                    Vec3 theHungryPos = tuple.getA();
                    Integer hungryId = tuple.getB();
                    TheHungry hungry = (TheHungry) level().getEntity(hungryId);
                    if (hungry != null && hungry.isAlive()) continue;
                    TheHungry newHungry = TEUtils.spawnEntity(()->new TheHungry(TEMonsterEntities.THE_HUNGRY.get(), level(),new AbstractPrefab().getPrefab()) {
                        @Override
                        protected boolean shouldDropLoot() {
                            return false;
                        }
                    }, (ServerLevel)level(), theHungryPos);

                    if (newHungry != null) {
                        this.theHungryList.set(i, new Tuple<>(theHungryPos, newHungry.getId()));
                        newHungry.minion_setOwner(this);
                        newHungry.setInitPos(this.position().add(theHungryPos).toVector3f());
                        addChild(newHungry, theHungryPos);
                        level().playSound(null, newHungry.blockPosition(), TESounds.WALL_OF_FLESH_SUMMON.get(), SoundSource.HOSTILE, 1, 1);
                    }
                }
            }
        }

        if (this.tickCount > genTick && genSegments && this.dirty) {
            genSegments = false;

            if (!level().isClientSide) {
                CameraShakeManager.addCameraShake(new CameraShakeData(300, this.position(), 180));
            }
        }

        if (shouldMove && this.isAlive()) {
            Vec3 forward = this.getForward();
            float yaw = (float) Math.toDegrees(
                    Math.atan2(-forward.x, forward.z)
            );

            float alignedYaw = Math.round(yaw / 90.0f) * 90.0f;

            this.setYRot(Mth.wrapDegrees(alignedYaw));
            Vec3 moveDirection = this.getForward();
            Vec3 currentOffset = this.position().subtract(InitPos);
            double progress = currentOffset.dot(moveDirection);

            // 四方向终点判断
            if ((moveDirection.x > 0 && progress >= FINISH_LINE_DISTANCE) || // 东方向
                    (moveDirection.x < 0 && progress <= -FINISH_LINE_DISTANCE) || // 西方向
                    (moveDirection.z > 0 && progress >= FINISH_LINE_DISTANCE) || // 南方向
                    (moveDirection.z < 0 && progress <= -FINISH_LINE_DISTANCE) || // 北方向
                    !this.level().getWorldBorder().isWithinBounds(this.position())) {
                this.discard();
            }
            this.addDeltaMovement(getForward().scale(moveSpeed).scale(0.15F));
        }

        if (this.getInsideBox()!= null&&this.getOutsideCollisionBox()!= null) {
            List<Player> nearbyPlayers = level().getEntitiesOfClass(Player.class,
                    this.getOutsideCollisionBox());

            List<Player> nearbyTargets= level().getEntitiesOfClass(Player.class,
                    this.getInsideBox());

            this.nearbyLivings.clear();
            this.nearbyLivings.addAll(nearbyTargets);
            nearbyPlayers.stream().filter(LivingEntity::canBeSeenByAnyone).forEach(player -> {
                DeferredHolder<MobEffect, HorrifiedEffect> horrifiedHolder = TEEffects.HORRIFIED;
                horrifiedHolder.get().setWallOfFlesh(this);
                player.addEffect(new MobEffectInstance(horrifiedHolder, 200, 3, false, true));
            });
        }

        if (true) {
            for (LivingEntity nearbyLiving : this.nearbyLivings) {
                for (WallOfFleshPart part : this.subEntities) {
                    double distanceSqr = part.position().distanceToSqr(nearbyLiving.position());
                    if (distanceSqr <= 60 * 60) {
                        Vec3 localOffset = part.position();
                        part.tickPart(localOffset.x, localOffset.y, localOffset.z);
                    }
                }
            }
        }
    }

    public AABB getInsideBox() {
        Direction dir = this.getDirection();
        boolean isReverse = dir == Direction.WEST || dir == Direction.SOUTH;
        int completion = isReverse?0:5;
        if(isMovingAlongX()) {
            double x = completion;
            double y = gridSizeY * gridSpacing / 2;
            double z = gridSizeX * gridSpacing / 2;
            insideCollisionBox = new AABB(
                    this.position().subtract(x, y, z + gridSpacing / 2),
                    this.position().add(x + 150 * this.getForward().x, y, z + 150 * this.getForward().z - gridSpacing / 2));
        }else {
            double x = gridSizeX * gridSpacing / 2;
            double y = gridSizeY * gridSpacing / 2;
            double z = completion;
            insideCollisionBox = new AABB(
                    this.position().subtract(x + gridSpacing / 2, y, z),
                    this.position().add(x, y, z + 150 * this.getForward().z - gridSpacing / 2));
        }
        return this.insideCollisionBox;
    }

    public AABB getOutsideCollisionBox() {
        Direction dir = this.getDirection();
        boolean isReverse = dir == Direction.WEST || dir == Direction.SOUTH;
        int completion1 = isReverse?-200:200;
        if(isMovingAlongX()) {
            double x = completion1;
            double y = gridSizeY * gridSpacing / 2 + 150;
            double z = gridSizeX * gridSpacing / 2 + 150;
            outsideCollisionBox = new AABB(
                    this.position().subtract(x, y, z + gridSpacing / 2),
                    this.position().add(x, y, z - gridSpacing / 2));
        }else {
            double x = gridSizeX * gridSpacing / 2 + 150;
            double y = gridSizeY * gridSpacing/2 + 150;
            double z = completion1;
            outsideCollisionBox = new AABB(
                    this.position().subtract(x + gridSpacing / 2, y, z),
                    this.position().add(x - gridSpacing / 2, y, z));
        }
        return this.outsideCollisionBox;
    }

    @Override
    public AABB getBoundingBoxForCulling() {
        return this.getOutsideCollisionBox();
    }

    @Override
    public void onRemovedFromLevel() {
        subEntities.forEach(Entity::onRemovedFromLevel);
        if(!this.isAlive()){
            subEntities.forEach(Entity::discard);
            localOffsets.clear();
        }
        this.bossEvent.removeAllPlayers();
        super.onRemovedFromLevel();
    }

    @Override
    public boolean requiresCustomPersistence() {
        return true;
    }

    @Override
    public boolean canAttack(LivingEntity entity) {
        return super.canAttack(entity);
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        if(source.is(DamageTypeTags.IS_FIRE)||source.is(DamageTypeTags.IS_DROWNING)){
            return true;
        }
        return super.isInvulnerableTo(source);
    }

    public boolean hurt(WallOfFleshPart wallOfFleshPart, @NotNull DamageSource source, float damage) {
        return this.hurt(source, damage);
    }

    public boolean hurt(DamageSource source, float amount) {
        if (source.is(DamageTypes.EXPLOSION)) {
            float resistance = switch (this.level().getDifficulty()) {
                case EASY -> 0.75f;   // 简单75%
                case NORMAL -> 0.85f; // 普通85%
                case HARD -> 0.95f;   // 困难95%
                default -> 0.85f;
            };
            amount *= (1.0f - resistance);
        }
        return super.hurt(source, amount);
    }

    @Override
    public void die(DamageSource damageSource) {
        subEntities.forEach(Entity::discard);
        localOffsets.clear();
        for (Tuple<Vec3, Integer> tuple : theHungryList) {
            Entity hungry = level().getEntity(tuple.getB());
            if (hungry != null) {
                hungry.discard();
            }
        }
        super.die(damageSource);
    }

    @Override
    public boolean hasLineOfSight(Entity entity) {
        double size = this.getOutsideCollisionBox().getSize()*1.5F;
        return distanceToSqr(entity) < size * size;
    }

    @Override
    public boolean isMultipartEntity() {
        return true;
    }

    @Override
    public WallOfFleshPart @NotNull [] getParts() {
        return this.subEntities.toArray(new WallOfFleshPart[0]);
    }

    @Override
    public void addSkills() {
    }

    @Override
    protected BossEvent.BossBarColor getBossBarColor(){
        return BossEvent.BossBarColor.RED;
    };

    @Override
    public boolean shouldEscape() {
        return false;
    }

    public boolean shouldRender(double x, double y, double z) {
        return true;
    }

    public boolean shouldRenderAtSqrDistance(double distance) {
        return true;
    }

    public boolean addEffect(MobEffectInstance effectInstance, @Nullable Entity entity) {
        return false;
    }

    protected boolean canRide(Entity entity) {
        return false;
    }

    public boolean canUsePortal(boolean allowPassengers) {
        return false;
    }

    public float getMoveSpeed(){
        return this.moveSpeed;
    }

    public int getGridSizeX() {
        return gridSizeX;
    }

    public int getGridSizeY() {
        return gridSizeY;
    }

    @Override
    public void setId(int id) {
        super.setId(id);
        for (int i = 0; i < this.subEntities.size(); ++i) {
            this.subEntities.get(i).setId(id + i + 1);
        }
    }

    public static boolean isWallOfFlesh(Entity entity) {
        return entity instanceof WallOfFlesh || entity instanceof WallOfFleshEye || entity instanceof WallOfFleshMouse;
    }

    public static boolean isWallOfFleshMob(Entity entity) {
        if (entity == null) {
            return false;
        }
        return entity instanceof WallOfFlesh || 
               entity instanceof WallOfFleshEye || 
               entity instanceof WallOfFleshMouse ||
               entity instanceof TheHungry || 
               entity.getType() == TEMonsterEntities.LEECH.get();
    }

    public void recreateFromPacket(ClientboundAddEntityPacket packet) {
        super.recreateFromPacket(packet);
       // WallOfFleshPart.assignPartIDs(this);
    }
}