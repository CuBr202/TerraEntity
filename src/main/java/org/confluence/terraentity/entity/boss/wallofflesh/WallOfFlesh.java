package org.confluence.terraentity.entity.boss.wallofflesh;

import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
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
import org.confluence.terraentity.init.entity.TEMonsterEntities;
import org.confluence.terraentity.utils.CameraShakeData;
import org.confluence.terraentity.utils.CameraShakeManager;
import org.confluence.terraentity.utils.TEUtils;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;


public class WallOfFlesh extends AbstractTerraBossBase<WallOfFlesh> implements Boss {
    public static final float MAX_HEALTHS = 3096;
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

    private final int gridSizeX = 15;
    private final int gridSizeY = 10;
    public float gridSpacing = 15.0f;

    private static final int summonCDAll = 1200; //饿鬼召唤cd
    private int summonCD = summonCDAll;

    private static final double FINISH_LINE_DISTANCE = 2000;

    public List<Entity> baseSegments = new CopyOnWriteArrayList<>();

    public final ConcurrentHashMap<Entity, Vec3> localOffsets = new ConcurrentHashMap<>(); // 存储子实体相对坐标
    public final ConcurrentHashMap<Vec3, TheHungry> theHungryMap = new ConcurrentHashMap<>();

    public WallOfFlesh(EntityType<? extends Monster> type, Level level) {
        super(type, level,MAX_HEALTHS, 0);
        this.noPhysics = true;
        this.noCulling = true;
        this.setNoGravity(true);
        this.xpReward = 3000;
    }

    @Override
    public boolean isNoGravity(){
        return true;
    }

    @Override
    protected void registerGoals() {}

    public void addChildSegment(Entity child, Vec3 localOffset) {
        baseSegments.add(child);
        localOffsets.put(child, localOffset);
        child.setPos(this.position().add(localOffset));
    }

    private void genGridWall() {
        if (!this.level().isClientSide) {
            Vec3 baseOffset = this.getForward().scale(7.5F);
            
            // 四叉树生成参数
            final int MAX_DEPTH = 5; // 减少深度
            final double EYE_CHANCE = 0.8; // 眼睛生成概率
            final double MOUTH_CHANCE = 0.4; // 嘴巴生成概率
            final double HUNGRY_CHANCE = 0.2; // 饿鬼生成概率
            final double SUBDIVISION_CHANCE = 0.85; // 细分概率
            
            // 存储生成的位置
            List<Vec3> eyePositions = new ArrayList<>();
            List<Vec3> mouthPositions = new ArrayList<>();
            List<Vec3> hungryPositions = new ArrayList<>();
            
            // 使用四叉树生成眼睛、嘴巴和饿鬼位置
            generateAllEntitiesQuadTree(0, 0, gridSizeX, gridSizeY, 0, MAX_DEPTH, 
                                      EYE_CHANCE, MOUTH_CHANCE, HUNGRY_CHANCE, SUBDIVISION_CHANCE,
                                      eyePositions, mouthPositions, hungryPositions, baseOffset);
            
            // 额外在眼睛之间生成嘴巴
            generateMouthsBetweenEyes(eyePositions, mouthPositions, baseOffset);
            
            // 生成眼睛
            for (Vec3 eyePos : eyePositions) {
                WallOfFleshEye eye = TEUtils.spawnEntity(() -> new WallOfFleshEye(level()), (ServerLevel) level(), eyePos);
                if (eye != null) {
                    addChildSegment(eye, eyePos);
                    eye.setYRot(this.getYRot());
                    eye.setParent(this);
                }
            }
            
            // 生成嘴巴
            for (Vec3 mouthPos : mouthPositions) {
                WallOfFleshMouth mouth = TEUtils.spawnEntity(() -> new WallOfFleshMouth(level()), (ServerLevel) level(), mouthPos);
                        if (mouth != null) {
                            addChildSegment(mouth, mouthPos);
                            mouth.parentMob = this;
                            mouth.setYRot(this.getYRot());
                }
            }
            
            // 生成饿鬼
            for (Vec3 hungryPos : hungryPositions) {
                TheHungry hungry = TEUtils.spawnEntity(() -> new TheHungry(TEMonsterEntities.THE_HUNGRY.get(), level(), 
                    new AbstractPrefab(60, 2, 15, 32, 0.75f, 1).getPrefab()) {
                            @Override
                            protected boolean shouldDropLoot() {
                                return false;
                            }
                }, (ServerLevel) level(), hungryPos);

                if (hungry != null) {
                    addChildSegment(hungry, hungryPos);
                    this.theHungryMap.put(hungryPos, hungry);
                            hungry.minion_setOwner(this);
                            hungry.setYRot(this.getYRot());
                    hungry.setInitPos(this.position().add(hungryPos).toVector3f());
                }
            }
            
            Boss.sendBossSpawnMessage(this);
        }
    }

    //四叉树

    private void generateAllEntitiesQuadTree(int x, int y, int width, int height, int depth, int maxDepth,
                                           double eyeChance, double mouthChance, double hungryChance, double subdivisionChance,
                                           List<Vec3> eyePositions, List<Vec3> mouthPositions, List<Vec3> hungryPositions, Vec3 baseOffset) {

        if (width <= 0 || height <= 0) {
            return;
        }

        int centerX = x + width / 2;
        int centerY = y + height / 2;

        double maxOffset = gridSpacing * 0.8;
        double offsetX = (random.nextDouble() - 0.5) * maxOffset;
        double offsetY = (random.nextDouble() - 0.5) * maxOffset;

        Vec3 worldPos;
        if (isMovingAlongX()) {
            worldPos = new Vec3(
                0,
                centerY * gridSpacing + offsetY,
                (centerX - gridSizeX / 2.0) * gridSpacing + offsetX
            ).add(baseOffset);
        } else {
            worldPos = new Vec3(
                (centerX - gridSizeX / 2.0) * gridSpacing + offsetX,
                centerY * gridSpacing + offsetY,
                0
            ).add(baseOffset);
        }

        boolean shouldSubdivide = depth < maxDepth && 
                                width > 1 && height > 1 && 
                                random.nextDouble() < subdivisionChance;

        if (depth < 3 && random.nextDouble() < 0.95) {
            shouldSubdivide = true;
        }
        
        if (shouldSubdivide) {
            int halfWidth = width / 2;
            int halfHeight = height / 2;

            generateAllEntitiesQuadTree(x, y, halfWidth, halfHeight, depth + 1, maxDepth,
                                      eyeChance, mouthChance, hungryChance, subdivisionChance * 0.95,
                                      eyePositions, mouthPositions, hungryPositions, baseOffset);
            
            generateAllEntitiesQuadTree(x + halfWidth, y, width - halfWidth, halfHeight, depth + 1, maxDepth,
                                      eyeChance, mouthChance, hungryChance, subdivisionChance * 0.95,
                                      eyePositions, mouthPositions, hungryPositions, baseOffset);
            
            generateAllEntitiesQuadTree(x, y + halfHeight, halfWidth, height - halfHeight, depth + 1, maxDepth,
                                      eyeChance, mouthChance, hungryChance, subdivisionChance * 0.95,
                                      eyePositions, mouthPositions, hungryPositions, baseOffset);
            
            generateAllEntitiesQuadTree(x + halfWidth, y + halfHeight, width - halfWidth, height - halfHeight, depth + 1, maxDepth,
                                      eyeChance, mouthChance, hungryChance, subdivisionChance * 0.95,
                                      eyePositions, mouthPositions, hungryPositions, baseOffset);
        } else {
            double rand = random.nextDouble();

            boolean hasConflict = false;
            double conflictDistance = gridSpacing * 0.6;

            for (Vec3 existingPos : eyePositions) {
                if (existingPos.distanceToSqr(worldPos) < conflictDistance * conflictDistance) {
                    hasConflict = true;
                    break;
                }
            }
            
            if (!hasConflict) {
                for (Vec3 existingPos : mouthPositions) {
                    if (existingPos.distanceToSqr(worldPos) < conflictDistance * conflictDistance) {
                        hasConflict = true;
                        break;
                    }
                }
            }
            
            if (!hasConflict) {
                for (Vec3 existingPos : hungryPositions) {
                    if (existingPos.distanceToSqr(worldPos) < conflictDistance * conflictDistance) {
                        hasConflict = true;
                        break;
                    }
                }
            }
            
            if (!hasConflict) {
                if (rand < eyeChance) {
                    eyePositions.add(worldPos);
                } else if (rand < eyeChance + mouthChance) {
                    mouthPositions.add(worldPos);
                } else if (rand < eyeChance + mouthChance + hungryChance) {
                    hungryPositions.add(worldPos);
                }
            }
        }
    }
    
    /**
     * 在上下相邻且空间足够的眼睛中间生成嘴巴
     */
    private void generateMouthsBetweenEyes(List<Vec3> eyePositions, List<Vec3> mouthPositions, Vec3 baseOffset) {
        // 按网格X坐标分组眼睛
        Map<Integer, List<Vec3>> eyesByGridX = new HashMap<>();
        
        for (Vec3 eyePos : eyePositions) {
            int gridX;
            if (isMovingAlongX()) {
                gridX = (int) Math.round((eyePos.z - baseOffset.z) / gridSpacing + gridSizeX / 2.0);
            } else {
                gridX = (int) Math.round((eyePos.x - baseOffset.x) / gridSpacing + gridSizeX / 2.0);
            }
            
            if (gridX >= 0 && gridX < gridSizeX) {
                eyesByGridX.computeIfAbsent(gridX, k -> new ArrayList<>()).add(eyePos);
            }
        }

        eyesByGridX.forEach((gridX, eyesInColumn) -> {
            eyesInColumn.sort((a, b) -> Double.compare(a.y, b.y));
            
            for (int i = 0; i < eyesInColumn.size() - 1; i++) {
                Vec3 eye1 = eyesInColumn.get(i);
                Vec3 eye2 = eyesInColumn.get(i + 1);

                double distance = Math.abs(eye2.y - eye1.y);
                if (distance >= gridSpacing * 1.5) {
                    double midY = (eye1.y + eye2.y) / 2.0;
                    
                    Vec3 mouthPos;
        if (isMovingAlongX()) {
                        mouthPos = new Vec3(
                0,
                            midY,
                            (gridX - gridSizeX / 2.0) * gridSpacing
            ).add(baseOffset);
        } else {
                        mouthPos = new Vec3(
                            (gridX - gridSizeX / 2.0) * gridSpacing,
                            midY,
                0
            ).add(baseOffset);
        }

        boolean hasConflict = false;
                    for (Vec3 existingMouth : mouthPositions) {
                        if (existingMouth.distanceToSqr(mouthPos) < gridSpacing * gridSpacing * 0.5) {
                hasConflict = true;
                break;
            }
        }
        
                    if (!hasConflict && random.nextDouble() < 0.8) {
                        mouthPositions.add(mouthPos);
                    }
                }
            }
        });
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
            case EAST -> this.setYRot(0.0F);    // 东: 0°
            case SOUTH -> this.setYRot(90.0F);   // 南: 90°
            case WEST -> this.setYRot(180.0F);   // 西: 180°
            case NORTH -> this.setYRot(270.0F);  // 北: 270°
            default -> throw new IllegalArgumentException("Invalid direction: " + direction);
        }
    }

    private void updateChildPosition(Entity child) {
        if(!level().isClientSide) {
            // 获取子实体相对于父实体的局部坐标
            Vec3 localOffset = localOffsets.get(child);

            if (localOffset == null)
                localOffset = Vec3.ZERO;

            Vec3 childPos = this.position().add(localOffset);

            // 更新子实体位置
            if (child instanceof TheHungry hungry) {
                Vec3 vec3 = hungry.position().subtract(hungry.getInitPos());
                Vec3 summonPos = childPos.add(vec3);
                hungry.setPos(summonPos);
                hungry.setInitPos(childPos.toVector3f());
            } else if (child instanceof WallOfFleshEye || child instanceof WallOfFleshMouth) {
                child.setPos(childPos.x, childPos.y, childPos.z);
            }
        }
    }

    @Override
    public void onAddedToLevel(){
        super.onAddedToLevel();
        this.noPhysics = true;
        this.noCulling = true;
        this.setNoGravity(true);
        double summonDir = 50;

        Vec3 summonPos = new Vec3(this.position().x, this.level().getMinBuildHeight(), this.position().z).add(getForward().scale(-summonDir));
        this.moveTo(summonPos);
        this.InitPos = summonPos;
        for (Entity child : baseSegments) {
            child.moveTo(this.position().add(localOffsets.get(child)));
        }
        this.setAttactDamage(DAMAGE);
    }

    @Override
    public boolean shouldBeSaved(){
        return false;
    }

    @Override
    public void tick() {
        super.tick();

        for (Entity child : baseSegments) {
            updateChildPosition(child);
        }
        if (!this.level().isClientSide) {
            if (--summonCD <= 0) {
                 summonCD = summonCDAll;
                for (Vec3 theHungryPos : theHungryMap.keySet()) {
                    TheHungry hungry = theHungryMap.get(theHungryPos);
                    if (hungry.isAlive()) continue;
                    TheHungry newHungry = TEUtils.spawnEntity(()->new TheHungry(TEMonsterEntities.THE_HUNGRY.get(), level(),new AbstractPrefab(60,2,15,32,0.75f,1).getPrefab()) {
                        @Override
                        protected boolean shouldDropLoot() {
                            return false;
                        }
                    }, (ServerLevel)level(), theHungryPos);

                    if (newHungry != null) {
                        this.theHungryMap.put(theHungryPos, newHungry);
                        newHungry.minion_setOwner(this);
                        newHungry.setInitPos(this.position().add(theHungryPos).toVector3f());
                        addChildSegment(newHungry, theHungryPos);
                    }

                }
            }
        }

        if (this.tickCount > genTick && genSegments && this.dirty) {
            genGridWall();
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

            if (!this.level().getWorldBorder().isWithinBounds(this.position())) {
                // 如果肉山碰撞到世界边界，立即死亡
                this.discard();
                return;
            }

            // 四方向终点判断
            if ((moveDirection.x > 0 && progress >= FINISH_LINE_DISTANCE) || // 东方向
                    (moveDirection.x < 0 && progress <= -FINISH_LINE_DISTANCE) || // 西方向
                    (moveDirection.z > 0 && progress >= FINISH_LINE_DISTANCE) || // 南方向
                    (moveDirection.z < 0 && progress <= -FINISH_LINE_DISTANCE)) { // 北方向
                this.discard();
            }
            this.addDeltaMovement(getForward().scale(moveSpeed).scale(0.1F));
        }

        if (this.getInsideBox()!= null&&this.getOutsideCollisionBox()!= null) {
            List<Player> nearbyPlayers = level().getEntitiesOfClass(Player.class,
                    this.getOutsideCollisionBox());

            nearbyPlayers.stream().filter(LivingEntity::canBeSeenByAnyone).forEach(player -> {
                DeferredHolder<MobEffect, HorrifiedEffect> horrifiedHolder = TEEffects.HORRIFIED;
                horrifiedHolder.get().setWallOfFlesh(this);
                player.addEffect(new MobEffectInstance(horrifiedHolder, 200, 3, false, true));
            });
        }
    }

    public AABB getInsideBox() {
        Direction dir = this.getDirection();
        boolean isReverse = dir == Direction.WEST || dir == Direction.SOUTH;
        int completion = isReverse?0:5;
        if(isMovingAlongX()) {
            double x = completion;
            double y = gridSizeY * gridSpacing - (double) gridSizeY /2;
            double z = gridSizeX * gridSpacing / 2;
            insideCollisionBox = new AABB(
                    this.position().subtract(x, 0 + (double) gridSizeY /2, z + gridSpacing / 2),
                    this.position().add(x + 150 * this.getForward().x, y, z + 150 * this.getForward().z - gridSpacing / 2));
        }else {
            double x = gridSizeX * gridSpacing / 2;
            double y = gridSizeY * gridSpacing - (double) gridSizeY /2;
            double z = completion;
            insideCollisionBox = new AABB(
                    this.position().subtract(x + gridSpacing / 2, 0 + (double) gridSizeY /2, z),
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
            double y = gridSizeY * gridSpacing + 150;
            double z = gridSizeX * gridSpacing / 2 + 150;
            outsideCollisionBox = new AABB(
                    this.position().subtract(x, 150, z + gridSpacing / 2),
                    this.position().add(x, y, z - gridSpacing / 2));
        }else {
            double x = gridSizeX * gridSpacing / 2 + 150;
            double y = gridSizeY * gridSpacing + 150;
            double z = completion1;
            outsideCollisionBox = new AABB(
                    this.position().subtract(x + gridSpacing / 2, 150, z),
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
        baseSegments.forEach(Entity::onRemovedFromLevel);
        if(!this.isAlive()){
            baseSegments.forEach(Entity::discard);
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
        baseSegments.forEach(Entity::discard);
        localOffsets.clear();
        theHungryMap.forEach((pos, hungry) -> hungry.discard());
        super.die(damageSource);
    }

    @Override
    public boolean hasLineOfSight(Entity entity) {
        double size = this.getOutsideCollisionBox().getSize()*1.5F;
        return distanceToSqr(entity) < size * size;
    }

    @Override
    public boolean isMainBody() {
        return true;
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

    public static boolean isWallOfFlesh(Entity entity) {
        return entity instanceof WallOfFlesh || entity instanceof WallOfFleshEye || entity instanceof WallOfFleshMouth;
    }

    public static boolean isWallOfFleshMob(Entity entity) {
        if (entity == null) {
            return false;
        }
        return entity instanceof WallOfFlesh || 
               entity instanceof WallOfFleshEye || 
               entity instanceof WallOfFleshMouth || 
               entity instanceof TheHungry || 
               entity.getType() == TEMonsterEntities.LEECH.get();
    }
}