package org.confluence.terraentity.init.item;

import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.confluence.terraentity.item.YoyosItem;

import static org.confluence.terraentity.TerraEntity.MODID;

public class TEYoyosItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);

    public static DeferredItem<YoyosItem> AMAZON = ITEMS.register("amazon", () -> new YoyosItem(new Item.Properties(), 1, 10,0xFF00FF00, "amazon"));
    public static DeferredItem<YoyosItem> ARTERY = ITEMS.register("artery", () -> new YoyosItem(new Item.Properties(), 1, 10,0xFF00FF00, "artery"));
    public static DeferredItem<YoyosItem> CASCADE = ITEMS.register("cascade", () -> new YoyosItem(new Item.Properties(), 1, 10,0xFF00FF00, "cascade"));
    public static DeferredItem<YoyosItem> CODE_1 = ITEMS.register("code_1", () -> new YoyosItem(new Item.Properties(), 1, 10,0xFF00FF00, "code_1"));
    public static DeferredItem<YoyosItem> HIVE_FIVE = ITEMS.register("hive_five", () -> new YoyosItem(new Item.Properties(), 1,10, 0xFF00FF00, "hive_five"));
    public static DeferredItem<YoyosItem> MALAISE = ITEMS.register("malaise", () -> new YoyosItem(new Item.Properties(), 1,10, 0xFF00FF00, "malaise"));
    public static DeferredItem<YoyosItem> RALLY = ITEMS.register("rally", () -> new YoyosItem(new Item.Properties(), 1, 10,0xFF00FF00, "rally"));
    public static DeferredItem<YoyosItem> VALOR = ITEMS.register("valor", () -> new YoyosItem(new Item.Properties(), 1, 10,0xFF00FF00, "valor"));
    public static DeferredItem<YoyosItem> WOODEN_YOYO = ITEMS.register("wooden_yoyo", () -> new YoyosItem(new Item.Properties(), 1, 10,0xFF00FF00, "wooden_yoyo"));


}
