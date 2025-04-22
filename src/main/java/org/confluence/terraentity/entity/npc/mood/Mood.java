package org.confluence.terraentity.entity.npc.mood;

import com.mojang.serialization.Codec;

public enum Mood {
    LOVER,
    LIKE,
    NEUTRAL,
    HATE,
    DISLIKE;

    public static Codec<Mood> CODEC = Codec.STRING.xmap(Mood::valueOf, Mood::toString);
}
