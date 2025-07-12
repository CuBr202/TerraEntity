package org.confluence.terraentity.entity.util;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LightLayer;

public class SpawnPlacementChecks {

    public static boolean checkFlyingFishSpawn(EntityType<? extends Mob> type, LevelAccessor pLevel, MobSpawnType pSpawnType, BlockPos pPos, RandomSource pRandom) {
        if (!(pLevel instanceof Level level)) {
            return false; // 如果 pLevel 不是 Level 的实例，返回 false
        }

        if (!Mob.checkMobSpawnRules(type, pLevel, pSpawnType, pPos, pRandom)) {
            return false;
        }

        // 判断是否下雨
        if (!level.isRaining()) {
            return false;
        }

        int y = pPos.getY();
        if (y < 60 || y >= 260) {
            return false; // 只能生成在 y = 60 到 y = 260 之间
        }

        return true;
    }

    public static boolean checkGroundSpawn(EntityType<? extends Mob> type, LevelAccessor pLevel, MobSpawnType pSpawnType, BlockPos pPos, RandomSource pRandom) {
        if (!(pLevel instanceof Level level)) {
            return false; // 如果 pLevel 不是 Level 的实例，返回 false
        }

        if (!Mob.checkMobSpawnRules(type, pLevel, pSpawnType, pPos, pRandom)) {
            return false;
        }

        int y = pPos.getY();
        if (y < 60 || y >= 260) {
            return false; // 只能生成在 y = 60 到 y = 260 之间
        }

        return true;
    }

    public static boolean checkRoutineMonsterSpawn(EntityType<? extends Mob> type, LevelAccessor pLevel, MobSpawnType pSpawnType, BlockPos pPos, RandomSource pRandom) {
        if (!(pLevel instanceof Level level)) {
            return false; // 如果 pLevel 不是 Level 的实例，返回 false
        }

        if (!Mob.checkMobSpawnRules(type, pLevel, pSpawnType, pPos, pRandom)) {
            return false;
        }

        int y = pPos.getY();
        if (y >= 260) {
            return false; // 不能生成在 y = 260 或更高的位置
        }

        return true;
    }

    public static boolean checkOnlyDayMonsterSpawn(EntityType<? extends Mob> type, LevelAccessor pLevel, MobSpawnType pSpawnType, BlockPos pPos, RandomSource pRandom) {
        if (!(pLevel instanceof Level level)) {
            return false; // 如果 pLevel 不是 Level 的实例，返回 false
        }

        if (!Mob.checkMobSpawnRules(type, pLevel, pSpawnType, pPos, pRandom)) {
            return false;
        }

        int y = pPos.getY();
        if (y >= 260) {
            return false; // 不能生成在 y = 260 或更高的位置
        }

        return level.isDay();
    }

    public static boolean checkNormalAnimalSpawn(EntityType<? extends Mob> type, LevelAccessor pLevel, MobSpawnType pSpawnType, BlockPos pPos, RandomSource pRandom) {
        if (!(pLevel instanceof Level level) || pPos == null) {
            return false;
        }

        if (!Mob.checkMobSpawnRules(type, pLevel, pSpawnType, pPos, pRandom)) {
            return false;
        }

        int y = pPos.getY();
        if (y >= 260) {
            return false;
        }

        return level.isDay()
                && pLevel.canSeeSky(pPos)
                && pLevel.getBrightness(LightLayer.SKY, pPos) > 8;
    }


    public static boolean checkUndergroundMonsterSpawn(EntityType<? extends Mob> type, LevelAccessor pLevel, MobSpawnType pSpawnType, BlockPos pPos, RandomSource pRandom) {
        if (!(pLevel instanceof Level level)) {
            return false; // 如果 pLevel 不是 Level 的实例，返回 false
        }

        if (!Mob.checkMobSpawnRules(type, pLevel, pSpawnType, pPos, pRandom)) {
            return false;
        }

        int y = pPos.getY();
        if (y < -55 || y > 30) {
            return false; // 只能生成在 y = -55 到 y = 30 之间
        }

        return true;
    }

    public static boolean checkCaveMonsterSpawn(EntityType<? extends Mob> type, LevelAccessor pLevel, MobSpawnType pSpawnType, BlockPos pPos, RandomSource pRandom) {
        if (!(pLevel instanceof Level level)) {
            return false; // 如果 pLevel 不是 Level 的实例，返回 false
        }

        if (!Mob.checkMobSpawnRules(type, pLevel, pSpawnType, pPos, pRandom)) {
            return false;
        }

        int y = pPos.getY();
        if (y < -55 || y > 0) {
            return false; // 只能生成在 y = -55 到 y = 0 之间
        }

        return true;
    }

    public static boolean checkDungeonMonsterSpawn(EntityType<? extends Mob> type, LevelAccessor pLevel, MobSpawnType pSpawnType, BlockPos pPos, RandomSource pRandom) {
        if (!(pLevel instanceof Level level)) {
            return false; // 如果 pLevel 不是 Level 的实例，返回 false
        }

        if (!Mob.checkMobSpawnRules(type, pLevel, pSpawnType, pPos, pRandom)) {
            return false;
        }

        int y = pPos.getY();
        if (y < -35 || y > 40) {
            return false; // 只能生成在 y = -35 到 y = 40 之间
        }

        return true;
    }

    public static boolean checkHighLevelMonsterSpawn(EntityType<? extends Mob> type, LevelAccessor pLevel, MobSpawnType pSpawnType, BlockPos pPos, RandomSource pRandom) {
        if (!(pLevel instanceof Level level)) {
            return false; // 如果 pLevel 不是 Level 的实例，返回 false
        }

        if (!Mob.checkMobSpawnRules(type, pLevel, pSpawnType, pPos, pRandom)) {
            return false;
        }

        int y = pPos.getY();
        if (y < 280 || y > 320) {
            return false; // 只能生成在 y = 280 到 y = 320 之间
        }

        return true;
    }

    public static boolean checkNetherMonsterSpawn(EntityType<? extends Mob> type, LevelAccessor pLevel, MobSpawnType pSpawnType, BlockPos pPos, RandomSource pRandom) {
        if (!(pLevel instanceof Level level)) {
            return false; // 如果 pLevel 不是 Level 的实例，返回 false
        }

        if (!Mob.checkMobSpawnRules(type, pLevel, pSpawnType, pPos, pRandom)) {
            return false;
        }

        int y = pPos.getY();
        if (y < 30 || y > 100) {
            return false; // 只能生成在 y = 30 到 y = 100 之间
        }

        return true;
    }
}
