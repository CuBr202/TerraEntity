package org.confluence.terraentity.item;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import org.confluence.terraentity.client.buffer.DebugBlocksHelper;
import org.confluence.terraentity.entity.npc.HouseDetectInfo;

import java.util.List;

public class HouseDetectItem extends Item {

    public HouseDetectItem(Properties properties) {
        super(properties);
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        if(level.isClientSide){
            final BlockHitResult result = getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY);
            final BlockHitResult raytraceResult = result.withPosition(result.getBlockPos().relative(result.getDirection()));
            final BlockPos pos = raytraceResult.getBlockPos();

            HouseDetectInfo info = HouseDetectInfo.detect(pos, level);
            player.sendSystemMessage(Component.translatable(info.type().translationKey));
            if(info.isError()){
                return super.use(level, player, usedHand);
            }
            final BlockPos min = info.min();
            final BlockPos max = info.max();
            final List<BlockPos> list = info.list();

            DebugBlocksHelper.Singleton().addDebugBlock(List.of(min, max));

        }
        return super.use(level, player, usedHand);


    }

}