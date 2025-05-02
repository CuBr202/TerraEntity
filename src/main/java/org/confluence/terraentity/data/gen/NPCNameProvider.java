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
                        "Andrew", 1f,
                        "Asher", 1f,
                        "Bradley", 1f,
                        "Brandon", 1f,
                        "Brett", 1f,
                        "Brian", 1f,
                        "Cody", 1f,
                        "Cole", 1f,
                        "Colin", 1f,
                        "Connor", 1f
                )),
                TENpcEntities.NURSE.getId(),
                NPCNames.of(Map.of(
                        "Abigail", 1f,
                "Allison", 1f,
                "Amy", 1f,
                "Caitlin", 1f,
                "Carly", 1f,
                "Claire", 1f,
                "Emily", 1f,
                "Emma", 1f,
                "Hannah", 1f,
                "Heather", 1f
                )),
                TENpcEntities.PAINTER.getId(),
                NPCNames.of(Map.of(
                "Bruno", 1f,
                "Carlo", 1f,
                "Darren", 1f,
                "Enzo", 1f,
                "Esreadel", 1f,
                "Guido", 1f,
                "Jim", 1f,
                "Leonardo", 1f,
                "Lorenzo", 1f,
                "Luca", 1f
                )),
                TENpcEntities.MERCHANT.getId(),
                NPCNames.of(Map.of(
                "Alfred", 1f,
                "Barney", 1f,
                "Calvin", 1f,
                "Edmund", 1f,
                "Edwin", 1f,
                "Eugene", 1f,
                "Frank", 1f,
                "Frederick", 1f,
                "Gilbert", 1f,
                "Gus", 1f
                )),
                TENpcEntities.GOBLIN_TINKERER.getId(),
                NPCNames.of(Map.of(
                "Arback", 1f,
                "Dalek", 1f,
                "Darz", 1f,
                "Durnok", 1f,
                "Fahd", 1f,
                "Fjell", 1f,
                "Gnudar", 1f,
                "Grodax", 1f,
                "Knogs", 1f,
                "Knub", 1f
                )),
                TENpcEntities.DEMOLITIONIST.getId(),
                NPCNames.of(Map.of(
                "Bazdin", 1f,
                "Beldin", 1f,
                "Boften", 1f,
                "Darur", 1f,
                "Dias", 1f,
                "Dolbere", 1f,
                "Dolgen", 1f,
                "Dolgrim", 1f,
                "Duerthen", 1f,
                "Durim", 1f
                )),
                TENpcEntities.DRYAD.getId(),
                NPCNames.of(Map.of(
                "Alalia", 1f,
                "Alura", 1f,
                "Ariella", 1f,
                "Caelia", 1f,
                "Calista", 1f,
                "Celestia", 1f,
                "Chryseis", 1f,
                "Elysia", 1f,
                "Emerenta", 1f,
                "Evvie", 1f
                )),
                TENpcEntities.DYE_TRADER.getId(),
                NPCNames.of(Map.of(
                "Abdosir", 1f,
                "Ahinadab", 1f,
                "Ahirom", 1f,
                "Akbar", 1f,
                "Batnoam", 1f,
                "Bodashtart", 1f,
                "Danel", 1f,
                "Hannibal", 1f,
                "Hanno", 1f,
                "Hiram", 1f
                )),
                TENpcEntities.ANGLER.getId(),
                NPCNames.of(Map.of(
                "Adam", 1f,
                "Bart", 1f,
                "Billy", 1f,
                "Bobby", 1f,
                "Charles", 1f,
                "Danny", 1f,
                "Grayson", 1f,
                "Ivan", 1f,
                "Izzy", 1f,
                "Jey", 1f
                )),
                TENpcEntities.ARMS_DEALER.getId(),
                NPCNames.of(Map.of(
                "Andre", 1f,
                "Brimst", 1f,
                "Bronson", 1f,
                "Dante", 1f,
                "Darius", 1f,
                "Darnell", 1f,
                "Darryl", 1f,
                "DeAndre", 1f,
                "DeShawn", 1f,
                "Demetrius", 1f
                ))
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
