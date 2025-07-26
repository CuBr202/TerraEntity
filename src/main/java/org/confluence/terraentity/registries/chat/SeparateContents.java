package org.confluence.terraentity.registries.chat;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.network.chat.ComponentContents;
import org.jetbrains.annotations.NotNull;

@Deprecated
public class SeparateContents implements ComponentContents {

    static final SeparateContents instance = new SeparateContents();

    public static final MapCodec<SeparateContents> CODEC = Codec.unit(SeparateContents::new).fieldOf("separate");
    public static final ComponentContents.Type<SeparateContents> TYPE = new ComponentContents.Type<>(CODEC, "separate");


    public SeparateContents() {
    }

    public static SeparateContents getInstance() {
        return instance;
    }



    @Override
    public String toString() {
        return "{separate}";
    }

    @Override
    public ComponentContents.@NotNull Type<?> type() {
        return TYPE;
    }
}
