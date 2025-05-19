package org.confluence.terraentity.entity.boss;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.confluence.terraentity.effect.harmful.HorrifiedEffect;
import org.confluence.terraentity.entity.ai.Boss;
import org.confluence.terraentity.entity.monster.TheHungry;
import org.confluence.terraentity.entity.monster.prefab.AbstractPrefab;
import org.confluence.terraentity.init.TEEffects;
import org.confluence.terraentity.init.TESounds;
import org.confluence.terraentity.init.entity.TEMonsterEntities;
import org.confluence.terraentity.utils.CameraShakeData;
import org.confluence.terraentity.utils.CameraShakeManager;

import javax.annotation.Nullable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;


public class WallOfFlesh extends AbstractTerraBossBase<WallOfFlesh> implements Boss {
    public static final float MAX_HEALTHS = 3068f;
    private static final float DAMAGE = 39f;//接触伤害

    static float moveSpeedBase = 0.15f;//移动速度

    boolean genSegments = true;//是否生成体节
    int genTick = 10;//生成体节延迟
    boolean shouldMove = true;

    float inverseFactor = Mth.clamp(0.5F - getHealthPercentage(),0.0F,1.0F);
    float moveSpeed = getHealthPercentage()<=0.5f ? moveSpeedBase * 1.5f + moveSpeedBase * inverseFactor : moveSpeedBase;
    Vec3 InitPos = Vec3.ZERO;

    public AABB insideCollisionBox;
    public AABB outsideCollisionBox;

    private int gridSizeX = 15;
    private int gridSizeY = 10;
    public float gridSpacing = 15.0f;

    private static int summonCDAll = 1200; //仆从召唤cd
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
            boolean[][] eyeMarkers = new boolean[gridSizeX][gridSizeY];
            boolean[][] mouthMarkers = new boolean[gridSizeX][gridSizeY];
            boolean[][] entityMarkers = new boolean[gridSizeX][gridSizeY];
            // 眼睛生成逻辑
            Map<Integer, List<Double>> eyeYPositions = new HashMap<>();
            for (int x = 0; x < gridSizeX; x++) {
                for (int y = 0; y < gridSizeY; y++) {
                    Vec3 eyeBasePos = new Vec3(
                            (x - gridSizeX / 2.0) * gridSpacing,
                            y * gridSpacing,
                            0
                    ).add(baseOffset);

                    if (isValidEyePosition(x, y, eyeMarkers)) {
                        WallOfFleshEye eye = new WallOfFleshEye(level());
                        addChildSegment(eye, eyeBasePos);
                        eye.setYRot(this.getYRot());
                        eye.setParent(this);
                        level().addFreshEntity(eye);
                        eyeMarkers[x][y] = true;

                        eyeYPositions.computeIfAbsent(x, k -> new ArrayList<>())
                                .add(eyeBasePos.y);
                    }
                }
            }
            // 嘴巴生成逻辑
            eyeYPositions.forEach((gridX, yCoords) -> {
                Collections.sort(yCoords);
                for (int i = 0; i < yCoords.size() - 1; i++) {
                    if (random.nextDouble() < 2.0 / 3) {
                        double midY = (yCoords.get(i) + yCoords.get(i + 1)) / 2.0;
                        int gridY = (int) (midY / gridSpacing);
                        Vec3 mouthPos = new Vec3(
                                (gridX - gridSizeX / 2.0) * gridSpacing,
                                midY,
                                0
                        ).add(baseOffset);

                        WallOfFleshMouth mouth = new WallOfFleshMouth(level());
                        addChildSegment(mouth, mouthPos);
                        mouth.parentMob = this;
                        mouth.setYRot(this.getYRot());
                        level().addFreshEntity(mouth);

                        if (gridY >= 0 && gridY < gridSizeY) {
                            mouthMarkers[gridX][gridY] = true;
                            entityMarkers[gridX][gridY] = eyeMarkers[gridX][gridY] || mouthMarkers[gridX][gridY];
                        }

                    }
                }
            });
            // 饿鬼生成逻辑
            for (int x = 0; x < gridSizeX; x++) {
                for (int y = 0; y < gridSizeY; y++) {
                    Vec3 theHungryPos = new Vec3(
                            (x - gridSizeX / 2.0) * gridSpacing,
                            y * gridSpacing,
                            0
                    ).add(baseOffset);

                    if (!this.level().isClientSide() && !entityMarkers[x][y]) {
                        TheHungry hungry = new TheHungry(TEMonsterEntities.THE_HUNGRY.get(), level(),new AbstractPrefab(60,2,15,32,0.75f,1).getPrefab()) {
                            @Override
                            protected boolean shouldDropLoot() {
                                return false;
                            }
                        };
                        addChildSegment(hungry, theHungryPos);
                        this.theHungryMap.put(theHungryPos, hungry);
                        hungry.minion_setOwner(this);
                        hungry.setYRot(this.getYRot());
                        hungry.setInitPos(this.position().add(theHungryPos).toVector3f());
                        level().addFreshEntity(hungry);
                    }
                }
            }
            Boss.sendBossSpawnMessage(this);
        }
    }

    // 间隔验证方法（曼哈顿距离）
    private boolean isValidEyePosition(int x, int y, boolean[][] eyeGrid) {
        final int BASE_RADIUS_X = 3;
        final int BASE_RADIUS_Y = 2;
        final double DENSITY_FACTOR = 0.47;
        final RandomSource rand = this.random;

        int dynamicRadiusY = BASE_RADIUS_Y + rand.nextInt(1);

        int verticalJitter = rand.nextInt(2);

        boolean earlyExit = false;

        // 非对称检测区域循环
        outer:
        for(int dx = -BASE_RADIUS_X; dx <= BASE_RADIUS_X; dx++) {
            // 30%概率提前退出横向检测
            if(rand.nextDouble() < 0.3) break outer;

            for(int dy = -dynamicRadiusY; dy <= dynamicRadiusY; dy++) {
                int jitteredDy = dy + verticalJitter;

                double distanceX = Math.abs(dx) * (0.9 + rand.nextDouble()*0.2);
                double distanceY = Math.abs(jitteredDy) * (1.0 - rand.nextDouble()*0.6);

                if((distanceX*distanceX)/(BASE_RADIUS_X*BASE_RADIUS_X*2) +(distanceY*distanceY)/(dynamicRadiusY*dynamicRadiusY) < 1)
                {
                    int nx = x + dx;
                    int ny = y + jitteredDy;

                    if(nx >=0 && nx < eyeGrid.length && ny >=0 && ny < eyeGrid[0].length) {
                        if(eyeGrid[nx][ny]) {
                            double verticalDecay = 1.0 - (distanceY / (dynamicRadiusY * 1.2));
                            if(rand.nextDouble() < DENSITY_FACTOR * verticalDecay) {
                                earlyExit = true;
                                break outer;
                            }
                        }
                    }
                }
            }
        }
        return !earlyExit;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public boolean isPickable() {
        return false;
    }

    @Override
    public Vec2 getRotationVector() {
        // 标准化YRot到0°或180°
        float originalYaw = this.getYRot();
        float normalizedYaw = (originalYaw + 180) % 360;
        if (normalizedYaw < 0) normalizedYaw += 360;
        normalizedYaw -= 180; // 范围[-180, 180)

        float alignedYaw = (normalizedYaw >= -90 && normalizedYaw < 90) ? 0 : 180;

        return new Vec2(this.getXRot(), alignedYaw);
    }

    @Override
    public Vec3 getForward() {
        float yawRad = (float) Math.toRadians(this.getYRot());
        return new Vec3(-Math.sin(yawRad), 0, Math.cos(yawRad));
    }

    public void setForward(boolean minus) {
        this.setYRot(minus ? 180 : 0);
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
            if (child instanceof WallOfFleshMouth) child.setYRot(this.getYRot());
        }
    }

    @Override
    public void onAddedToLevel(){
        super.onAddedToLevel();
        this.noPhysics = true;
        this.noCulling = true;
        this.setNoGravity(true);
        double summonDir = 50;
        this.setForward(this.getZ() <= 0);
        Vec3 summonPos = this.position().add(getForward().scale(-summonDir).add(0, this.level().getMinBuildHeight(), 0));
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
                    TheHungry newHungry = new TheHungry(TEMonsterEntities.THE_HUNGRY.get(), level(),new AbstractPrefab(60,2,15,32,0.75f,1).getPrefab()) {
                        @Override
                        protected boolean shouldDropLoot() {
                            return false;
                        }
                    };
                    this.theHungryMap.put(theHungryPos, newHungry);
                    newHungry.minion_setOwner(this);
                    newHungry.setInitPos(this.position().add(theHungryPos).toVector3f());
                    addChildSegment(newHungry, theHungryPos);
                    level().addFreshEntity(newHungry);
                }
            }
        }

        //召唤的瞬间位置为初始值，要延迟召唤segments
        if (this.tickCount > genTick && genSegments && this.dirty) {
            genGridWall();
            genSegments = false;

            if (!level().isClientSide) {
                CameraShakeManager.addCameraShake(new CameraShakeData(600, this.position(), 90));
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
        double x = gridSizeX * gridSpacing/2;
        double y = gridSizeY * gridSpacing;
        double z = 0;
        insideCollisionBox = new AABB(
                this.position().subtract(x+gridSpacing/2, 0, z),
                this.position().add(x-gridSpacing/2, y, z + 150 * this.getForward().z));

        return this.insideCollisionBox;
    }

    public AABB getOutsideCollisionBox() {
        double x = gridSizeX * gridSpacing/2 + 150;
        double y = gridSizeY * gridSpacing + 150;
        double z = 200;
        outsideCollisionBox = new AABB(
                this.position().subtract(x+gridSpacing/2, 150, z),
                this.position().add(x-gridSpacing/2, y, z));
        return this.outsideCollisionBox;
    }

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

    public boolean isInvulnerableTo(DamageSource source) {
        if(source.is(DamageTypeTags.IS_FIRE)||source.is(DamageTypeTags.IS_DROWNING)){
            return true;
        }
        return super.isInvulnerableTo(source);
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return TESounds.ROUTINE_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return TESounds.ROUTINE_DEATH.get();
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
        return entity instanceof WallOfFlesh || entity instanceof WallOfFleshEye || entity instanceof WallOfFleshMouth || entity instanceof TheHungry;
    }
}