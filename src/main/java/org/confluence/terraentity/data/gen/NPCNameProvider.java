package org.confluence.terraentity.data.gen;

import com.mojang.serialization.Codec;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.entity.npc.misc.NPCNames;
import org.confluence.terraentity.init.entity.TENpcEntities;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class NPCNameProvider extends AbstractExistCodecProvider<Map<ResourceLocation, NPCNames>> {

    public NPCNameProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider);
    }

    @Override
    protected void run(HolderLookup.Provider provider) {
        gen(TerraEntity.space(NPCNames.FILE_NAME),Map.of(
                TENpcEntities.GUIDE.getId(),
                NPCNames.of(Map.of(
                        "jack",5f,
                        "daniel",10f
                )),
                TENpcEntities.NURSE.getId(),
                NPCNames.of(Map.of(
                        "jane",2f,
                        "miss",3f)
                )
        ));
    }

    @Override
    protected Codec<Map<ResourceLocation, NPCNames>> getCodec() {
        return NPCNames.MAP_CODEC;
    }

    @Override
    public String getName() {
        return NPCNames.KEY;
    }
}
