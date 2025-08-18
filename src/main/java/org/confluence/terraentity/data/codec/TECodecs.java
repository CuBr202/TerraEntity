package org.confluence.terraentity.data.codec;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.codecs.PrimitiveCodec;

import java.util.Locale;

public class TECodecs {
    public static final Codec<Integer> INT_KEY = new PrimitiveCodec<>() {
        @Override
        public <T> DataResult<Integer> read(DynamicOps<T> ops, T input) {
            return ops.getStringValue(input).map(Integer::parseInt);
        }

        @Override
        public <T> T write(DynamicOps<T> ops, Integer value) {
            return ops.createString(value.toString());
        }
    };


    public static<T extends Enum<T>> Codec<T> createEnumCodec(Class<T> enumClass) {
        return Codec.STRING.xmap(
                name->Enum.valueOf(enumClass, name.toUpperCase()),
                baker-> baker.name().toLowerCase(Locale.ROOT)
        );
    }

}
