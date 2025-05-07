package org.confluence.terraentity.mixed.colorful_light;

import net.minecraft.world.phys.Vec3;
import net.neoforged.fml.ModList;
import org.joml.Vector3f;

public class PointLight implements LightSource {
    LightPos position;
    Light light;

    public PointLight(LightPos position, Light light) {
        this.position = position;
        this.light = light;
    }

    @Override
    public LightPos getPosition() {
        return position;
    }

    @Override
    public Light getLight() {
        return light;
    }

    @Override
    public void mixColor(Vector3f color, Vec3 vertexPos){
        LightPos lightPos = getPosition();
        Light light = getLight();
        float distanceToLight = (float) vertexPos.distanceToSqr(lightPos.x(), lightPos.y(), lightPos.z());
        float radiusSqr = light.radius() * light.radius();
        if (distanceToLight < radiusSqr) {
            double attenuation = getAttenuation(distanceToLight, radiusSqr);
//                attenuation = Math.sqrt(attenuation);
            color.add((float) (light.r() * attenuation), (float)(light.g() * attenuation), (float)(light.b() * attenuation));
        }
    }

    double getAttenuation(float distanceSqr, float radiusSqr){
        float f = (distanceSqr * 2 / radiusSqr + 0.1f);
        return 1.0f / (f * f * getMagicNumber()) ;
    }

    float getMagicNumber(){
        if(ModList.get().isLoaded("sodium")){
            return 10;
        }
        return 10;
    }
}
