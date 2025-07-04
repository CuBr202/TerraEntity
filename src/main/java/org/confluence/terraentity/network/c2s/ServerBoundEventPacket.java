package org.confluence.terraentity.network.c2s;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;
import org.confluence.terraentity.entity.boss.Skeletron;
import org.confluence.terraentity.entity.npc.AbstractTerraNPC;
import org.confluence.terraentity.init.entity.TEBossEntities;
import org.confluence.terraentity.init.entity.TENpcEntities;
import org.confluence.terraentity.mixed.IPlayer;
import org.confluence.terraentity.utils.AdapterUtils;
import org.confluence.terraentity.utils.TEUtils;

import java.util.function.Supplier;

public class ServerBoundEventPacket {
    private enum TypeEnum {
        SUMMON_SKELETRON
    }
    private final TypeEnum _type;


    ServerBoundEventPacket(TypeEnum type) {
        this._type = type;
    }

    ServerBoundEventPacket(FriendlyByteBuf buf) {
        this._type = buf.readEnum(TypeEnum.class);
    }


    public static ServerBoundEventPacket decode(FriendlyByteBuf buffer) {
        return new ServerBoundEventPacket(buffer);
    }

    public static void encode(ServerBoundEventPacket packet, FriendlyByteBuf buf) {
        buf.writeEnum(packet._type);
    }


    public static void handle(ServerBoundEventPacket packet, Supplier<NetworkEvent.Context> ctx) {
        var context = ctx.get();
        context.enqueueWork(() -> {
            Player player = context.getSender();
            TypeEnum type = packet._type;
            if(type == TypeEnum.SUMMON_SKELETRON) {
                if (player != null) {
                    Vec3 pos = player.position();
                    if (((IPlayer) player).terra_entity$getTradeHolder() instanceof AbstractTerraNPC npc && npc.getType() == TENpcEntities.OLD_MAN.get()) {
                        // confluence mixed here
                        npc.discard(); // 这样不会肢解，但是不会触发死亡事件所以需要mixin
                        Skeletron skeletron = TEBossEntities.SKELETRON.get().create(player.level());
                        if (skeletron != null) {
                            skeletron.setPos(pos.add(TEUtils.sphere(10, (float) Math.random() * 3.14F, (float) Math.random() * 3.14F)));
                            player.level().addFreshEntity(skeletron);
                        }
                    }
                }
            }
        });
    }


    public static void summonSkeletron(){
        AdapterUtils.sendToServer(new ServerBoundEventPacket(TypeEnum.SUMMON_SKELETRON));
    }
}
