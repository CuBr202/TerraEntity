package org.confluence.terraentity.data.gen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static org.confluence.terraentity.TerraEntity.MODID;

@EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.MOD)
public class DataGenerator {
    public static Map<String, DataProvider> PROVIDERS = null;

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        net.minecraft.data.DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        ExistingFileHelper helper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookup = event.getLookupProvider();

        boolean client = event.includeClient();
        generator.addProvider(client, new TEChineseProvider(output));
        generator.addProvider(client, new TEEnglishProvider(output));
        generator.addProvider(client, new TEItemModelProvider(output, helper));

        boolean server = event.includeServer();
        TEBlockTagsProvider blockTagsProvider = new TEBlockTagsProvider(output, lookup, helper);
        generator.addProvider(server, blockTagsProvider);
        generator.addProvider(server, new TEEntityTypeTagsProvider(output, lookup, helper));
        generator.addProvider(server, new TEItemTagsProvider(output, lookup, blockTagsProvider.contentsGetter(), helper));
//        generator.addProvider(server, new ModLootTableProvider(output));
//        generator.addProvider(server, new ModDamageTypeTagsProvider(output, lookup, helper));
//        generator.addProvider(server, new ModPoiTypeTagsProvider(output, lookup, helper));

        PROVIDERS = generator.getProvidersView();

    }
}