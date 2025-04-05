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
import java.util.function.Supplier;

public class RideableItem<T extends AbstractRideableEntity> extends Item {
    Supplier<EntityType<T>> entityType;
    Predicate<Player> canUse;
    public RideableItem(Properties properties,  Supplier<EntityType<T>> entityType) {
        this(properties.stacksTo(1), entityType, player -> true);
    }

    public RideableItem(Properties properties,  Supplier<EntityType<T>> entityType, Predicate<Player> canUse) {
        super(properties);
        this.entityType = entityType;
        this.canUse = canUse;
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        summonRideableEntity(player);
        return super.use(level, player, usedHand);
    }

    public void summonRideableEntity(Player player) {
        Level level = player.level();
        if(!level.isClientSide){
            if(player.getVehicle() == null){
                if(canUse.test(player)) {
                    AbstractRideableEntity slime = entityType.get().create(level);
                    if (slime != null) {
                        slime.setOwnerUUID(player.getUUID());
                        slime.setXRot(player.getXRot());
                        slime.setYRot(player.getYRot());
                        slime.setPos(player.getX(), player.getY(), player.getZ());

                        slime.doPlayerRide(player);

                        level.addFreshEntity(slime);
                        slime.onInit(player);
                    }
                }
            }
        }
    }

}