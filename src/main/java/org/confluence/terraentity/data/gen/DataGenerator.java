package org.confluence.terraentity.data.gen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.confluence.terraentity.data.gen.tags.ModDamageTypeTagsProvider;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import static org.confluence.terraentity.TerraEntity.MODID;

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerator {
    public static Map<String, DataProvider> PROVIDERS = null;

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        net.minecraft.data.DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        ExistingFileHelper helper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookup = event.getLookupProvider();

        DatapackBuiltinEntriesProvider provider = new DatapackBuiltinEntriesProvider(output, lookup, RegisterDataPack.DATA_BUILDER, Set.of(MODID));
        lookup = provider.getRegistryProvider();


        boolean client = event.includeClient();
        generator.addProvider(client, new TEChineseProvider(output));
        generator.addProvider(client, new TEEnglishProvider(output));
        generator.addProvider(client, new TEItemModelProvider(output, helper));

        boolean server = event.includeServer();
        generator.addProvider(server, provider);
        TEBlockTagsProvider blockTagsProvider = new TEBlockTagsProvider(output, lookup, helper);
        generator.addProvider(server, blockTagsProvider);
        generator.addProvider(server, new TEEntityTypeTagsProvider(output, lookup, helper));
        generator.addProvider(server, new TEItemTagsProvider(output, lookup, blockTagsProvider.contentsGetter(), helper));

//        generator.addProvider(server, new ModLootTableProvider(output));
        generator.addProvider(server, new ModDamageTypeTagsProvider(output, lookup, helper));
//        generator.addProvider(server, new ModPoiTypeTagsProvider(output, lookup, helper));

        PROVIDERS = generator.getProvidersView();

    }
}