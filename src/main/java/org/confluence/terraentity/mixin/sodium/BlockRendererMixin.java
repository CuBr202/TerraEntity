package org.confluence.terraentity.mixin.sodium;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.caffeinemc.mods.sodium.api.util.ColorARGB;
import net.caffeinemc.mods.sodium.client.render.chunk.compile.pipeline.BlockRenderer;
import net.caffeinemc.mods.sodium.client.render.chunk.terrain.material.Material;
import net.caffeinemc.mods.sodium.client.render.frapi.mesh.MutableQuadViewImpl;
import net.minecraft.core.BlockPos;
import org.confluence.terraentity.mixed.colorful_light.LightManager;
import org.confluence.terraentity.mixed.colorful_light.VanillaLightManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;

@Pseudo
@Mixin(targets = "net.caffeinemc.mods.sodium.client.render.chunk.compile.pipeline.BlockRenderer", priority = 990)
public class BlockRendererMixin {

    @WrapOperation(method = "processQuad", at = @At(value = "INVOKE", target = "Lnet/caffeinemc/mods/sodium/client/render/chunk/compile/pipeline/BlockRenderer;bufferQuad(Lnet/caffeinemc/mods/sodium/client/render/frapi/mesh/MutableQuadViewImpl;[FLnet/caffeinemc/mods/sodium/client/render/chunk/terrain/material/Material;)V"))
    public void bufferQuad(BlockRenderer instance, MutableQuadViewImpl out, float[] dstIndex, Material material, Operation<Void> original) {
        if(out.normalFace().isAligned()) {
            LightManager.getInstance().processRenderQuadList(() -> {
                BlockPos pos = ((AbstractBlockRenderContextAccessor) this).getPos();
                LightManager.tempPos.set(pos);
                LightManager.count.set(0);
//        LightManager.quadDirection.set(out.normalFace().getAlignedNormal());
                LightManager.quadDirectionNormal.set(out.normalFace().getAlignedNormal());
                original.call(instance, out, dstIndex, material);
            });
        }
    }


    @WrapOperation(method = "bufferQuad", at = @At(value = "INVOKE", target = "Lnet/caffeinemc/mods/sodium/api/util/ColorARGB;toABGR(I)I"))
    public int toABGR(int color, Operation<Integer> original) {
        int newColor = LightManager.getInstance().processVertex(
                (index)->VanillaLightManager.getVertexPos(LightManager.quadDirectionNormal.get(), LightManager.tempPos.get(), index),
                color,
                (n)->{},
                ()-> {}
        );
        return ColorARGB.toABGR(newColor);
    }

}
