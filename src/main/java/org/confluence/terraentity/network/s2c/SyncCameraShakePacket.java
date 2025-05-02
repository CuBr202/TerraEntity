package org.confluence.terraentity.network.s2c;

import net.minecraft.network.FriendlyByteBuf;

import net.minecraftforge.network.NetworkEvent;
import org.confluence.terraentity.utils.CameraShakeData;
import org.confluence.terraentity.utils.CameraShakeManager;

import java.util.ArrayList;
import java.util.function.Supplier;

public class SyncCameraShakePacket{
    ArrayList<CameraShakeData> cameraShakeData;

    public SyncCameraShakePacket(ArrayList<CameraShakeData> cameraShakeData) {
        this.cameraShakeData = cameraShakeData;
    }

    public SyncCameraShakePacket(FriendlyByteBuf buf) {
        cameraShakeData = new ArrayList<>();
        int i = buf.readInt();
        for (int j = 0; j < i; j++) {
            cameraShakeData.add(CameraShakeData.deserializeFromBuffer(buf));
        }
    }

    public static SyncCameraShakePacket decode(FriendlyByteBuf buffer) {
        return new SyncCameraShakePacket(buffer);
    }

    public static void encode(SyncCameraShakePacket packet, FriendlyByteBuf buf) {
        buf.writeInt(packet.cameraShakeData.size());
        for (CameraShakeData data : packet.cameraShakeData)
            data.serializeToBuffer(buf);
    }

    public static void handle(SyncCameraShakePacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            CameraShakeManager.clientCameraShakeData = packet.cameraShakeData;
        });
        ctx.get().setPacketHandled(true);

    }

}