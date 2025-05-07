package org.confluence.terraentity.mixed.colorful_light;

import net.minecraft.core.BlockPos;

public record LightPos(int x, int y, int z) {
    public static LightPos of(BlockPos pos) {
        return new LightPos(pos.getX(), pos.getY(), pos.getZ());
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LightPos lightPos = (LightPos) o;
        return x == lightPos.x && y == lightPos.y && z == lightPos.z;
    }

    public float distanceToSq(LightPos other) {
        int dx = x - other.x;
        int dy = y - other.y;
        int dz = z - other.z;
        return dx * dx + dy * dy + dz * dz;
    }
}
