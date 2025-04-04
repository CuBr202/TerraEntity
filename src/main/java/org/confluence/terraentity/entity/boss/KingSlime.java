package org.confluence.terraentity.entity.boss;

import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.JumpControl;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.confluence.terraentity.client.gui.CustomizeBossHealthBar;
import org.confluence.terraentity.entity.ai.Boss;
import org.confluence.terraentity.entity.ai.IBossFSM;
import org.confluence.terraentity.entity.model.CrownOfKingSlimeModelEntity;
import org.confluence.terraentity.entity.monster.slime.BaseSlime;
import org.confluence.terraentity.entity.util.DeathAnimOptions;
import org.confluence.terraentity.init.TEParticles;
import org.confluence.terraentity.init.entity.TEBossEntities;
import org.confluence.terraentity.init.entity.TEMonsterEntities;
import org.confluence.terraentity.mixin.accessor.SlimeAccessor;
import org.confluence.terraentity.mixinauxiliary.IBossEvent;
import org.confluence.terraentity.network.s2c.SyncBossEventHealthPacket;
import org.confluence.terraentity.utils.AdapterUtils;
import org.confluence.terraentity.utils.FloatRGB;
import org.confluence.terraentity.utils.TEUtils;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;

import static org.confluence.terraentity.utils.TEUtils.isAtLeastExpert;
import static org.confluence.terraentity.utils.TEUtils.switchByDifficulty;

/**
 * 史王
 */
@SuppressWarnings("all")
public class KingSlime extends Slime implements DeathAnimOptions, IBossFSM, Boss{
    private static final int COLOR_INT = 0x73bcf4;
    // 缩小/膨胀时长，单位：刻
    private static final int SHRINK_ENLARGE_DURATION = 20;
    // 大师 专家 普通
    private static final int[] TOTAL_SPLITS = {75, 50, 30};
    private static final float[] MAX_HEALTHS = {520f, 728f, 928f};
    private static final float[] DAMAGE = {26f, 20f, 10f};
    private static final float[] JUMP_SPEED_HORIZONTAL = {1.1f, 1.35f, 1.55f};
    private static final float[] JUMP_SPEED_VERTICAL = {1.5f, 1.75f, 2f};
    private static final float[] JUMP_SPEED_VERTICAL_THIRD = {2f, 2.25f, 2.5f};
    private static final float[] SWIM_SPEED_HORIZONTAL = {0.1f, 0.15f, 0.2f};
    private static final float FLOATING_ACCELERATION = 0.05f;
    private static final FloatRGB COLOR = FloatRGB.fromInteger(COLOR_INT);
    private static final float[] BLOOD_COLOR = COLOR.mixture(FloatRGB.ZERO, 0.5f).toArray();
    private static final State<KingSlime> STATE_NORMAL = new State<>() {
        private Vec3 horVel = Vec3.ZERO;
        @Override
        public void tick(KingSlime boss) {
            // 脱战
            List<Player> playersInRange = boss.getNearbyPlayers(100);

            if (! boss.shouldDisappear) {
                // 不要每次都初始化playersInRange2
                if (playersInRange.isEmpty() && boss.getNearbyPlayers(150).isEmpty()) {
                    boss.shouldDisappear = true;
                }
            }

            // 更新BOSS大小
            boss.setSize( boss.getMaxSize(), false );
            // 在地面上/液体中
            boolean inLiquid = boss.isInWater() || boss.isInLava();
            if (boss.onGround() || inLiquid) {
                boss.indexAI ++;
                // 缩地消失
                if (boss.shouldDisappear) {
                    boss.toState(STATE_SHRINK);
                    return;
                }
                double horizontalSpd;
                double verticalAcc;
                // 漂浮、移动
                if (inLiquid) {
                    horizontalSpd = SWIM_SPEED_HORIZONTAL[boss.difficultyIdx];
                    verticalAcc = FLOATING_ACCELERATION;
                }
                // 跳跃
                else {
                    switch (boss.indexAI) {
                        case 20, 40, 60 -> {
                            horizontalSpd = JUMP_SPEED_HORIZONTAL[boss.difficultyIdx];
                            verticalAcc = (boss.indexAI == 60 ? JUMP_SPEED_VERTICAL_THIRD : JUMP_SPEED_VERTICAL)[boss.difficultyIdx];
                        }
                        default -> {
                            horizontalSpd = 0;
                            verticalAcc = 0;
                        }
                    }
                }
                // 调整速度
                horVel = TEUtils.rotToDir(boss.getYRot(), 0).scale(horizontalSpd);
                if (verticalAcc != 0) {
                    Vec3 motion = boss.getDeltaMovement();
                    motion = motion.add(0, verticalAcc, 0);
                    boss.setDeltaMovement(motion);
                }

                // 下一阶段
                if (boss.indexAI >= 65) {
                    boss.toState(STATE_SHRINK);
                }
                // 水平方向速度更新
                boss.setHorizontalSpeed(horVel);
            }
            // 重置水平方向速度
            else {
                horVel = Vec3.ZERO;
            }
        }
    };
    private static final State<KingSlime> STATE_SHRINK = new State<>() {
        @Override
        public void tick(KingSlime boss) {
            boss.indexAI ++;
            // 防止BOSS水平方向的移动
            boss.setHorizontalSpeed(Vec3.ZERO);
            // 更新BOSS大小
            int maxSize = boss.getMaxSize();
            int s = Mth.clamp(1, boss.getMaxSize() *
                    (SHRINK_ENLARGE_DURATION - boss.indexAI) / SHRINK_ENLARGE_DURATION, maxSize);

            boss.setSize( s, false );
            if (boss.indexAI >= SHRINK_ENLARGE_DURATION) {
                // BOSS脱战
                if (boss.shouldDisappear) {
                    boss.discard();
                    return;
                }
                // TP到玩家位置并开始膨胀
                if (boss.level() instanceof ServerLevel serverLevel) {
                    Vec3 closestPlayerPos;
                    if (serverLevel.getRandomPlayer() != null) {
                        if (boss.getTarget() != null){
                            closestPlayerPos = boss.getTarget().getOnPos().getCenter();
                        } else {
                            closestPlayerPos = serverLevel.getRandomPlayer().getOnPos().getCenter();
                        }
                        serverLevel.addFreshEntity(new CrownOfKingSlimeModelEntity(serverLevel, boss.position().add(0.0, boss.getDimensions(boss.getPose()).height, 0.0)));
                        boss.teleportTo(closestPlayerPos.x, closestPlayerPos.y + 0.75F, closestPlayerPos.z);
                    }
                }
                boss.toState(STATE_ENLARGE);
            }
        }
    };
    private static final State<KingSlime> STATE_ENLARGE = new State<>() {
        @Override
        public void tick(KingSlime boss) {
            boss.indexAI ++;
            // 防止BOSS水平方向的移动
            boss.setHorizontalSpeed(Vec3.ZERO);
            // 更新BOSS大小
            int maxSize = boss.getMaxSize();

            boss.setSize( Mth.clamp(1, boss.getMaxSize() *
                    boss.indexAI / SHRINK_ENLARGE_DURATION, maxSize), false );
            if (boss.indexAI >= SHRINK_ENLARGE_DURATION) {
                boss.toState(STATE_NORMAL);
            }
        }
    };

    // 变量
    private final ServerBossEvent bossEvent = (ServerBossEvent) new ServerBossEvent(getDisplayName(), BossEvent.BossBarColor.BLUE, BossEvent.BossBarOverlay.NOTCHED_12).setPlayBossMusic(true);
    private int indexAI;
    private final int difficultyIdx;
    private boolean shouldDisappear;
    private State<KingSlime> AIState;
    // 重写跳跃-水平方向的移动
    private Vec3 horMoveDir;

    public KingSlime(EntityType<KingSlime> slime, Level level) {
        super(slime, level);
        this.shouldDisappear = false;
        this.difficultyIdx = switchByDifficulty(level, 0, 1, 2);
        this.indexAI = 0;
        this.AIState = STATE_NORMAL;

        // 重写跳跃-防止垂直方向的跳跃
        this.jumpControl = new JumpControl(this) {
            @Override
            public void jump() {}
        };
        // 水平方向移动
        horMoveDir = Vec3.ZERO;

        attrInit(this.getNearbyPlayers(100.0D));

        if(level().isClientSide){
            CustomizeBossHealthBar.registerBossHealthBar(getDisplayName().getString(),this.getType());
        }
        if(!level().isClientSide){
            try {
                TEUtils.multiplePlayerEnhance(this, true);
            }catch (Exception e){}
        }
        this.xpReward = 500;
    }

    public KingSlime(Level level) {
        this(TEBossEntities.KING_SLIME.get(), level);
    }

    @Override
    public void toState(State newState) {
        if (newState == this.AIState)
            return;
        this.AIState.leave(this);
        this.AIState = newState;
        this.AIState.enter(this);
        // 重置index
        this.indexAI = 0;
    }

    @Override
    public List<Player> getNearbyPlayers(double radius) {
        return level().getEntitiesOfClass(Player.class, getBoundingBox().inflate(radius));
    }

    public static AttributeSupplier.Builder createSlimeAttributes() {
        return Mob.createMobAttributes()
            .add(Attributes.ATTACK_DAMAGE, 1.0)
            .add(Attributes.ATTACK_KNOCKBACK, 2.2)
            .add(Attributes.ARMOR, 2)
            .add(Attributes.KNOCKBACK_RESISTANCE, 1)
            .add(Attributes.FOLLOW_RANGE, 100.0)

                ;
    }

    private void setHorizontalSpeed(Vec3 newDir) {
        Vec3 vel = getDeltaMovement();
        vel = vel.with(Direction.Axis.X, newDir.x()).with(Direction.Axis.Z, newDir.z());
        setDeltaMovement(vel);
    }

    // 原版跳跃依旧会略微顿一下，给调整到几乎不会触发的间隔
    @Override
    protected int getJumpDelay() {
        return Short.MAX_VALUE;
    }

    private void attrInit(List<Player> nearbyPlayers) {
        getAttribute(Attributes.MAX_HEALTH).setBaseValue(MAX_HEALTHS[difficultyIdx]);
        setHealth(MAX_HEALTHS[difficultyIdx]);
        getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(DAMAGE[difficultyIdx]);

        for (Player player : nearbyPlayers){
            //todo music
        }
    }

    private int getMaxSize() {
        return Math.round(getHealth() / getMaxHealth() * 10) + 6;
    }

    @Override
    public void AI() {
        // tick
        if (! level().isClientSide()) {
            this.AIState.tick(this);
//            ModUtils.testMessage(level(), this.jumping + ", " + getJumpDelay());
        }
    }

    protected void registerGoals() {
        this.targetSelector.addGoal(0, new HurtByTargetGoal(this));
        super.registerGoals();
    }

    public float[] getBossEventProgress(){
        return new float[]{this.getHealth(), this.getMaxHealth()};
    }

    public void syncBossHealthBar(ServerPlayer player){
        float[] datas = getBossEventProgress();
        ((IBossEvent)this.bossEvent).terra_enity$setBossHealth(datas[0]);
        ((IBossEvent)this.bossEvent).terra_enity$setBossMaxHealth(datas[1]);
        AdapterUtils.sendToPlayer(player, new SyncBossEventHealthPacket(bossEvent.getId(), datas[0], datas[1]));
    }

    @Override
    public void tick() {
        // 先进行super.tick()
        super.tick();

        // 更新boss血条
        if(!level().isClientSide()) {
            float[] datas = getBossEventProgress();
            ((IBossEvent)this.bossEvent).terra_enity$setBossHealth(datas[0]);
            ((IBossEvent)this.bossEvent).terra_enity$setBossMaxHealth(datas[1]);
            bossEvent.setProgress(datas[0] / datas[1]);
        }

        bossEvent.setName(getDisplayName());
        // 不会受到摔落伤害
        resetFallDistance();
        // 落地的粒子效果，十分的高级
        if (onGround() && !((SlimeAccessor) this).isWasOnGround()) {
            int i = getSize();
            for (int j = 0; j < i * 8; ++j) {
                float f = random.nextFloat() * Mth.TWO_PI;
                float f1 = random.nextFloat() * 0.5F + 0.5F;
                float f2 = Mth.sin(f) * (float) i * 0.5F * f1;
                float f3 = Mth.cos(f) * (float) i * 0.5F * f1;
                level().addParticle(TEParticles.ITEM_GEL.get(), getX() + (double) f2, getY(), getZ() + (double) f3, COLOR.red(), COLOR.green(), COLOR.blue());
            }
        }

        // 额外的AI行为
        if (! isNoAi()) {
            AI();
        }
    }

    @Override
    public void startSeenByPlayer(@NotNull ServerPlayer pServerPlayer) {
        super.startSeenByPlayer(pServerPlayer);
        bossEvent.addPlayer(pServerPlayer);
        if(tickCount != 0)
            syncBossHealthBar(pServerPlayer);
    }

    @Override
    public void stopSeenByPlayer(@NotNull ServerPlayer pServerPlayer) {
        super.stopSeenByPlayer(pServerPlayer);
        bossEvent.removePlayer(pServerPlayer);
    }

    private int getSlimesLeft() {
        return (int) (getHealth() / getMaxHealth() * TOTAL_SPLITS[difficultyIdx]);
    }

    private void spawnSlime(LivingEntity target) {
        if (level() instanceof ServerLevel serverLevel) {
            BaseSlime slime = new BaseSlime(TEMonsterEntities.BLUE_SLIME.get(), serverLevel, COLOR_INT, 2);
            slime.setPos(getOnPos().getX(), getOnPos().getY() + 0.5, getOnPos().getZ());
            slime.setTarget(target);
            if (isAtLeastExpert(serverLevel)) {
                //todo 尖刺史莱姆
                //尖刺史莱姆，你的头顶怎么尖尖的
            }
            serverLevel.addFreshEntity(slime);
        }
    }
    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        if (pSource.is(DamageTypes.IN_WALL)) {
            return false;
        }
        // 在大小改变时不会受伤
        if (AIState != STATE_NORMAL) {
            return false;
        }
        // 记录受伤前生命对应的剩余分裂次数
        int lastSlimesLeft = getSlimesLeft();

        boolean result = super.hurt(pSource, pAmount);

        // 根据受伤前后剩余分裂次数差生成史莱姆
        for (int i = 0; i < lastSlimesLeft - getSlimesLeft(); i ++) {
            if (this.getTarget() != null){
                spawnSlime(this.getTarget());
            } else {
                spawnSlime(null);
            }
        }

        return result;
    }

    @Override
    public void onAddedToWorld(){
        super.onAddedToWorld();
        if(!level().isClientSide){
            if(bossEvent!= null){
                bossEvent.getPlayers().forEach(p->syncBossHealthBar(p));
            }
        }
    }

    // 不要被推来推去
    @Override
    public boolean isPushable(){
        return false;
    }

    // 缩地期间不要伤害玩家
    public void playerTouch(Player pEntity) {
        if (AIState != STATE_NORMAL) {
            return;
        }

        super.playerTouch(pEntity);
    }

    // 不要让史莱姆生成默认落地粒子
    @Override
    protected boolean spawnCustomParticles() {
        return true;
    }

    @Override
    public void setSize(int pSize, boolean pResetHealth) {
        int i = Mth.clamp(pSize, 1, 127);
        entityData.set(ID_SIZE, i);
        reapplyPosition();

        refreshDimensions();
        getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.1F * i);
        this.xpReward = i;
    }

    @Override
    public void remove(@NotNull RemovalReason removalReason) {
        brain.clearMemories();
        setRemoved(removalReason);
//        invalidateCaps();
    }

    protected int calculateFallDamage(float pFallDistance, float pDamageMultiplier) {
        return 0;
    }

    @Override
    public float[] getBloodColor() {
        return BLOOD_COLOR;
    }

    @Override
    public boolean canAttack(LivingEntity target) {
        return super.canAttack(target) && !(target instanceof Slime);
    }


    public static class HurtByTargetGoal extends TargetGoal {
        private static final TargetingConditions HURT_BY_TARGETING = TargetingConditions.forCombat().ignoreLineOfSight().ignoreInvisibilityTesting();
        private static final int ALERT_RANGE_Y = 10;
        private boolean alertSameType;
        private int timestamp;
        private final Class<?>[] toIgnoreDamage;
        @Nullable
        private Class<?>[] toIgnoreAlert;

        public HurtByTargetGoal(Slime mob, Class<?>... toIgnoreDamage) {
            super(mob, true);
            this.toIgnoreDamage = toIgnoreDamage;
            this.setFlags(EnumSet.of(Flag.TARGET));
        }

        public boolean canUse() {
            int i = this.mob.getLastHurtByMobTimestamp();
            LivingEntity livingentity = this.mob.getLastHurtByMob();
            if (i != this.timestamp && livingentity != null) {
                if (livingentity.getType() == EntityType.PLAYER && this.mob.level().getGameRules().getBoolean(GameRules.RULE_UNIVERSAL_ANGER)) {
                    return false;
                } else {
                    Class[] var3 = this.toIgnoreDamage;
                    int var4 = var3.length;

                    for(int var5 = 0; var5 < var4; ++var5) {
                        Class<?> oclass = var3[var5];
                        if (oclass.isAssignableFrom(livingentity.getClass())) {
                            return false;
                        }
                    }

                    return this.canAttack(livingentity, HURT_BY_TARGETING);
                }
            } else {
                return false;
            }
        }

        public HurtByTargetGoal setAlertOthers(Class<?>... reinforcementTypes) {
            this.alertSameType = true;
            this.toIgnoreAlert = reinforcementTypes;
            return this;
        }

        public void start() {
            this.mob.setTarget(this.mob.getLastHurtByMob());
            this.targetMob = this.mob.getTarget();
            this.timestamp = this.mob.getLastHurtByMobTimestamp();
            this.unseenMemoryTicks = 300;
            if (this.alertSameType) {
                this.alertOthers();
            }

            super.start();
        }

        protected void alertOthers() {
            double d0 = this.getFollowDistance();
            AABB aabb = AABB.unitCubeFromLowerCorner(this.mob.position()).inflate(d0, 10.0, d0);
            List<? extends Mob> list = this.mob.level().getEntitiesOfClass(this.mob.getClass(), aabb, EntitySelector.NO_SPECTATORS);
            Iterator iterator = list.iterator();

            while(true) {
                Mob mob;
                boolean flag;
                do {
                    do {
                        do {
                            do {
                                do {
                                    if (!iterator.hasNext()) {
                                        return;
                                    }

                                    mob = (Mob)iterator.next();
                                } while(this.mob == mob);
                            } while(mob.getTarget() != null);
                        } while(this.mob instanceof TamableAnimal && ((TamableAnimal)this.mob).getOwner() != ((TamableAnimal)mob).getOwner());
                    } while(mob.isAlliedTo(this.mob.getLastHurtByMob()));

                    if (this.toIgnoreAlert == null) {
                        break;
                    }

                    flag = false;
                    Class[] var8 = this.toIgnoreAlert;
                    int var9 = var8.length;

                    for(int var10 = 0; var10 < var9; ++var10) {
                        Class<?> oclass = var8[var10];
                        if (mob.getClass() == oclass) {
                            flag = true;
                            break;
                        }
                    }
                } while(flag);

                this.alertOther(mob, this.mob.getLastHurtByMob());
            }
        }

        protected void alertOther(Mob mob, LivingEntity target) {
            mob.setTarget(target);
        }
    }
}
