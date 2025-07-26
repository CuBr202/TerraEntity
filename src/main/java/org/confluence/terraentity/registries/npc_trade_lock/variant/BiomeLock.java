package org.confluence.terraentity.registries.npc_trade_lock.variant;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.biome.Biome;
import org.confluence.terraentity.api.npc.trade.ITradeHolder;
import org.confluence.terraentity.api.npc.trade.ITradeLock;
import org.confluence.terraentity.registries.npc_trade_lock.TradeLockProvider;
import org.confluence.terraentity.registries.npc_trade_lock.TradeLockProviderTypes;

import java.util.List;
import java.util.Optional;

public record BiomeLock(Optional<List<ResourceKey<Biome>>> values, Optional<List<TagKey<Biome>>> tags) implements ITradeLock {
    public static final MapCodec<BiomeLock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ResourceKey.codec(Registries.BIOME).listOf().lenientOptionalFieldOf("values").forGetter(BiomeLock::values),
            TagKey.codec(Registries.BIOME).listOf().lenientOptionalFieldOf("tags").forGetter(BiomeLock::tags)
    ).apply(instance, BiomeLock::new));

    @Override
    public boolean canTrade(Player player, ITradeHolder npc, int index) {
        Holder<Biome> biome = npc.level().getBiome(npc.blockPosition());
        return (values.isEmpty() || values.get().stream().anyMatch(biome::is)) && (tags.isEmpty() || tags.get().stream().anyMatch(biome::is));
    }

    @Override
    public TradeLockProvider getCodec() {
        return TradeLockProviderTypes.BIOME_LOCK.get();
    }
}
