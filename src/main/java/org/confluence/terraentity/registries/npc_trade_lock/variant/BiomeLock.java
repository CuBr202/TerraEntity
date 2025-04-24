package org.confluence.terraentity.registries.npc_trade_lock.variant;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.confluence.terraentity.entity.npc.trade.ITradeHolder;
import org.confluence.terraentity.registries.npc_trade_lock.ITradeLock;
import org.confluence.terraentity.registries.npc_trade_lock.TradeLockProvider;
import org.confluence.terraentity.registries.npc_trade_lock.TradeLockProviderTypes;

public record BiomeLock(ResourceLocation biomeId) implements ITradeLock  {

    public static final MapCodec<BiomeLock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("biome").forGetter(BiomeLock::biomeId)
    ).apply(instance, BiomeLock::new));

    @Override
    public boolean canTrade(Player player, ITradeHolder npc, int index) {
        return npc.level().getBiome(npc.blockPosition()).is(biomeId);
    }

    @Override
    public TradeLockProvider getCodec() {
        return TradeLockProviderTypes.BIOME_LOCK.get();
    }
}
