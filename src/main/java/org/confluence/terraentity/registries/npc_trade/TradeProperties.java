package org.confluence.terraentity.registries.npc_trade;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import org.confluence.terraentity.registries.npc_trade_lock.ITradeLock;

import java.util.Optional;

public record TradeProperties(ITradeLock lock) {


    public static final Codec<TradeProperties> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ITradeLock.TYPED_CODEC.optionalFieldOf("lock").forGetter(i-> Optional.ofNullable(i.lock))

    ).apply(instance, (lock)-> new TradeProperties(
            lock.orElse(null)
    )));



    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private ITradeLock lock;
        public Builder setLock(ITradeLock lock) {
            this.lock = lock;
            return this;
        }
        public TradeProperties build() {
            return new TradeProperties(lock);
        }
    }
}
