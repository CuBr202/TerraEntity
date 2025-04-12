package org.confluence.terraentity.init;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.init.block.TEFigureBlocks;

import static org.confluence.terraentity.TerraEntity.MODID;

public class TEBlocks {
    public static final DeferredRegister<Item> BLOCKITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, TerraEntity.MODID);
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);


    public static void register(IEventBus bus) {
        TEFigureBlocks.register(bus);

        TEBlocks.BLOCKITEMS.register(bus);
        TEBlocks.BLOCKS.register(bus);
        TEBlocks.BLOCK_ENTITIES.register(bus);
    }
}
