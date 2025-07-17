package org.confluence.terraentity.mixin.container;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ChestMenu;
import org.confluence.terraentity.mixed.IPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChestMenu.class)
public class ChestMenuMixin {

    @Inject(method = "stillValid", at = @At("HEAD"), cancellable = true)
    private void stillValid(Player pPlayer, CallbackInfoReturnable<Boolean> cir) {
        if(((IPlayer)pPlayer).terra_entity$isInfiniteInteractBlock()){
            cir.setReturnValue(true);
        }
    }
}
