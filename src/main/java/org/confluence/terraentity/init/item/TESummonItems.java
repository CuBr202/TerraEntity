package org.confluence.terraentity.init.item;

import net.minecraft.world.item.Item;

import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.confluence.terraentity.init.TEEntities;
import org.confluence.terraentity.item.SummonItem;

import static org.confluence.terraentity.TerraEntity.MODID;

public class TESummonItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS,MODID);

    public static final RegistryObject<Item> HORNET_STAFF = ITEMS.register("hornet_staff", () -> new SummonItem<>(new Item.Properties(), TEEntities.SUMMON_HORNET, 1, 8));

    public static final RegistryObject<Item> IRON_GOLEM_STAFF = ITEMS.register("iron_golem_staff", () -> new SummonItem<>(new Item.Properties(), TEEntities.SUMMON_IRON_GOLEM, 1, 8));

    public static final RegistryObject<Item> SLIME_STAFF = ITEMS.register("slime_staff", () -> new SummonItem<>(new Item.Properties(), TEEntities.SUMMON_SLIME, 1, 5));

}
