package org.confluence.terraentity.entity.npc.house;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

/**
 * 房子类,未来可能扩展不同的房子类型
 *
 * @param uuid   房子的uuid == 实体的uuid
 * @param min    左下角的坐标
 * @param max    右上角的坐标
 * @param center 房子的中心坐标
 */
public record House(Optional<UUID> uuid, BlockPos min, BlockPos max, BlockPos center) {
    public static final String KEY = "npc_house";
    public static final House EMPTY = new House(Optional.empty(), BlockPos.ZERO, BlockPos.ZERO, BlockPos.ZERO);

    public boolean isEmpty() {
        return uuid.isEmpty();
    }

    public boolean contains(BlockPos pos) {
        return pos.getX() >= min.getX() && pos.getX() <= max.getX() && pos.getZ() >= min.getZ() && pos.getZ() <= max.getZ();
    }

    public static final Codec<House> CODEC = RecordCodecBuilder.create((builder) -> builder.group(
            Codec.either(Codec.STRING, UUIDUtil.CODEC).xmap(
                    either -> either.map(UUID::fromString, Function.identity()), Either::right
            ).optionalFieldOf("uuid").forGetter(House::uuid),
            BlockPos.CODEC.fieldOf("min").forGetter(House::min),
            BlockPos.CODEC.fieldOf("max").forGetter(House::max),
            BlockPos.CODEC.fieldOf("center").forGetter(House::center)
    ).apply(builder, House::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, House> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.optional(UUIDUtil.STREAM_CODEC), House::uuid,
            BlockPos.STREAM_CODEC, House::min,
            BlockPos.STREAM_CODEC, House::max,
            BlockPos.STREAM_CODEC, House::center,
            House::new
    );
}
