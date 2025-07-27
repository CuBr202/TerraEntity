package org.confluence.terraentity.entity.npc.chat;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.api.entity.ai.ISkill;
import org.confluence.terraentity.entity.ai.goal.skill.SkillCooldownManager;
import org.confluence.terraentity.entity.npc.AbstractTerraNPC;

import java.io.IOException;
import java.io.Reader;
import java.util.*;

/**
 * NPC 对话管理器
 */
public class ChatManager extends SkillCooldownManager {

    List<ChatHolder> chatHolders;
    ToTypeChat toOtherChat;

    public static Codec<ChatManager> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.list(ChatHolder.CODEC).fieldOf("chatHolders").forGetter(ChatManager::getChatHolders),
            ToTypeChat.CODEC.optionalFieldOf("toOtherChat").forGetter(ChatManager::getToOtherChat)
    ).apply(instance, (chatHolders, toOtherChat)-> new ChatManager(chatHolders, toOtherChat.orElse(null))));

//            ChatHolder.CODEC.listOf().xmap(ChatManager::new, ChatManager::getChatHolders);

    public Optional<ToTypeChat> getToOtherChat() {
        if(this.toOtherChat!= null){
            return Optional.of(this.toOtherChat);
        }
        return Optional.empty();
    }

    private List<ChatHolder> getChatHolders() {
        return chatHolders;
    }

    AbstractTerraNPC owner;
    int forceCooldown = 50;

    public ChatManager(List<ChatHolder> chatHolders) {
        this.chatHolders = chatHolders;
        chatHolders.forEach(this::addSkill);
    }

    public ChatManager(List<ChatHolder> chatHolders, ToTypeChat toOtherChat) {
        this.chatHolders = chatHolders;
        this.toOtherChat = toOtherChat;
    }


    public void setOwner(AbstractTerraNPC owner) {
        this.owner = owner;
        this.chatHolders.forEach(chatHolder -> chatHolder.setOwner(owner));
    }

    @Override
    public void update(int deltaTime) {
        -- this.forceCooldown;
        if(this.owner == null){
            return;
        }
        super.update(deltaTime);
        for(ChatHolder chatHolder : chatHolders){
            if(chatHolder.canChat(owner, chatHolder)){
                this.triggerSkill(chatHolder);
            }
        }

    }

    @Override
    public boolean canTriggerSkill(ISkill skill) {
        if(this.forceCooldown > 0){
            return false;
        }
        if(super.canTriggerSkill(skill)){
            return true;
        }
        this.exchangeQueue();
        return false;
    }


    @Override
    public boolean triggerSkill(ISkill skill) {
        if(super.triggerSkill(skill)){
            this.forceCooldown = 50;
            this.owner.setChat(((ChatHolder) skill).getChat());
            return true;
        }
        return false;
    }


    public static final String KEY = "npc/chat";

    public static final Map<ResourceLocation, JsonElement> CHAT_MAP = new HashMap<>();

    public static ChatManager getChatManager(ResourceLocation id, RegistryAccess registries) {
        var ops = registries.createSerializationContext(JsonOps.INSTANCE);
        if(CHAT_MAP.containsKey(id)){
            var res =  ChatManager.CODEC.parse(ops, CHAT_MAP.get(id));
            if(res.result().isPresent()){
                return res.result().get();
            }else if(res.error().isPresent()){
                TerraEntity.LOGGER.error("Failed to parse chat list {} : {}", id, res.error().get().message());
                return null;
            }
        }
        return null;
    }

    public static void readChatsFromJson(MinecraftServer server, HolderLookup.Provider registries) {
        ResourceManager manager = server.getResourceManager();
//        RegistryOps<JsonElement> ops = registries.createSerializationContext(JsonOps.INSTANCE);

        Map<ResourceLocation, Resource> jsons = manager.listResources(KEY, r -> r.getPath().endsWith(".json"));
        jsons.forEach((k, v) -> {
            ResourceLocation id = TerraEntity.fromSpaceAndPath(k.getNamespace(),
                    k.getPath().replace(".json", "").replace(KEY + "/", ""));
            try (Reader reader = v.openAsReader()) {
                JsonElement element = JsonParser.parseReader(reader);
                CHAT_MAP.put(id, element);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (NoSuchElementException e) {
                throw new RuntimeException("Failed to read chat list " + k, e);
            }
        });
    }

}
