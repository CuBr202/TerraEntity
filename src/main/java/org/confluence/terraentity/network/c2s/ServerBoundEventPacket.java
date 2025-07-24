package org.confluence.terraentity.network.c2s;

import com.google.common.collect.ImmutableMap;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.api.item.ILeftClickStateItem;
import org.confluence.terraentity.entity.boss.Skeletron;
import org.confluence.terraentity.entity.npc.AbstractTerraNPC;
import org.confluence.terraentity.init.TEAttachments;
import org.confluence.terraentity.init.entity.TEBossEntities;
import org.confluence.terraentity.init.entity.TENpcEntities;
import org.confluence.terraentity.mixed.IPlayer;
import org.confluence.terraentity.utils.AdapterUtils;
import org.confluence.terraentity.utils.TEUtils;

import java.util.EnumMap;
import java.util.function.Consumer;

public class ServerBoundEventPacket implements CustomPacketPayload{
    private enum TypeEnum {
        SUMMON_SKELETRON, // 由于mixin，不能添加到handler
        MOUSE_LEFT_CLICK,
        MOUSE_RELEASE
    }
    static EnumMap<TypeEnum, Consumer<Player>> handlers = new EnumMap<>(ImmutableMap.<TypeEnum, Consumer<Player>>builder()
            .put(TypeEnum.MOUSE_LEFT_CLICK, (player)-> {
                player.getData(TEAttachments.WEAPON_STORAGE.get()).leftClicking = true;
                ItemStack stack = player.getMainHandItem();
                if(stack.getItem() instanceof ILeftClickStateItem item){
                    item.onLeftClick(player, stack);
                }
            })
            .put(TypeEnum.MOUSE_RELEASE, (player)-> {
                player.getData(TEAttachments.WEAPON_STORAGE.get()).leftClicking = false;
                ItemStack stack = player.getMainHandItem();
                if(stack.getItem() instanceof ILeftClickStateItem item){
                    item.onLeftRelease(player, stack);
                }
            })

            .build());

    private final TypeEnum _type;

    public static final Type<ServerBoundEventPacket> TYPE = new Type<>(TerraEntity.fromSpaceAndPath(TerraEntity.MODID, "server_bound_event_packet"));
    public static final StreamCodec<RegistryFriendlyByteBuf, ServerBoundEventPacket> STREAM_CODEC = CustomPacketPayload.codec(ServerBoundEventPacket::write, ServerBoundEventPacket::new);

    ServerBoundEventPacket(TypeEnum type) {
        this._type = type;
    }

    ServerBoundEventPacket(FriendlyByteBuf buf) {
        this._type = buf.readEnum(TypeEnum.class);
    }


    public void write(FriendlyByteBuf buf) {
        buf.writeEnum(_type);
    }

    public static void handle(ServerBoundEventPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            Player player = context.player();
            TypeEnum type = packet._type;
            if (handlers.containsKey(type)) {
                handlers.get(type).accept(player);
            } else if (type == TypeEnum.SUMMON_SKELETRON) {
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

        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void summonSkeletron(){
        AdapterUtils.sendToServer(new ServerBoundEventPacket(TypeEnum.SUMMON_SKELETRON));
    }

    public static void mouseLeftClick(){
        AdapterUtils.sendToServer(new ServerBoundEventPacket(TypeEnum.MOUSE_LEFT_CLICK));
    }

    public static void mouseRelease(){
        AdapterUtils.sendToServer(new ServerBoundEventPacket(TypeEnum.MOUSE_RELEASE));
    }
}
