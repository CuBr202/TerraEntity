package org.confluence.terraentity.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.confluence.terraentity.config.ServerConfig;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.entity.ai.Boss;
import org.confluence.terraentity.entity.boss.AbstractTerraBossBase;

import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import static net.minecraft.world.item.Item.getPlayerPOVHitResult;


public final class TEUtils {
    public static float nextFloat(RandomSource randomSource, float origin, float bound) {
        if (origin >= bound) {
            throw new IllegalArgumentException("bound - origin is non positive");
        } else {
            return origin + randomSource.nextFloat() * (bound - origin);
        }
    }

    public static double nextDouble(RandomSource randomSource, double origin, double bound) {
        if (origin >= bound) {
            throw new IllegalArgumentException("bound - origin is non positive");
        } else {
            return origin + randomSource.nextDouble() * (bound - origin);
        }
    }


    @SuppressWarnings("unchecked")
    public static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> getTicker(BlockEntityType<A> a, BlockEntityType<E> b, BlockEntityTicker<? super E> ticker) {
        return a == b ? (BlockEntityTicker<A>) ticker : null;
    }

    public static boolean isHalloween() {
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH);
        int date = calendar.get(Calendar.DATE);
        return (month == Calendar.OCTOBER && date >= 10) || // 从 10月10日
                (month == Calendar.NOVEMBER && date == 1);  // 到 11月01日
    }

    /**
     * 把向量转成角度
     */
    public static float[] dirToRot(Vec3 vec) {
        double x = vec.x;
        double y = vec.y;
        double z = vec.z;

        double yaw = Math.toDegrees(Mth.atan2(-x, z));
        double pitch = Math.toDegrees(Mth.atan2(-y, Math.sqrt(x * x + z * z)));

        return new float[]{(float) yaw, (float) pitch};
    }

    /**
     * 把角度转成向量
     *
     * @param yaw   角度的yaw，单位为角度而非弧度
     * @param pitch 角度的pitch，单位为角度
     * @return 返回朝向对应角度（yaw、pitch）的单位向量
     */
    public static Vec3 rotToDir(float yaw, float pitch) {
        float yawRad = (float) Math.toRadians(yaw);
        float pitchRad = (float) Math.toRadians(pitch);
        // Mth类的三角函数优化较好
        double y = -1 * Mth.sin(pitchRad);
        double div = Mth.cos(pitchRad);
        double x = -1 * Mth.sin(yawRad);
        double z = Mth.cos(yawRad);
        x *= div;
        z *= div;
        return new Vec3(x, y, z); // Vec3.directionFromRotation(pitch, yaw);
    }

    /**
     * 更新实体朝向
     */
    public static void updateEntityRotation(Entity entity, Vec3 dir) {
        float[] angle = dirToRot(dir);
        entity.setYRot(angle[0]);
        entity.setXRot(angle[1]);
    }

    /**
     * 获得两个位置之间的方向向量；若两点重合则默认返回向上的向量
     * 若要自定义默认返回的向量，请在length后传入一个默认向量
     *
     * @param start  开始位置的位置向量
     * @param end    结束位置的位置向量
     * @param length 返回向量的长度
     */
    public static Vec3 getDirection(Vec3 start, Vec3 end, double length) {
        return getDirection(start, end, length, new Vec3(0, length, 0));
    }

    /**
     * 获得两个位置之间的方向向量；若两点重合则默认返回的向量
     *
     * @param start      开始位置的位置向量
     * @param end        结束位置的位置向量
     * @param length     返回向量的长度
     * @param defaultVec 两点重合时返回的默认向量（注：直接原样返回，不会判定该向量的长度）
     */
    public static Vec3 getDirection(Vec3 start, Vec3 end, double length, Vec3 defaultVec) {
        return getDirection(start, end, length, defaultVec, false);
    }

    /**
     * 获得两个位置之间的方向向量
     * 若preserveShorterVectors为true且两点之间的距离小于length则不会改变向量长度
     *
     * @param start                  开始位置的位置向量
     * @param end                    结束位置的位置向量
     * @param length                 返回向量的长度
     * @param defaultVec             两点重合时返回的默认向量（注：直接原样返回，不会判定该向量的长度）
     * @param preserveShorterVectors 若向量比length短，是否保留原向量
     */
    public static Vec3 getDirection(Vec3 start, Vec3 end, double length,
                                    Vec3 defaultVec, boolean preserveShorterVectors) {
        Vec3 result = end.subtract(start);
        double distSqr = result.lengthSqr();
        // 此时直接返回比length更短的向量
        if (preserveShorterVectors && distSqr <= length * length) {
            return result;
        }
        // 向量长度重设为length

        // 两点之间过近
        if (distSqr < 1e-9) {
            return defaultVec;
        }
        result.scale(length / Math.sqrt(distSqr));
        return result;
    }


    public static void testMessage(Player player, String msg) {
        player.sendSystemMessage(Component.literal(msg));
    }

    public static void testMessage(Level level, String msg) {
        for (Player ply : level.players())
            ply.sendSystemMessage(Component.literal(msg));
    }

    /**
     * 为专家?在处理if...else if时应先使用isMaster
     */
    public static boolean isAtLeastExpert(Level level) {
        return level.getDifficulty().getId() >= Difficulty.NORMAL.getId();
    }

    /**
     * 为大师?在处理if...else if时应先使用此方法
     */
    public static boolean isMaster(Level level) {
        return level.getDifficulty() == Difficulty.HARD;
    }

    /**
     * 根据游戏难度选择值
     *
     * @param classic 经典难度的值
     * @param expert  专家难度的值
     * @param master  大师难度的值
     * @return 选择到的值
     */
    public static <T> T switchByDifficulty(Level level, T classic, T expert, T master) {
        return switch (level.getDifficulty()) {
            case PEACEFUL, EASY -> classic;
            case NORMAL -> expert;
            case HARD -> master;
        };
    }

    /**
     * 获取当前难度的不同属性加成倍率
     * @return 倍率
     */
    public static float getMultiple(Level level, Holder<Attribute> attribute) {
        if(attribute == Attributes.MAX_HEALTH)
            return switchByDifficulty(level, 1f, 1.5f, 2f);
        else if(attribute == Attributes.ATTACK_DAMAGE)
            return switchByDifficulty(level, 1f, 1.5f, 2f);
        else return 1f;
    }

    static ResourceLocation healthKey = TerraEntity.space("server_modifier_max_health");
    static ResourceLocation damageKey = TerraEntity.space("server_modifier_max_attack_damage");
    static ResourceLocation difficultyHealthKey = TerraEntity.space("difficulty_modifier_max_health");
    static ResourceLocation difficultyDamageKey = TerraEntity.space("difficulty_modifier_attack_damage");

    public static void multiplePlayerEnhance(LivingEntity entity, boolean dirty) {
        if(!entity.level().isClientSide) {
            float multiplier = getMultiple(entity.level(), Attributes.MAX_HEALTH);
            if (dirty) {
                int size = Math.min(entity.level().players().size(), 8);
                if (!entity.getAttribute(Attributes.MAX_HEALTH).hasModifier(difficultyHealthKey))
                    entity.getAttribute(Attributes.MAX_HEALTH).addPermanentModifier(new AttributeModifier(difficultyHealthKey, multiplier * size - 1, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
                if (!entity.getAttribute(Attributes.MAX_HEALTH).hasModifier(healthKey))
                    entity.getAttribute(Attributes.MAX_HEALTH).addPermanentModifier(new AttributeModifier(healthKey, ServerConfig.BOSS_ATTRIBUTES_MULTIPLIER_HEALTH.get() - 1, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
                entity.setHealth(entity.getMaxHealth());
            }
            if(!entity.getAttribute(Attributes.ATTACK_DAMAGE).hasModifier(difficultyDamageKey))
                entity.getAttribute(Attributes.ATTACK_DAMAGE).addTransientModifier(new AttributeModifier(difficultyDamageKey, multiplier - 1, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
            if(!entity.getAttribute(Attributes.ATTACK_DAMAGE).hasModifier(damageKey))
                entity.getAttribute(Attributes.ATTACK_DAMAGE).addTransientModifier(new AttributeModifier(damageKey, ServerConfig.BOSS_ATTRIBUTES_MULTIPLIER_DAMAGE.get() - 1, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
        }
    }

    public static void monsterEnhance(LivingEntity entity) {
        if(entity instanceof Boss || entity instanceof AbstractTerraBossBase<?>) return;
        if(!ServerConfig.ENHANCE_ALL_MONSTER.get() && !BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType()).getNamespace().equals(TerraEntity.MODID)) return;
        if(!entity.level().isClientSide) {
            float multiplier = getMultiple(entity.level(), Attributes.MAX_HEALTH);
            if(!entity.getAttribute(Attributes.MAX_HEALTH).hasModifier(healthKey)){
                entity.getAttribute(Attributes.MAX_HEALTH).addPermanentModifier(new AttributeModifier(healthKey, ServerConfig.MONSTER_ATTRIBUTES_MULTIPLIER_HEALTH.get() - 1, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
                entity.setHealth(entity.getMaxHealth());
            }
            if(!entity.getAttribute(Attributes.ATTACK_DAMAGE).hasModifier(damageKey))
                entity.getAttribute(Attributes.ATTACK_DAMAGE).addTransientModifier(new AttributeModifier(damageKey, ServerConfig.BOSS_ATTRIBUTES_MULTIPLIER_DAMAGE.get() - 1, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
            if(!entity.getAttribute(Attributes.ATTACK_DAMAGE).hasModifier(difficultyDamageKey))
                entity.getAttribute(Attributes.ATTACK_DAMAGE).addTransientModifier(new AttributeModifier(difficultyDamageKey, multiplier - 1, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
        }
    }

    /**
     * 获得从实体A到实体B的单位向量，即A→B
     *
     * @param a 实体A
     * @param b 实体B
     * @return A→B的单位向量
     */
    public static Vec3 getVectorA2B(Entity a, Entity b) {
        return b.position().subtract(a.position()).normalize();
    }

    /**
     * 给予实体B一个击退动量，方向为A→B
     *
     * @param a       实体A
     * @param b       实体B
     * @param scale   击退动量的缩放
     * @param motionY 击退的Y轴动量
     */
    public static void knockBackA2B(Entity a, Entity b, double scale, double motionY) {
        if (b instanceof LivingEntity living) {
            AttributeInstance instance = living.getAttribute(Attributes.KNOCKBACK_RESISTANCE);
            if (instance != null) scale *= (1.0 - instance.getValue());
        }
        if (scale > 0.0) {
            if (a instanceof LivingEntity living) {
                AttributeInstance instance = living.getAttribute(Attributes.ATTACK_KNOCKBACK);
                if (instance != null) scale *= (1.0 + instance.getValue());
            }
            b.addDeltaMovement(getVectorA2B(a, b).scale(scale).add(0.0, motionY, 0.0));
        }
    }

    public static Vec3 componentMin(Vec3 vec1, Vec3 vec2) {
        return new Vec3(Math.min(vec1.x, vec2.x), Math.min(vec1.y, vec2.y), Math.min(vec1.z, vec2.z));
    }

    public static Vec3 componentMax(Vec3 vec1, Vec3 vec2) {
        return new Vec3(Math.max(vec1.x, vec2.x), Math.max(vec1.y, vec2.y), Math.max(vec1.z, vec2.z));
    }


    /**
     * 将输入的向量的某个轴乘一个缩放
     *
     * @param vec3  输入的向量
     * @param axis  某个轴
     * @param scale 缩放
     * @return 新向量
     */
    public static Vec3 relativeScale(Vec3 vec3, Direction.Axis axis, double scale) {
        double x = axis == Direction.Axis.X ? scale * vec3.x : vec3.x;
        double y = axis == Direction.Axis.Y ? scale * vec3.y : vec3.y;
        double z = axis == Direction.Axis.Z ? scale * vec3.z : vec3.z;
        return new Vec3(x, y, z);
    }

    /**
     * 计算向量夹角
     * @param v1
     * @param v2
     * @return degree
     */
    public static double angleBetween(Vec3 v1,Vec3 v2){
        return Math.acos(v1.dot(v2)/v1.length()/v2.length());
    }

    public static List<? extends Entity> getNearbyEntities(double radius, Level level,
                                                           Class<? extends Entity> entity, AABB box) {
        return level.getEntitiesOfClass(entity, box.inflate(radius));
    }

    public static Vec3 sphere(float r, float theta, float beta){
        double x = r * Math.sin(theta) * Math.cos(beta);
        double y = r * Math.sin(theta) * Math.sin(beta);
        double z = r * Math.cos(theta);
        return new Vec3(x, y, z);
    }

    /**
     * 根据权重随机获取物品
     */
    public static <T> T getRandomByWeight(Map<T, Float> map) {
        // 计算总权重
        float totalWeight = 0.0f;

        for (var pair : map.values()) {
            totalWeight += pair;
        }

        if (totalWeight == 0.0f) {
            throw new IllegalArgumentException("Total weight cannot be zero.");
        }

        float randomValue = ThreadLocalRandom.current().nextFloat(0, totalWeight);

        // 遍历物品，累积权重，直到累积权重超过随机数
        float cumulativeWeight = 0.0f;
        for (var entry : map.entrySet()) {
            cumulativeWeight += entry.getValue();
            if (cumulativeWeight >= randomValue) {
                return entry.getKey();
            }
        }
        // 理论上不会走到这里
        throw new IllegalStateException("Failed to find random item.");
    }

    /**
     * 获取玩家视角下距离指定距离的实体
     * @param player
     * @param distance
     * @return
     */
    public static EntityHitResult getEyeTraceHitResult(Player player, double distance){
        AABB aabb = player.getBoundingBox().inflate(distance);
        Vec3 from = player.getEyePosition();
        Vec3 to = player.getEyePosition().add(player.getLookAngle().scale(distance));
        return ProjectileUtil.getEntityHitResult(player.level(), player, from, to, aabb, e-> true, 0.1F);
    }

    /**
     * 获取玩家视角下方块
     * @param player
     * @return
     */
    public static BlockPos getEyeBlockHitResult(Player player){
        final BlockHitResult result = getPlayerPOVHitResult(player.level(), player, ClipContext.Fluid.SOURCE_ONLY);
        final BlockHitResult raytraceResult = result.withPosition(result.getBlockPos().above());
        final BlockPos pos = raytraceResult.getBlockPos();
        return pos;
    }

/*
    public static boolean hasBoss(double radius, Level level,
                                  AABB box){
        boolean flag = false;
        for (Entity entity : getNearbyEntities(radius, level, Entity.class, box)) {
            if (entity instanceof Boss) {
                flag = true;
                break;
            }
        }
        return flag;
    }
*//*
    public static int getRespawnWaitTime(LocalPlayer player) {
        boolean hasBoss = hasBoss(Short.MAX_VALUE, player.level(),
                player.getBoundingBox());
        if (hasBoss) {
            return player.getRandom().nextInt(ModConfigs.BOSS_RESPAWN_TIME_MIN.getPrefab()
                    , ModConfigs.BOSS_RESPAWN_TIME_MAX.getPrefab());
        } else {
            return player.getRandom().nextInt(ModConfigs.DEFAULT_RESPAWN_TIME_MIN.getPrefab(),
                    ModConfigs.DEFAULT_RESPAWN_TIME_MAX.getPrefab());
        }
    }*/


}
