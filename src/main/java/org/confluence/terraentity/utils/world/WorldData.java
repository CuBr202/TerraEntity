package org.confluence.terraentity.utils.world;



import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class WorldData {
    protected final ConcurrentHashMap<ChunkPos, ChunkData> chunkData = new ConcurrentHashMap<>();
    private final ServerLevel world;
    public WorldData(ServerLevel world) {
        this.world = world;
    }

    public Optional<ChunkData> getChunkData(ChunkPos pos) {
        return Optional.ofNullable(chunkData.get(pos));
    }

    public Collection<ChunkData> getChunks() {
        return chunkData.values();
    }

    public ChunkData addForcedChunk(ChunkData chunkData) {
        return this.chunkData.put(chunkData.pos, chunkData);
    }

    public ChunkData unloadChunk(ChunkPos pos) {
        ChunkData data = this.chunkData.remove(pos);
        if (data != null) {
            this.world.setChunkForced(pos.x, pos.z, false);
        }
        return data;
    }
}