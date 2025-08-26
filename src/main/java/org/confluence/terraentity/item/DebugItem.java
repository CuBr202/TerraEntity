package org.confluence.terraentity.item;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.confluence.terraentity.init.entity.TEBossEntities;

public class DebugItem extends Item {


    public DebugItem(Properties properties) {
        super(properties);
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        if (!level.isClientSide) {
            var entity = TEBossEntities.QUEEN_BEE.get().spawn((ServerLevel) level, player.blockPosition(), MobSpawnType.MOB_SUMMONED);


        }
        return super.use(level, player, usedHand);
    }
}
