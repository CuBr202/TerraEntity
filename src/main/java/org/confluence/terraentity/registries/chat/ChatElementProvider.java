package org.confluence.terraentity.registries.chat;

import com.github.edg_thexu.cafelib.data.codec.LazyVarMapCodecProvider;
import com.mojang.serialization.MapCodec;
import org.confluence.terraentity.api.npc.chat.IChatElement;

import java.util.function.Supplier;

public class ChatElementProvider extends LazyVarMapCodecProvider<IChatElement>{
    public ChatElementProvider(Supplier<MapCodec<? extends IChatElement>> mapCodecSupplier) {
        super(mapCodecSupplier);
    }
}
