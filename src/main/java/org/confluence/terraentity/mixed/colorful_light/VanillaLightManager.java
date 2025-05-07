package org.confluence.terraentity.mixed.colorful_light;

import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3fc;

public class VanillaLightManager extends LightManager{

    protected void cleanup() {
        tempPos.remove();
    }


    public static @NotNull Vec3 getVertexPos(Vector3fc direction, BlockPos pos, int index) {

        Vec3 vertexPos = new Vec3(pos.getX() + direction.x() * 0.5f, pos.getY() + direction.y()* 0.5f, pos.getZ() + direction.z()* 0.5f);

        if(direction.y() > 0){
            Vec3 offset = switch (index % 4){
                case 2 -> new Vec3(0.5, 0, 0.5);
                case 3 -> new Vec3(0.5, 0, -0.5);
                case 0 -> new Vec3(-0.5, 0, -0.5);
                case 1 -> new Vec3(-0.5, 0, 0.5);
                default -> new Vec3(0, 0, 0);
            };
//            Vec3 offset = switch (index % 4){
//                case 3 -> new Vec3(0.5, 0, 0.5);
//                case 0 -> new Vec3(0.5, 0, -0.5);
//                case 1 -> new Vec3(-0.5, 0, -0.5);
//                case 2 -> new Vec3(-0.5, 0, 0.5);
//                default -> new Vec3(0, 0, 0);
//            };
            vertexPos = vertexPos.add(offset);
        }else{
            Vec3 offset = switch (index % 4){
                case 2 -> new Vec3(0.5, 0, 0.5);
                case 3 -> new Vec3(0.5, 0, -0.5);
                case 0 -> new Vec3(-0.5, 0, -0.5);
                case 1 -> new Vec3(-0.5, 0, 0.5);
                default -> new Vec3(0, 0, 0);
            };
            vertexPos = vertexPos.add(offset);
        }
        return vertexPos;
    }
}
