package org.confluence.terraentity.mixin.client;


import net.minecraft.server.level.ChunkMap;
import net.minecraft.server.level.GenerationChunkHolder;
import net.minecraft.world.entity.Entity;
import org.confluence.terraentity.entity.boss.wallofflesh.WallOfFlesh;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin({ChunkMap.class})
public abstract class ServerChunkLoadingManagerMixin {

    public ServerChunkLoadingManagerMixin() {
    }
    //控制肉山的渲染距离
    @ModifyVariable(
            method = "addEntity",
            at = @At("STORE"),
            ordinal = 0
    )
    private int replaceDistance(int distance, Entity entity) {
        if (entity instanceof WallOfFlesh wallOfFlesh && entity.isAlive()) {
            int bonus = 1000;
            return (int) (wallOfFlesh.getGridSizeX() *
                    wallOfFlesh.getGridSizeY() *
                    wallOfFlesh.gridSpacing) + bonus;
        }
        return distance;
    }

}
