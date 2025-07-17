package org.confluence.terraentity.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.entity.player.Player;

import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.InventoryMenu;
import org.confluence.terraentity.entity.npc.trade.ITradeHolder;
import org.confluence.terraentity.mixed.IPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = Player.class, remap = false)
public abstract class PlayerMixin implements IPlayer{

    @Unique
    private ITradeHolder terra_entity$tradeHolder;

    @Unique
    private boolean terra_entity$isInfiniteInteractBlock;

    @Override
    public ITradeHolder terra_entity$getTradeHolder() {
        return terra_entity$tradeHolder;
    }

    @Override
    public void terra_entity$setTradeHolder(ITradeHolder  entity) {
        this.terra_entity$tradeHolder = entity;
    }

    @Override
    public boolean terra_entity$isInfiniteInteractBlock(){
        return terra_entity$isInfiniteInteractBlock;
    }

    @Override
    public void terra_entity$setInfiniteInteractBlock(boolean flag){
        this.terra_entity$isInfiniteInteractBlock = flag;
    }

//    @WrapOperation(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/AbstractContainerMenu;stillValid(Lnet/minecraft/world/entity/player/Player;)Z"))
//    public boolean terra_entity$tick(AbstractContainerMenu instance, Player player, Operation<Boolean> original){
//        if(this.terra_entity$isInfiniteInteractBlock()){
//            if(player.containerMenu instanceof InventoryMenu){
//                this.terra_entity$setInfiniteInteractBlock(false);
//                return original.call(instance, player);
//            }
//            return true;
//        }
//
//        return original.call(instance, player);
//    }

}
