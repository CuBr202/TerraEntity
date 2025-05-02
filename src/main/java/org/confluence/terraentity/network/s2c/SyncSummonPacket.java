package org.confluence.terraentity.network.s2c;


import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.init.TEAttachments;

import java.util.function.Supplier;

public class SyncSummonPacket{

    int currentCapability;

//    List<Integer> indexList;

    public SyncSummonPacket(int currentCapability) {
        this.currentCapability = currentCapability;

//        this.indexList = indexList;
    }

    public SyncSummonPacket(FriendlyByteBuf buf) {
        this.currentCapability = buf.readInt();

//        this.indexList = new LinkedList<>(Arrays.stream(buf.readVarIntArray()).boxed().toList());
    }

    public static SyncSummonPacket decode(FriendlyByteBuf buf) {
        SyncSummonPacket packet = new SyncSummonPacket(buf);
        return packet;
    }


    public static void encode(SyncSummonPacket packet, FriendlyByteBuf buf) {
        buf.writeInt(packet.currentCapability);

//        buf.writeVarIntArray(indexList.stream().mapToInt(Integer::intValue).toArray());

    }

    public static void handle(SyncSummonPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            var data = Minecraft.getInstance().player.getCapability(TEAttachments.SUMMONER_STORAGE).resolve().get();
            data.setCurrentCapacity(packet.currentCapability);
//            data.setIds(packet.indexList);
        });
        ctx.get().setPacketHandled(true);
    }


    
}
