package org.confluence.terraentity.entity.npc.trade;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import org.confluence.terraentity.api.npc.trade.ITradeModifier;

import java.io.IOException;
import java.io.Reader;
import java.util.*;

/**
 * 交易表修饰器：修改单个表项或者修改整个交易表{@link ITradeModifier}
 */
public class TradeModifiers {

    private static Map<ResourceLocation, List<ITradeModifier>> modifiersMap;
    public static final String KEY = "npc/trade_modifiers";
    public static final Codec<List<ITradeModifier>> CODEC = ITradeModifier.TYPED_CODEC.listOf();

    public Map<ResourceLocation, List<ITradeModifier>> getModifiers() {
        return modifiersMap;
    }

    public static void applyModifiers(NPCTradeManager trade, ResourceLocation location) {
        List<ITradeModifier> modifiers = TradeModifiers.modifiersMap.get(location);
        if(modifiers != null) {
            modifiers.sort(Comparator.comparing(ITradeModifier::priority));
            for (ITradeModifier modifier : modifiers) {
                modifier.accept(trade, location);
            }
        }
    }

    public static void readTradesFromJson(MinecraftServer server, HolderLookup.Provider registries) {
        ResourceManager manager = server.getResourceManager();
        RegistryOps<JsonElement> ops = registries.createSerializationContext(JsonOps.INSTANCE);
        modifiersMap = new HashMap<>();
        Map<ResourceLocation, Resource> jsons = manager.listResources(KEY, r -> r.getPath().endsWith(".json"));
        jsons.forEach((k, v) -> {
//            ResourceLocation id = TerraEntity.fromSpaceAndPath(k.getNamespace(),
//                    k.getPath().replace(".json", "").replace(KEY + "/", ""));
            try (Reader reader = v.openAsReader()) {
                JsonElement element = JsonParser.parseReader(reader);
                CODEC.parse(ops, element).ifSuccess(rl -> {
                    rl.forEach(r->{
                        ResourceLocation location = r.id();
                        if(!modifiersMap.containsKey(location)){
                            modifiersMap.put(location, new ArrayList<>());
                        }
                        List<ITradeModifier> list = modifiersMap.get(location);
                        list.add(r);
                    });
                }).ifError(err -> {
                    throw new RuntimeException("Failed to read modifier map " + k + " :" + err);
                });
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (NoSuchElementException e) {
                throw new RuntimeException("Failed to read modifier " + k, e);
            }
        });
    }
}
