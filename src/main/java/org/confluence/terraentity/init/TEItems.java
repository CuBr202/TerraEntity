package org.confluence.terraentity.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.config.ServerConfig;
import org.confluence.terraentity.init.item.TEBoomerangItems;
import org.confluence.terraentity.init.item.TESpawnEggItems;
import org.confluence.terraentity.init.item.TESummonItems;
import org.confluence.terraentity.init.item.TEWhipItems;

import static org.confluence.terraentity.TerraEntity.MODID;

public class TEItems {

    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);


    // Sentry Items
//    public static final DeferredItem<Item> SENTRY_STAFF = SENTRY_ITEMS.register("sentry_staff", () -> new SentryItem<>(new Item.Properties(), TEEntities.SUMMON_HORNET, 1, 5));
//    public static final DeferredItem<Item> DEBUG_ITEM = SUMMON_ITEMS.register("debug_item", () -> new DebugItem(new Item.Properties().stacksTo(1)));


    public static final RegistryObject<CreativeModeTab> NEO_TERRA =
            TABS.register(MODID + "_tab", ()-> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.terraentity.title"))
                    .icon(()-> TESpawnEggItems.KING_SLIME_SPAWN_EGG.get().getDefaultInstance())
                    .displayItems((itemDisplayParameters, output) -> {
                        TESpawnEggItems.ITEMS.getEntries().forEach(item -> output.accept(item.get()));
                        if(ServerConfig.DISPLAY_SUMMON_ITEMS.get()) {
                            TESummonItems.ITEMS.getEntries().forEach(item -> output.accept(item.get()));
                            TEWhipItems.ITEMS.getEntries().forEach(item -> output.accept(item.get()));
                            TEBoomerangItems.ITEMS.getEntries().forEach(item -> output.accept(item.get()));
                        }
                    })
                    .withTabsAfter(ResourceKey.create(Registries.CREATIVE_MODE_TAB, TerraEntity.asResource("terra_moment", "tab")))
                    .withTabsBefore(ResourceKey.create(Registries.CREATIVE_MODE_TAB, TerraEntity.asResource("confluence", "summoners")))
                    .build());

    public static void register(IEventBus bus) {
        TESpawnEggItems.ITEMS.register(bus);
        TESummonItems.ITEMS.register(bus);
        TEWhipItems.ITEMS.register(bus);
        TEBoomerangItems.ITEMS.register(bus);
//        SENTRY_ITEMS.register(bus);
        TABS.register(bus);

    }
}
