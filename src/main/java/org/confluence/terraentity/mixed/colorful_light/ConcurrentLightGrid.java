package org.confluence.terraentity.mixed.colorful_light;

import net.minecraft.world.phys.Vec3;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.ReentrantLock;


// 线程安全的光源管理器
public class ConcurrentLightGrid {
    private static final int GRID_SIZE = 16; // 网格尺寸
    private final ConcurrentHashMap<GridKey, ConcurrentLinkedQueue<LightSource>> gridMap = new ConcurrentHashMap<>();
    private final ReentrantLock updateLock = new ReentrantLock(); // 批量更新锁

    // 网格键（线程安全且不可变）
    private static class GridKey {
        final int gx, gy, gz;

        GridKey(int gx, int gy, int gz) {
            this.gx = gx;
            this.gy = gy;
            this.gz = gz;
        }

        @Override
        public int hashCode() {
            return Objects.hash(gx, gy, gz);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            GridKey gridKey = (GridKey) o;
            return gx == gridKey.gx && gy == gridKey.gy && gz == gridKey.gz;
        }
    }

    // 添加光源
    public void addLight(LightSource light) {
        GridKey key = calculateGridKey(light.getPosition());
        gridMap.computeIfAbsent(key, k -> new ConcurrentLinkedQueue<>())
               .add(light);
    }

    // 检查光源是否存在
    public boolean contains(LightSource light) {
        // 遍历所有网格中的光源队列
        for (ConcurrentLinkedQueue<LightSource> queue : gridMap.values()) {
            // 使用队列的contains方法检查存在性
            if (queue.contains(light)) {
                return true;
            }
        }
        return false;
    }

    // 检查光源是否存在
    public boolean contains(LightPos light) {
        // 遍历所有网格中的光源队列
        for (ConcurrentLinkedQueue<LightSource> queue : gridMap.values()) {
            // 使用队列的contains方法检查存在性
            if (queue.stream().anyMatch(l -> l.getPosition().equals(light))) {
                return true;
            }
        }
        return false;
    }

    // 移除光源
    public boolean removeLight(LightSource light) {
        GridKey key = calculateGridKey(light.getPosition());
        ConcurrentLinkedQueue<LightSource> queue = gridMap.get(key);
        if (queue != null) {
            boolean removed = queue.remove(light);
            if (queue.isEmpty()) gridMap.remove(key);
            return removed;
        }
        return false;
    }

    // 移除光源
    public boolean removeLight(LightPos light) {
        GridKey key = calculateGridKey(light);
        ConcurrentLinkedQueue<LightSource> queue = gridMap.get(key);
        if (queue != null) {
            boolean removed = queue.removeIf(l -> l.getPosition().equals(light));
            if (queue.isEmpty()) gridMap.remove(key);
            return removed;
        }
        return false;
    }

    // 批量更新光源
    public void updateLights(Runnable updateTask) {
        updateLock.lock();
        try {
            updateTask.run();
        } finally {
            updateLock.unlock();
        }
    }

    // 获取附近光源
    public List<LightSource> getNearbyLights(LightPos target, float maxDistanceSq) {
        List<LightSource> results = new ArrayList<>();
        int radius = (int) Math.ceil(Math.sqrt(maxDistanceSq));

        // 计算覆盖的网格范围
        int minX = target.x() - radius;
        int maxX = target.x() + radius;
        int minY = target.y() - radius;
        int maxY = target.y() + radius;
        int minZ = target.z() - radius;
        int maxZ = target.z() + radius;

        // 遍历所有可能覆盖的网格
        for (int gx = (minX - GRID_SIZE) / GRID_SIZE; gx <= (maxX + GRID_SIZE) / GRID_SIZE; gx++) {
            for (int gy = (minY - GRID_SIZE) / GRID_SIZE; gy <= (maxY + GRID_SIZE) / GRID_SIZE; gy++) {
                for (int gz = (minZ - GRID_SIZE) / GRID_SIZE; gz <= (maxZ + GRID_SIZE) / GRID_SIZE; gz++) {
                    GridKey key = new GridKey(gx, gy, gz);
                    ConcurrentLinkedQueue<LightSource> queue = gridMap.get(key);
                    if (queue != null) {
                        for (LightSource light : queue) {
                            if (target.distanceToSq(light.getPosition()) <= maxDistanceSq) {
                                results.add(light);
                            }
                        }
                    }
                }
            }
        }
        return results;
    }

    public List<LightSource> getNearbyLights(Vec3 target, float maxDistanceSq) {
        List<LightSource> results = new ArrayList<>();
        int radius = (int) Math.ceil(Math.sqrt(maxDistanceSq));

        // 计算覆盖的网格范围
        int minX = (int) (target.x() - radius);
        int maxX = (int) target.x() + radius;
        int minY = (int) target.y() - radius;
        int maxY = (int) target.y() + radius;
        int minZ = (int) target.z() - radius;
        int maxZ = (int) target.z() + radius;

        // 遍历所有可能覆盖的网格
        for (int gx = (minX - GRID_SIZE) / GRID_SIZE; gx <= (maxX + GRID_SIZE) / GRID_SIZE; gx++) {
            for (int gy = (minY - GRID_SIZE) / GRID_SIZE; gy <= (maxY + GRID_SIZE) / GRID_SIZE; gy++) {
                for (int gz = (minZ - GRID_SIZE) / GRID_SIZE; gz <= (maxZ + GRID_SIZE) / GRID_SIZE; gz++) {
                    GridKey key = new GridKey(gx, gy, gz);
                    ConcurrentLinkedQueue<LightSource> queue = gridMap.get(key);
                    if (queue != null) {
                        for (LightSource light : queue) {
                            LightPos pos = light.getPosition();
                            if (target.distanceToSqr(pos.x(), pos.y(), pos.z()) <= maxDistanceSq) {
                                results.add(light);
                            }
                        }
                    }
                }
            }
        }
        return results;
    }
    // 计算网格键
    private GridKey calculateGridKey(LightPos pos) {
        int gx = pos.x() >> 4;
        int gy = pos.y() >> 4;
        int gz = pos.z() >> 4;
        return new GridKey(gx, gy, gz);
    }

}