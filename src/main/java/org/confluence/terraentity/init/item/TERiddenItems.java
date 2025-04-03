package org.confluence.terraentity.init.item;

import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.confluence.terraentity.init.TEEntities;
import org.confluence.terraentity.item.RideableItem;

import static org.confluence.terraentity.TerraEntity.MODID;

public class TERiddenItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);

    public static final DeferredItem<RideableItem> RIDEABLE_SLIME = ITEMS.register("rideable_slime",()->new RideableItem(new Item.Properties(), TEEntities.RIDEABLE_SLIME.get()));


}
