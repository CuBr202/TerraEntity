package org.confluence.terraentity.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.config.ServerConfig;
import org.confluence.terraentity.init.item.*;
import org.confluence.terraentity.item.HouseDetectItem;

import java.util.function.Supplier;

import static org.confluence.terraentity.TerraEntity.MODID;

public class TEItems {
    public static DeferredRegister<Item> TOOLS = DeferredRegister.create(Registries.ITEM, MODID);

    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    public static final Supplier<Item> HOUSE_DETECTOR = TOOLS.register("house_detector", () -> new HouseDetectItem(new Item.Properties().stacksTo(1)));


    // Sentry Items
//    public static final DeferredItem<Item> SENTRY_STAFF = SENTRY_ITEMS.register("sentry_staff", () -> new SentryItem<>(new Item.Properties(), TEEntities.SUMMON_HORNET, 1, 5));
//    public static final DeferredItem<Item> DEBUG_ITEM = SUMMON_ITEMS.register("debug_item", () -> new DebugItem(new Item.Properties().stacksTo(1)));


    public static final RegistryObject<CreativeModeTab> NEO_TERRA =
            TABS.register(MODID + "_tab", ()-> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.terraentity.title"))
                    .icon(()-> TESpawnEggItems.KING_SLIME_SPAWN_EGG.get().getDefaultInstance())
                    .displayItems((itemDisplayParameters, output) -> {
                        TESpawnEggItems.ITEMS.getEntries().forEach(item -> output.accept(item.get()));
                        TERideableItems.ITEMS.getEntries().forEach(item -> output.accept(item.get()));
                        if(ServerConfig.DISPLAY_SUMMON_ITEMS.get()) {
                            TESummonItems.ITEMS.getEntries().forEach(item -> output.accept(item.get()));
                            TEWhipItems.ITEMS.getEntries().forEach(item -> output.accept(item.get()));
                            TEBoomerangItems.ITEMS.getEntries().forEach(item -> output.accept(item.get()));
                        }
//                        TEBlocks.BLOCKITEMS.getEntries().forEach(item -> output.accept(item.get()));
                        TEItems.TOOLS.getEntries().forEach(item -> output.accept(item.get()));
                    })
                    .withTabsAfter(ResourceKey.create(Registries.CREATIVE_MODE_TAB, TerraEntity.fromSpaceAndPath("terra_moment", "tab")))
                    .withTabsBefore(ResourceKey.create(Registries.CREATIVE_MODE_TAB, TerraEntity.fromSpaceAndPath("confluence", "summoners")))
                    .build());

    public static void register(IEventBus bus) {
        TESpawnEggItems.ITEMS.register(bus);
        TESummonItems.ITEMS.register(bus);
        TEWhipItems.ITEMS.register(bus);
        TEBoomerangItems.ITEMS.register(bus);
        TERideableItems.ITEMS.register(bus);
        TEItems.TOOLS.register(bus);
//        SENTRY_ITEMS.register(bus);
        TABS.register(bus);

    }
}
