package org.confluence.terraentity.mixin.sodium;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.caffeinemc.mods.sodium.api.util.ColorARGB;
import net.caffeinemc.mods.sodium.client.render.chunk.compile.pipeline.BlockRenderer;
import net.caffeinemc.mods.sodium.client.render.chunk.terrain.material.Material;
import net.caffeinemc.mods.sodium.client.render.frapi.mesh.MutableQuadViewImpl;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import org.confluence.terraentity.mixed.LightManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;

@Pseudo
@Mixin(targets = "net.caffeinemc.mods.sodium.client.render.chunk.compile.pipeline.BlockRenderer", priority = 1001)
public class BlockRendererMixin {

    @WrapOperation(method = "processQuad", at = @At(value = "INVOKE", target = "Lnet/caffeinemc/mods/sodium/client/render/chunk/compile/pipeline/BlockRenderer;bufferQuad(Lnet/caffeinemc/mods/sodium/client/render/frapi/mesh/MutableQuadViewImpl;[FLnet/caffeinemc/mods/sodium/client/render/chunk/terrain/material/Material;)V"))
    public void bufferQuad(BlockRenderer instance, MutableQuadViewImpl out, float[] dstIndex, Material material, Operation<Void> original) {
        BlockPos pos = ((AbstractBlockRenderContextAccessor) this).getPos();
        LightManager.tempPos.set(pos);
        LightManager.count.set(0);
        original.call(instance, out, dstIndex, material);
        LightManager.tempPos.remove();
    }


    @WrapOperation(method = "bufferQuad", at = @At(value = "INVOKE", target = "Lnet/caffeinemc/mods/sodium/api/util/ColorARGB;toABGR(I)I"))
    public int toABGR(int color, Operation<Integer> original) {

        if(LightManager.tempPos.get() != null) {
            int newColor = LightManager.getVertexLightColor(Direction.UP, LightManager.tempPos.get(), color);
            if (newColor != color) {
                return ColorARGB.toABGR(newColor);
            }
        }
        return ColorARGB.toABGR(color);
    }

}
