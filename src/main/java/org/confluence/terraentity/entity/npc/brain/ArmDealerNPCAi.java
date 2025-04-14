package org.confluence.terraentity.entity.npc.brain;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.confluence.terraentity.entity.npc.AbstractTerraNPC;
import org.confluence.terraentity.entity.npc.brain.behavior.NPCRangeAttackBrain;

import java.util.List;

/**
 * 军火商专家的AI，使用弩
 */
public class ArmDealerNPCAi extends NPCAi {

    public ArmDealerNPCAi(AbstractTerraNPC npc) {
        super(npc);
    }

    protected NPCRangeAttackBrain<? super AbstractTerraNPC> createRangeAttackBrain() {
        return new NPCRangeAttackBrain<>(10, npc.getAttackRange()){
            protected boolean customDoAttack(ServerLevel level, Mob owner, LivingEntity target){
                ItemStack stack = owner.getMainHandItem();

                if(stack.getItem() instanceof CrossbowItem weaponItem){
                    weaponItem.shoot( level, owner, InteractionHand.MAIN_HAND, stack, List.of(Items.ARROW.getDefaultInstance()), 2.0f, 1f, owner.getRandom().nextFloat() < 0.3f, target);
                    return true;
                }
                return false;
            }
        };
    }
}
