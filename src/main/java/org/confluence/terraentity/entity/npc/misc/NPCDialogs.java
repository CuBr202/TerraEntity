package org.confluence.terraentity.entity.npc.misc;

import com.google.gson.JsonElement;
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

import javax.annotation.Nullable;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public record NPCDialogs(List<String> dialogs) {
    public static final String KEY = "npc_dialog";
    public static final String FILE_NAME = "dialogs";

    private static final Map<ResourceLocation, NPCDialogs> dialog_map = new HashMap<>();

    public static final Codec<NPCDialogs> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.listOf().fieldOf("dialogs").forGetter(NPCDialogs::dialogs)
    ).apply(instance, NPCDialogs::new));

    public static final Codec<Map<ResourceLocation, NPCDialogs>> MAP_CODEC = Codec.unboundedMap(ResourceLocation.CODEC, CODEC);

    public static @Nullable String getRandomDialog(ResourceLocation id) {
        NPCDialogs dialogs1 = dialog_map.get(id);
        if(dialogs1 == null || dialogs1.dialogs.isEmpty()){
            return null;
        }
        int index = (int) (Math.random() * dialogs1.dialogs.size());
        return dialogs1.dialogs.get(index);
    }

    public static NPCDialogs of(List<String> dialogs){
        return new NPCDialogs(dialogs);
    }

    public static Map<ResourceLocation, NPCDialogs> getDialog_map(){
        return dialog_map;
    }

    public static void clear(){
        dialog_map.clear();
    }

    public static void loadNPCDialogs(ResourceManager manager) {
        clear();
        LoadResourceEvent event = new LoadResourceEvent(LoadResourceEvent.Type.NPC_DIALOGS);
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
                        Map<ResourceLocation, NPCDialogs> map = MAP_CODEC.decode(JsonOps.INSTANCE, jsonobject).result().get().getFirst();
                        for (Map.Entry<ResourceLocation, NPCDialogs> entry : map.entrySet()) {
                            ResourceLocation id = entry.getKey();
                            NPCDialogs other = entry.getValue();

                            // 处理对话
                            if(dialog_map.containsKey(id)){
                                var has = dialog_map.get(id);
                                for(String dialog : other.dialogs){
                                    if(!has.dialogs.contains(dialog)){
                                        has.dialogs.add(dialog);
                                    }
                                }
                            }else{
                                dialog_map.put(id, new NPCDialogs(new ArrayList<>(other.dialogs)));
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
        TerraEntity.LOGGER.info("Loaded {} NPC names", dialog_map.size());
    }

    public static void loadFromServer(JsonElement json){
        dialog_map.clear();
        MAP_CODEC.decode(JsonOps.INSTANCE, json).result().ifPresent(res->{
            dialog_map.putAll(res.getFirst());
        });
    }
}
