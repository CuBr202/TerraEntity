package org.confluence.terraentity.mixin.client;

import net.minecraft.client.renderer.LevelRenderer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({LevelRenderer.class})
public abstract class LevelRendererMixin {

//    @Shadow public abstract boolean isSectionCompiled(BlockPos pos);

    public LevelRendererMixin() {
    }
//    //修复肉山在地下被剔除的问题
//    @Redirect(method = "renderLevel", at = @At(
//            value = "INVOKE",
//            target = "Lnet/minecraft/client/renderer/LevelRenderer;isSectionCompiled(Lnet/minecraft/core/BlockPos;)Z",
//            ordinal = 0
//    ))
//    public boolean renderLevel(
//            LevelRenderer instance, BlockPos pos,@Local(ordinal = 0) Entity entity) {
//        return entity instanceof WallOfFlesh || this.isSectionCompiled(pos);
//    }

}
