package org.confluence.terraentity.item;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.neoforged.fml.ModLoader;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.confluence.terraentity.api.event.SummonEvent;
import org.confluence.terraentity.entity.summon.AbstractSummonMob;
import org.confluence.terraentity.init.TEAttachments;
import org.confluence.terraentity.utils.TEUtils;

import java.util.List;

public class SummonItem<T extends AbstractSummonMob> extends Item {
    public final DeferredHolder<EntityType<?>, EntityType<T>> entityType;
    public final int consume;

    public SummonItem(Properties properties, DeferredHolder<EntityType<?>, EntityType<T>> entityType, int consume) {
        super(properties.stacksTo(1));
        this.entityType = entityType;
        this.consume = consume;
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);

        if(!level.isClientSide) {
            EntityHitResult hit =  TEUtils.getEyeTraceHitResult(player, player.getAttributeValue(Attributes.ENTITY_INTERACTION_RANGE));
            if(hit!= null){
                if(hit.getEntity() instanceof AbstractSummonMob) {
                    hit.getEntity().discard();
                    return InteractionResultHolder.success(player.getItemInHand(hand));
                }
            }

            if (itemstack.getItem() instanceof SummonItem<?> summonItem) {
                if (!player.canBeSeenAsEnemy()) { // 创造
                    summon(player, level, itemstack);
                    return InteractionResultHolder.success(itemstack);
                }

                var data = player.getData(TEAttachments.SUMMONER_STORAGE.get());
                if (data.canSummon(summonItem.consume)) {
                    summon(player, level, itemstack);
                    return InteractionResultHolder.success(itemstack);
                }
            }
        }

        return InteractionResultHolder.fail(itemstack);
    }

    public void summon(Player player, Level level, ItemStack stack) {
        SummonEvent.Pre<T> event = new SummonEvent.Pre<>(player, stack, entityType.get());
        ModLoader.postEvent(event);
        if (event.isCancel()) {
            return;
        }

        var entity = entityType.get().create(level);
        BlockPos pos = TEUtils.getEyeBlockHitResult(player);
        entity.setPos(pos.getX(), pos.getY(), pos.getZ());
        entity.summon(player, stack);
        entity.cost = consume;
        level.addFreshEntity(entity);
        var data = player.getData(TEAttachments.SUMMONER_STORAGE.get());
        data.summon(consume);
        if(player instanceof ServerPlayer serverPlayer)
            data.sync(serverPlayer);
    }


    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.translatable("tooltip.terra_entity.summon_item_cost", consume).withColor(0xABAC00));
        tooltipComponents.add(Component.translatable("tooltip.terra_entity.summon_item_entity", entityType.get().getDescription()).withColor(0x1E90FF));

        var data = Minecraft.getInstance().player.getData(TEAttachments.SUMMONER_STORAGE.get());
        int a = data.getCurrentCapacity();
        int b = data.getMaxCapacity();
        tooltipComponents.add(Component.translatable("tooltip.terra_entity.summon_info", b - a ,  b).withColor(a <= 0? 0xAB0000 : 0x00ABAC));
    }

}