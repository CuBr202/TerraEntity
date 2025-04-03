package org.confluence.terraentity.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.confluence.terraentity.entity.rideable.AbstractRideableEntity;

import java.util.function.Predicate;

public class RideableItem extends Item {
    EntityType<? extends AbstractRideableEntity> entityType;
    Predicate<Player> canUse;
    public RideableItem(Properties properties, EntityType<? extends AbstractRideableEntity> entityType) {
        this(properties, entityType, player -> true);
    }

    public RideableItem(Properties properties, EntityType<? extends AbstractRideableEntity> entityType, Predicate<Player> canUse) {
        super(properties);
        this.entityType = entityType;
        this.canUse = canUse;
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        if(player.onGround())
            summonRideableEntity(player);
        return super.use(level, player, usedHand);
    }

    public void summonRideableEntity(Player player) {
        Level level = player.level();
        if(!level.isClientSide){
            if(player.getVehicle() == null){
                if(canUse.test(player)) {
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
        }
    }

}