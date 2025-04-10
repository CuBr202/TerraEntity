package org.confluence.terraentity.entity.npc;

import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import org.apache.commons.lang3.mutable.MutableLong;

/**
 * 自动设置房屋
 */
public class NPCHouseBehaviors {
    public static int Detect_Interval = 100;

    public static BehaviorControl<AbstractTerraNPC> walkToHouse(float speedModifier) {

        MutableLong mutablelong = new MutableLong(0L);
        return BehaviorBuilder.create((instance) -> instance.group(
                instance.absent(MemoryModuleType.WALK_TARGET),
                instance.present(MemoryModuleType.HOME)
        ).apply(instance, (walk_target, home_pos) -> (serverLevel, entity, l) -> {
            if (serverLevel.getGameTime() - mutablelong.getValue() >= 20L) {
                entity.house = HouseManager.getInstance().getHouse(entity.getUUID());
                if (!entity.house.isEmpty()) {
                    // 如果已经有房屋，则回到房屋
                    GlobalPos globalpos = instance.get(home_pos);
                    walk_target.set(new WalkTarget(globalpos.pos(), speedModifier, 1));
//                    walk_target.set(new WalkTarget(entity.house.center(), speedModifier, 1));
                    HouseManager.getInstance().tryAddHouse(entity.getUUID().toString(),
                            entity.house.min(), entity.house.max(), entity.house.center());
                    // todo 设置为椅子坐标
//                    home_pos.set(GlobalPos.of(serverLevel.dimension(), entity.house.center()));
                    return true;
                }

            }
            return false;
        }));
    }



    public static BehaviorControl<AbstractTerraNPC> FindHouse(MemoryModuleType<GlobalPos> poiPosMemory) {
        return BehaviorBuilder.create((instance) -> instance.group(
                instance.registered(poiPosMemory)
        ).apply(instance, (memoryAccessor) -> (serverLevel, entity, l) -> {

            boolean timeToRefresh = entity.tickCount % Detect_Interval == 0;
            if(timeToRefresh){
                HouseManager.getInstance().removeHouse(entity.getUUID());
            }
            if(entity.house.isEmpty()) {

                // 如果房屋为空，尝试添加房屋
                BlockPos blockpos = entity.blockPosition();

                HouseDetectInfo info = HouseDetectInfo.detect(blockpos, serverLevel);
                if (info.isError()) {
                    // 检测失败
                    entity.setHouse(House.EMPTY);
                    return false;
                }

                House house = new House(entity.getStringUUID(), info.min(), info.max(), blockpos);

                if(HouseManager.getInstance().tryAddHouse(house)){
                    // 成功添加房屋
                    entity.setHouse(house);
                    memoryAccessor.set(GlobalPos.of(serverLevel.dimension(), house.center()));
                    return true;
                }
                // 房屋已存在
                entity.setHouse(House.EMPTY);
                return false;
            }
            // 已有房屋，检查房屋是否合理
            if(HouseManager.getInstance().getHouse(entity.getUUID()) == null){
                entity.setHouse(House.EMPTY);
            }

            return true;

        }));
    }


}
