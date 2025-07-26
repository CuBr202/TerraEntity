package org.confluence.terraentity.entity.npc.chat;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import org.confluence.terraentity.api.npc.chat.IChatElement;
import org.confluence.terraentity.api.entity.ai.ISkill;
import org.confluence.terraentity.entity.npc.AbstractTerraNPC;
import org.confluence.terraentity.api.npc.chat.IChatCondition;
import org.confluence.terraentity.registries.chat.variant.SpriteChatElement;

import java.util.List;

/**
 * 实际上npc持有的单个对话信息，包含对话内容、触发条件和对话效果
 */
public class ChatHolder implements ISkill {

    NPCChat chat;
    IChatCondition condition;
    int _maxCooldown;

    public static Codec<ChatHolder> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            NPCChat.CODEC.fieldOf("chat").forGetter(ChatHolder::getChat),
            IChatCondition.TYPE_CODEC.fieldOf("condition").forGetter(ChatHolder::getCondition),
            Codec.INT.fieldOf("maxCooldown").forGetter(ChatHolder::_maxCooldown)
    ).apply(instance, ChatHolder::new));

    private Integer _maxCooldown() {
        return _maxCooldown;
    }

    int cooldown;
    AbstractTerraNPC owner;

    public ChatHolder(NPCChat chat, IChatCondition condition, int maxCooldown) {
        this.chat = chat;
        this.condition = condition;
        this._maxCooldown = maxCooldown;
        this.cooldown = maxCooldown;
    }

    public ChatHolder(List<IChatElement> chatElements, IChatCondition condition, int maxCooldown){
        this(new NPCChat(chatElements), condition, maxCooldown);
    }

    /**
     * 单个npc表情
     * @param sprites 表情动图
     * @param condition 触发条件
     * @param maxCooldown 冷却时间
     */
    public static ChatHolder NPCEmoji(List<ResourceLocation> sprites, IChatCondition condition, int maxCooldown){
        return new ChatHolder(new NPCChat(List.of(new SpriteChatElement(sprites, 2f))), condition, maxCooldown);
    }

    public NPCChat getChat() {
        return chat;
    }

    public IChatCondition getCondition() {
        return condition;
    }

    public boolean canChat(AbstractTerraNPC npc, ChatHolder chatHolder) {
        return condition.canChat(npc, chatHolder);
    }

    public void setOwner(AbstractTerraNPC owner) {
        this.owner = owner;
    }


    @Override
    public int getCooldown() {
        return cooldown;
    }

    @Override
    public void update(int delta) {
        this.cooldown -= delta;
    }

    @Override
    public void reset() {
        if(this.owner!= null){
            this.cooldown = (int) (_maxCooldown * (1 + this.owner.getRandom().nextFloat() * 0.5f));
        } else{
            this.cooldown = this._maxCooldown;
        }
    }



}
