package org.confluence.terraentity.registries.chat_condition;

import com.github.edg_thexu.cafelib.data.codec.LazyVarMapCodecProvider;
import com.mojang.serialization.MapCodec;
import org.confluence.terraentity.api.npc.chat.IChatCondition;

import java.util.function.Supplier;

public class ChatConditionProvider extends LazyVarMapCodecProvider<IChatCondition> {

    public ChatConditionProvider(Supplier<MapCodec<? extends IChatCondition>> mapCodecSupplier) {
        super(mapCodecSupplier);
    }
}