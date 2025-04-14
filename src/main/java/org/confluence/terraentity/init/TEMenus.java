package org.confluence.terraentity.init;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.menu.TETradesMenu;
import org.confluence.terraentity.registries.npc_trade.variant.ItemTradeItem;

import java.util.function.Supplier;

public class TEMenus {
    public static final DeferredRegister<MenuType<?>> TYPES = DeferredRegister.create(BuiltInRegistries.MENU, TerraEntity.MODID);


    public static final Supplier<MenuType<TETradesMenu<ItemTradeItem>>> NPC_TRADES_MENU = TYPES.register("npc_trades", () -> new MenuType<>((id, inv)->new TETradesMenu<>(id, inv, null), FeatureFlags.VANILLA_SET));


}
