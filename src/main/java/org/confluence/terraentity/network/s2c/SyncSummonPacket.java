package org.confluence.terraentity.network.s2c;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.init.TEAttachments;

public class SyncSummonPacket implements CustomPacketPayload {

    int currentCapability;

//    List<Integer> indexList;
    public static final CustomPacketPayload.Type<SyncSummonPacket> TYPE = new CustomPacketPayload.Type<>(TerraEntity.asResource(TerraEntity.MODID, "sync_summon_packet"));
    public static final StreamCodec<RegistryFriendlyByteBuf, SyncSummonPacket> STREAM_CODEC = CustomPacketPayload.codec(SyncSummonPacket::write, SyncSummonPacket::new);

    public SyncSummonPacket(int currentCapability) {
        this.currentCapability = currentCapability;

//        this.indexList = indexList;
    }

    public SyncSummonPacket(FriendlyByteBuf buf) {
        this.currentCapability = buf.readInt();

//        this.indexList = new LinkedList<>(Arrays.stream(buf.readVarIntArray()).boxed().toList());
    }


    public void write(FriendlyByteBuf buf) {
        buf.writeInt(currentCapability);

//        buf.writeVarIntArray(indexList.stream().mapToInt(Integer::intValue).toArray());

    }

    public static void handle(SyncSummonPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            var data = context.player().getData(TEAttachments.SUMMONER_STORAGE.get());
            data.setCurrentCapacity(packet.currentCapability);
//            data.setIds(packet.indexList);
        });
    }

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
    
    
}
