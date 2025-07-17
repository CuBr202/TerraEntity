package org.confluence.terraentity.data.gen.loot;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class TELootTableProvider extends LootTableProvider {

    public TELootTableProvider(PackOutput pOutput, Set<ResourceLocation> pRequiredTables, List<SubProviderEntry> subProviders, CompletableFuture<HolderLookup.Provider> registries) {
        super(pOutput, pRequiredTables, subProviders);
    }

    public static LootTableProvider getProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProviderFuture) {
        return new LootTableProvider(output, Collections.emptySet(),
                List.of(
                        new SubProviderEntry(TEBlockLootProvider::new, LootContextParamSets.BLOCK),
                        new SubProviderEntry(TEEntityLootProvider::new, LootContextParamSets.ENTITY),
                        new SubProviderEntry(TESubLoot::new, LootContextParamSets.EMPTY),
                        new SubProviderEntry(()-> {
                            try {
                                return new TENPCLoot(lookupProviderFuture.get());
                            } catch (InterruptedException | ExecutionException e) {
                                throw new RuntimeException(e);
                            }}, LootContextParamSets.EMPTY)

                )

        );
    }
}