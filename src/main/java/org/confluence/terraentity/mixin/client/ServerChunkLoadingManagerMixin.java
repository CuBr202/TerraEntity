package org.confluence.terraentity.mixin.client;


import net.minecraft.server.level.ChunkMap;
import net.minecraft.world.entity.Entity;
import org.confluence.terraentity.entity.boss.wallofflesh.WallOfFlesh;
import org.confluence.terraentity.entity.boss.wallofflesh.WallOfFleshEye;
import org.confluence.terraentity.entity.boss.wallofflesh.WallOfFleshMouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin({ChunkMap.class})
public abstract class ServerChunkLoadingManagerMixin {
//    @Shadow public abstract void releaseGeneration(GenerationChunkHolder chunk);

    public ServerChunkLoadingManagerMixin() {
    }
    //控制肉山的渲染距离
    @ModifyVariable(
            method = "addEntity",
            at = @At("STORE"),
            ordinal = 0
    )
    private int replaceDistance(int distance, Entity entity) {
        if (entity != null && entity.isAlive() && WallOfFlesh.isWallOfFlesh(entity)) {
            WallOfFlesh wallOfFlesh = null;
            int bonus = 750;

            if(entity instanceof WallOfFleshEye eye){
                wallOfFlesh = eye.parentMob;
                bonus = 250;

            }else if(entity instanceof WallOfFleshMouse mouth){
                wallOfFlesh = mouth.parentMob;
                bonus = 250;
            }

            if (wallOfFlesh != null) {
                return (int)(wallOfFlesh.getGridSizeX() *
                        wallOfFlesh.getGridSizeY() *
                        wallOfFlesh.gridSpacing) + bonus;
            }
        }
        return distance;
    }

}
