package org.confluence.terraentity.entity.npc;

import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.GsonHelper;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.api.event.LoadResourceEvent;
import org.confluence.terraentity.utils.AdapterUtils;
import org.confluence.terraentity.utils.TEUtils;

import javax.annotation.Nullable;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

public record NPCNames(Map<String, Float> namesWeights) {
    public static final String KEY = "npc_names";
    public static final String FILE_NAME = "names";

    private static final Map<ResourceLocation, NPCNames> names_map = new HashMap<>();

    public static final Codec<NPCNames> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.unboundedMap(Codec.STRING, Codec.FLOAT).fieldOf("names_weights").forGetter(NPCNames::namesWeights)
    ).apply(instance, NPCNames::new));

    public static final Codec<Map<ResourceLocation, NPCNames>> MAP_CODEC = Codec.unboundedMap(ResourceLocation.CODEC, CODEC);

    public static NPCNames getNames(ResourceLocation id) {
        return names_map.get(id);
    }

    public static @Nullable String getRandomName(ResourceLocation id) {
        NPCNames names = names_map.get(id);
        if(names == null || names.namesWeights.isEmpty()){
            return null;
        }
        return TEUtils.getRandomByWeight(names.namesWeights);
    }

    public static NPCNames of(Map<String, Float> names){
        return new NPCNames(names);
    }

    public static void clear(){
        names_map.clear();
    }

    public static void loadNPCNames(ResourceManager manager) {
        clear();
        LoadResourceEvent event = new LoadResourceEvent(LoadResourceEvent.Type.NPC_NAMES);
        AdapterUtils.postEvent(event);
        if(!event.isCanceled()) {
            if(!event.isReplace()) {
                ResourceLocation defaultFile = TerraEntity.space(KEY + "/" + FILE_NAME + ".json");
                readNamesFromJson(manager, defaultFile);
            }
            for(ResourceLocation file : event.getFiles()) {
                readNamesFromJson(manager, file);
            }
        }
    }

    public static void readNamesFromJson(ResourceManager manager, ResourceLocation file) {
        manager.getResource(file).ifPresentOrElse(
            resource -> {
                try {
                    Reader reader = resource.openAsReader();
                    JsonObject jsonobject = GsonHelper.parse(reader);
                    Map<ResourceLocation, NPCNames> map = MAP_CODEC.decode(JsonOps.INSTANCE, jsonobject).result().get().getFirst();
                    for (Map.Entry<ResourceLocation, NPCNames> entry : map.entrySet()) {
                        ResourceLocation id = entry.getKey();
                        NPCNames other = entry.getValue();
                        if(names_map.containsKey(id)){
                            var has = names_map.get(id).namesWeights;
                            // 替换已有NPC名称权重
                            for(String name : other.namesWeights.keySet()){
                                float weight = other.namesWeights.get(name);
                                has.put(name, weight);
                            }
                        }else{
                            names_map.put(id, new NPCNames(new HashMap<>(other.namesWeights)));
                        }
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            },
            ()->{
                TerraEntity.LOGGER.warn("No trade data found for NPCs");
            }
        );
        TerraEntity.LOGGER.info("Loaded {} NPC names", names_map.size());
    }
}
