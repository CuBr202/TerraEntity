package org.confluence.terraentity.mixed;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.util.FastColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@OnlyIn(Dist.CLIENT)
public class LightManager {
    // ThreadLocal
    public static final ThreadLocal<BlockPos> tempPos = ThreadLocal.withInitial(() -> null);
    public static final ThreadLocal<Float> r = ThreadLocal.withInitial(() -> 0f);
    public static final ThreadLocal<Float> g = ThreadLocal.withInitial(() -> 0f);
    public static final ThreadLocal<Float> b = ThreadLocal.withInitial(() -> 0f);
    public static final ThreadLocal<BakedQuad> quad = ThreadLocal.withInitial(() -> null);
    public static final ThreadLocal<Integer> count = ThreadLocal.withInitial(() -> 4);

    public static LightManager instance;
    public static LightManager getInstance() {
        if (instance == null) {
            instance = new LightManager();
            instance.init();
        }
        return instance;
    }

    static Light Torch = new Light(1, 1, 0, 12);

    Map<LightPos, Light> lights = new ConcurrentHashMap<>();
    final Map<Block, Light> lightMap = new HashMap<>();
    static float lightDecline = 15f;

    void init(){
        synchronized (lightMap) {
            // for test
            lightMap.put(Blocks.TORCH, Torch);
            lightMap.put(Blocks.SOUL_TORCH, new Light(1,0,1,12));
            lightMap.put(Blocks.REDSTONE_TORCH, new Light(1,0,0,12));

        }
    }

    static void cleanup() {
        tempPos.remove();
        r.remove();
        g.remove();
        b.remove();
        quad.remove();
    }

    public static void processRenderQuadList(VertexConsumer instance, PoseStack.Pose f7, BakedQuad f8, float[] f3, float f4, float f5, float f, float f1, int[] f2, int f33, boolean f44, Operation<Void> original, BlockPos pos) {

        tempPos.set(pos);
        r.set(f4);
        g.set(f5);
        b.set(f);
        quad.set(f8);
        count.set(0);
        instance.putBulkData(f7, f8, f3, f4, f5, f, f1, f2, f33, f44);
        cleanup();

    }

    public static int getVertexLightColor(float r, float g, float b, BakedQuad bakedQuad, BlockPos pos, int originalLight){
        if(pos == null){
            return originalLight;
        }
        int index = count.get();

        Vec3 vertexPos = getVertexPos(bakedQuad, pos, index);

        int ro = originalLight >> 16 & 255;
        int go = originalLight >> 8 & 255;
        int bo = originalLight & 255;

        double brightness = ro / 255f / r;

        Vec3 mixedLight = getInstance().getMixedLightColor(vertexPos);
//        Vec3 mixedLight = new Vec3(1,1,0).scale( 1 / (distance * 0.2F + 1f));
        if(mixedLight.length() < 0.01f){
            return originalLight;
        }
        float additionR = (float) mixedLight.x;
        float additionG = (float) mixedLight.y;
        float additionB = (float) mixedLight.z;

        r = ro / 255f;
        g = go / 255f;
        b = bo / 255f;

        float max1 = Math.max(Math.max(r, g), b);

        r = r + additionR;
        g = g + additionG;
        b = b + additionB;

        float max = Math.max(Math.max(r, g), b);

        Vector3f color1 = new Vector3f(r, g, b).mul(max1/max);
        // 颜色计算逻辑（保持原有实现）
        int color = FastColor.ARGB32.alpha(originalLight) << 24
                | ((int)(color1.x() * 255) << 16
                | (int)(color1.y() * 255) << 8
                | (int)(color1.z() * 255));

        count.set(index + 1);
        return color;
    }

    private static @NotNull Vec3 getVertexPos(BakedQuad bakedQuad, BlockPos pos, int index) {
        Vec3i n = bakedQuad.getDirection().getNormal();

        Vec3 vertexPos = new Vec3(pos.getX() + n.getX() * 0.5f, pos.getY() + n.getY()* 0.5f, pos.getZ() + n.getZ()* 0.5f);

        if(bakedQuad.getDirection() == Direction.UP){
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


    public void addLight(BlockPos pos, Block block) {
        LightPos lightPos = LightPos.of(pos);
        if( lightMap.containsKey(block)) {
            if(!lights.containsKey(lightPos)) {
                lights.put(lightPos, lightMap.get(block));
            }
        }else{
            if(lights.containsKey(lightPos)) {
                removeLight(lightPos);
            }
        }
    }

    void removeLight(LightPos pos) {
        lights.remove(pos);
    }

    // 获取混合光照颜色
    private Vec3 getMixedLightColor(Vec3 vertexPos) {

        Vec3 color = new Vec3(0, 0, 0);
        for (Map.Entry<LightPos, Light> entry : lights.entrySet()) {
            Light light = entry.getValue();
            LightPos lightPos = entry.getKey();
            float distanceToLight = (float) vertexPos.distanceToSqr(lightPos.x, lightPos.y, lightPos.z);
            if (distanceToLight < light.radius * light.radius) {
                double attenuation = 1.0f / (distanceToLight / (light.radius * light.radius) * lightDecline + 1.0f);
//                attenuation = Math.sqrt(attenuation);
                color = color.add(new Vec3(light.r * attenuation, light.g * attenuation, light.b * attenuation));
            }
        }
        return color;
    }


    record LightPos(int x, int y, int z){
        public static LightPos of(BlockPos pos) {
            return new LightPos(pos.getX(), pos.getY(), pos.getZ());
        }
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass()!= o.getClass()) return false;
            LightPos lightPos = (LightPos) o;
            return x == lightPos.x && y == lightPos.y && z == lightPos.z;
        }
    }

    record Light(float r, float g, float b, float radius){
    }

}
