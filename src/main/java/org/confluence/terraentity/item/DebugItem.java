package org.confluence.terraentity.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import org.confluence.terraentity.init.TEEntities;
import org.confluence.terraentity.init.entity.TEProjectileEntities;
import org.confluence.terraentity.utils.TEUtils;

public class DebugItem extends Item {


    public DebugItem(Properties properties) {
        super(properties);
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        if(!level.isClientSide){


            EntityHitResult hit =  TEUtils.getEyeTraceHitResult(player, player.getAttributeValue(Attributes.ENTITY_INTERACTION_RANGE));
//            if(hit!= null){
//                hit.getEntity().discard();
//                return InteractionResultHolder.success(player.getItemInHand(usedHand));
//            }

            Projectile proj = TEProjectileEntities.TRAIL_SWORD_PROJECTILE.get().create(level);
            proj.setOwner(player);
            proj.setPos(player.getX(), player.getY(), player.getZ());
            level.addFreshEntity(proj);


//            ThrowableProj proj = TEEntities.CABBAGE_PROJ.get().create(level);
//            Vec3 tar = player.getEyePosition().add(player.getForward().normalize().scale(10));
//            float f = player.getRandom().nextFloat() * 5 + 5;
//            boolean d = player.getRandom().nextBoolean();
//            Vec3 offset = d?tar.subtract(player.getEyePosition()).cross(new Vec3(0,1,0)):
//                    player.getEyePosition().subtract(tar).cross(new Vec3(0,1,0));
//            Vec3 c1 = tar.add(player.getEyePosition()).scale(0.5).add(offset.normalize().scale(f));
//            proj.setTargetPos(tar);
//            proj.setControlPosPos(c1);
//            proj.setOwner(player);
//            proj.setPos(player.getEyePosition());
//            level.addFreshEntity(proj);

        }
        return super.use(level, player, usedHand);
    }
}
