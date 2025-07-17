package org.confluence.terraentity.registries.chester;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.registries.TERegistries;

import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.function.Supplier;

public class ChesterConditionalTypes {
    public static DeferredRegister<ChesterConditionalType> TYPES = DeferredRegister.create(TERegistries.ChesterConditionalTypesProviders.KEY, TerraEntity.MODID);
    public static final Supplier<IForgeRegistry<ChesterConditionalType>> REGISTRY = TYPES.makeRegistry(RegistryBuilder::new);

    public static final Supplier<ChesterConditionalType> ENDER_CHEST = TYPES.register("routine_container", ()->new ChesterConditionalType(
            5,
            (pos, player, level)-> {
                return level.getBlockEntity(pos) instanceof BaseContainerBlockEntity;
            },
            (pos, player, level)->{
                BlockState state = level.getBlockState(pos);
                return state.getMenuProvider(level, pos);
            }));

//    public static final Supplier<ChesterConditionalType> SHULKER_CHEST = TYPES.register("shulker_chest", ()->new ChesterConditionalType(
//            6,
//            (pos, player, level)-> player.level().getBlockEntity(pos) instanceof ShulkerBoxBlockEntity,
//            (pos, player1, level)-> new SimpleMenuProvider((id, inv, player)-> new ShulkerBoxMenu(id, inv, (Container) player.level().getBlockEntity(pos)), Component.literal("Remote Shulker Chest"))));


    @Nullable
    public static ChesterConditionalType match(BlockPos pos, Player player, Level level){
        return REGISTRY.get().getValues().stream()
                .filter(provider -> provider.canOpen(pos, player, level))
                .min(Comparator.comparingInt(e->e.priority))
                .orElse(null);
    }
}
