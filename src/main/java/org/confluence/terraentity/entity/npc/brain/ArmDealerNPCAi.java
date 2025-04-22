package org.confluence.terraentity.entity.npc.brain;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.confluence.terraentity.entity.npc.AbstractTerraNPC;
import org.confluence.terraentity.entity.npc.brain.behavior.NPCRangeAttackBrain;

/**
 * 军火商专家的AI，使用弩
 */
public class ArmDealerNPCAi extends NPCAi {

    public ArmDealerNPCAi(AbstractTerraNPC npc) {
        super(npc);

    }

    @Override
    protected void init(){
        npc.setAttackRange(10);
        npc.setCooldownTicks(20);
    }

    @Override
    protected NPCRangeAttackBrain<? super AbstractTerraNPC> getRangeAttackBrain() {
        return new NPCRangeAttackBrain<>(10, npc.getAttackRange()){
            protected boolean customDoAttack(ServerLevel level, AbstractTerraNPC owner, LivingEntity target){
                ItemStack stack = owner.getMainHandItem();

                if(stack.getItem() instanceof CrossbowItem){
                    Projectile projectile = getArrow(level, owner, stack, Items.ARROW.getDefaultInstance());
                    owner.shootCrossbowProjectile(target, stack, projectile, 0f);
                    level.addFreshEntity(projectile);
                    stack.hurtAndBreak(1, owner, (self) -> {
                        self.broadcastBreakEvent(owner.getUsedItemHand());
                    });
                    return true;
                }
                return false;
            }
        };
    }


}
