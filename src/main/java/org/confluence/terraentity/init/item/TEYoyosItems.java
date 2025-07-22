package org.confluence.terraentity.init.item;

import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.confluence.terraentity.item.YoyosItem;

import static org.confluence.terraentity.TerraEntity.MODID;

public class TEYoyosItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);

    public static DeferredItem<YoyosItem> BASE_YOYOS = ITEMS.register("base_yoyos", () -> new YoyosItem(new Item.Properties(), 1, 0xFF00FF00));


}
