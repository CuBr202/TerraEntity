package org.confluence.terraentity.data.gen;

import com.mojang.serialization.Codec;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.entity.npc.NPCNames;
import org.confluence.terraentity.init.entity.TENpcEntities;

import java.util.Map;

public class NPCNameProvider extends AbstractExistCodecProvider<Map<ResourceLocation, NPCNames>> {

    public NPCNameProvider(PackOutput output) {
        super(output);
    }

    @Override
    protected void run() {
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
