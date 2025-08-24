package org.confluence.terraentity.registries.mappeddata;

import com.mojang.serialization.Codec;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * 键类型
 * @param <V> 指示值的类型，便于编译器检查
 */
public class MappedKey<V> implements IAutoReloadable<V> {
    final String key;
    Consumer<V> onReload;
    Supplier<V> defaultValue;


    public MappedKey(String key) {
        this.key = key;
    }
    public MappedKey(ResourceLocation key) {
        this.key = key.toString();
    }

    public MappedKey<V> withOnReload(Consumer<V> onReload) {
        this.onReload = onReload;
        return this;
    }

    public MappedKey<V> withDefaultValue(Supplier<V> defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }

    public static Codec<MappedKey<?>> CODEC = Codec.STRING.xmap(MappedKey::new, MappedKey::getKey);

    public String getKey() {
        return key;
    }

    @Override
    public String toString() {
        return key;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        MappedKey<?> other = (MappedKey<?>) obj;
        return key.equals(other.key);
    }

    @Override
    public int hashCode() {
        return key.hashCode();
    }

    @Override
    public void onReload(Object value) {
        if (onReload != null) {
            onReload.accept((V) value);
        }
    }

    @Override
    public Supplier<V> defaultValue() {
        if (defaultValue == null) {
            return () -> null;
        }
        return defaultValue;
    }


}
