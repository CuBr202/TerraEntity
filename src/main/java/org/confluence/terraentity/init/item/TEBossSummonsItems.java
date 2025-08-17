package org.confluence.terraentity.init.item;

import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.init.entity.TEBossEntities;
import org.confluence.terraentity.item.BossSummonsItem;

public class TEBossSummonsItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(TerraEntity.MODID);

    public static DeferredHolder<Item, BossSummonsItem<?>> TEST = ITEMS.register("eye_of_cthulhu_summons", () -> new BossSummonsItem<>(new Item.Properties(), TEBossEntities.EYE_OF_CTHULHU));
    public static DeferredHolder<Item, BossSummonsItem<?>> TEST1 = ITEMS.register("hill_of_flesh_summons", () -> new BossSummonsItem<>(new Item.Properties(), TEBossEntities.HILL_OF_FLESH).setMaxSummonRange(25).setOffsetY(-10));



}
