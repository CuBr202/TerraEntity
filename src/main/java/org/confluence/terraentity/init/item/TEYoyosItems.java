package org.confluence.terraentity.init.item;

import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.confluence.terraentity.item.YoyosItem;

import static org.confluence.terraentity.TerraEntity.MODID;

public class TEYoyosItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);

    public static DeferredItem<YoyosItem> AMAZON = ITEMS.register("amazon", () -> new YoyosItem(new Item.Properties(), 4.5f, 14,0xFFC896, "amazon"));
    public static DeferredItem<YoyosItem> ARTERY = ITEMS.register("artery", () -> new YoyosItem(new Item.Properties(), 4.2f, 13,0x9696FF, "artery"));
    public static DeferredItem<YoyosItem> CASCADE = ITEMS.register("cascade", () -> new YoyosItem(new Item.Properties(), 5.5f, 15,0xFFC896, "cascade"));
    public static DeferredItem<YoyosItem> CODE_1 = ITEMS.register("code_1", () -> new YoyosItem(new Item.Properties(), 4.8f, 14,0x96FF96, "code_1"));
    public static DeferredItem<YoyosItem> HIVE_FIVE = ITEMS.register("hive_five", () -> new YoyosItem(new Item.Properties(), 5.2f,14, 0xFFC896, "hive_five"));
    public static DeferredItem<YoyosItem> MALAISE = ITEMS.register("malaise", () -> new YoyosItem(new Item.Properties(), 3.8f,12, 0x9696FF, "malaise"));
    public static DeferredItem<YoyosItem> RALLY = ITEMS.register("rally", () -> new YoyosItem(new Item.Properties(), 3.5f, 10,0x9696FF, "rally"));
    public static DeferredItem<YoyosItem> VALOR = ITEMS.register("valor", () -> new YoyosItem(new Item.Properties(), 5.7f, 15,0xFFC896, "valor"));
    public static DeferredItem<YoyosItem> WOODEN_YOYO = ITEMS.register("wooden_yoyo", () -> new YoyosItem(new Item.Properties(), 1.5f, 8,0xFF00FF00, "wooden_yoyo"));


}
