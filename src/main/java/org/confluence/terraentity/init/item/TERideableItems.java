package org.confluence.terraentity.init.item;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;

import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.confluence.terraentity.init.entity.TERideableEntities;
import org.confluence.terraentity.item.RideableItem;

import static org.confluence.terraentity.TerraEntity.MODID;

public class TERideableItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);

    public static final RegistryObject<RideableItem> SLIMY_SADDLE = ITEMS.register("slimy_saddle",()->new RideableItem<>(new Item.Properties(), TERideableEntities.RIDEABLE_SLIME));
    public static final RegistryObject<RideableItem> HONEYED_GOGGLES = ITEMS.register("honeyed_goggles",()->new RideableItem<>(new Item.Properties(), TERideableEntities.RIDEABLE_BEE, Entity::onGround));


}
