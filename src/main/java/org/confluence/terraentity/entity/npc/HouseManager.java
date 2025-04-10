package org.confluence.terraentity.entity.npc;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 全局房屋管理器

 */
public class HouseManager {
    private static HouseManager instance;
    private final Map<UUID, House> houses;

    public HouseManager(Map<UUID, House> houses){
        this.houses = houses;
    }

    public static HouseManager getInstance(){
        if(instance == null){
            instance = new HouseManager(new HashMap<>());
        }
        return instance;
    }

    public Map<UUID, House> getHouses(){
        return houses;
    }

    /**
     * 添加房子
     * @param uuid 房子的uuid
     * @param min 左下角的坐标
     * @param max 右上角的坐标
     * @param center 房子的中心坐标
     * @return 是否添加成功
     */
    public boolean tryAddHouse(String uuid, BlockPos min, BlockPos max, BlockPos center){
        if(houses.containsKey(UUID.fromString(uuid))){
            return false;
        }
        if(isInsideHouse(center)){
            return false;
        }
        addHouse(new House(uuid, min, max, center));
        return true;
    }

    public boolean tryAddHouse(House house){
        return tryAddHouse(house.uuid(), house.min(), house.max(), house.center());
    }

    private void addHouse(House house){
        houses.put(UUID.fromString(house.uuid()), house);
    }

    public House getHouse(UUID uuid){
        return houses.get(uuid);
    }

    public void removeHouse(UUID uuid){
        houses.remove(uuid);
    }

    public boolean isInsideHouse(BlockPos pos, UUID uuid){
        House house = houses.get(uuid);
        if(house == null){
            return false;
        }
        return house.contains(pos);
    }

    public boolean isInsideHouse(BlockPos pos){
        for(House house : houses.values()){
            if(house.contains(pos)){
                return true;
            }
        }
        return false;
    }

    public void clear(){
        houses.clear();
    }

    public static Codec<HouseManager> CODEC = Codec.unboundedMap(Codec.STRING.xmap(UUID::fromString, UUID::toString), House.CODEC)
            .xmap(HouseManager::new, HouseManager::getHouses);

}
