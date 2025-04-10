package org.confluence.terraentity.entity.npc;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.TorchBlock;
import org.confluence.lib.util.ComputerUtils;

import java.util.List;

/**
 * 房屋检测信息
 *
 * @param min  左下角
 * @param max  右上角
 * @param list 包含范围
 * @param type 检测信息类型
 */
public record HouseDetectInfo(BlockPos min, BlockPos max, List<BlockPos> list, DetectType type) {
    public static int DetectRange = 20;

    public static HouseDetectInfo error(DetectType type) {
        return new HouseDetectInfo(BlockPos.ZERO, BlockPos.ZERO, List.of(), type);
    }

    public static HouseDetectInfo detect(BlockPos start, Level level){
        // TODO: 房间内可包含方块
        var list = ComputerUtils.zoomDetection(level, start, DetectRange, state->
                state.isAir() || state.getBlock() instanceof TorchBlock
        );

        if(list.isEmpty()){
            return error(DetectType.TO_LARGE);
        }
        // 空间太小
        if(list.size() < 16){
            return error(DetectType.TO_SMALL);
        }
        int minx = Integer.MAX_VALUE;
        int miny = Integer.MAX_VALUE;
        int minz = Integer.MAX_VALUE;
        int maxx = Integer.MIN_VALUE;
        int maxy = Integer.MIN_VALUE;
        int maxz = Integer.MIN_VALUE;

        boolean test1 = false;
        for(var blockPos : list){
            // TODO: 桌子 椅子
            if(level.getBlockState(blockPos).getBlock() instanceof TorchBlock)
                test1 = true;
            minx = Math.min(minx, blockPos.getX());
            miny = Math.min(miny, blockPos.getY());
            minz = Math.min(minz, blockPos.getZ());
            maxx = Math.max(maxx, blockPos.getX());
            maxy = Math.max(maxy, blockPos.getY());
            maxz = Math.max(maxz, blockPos.getZ());
        }
        // 空间xz单个方向最小值
        if(maxx - minx < 4 || maxz - minz < 4){
            return error(DetectType.TO_SMALL);
        }
        if(!test1){
            return error(DetectType.NO_DYNAMIC_LIGHT);
        }
        BlockPos min = new BlockPos(minx, miny, minz);
        BlockPos max = new BlockPos(maxx, maxy, maxz);

        return new HouseDetectInfo(min, max, list, DetectType.FOUND_HOUSE);
    }

    public boolean isError() {
        return type != DetectType.FOUND_HOUSE;
    }

    public enum DetectType {

        TO_LARGE("message.house_detector.to_large"),
        TO_SMALL("message.house_detector.to_small"),
        NO_DYNAMIC_LIGHT("message.house_detector.no_dynamic_light"),
        FOUND_HOUSE("message.house_detector.found_house");

        public final String translationKey;
        DetectType(String translationKey){
            this.translationKey = translationKey;
        }
    }
}
