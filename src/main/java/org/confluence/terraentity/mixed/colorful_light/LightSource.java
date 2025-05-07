package org.confluence.terraentity.mixed.colorful_light;

import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

// 光源接口
public interface LightSource {

    LightPos getPosition();

    Light getLight();

    void mixColor(Vector3f color, Vec3 vertexPos);

    default float getRadius(){
        return getLight().radius();
    }

}
