package org.confluence.terraentity.network.s2c;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import org.confluence.terraentity.mixed.IBossEvent;
import org.confluence.terraentity.mixed.IBossHealthOverlay;

import java.util.UUID;
import java.util.function.Supplier;

public class SyncBossEventHealthPacket {

    UUID uuid;
    float health;
    float maxHealth;

    public SyncBossEventHealthPacket(UUID uuid, float health , float maxHealth) {
        this.uuid = uuid;
        this.health = health;
        this.maxHealth = maxHealth;
    }

    public SyncBossEventHealthPacket(FriendlyByteBuf buf) {
        this.uuid = buf.readUUID();
        this.health = buf.readFloat();
        this.maxHealth = buf.readFloat();
    }


    public void write(FriendlyByteBuf buf) {
        buf.writeUUID(this.uuid);
        buf.writeFloat(this.health);
        buf.writeFloat(this.maxHealth);
    }

    public static SyncBossEventHealthPacket decode(FriendlyByteBuf buffer) {
        return new SyncBossEventHealthPacket(buffer);
    }

    public static void encode(SyncBossEventHealthPacket packet, FriendlyByteBuf buf) {
        buf.writeUUID(packet.uuid);
        buf.writeFloat(packet.health);
        buf.writeFloat(packet.maxHealth);
    }

    public static void handle(SyncBossEventHealthPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            try {
                IBossEvent bossEvent = (IBossEvent) ((IBossHealthOverlay) Minecraft.getInstance().gui.getBossOverlay()).terra_entity$getEvents().get(packet.uuid);
                bossEvent.terra_enity$setBossHealth(packet.health);
                bossEvent.terra_enity$setBossMaxHealth(packet.maxHealth);
            } catch (Exception ignored) {

            }

        });
        ctx.get().setPacketHandled(true);
    }

}
