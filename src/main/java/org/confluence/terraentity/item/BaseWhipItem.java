package org.confluence.terraentity.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import net.minecraft.world.level.Level;

import org.confluence.terraentity.init.TEDataComponentTypes;
import org.confluence.terraentity.entity.proj.WhipEntity;
import org.confluence.terraentity.init.TEAttributes;
import org.confluence.terraentity.init.TEEntities;
import org.confluence.terraentity.registries.datacomponent.IDataComponentType;
import org.confluence.terraentity.registries.hit_effect.IEffectStrategy;
import org.confluence.terraentity.utils.TEUtils;

import java.util.List;

public class BaseWhipItem extends Item implements IItemExtension {

    public final int hitCooldown;
    public final float markDamage;
    public final float attackSpeed;
    public final float damage;

    TEItemProperties properties;
    /**
     * <h1>鞭子
     * @param damage - 召唤伤害
     * @param markDamage - 标记伤害
     * @param attackSpeed - 攻击速度
     * @param hitCooldown - 击中同一目标的间隔
     */
    public BaseWhipItem(TEItemProperties properties,
                        float damage,
                        float markDamage,
                        float attackSpeed,
                        int hitCooldown) {
        super(properties.stacksTo(1).setNoRepair());
        this.hitCooldown = hitCooldown;
        this.markDamage = markDamage;
        this.attackSpeed = attackSpeed;
        this.damage = damage;
        this.properties = properties;
    }

    private double getCdReduction(Player player) {
        double speed = player.getAttribute(Attributes.ATTACK_SPEED).getValue();
        return 1 / (speed * 0.25f);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        if(usedHand == InteractionHand.OFF_HAND) return super.use(level, player, usedHand);
        if(!level.isClientSide){
            ItemStack stack = player.getItemInHand(usedHand);
            if(stack.getItem() instanceof  BaseWhipItem self) {
                WhipEntity whipEntity = TEEntities.WHIP_PROJECTILE.get().create(level);
                whipEntity.setOwner(player);
                whipEntity.setPos(player.position().add(0, 1, 0).add(TEUtils.getPlayerHandPos(player)));
                whipEntity.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 0.05f, 1.0F);
                var data = IDataComponentType.getData(stack, TEDataComponentTypes.EFFECT_STRATEGY.get());
                if (data != null)
                    whipEntity.hiteffect = data;
                whipEntity.hitCooldown = hitCooldown;
                level.addFreshEntity(whipEntity);
                player.getCooldowns().addCooldown(this, (int) (20 * getCdReduction(player)));
            }
        }
        player.swing(usedHand);
        return super.use(level, player, usedHand);
    }

    public void appendHoverText(ItemStack stack, Level context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        var data = IDataComponentType.getData(stack, TEDataComponentTypes.EFFECT_STRATEGY.get());
        if(data!=null){
            IEffectStrategy.appendDescription(tooltipComponents, data.effects(),Component.translatable("tooltip.terraentity.whip.effect_strategy"));
        }

    }

    @Override
    public void onStackInit(ItemStack stack) {
        this.properties.dataComponentTypeMap.forEach((v)->{
            v.writeToNBT(stack.getOrCreateTag());
        });
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        if(slot == EquipmentSlot.MAINHAND)
            return ImmutableMultimap.of(
                    TEAttributes.SUMMON_DAMAGE.get(), new AttributeModifier("whip_damage_modifier", damage, AttributeModifier.Operation.ADDITION),
                    TEAttributes.MARK_DAMAGE.get(), new AttributeModifier("whip_mark_damage_modifier", markDamage, AttributeModifier.Operation.ADDITION),
                    Attributes.ATTACK_SPEED, new AttributeModifier("whip_attack_speed_modifier", attackSpeed, AttributeModifier.Operation.MULTIPLY_BASE)
            );
        return super.getAttributeModifiers(slot, stack);
    }
}