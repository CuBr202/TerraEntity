package org.confluence.terraentity.mixed.colorful_light;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.util.FastColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.fml.ModList;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;
import org.joml.Vector3fc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@OnlyIn(Dist.CLIENT)
public class LightManager {
    // ThreadLocal
    public static final ThreadLocal<BlockPos> tempPos = ThreadLocal.withInitial(() -> null);
    public static final ThreadLocal<Vector3fc> quadDirectionNormal = ThreadLocal.withInitial(() -> null);
    public static final ThreadLocal<Direction> quadDirection = ThreadLocal.withInitial(() -> null);
    public static final ThreadLocal<Integer> count = ThreadLocal.withInitial(() -> 4);

    private static LightManager instance;
    public static LightManager getInstance() {
        if (instance == null) {
            if(ModList.get().isLoaded("sodium")){
                instance = new VanillaLightManager();
            }else {
                instance = new LightManager();
            }
            instance.init();
        }
        return instance;
    }

    static Light Torch = new Light(1, 1, 0, 12);

    ConcurrentLightGrid lightGrid = new ConcurrentLightGrid();
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

    protected void cleanup() {
        tempPos.remove();
        quadDirection.remove();
    }

    public void processRenderQuadList(Runnable prepare) {
        prepare.run();
        cleanup();
    }

    public int processVertex(Function<Integer, Vec3> getVertexPos, int color, Consumer<Integer> onColorChange, Runnable original){
        if (LightManager.tempPos.get() != null) {
            int index = LightManager.count.get();
            int newColor = LightManager.getInstance().getVertexLightColor(
                    getVertexPos,
                    color, index
            );
            LightManager.count.set(index + 1);
            if (color != newColor) {
                onColorChange.accept(newColor);
                return newColor;
            }
            original.run();
        }
        return color;
    }

    protected int getVertexLightColor(Function<Integer, Vec3> getVertexPos, int originalLight, int index){

//        Vec3 vertexPos = getVertexPos(direction, pos, index);
        Vec3 vertexPos = getVertexPos.apply(index);
        Vector3f mixedLight = getMixedLightColor(vertexPos);

        if(mixedLight.length() < 0.01f){
            return originalLight;
        }

        float r = (originalLight >> 16 & 255) / 255f;
        float g = (originalLight >> 8 & 255) / 255f;
        float b = (originalLight & 255) / 255f;

        float max1 = Math.max(Math.max(r, g), b);

        r = r + mixedLight.x;
        g = g + mixedLight.y;
        b = b + mixedLight.z;

        float max = Math.max(Math.max(r, g), b);

        Vector3f color1 = new Vector3f(r, g, b).mul(max1/max);


        int color = FastColor.ARGB32.alpha(originalLight) << 24
                | ((int)(color1.x() * 255) << 16
                | (int)(color1.y() * 255) << 8
                | (int)(color1.z() * 255));

        return color;
    }

    public static @NotNull Vec3 getVertexPos(Direction direction, BlockPos pos, int index) {
        Vec3i n = direction.getNormal();

        Vec3 vertexPos = new Vec3(pos.getX() + n.getX() * 0.5f, pos.getY() + n.getY()* 0.5f, pos.getZ() + n.getZ()* 0.5f);

        if(direction == Direction.UP){
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


    public void addLight(BlockPos pos, Block block) {
        LightPos lightPos = LightPos.of(pos);
        if( lightMap.containsKey(block)) {
            if(!lightGrid.contains(lightPos)){
                lightGrid.addLight(new PointLight(lightPos, lightMap.get(block)));
            }
        }else{
            if(lightGrid.contains(lightPos)) {
                removeLight(lightPos);
            }
        }
    }

    void removeLight(LightPos pos) {
        lightGrid.removeLight(pos);
    }

    // 获取混合光照颜色
    public Vector3f getMixedLightColor(Vec3 vertexPos) {
        Vector3f color = new Vector3f(0, 0, 0);
        List<LightSource> list = lightGrid.getNearbyLights(vertexPos, 225);
        for (LightSource lightSource : list) {
            lightSource.mixColor(color, vertexPos);
        }
        return color;
    }

}
