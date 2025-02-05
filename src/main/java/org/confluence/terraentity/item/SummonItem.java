package org.confluence.terraentity.item;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.fml.ModLoader;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.confluence.terraentity.api.event.SummonEvent;
import org.confluence.terraentity.attachment.SummonerAttachment;
import org.confluence.terraentity.entity.summon.AbstractSummonMob;
import org.confluence.terraentity.init.TEAttachments;
import org.confluence.terraentity.init.TEAttributes;
import org.confluence.terraentity.utils.TEUtils;

import java.util.List;

public class SummonItem<T extends AbstractSummonMob> extends Item {
    public final DeferredHolder<EntityType<?>, EntityType<T>> entityType;
    public final int consume;

    public final float baseAttackDamage;

    public SummonItem(Properties properties, DeferredHolder<EntityType<?>, EntityType<T>> entityType, int consume, float baseAttackDamage) {
        super(properties.stacksTo(1));
        this.entityType = entityType;
        this.consume = consume;
        this.baseAttackDamage = baseAttackDamage;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);

        if (!level.isClientSide) {

            var data = player.getData(TEAttachments.SUMMONER_STORAGE.get());
            data.refresh((ServerPlayer) player);

            EntityHitResult hit = TEUtils.getEyeTraceHitResult(player, player.getAttributeValue(Attributes.ENTITY_INTERACTION_RANGE));
            if (hit != null) {
                if (hit.getEntity() instanceof AbstractSummonMob) {
                    hit.getEntity().discard();
                    return InteractionResultHolder.success(player.getItemInHand(hand));
                }
            }

            player.startUsingItem(hand);

            return InteractionResultHolder.pass(itemstack);
        }
        return InteractionResultHolder.fail(itemstack);
    }


    public void summon(Player player, ItemStack stack) {
        Level level = player.level();
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
        data.summon(consume, entity.getId());
        if (player instanceof ServerPlayer serverPlayer)
            data.sync(serverPlayer);
    }


    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        float additionAttackDamage = (float) Minecraft.getInstance().player.getAttributeValue(TEAttributes.SUMMON_DAMAGE) - 1;
        tooltipComponents.add(Component.translatable("attribute.name.player.summon_damage").append(": " +
                        (baseAttackDamage + (additionAttackDamage > 0 ? "  +%d%%".formatted((int)(additionAttackDamage * 100)): "")))
                .withColor(0x00AB00));

        tooltipComponents.add(Component.translatable("tooltip.terra_entity.summon_item_cost", consume).withColor(0xABAC00));
        tooltipComponents.add(Component.translatable("tooltip.terra_entity.summon_item_entity", entityType.get().getDescription()).withColor(0x1E90FF));

        var data = Minecraft.getInstance().player.getData(TEAttachments.SUMMONER_STORAGE.get());
        int a = data.getCurrentCapacity();
        int b = SummonerAttachment.getMaxCapacity(Minecraft.getInstance().player);
        tooltipComponents.add(Component.translatable("tooltip.terra_entity.summon_info", b - a, b).withColor(a <= 0 ? 0xAB0000 : 0x00ABAC));
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 1000;
    }

    @Override
    public void onStopUsing(ItemStack stack, LivingEntity livingEntity, int count) {

        // 召唤
        if (count > getUseDuration(stack, livingEntity) - 20) {
            livingEntity.swing(livingEntity.getUsedItemHand());
            if (livingEntity instanceof ServerPlayer player) {
                var data = player.getData(TEAttachments.SUMMONER_STORAGE.get());
                    // 创造
                if (!player.canBeSeenAsEnemy()) {
                    summon(player, stack);
                    return;
                }
                if (data.canSummon(consume)) {
                    summon(player, stack);
                    return;
                }
            }
        }
    }

    @Override
    public void onUseTick(Level level, LivingEntity livingEntity, ItemStack stack, int remainingUseDuration) {
        // 收回所有召唤物
        if (getUseDuration(stack, livingEntity) - remainingUseDuration == 20) {
            if (livingEntity instanceof ServerPlayer player) {
                var data = player.getData(TEAttachments.SUMMONER_STORAGE.get());
                data.clear(player);
                data.sync(player);
            }
        }
    }
}