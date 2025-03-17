package org.confluence.terraentity.data.gen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import org.confluence.terraentity.data.gen.loot.TELootTableProvider;
import org.confluence.terraentity.data.gen.recipe.TERecipeProvider;
import org.confluence.terraentity.data.gen.tags.TEDamageTypeTagsProvider;
import org.confluence.terraentity.data.gen.tags.TEBlockTagsProvider;
import org.confluence.terraentity.data.gen.tags.TEEntityTypeTagsProvider;
import org.confluence.terraentity.data.gen.tags.TEItemTagsProvider;

import java.util.Map;
import java.util.Set;
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
        boolean server = event.includeServer();


        DatapackBuiltinEntriesProvider provider = new DatapackBuiltinEntriesProvider(output, lookup, RegisterDataPack.DATA_BUILDER, Set.of(MODID));
        lookup = provider.getRegistryProvider();


        generator.addProvider(server, provider);
        generator.addProvider(server, new TEEntityTypeTagsProvider(output, lookup, helper));
        generator.addProvider(server, new TEDamageTypeTagsProvider(output, lookup, helper));
        TEBlockTagsProvider blockTagsProvider = new TEBlockTagsProvider(output, lookup, helper);
        generator.addProvider(server, blockTagsProvider);
        generator.addProvider(server, new TEItemTagsProvider(output, lookup, blockTagsProvider.contentsGetter(), helper));
//        generator.addProvider(server,new TERecipeProvider(output,lookup));
//        generator.addProvider(server, TELootTableProvider.getProvider(output, lookup));


        boolean client = event.includeClient();
        generator.addProvider(client, new TEChineseProvider(output));
        generator.addProvider(client, new TEEnglishProvider(output));
        generator.addProvider(client, new TEItemModelProvider(output, helper));


        PROVIDERS = generator.getProvidersView();

    }
}