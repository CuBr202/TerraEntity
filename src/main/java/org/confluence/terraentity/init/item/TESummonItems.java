package org.confluence.terraentity.init.item;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.confluence.terraentity.entity.summon.SummonSword;
import org.confluence.terraentity.init.TEEntities;
import org.confluence.terraentity.init.entity.TESummonEntities;
import org.confluence.terraentity.item.SummonItem;

import static org.confluence.terraentity.TerraEntity.MODID;

public class TESummonItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);

    public static final DeferredItem<Item> FINCH_STAFF = ITEMS.register("finch_staff", () -> new SummonItem<>(new Item.Properties(), TESummonEntities.SUMMON_FINCH, 1, 2));
    public static final DeferredItem<Item> IRON_GOLEM_STAFF = ITEMS.register("iron_golem_staff", () -> new SummonItem<>(new Item.Properties(), TESummonEntities.SUMMON_IRON_GOLEM, 1, 8));
    public static final DeferredItem<Item> SLIME_STAFF = ITEMS.register("slime_staff", () -> new SummonItem<>(new Item.Properties(), TESummonEntities.SUMMON_SLIME, 1, 5));
    public static final DeferredItem<Item> HORNET_STAFF = ITEMS.register("hornet_staff", () -> new SummonItem<>(new Item.Properties(), TESummonEntities.SUMMON_HORNET, 1, 8));


    //棱镜系列
    public static final DeferredItem<Item> SUMMON_STONE_SWORD_STAFF = ITEMS.register("summon_stone_sword_staff", () -> new SummonItem<>(new Item.Properties(), TESummonEntities.SUMMON_STONE_SWORD, 1, 3));
    public static final DeferredItem<Item> SUMMON_IRON_SWORD_STAFF = ITEMS.register("summon_iron_sword_staff", () -> new SummonItem<>(new Item.Properties(), TESummonEntities.SUMMON_IRON_SWORD, 1, 4));
    public static final DeferredItem<Item> SUMMON_GOLDEN_SWORD_STAFF = ITEMS.register("summon_golden_sword_staff", () -> new SummonItem<>(new Item.Properties(), TESummonEntities.SUMMON_GOLDEN_SWORD, 1, 5));
    public static final DeferredItem<Item> SUMMON_DIAMOND_SWORD_STAFF = ITEMS.register("summon_diamond_sword_staff", () -> new SummonItem<>(new Item.Properties(), TESummonEntities.SUMMON_DIAMOND_SWORD, 1, 6));
    public static final DeferredItem<Item> SUMMON_NETHERITE_SWORD_STAFF = ITEMS.register("summon_netherite_sword_staff", () -> new SummonItem<>(new Item.Properties(), TESummonEntities.SUMMON_NETHERITE_SWORD, 1, 7));


}
