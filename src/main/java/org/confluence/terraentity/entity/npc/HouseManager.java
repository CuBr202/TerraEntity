package org.confluence.terraentity.entity.npc;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 全局房屋管理器
 */
public record HouseManager(Map<UUID, House> houses) {
    private static HouseManager instance;

    /**
     * 获取房屋管理器单例
     */
    public static HouseManager getInstance() {
        if (instance == null) {
            instance = new HouseManager(new HashMap<>());
        }
        return instance;
    }

    /**
     * 重新加载房屋信息
     * @param houseManager 房屋管理器
     */
    public void load(HouseManager houseManager) {
        houses.clear();
        houses.putAll(houseManager.houses());
    }

    /**
     * 尝试添加房子
     *
     * @param uuid   房子的uuid
     * @param min    左下角的坐标
     * @param max    右上角的坐标
     * @param center 房子的中心坐标
     * @return 如果已经存在，则不添加，返回false；否则添加，返回true
     */
    public boolean tryAddHouse(String uuid, BlockPos min, BlockPos max, BlockPos center) {
        if (houses.containsKey(UUID.fromString(uuid))) {
            return false;
        }
        if (isInsideHouse(center)!=null) {
            return false;
        }
        addHouse(new House(uuid, min, max, center));
        return true;
    }

    /**
     * 尝试添加房子
     *
     * @param house 房子对象
     * @return 如果已经存在，则不添加，返回false；否则添加，返回true
     */
    public boolean tryAddHouse(House house) {
        return tryAddHouse(house.uuid(), house.min(), house.max(), house.center());
    }

    /**
     * 强制添加房子
     * @param house 房子对象
     */
    private void addHouse(House house) {
        houses.put(UUID.fromString(house.uuid()), house);
    }

    /**
     * 获取房屋
     * @param uuid 房子的uuid
     * @return 房子对象
     */
    public House getHouse(UUID uuid) {
        return houses.get(uuid);
    }

    /**
     * 删除房子
     * @param uuid 房子的uuid
     */
    public void removeHouse(UUID uuid) {
        houses.remove(uuid);
    }

    /**
     * 判断pos是否在uuid对应的房子内
     * @param pos 坐标
     * @param uuid 房子的uuid
     * @return 如果在房屋内，返回true，否则返回false
     */
    public boolean isInsideHouse(BlockPos pos, UUID uuid) {
        House house = houses.get(uuid);
        if (house == null) {
            return false;
        }
        return house.contains(pos);
    }

    /**
     * 判断pos是否在任何一个房子内
     * @param pos 坐标
     * @return 如果在房屋内，返回房屋对象，否则返回null
     */
    public House isInsideHouse(BlockPos pos) {
        for (House house : houses.values()) {
            if (house.contains(pos)) {
                return house;
            }
        }
        return null;
    }

    /**
     * 清空房屋管理器
     */
    public void clear() {
        houses.clear();
    }

    public static Codec<HouseManager> CODEC = Codec.unboundedMap(Codec.STRING.xmap(UUID::fromString, UUID::toString), House.CODEC)
            .xmap(HouseManager::new, HouseManager::houses);

}
