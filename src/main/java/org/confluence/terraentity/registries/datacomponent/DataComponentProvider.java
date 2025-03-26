package org.confluence.terraentity.registries.datacomponent;

import com.mojang.serialization.Codec;

import java.util.function.Supplier;

public record DataComponentProvider<T extends IDataComponentType<T>>(String name, Supplier<Codec<T>> codec) {
}
