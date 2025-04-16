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

    @Override
    protected void init(){
        npc.setAttackRange(10);
        npc.setCooldownTicks(20);
    }

    protected NPCRangeAttackBrain<? super AbstractTerraNPC> getRangeAttackBrain() {
        return new NPCRangeAttackBrain<>(10, npc.getAttackRange()){
            protected boolean customDoAttack(ServerLevel level, AbstractTerraNPC owner, LivingEntity target){
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
