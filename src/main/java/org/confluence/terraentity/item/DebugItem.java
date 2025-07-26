package org.confluence.terraentity.item;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.confluence.terraentity.entity.proj.DemonScytheProj;
import org.confluence.terraentity.init.entity.TEProjectileEntities;
import org.confluence.terraentity.utils.TEUtils;

public class DebugItem extends Item {


    public DebugItem(Properties properties) {
        super(properties);
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        if (!level.isClientSide) {


            Component c = Component.literal("").append(Items.BOWL.getDefaultInstance().getDisplayName());
//            Component b = MutableComponent.create(ItemStackContents.create(TerraEntity.space("test")))
                    ;
//            player.sendSystemMessage(b);

//                    .append(" x").append(String.valueOf(1))

            EntityHitResult hit = TEUtils.getEyeTraceHitResult(player, player.getAttributeValue(Attributes.ENTITY_INTERACTION_RANGE));
//            if(hit!= null){
//                hit.getEntity().discard();
//                return InteractionResultHolder.success(player.getItemInHand(usedHand));
//            }

//            Projectile proj = TEProjectileEntities.TRAIL_SWORD_PROJECTILE.get().create(level);
//            proj.setOwner(player);
//            proj.setPos(player.getX(), player.getY(), player.getZ());
//            level.addFreshEntity(proj);




            DemonScytheProj proj = TEProjectileEntities.DEMON_SCYTHE_PROJ.get().create(level);
            if (proj != null) {
                proj.setPos(player.getEyePosition());
                proj.setOwner(player);
                Vec3 forward = player.getLookAngle().scale(10);

//                for(int i=0;i<100;i++){
//                    for(int j=0;j<100;j++){
//                        level.setBlock(player.blockPosition().offset((int) (i + forward.x), (int) forward.y, (int) (j + forward.z)), Blocks.GLASS.defaultBlockState(), 3);
//                    }
//                }


                proj.shoot(forward.x, forward.y, forward.z, 0.5f, 2f);
//                proj.setDamage((float) this.getAttributeValue(Attributes.ATTACK_DAMAGE));
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

        }
        return super.use(level, player, usedHand);
    }
}
