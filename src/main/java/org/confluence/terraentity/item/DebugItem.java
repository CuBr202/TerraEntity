package org.confluence.terraentity.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.confluence.terraentity.entity.proj.ThrowableProj;
import org.confluence.terraentity.init.TEEntities;
import org.confluence.terraentity.utils.Gomoku;

import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

public class DebugItem extends Item {


    public DebugItem(Properties properties) {
        super(properties);
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        if(!level.isClientSide){
            ThrowableProj proj = TEEntities.CABBAGE_PROJ.get().create(level);
            Vec3 tar = player.getEyePosition().add(player.getForward().normalize().scale(10));
            float f = player.getRandom().nextFloat() * 5 + 5;
            boolean d = player.getRandom().nextBoolean();
            Vec3 offset = d?tar.subtract(player.getEyePosition()).cross(new Vec3(0,1,0)):
                    player.getEyePosition().subtract(tar).cross(new Vec3(0,1,0));
            Vec3 c1 = tar.add(player.getEyePosition()).scale(0.5).add(offset.normalize().scale(f));
            proj.setTargetPos(tar);
            proj.setControlPosPos(c1);
            proj.setOwner(player);
            proj.setPos(player.getEyePosition());
            level.addFreshEntity(proj);


            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    int[] arr = {7,7,7,6,5,8};
                    int [] arr2 = {7,6,8,7,8,7};
                    int[] res = new Gomoku().compute(arr, arr2, 1000);
                    System.out.println(Arrays.toString(res));
                    player.sendSystemMessage(Component.literal(Arrays.toString(res)));
                }
            },10);


        }
        return super.use(level, player, usedHand);
    }
}
