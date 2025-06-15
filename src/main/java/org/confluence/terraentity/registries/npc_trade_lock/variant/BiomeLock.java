package org.confluence.terraentity.registries.npc_trade_lock.variant;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.biome.Biome;
import net.neoforged.neoforge.registries.holdersets.AndHolderSet;
import net.neoforged.neoforge.registries.holdersets.NotHolderSet;
import net.neoforged.neoforge.registries.holdersets.OrHolderSet;
import org.confluence.terraentity.entity.npc.trade.ITradeHolder;
import org.confluence.terraentity.registries.npc_trade_lock.ITradeLock;
import org.confluence.terraentity.registries.npc_trade_lock.TradeLockProvider;
import org.confluence.terraentity.registries.npc_trade_lock.TradeLockProviderTypes;

public record BiomeLock(HolderSet<Biome> biomes) implements ITradeLock {
    public static final MapCodec<BiomeLock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            RegistryCodecs.homogeneousList(Registries.BIOME).fieldOf("biomes").forGetter(BiomeLock::biomes)
    ).apply(instance, BiomeLock::new));

    @SafeVarargs
    public static BiomeLock and(HolderSet<Biome>... biomeSets) {
        return new BiomeLock(new AndHolderSet<>(biomeSets));
    }

    @SafeVarargs
    public static BiomeLock or(HolderSet<Biome>... biomeSets) {
        return new BiomeLock(new OrHolderSet<>(biomeSets));
    }

    public static BiomeLock not(HolderLookup.RegistryLookup<Biome> lookup, HolderSet<Biome> biomes) {
        return new BiomeLock(new NotHolderSet<>(lookup, biomes));
    }

    @Override
    public boolean canTrade(Player player, ITradeHolder npc, int index) {
        return biomes.contains(npc.level().getBiome(npc.blockPosition()));
    }

    @Override
    public TradeLockProvider getCodec() {
        return TradeLockProviderTypes.BIOME_LOCK.get();
    }
}
