package org.confluence.terraentity.mixin.sodium;

import com.llamalad7.mixinextras.sugar.Local;
import net.caffeinemc.mods.sodium.client.render.chunk.compile.ChunkBuildContext;
import net.caffeinemc.mods.sodium.client.render.chunk.compile.ChunkBuildOutput;
import net.caffeinemc.mods.sodium.client.render.chunk.compile.tasks.ChunkBuilderMeshingTask;
import net.caffeinemc.mods.sodium.client.util.task.CancellationToken;
import net.caffeinemc.mods.sodium.client.world.LevelSlice;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import org.confluence.terraentity.mixed.colorful_light.LightManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChunkBuilderMeshingTask.class)
public class ChunkBuilderMeshingTaskMixin {


    @Inject(method = "execute(Lnet/caffeinemc/mods/sodium/client/render/chunk/compile/ChunkBuildContext;Lnet/caffeinemc/mods/sodium/client/util/task/CancellationToken;)Lnet/caffeinemc/mods/sodium/client/render/chunk/compile/ChunkBuildOutput;",at = @At(value = "INVOKE", target = "Lnet/caffeinemc/mods/sodium/client/render/chunk/compile/pipeline/BlockRenderCache;getBlockRenderer()Lnet/caffeinemc/mods/sodium/client/render/chunk/compile/pipeline/BlockRenderer;"))
    private void execute(ChunkBuildContext context, CancellationToken token, CallbackInfoReturnable<ChunkBuildOutput> cir, @Local LevelSlice slice) {
        var renderer = ((ChunkBuilderTaskAccessor)this).getRender();
        BlockPos blockpos = new BlockPos(renderer.getOriginX(), renderer.getOriginY(), renderer.getOriginZ());
        BlockPos blockpos1 = blockpos.offset(15, 15, 15);
        for (BlockPos pos : BlockPos.betweenClosed(blockpos, blockpos1)) {
            LightManager.getInstance().addLight(pos, Minecraft.getInstance().level.getBlockState(pos).getBlock());

        }
    }
}
