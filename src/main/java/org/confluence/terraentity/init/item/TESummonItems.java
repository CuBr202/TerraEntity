package org.confluence.terraentity.init.item;

import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.confluence.terraentity.init.entity.TESummonEntities;
import org.confluence.terraentity.item.SummonItem;

import static org.confluence.terraentity.TerraEntity.MODID;

public class TESummonItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);

    public static final DeferredItem<Item> FINCH_STAFF = ITEMS.register("finch_staff", () -> new SummonItem<>(new Item.Properties(), TESummonEntities.SUMMON_HORNET, 1, 2));
    public static final DeferredItem<Item> IRON_GOLEM_STAFF = ITEMS.register("iron_golem_staff", () -> new SummonItem<>(new Item.Properties(), TESummonEntities.SUMMON_IRON_GOLEM, 1, 8));
    public static final DeferredItem<Item> SLIME_STAFF = ITEMS.register("slime_staff", () -> new SummonItem<>(new Item.Properties(), TESummonEntities.SUMMON_SLIME, 1, 5));
    public static final DeferredItem<Item> HORNET_STAFF = ITEMS.register("hornet_staff", () -> new SummonItem<>(new Item.Properties(), TESummonEntities.SUMMON_HORNET, 1, 8));



}
