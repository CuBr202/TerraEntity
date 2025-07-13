package org.confluence.terraentity.entity.summon;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.confluence.terraentity.attachment.SummonerAttachment;
import org.confluence.terraentity.init.TEAttachments;
import org.confluence.terraentity.item.SummonItem;
import org.confluence.terraentity.mixed.IPlayer;
import org.confluence.terraentity.registries.TERegistries;
import org.confluence.terraentity.registries.chester.ChesterConditionalType;
import software.bernie.geckolib.animation.AnimatableManager;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Chester extends AbstractSummonMob<Chester> {

    ChesterItemHandler itemHandler = new ChesterItemHandler(27);

    public Chester(EntityType<? extends Chester> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        if(player.getMainHandItem().getItem() instanceof SummonItem<?>){
            return InteractionResult.PASS;
        }
        if(!level().isClientSide()){
            var data = player.getData(TEAttachments.SUMMONER_STORAGE);
            var globalEntry = TERegistries.ChesterTypesProviders.REGISTRY.entrySet();
            int globalSize = globalEntry.size();
            if(data.chestType == globalSize - 1 && data.chestTypeAdditional != 0){
                int additionalSize = data.chestTypeAdditional;
                int maxAdditionalSize = data.boundBlocks.size();
                if(additionalSize <= maxAdditionalSize){
                    // 打开箱子
                    Map.Entry<SummonerAttachment.Key, ChesterConditionalType> entry = data.boundBlocks.entrySet().stream().toList().get(data.chestTypeAdditional - 1);
                    BlockPos pos = entry.getKey().pos();
                    ChesterConditionalType type = entry.getValue();
                    ((IPlayer) player).terra_entity$setInfiniteInteractBlock(true);
                    Level level = Objects.requireNonNull(level().getServer()).getLevel(entry.getKey().levelId());
                    if(type.tryOpen(pos, player, level)){
//                        player.sendSystemMessage(Component.literal("打开此箱子 :" + pos.toString()));
                        return InteractionResult.SUCCESS;
                    }
                    player.sendSystemMessage(Component.literal("无法打开此箱子 :" + pos.toString()));
                }
            }else{
                // 打开全局存储器
                player.openMenu(globalEntry.stream().toList()
                        .get(data.chestType).getValue()
                        .getMenuProviderSupplier().get());
            }


        }
        return InteractionResult.SUCCESS;
    }
    @Override
    public boolean isPickable() {
        return true;
    }
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

    }

    public ItemStackHandler getInventory() {
        return itemHandler;
    }

    public static class ChesterItemHandler extends ItemStackHandler{
        public ChesterItemHandler () {
            super();
        }

        public ChesterItemHandler (int size) {
            super(size);
        }

        public ChesterItemHandler (NonNullList<ItemStack> stacks) {
            super(stacks);
        }

    }

    public void summon(Player player, ItemStack stack) {
        super.summon(player, stack);
        // 只能同时存在一个
        SummonerAttachment data = player.getData(TEAttachments.SUMMONER_STORAGE);
        List<Integer> list = data.getIds();
        for (Integer integer : list) {
            Entity e = level().getEntity(integer);
            if (e instanceof Chester) {
                e.discard();
            }
        }
    }
}
