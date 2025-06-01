package org.confluence.terraentity.data.gen;

import com.google.common.collect.ImmutableMap;
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
        gen(TerraEntity.space(NPCNames.FILE_NAME), ImmutableMap.<ResourceLocation, NPCNames>builder()
                .put(TENpcEntities.GUIDE.getId(), NPCNames.of(ImmutableMap.<String, Float>builder()
                        .put("Andrew", 1f)
                        .put("Asher", 1f)
                        .put("Bradley", 1f)
                        .put("Brandon", 1f)
                        .put("Brett", 1f)
                        .put("Brian", 1f)
                        .put("Cody", 1f)
                        .put("Cole", 1f)
                        .put("Colin", 1f)
                        .put("Connor", 1f)
                        .build()))
                .put(TENpcEntities.NURSE.getId(), NPCNames.of(ImmutableMap.<String, Float>builder()
                        .put("Abigail", 1f)
                        .put("Allison", 1f)
                        .put("Amy", 1f)
                        .put("Caitlin", 1f)
                        .put("Carly", 1f)
                        .put("Claire", 1f)
                        .put("Emily", 1f)
                        .put("Emma", 1f)
                        .put("Hannah", 1f)
                        .put("Heather", 1f)
                        .build()))
                .put(TENpcEntities.PAINTER.getId(), NPCNames.of(ImmutableMap.<String, Float>builder()
                        .put("Bruno", 1f)
                        .put("Carlo", 1f)
                        .put("Darren", 1f)
                        .put("Enzo", 1f)
                        .put("Esreadel", 1f)
                        .put("Guido", 1f)
                        .put("Jim", 1f)
                        .put("Leonardo", 1f)
                        .put("Lorenzo", 1f)
                        .put("Luca", 1f)
                        .build()))
                .put(TENpcEntities.MERCHANT.getId(), NPCNames.of(ImmutableMap.<String, Float>builder()
                        .put("Alfred", 1f)
                        .put("Barney", 1f)
                        .put("Calvin", 1f)
                        .put("Edmund", 1f)
                        .put("Edwin", 1f)
                        .put("Eugene", 1f)
                        .put("Frank", 1f)
                        .put("Frederick", 1f)
                        .put("Gilbert", 1f)
                        .put("Gus", 1f)
                        .build()))
                .put(TENpcEntities.GOBLIN_TINKERER.getId(), NPCNames.of(ImmutableMap.<String, Float>builder()
                        .put("Arback", 1f)
                        .put("Dalek", 1f)
                        .put("Darz", 1f)
                        .put("Durnok", 1f)
                        .put("Fahd", 1f)
                        .put("Fjell", 1f)
                        .put("Gnudar", 1f)
                        .put("Grodax", 1f)
                        .put("Knogs", 1f)
                        .put("Knub", 1f)
                        .build()))
                .put(TENpcEntities.DEMOLITIONIST.getId(), NPCNames.of(ImmutableMap.<String, Float>builder()
                        .put("Bazdin", 1f)
                        .put("Beldin", 1f)
                        .put("Boften", 1f)
                        .put("Darur", 1f)
                        .put("Dias", 1f)
                        .put("Dolbere", 1f)
                        .put("Dolgen", 1f)
                        .put("Dolgrim", 1f)
                        .put("Duerthen", 1f)
                        .put("Durim", 1f)
                        .build()))
                .put(TENpcEntities.DRYAD.getId(), NPCNames.of(ImmutableMap.<String, Float>builder()
                        .put("Alalia", 1f)
                        .put("Alura", 1f)
                        .put("Ariella", 1f)
                        .put("Caelia", 1f)
                        .put("Calista", 1f)
                        .put("Celestia", 1f)
                        .put("Chryseis", 1f)
                        .put("Elysia", 1f)
                        .put("Emerenta", 1f)
                        .put("Evvie", 1f)
                        .build()))
                .put(TENpcEntities.DYE_TRADER.getId(), NPCNames.of(ImmutableMap.<String, Float>builder()
                        .put("Abdosir", 1f)
                        .put("Ahinadab", 1f)
                        .put("Ahirom", 1f)
                        .put("Akbar", 1f)
                        .put("Batnoam", 1f)
                        .put("Bodashtart", 1f)
                        .put("Danel", 1f)
                        .put("Hannibal", 1f)
                        .put("Hanno", 1f)
                        .put("Hiram", 1f)
                        .build()))
                .put(TENpcEntities.ANGLER.getId(), NPCNames.of(ImmutableMap.<String, Float>builder()
                        .put("Adam", 1f)
                        .put("Bart", 1f)
                        .put("Billy", 1f)
                        .put("Bobby", 1f)
                        .put("Charles", 1f)
                        .put("Danny", 1f)
                        .put("Grayson", 1f)
                        .put("Ivan", 1f)
                        .put("Izzy", 1f)
                        .put("Jey", 1f)
                        .build()))
                .put(TENpcEntities.FEMALE_ANGLER.getId(), NPCNames.of(ImmutableMap.<String, Float>builder()
                        .put("Emma", 1f)
                        .put("Olivia", 1f)
                        .put("Sophia", 1f)
                        .put("Ava", 1f)
                        .put("Isabella", 1f)
                        .put("Mia", 1f)
                        .put("Charlotte", 1f)
                        .put("Amelia", 1f)
                        .put("Harper", 1f)
                        .put("Luna", 1f)
                        .build()))
                .put(TENpcEntities.ARMS_DEALER.getId(), NPCNames.of(ImmutableMap.<String, Float>builder()
                        .put("Andre", 1f)
                        .put("Brimst", 1f)
                        .put("Bronson", 1f)
                        .put("Dante", 1f)
                        .put("Darius", 1f)
                        .put("Darnell", 1f)
                        .put("Darryl", 1f)
                        .put("DeAndre", 1f)
                        .put("DeShawn", 1f)
                        .put("Demetrius", 1f)
                        .build()))
                .build());
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
