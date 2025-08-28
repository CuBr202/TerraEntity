package org.confluence.terraentity.data.codec;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.codecs.PrimitiveCodec;

import java.util.List;
import java.util.Locale;
import java.util.function.Function;

public class TECodecs {

    public static Codec<List<Integer>> INT_LIST_CODEC = Codec.INT.listOf();
    public static Codec<List<Float>> FLOAT_LIST_CODEC = Codec.FLOAT.listOf();
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

    /**
     * 修复Decode总是选择left的codec
     *
     * <p>{@link Codec#withAlternative(Codec, Codec)} 在Decode时总是选择left，这是不对的</p>
     * @param defaultCodec 默认的codec
     * @param alternativeCodec 备用codec
     * @param chooser 根据情况选择left或者right
     */
    public static <T> Codec<T> alternativeCodec(Codec<T> defaultCodec, Codec<T> alternativeCodec, Function<T, Either<T, T>> chooser) {
        return Codec.either(defaultCodec, alternativeCodec).xmap(Either::unwrap, chooser);
    }

}
