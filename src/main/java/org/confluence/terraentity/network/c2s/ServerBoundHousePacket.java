package org.confluence.terraentity.network.c2s;

import net.minecraft.network.FriendlyByteBuf;

import net.minecraft.network.chat.Component;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import org.confluence.terraentity.entity.npc.AbstractTerraNPC;
import org.confluence.terraentity.entity.npc.house.House;
import org.confluence.terraentity.entity.npc.house.HouseManager;
import org.confluence.terraentity.item.HouseDetectItem;
import org.confluence.terraentity.utils.AdapterUtils;

import java.util.UUID;
import java.util.function.Supplier;

public class ServerBoundHousePacket {

    public enum Action {
        ADD,
        DELETE,
        CHECK
    }
    Action action;
    House house;


    public ServerBoundHousePacket(Action action, House house) {
        this.action = action;
        this.house = house;
    }

    public ServerBoundHousePacket(FriendlyByteBuf buf) {
        this.action = Action.values()[buf.readByte()];
        this.house = new House(buf.readUUID().toString(), buf.readBlockPos(), buf.readBlockPos(), buf.readBlockPos());
    }

    public static ServerBoundHousePacket decode(FriendlyByteBuf buffer) {
        return new ServerBoundHousePacket(buffer);
    }

    public static void encode(ServerBoundHousePacket packet, FriendlyByteBuf buf) {
        House house = packet.house;
        buf.writeByte(packet.action.ordinal());
        buf.writeUUID(UUID.fromString(house.uuid()));
        buf.writeBlockPos(house.min());
        buf.writeBlockPos(house.max());
        buf.writeBlockPos(house.center());
    }

    public static void handle(ServerBoundHousePacket packet, Supplier<NetworkEvent.Context> ctx) {
        var context = ctx.get();
        context.enqueueWork(() -> {
            Player player = context.getSender();
            House house = packet.house;
            Action action = packet.action;
            ServerLevel level = (ServerLevel) player.level();
            UUID id = UUID.fromString(house.uuid());
            ItemStack stack = player.getMainHandItem();
            if(stack.getItem() instanceof HouseDetectItem item) {
                player.getCooldowns().addCooldown(item, 10);
            }else{
                return;
            }
            if(action == Action.CHECK){
                var existHouse = HouseManager.getInstance().isInsideHouse(house.center());
                if(existHouse != null){
                    var entity = level.getEntity(UUID.fromString(existHouse.uuid()));
                    if(entity !=null && entity.isAlive()) {
                        Component name = entity.getDisplayName();
                        if(name == null){
                            name = entity.getName();
                        }
                        player.sendSystemMessage(Component.translatable("tooltip.terra_entity.house_detect.mode.check.owner")
                                .append(": ").append(name)
                        );
                    }else{
                        HouseManager.getInstance().removeHouse(id);
                    }

                }

            } else if(action == Action.ADD){
                Entity entity = level.getEntity(id);
                if(entity instanceof AbstractTerraNPC npc){
                    if(HouseManager.getInstance().tryAddHouse(house)){
                        npc.setHouse(house);
                        player.sendSystemMessage(Component.translatable("tooltip.terra_entity.house_detect.mode.add.success"));
                    }else{
                        player.sendSystemMessage(Component.translatable("tooltip.terra_entity.house_detect.mode.add.failed"));
                    }
                }
            }else if(action == Action.DELETE){
                if(id.equals(player.getUUID())){
                    // 选中实体为空，则删除当前位置的房屋，有时候可能会出现实体死亡但没有刷新缓存，需要手动删除
                    HouseManager.getInstance().removeHouse(house.center());
                }else {
                    HouseManager.getInstance().removeHouse(id);
                }
                player.sendSystemMessage(Component.translatable("tooltip.terra_entity.house_detect.mode.delete.success"));
            }
        });
    }


    public static void sendAction(Action action, House house){
        AdapterUtils.sendToServer(new ServerBoundHousePacket(action, house));
    }
}
