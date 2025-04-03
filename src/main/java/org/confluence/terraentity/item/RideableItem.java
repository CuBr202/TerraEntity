package org.confluence.terraentity.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.confluence.terraentity.entity.rideable.AbstractRideableEntity;

public class RideableItem extends Item {
    EntityType<? extends AbstractRideableEntity> entityType;

    public RideableItem(Properties properties, EntityType<? extends AbstractRideableEntity> entityType) {
        super(properties);
        this.entityType = entityType;
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        if(!level.isClientSide){
            if(player.getVehicle() == null){
                AbstractRideableEntity slime = entityType.create(level);
                if (slime != null) {
                    slime.setOwnerUUID(player.getUUID());
                    slime.setXRot(player.getXRot());
                    slime.setYRot(player.getYRot());
                    slime.setPos(player.getX(), player.getY(), player.getZ());
                    slime.doPlayerRide(player);
                    level.addFreshEntity(slime);
                }
            }
        }
        return super.use(level, player, usedHand);
    }

}