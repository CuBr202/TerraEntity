package org.confluence.terraentity.item;

import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.init.TEDataComponentTypes;
import org.confluence.terraentity.entity.proj.WhipEntity;
import org.confluence.terraentity.init.TEAttributes;
import org.confluence.terraentity.init.TEEntities;

public class BaseWhipItem extends Item {

    public int hitCooldown;

    /**
     * <h1>鞭子
     * @param damage - 召唤伤害
     * @param hitCooldown - 击中同一目标的间隔
     */
    public BaseWhipItem(Properties properties,
                        float damage,
                        int hitCooldown) {
        super(properties.attributes(
                ItemAttributeModifiers.builder()
                        .add(
                                TEAttributes.SUMMON_DAMAGE.getDelegate(),
                                new AttributeModifier(TerraEntity.asResource("whip_damage_modifier"), damage, AttributeModifier.Operation.ADD_VALUE),
                                EquipmentSlotGroup.MAINHAND
                        ).build()
                )
        );
        this.hitCooldown = hitCooldown;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        if(!level.isClientSide){
            ItemStack stack = player.getItemInHand(usedHand);
            if(stack.getItem() instanceof  BaseWhipItem self) {
                WhipEntity whipEntity = TEEntities.WHIP_PROJECTILE.get().create(level);
                whipEntity.setOwner(player);
                whipEntity.setPos(player.position().add(0, 1, 0).add(getPlayerHandPos(player)));
                whipEntity.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 0, 1.0F);
                var data = stack.get(TEDataComponentTypes.EFFECT_STRATEGY);
                if (data != null)
                    whipEntity.hiteffect = data;
                whipEntity.hitCooldown = hitCooldown;
                level.addFreshEntity(whipEntity);
                player.getCooldowns().addCooldown(this, 20);
            }
        }
        player.swing(usedHand);
        return super.use(level, player, usedHand);
    }

    private Vec3 getPlayerHandPos(Player player) {
        int i = player.getMainArm() == HumanoidArm.RIGHT ? 1 : -1;
        float f =  player.yBodyRot* 0.017453292F + 1f;
        double d0 = Mth.sin(f);
        double d1 = Mth.cos(f);
        float f1 = player.getScale();
        double d2 = (double)i * 0.35 * (double)f1;
        double d3 = 0.8 * (double)f1;
        return new Vec3(-d1 * d2 - d0 * d3, 0, -d0 * d2 + d1 * d3);
    }
}