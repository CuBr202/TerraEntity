package org.confluence.terraentity.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.ItemModelShaper;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.confluence.terraentity.client.init.model.AdditionalItemRegister;
import org.confluence.terraentity.init.TEAttachments;
import org.confluence.terraentity.init.item.TESummonItems;
import org.confluence.terraentity.item.BaseWhipItem;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemRenderer.class)
public class ItemRendererMixin {
    @Shadow
    @Final
    private ItemModelShaper itemModelShaper;

    @Inject(method = "renderStatic(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemDisplayContext;ZLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/world/level/Level;III)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/ItemRenderer;getModel(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/LivingEntity;I)Lnet/minecraft/client/resources/model/BakedModel;"), cancellable = true)
    private void renderStatic(LivingEntity entity, ItemStack itemStack, ItemDisplayContext displayContext, boolean leftHand, PoseStack poseStack, MultiBufferSource bufferSource, Level level, int combinedLight, int combinedOverlay, int seed, CallbackInfo ci) {
        if (entity instanceof Player player && !leftHand && itemStack.getItem() instanceof BaseWhipItem item) {
            // 右手使用鞭子时取消渲染
            if (player.getCooldowns().isOnCooldown(item)) {
                if (displayContext == ItemDisplayContext.THIRD_PERSON_RIGHT_HAND) {
                    ci.cancel();
                }
            }
        }
    }

    @Inject(method = "getModel", at = @At("HEAD"), cancellable = true)
    public void getModel(ItemStack stack, Level level, LivingEntity entity, int seed, CallbackInfoReturnable<BakedModel> cir) {
        if (entity instanceof Player player) {
            if (!player.getData(TEAttachments.SUMMONER_STORAGE).canSummon(1)) {
                if (stack.getItem() == TESummonItems.FINCH_STAFF.get()) {
                    cir.setReturnValue(itemModelShaper.getModelManager().getModel(AdditionalItemRegister.FINCH_STAFF_MODEL));
                }
            }
        }
    }
}
