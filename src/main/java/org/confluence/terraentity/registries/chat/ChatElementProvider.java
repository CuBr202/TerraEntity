package org.confluence.terraentity.registries.chat;

import com.mojang.serialization.MapCodec;
import org.confluence.terraentity.api.chat.IChatElement;

public record ChatElementProvider(MapCodec<? extends IChatElement> codec) {
}
